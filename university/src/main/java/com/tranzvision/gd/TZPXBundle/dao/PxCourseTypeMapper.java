package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxCourseType;

public interface PxCourseTypeMapper {
    int deleteByPrimaryKey(String tzCourseTypeId);

    int insert(PxCourseType record);

    int insertSelective(PxCourseType record);

    PxCourseType selectByPrimaryKey(String tzCourseTypeId);

    int updateByPrimaryKeySelective(PxCourseType record);

    int updateByPrimaryKey(PxCourseType record);
}