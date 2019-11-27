package pretreatment;

import org.apache.commons.lang3.StringUtils;
import utils.JDBCInstance;
import utils.JDBCUtil;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 取出数据库中数据，并对其进行整理的操作
 */
public class PreOut {

    public PreOut() {}

    private static String filepath ;//数据输出的路径
    private static Connection conn = null;
    private static String SQL = null;//SQL语句
    private static PreparedStatement statement = null;//预查询SQL语句
    private static ResultSet resultSet = null;//结果集
    private static String name = null;//姓名
    private static String jobNumber = null;//工号
    private static String department = null;//部门
    private static String type = null; //员工类型
    private static String position;//职位
    private static String sex = null;//性别
    private static String education = null;//最高学历
    private static String jobTitle = null;//最高职称
    private static String identityCard = null;//身份证号
    private static String telephone = null;//手机号
    private static String address = null;//住址
    private static String graduateTime = null;//毕业时间
    private static String startTime = null;//入职时间
    private static String birthday;//出生日static

    public static void pre(String filepath) throws SQLException {
        //初始化JDBC连接器对象
        conn = JDBCInstance.getInstance();
        if (SQL == null)
        {
            SQL = "select a.name,a.jobNumber,b.name,a.type,a.position,a.sex,c.item_value,a.jobTitle,a.identityCard,a.telephone,a.address,a.graduateTime,a.startTime,a.birthday from customer a inner join college b on a.department = b.cid inner join base_dictionary c on a.education = c.bid;";
        }
        statement = conn.prepareStatement(SQL);
        resultSet = statement.executeQuery();
        while (true) {
            if (resultSet.next()) {

                name = resultSet.getString(1);

                //去除数据库中测试用的测试数据（管理员账户）
                if(name.equals("1") || name.equals("小火") || name.equals("超级管理员")) continue;

                jobNumber = resultSet.getString(2);
                department = resultSet.getString(3);
                type = resultSet.getString(4);
                position = resultSet.getString(5);
                sex = resultSet.getString(6);
                education = resultSet.getString(7);
                jobTitle = resultSet.getString(8);
                if(StringUtils.isBlank(jobTitle)) jobTitle = null;
                identityCard = resultSet.getString(9);
                if(StringUtils.isBlank(identityCard)) identityCard = null;
                telephone = resultSet.getString(10);
                address = resultSet.getString(11).trim();
                if(StringUtils.isBlank(address)) address = null;
                graduateTime = resultSet.getString(12);
                if(StringUtils.isBlank(graduateTime)) graduateTime = null;
                startTime = resultSet.getString(13);
                birthday = resultSet.getString(14);

                //拼接并将数据打印到控制台
                StringBuilder sb = new StringBuilder();
                sb.append(name + ",").append(jobNumber + ",").append(department + ",").append(type + ",").append(position + ",").append(sex + ",").append(education + ",").append(jobTitle + ",").append(identityCard + ",").append(telephone + ",").append(address + ",").append(graduateTime + ",").append(startTime + ",").append(birthday).append("\n");
                String DataSplicing = sb.toString();
                System.out.println(DataSplicing);

                try {
                    RandomAccessFile rs = new RandomAccessFile(filepath, "rw");
                    //定位到最后一行
                    rs.seek(rs.length());
                    //将数据写入文件
                    rs.write(DataSplicing.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                JDBCUtil.close(conn, statement, resultSet);
                break;
            }
        }
    }
    public static void main (String[] args){
        try {
            //"D:\\项目实战\\人事档案管理系统职员信息数据分析系统（Hadoop）\\sourcedata.txt"
            PreOut.pre("D:\\项目实战\\人事档案管理系统职员信息数据分析系统（Hadoop）\\sourcedata.txt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
