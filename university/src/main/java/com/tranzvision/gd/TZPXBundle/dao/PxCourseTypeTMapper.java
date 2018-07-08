package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxCourseTypeT;

public interface PxCourseTypeTMapper {
    int deleteByPrimaryKey(String tzCourseTypeId);

    int insert(PxCourseTypeT record);

    int insertSelective(PxCourseTypeT record);

    PxCourseTypeT selectByPrimaryKey(String tzCourseTypeId);

    int updateByPrimaryKeySelective(PxCourseTypeT record);

    int updateByPrimaryKey(PxCourseTypeT record);
}