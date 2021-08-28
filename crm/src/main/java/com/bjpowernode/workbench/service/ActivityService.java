package com.bjpowernode.workbench.service;

import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.ActivityRemark;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ActivityService {
    void save(HttpServletResponse response, Activity activity);

    void pageList(HttpServletResponse response, Map<String, Object> map);

    void delete(HttpServletResponse response, String[] ids);

    void selectById(HttpServletResponse response,String id);

    void update(HttpServletResponse response, Activity a);

    Activity getById(String id);

    List<ActivityRemark> selectAllRemark(String id);

    int updateRemarkById(String id, String content);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark remark);

    List<Activity> selectActivityByName(String name);
}
