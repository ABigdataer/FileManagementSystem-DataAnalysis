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
    <title>年龄比例图</title>
    <script type="text/javascript" src="../js/echarts.js"></script>
    <script type="text/javascript" src="../js/ecStat.js"></script>
</head>

<body style="height: 100%; width: 100%;margin: 0;">

<div id="container1" style="height: 90%; width:100%; margin: 20px" ></div>

<script type="text/javascript">

    var age_20_30 = "${requestScope.age_20_30}"
    var age_30_40 = "${requestScope.age_30_40}"
    var age_40_50 = "${requestScope.age_40_50}"
    var age_50_60 = "${requestScope.age_50_60}"
    var age_60_70 = "${requestScope.age_60_70}"
    var age_n = "${requestScope.age_n}"

    var array = [];

    var map1 = {value:age_20_30,name:'20-30岁'};
    array.push(map1);

    var map2 = {value:age_30_40,name:'30-40岁'}
    array.push(map2);

    var map3 = {value:age_40_50,name:'40-50岁'}
    array.push(map3);

    var map4 = {value:age_50_60,name:'50-60岁'}
    array.push(map4);

    var map5 = {value:age_60_70,name:'60-70岁'}
    array.push(map5);

    var map6 = {value:age_n,name:'大于70岁'}
    array.push(map6);

    Employee();

    function Employee() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('container1'));
        myChart.showLoading();//显示加载框

        var option = {
            title : {
                text: '职员年龄阶段南丁格尔图',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                x : 'center',
                y : 'bottom',
                data:['20-30岁','30-40岁','40-50岁','50-60岁','60-70岁','大于70岁']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {
                        show: true,
                        type: ['pie', 'funnel']
                    },
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            series : [
                {
                    name:'半径模式',
                    type:'pie',
                    radius : [20, 110],
                    center : ['25%', '50%'],
                    roseType : 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    lableLine: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    data:array
                },
                {
                    name:'面积模式',
                    type:'pie',
                    radius : [30, 110],
                    center : ['75%', '50%'],
                    roseType : 'area',
                    data:array
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
