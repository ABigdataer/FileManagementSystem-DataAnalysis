package utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 获取JDBC数据库连接类
 */
public class JDBCInstance {
    private static Connection connection = null;
    private JDBCInstance(){}
    public static Connection getInstance()
    {
        try
        {
            /**
             * isClosed是判断一个connection是否被关闭，而是否被关闭是停留在java程序层的判断，不会去检测是否与数据库连通。
             * 意思是，你在程序运行过程中，即使把数据库关了，对于一个之前未被关闭的connection来说，调用connection.isClosed依然为false。
             * 但是如果用已经不能使用的connection访问数据库导致一次异常之后，connection则会自动设置为true。
             * 而isValid是检测connection是否有效，它会尝试与数据库作连接，即如果Connection没有关闭并且有效，返回true
             */
            if(connection == null || connection.isClosed() || connection.isValid(3))
            {
                connection = JDBCUtil.getConnection();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return  connection;
    }
}
