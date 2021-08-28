<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta charset="UTF-8">

        <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
        <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
              rel="stylesheet"/>

        <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
        <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
        <script type="text/javascript"
                src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript"
                src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
        <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

        <script type="text/javascript">
            $(function () {
                $("#create-customerName").typeahead({
                    source: function (query, process) {
                        $.post(
                            "workbench/transaction/getCustomerName.do",
                            { "name" : query },
                            function (data) {
                                //alert(data);
                                process(data);
                            },
                            "json"
                        );
                    },
                    delay: 1500
                });

                $(".time1").datetimepicker({
                    minView: "month",
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });

                $(".time2").datetimepicker({
                    minView: "month",
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "top-left"
                });

                //对市场搜索活动绑定一个键盘事件
                $("#activityName").keydown(function (event) {
                    if (event.keyCode == 13){
                        $("#actBody").empty();
                        selectActivityByName($("#activityName").val());
                        return false;
                    }
                })

                //对联系人搜索活动绑定一个键盘事件
                $("#ContactName").keydown(function (event) {
                    if (event.keyCode == 13){
                        $("#ContanctsBody").empty();
                        selectContactByName($("#ContactName").val());
                        return false;
                    }
                })

                //打开搜索市场活动的模态窗口
                $("#openSearchAct").click(function () {
                    $("#findMarketActivity").modal("show");
                })

                //打开搜索联系人的模态窗口
                $("#openSearchCon").click(function () {
                    $("#findContacts").modal("show");
                })

                //点击添加市场活动
                $("#createActivity").click(function () {
                    $("#myactvtiyid").val($("input[name=xz]:checked").val());
                    $("#create-activitySrc").val($("#e" + $("input[name=xz]:checked").val()).html());
                    $("#findMarketActivity").modal("hide");
                })

                //点击添加联系人
                $("#createContacts").click(function () {
                    $("#mycontactid").val($("input[name=xzc]:checked").val());
                    $("#create-contactsName").val($("#e" + $("input[name=xzc]:checked").val()).html());
                    $("#findContacts").modal("hide");
                })

                $("#create-Stage1").change(function () {
                    var json = ${applicationScope.json};
                    var key = $("#create-Stage1").val();
                    $("#create-possibility").val(json[key]);
                })

                $("#submitBtn").click(function () {
                    //提交表单
                    $("#create-form").submit();
                })
                selectUser();
            })

            function selectContactByName(name) {
                $.ajax({
                    url:"workbench/contact/selectContactByName.do",
                    data:{
                        name:name,
                    },
                    dataType:"json",
                    type:"get",
                    success:function (data) {
                        var html = "";
                        $.each(data,function (i,n) {
                            html+='<tr id="'+ n.id +'">';
                            html+='<td><input type="radio" name="xzc" value="'+ n.id +'"/></td>';
                            html+='<td id="e'+ n.id +'">'+ n.fullname +'</td>';
                            html+='<td>'+ n.email +'</td>';
                            html+='<td>'+ n.mphone +'</td>';
                            html+='</tr>';
                        })
                        $("#ContanctsBody").append(html);
                    }
                })
            }

            //通过模糊查询进行查找市场来源
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
                        $("#actBody").append(html);
                    }
                })
            }

            //重复找到所有者的操作
            function selectUser() {
                $("#create-transactionOwner").empty();
                $.ajax({
                    url:"workbench/clue/getUserList.do",
                    type:"get",
                    dataType:"json",
                    success:function (data) {
                        var html="";
                        $.each(data,function (i,n) {
                            html+="<option value="+ n.id +">" + n.name + "</option>"
                        })
                        $("#create-transactionOwner").append(html);

                        $("#create-transactionOwner").val("${user.id}");
                    }
                })
            }
        </script>
    </head>
    <body>

        <!-- 查找市场活动 -->
        <div class="modal fade" id="findMarketActivity" role="dialog">
            <div class="modal-dialog" role="document" style="width: 80%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">×</span>
                        </button>
                        <h4 class="modal-title">查找市场活动</h4>
                    </div>
                    <div class="modal-body">
                        <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                            <form class="form-inline" role="form">
                                <div class="form-group has-feedback">
                                    <input type="text" class="form-control" style="width: 300px;" id="activityName"
                                           placeholder="请输入市场活动名称，支持模糊查询">
                                    <span class="glyphicon glyphicon-search form-control-feedback"></span>
                                </div>
                            </form>
                        </div>
                        <table id="activityTable3" class="table table-hover"
                               style="width: 900px; position: relative;top: 10px;">
                            <thead>
                            <tr style="color: #B3B3B3;">
                                <td></td>
                                <td>名称</td>
                                <td>开始日期</td>
                                <td>结束日期</td>
                                <td>所有者</td>
                            </tr>
                            </thead>
                            <tbody id="actBody">
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

        <!-- 查找联系人 -->
        <div class="modal fade" id="findContacts" role="dialog">
            <div class="modal-dialog" role="document" style="width: 80%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">×</span>
                        </button>
                        <h4 class="modal-title">查找联系人</h4>
                    </div>
                    <div class="modal-body">
                        <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                            <form class="form-inline" role="form">
                                <div class="form-group has-feedback">
                                    <input type="text" class="form-control" style="width: 300px;" id="ContactName"
                                           placeholder="请输入联系人名称，支持模糊查询">
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
                                <td>邮箱</td>
                                <td>手机</td>
                            </tr>
                            </thead>
                            <tbody id="ContanctsBody">
                            <%--
                            <tr>
                                <td><input type="radio" name="activity"/></td>
                                <td>李四</td>
                                <td>lisi@bjpowernode.com</td>
                                <td>12345678901</td>
                            </tr>
                            <tr>
                                <td><input type="radio" name="activity"/></td>
                                <td>李四</td>
                                <td>lisi@bjpowernode.com</td>
                                <td>12345678901</td>
                            </tr>--%>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" id="createContacts">添加</button>
                    </div>
                </div>
            </div>
        </div>


        <div style="position:  relative; left: 30px;">
            <h3>创建交易</h3>
            <div style="position: relative; top: -40px; left: 70%;">
                <button type="button" class="btn btn-primary" id="submitBtn">保存</button>
                <button type="button" class="btn btn-default">取消</button>
            </div>
            <hr style="position: relative; top: -40px;">
        </div>
        <form class="form-horizontal" role="form" style="position: relative; top: -30px;"action="workbench/tran/save.do" type="post" id="create-form">
            <div class="form-group">
                <label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span
                        style="font-size: 15px; color: red;">*</span></label>
                <div class="col-sm-10" style="width: 300px;">
                    <select class="form-control" name="create-owner" id="create-transactionOwner">
                        <option></option>
                    </select>
                </div>
                <label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
                <div class="col-sm-10" style="width: 300px;">
                    <input type="text" class="form-control" name="create-money" id="create-amountOfMoney">
                </div>
            </div>

            <div class="form-group">
                <label for="create-transactionName" class="col-sm-2 control-label">名称<span
                        style="font-size: 15px; color: red;">*</span></label>
                <div class="col-sm-10" style="width: 300px;">
                    <input type="text" class="form-control" name="create-name" id="create-transactionName">
                </div>
                <label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span
                        style="font-size: 15px; color: red;">*</span></label>
                <div class="col-sm-10" style="width: 300px;">
                    <input type="text" class="form-control time1" name="create-expectedDate" id="create-expectedClosingDate">
                </div>
            </div>

            <div class="form-group">
                <label for="create-accountName" class="col-sm-2 control-label">客户名称<span
                        style="font-size: 15px; color: red;">*</span></label>
                <div class="col-sm-10" style="width: 300px;">
                    <input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
                    <input type="hidden" name="customerid" id="mycustomerid">
                </div>
                <label for="create-transactionStage" class="col-sm-2 control-label">阶段<span
                        style="font-size: 15px; color: red;">*</span></label>
                <div class="col-sm-10" style="width: 300px;">
                    <select class="form-control" name="create-stage" id="create-Stage1">
                        <option></option>
                        <option value="01资质审查">资质审查</option>
                        <option value="02需求分析">需求分析</option>
                        <option value="03价值建议">价值建议</option>
                        <option value="04确定决策者">确定决策者</option>
                        <option value="05提案/报价">提案/报价</option>
                        <option value="06谈判/复审">谈判/复审</option>
                        <option value="07成交">成交</option>
                        <option value="08丢失的线索">丢失的线索</option>
                        <option value="09因竞争丢失关闭">因竞争丢失关闭</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="create-transactionType" class="col-sm-2 control-label">类型</label>
                <div class="col-sm-10" style="width: 300px;">
                    <select class="form-control" name="create-type" id="create-transactionType">
                        <option></option>
                        <option>已有业务</option>
                        <option>新业务</option>
                    </select>
                </div>
                <label for="create-possibility" class="col-sm-2 control-label">可能性</label>
                <div class="col-sm-10" style="width: 300px;">
                    <input type="text" class="form-control" name="create-possibility" id="create-possibility">
                </div>
            </div>

            <div class="form-group">
                <label for="create-clueSource" class="col-sm-2 control-label">来源</label>
                <div class="col-sm-10" style="width: 300px;">
                    <select class="form-control" name="create-source" id="create-clueSource">
                        <option></option>
                        <option>广告</option>
                        <option>推销电话</option>
                        <option>员工介绍</option>
                        <option>外部介绍</option>
                        <option>在线商场</option>
                        <option>合作伙伴</option>
                        <option>公开媒介</option>
                        <option>销售邮件</option>
                        <option>合作伙伴研讨会</option>
                        <option>内部研讨会</option>
                        <option>交易会</option>
                        <option>web下载</option>
                        <option>web调研</option>
                        <option>聊天</option>
                    </select>
                </div>
                <label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a
                        href="javascript:void(0);" data-toggle="modal" id="openSearchAct">
                    <span class="glyphicon glyphicon-search"></span></a></label>
                <div class="col-sm-10" style="width: 300px;">
                    <input type="hidden" name="activityid" id="myactvtiyid">
                    <input type="text" class="form-control" id="create-activitySrc">
                </div>
            </div>

            <div class="form-group">
                <label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a
                        href="javascript:void(0);" data-toggle="modal" id="openSearchCon"><span
                        class="glyphicon glyphicon-search"></span></a></label>
                <div class="col-sm-10" style="width: 300px;">
                    <input type="hidden" name="contactid" id="mycontactid">
                    <input type="text" class="form-control" id="create-contactsName">
                </div>
            </div>

            <div class="form-group">
                <label for="create-describe" class="col-sm-2 control-label">描述</label>
                <div class="col-sm-10" style="width: 70%;">
                    <textarea class="form-control" rows="3" name="create-description" id="create-describe"></textarea>
                </div>
            </div>

            <div class="form-group">
                <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                <div class="col-sm-10" style="width: 70%;">
                    <textarea class="form-control" rows="3" name="create-contactSummary" id="create-contactSummary"></textarea>
                </div>
            </div>

            <div class="form-group">
                <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                <div class="col-sm-10" style="width: 300px;">
                    <input type="text" class="form-control time2" name="create-nextContactTime" id="create-nextContactTime">
                </div>
            </div>

        </form>
    </body>
</html>