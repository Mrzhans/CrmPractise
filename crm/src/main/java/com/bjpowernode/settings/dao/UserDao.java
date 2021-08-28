package com.bjpowernode.settings.dao;

import com.bjpowernode.settings.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    User login(@Param("loginAct") String loginAct,@Param("loginPwd") String loginPwd);

    List<User> getUserList();
}
