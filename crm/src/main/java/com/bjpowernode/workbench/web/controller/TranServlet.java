package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.util.DateTimeUtil;
import com.bjpowernode.util.PrintJson;
import com.bjpowernode.util.ServiceFactory;
import com.bjpowernode.util.UUIDUtil;
import com.bjpowernode.workbench.domain.Contacts;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.service.ContactService;
import com.bjpowernode.workbench.service.TranService;
import com.bjpowernode.workbench.service.impl.ContactServiceImpl;
import com.bjpowernode.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TranServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入交易控制器");

        String path = request.getServletPath();

        if("/workbench/contact/selectContactByName.do".equals(path)){
            selectContactByName(request,response);
        }else if ("/workbench/tran/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/tran/getECharts.do".equals(path)){
            getECharts(request, response);
        }
    }

    private void getECharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("展现所有交易数据转换为图表");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = ts.getECharts();
        PrintJson.printJsonObj(response,map);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("添加交易并且创建一条交易历史");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("create-owner");
        String money = request.getParameter("create-money");
        String name = request.getParameter("create-name");
        String expectedDate = request.getParameter("create-expectedDate");
        String customerId = request.getParameter("customerid");
        String stage = request.getParameter("create-stage");
        String type = request.getParameter("create-type");
        String source = request.getParameter("create-source");
        String activityId = request.getParameter("activityid");
        String contactsId = request.getParameter("contactid");
        String createBy = ((User)(request.getSession().getAttribute("user"))).getCreateBy();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("create-description");
        String contactSummary = request.getParameter("create-contactSummary");
        String nextContactTime = request.getParameter("create-nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setCustomerId(customerId);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.save(t);
        if (flag){
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }
    }

    private void selectContactByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("通过姓名搜索联系人");
        String name = request.getParameter("name");
        ContactService cons = (ContactService) ServiceFactory.getService(new ContactServiceImpl());
        List<Contacts> conlist = cons.selectContactByName(name);
        PrintJson.printJsonObj(response,conlist);
    }
}
