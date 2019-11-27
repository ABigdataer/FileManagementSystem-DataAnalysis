<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/3/2
  Time: 11:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>职工年龄和入职时长散点图</title>
    <script type="text/javascript" src="../js/echarts.js"></script>
    <script type="text/javascript" src="../js/ecStat.js"></script>
</head>

<body style="height: 100%; width: 100%;margin: 0;">

<h3 align="center" style="color: #2b2b2b;font-size: 25px" > 职工年龄和入职时长示意图</h3>

<div id="container1" style="height: 90%; width:100%; margin: 20px" ></div>

<script type="text/javascript">

        var age = "${requestScope.age}"
        var entrydurationtime = "${entrydurationtime}"
        var age1 = age.split(",")
        var entrydurationtime1 = entrydurationtime.split(",")
        var pieData = converterFun1(age1,entrydurationtime1)

        Employee1();

        function converterFun1(age1, entrydurationtime1) {
            var array = [];
            for (var i = 0; i < age1.length; i++) {
                 var key = age1[i];
                 var value = entrydurationtime1[i];
                 var array1 = [ key , value];
                 array.push(array1);
            }
            return array;
        }

    function Employee1() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('container1'));
        myChart.showLoading();//显示加载框

        var option = {
            xAxis: {name:'职工年龄',
                    nameTextStyle:
                    {
                        color:'#2b2b2b',
                        fontWeight:'400',
                        fontSize:'20'
                    }
                    },
            yAxis: {
                name:'职工入职时长',
                nameTextStyle:
                    {
                        color:'#2b2b2b',
                        fontWeight:'400',
                        fontSize:'20'
                    }
            },
            series: [{
                symbolSize: 10,
                data: pieData,
                type: 'scatter'
            }]
        };
        //给myChart设置option
        myChart.setOption(option);
        myChart.hideLoading()
    }

</script>


</body>
</html>
