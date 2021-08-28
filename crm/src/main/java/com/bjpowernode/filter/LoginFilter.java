package com.bjpowernode.filter;

import com.bjpowernode.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到登录验证界面");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            //不应该被拦截的资源
            filterChain.doFilter(request,response);
        }

        //应该被拦截的资源
        else {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            if (user != null) {
                filterChain.doFilter(request, response);
            } else {
            /*
                为什么不能使用请求转发，因为请求转发的项目名会停留在老项目的路径而不是显示到一个绝对新的路径上
                请求转发：不用加项目名，只用加想要的那个模块名名例如：/login.jsp
                重定向：要加项目名/crm/login.jsp
                出现一个问题：每次我们欢迎资源页面就是去到login.jsp当中，触发了过滤器机制，然后重定向又到了login.jsp
                然后又会匹配到.jsp的过滤器机制，出现死循环的局面，所以我们需要有一个默认不触发的页面
             */
                resp.sendRedirect(req.getContextPath() + "/login.jsp");

            }
        }
    }
}
