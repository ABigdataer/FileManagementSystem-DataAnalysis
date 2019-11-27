package reducer;

import kvbean.key.EducationalBackgroundStatistics;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import utils.JDBCInstance;
import utils.JDBCUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EducationDataAnalysisReducer extends Reducer<Text, IntWritable, EducationalBackgroundStatistics, NullWritable> {

    public EducationDataAnalysisReducer() {super();}

    public EducationalBackgroundStatistics ebs = new EducationalBackgroundStatistics();

    //初始化JDBC连接器对象
    public Connection connection = null;
    public String SearchSQL = null;
    public PreparedStatement preparedStatement = null;

    Map<String,Integer> map=new HashMap<String, Integer>();

    public int headcount;
    public int juniorhighschoolsum = 0;
    public int seniorsighschoolsum = 0;
    public int technicalsecondaryschoolsum = 0;
    public int juniorcollegesum = 0;
    public int bachelordegreesum = 0;
    public int mastersum = 0;
    public int doctorsum = 0;
    public int postdoctorsum = 0;


    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //创建随机ID
        ebs.setId(RandomUtils.nextInt());
        //此模块查询教师总人数
        try {
            if (connection == null) connection = JDBCInstance.getInstance();
            if (SearchSQL == null) SearchSQL = "SELECT  COUNT(NAME) FROM `customer`;";
            if (preparedStatement == null) preparedStatement = connection.prepareStatement(SearchSQL);
            ResultSet resultSet = preparedStatement.executeQuery(SearchSQL);
            if (resultSet.next())
            {
                headcount = resultSet.getInt(1) - 3 ;
                ebs.setHeadcount(headcount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close(connection, preparedStatement, null);
        }
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        int count = 0;
        for (IntWritable value : values)
        {
            count += value.get();
        }

        String keyvalue = key.toString();
        map.put(keyvalue, count);

    }

    @Override
    protected void cleanup(Reducer<Text , IntWritable,EducationalBackgroundStatistics, NullWritable>.Context context) throws IOException, InterruptedException {

        //foreach功能，遍历Map（JAVA8特性------Lambda表达式）
        map.forEach(
            (k,v)->{
                  if("初中".equals(k)){
                      juniorhighschoolsum+=v;
                    }
                  else if ("高中".equals(k)){
                      seniorsighschoolsum+=v;
                  }
                  else if ("中专".equals(k)){
                      technicalsecondaryschoolsum+=v;
                  }
                  else if ("大学专科".equals(k)){
                      juniorcollegesum+=v;
                  }
                  else if ("大学本科".equals(k)){
                      bachelordegreesum+=v;
                  }
                  else if ("硕士".equals(k)){
                      mastersum+=v;
                  }
                  else if ("博士".equals(k)){
                      doctorsum+=v;
                  }
                  else if ("博士后".equals(k)){
                      postdoctorsum+=v;
                  }
             }
        );

        ebs.setJuniorhighschoolsum(juniorhighschoolsum);
        ebs.setSeniorsighschoolsum(seniorsighschoolsum);
        ebs.setTechnicalsecondaryschoolsum(technicalsecondaryschoolsum);
        ebs.setJuniorcollegesum(juniorcollegesum);
        ebs.setBachelordegreesum(bachelordegreesum);
        ebs.setMastersum(mastersum);
        ebs.setDoctorsum(doctorsum);
        ebs.setPostdoctorsum(postdoctorsum);

        context.write(ebs,null);
    }
}

