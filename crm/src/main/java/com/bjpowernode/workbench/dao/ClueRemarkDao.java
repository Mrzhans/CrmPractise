package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> selectById(String clueid);

    int deleteById(String clueid);
}
