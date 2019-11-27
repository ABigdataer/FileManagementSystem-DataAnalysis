package runner;

import kvbean.key.BirthdayMapperKey;
import kvbean.key.BirthdayReducerKey;
import kvbean.key.EducationalBackgroundStatistics;
import kvbean.value.BasicData;
import mapper.BirthdayDataAnalysisMapper;
import mapper.EducationDataAnalysisMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import reducer.BirthdayDataAnalysisReducer;
import reducer.EducationDataAnalysisReducer;

import java.io.IOException;

/**
 * 1、调度Mapper和Reducer的Runner类
 * 2、继承Tool，实现setConf、getConf和run方法
 */

public class DataAnalysisRunner  implements Tool{

    public static Configuration conf = null;

    public static String driverClass = "com.mysql.jdbc.Driver";
    public static String dbUrl = "jdbc:mysql://mini:3306/db_employee?useUnicode=true&characterEncoding=UTF-8";
    public static String userName = "root";
    public static String passwd = "123456";
    public static String [] fields1 = {"id","headcount","juniorhighschoolsum" , "seniorsighschoolsum" , "technicalsecondaryschoolsum" , "juniorcollegesum" , "bachelordegreesum" , "mastersum" , "doctorsum" , "postdoctorsum"};
    public static String[] fields2 = {"id", "name", "age", "entrydurationtime"};
    public static Path path = new Path("hdfs://mini:9000/lib/mysql/mysql-connector-java-5.1.39-bin.jar");
    public static int result2 ;

    /**
     * 该方法用来准备MR程序会用到的HBaseConf
     *
     * @param conf
     */
    @Override
    public void setConf(Configuration conf) {
        /**
         * 1、通过HBaseConfiguration.create(conf)读取链接HBAse的配置
         * 2、默认优先读取default配置，再读取hadoop-core.xml配置，最后
         * 读取hbase-site.xml配置,后读取的配置属性会覆盖前面优先级较高的
         * 配置属性
         */
        this.conf = HBaseConfiguration.create(conf);
    }

    /**
     * MR默认调用该方法获取配置信息
     *
     * @return
     */
    @Override
    public Configuration getConf() {
        return this.conf;
    }

    @Override
    public int run(String[] strings) throws Exception {

            String tableName1 = "tb_educationalstatistics";
            DBConfiguration.configureDB(conf,driverClass,dbUrl,userName,passwd);

            //实例化操作HBase的Job类
            Job job1 = Job.getInstance(conf,"DataAnalysisRunner1");
            // 设置reduce task的运行实例数
            job1.setNumReduceTasks(1); // 默认是1
            job1.setJarByClass(DataAnalysisRunner.class);

            //组装Mapper InpouFormat
            initHBaseInputConfig(job1);
            //组装Reducer OutputFormat
            initReducerOutputConfig(job1);

            // 添加mysql数据库jar
            //DistributedCache.addFileToClassPath(path , conf);
            //将数据输出到数据库
            DBOutputFormat.setOutput(job1,tableName1,fields1);

            int result = job1.waitForCompletion(true) ? 0 : 1;

           if (result == 0) {

               //DBConfiguration.configureDB(conf, driverClass, dbUrl, userName, passwd);
               String tableName2 = "tb_age_entrydurationtime";

               //实例化操作HBase的Job类
               Job job2 = Job.getInstance(conf, "DataAnalysisRunner2");
               // 设置reduce task的运行实例数
               job2.setNumReduceTasks(1); // 默认是1
               job2.setJarByClass(DataAnalysisRunner.class);
               //组装Mapper InpouFormat
               initHBaseInputConfig2(job2);
               //组装Reducer OutputFormat
               initReducerOutputConfig2(job2);
               // 添加mysql数据库jar
               //DistributedCache.addFileToClassPath(path, conf);
               DBOutputFormat.setOutput(job2, tableName2, fields2);

               //提交运行作业job2 并打印信息
               result2 = job2.waitForCompletion(true) ? 0 : 1;
           }
        return result2;
    }

