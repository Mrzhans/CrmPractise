package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.util.*;
import com.bjpowernode.workbench.dao.ActivityDao;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.ActivityRemark;
import com.bjpowernode.workbench.domain.PaginationVO;
import com.bjpowernode.workbench.service.ActivityService;
import com.bjpowernode.workbench.service.impl.ActivityServiceImpl;
import com.sun.javafx.sg.prism.NGNode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ActivityServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");

        String path = request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(request, response);
        }else if ("/workbench/activity/selectById.do".equals(path)){
            selectById(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(request, response);
        }else if ("/workbench/activity/detail.do".equals(path)){
            getByid(request,response);
        }else if ("/workbench/activity/selectAllRemark.do".equals(path)){
            selectAllRemark(request,response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){
            updateRemarkById(request, response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request, response);
        }else if ("/workbench/activtiy/selectActivityByName.do".equals(path)){
            selectActivityByName(request,response);
        }
    }

    private void selectActivityByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("通过名字搜索活动列表");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String name = request.getParameter("name");
        List<Activity> activityList = ac.selectActivityByName(name);
        PrintJson.printJsonObj(response,activityList);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("添加备注信息");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String actId = request.getParameter("activityid");
        String content = request.getParameter("content");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = request.getParameter("createBy");
        ActivityRemark remark = new ActivityRemark();
        remark.setId(id);
        remark.setActivityId(actId);
        remark.setNoteContent(content);
        remark.setCreateTime(createTime);
        remark.setCreateBy(createBy);
        remark.setEditFlag("0");
        int count = ac.saveRemark(remark);
        boolean flag = false;
        if (count > 0){
            flag = true;
        }
        PrintJson.printJsonFlag(response,flag);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除备注信息");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = request.getParameter("id");
        int count = ac.deleteRemark(id);
        boolean flag = false;
        if (count > 0){
            flag = true;
        }
        PrintJson.printJsonFlag(response,flag);
    }

    private void updateRemarkById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("修改备注信息");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = request.getParameter("id");
        String content = request.getParameter("content");
        int count = ac.updateRemarkById(id, content);
        boolean flag = false;
        if (count > 0){
            flag = true;
        }
        PrintJson.printJsonFlag(response,flag);
    }

    private void selectAllRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("显示所有备注信息");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = request.getParameter("id");
        List<ActivityRemark> arlist =  ac.selectAllRemark(id);
        PrintJson.printJsonObj(response,arlist);
    }

    private void getByid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("执行展现市场活动细节操作");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = request.getParameter("id");
        Activity activity =  ac.getById(id);
        //请求转发的方式传入activity参数
        request.setAttribute("a",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动修改操作");
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String cost = request.getParameter("cost");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession(false).getAttribute("user");
        String editBy = user.getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ac.update(response, a);
    }

    private void selectById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行查找市场活动操作");
        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        as.selectById(response,id);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行删除市场活动操作");
        String[] ids = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        as.delete(response,ids);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行分页操作");

        //首先先获取数据
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        int skipCount = (Integer.valueOf(pageNo) - 1) * Integer.valueOf(pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //调用业务逻辑层当中的方法，那么我们要传入什么类型的参数进去呢？Map类型，注意vo对象是值对象，是dao层获取了数据才要用到的
        Map<String,Object> map = new HashMap<>();
        map.put("skipCount", skipCount);
        map.put("pageSize", Integer.valueOf(pageSize));
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        /*
            那我们如何来接收数据呢？？Dao层传回来的数据有UserList以及total
            没有相应的domain类，所以我们只剩下两种方法，一种是vo类，一种是map集合
            其中怎么选择这两种？要是用的多我们就用vo，要是用的少我们就map集合。
            分页查询用的比较多，所以我们选择VO
         */
        as.pageList(response,map);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动添加操作");
        ActivityService a = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");;
        String cost = request.getParameter("cost");;
        String description = request.getParameter("description");;
        String createTime = DateTimeUtil.getSysTime();
        User user = (User)(request.getSession().getAttribute("user"));
        String createBy = user.getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        a.save(response,activity);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService ss = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userlist = ss.getUserList();

        PrintJson.printJsonObj(response,userlist);
    }
}
