package dao;

import bean.AgeInfo;
import bean.SexInfo;
import bean.EducationInfo;
import bean.EmployeeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<EmployeeInfo> getEmployee()
    {
        //查询每个用户的姓名、年龄和入职时长
        String sql ="select `name`,`age`,`entrydurationtime` from  tb_age_entrydurationtime;";
        BeanPropertyRowMapper<EmployeeInfo> beanPropertyRowMapper = new BeanPropertyRowMapper<>(EmployeeInfo.class);
        List<EmployeeInfo> list = namedParameterJdbcTemplate.query(sql, beanPropertyRowMapper);
        return list;
    }

    //查询各学历人数
    public List<EducationInfo> getEducation()
    {
        String sql = "SELECT `headcount` , `juniorhighschoolsum` , `seniorsighschoolsum` , `technicalsecondaryschoolsum` , `juniorcollegesum` ,  `bachelordegreesum`, `mastersum` , `doctorsum` , `postdoctorsum` from tb_educationalstatistics;";
        BeanPropertyRowMapper<EducationInfo> beanPropertyRowMapper = new BeanPropertyRowMapper<>(EducationInfo.class);
        List<EducationInfo> list = namedParameterJdbcTemplate.query(sql, beanPropertyRowMapper);
        return list;
    }

    //查询性别
    public List<SexInfo> getSex()
    {
        String sql = "SELECT sex from customer ;";
        BeanPropertyRowMapper<SexInfo> beanPropertyRowMapper = new BeanPropertyRowMapper<>(SexInfo.class);
        List<SexInfo> list = namedParameterJdbcTemplate.query(sql, beanPropertyRowMapper);
        return list;
    }

    //查询年龄
    public List<AgeInfo> getAge()
    {
        String sql = "SELECT age from tb_age_entrydurationtime;";
        BeanPropertyRowMapper<AgeInfo> beanPropertyRowMapper = new BeanPropertyRowMapper<>(AgeInfo.class);
        List<AgeInfo> list = namedParameterJdbcTemplate.query(sql, beanPropertyRowMapper);
        return list;
    }

}
