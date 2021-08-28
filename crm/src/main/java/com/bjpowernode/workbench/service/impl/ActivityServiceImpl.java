package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.util.PrintJson;
import com.bjpowernode.util.SqlSessionUtil;
import com.bjpowernode.workbench.dao.ActivityDao;
import com.bjpowernode.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.ActivityRemark;
import com.bjpowernode.workbench.domain.PaginationVO;
import com.bjpowernode.workbench.service.ActivityService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao acdao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public void save(HttpServletResponse response, Activity activity) {
        int count = acdao.save(activity);
        if (count > 0){
            PrintJson.printJsonFlag(response,true);
        }else {
            PrintJson.printJsonFlag(response,false);
        }
    }

    @Override
    public void pageList(HttpServletResponse response, Map<String, Object> map) {
        PaginationVO<Activity> vo = new PaginationVO<>();
        List<Activity> acList = acdao.selectByCondition(map);
        int total = acdao.selectPageTotal();
        vo.setPageList(acList);
        vo.setTotal(total);
        PrintJson.printJsonObj(response,vo);
    }

    @Override
    public void delete(HttpServletResponse response, String[] ids) {
        ActivityRemarkDao remarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        boolean flag = false;
        remarkDao.deleteByAcId(ids);
        int count2 = acdao.deleteById(ids);
        if (count2 > 0){
            flag = true;
        }
        PrintJson.printJsonFlag(response,flag);
    }

    @Override
    public void selectById(HttpServletResponse response,String id) {
        Activity activity = acdao.selectById(id);
        PrintJson.printJsonObj(response,activity);
    }

    @Override
    public void update(HttpServletResponse response, Activity a) {
        boolean flag = false;
        int count = acdao.update(a);
        if (count > 0){
            flag = true;
        }
        PrintJson.printJsonFlag(response,flag);
    }

    @Override
    public Activity getById(String id) {
        Activity activity = acdao.getById(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> selectAllRemark(String id) {
        ActivityRemarkDao activityRemarkDao = (ActivityRemarkDao) SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        List<ActivityRemark> list = activityRemarkDao.selectAllRemark(id);
        return list;
    }

    @Override
    public int updateRemarkById(String id, String content) {
        ActivityRemarkDao activityRemarkDao = (ActivityRemarkDao) SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        int count = activityRemarkDao.updateRemarkById(id, content);
        return count;
    }

    @Override
    public int deleteRemark(String id) {
        ActivityRemarkDao activityRemarkDao = (ActivityRemarkDao) SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        int count = activityRemarkDao.deleteRemark(id);
        return count;
    }

    @Override
    public int saveRemark(ActivityRemark remark) {
        ActivityRemarkDao activityRemarkDao = (ActivityRemarkDao) SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        int count = activityRemarkDao.saveRemark(remark);
        return count;
    }

    @Override
    public List<Activity> selectActivityByName(String name) {
        List<Activity> activityList = acdao.selectActivityByName(name);
        return activityList;
    }
}
