package com.bjpowernode.settings.test;

import com.bjpowernode.util.DateTimeUtil;
import org.junit.Test;

public class Test1 {
    @Test
    public void test(){
        String expireTime = "2021-10-10 10:10:10";
        String currentTime = DateTimeUtil.getSysTime();

        int count = expireTime.compareTo(currentTime);
        if (count > 0){
            System.out.println("没失效");
        }
    }
}
