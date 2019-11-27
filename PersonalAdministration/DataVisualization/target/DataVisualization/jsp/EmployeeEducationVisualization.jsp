<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/3/3
  Time: 11:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>职工各学历分析饼图</title>
    <script type="text/javascript" src="../js/echarts.js"></script>
    <script type="text/javascript" src="../js/ecStat.js"></script>
</head>

<body style="height: 100%; width: 100%;margin: 0;">

<h3 align="center" style="color: #2b2b2b;font-size: 25px" > 职工各学历人数分析示意图</h3>

<div id="container1" style="height: 90%; width:100%; margin: 20px" ></div>

<script>

    var juniorhighschoolsum = ${requestScope.juniorhighschoolsum};
    var seniorsighschoolsum = ${requestScope.seniorsighschoolsum};
    var technicalsecondaryschoolsum = ${requestScope.technicalsecondaryschoolsum};
    var juniorcollegesum = ${requestScope.juniorcollegesum};
    var bachelordegreesum = ${requestScope.bachelordegreesum};
    var mastersum = ${requestScope.mastersum} ;
    var doctorsum = ${requestScope.doctorsum} ;
    var postdoctorsum = ${requestScope.postdoctorsum};

    var array1 = [juniorhighschoolsum,seniorsighschoolsum,technicalsecondaryschoolsum,juniorcollegesum,bachelordegreesum,mastersum,doctorsum,postdoctorsum]
    var array2 = [ '初中人数' , '高中人数' , '中专人数' , '专科人数' , '本科人数' , '硕士人数' , '博士人数' , '博士后人数' ];

    var echartData = converterFun(array1 , array2)

    Employee1();

    function converterFun(array11 , array22) {
        var array = [];
        for (var i = 0; i < array11.length ; i++) {
            var map = {};
            map['value'] = array11[i];
            map['name'] = array22[i];
            array.push(map);
        }
        return array;
    }

    function Employee1() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('container1'));
        myChart.showLoading();//显示加载框

        var option = {
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: array2
            },
            series : [
                {
                    name: '当前学历占比',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:echartData
                    ,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'}
                        }
                    }

            ]
        };
        //给myChart设置option
        myChart.setOption(option);
        myChart.hideLoading();
    }

</script>


</body>
</html>