    /**
     * 用来运行Runner的主类，是Runner的入口
     * @param args
     */
    public static void main(String[] args) {

        try {
            int status = ToolRunner.run(new DataAnalysisRunner(), args);
            System.exit(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1、组装Mapper InpouFormat
     * 2、用TableMapReduceUtil.initTableReducerJob将数据写到关系型数据库中
     * 3、
     *   ①将数据写入表的第一种方式是用TableMapReduceUtil.initTableReducerJob的方法，这里既可以在map阶段输出，也能在reduce阶段输出。
     *   ②写入表的方式还有一种，就是调用hbase的原生api，即HTable.put的方式写入数据（这种方式适合写少量数据，或者统计后的结果）
     * @param job1
     */
    private static void initHBaseInputConfig(Job job1) {

        //HBase的链接类
        Connection connection  = null;
        //HBase的表管理类
        Admin admin = null;
        //目标表
        String tableName = "data:employeeinformation";
        try {
            connection  = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
            /**
             * 校验表是否存在，如果不存在则直接返回，若存在则运行Mapper类
             */
            if (!admin.tableExists(TableName.valueOf(tableName))) throw new RuntimeException("*************无法找到对应表！************");
            //创建表扫描器，作为TableMapReduceUtil.initTableMapperJob的参数，用来扫描HBase中的表结构
            Scan scan = new Scan();
            /**
             * 1、初始化TableMapperJOb;
             * 2、指定表扫描器，取出HBase中tableName中的数据作为Mapper的输入
             * 3、指定MapperClass和其输出key\value类型
             */
            TableMapReduceUtil.initTableMapperJob(tableName, scan, EducationDataAnalysisMapper.class, Text.class, IntWritable.class, job1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Reducer类，以及输出键值类型
     * @param job1
     */
    private static void initReducerOutputConfig(Job job1) {
        job1.setReducerClass(EducationDataAnalysisReducer.class);
        job1.setOutputKeyClass(EducationalBackgroundStatistics.class);
        job1.setOutputValueClass(NullWritable.class);
        job1.setOutputFormatClass(DBOutputFormat.class);
    }

    /**
     * 1、组装Mapper InpouFormat
     * 2、用TableMapReduceUtil.initTableReducerJob将数据写到关系型数据库中
     * 3、
     *   ①将数据写入表的第一种方式是用TableMapReduceUtil.initTableReducerJob的方法，这里既可以在map阶段输出，也能在reduce阶段输出。
     *   ②写入表的方式还有一种，就是调用hbase的原生api，即HTable.put的方式写入数据（这种方式适合写少量数据，或者统计后的结果）
     * @param job2
     */
    private static void initHBaseInputConfig2(Job job2) {
        //HBase的链接类
        Connection connection  = null;
        //HBase的表管理类
        Admin admin = null;
        //目标表
        String tableName = "data:employeeinformation";
        try {
            connection  = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
            /**
             * 校验表是否存在，如果不存在则直接返回，若存在则运行Mapper类
             */
            if (!admin.tableExists(TableName.valueOf(tableName))) throw new RuntimeException("*************无法找到对应表！************");
            //创建表扫描器，作为TableMapReduceUtil.initTableMapperJob的参数，用来扫描HBase中的表结构
            Scan scan = new Scan();
            /**
             * 1、初始化TableMapperJOb;
             * 2、指定表扫描器，取出HBase中tableName中的数据作为Mapper的输入
             * 3、指定MapperClass和其输出key\value类型
             */
            TableMapReduceUtil.initTableMapperJob(tableName, scan, BirthdayDataAnalysisMapper.class, BirthdayMapperKey.class, BasicData.class, job2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /**
             * 关闭资源，避免内存溢出
             */
            try {
                if (admin != null)
                    admin.close();
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置Reducer类，以及输出键值类型
     * @param job2
     */
    private static void initReducerOutputConfig2(Job job2) {
        job2.setReducerClass(BirthdayDataAnalysisReducer.class);
        job2.setOutputKeyClass(BirthdayReducerKey.class);
        job2.setOutputValueClass(NullWritable.class);
        job2.setOutputFormatClass(DBOutputFormat.class);
    }

}
