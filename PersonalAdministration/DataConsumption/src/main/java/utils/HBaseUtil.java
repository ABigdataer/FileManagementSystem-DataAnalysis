package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

public class HBaseUtil {

    /**
     * 判断表是否存在
     *
     * @param conf      HBaseConfiguration
     * @param tableName
     * @return
     */
    public static boolean isExistTable(Configuration conf, String tableName) throws IOException {

        //通过配置属性建立与HDFS的链接
        Connection connection = ConnectionFactory.createConnection(conf);
        //获取管理者
        Admin admin = connection.getAdmin();
        //判断表是否存在
        boolean result = admin.tableExists(TableName.valueOf(tableName));
        //关闭资源
        connection.close();
        admin.close();
        //返回结果
        return result;

    }

    /**
     * 初始化名称空间
     *
     * @param conf
     * @param namespace
     */
    public static void initNamespace(Configuration conf, String namespace) throws IOException {
        //通过配置属性建立与HDFS的链接
        Connection connection = ConnectionFactory.createConnection(conf);
        //获取文件管理者
        Admin admin = connection.getAdmin();
        //命名空间描述器
        NamespaceDescriptor nd = NamespaceDescriptor
                .create(namespace)
                .addConfiguration("create_time", String.valueOf(System.currentTimeMillis()))
                .addConfiguration("creator", "Liu")
                .build();

        admin.createNamespace(nd);
        admin.close();
        connection.close();
    }


    /**
     * 创建表
     */
    public static void createTable(Configuration conf, String tablename, int regions, String... columnFamily) throws IOException {
        //通过配置属性建立与HDFS的链接
        Connection connection = ConnectionFactory.createConnection(conf);
        //获取文件管理者
        Admin admin = connection.getAdmin();
        //如果表已存在则直接返回
        if (isExistTable(conf, tablename)) return;
        //创建一个表的描述类
        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tablename));
        //使用for循环将列属性赋值给表的描述类
        for (String cf : columnFamily) {
            htd.addFamily(new HColumnDescriptor(cf));
        }
        //创建表
        admin.createTable(htd, gensplitKeys(regions));
        connection.close();
        admin.close();
    }

    /**
     * 生成分区键
     *
     * @return
     */
    private static byte[][] gensplitKeys(int regions) {
        //定义大小为regions,存放分区键的数组
        String[] keys = new String[regions];
        //region个数不超过两位数，所以region分区键格式化为两位数所代表的字符串
        DecimalFormat df = new DecimalFormat("00");

        for (int i = 0; i < regions; i++) {
            //keys[1] = 1|  keys[2] = 2|  keys[3] = 3|  ...    keys[6] = 6|
            keys[i] = df.format(i) + "|";
        }

        byte[][] splitKeys = new byte[regions][];

        //生成byte[][]类型的分区键的时候，要保证分区键是有序的
        //TreeSet内部数据按照Key自动排序
        TreeSet<byte[]> treeSet = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for (int i = 0; i < regions; i++) {
            treeSet.add(Bytes.toBytes(keys[i]));
        }

        Iterator<byte[]> splitKeysIterator = treeSet.iterator();

        int index = 0;

        while (splitKeysIterator.hasNext()) {
            byte[] b = splitKeysIterator.next();
            //splitKeys[6][1] = 1|  splitKeys[6][2] = 2|  ...  splitKeys[6][6] = 6|
            splitKeys[index++] = b;
        }

        for (byte[] a : splitKeys) {
            System.out.println(Arrays.toString(a));
        }
        return splitKeys;
    }

    /**
     * 生成rowkey
     * regionCode_name_department_position_sex_education_telephone_address_graduatetime_entrytime_birthdate
     *
     * @return
     */
    public static String genRowKey(String regionCode,String name,String department,String position,String sex,String education,String telephone,String address,String graduatetime,String entrytime,String birthdate) {
        StringBuilder sb = new StringBuilder();
        sb.append(regionCode + "_")
                .append(name + "_")
                .append(department + "_")
                .append(position + "_")
                .append(sex + "_")
                .append(education+ "_")
                .append(telephone+ "_")
                .append(address+ "_")
                .append(graduatetime+ "_")
                .append(entrytime+ "_")
                .append(birthdate );
        return sb.toString();
    }

    /**
     * 方法作用：获取分区号
     * 根据传入的手机号和入职时间以及分区数生成将数据放入的指定分区号
     *
     * @param telephone
     * @param entrytime
     * @param regions
     * @return
     */
    public static String genRegionCode(String telephone, String entrytime, int regions) {
        //实际数据库中数据含有一三条测试数据没有手机号，需要对其进行赋予一个手机号，避免报下标越界错误
        if(telephone.length() == 1 || telephone == "01203010312" || telephone == "1111111111111")
        {
            telephone = "17344991523";
        }
        int len = telephone.length();
        //取出后4位号码
        String lastPhone = telephone.substring(len - 4);
        //取出年月
        String ym = entrytime
                .replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll(" ", "")
                .substring(0, 6);
        //离散操作1
        Integer x = Integer.valueOf(lastPhone) ^ Integer.valueOf(ym);
        int a = 10;
        int b = 20;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        //离散操作2
        int y = x.hashCode();
        //生成分区号
        int regionCode = y % regions;
        //格式化分区号
        DecimalFormat df = new DecimalFormat("00");
        return df.format(regionCode);
    }

}
