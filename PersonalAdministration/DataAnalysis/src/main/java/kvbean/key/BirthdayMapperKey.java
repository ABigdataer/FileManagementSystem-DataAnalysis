package kvbean.key;

import kvbean.base.BasicKeyDemo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class BirthdayMapperKey extends BasicKeyDemo {

    private String name;

    public BirthdayMapperKey() {
    }

    public BirthdayMapperKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(BasicKeyDemo o) {
        BirthdayMapperKey birthdayMapperKey = (BirthdayMapperKey) o ;
        int result = this.name.compareTo(((BirthdayMapperKey) o).name);
        return result;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
      dataOutput.writeUTF(name);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.name = dataInput.readUTF();
    }
}
