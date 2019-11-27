package reducer;

import kvbean.key.BirthdayMapperKey;
import kvbean.key.BirthdayReducerKey;
import kvbean.value.BasicData;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BirthdayDataAnalysisReducer extends Reducer<BirthdayMapperKey, BasicData, BirthdayReducerKey, NullWritable> {

    BirthdayReducerKey birthdayReducerKey = new BirthdayReducerKey();

    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //获取当前时间的毫秒数
    long nowTime = System.currentTimeMillis();

    public BirthdayDataAnalysisReducer() {
        super();
    }

    @Override
    protected void reduce(BirthdayMapperKey key, Iterable<BasicData> values, Context context) throws IOException, InterruptedException {

        for (BasicData value : values)
        {
            try {
                String name = value.getName();
                String graduatetime = value.getGraduatetime();
                String entrytime = value.getEntrytime();
                String birthday = value.getBirthdate();

                Date entrytimedate = sdf1.parse(entrytime);
                Date birthdaydate = sdf1.parse(birthday);

                long entrylong = entrytimedate.getTime();
                long birthdaylong = birthdaydate.getTime();

                long theentrytime = nowTime - entrylong;
                long thebirthdaytime = nowTime - birthdaylong;

                long endateTemp1=theentrytime/1000; //秒
                long endateTemp2=endateTemp1/60; //分钟
                long endateTemp3=endateTemp2/60; //小时
                long endateTemp4=endateTemp3/24; //天数
                long endateTemp5=endateTemp4/30; //月数
                long endateTemp6=endateTemp5/12; //年数

                long bidateTemp1=thebirthdaytime/1000; //秒
                long bidateTemp2=bidateTemp1/60; //分钟
                long bidateTemp3=bidateTemp2/60; //小时
                long bidateTemp4=bidateTemp3/24; //天数
                long bidateTemp5=bidateTemp4/30; //月数
                long bidateTemp6=bidateTemp5/12; //年数
                long month = bidateTemp5 % 12;//几个月
                long day = (bidateTemp4 % 365)%30;//几天

                String entrydurationtime = endateTemp6 + "-" + month + "-" + day;//入职时长
                int age =  (int)( bidateTemp6 + 1 ) ;//年龄

                //创建随机ID
                birthdayReducerKey.setId(RandomUtils.nextInt());
                birthdayReducerKey.setName(name);
                birthdayReducerKey.setAge(age);
                birthdayReducerKey.setEntrydurationtime(entrydurationtime);

                context.write(birthdayReducerKey,null);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
}
