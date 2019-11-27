package kvbean.value;


import kvbean.base.BaseDBDemo;
import kvbean.base.BaseDemo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 1、封装了用户基本信息维度的Bean类:姓名、学院、职位、最高学位、住址、毕业时间、入职时间、出生日期
 * 2、实现序列化方法
 */
public class BasicData extends BaseDemo {

    /**
     * 自定义的key必须有无参构造器，否则Mapper阶段无法创建该类对象
     */
    public  BasicData(){super();}

    private String name;//姓名
    private String department;//部门学院
    private String position;//职位
    private String education ; //学历（初中、高中、中专、大学专科、大学本科、硕士、博士、博士后）
    private String address;//住址
    private String graduatetime;//毕业时间
    private String entrytime;//就职时间
    private String birthdate;//出生时间

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGraduatetime() {
        return graduatetime;
    }

    public void setGraduatetime(String graduatetime) {
        this.graduatetime = graduatetime;
    }

    public String getEntrytime() {
        return entrytime;
    }

    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(name);
        dataOutput.writeUTF(department);
        dataOutput.writeUTF(position);
        dataOutput.writeUTF(education);
        dataOutput.writeUTF(address);
        dataOutput.writeUTF(graduatetime);
        dataOutput.writeUTF(entrytime);
        dataOutput.writeUTF(birthdate);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.name = dataInput.readUTF();
        this.department = dataInput.readUTF();
        this.position = dataInput.readUTF();
        this.education = dataInput.readUTF();
        this.address = dataInput.readUTF();
        this.graduatetime = dataInput.readUTF();
        this.entrytime = dataInput.readUTF();
        this.birthdate = dataInput.readUTF();
    }
}
