package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    int deleteById(String clueid);

    List<ClueActivityRelation> getActRelationById(String clueid);
}
