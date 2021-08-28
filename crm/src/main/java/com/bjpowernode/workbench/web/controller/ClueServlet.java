package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.util.DateTimeUtil;
import com.bjpowernode.util.PrintJson;
import com.bjpowernode.util.ServiceFactory;
import com.bjpowernode.util.UUIDUtil;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.service.ClueService;
import com.bjpowernode.workbench.service.impl.ClueServiceImpl;
import com.sun.org.apache.regexp.internal.RE;
import sun.security.util.AuthResources_it;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索控制器");

        String path = request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/clue/save.do".equals(path)){
            save(request, response);
        } else if ("/workbench/clue/selectAllClue.do".equals(path)) {
            selectAllClue(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            showDetail(request,response);
        }else if ("/workbench/clue/getActivityByClueId.do".equals(path)){
            getActivityByClueId(request,response);
        } else if ("/workbench/clue/delByActivityId.do".equals(path)) {
            unbund(request,response);
        }else if ("/workbench/clue/getActivtiys.do".equals(path)){
            getActivtiys(request,response);
        }else if ("/workbench/clue/createActivtiyRelation.do".equals(path)){
            createActivtiyRelation(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request, response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //因为我们创建交易和不创建交易都是使用同一个方法，但是创建交易会出现需要接受参数的情况，所以我们需要传入一个布尔标记进行判断
        System.out.println("转换线索并且判断是否需要创建交易");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String clueid = request.getParameter("clueid");
        String flag = request.getParameter("flag");
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        Tran t = null;
        if ("true".equals(flag)){
            //需要创建交易，我们需要获取参数
            String id = UUIDUtil.getUUID();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityid = request.getParameter("activityid");
            String createTime = DateTimeUtil.getSysTime();

            t = new Tran();
            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityid);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }
        boolean flag1 = cs.convert(clueid,t,createBy);
        System.out.println(flag1);
        if (flag1){
            response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");
        }
    }

    private void createActivtiyRelation(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("添加与线索相关的市场活动");
        String clueid = request.getParameter("clueid");
        String[] activityids = request.getParameter("activityids").split("&");
        Map<String,Object> map = new HashMap<>();
        boolean flag = false;
        for (String activityid : activityids) {
            ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
            String id = UUIDUtil.getUUID();
            map.put("id", id);
            map.put("clueid",clueid);
            map.put("activityid", activityid);
            flag = cs.createActivtiyRelation(map);
            if (!flag){
                System.out.println("插入有问题！");
                return;
            }
        }
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivtiys(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取该线索可以连接的市场活动");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String clueid = request.getParameter("clueid");
        String name = request.getParameter("name");
        List<Activity> activityList = cs.getActivtiys(clueid,name);
        PrintJson.printJsonObj(response,activityList);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除线索关联活动");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String id = request.getParameter("id");
        boolean flag = cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("通过线索id查找关联的活动");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String clueid = request.getParameter("id");
        List<Activity> activityList = cs.getActivityByClueId(clueid);
        PrintJson.printJsonObj(response,activityList);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("展示所有线索的细节");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String id = request.getParameter("id");
        Clue clue = cs.selectByClueId(id);
        request.setAttribute("clue",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void selectAllClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("列出所有线索");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Clue> clueList = cs.selectAllClue();
        PrintJson.printJsonObj(response,clueList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("添加线索活动");
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = request.getParameter("createBy");
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        int count = cs.save(c);
        boolean flag = false;
        if (count > 0){
            flag = true;
        }
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取线索用户列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(response,userList);
    }
}
