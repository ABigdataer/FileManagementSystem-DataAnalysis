package kvbean.key;

import kvbean.base.BaseDBDemo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BirthdayReducerKey extends BaseDBDemo {

    private int id;
    private String name;//姓名
    private int age;//年龄
    private String entrydurationtime;//入职时长

    public BirthdayReducerKey() {
    }

    public BirthdayReducerKey(int id, String name, int age, String entrydurationtime) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.entrydurationtime = entrydurationtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEntrydurationtime() {
        return entrydurationtime;
    }

    public void setEntrydurationtime(String entrydurationtime) {
        this.entrydurationtime = entrydurationtime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(id);
        dataOutput.writeUTF(name);
        dataOutput.writeInt(age);
        dataOutput.writeUTF(entrydurationtime);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.id = dataInput.readInt();
        this.name = dataInput.readUTF();
        this.age = dataInput.readInt();
        this.entrydurationtime = dataInput.readUTF();
    }

    @Override
    public void write(PreparedStatement statement) throws SQLException {
         statement.setInt(1,id);
         statement.setString(2,this.name);
         statement.setInt(3,this.age);
         statement.setString(4,this.entrydurationtime);
    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt(1);
        this.name = resultSet.getString(2);
        this.age = resultSet.getInt(3);
        this.entrydurationtime = resultSet.getString(4);
    }
}
