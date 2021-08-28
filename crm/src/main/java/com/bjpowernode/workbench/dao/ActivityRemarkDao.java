package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityRemarkDao {
    int deleteByAcId(String[] ids);

    List<ActivityRemark> selectAllRemark(String id);

    int updateRemarkById(@Param("id") String id,@Param("content") String content);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark remark);
}
