package dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import utils.ConnectionInstance;
import utils.HBaseUtil;
import utils.PropertiesUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对传输进来数据进行处理转换，再将处理后数据存入HBase表中
 */
public class HBaseDAO {

    private int regions;//分区号
    private String namespase;//名称空间
    private String tableName;//表名
    public  static final Configuration conf;//HBase的配置信息
    private HTable table;//表
    private Connection connection;
    //HBaseConsumptionDataFromKafka每次调用HBaseDAO的put方法，便先将put对象放入List集合内，当集合内数据put对象达到一定量，便提交put对象做批量处理
    private List<Put> cacheList = new ArrayList<>();

    //在静态代码块中初始化HBaseConfiguration（conf），默认读取resources中的HBase的集群配置（读取顺序有一定的优先级）
    static
    {
        conf = HBaseConfiguration.create();
    }

    /**
     * 1、无参构造器 初始化Hbase的regions、namespase、tablename
     * 2、初始化HBase的表
     * hbase.employeeinformation.regions=6
     * hbase.employeeinformation.namespase=data
     * hbase.employeeinformation.tablename=data:employeeinformation
     */
     public HBaseDAO()
     {
         try {
             //初始化HBase的配置参数--分区数、名称空间、表名
             regions = Integer.valueOf(PropertiesUtil.getProperty("hbase.employeeinformation.regions"));
             namespase = PropertiesUtil.getProperty("hbase.employeeinformation.namespase");
             tableName = PropertiesUtil.getProperty("hbase.employeeinformation.tablename");

             /**
              * 1、判断HBase中是否已存在对应表
              * 2、如果不存在则初始化名称空间并创建该表
              */
             if (!HBaseUtil.isExistTable(conf, tableName)) {
                 HBaseUtil.initNamespace(conf, namespase);
                 HBaseUtil.createTable(conf, tableName, regions, "f1");
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

    /**
     * 1、将每次从Kafka的topic中取出的数据存放到HBase指定表结构中
     * 2、传入的数据格式：路玲玲,012313,信息工程学院,普通员工,信息工程学院教师,2,硕士,,412727199008044049,18339956040,河南省周口市淮阳县冯塘乡,2017-07,2018-03-03,1990-08-04
     * 3、需要生成的rowkey实例：路玲玲_信息工程学院_信息工程学院教师_2_硕士_18339956040_河南省周口市淮阳县冯塘乡_2017-07_2018-03-03_1990-08-04
     *                       姓名、所属学院、职位、性别（1为男，2为女）、最高学历、手机号、住址、毕业时间、入职时间、出生日期
     * 4、HBASE表的列： name  department  position sex education telephone address graduatetime entrytime birthdate
     *
     * @param ori
     */
     public void put(String ori)
     {
         try {
             //如果cacheList为空，则初始化connection和table
             if (cacheList.size() == 0) {
                 //获取HBase的链接类Connection
                 connection = ConnectionInstance.getConnection(conf);
                 //获取HBase表的操作类HTable
                 table = (HTable) connection.getTable(TableName.valueOf(tableName));
                 //关闭HBase的自动提交任务功能
                 table.setAutoFlushTo(false);
                 //设置缓存的字节数上限（2MB）
                 table.setWriteBufferSize(2 * 1024 * 1024);
             }

             String[] splitOri = ori.split(",");
             String name = splitOri[0];//姓名
             String jobnumber = splitOri[1];//工号
             String department = splitOri[2];//学院
             String type = splitOri[3];//员工类型
             String position = splitOri[4];//职位（教师）
             String sex = splitOri[5];//性别
             String education = splitOri[6];//最高学历
             String jobtitle = splitOri[7];//职称
             String identitycard = splitOri[8];//身份证号
             String telephone = splitOri[9];//手机号
             String address = splitOri[10];//住址
             String graduatetime = splitOri[11];//毕业时间
             String entrytime = splitOri[12];//入职时间
             String birthdate = splitOri[13];//出生日期
             String regionCode = HBaseUtil.genRegionCode(telephone,entrytime, regions);//将数据存放的分区号

             System.out.println("待生成的rowkey数据有"+ name + "|" + department + "|" + position + "|" + sex + "|"
                     + education + "|"+ telephone + "|" + address + "|" + graduatetime + "|" + entrytime + "|" + birthdate);
             /**
              * 1、根据传入的参数生成rowkey
              * 2、RowKey数据格式：分区号_路玲玲_信息工程学院_信息工程学院教师_2_硕士_18339956040_河南省周口市淮阳县冯塘乡_2017-07_2018-03-03_1990-08-04
              * 3、regionCode_name_department_position_sex_education_telephone_address_graduatetime_entrytime_birthdate
              */
             String rowkey = HBaseUtil.genRowKey(regionCode,name,department,position,sex, education,telephone,address,graduatetime,entrytime,birthdate);

             System.out.println("最终生成的rowkey为" + rowkey);

             //初始化操作指定rowkey的Put对象
             Put put = new Put(Bytes.toBytes(rowkey));
             //将切分生成的数据封装到put对象内
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes(name));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("department"), Bytes.toBytes(department));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("position"), Bytes.toBytes(position));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("sex"), Bytes.toBytes(sex));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("education"), Bytes.toBytes(education));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("telephone"), Bytes.toBytes(telephone));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("address"), Bytes.toBytes(address));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("graduatetime"), Bytes.toBytes(graduatetime));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("entrytime"), Bytes.toBytes(entrytime));
             put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("birthdate"), Bytes.toBytes(birthdate));

             //将put对象缓存到cacheList
             cacheList.add(put);

             //当数据量达到30，便将数据提交，清空缓存集合以及关闭table占用的资源
             if (cacheList.size() >= 10) {
                 table.put(cacheList);
                 table.flushCommits();
                 table.close();
                 cacheList.clear();
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}
