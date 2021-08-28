package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.util.SqlSessionUtil;
import com.bjpowernode.workbench.dao.TranDao;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);

    @Override
    public boolean save(Tran t) {
        boolean flag = false;
        int count = tranDao.save(t);
        if (count > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getECharts() {
        int total = tranDao.getTotal();

        List<Map<String,Object>> dataList = tranDao.getECharts();

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList",dataList);
        return map;
    }
}
