package mapper;

import kvbean.key.BirthdayMapperKey;
import kvbean.value.BasicData;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;

public class BirthdayDataAnalysisMapper extends TableMapper<BirthdayMapperKey, BasicData> {

    BirthdayMapperKey birthdayMapperKey = new BirthdayMapperKey();
    BasicData basicData = new BasicData();

    public BirthdayDataAnalysisMapper() {
        super();
    }

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

        /**
         *  1、取出HBase中对应表的RowKey，并进行切分
         *  2、RowKey数据格式：分区号_路玲玲_信息工程学院_信息工程学院教师_2_硕士_18339956040_河南省周口市淮阳县冯塘乡_2017-07_2018-03-03_1990-08-04
         *  3、regionCode_name_department_position_sex_education_telephone_address_graduatetime_entrytime_birthdate
         *  4、使用HBase API时不要使用str.getBytes将String转化为byte[] ，而应该使用Bytes.toBytes(str)；同样使用Bytes.toString(bytes);完成逆向转换。
         */
        String rowkey = Bytes.toString(key.get());
        String[] splits = rowkey.split("_");

        String name = splits[1];//姓名
        String department = splits[2];//部门学院
        String position = splits[3];//职位
        String education = splits[5]; //学历（初中、高中、中专、大学专科、大学本科、硕士、博士、博士后）
        String address = splits[7];//住址
        String graduatetime = splits[8];//毕业时间
        String entrytime = splits[9];//就职时间
        String birthdate = splits[10];//出生时间

        birthdayMapperKey.setName(name);

        basicData.setName(name);
        basicData.setDepartment(department);
        basicData.setPosition(position);
        basicData.setEducation(education);
        basicData.setAddress(address);
        basicData.setGraduatetime(graduatetime);
        basicData.setEntrytime(entrytime);
        basicData.setBirthdate(birthdate);

        context.write(birthdayMapperKey,basicData);
    }
}
