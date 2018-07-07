package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxTeacher;

public interface PxTeacherMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(PxTeacher record);

    int insertSelective(PxTeacher record);

    PxTeacher selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(PxTeacher record);

    int updateByPrimaryKey(PxTeacher record);
}