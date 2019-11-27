package kvbean.base;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

/**
 * 作为封装到数据库内数据model的父类
 */
public abstract class BaseDBDemo implements Writable , DBWritable {

}
