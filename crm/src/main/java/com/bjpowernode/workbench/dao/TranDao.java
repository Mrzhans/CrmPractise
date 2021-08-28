package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    int getTotal();

    List<Map<String, Object>> getECharts();
}