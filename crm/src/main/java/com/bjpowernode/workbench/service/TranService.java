package com.bjpowernode.workbench.service;

import com.bjpowernode.workbench.domain.Tran;

import java.util.Map;

public interface TranService {
    boolean save(Tran t);

    Map<String, Object> getECharts();
}
