package com.bjpowernode.workbench.service;

import com.bjpowernode.settings.domain.DicType;
import com.bjpowernode.settings.domain.DicValue;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService{
    List<DicType> getAll();

    List<DicValue> getValueList(String code);

    int save(Clue c);

    List<Clue> selectAllClue();

    Clue selectByClueId(String id);

    List<Activity> getActivityByClueId(String clueid);

    boolean unbund(String id);

    List<Activity> getActivtiys(String clueid,String name);

    boolean createActivtiyRelation(Map<String,Object> map);

    boolean convert(String clueid, Tran t, String createBy);
}
