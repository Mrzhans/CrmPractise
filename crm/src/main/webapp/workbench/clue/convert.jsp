<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
    <head>
		<base href="<%=basePath%>">
        <meta charset="UTF-8">

        <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
        <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
        <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


        <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
              rel="stylesheet"/>
        <script type="text/javascript"
                src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript"
                src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

        <script type="text/javascript">
            $(function () {
                $(".time").datetimepicker({
                    minView: "month",
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });
                $("#isCreateTransaction").click(function () {
                    if (this.checked) {
                        $("#create-transaction2").show(200);
                    } else {
                        $("#create-transaction2").hide(200);
                    }
                });

                $("#openSearch").click(function () {
                    $("#searchActivityModal").modal("show");
                })

                $("#activityname").keydown(function (event) {
                    if (event.keyCode == 13){
                         $("#activityBody").empty();
                         selectActivityByName( $("#activityname").val());
                         return false;
                    }
                })

                $("#createActivity").click(function () {
                     $("#activitySource").val($("#e" + $("input[name=xz]:checked").val()).html());
                     $("#activityid").val($("input[name=xz]:checked").val());
                     $("#searchActivityModal").modal("hide");
                })

                //转换按钮
                $("#tranBtn").click(function () {
                    //判断复选框当中有没有选中，以及一些状态信息都是用prop方法！！！！很重要一定要记住
                    if($("#isCreateTransaction").prop("checked")){
                        $("#tranform").submit();
                    }else {
                        window.location.href = 'workbench/clue/convert.do?clueid=${param.id}'
                    }
                })
            });
            function selectActivityByName(name) {
                $.ajax({
                    url:"workbench/activtiy/selectActivityByName.do",
                    data:{
                        name:name,
                    },
                    dataType:"json",
                    type:"get",
                    success:function (data) {
                        var html = "";
                        $.each(data,function (i,n) {
                            html+='<tr id="'+ n.id +'">';
                            html+='<td><input type="radio" name="xz" value="'+ n.id +'"/></td>';
                            html+='<td id="e'+ n.id +'">'+ n.name +'</td>';
                            html+='<td>'+ n.startDate +'</td>';
                            html+='<td>'+ n.endDate +'</td>';
                            html+='<td>'+ n.owner +'</td>';
                            html+='</tr>';
                        })
                        $("#activityBody").append(html);
                    }
                })
            }
        </script>
    </head>
    <body>

        <!-- 搜索市场活动的模态窗口 -->
        <div class="modal fade" id="searchActivityModal" role="dialog">
            <div class="modal-dialog" role="document" style="width: 90%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">×</span>
                        </button>
                        <h4 class="modal-title">搜索市场活动</h4>
                    </div>
                    <div class="modal-body">
                        <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                            <form class="form-inline" role="form">
                                <div class="form-group has-feedback">
                                    <input type="text" class="form-control" style="width: 300px;"
                                           placeholder="请输入市场活动名称，支持模糊查询" id="activityname">
                                    <span class="glyphicon glyphicon-search form-control-feedback"></span>
                                </div>
                            </form>
                        </div>
                        <table id="activityTable" class="table table-hover"
                               style="width: 900px; position: relative;top: 10px;">
                            <thead>
                            <tr style="color: #B3B3B3;">
                                <td></td>
                                <td>名称</td>
                                <td>开始日期</td>
                                <td>结束日期</td>
                                <td>所有者</td>
                                <td></td>
                            </tr>
                            </thead>
                            <tbody id="activityBody">
                            <%--<tr>
                                <td><input type="radio" name="activity"/></td>
                                <td>发传单</td>
                                <td>2020-10-10</td>
                                <td>2020-10-20</td>
                                <td>zhangsan</td>
                            </tr>
                            <tr>
                                <td><input type="radio" name="activity"/></td>
                                <td>发传单</td>
                                <td>2020-10-10</td>
                                <td>2020-10-20</td>
                                <td>zhangsan</td>
                            </tr>--%>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" id="createActivity">添加</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="title" class="page-header" style="position: relative; left: 20px;">
            <h4>转换线索 <small>李四先生-动力节点</small></h4>
        </div>
        <div id="create-customer" style="position: relative; left: 40px; height: 35px;">
            新建客户：${param.company}
        </div>
        <div id="create-contact" style="position: relative; left: 40px; height: 35px;">
            新建联系人:${param.fullname}
        </div>
        <div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
            <input type="checkbox" id="isCreateTransaction"/>
            为客户创建交易
        </div>
        <div id="create-transaction2"
             style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;">
            <%--
                此时我们需要获取客户创建的交易信息，将这些交易信息需要填到tran表单单中
                所以我们需要获取他们当作参数传入进去后台当中，这时候我们就想到，是局部刷新呢？还是全局刷新呢！？
                因为页面没什么可以局部刷新的数据，所以我们需要全局刷新，全局刷新有两种传参的方式，一种是地址栏后面直接加参数
                另一种是form表单加name属性进行传入参数，这里需要填数据所以使用form表单的方式进行传参。
            --%>
            <form id="tranform" type="post" action="workbench/clue/convert.do">
                <input type="hidden" value="${param.id}" name="clueid">
                <input type="hidden" value="true" name="flag">
                <div class="form-group" style="width: 400px; position: relative; left: 20px;">
                    <label for="amountOfMoney">金额</label>
                    <input type="text" class="form-control" id="amountOfMoney" name="money">
                </div>
                <div class="form-group" style="width: 400px;position: relative; left: 20px;">
                    <label for="tradeName">交易名称</label>
                    <input type="text" class="form-control" id="tradeName" name="name">
                </div>
                <div class="form-group" style="width: 400px;position: relative; left: 20px;">
                    <label for="expectedClosingDate">预计成交日期</label>
                    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
                </div>
                <div class="form-group" style="width: 400px;position: relative; left: 20px;">
                    <label for="stage">阶段</label>
                    <select id="stage" class="form-control" name="stage">
                        <option></option>
                        <c:forEach items="${stagelist}" var="s">
                            <option value="${s.value}">${s.text}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group" style="width: 400px;position: relative; left: 20px;">
                    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearch" style="text-decoration: none;"><span
                            class="glyphicon glyphicon-search"></span></a></label>
                    <input type="hidden" name="activityid" id="activityid">
                    <input type="text" class="form-control" id="activitySource" name="source" placeholder="点击上面搜索" readonly>
                </div>
            </form>

        </div>

        <div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
            记录的所有者：<br>
            <b>${param.owner}</b>
        </div>
        <div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
            <input class="btn btn-primary" type="button" value="转换" id="tranBtn">
            <input class="btn btn-default" type="button" value="取消">
        </div>
    </body>
</html>