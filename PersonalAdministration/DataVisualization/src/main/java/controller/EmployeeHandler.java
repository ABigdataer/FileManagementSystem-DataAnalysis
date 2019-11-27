package controller;

import bean.*;
import dao.EmployeeDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
public class EmployeeHandler {

    @RequestMapping("/queryEmployeeList")
    public String queryEmployee(Model model)
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        EmployeeDao employeeDao = applicationContext.getBean(EmployeeDao.class);

        List<EmployeeInfo> list = employeeDao.getEmployee();

        StringBuilder nameSB = new StringBuilder();
        StringBuilder ageSB = new StringBuilder();
        StringBuilder entrydurationtimeSB = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {

            EmployeeInfo employeeInfo = list.get(i);

            nameSB.append(employeeInfo.getName() + ",");

            ageSB.append(employeeInfo.getAge() + ",");

            String s = employeeInfo.getEntrydurationtime().split("-")[0];
            int i1 = Integer.parseInt(s);
            String ss = employeeInfo.getEntrydurationtime().split("-")[1];
            if(Integer.parseInt(ss) >= 6) i1 += 1;
            entrydurationtimeSB.append( i1 + ",");
        }

        //删除多余的最后一个 ","
        nameSB.deleteCharAt(nameSB.length() - 1);
        ageSB.deleteCharAt(ageSB.length() - 1);
        entrydurationtimeSB.deleteCharAt(entrydurationtimeSB.length() - 1);

        //通过model返回数据
        model.addAttribute("name", nameSB.toString());
        model.addAttribute("age", ageSB.toString());
        model.addAttribute("entrydurationtime", entrydurationtimeSB.toString());

        return "jsp/EmployeeInformationVisualization";
    }

    @RequestMapping("/queryEducationList")
    public String queryEducation(Model model)
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        EmployeeDao employeeDao = applicationContext.getBean(EmployeeDao.class);

        List<EducationInfo> list = employeeDao.getEducation();
        String headcount = null;
        String juniorhighschoolsum = null;
        String seniorsighschoolsum = null;
        String technicalsecondaryschoolsum = null;
        String juniorcollegesum = null;
        String bachelordegreesum = null;
        String mastersum = null ;
        String doctorsum = null ;
        String postdoctorsum = null;

        for (int i = 0; i < list.size(); i++) {

            EducationInfo educationInfo = list.get(i);
            headcount = educationInfo.getHeadcount();
            juniorhighschoolsum = educationInfo.getJuniorhighschoolsum();
            seniorsighschoolsum = educationInfo.getSeniorsighschoolsum();
            technicalsecondaryschoolsum = educationInfo.getTechnicalsecondaryschoolsum();
            juniorcollegesum = educationInfo.getJuniorcollegesum();
            bachelordegreesum = educationInfo.getBachelordegreesum();
            mastersum = educationInfo.getMastersum();
            doctorsum = educationInfo.getDoctorsum();
            postdoctorsum = educationInfo.getPostdoctorsum();
        }

        model.addAttribute("headcount", headcount);
        model.addAttribute("juniorhighschoolsum", juniorhighschoolsum);
        model.addAttribute("seniorsighschoolsum", seniorsighschoolsum);
        model.addAttribute("technicalsecondaryschoolsum", technicalsecondaryschoolsum);
        model.addAttribute("juniorcollegesum", juniorcollegesum);
        model.addAttribute("bachelordegreesum", bachelordegreesum);
        model.addAttribute("mastersum", mastersum);
        model.addAttribute("doctorsum", doctorsum);
        model.addAttribute("postdoctorsum", postdoctorsum);

        return "jsp/EmployeeEducationVisualization";
    }

    @RequestMapping("/querySexList")
    public String querySex(Model model) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        EmployeeDao employeeDao = applicationContext.getBean(EmployeeDao.class);

        List<SexInfo> list = employeeDao.getSex();
        int man = 0;
        int woman = 0;

        for (int i = 0; i < list.size(); i++) {
            SexInfo sexInfo = list.get(i);
            int sex = Integer.parseInt(sexInfo.getSex());
            if (sex == 1) man += 1;
            else woman += 1;
        }
        String mans = String.valueOf(man);
        String womans = String.valueOf(woman);

        model.addAttribute("man",mans);
        model.addAttribute("woman", womans);

        return "/jsp/EmployeeSex";
    }

    @RequestMapping("/queryAgeList")
    public String queryAge(Model model) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        EmployeeDao employeeDao = applicationContext.getBean(EmployeeDao.class);

        List<AgeInfo> list = employeeDao.getAge();
        int age_20_30 = 0;
        int age_30_40 = 0;
        int age_40_50 = 0;
        int age_50_60 = 0;
        int age_60_70 = 0;
        int age_n = 0;

        for (int i = 0; i < list.size(); i++) {
            AgeInfo ageInfo = list.get(i);
            int age = Integer.parseInt(ageInfo.getAge());
            if(30 > age ) age_20_30+=1;
            if(40 > age && age >= 30) age_30_40+=1;
            if(50 > age && age >= 40) age_40_50+=1;
            if(60 > age && age >= 50) age_50_60+=1;
            if(70 > age && age >= 60) age_60_70+=1;
            if (age >= 70 ) age_n +=1 ;
        }

        model.addAttribute("age_20_30",age_20_30);
        model.addAttribute("age_30_40", age_30_40);
        model.addAttribute("age_40_50", age_40_50);
        model.addAttribute("age_50_60", age_50_60);
        model.addAttribute("age_60_70", age_60_70);
        model.addAttribute("age_n", age_n);

        return "/jsp/EmployeeAge";
    }

}
