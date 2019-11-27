package kvbean.base;

import org.apache.hadoop.io.WritableComparable;

/**
 * 1、Mapper的key的实现序列化的封装类
 * 2、数据在MR的Shuffle阶段需要对key进行排序，故实现WritableComparable接口
 */
public abstract class BasicKeyDemo implements WritableComparable<BasicKeyDemo> {

}
