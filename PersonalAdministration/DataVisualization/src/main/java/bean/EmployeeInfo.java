package bean;

/**
 * 存放职员年龄和入职时长信息
 */
public class EmployeeInfo {

   private String name;//姓名
   private String age;//年龄
   private String entrydurationtime;//入职时长



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEntrydurationtime() {
        return entrydurationtime;
    }

    public void setEntrydurationtime(String entrydurationtime) {
        this.entrydurationtime = entrydurationtime;
    }
}
