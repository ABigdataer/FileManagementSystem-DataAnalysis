<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>职员信息统计分析结果查询</title>
</head>
<body >

<div align="center" style="height: 100%; width: 100%;margin: 0;" >

    <form action="/queryEmployeeList"  method="post" style="font-size: 30px; height: 50%">
        <input  type="submit" value="查询职工年龄和入职时长散点图">
    </form>

    <form action="/queryEducationList" method="post">
        <input type="submit" value="查询职工学历分布饼图">
    </form>

    <form action="/querySexList" method="post">
        <input type="submit" value="查询性别比例饼图">
    </form>

    <form action="/queryAgeList" method="post">
        <input type="submit" value="查询年龄比例饼图">
    </form>

</div>


</body>
</html>
