package cn.ui;

import com.intellij.ui.JBColor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * @Description: sql format
 * @Author haodd
 * @Date 2024/6/9 20:35
 * @Version 1.0
 */
public class SqlUI {

    private static JFrame frame;

    private static RSyntaxTextArea textArea;

    public static void createAndShow(String data) {

        if (frame == null) {

            frame = new JFrame("sql show");
            frame.setLayout(new BorderLayout());

            textArea = new RSyntaxTextArea(20, 90);
            Font font = new Font("Monospaced", Font.PLAIN, 17);
            textArea.setFont(font);
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
            textArea.setText(data);
            textArea.setCodeFoldingEnabled(true);
            textArea.setLineWrap(true);
            textArea.setBorder(new MatteBorder(1, 1, 1, 1, JBColor.GRAY));

            RTextScrollPane  scrollPane = new RTextScrollPane(textArea);
            scrollPane.setFoldIndicatorEnabled(true);

            frame.add(scrollPane, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null);

        }

        textArea.setText(data);
        frame.setVisible(true);

    }

}
