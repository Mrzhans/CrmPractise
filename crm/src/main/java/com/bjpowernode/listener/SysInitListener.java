package com.bjpowernode.listener;

import com.bjpowernode.settings.domain.DicType;
import com.bjpowernode.settings.domain.DicValue;
import com.bjpowernode.util.PrintJson;
import com.bjpowernode.util.ServiceFactory;
import com.bjpowernode.workbench.service.ClueService;
import com.bjpowernode.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("全局作用域对象被创建了");

        ServletContext context = event.getServletContext();

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<DicType> typeList = cs.getAll();

        //通过监听器的方式把数据字典当中的值传入到全局作用域对象当中
        // （全局作用域对象当中的值一般来说都是整个系统当中都比较常用的，而且数据基本都不改变，保存在数据库当中都是没有意义的）
        for (DicType d : typeList) {
            List<DicValue> valueList = cs.getValueList(d.getCode());
            context.setAttribute(d.getCode()+"list",valueList);
        }

        //监听器的方式绑定可能性的一对一关系，记住bundle读取文件的时候不能带后缀名
        String json = "{";
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = bundle.getString(key);
            if (keys.hasMoreElements()){
                json += "\"" + key + "\"" + ":" + value + ",";
            }else {
                json += "\"" + key + "\"" + ":" + value + "}";
            }
        }
        context.setAttribute("json",json);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("全局作用域对象被销毁了");
    }
}
