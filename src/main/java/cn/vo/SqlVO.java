package cn.vo;

/**
 * @Description: sql show entity
 * @Author haodd
 * @Date 2024/6/9 15:12
 * @Version 1.0
 */
public class SqlVO {

    private String tip;

    private String sql;

    private String parameters;

    private String result;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SqlVO{" +
                "tip='" + tip + '\'' +
                ", sql='" + sql + '\'' +
                ", parameters='" + parameters + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
