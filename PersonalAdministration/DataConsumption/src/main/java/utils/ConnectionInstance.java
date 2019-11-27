package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 *1、HBase获取Connection的类，对传入的参数conn做判断，如果为空则创建新的，如果不为空，则直接返回
 *2、synchronized方法，避免多个线程同时创建连接
 */
public class ConnectionInstance {

    private static Connection conn;

    public static synchronized Connection getConnection(Configuration conf) {
        try {
            if (conn == null || conn.isClosed()) {
                conn = ConnectionFactory.createConnection(conf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
