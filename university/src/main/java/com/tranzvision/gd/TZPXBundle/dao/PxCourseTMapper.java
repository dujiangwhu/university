package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxCourseT;

public interface PxCourseTMapper {
    int deleteByPrimaryKey(String tzCourseId);

    int insert(PxCourseT record);

    int insertSelective(PxCourseT record);

    PxCourseT selectByPrimaryKey(String tzCourseId);

    int updateByPrimaryKeySelective(PxCourseT record);

    int updateByPrimaryKeyWithBLOBs(PxCourseT record);

    int updateByPrimaryKey(PxCourseT record);
}