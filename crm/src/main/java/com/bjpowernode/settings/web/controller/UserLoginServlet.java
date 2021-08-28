package com.bjpowernode.settings.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.util.MD5Util;
import com.bjpowernode.util.PrintJson;
import com.bjpowernode.util.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLoginServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到登录后台");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = MD5Util.getMD5(request.getParameter("loginPwd"));
        //获取ip地址
        String ip = request.getRemoteAddr();

        //未来业务层的开发，统一使用代理类形态的接口对象，因为可能涉及到事务的操作
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user = us.login(loginAct, loginPwd,ip);
            //一个二分条件，如果能执行到这一语句，那么代表User获取登录成功了，需要返回一个success为true
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            //运行到这里的时候则是登录失败了，需要返回一个success为false
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String, Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
