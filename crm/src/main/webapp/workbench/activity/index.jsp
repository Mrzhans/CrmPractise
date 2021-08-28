<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
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

        <!--导入包的顺序一定不能乱，因为是有主体和扩展功能的-->
        <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
        <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
        <script type="text/javascript"
                src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript"
                src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
        <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
        <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
        <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

        <script type="text/javascript">

            $(function () {
                $("#addBtn").click(function () {
                    //创建时间插件，通过类选择器进行操作
                    $(".time").datetimepicker({
                        minView: "month",
                        language: 'zh-CN',
                        format: 'yyyy-mm-dd',
                        autoclose: true,
                        todayBtn: true,
                        pickerPosition: "bottom-left"
                    });

                    selectUser($("#create-owner"))
                    $("#createActivityModal").modal("show");
                })

                //添加按钮的对应点击操作，添加市场活动
                $("#saveBtn").click(function () {
                    $.ajax({
                        url: "workbench/activity/save.do",
                        data: {
                            "owner": $.trim($("#create-owner").val()),
                            "name": $.trim($("#create-name").val()),
                            "startDate": $.trim($("#create-startDate").val()),
                            "endDate": $.trim($("#create-endDate").val()),
                            "cost": $.trim($("#create-cost").val()),
                            "description": $.trim($("#create-description").val())
                        },
                        dataType: "json",
                        type: "get",
                        success: function (data) {
                            if (data.success) {
                                //添加成功之后我们要局部刷新，关闭添加操作的模态窗口，然后我们再刷新用户创建表就行了。
                                /*
                                    我们每次添加成功后需要及时清除里面的数据，防止下次泄漏以及下次填写
                                    注意我们直接调用jQuery对象当中的reset方法是有存在的，但是没办法起作用，
                                    所以我们转换成dom对象再调用reset方法才有用。
                                 */
                                $("#create-form")[0].reset();

                                $("#createActivityModal").modal("hide")
                            } else {
                                alert("添加市场活动失败");
                            }
                        }
                    })
                    pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
                })
                //查询按钮的绑定操作
                $("#select-button").click(function () {
                    $("#hidden-name").val($("#select-name").val())
                    $("#hidden-owner").val($("#select-owner").val())
                    $("#hidden-startDate").val($("#select-startDate").val())
                    $("#hidden-endDate").val($("#select-endDate").val())

                    pageList($("#activityPage").bs_pagination('getOption','currentPage'),
                        $("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
                })

                //全选按钮的绑定操作
                $("#qx").click(function () {
                    $("input[name=xz]").prop("checked",this.checked);
                })

                //这种方法做是不行的，因为我们其中的input标签中的元素是我们动态生成的，动态生成的元素没办法使用这样的方法
                /*$("input[name=xz]").click(function () {
                    alert(123)
                })*/

                /*
                    其他按钮全选导致全选按钮打√的操作
                    动态生成的元素，我们要以on的形式来触发事件
                    需要绑定元素的有效外层元素.on("事件名称"，"需要处理的JQ对象",function处理函数)
                */
                $("#activityBody").on("click",$("input[name=xz]"),function () {
                    $("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);
                })

                //添加市场活动删除操作
                $("#delBtn").click(function () {
                    if(confirm("确定是否删除？")){
                        /*
                        删除需要进行ajax操作进行局部刷新，所以一定要用到ajax方法,此时传入进去的参数都是id形式的
                        /workbench/activity/deleted.do?id=....&id=...
                        所以我们没办法直接获取json数组的方式传入参数进去，我们只能使用拼接字符串的形式进行传入参数。
                    */
                        var canshu = "";
                        var $xz = $("input[name=xz]:checked");
                        for (var i = 0; i < $xz.length ; i++) {
                            canshu += $xz[i].value;
                            if(i < $xz.length - 1){
                                canshu += "&";
                            }
                        }

                        $.ajax({
                            url:"workbench/activity/delete.do",
                            data: {
                                "id": canshu
                            },
                            dataType:"json",
                            type:"post",
                            success:function (data) {
                                if (data.success){
                                    alert("删除成功");
                                    pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
                                }else {
                                    alert("删除失败");
                                }
                            }
                        })
                    }
                })

                //市场活动的修改操作
                $("#editBtn").click(function () {
                    $(".time").datetimepicker({
                        minView: "month",
                        language: 'zh-CN',
                        format: 'yyyy-mm-dd',
                        autoclose: true,
                        todayBtn: true,
                        pickerPosition: "bottom-left"
                    });
                    selectUser($("#edit-owner"))
                    var $id = $("input[name=xz]:checked").val();
                    $.ajax({
                        url:"workbench/activity/selectById.do",
                        data:{
                            "id":$id
                        },
                        dataType:"json",
                        type:"post",
                        success:function (data) {
                            $("#edit-name").val(data.name)
                            $("#edit-startDate").val(data.startDate)
                            $("#edit-endDate").val(data.endDate)
                            $("#edit-cost").val(data.cost)
                            $("#edit-description").val(data.description)
                        }
                    })
                    $("#editActivityModal").modal("show");
                })

                //市场活动点击更新之后的更新操作
                $("#updateBtn").click(function () {
                    $.ajax({
                        url:"workbench/activity/update.do",
                        data:{
                            "id":$("input[name=xz]:checked").val(),
                            "owner":$("#edit-owner").val(),
                            "name":$("#edit-name").val(),
                            "startDate":$("#edit-startDate").val(),
                            "endDate":$("#edit-endDate").val(),
                            "cost":$("#edit-cost").val(),
                            "description":$("#edit-description").val(),
                        },
                        dataType:"json",
                        type:"post",
                        success:function (data) {
                            if (data.success){
                                pageList($("#activityPage").bs_pagination('getOption','currentPage'),
                                    $("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
                            }else {
                                alert("修改失败");
                            }
                        }
                    })
                    $("#editActivityModal").modal("hide");
                })
                pageList(1,2)
            });

            function pageList(pageNo, pageSize) {
                $("#select-name").val($("#hidden-name").val())
                $("#select-owner").val($("#hidden-owner").val())
                $("#select-startDate").val($("#hidden-startDate").val())
                $("#select-endDate").val($("#hidden-endDate").val())

                $.ajax({
                    url: "workbench/activity/pageList.do",
                    data: {
                        /*
                            一共有6种情况我们都要调用到pageList的显示方法当中，
                            所以我们要将6种情况的参数全部都列出来，在xml当中使用
                            动态sql的方式写语句，而只用写一个方法就能实现这6种情况的功能
                            否则还要写好多个方法。
                         */
                        "pageNo": pageNo,
                        "pageSize": pageSize,
                        "name": $.trim($("#select-name").val()),
                        "owner": $.trim($("#select-ownerowner").val()),
                        "startDate": $.trim($("#select-startDate").val()),
                        "endDate": $.trim($("#select-endDate").val())
                    },
                    dataType: "json",
                    type: "get",
                    success: function (data) {
                        /*
                            后台取出的数据data有什么值？
                            我们需要取出的数据是User对象，所以肯定有一个json数组里面是user对象[{user对象},{}]
                            我们还需要总的条数给page前端插件，所以我们还有一个参数total表示总的条数
                            data:{"total":100,"userList":[{user},{user},....]}
                        */
                        var html = "";
                        //这个each！！！是用data.的形式取！！！没想到
                        $.each(data.pageList,function (i,n) {
                            html+='<tr class="active">';
                            html+='<td><input type="checkbox" value='+ n.id +' name="xz"/></td>'
                            html+='<td><a style="text-decoration: none; cursor: pointer;" href="workbench/activity/detail.do?id='+ n.id +'">' +  n.name + '</a></td>';
                            html+='<td>'+ n.owner +'</td>';
                            html+='<td>'+ n.startDate +'</td>';
                            html+='<td>'+ n.endDate +'</td>';
                            html+='</tr>';
                        })
                        $("#activityBody").html(html);

                        var totalPages = Math.ceil(data.total / pageSize);
                        //数据处理完毕，使用分页插件进行分页处理
                        $("#activityPage").bs_pagination({
                            currentPage: pageNo, // 页码
                            rowsPerPage: pageSize, // 每页显示的记录条数
                            maxRowsPerPage: 20, // 每页最多显示的记录条数
                            totalPages: totalPages, // 总页数
                            totalRows: data.total, // 总记录条数

                            visiblePageLinks: 3, // 显示几个卡片

                            showGoToPage: true,
                            showRowsPerPage: true,
                            showRowsInfo: true,
                            showRowsDefaultInfo: true,

                            onChangePage : function(event, data){
                                pageList(data.currentPage , data.rowsPerPage);
                            }
                        });
                    }
                })
            }

            function selectUser($user) {
                $user.empty();
                //走后台，为下拉框获取请求
                $.ajax({
                    url: "workbench/activity/getUserList.do",
                    dataType: "json",
                    type: "get",
                    success: function (data) {
                        $.each(data, function (i, n) {
                            $user.append("<option value=" + n.id + ">" + n.name + "</option>");
                        })
                        $user.val("${user.id}");
                    }
                })
            }

        </script>
    </head>
    <body>

        <input type="hidden" id="hidden-name">
        <input type="hidden" id="hidden-owner">
        <input type="hidden" id="hidden-startDate">
        <input type="hidden" id="hidden-endDate">

        <!-- 创建市场活动的模态窗口 -->
        <div class="modal fade" id="createActivityModal" role="dialog">
            <div class="modal-dialog" role="document" style="width: 85%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">×</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
                    </div>
                    <div class="modal-body">

                        <form id="create-form" class="form-horizontal" role="form">

                            <div class="form-group">
                                <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                        style="font-size: 15px; color: red;">*</span></label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <select class="form-control" id="create-owner">
                                    </select>
                                </div>
                                <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                        style="font-size: 15px; color: red;">*</span></label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="create-name">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="create-startDate" readonly>
                                </div>
                                <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="create-endDate" readonly>
                                </div>
                            </div>
                            <div class="form-group">

                                <label for="create-cost" class="col-sm-2 control-label">成本</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="create-cost">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-describe" class="col-sm-2 control-label">描述</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="create-description"></textarea>
                                </div>
                            </div>

                        </form>

                    </div>
                    <div class="modal-footer">
                        <%--
                            data-dismiss关闭模态窗口
                         --%>
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" data-dismiss="modal" id="saveBtn">保存</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 修改市场活动的模态窗口 -->
        <div class="modal fade" id="editActivityModal" role="dialog">
            <div class="modal-dialog" role="document" style="width: 85%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">×</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
                    </div>
                    <div class="modal-body">

                        <form class="form-horizontal" role="form">

                            <div class="form-group">
                                <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                        style="font-size: 15px; color: red;">*</span></label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <select class="form-control" id="edit-owner">

                                    </select>
                                </div>
                                <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                        style="font-size: 15px; color: red;">*</span></label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="edit-name">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="edit-startDate" readonly>
                                </div>
                                <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="edit-endDate" readonly>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="edit-cost">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-description"></textarea>
                                </div>
                            </div>

                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
                    </div>
                </div>
            </div>
        </div>


        <div>
            <div style="position: relative; left: 10px; top: -10px;">
                <div class="page-header">
                    <h3>市场活动列表</h3>
                </div>
            </div>
        </div>
        <div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
            <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

                <div class="btn-toolbar" role="toolbar" style="height: 80px;">
                    <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-addon">名称</div>
                                <input class="form-control" type="text" id="select-name">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-addon">所有者</div>
                                <input class="form-control" type="text" id="select-owner">
                            </div>
                        </div>


                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-addon">开始日期</div>
                                <input class="form-control" type="text" id="select-startDate"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-addon">结束日期</div>
                                <input class="form-control" type="text" id="select-endDate">
                            </div>
                        </div>

                        <button type="button" class="btn btn-default" id="select-button">查询</button>

                    </form>
                </div>
                <div class="btn-toolbar" role="toolbar"
                     style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
                    <div class="btn-group" style="position: relative; top: 18%;">
                        <!--
                            在这里面写死了事件，我们只能通过点击此处然后创建模态窗口，而无法添加我们想要的操作，所以我们应该解耦合
                            通过js代码的方式进行事件的处理
                        -->
                        <button type="button" class="btn btn-primary" id="addBtn"><span
                                class="glyphicon glyphicon-plus"></span> 创建
                        </button>
                        <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改
                        </button>
                        <button type="button" id="delBtn" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除
                        </button>
                    </div>

                </div>
                <div style="position: relative;top: 10px;">
                    <table class="table table-hover">
                        <thead>
                        <tr style="color: #B3B3B3;">
                            <td><input type="checkbox" id="qx"/></td>
                            <td>名称</td>
                            <td>所有者</td>
                            <td>开始日期</td>
                            <td>结束日期</td>
                        </tr>
                        </thead>
                        <tbody id="activityBody">
                        <%--
                        <tr class="active">
                            <td><input type="checkbox"/></td>
                            <td><a style="text-decoration: none; cursor: pointer;"
                                   onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>
                        <tr class="active">
                            <td><input type="checkbox"/></td>
                            <td><a style="text-decoration: none; cursor: pointer;"
                                   onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
                        </tbody>
                    </table>
                </div>

                <div style="height: 50px; position: relative;top: 30px;">
                    <div id="activityPage">

                    </div>
                </div>
            </div>

        </div>
    </body>
</html>