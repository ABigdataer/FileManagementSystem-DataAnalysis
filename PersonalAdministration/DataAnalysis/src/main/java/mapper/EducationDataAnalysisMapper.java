package mapper;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;

import java.io.IOException;
/**
 * 1、取出HBase中数据进行基本处理的TableMapper类
 * 2、继承TableMapper，自定义输出键值类型：
 * ①参数简介
 * TableMapper继承自Mapper类。但是Mapper类有四个输入泛型，为何这里的TableMapper只有两个呢？
 * 通过源码，可以看到TableMapper的KEYIN，VALUEIN分别设置为ImmutabelBytesWriteable和Result类型，
 * ImmutableBytesWritable存放的数据为表的rowkey，
 * 所以只需要实现KEYOUT，VALUEOUT即可。
 * ②这里的TableMapper类完全是为了从HBase中读取数据而设置的，也就是说，这个TableMapper是专为HBase定义的抽象类；
 */
public class EducationDataAnalysisMapper extends TableMapper<Text, IntWritable> {

    private Logger logger = Logger.getLogger(EducationDataAnalysisMapper.class);

    public Text keyText ;
    public IntWritable intValue = new IntWritable(1);

    public EducationDataAnalysisMapper() {super();}

    /**
     * 1、根据Rowkey切分出需要的数据，进行封装后传输给reducer
     * 2、传进来的每条Rowkey数据将拆分出六条数据传递给reducer进行处理,包含职位、最高学位、住址、毕业时间、入职时间、出生日期
     *
     * @param key     RowKey
     * @param value   Columns
     * @param context
     */
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
        String education = splits[5];//最高学历

        if (StringUtils.equals(education,"初中"))
        {
            keyText = new Text(education) ;
            context.write(keyText,intValue);
        }
        else if (StringUtils.equals(education,"高中"))
        {
            keyText = new Text(education);
            context.write(keyText,intValue);
        }
        else if (StringUtils.equals(education,"中专"))
        {
            keyText = new Text(education);
            context.write(keyText,intValue);
        }
        else if (StringUtils.equals(education,"大学专科"))
        {
            keyText = new Text(education);
            context.write(keyText,intValue);
        }
        else if (StringUtils.equals(education,"大学本科"))
        {
            keyText = new Text(education);
            context.write(keyText,intValue);
        }
        else if (StringUtils.equals(education,"硕士"))
        {
            keyText = new Text(education);
            context.write(keyText,intValue);
        }
        else if (StringUtils.equals(education,"博士"))
        {
            keyText = new Text(education);
            context.write(keyText,intValue);
        }
        else if (StringUtils.equals(education,"博士后"))
        {
            keyText = new Text(education);
            context.write(keyText,intValue);
        }

    }
}
