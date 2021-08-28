package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.PaginationVO;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> selectByCondition(Map<String, Object> map);

    int selectPageTotal();

    int deleteById(String[] ids);

    Activity selectById(String id);

    int update(Activity a);

    Activity getById(String id);

    List<Activity> selectActivityByName(String name);
}
