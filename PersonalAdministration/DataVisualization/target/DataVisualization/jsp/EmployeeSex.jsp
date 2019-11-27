<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/3/25
  Time: 13:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>性别比例图</title>
    <script type="text/javascript" src="../js/echarts.js"></script>
    <script type="text/javascript" src="../js/ecStat.js"></script>
</head>

<body style="height: 100%; width: 100%;margin: 0;">

<div id="container1" style="height: 90%; width:100%; margin: 20px" ></div>

<script type="text/javascript">

    var man = "${requestScope.man}"
    var woman = "${requestScope.woman}"

    var array = [];

    var map1 = {};
    map1['value'] = man;
    map1['name'] = '男员工';
    array.push(map1);

    var map2 = {}
    map2['value'] = woman;
    map2['name'] = '女员工';
    array.push(map2);

    Employee();

    function Employee() {
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
                data: ['男员工','女员工']
            },
            series : [
                {
                    name: '职工性别占比',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:array
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
        myChart.hideLoading()
    }

</script>

</body>
</html>
