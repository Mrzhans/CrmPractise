package com.bjpowernode.settings.service.impl;

import com.bjpowernode.exception.LoginException;
import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.util.DateTimeUtil;
import com.bjpowernode.util.SqlSessionUtil;

import java.util.List;

public class UserServiceImpl implements UserService{
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd,String ip) throws LoginException {
        User user = userDao.login(loginAct,loginPwd);
        if (user == null){
            throw new LoginException("登陆失败，账号密码错误");
        }
        //如果程序能运行到这里代表账号密码登陆是成功的，我们就可以继续判断下面的信息
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账号已失效");
        }

        if ("0".equals(user.getLockState())){
            throw new LoginException("账号已锁定");
        }

        if (!user.getAllowIps().contains(ip)){
            throw new LoginException("账号ip不被允许");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }
}
