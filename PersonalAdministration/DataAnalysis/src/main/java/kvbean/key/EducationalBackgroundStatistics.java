package kvbean.key;

import kvbean.base.BaseDBDemo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 封装各学历的人数，作为reduce的key
 */
public class EducationalBackgroundStatistics extends BaseDBDemo {

    public EducationalBackgroundStatistics() {super(); }

    private int id;
    private int headcount;//总人数
    private int juniorhighschoolsum;//初中人数
    private int seniorsighschoolsum;//高中人数
    private int technicalsecondaryschoolsum;//中专人数
    private int juniorcollegesum;//大专人数
    private int bachelordegreesum;//本科人数
    private int mastersum;//硕士人数
    private int doctorsum;//博士人数
    private int postdoctorsum;//博士后人数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeadcount() {
        return headcount;
    }

    public void setHeadcount(int headcount) {
        this.headcount = headcount;
    }

    public int getJuniorhighschoolsum() {
        return juniorhighschoolsum;
    }

    public void setJuniorhighschoolsum(int juniorhighschoolsum) {
        this.juniorhighschoolsum = juniorhighschoolsum;
    }

    public int getSeniorsighschoolsum() {
        return seniorsighschoolsum;
    }

    public void setSeniorsighschoolsum(int seniorsighschoolsum) {
        this.seniorsighschoolsum = seniorsighschoolsum;
    }

    public int getTechnicalsecondaryschoolsum() {
        return technicalsecondaryschoolsum;
    }

    public void setTechnicalsecondaryschoolsum(int technicalsecondaryschoolsum) {
        this.technicalsecondaryschoolsum = technicalsecondaryschoolsum;
    }

    public int getJuniorcollegesum() {
        return juniorcollegesum;
    }

    public void setJuniorcollegesum(int juniorcollegesum) {
        this.juniorcollegesum = juniorcollegesum;
    }

    public int getBachelordegreesum() {
        return bachelordegreesum;
    }

    public void setBachelordegreesum(int bachelordegreesum) {
        this.bachelordegreesum = bachelordegreesum;
    }

    public int getMastersum() {
        return mastersum;
    }

    public void setMastersum(int mastersum) {
        this.mastersum = mastersum;
    }

    public int getDoctorsum() {
        return doctorsum;
    }

    public void setDoctorsum(int doctorsum) {
        this.doctorsum = doctorsum;
    }

    public int getPostdoctorsum() {
        return postdoctorsum;
    }

    public void setPostdoctorsum(int postdoctorsum) {
        this.postdoctorsum = postdoctorsum;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
       dataOutput.writeInt(id);
       dataOutput.writeInt(headcount);
       dataOutput.writeInt(juniorhighschoolsum);
       dataOutput.writeInt(seniorsighschoolsum);
       dataOutput.writeInt(technicalsecondaryschoolsum);
       dataOutput.writeInt(juniorcollegesum);
       dataOutput.writeInt(bachelordegreesum);
       dataOutput.writeInt(mastersum);
       dataOutput.writeInt(doctorsum);
       dataOutput.writeInt(postdoctorsum);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.id = dataInput.readInt();
        this.headcount = dataInput.readInt();
        this.juniorhighschoolsum = dataInput.readInt();
        this.seniorsighschoolsum = dataInput.readInt();
        this.technicalsecondaryschoolsum = dataInput.readInt();
        this.juniorcollegesum = dataInput.readInt();
        this.bachelordegreesum = dataInput.readInt();
        this.mastersum = dataInput.readInt();
        this.doctorsum = dataInput.readInt();
        this.postdoctorsum = dataInput.readInt();
    }

    @Override
    public void write(PreparedStatement statement) throws SQLException {
        statement.setInt(1,this.id);
        statement.setInt(2,this.headcount);
        statement.setInt(3,this.juniorhighschoolsum);
        statement.setInt(4,this.seniorsighschoolsum);
        statement.setInt(5,this.technicalsecondaryschoolsum);
        statement.setInt(6,this.juniorcollegesum);
        statement.setInt(7,this.bachelordegreesum);
        statement.setInt(8,this.mastersum);
        statement.setInt(9,this.doctorsum);
        statement.setInt(10,this.postdoctorsum);
    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt(1);
        this.headcount = resultSet.getInt(2);
        this.juniorhighschoolsum = resultSet.getInt(3);
        this.seniorsighschoolsum = resultSet.getInt(4);
        this.technicalsecondaryschoolsum = resultSet.getInt(5);
        this.juniorcollegesum = resultSet.getInt(6);
        this.bachelordegreesum = resultSet.getInt(7);
        this.mastersum = resultSet.getInt(8);
        this.doctorsum = resultSet.getInt(9);
        this.postdoctorsum = resultSet.getInt(10);
    }
}
