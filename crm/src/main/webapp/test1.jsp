<%--
  Created by IntelliJ IDEA.
  User: Yjt
  Date: 2021/8/9
  Time: 12:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <title>model</title>
    </head>
    <body>
        $.ajax({
        url:"",
        data:{},
        dataType:"json",
        success:function (data) {

        }
        })

        String id = UUIDUtil.getUUID();

        String createTime = DateTimeUtil.getSysTime();

        boolean flag = false;
        if (count > 0){
        flag = true;
        }
        PrintJson.printJsonFlag(response,flag);

        $(".time").datetimepicker({
        minView: "month",
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left"
        });

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();

        if (count1 != 1){
        flag = false;
        }
    </body>
</html>
