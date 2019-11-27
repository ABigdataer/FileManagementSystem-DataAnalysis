package bean;

/**
 * 该类用于存放用户请求的数据
 */
public class QueryInfo {

    private String name;

    public QueryInfo() {
    }

    public QueryInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
