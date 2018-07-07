package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxCourse;

public interface PxCourseMapper {
    int deleteByPrimaryKey(String tzCourseId);

    int insert(PxCourse record);

    int insertSelective(PxCourse record);

    PxCourse selectByPrimaryKey(String tzCourseId);

    int updateByPrimaryKeySelective(PxCourse record);

    int updateByPrimaryKeyWithBLOBs(PxCourse record);

    int updateByPrimaryKey(PxCourse record);
}