package utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;

public class JDBCUtil {

    //引入操作日志类
    private static final Logger logger = LoggerFactory.getLogger("JDBCUtil.class");

    //通过标识名获取数据库连接池
    static ComboPooledDataSource datasource = new ComboPooledDataSource("mysql");

    public static Connection getConnection()
    {
        try
        {
            return datasource.getConnection();
        }
        catch (SQLException e)
        {
            logger.error("JDBCUtil获取Connection报错");
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection connection , Statement statement , ResultSet resultSet)
    {
        try
        {
            if (resultSet != null && !resultSet.isClosed())
            {
                resultSet.close();
            }
            if (statement != null && !statement.isClosed())
            {
                statement.close();
            }
            if (connection != null && !connection.isClosed())
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
