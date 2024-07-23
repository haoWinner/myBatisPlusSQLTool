package cn;

import cn.ui.SqlUI;
import cn.vo.SqlVO;
import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: sql format
 * @Author haodd
 * @Date 2024/6/9 17:35
 * @Version 1.0
 */
public class SqlFormat extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor != null) {

            String selectedText = editor.getSelectionModel().getSelectedText();

            String rtnSql = "";

            if (StringUtils.isNotEmpty(selectedText) && StringUtils.isNotEmpty(rtnSql = formatSql(selectedText))) {

                SqlUI.createAndShow(rtnSql);

            }else {

                Messages.showErrorDialog("Please select data!","Tip");

            }

        }

    }

    public static String formatSql(String text){

        String[] lines = text.split("\n");
        StringBuilder sql = new StringBuilder();

        List<SqlVO> sqlParaStrMap = getSqlMaps(lines);

        if (sqlParaStrMap.isEmpty()) {
            return "";
        }

        for (SqlVO sqlVO : sqlParaStrMap) {

            sql.append("-- ").append(sqlVO.getTip()).append("\n");
            sql.append("-- ").append(sqlVO.getResult()).append("\n");

            String sqlStr = sqlVO.getSql();
            String paramMarkers = "!@@##abcdef";
            sqlStr = sqlStr.replace("?", paramMarkers);
            if (sqlVO.getParameters() != null && !sqlVO.getParameters().isEmpty()) {
                String[] paramValues = sqlVO.getParameters().split(", ");
                for (String paramValue : paramValues) {

                    if(paramValue.contains("\"")) {
                        paramValue = paramValue.replace("\"", "\\\\\"");
                    }

                    if (paramValue.contains("(")) {
                        String value = paramValue.substring(0, paramValue.lastIndexOf("("));
                        String type = paramValue.substring(paramValue.lastIndexOf("(") + 1, paramValue.lastIndexOf(")"));
                        sqlStr = sqlStr.replaceFirst(paramMarkers, shouldAddQuotes(type, value));
                    }else {
                        sqlStr = sqlStr.replaceFirst(paramMarkers, paramValue);
                    }

                }
            }

            sql.append(SqlFormatter.format(sqlStr));

            sql.append("\n");
            sql.append("\n");

        }

        return sql.toString();

    }

    @NotNull
    private static List<SqlVO> getSqlMaps(String[] lines) {
        List<SqlVO> sqlParaStrMap = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Preparing:")) {

                SqlVO sqlVO = new SqlVO();

                sqlVO.setTip(lines[i].substring(0, lines[i].indexOf("Preparing")));
                sqlVO.setSql(lines[i].replaceAll(".*Preparing:\\s*", ""));

                String parameters = "";
                if (i + 1 < lines.length) {
                    parameters = lines[i + 1].replaceAll(".*Parameters:\\s*", "");
                }

                sqlVO.setParameters(parameters);

                String rtn = "";
                if (i + 2 < lines.length && (lines[i + 2].contains("Total:") || lines[i + 2].contains("Updates:"))) {
                    rtn = lines[i + 2].substring(Math.max(lines[i + 2].indexOf("Total:"), lines[i + 2].indexOf("Updates:")));
                }

                sqlVO.setResult(rtn);
                sqlParaStrMap.add(sqlVO);
                i = i + 2;
            }
        }
        return sqlParaStrMap;
    }

    private static String shouldAddQuotes(String type, String value){

        String strTemplate = "\"" + value + "\"";

        return switch (type) {
            case "String", "Timestamp" -> strTemplate;
            default -> value;
        };

    }

}
