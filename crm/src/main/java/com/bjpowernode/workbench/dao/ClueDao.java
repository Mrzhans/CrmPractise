package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    int save(Clue c);

    List<Clue> selectAllClue();

    Clue selectByClueId(String id);

    List<Activity> getActivityByClueId(String clueid);

    int unbund(String id);

    List<Activity> getActivitys(@Param("clueid") String clueid,@Param("name") String name);

    int createActivtiyRelation(Map<String,Object> map);

    int deleteById(String clueid);
}
