package kafka;

import dao.HBaseDAO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.util.Arrays;
import java.util.Properties;

/**
 * 1、数据生成到指定位置后，使用Flume实时采集数据到Kafka集群，本类为采用Kafka的JavaAPI对订阅的Topic中数据进行消费
 * 2、将Kafka上的数据取出后调用HBaseDAO进行基本数据处理后放入HBase中进行数据存储
 */
public class HBaseConsumptionDataFromKafka {
    public static void main(String[] args) {
        /**
          初始化Kafka数据消费者KafkaConf
         */
        Properties props = new Properties();
        // 设置kafka的brokerlist
        props.put("bootstrap.servers","mini:9092,mini1:9092,mini2:9092");
        // 设置消费者所属的消费组
        props.put("group.id", "hbase_kafka_consumer_group");
        // 是否自动确认offset
        props.put("enable.auto.commit", "true");
        // 自动确认offset的时间间隔
        props.put("auto.commit.interval.ms", "1000");
        // key的序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // value的序列化类
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);

        /**
         *  指定消费者订阅的topic, 可同时订阅多个
         *  kafkaConsumer.subscribe(Arrays.asList("first", "second","third"));
         */
        kafkaConsumer.subscribe(Arrays.asList("employeeinformation"));

        HBaseDAO hd = new HBaseDAO();
        int i = 0;

        while (true)
        {
            //拉取数据，每100ms拉取一次数据，poll方法返回一个消息列表，每条记录都包含记录所属主题、所在分区、偏移量、记录的键值对
            ConsumerRecords<String,String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String,String> cr : records)
            {
                //取出职员基本信息数据
                String oriValue = cr.value();

                //打印出取出的每条数据
                System.out.println("取出的第"+ ++i +"数据为" + oriValue );

                //调用HBaseDao的put方法将取出的数据使用HBase的API进行处理后存放到HBase中
                hd.put(oriValue);
            }
        }
    }
}
