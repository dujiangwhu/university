package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxTeaScheduleT;

public interface PxTeaScheduleTMapper {
    int deleteByPrimaryKey(String tzScheduleId);

    int insert(PxTeaScheduleT record);

    int insertSelective(PxTeaScheduleT record);

    PxTeaScheduleT selectByPrimaryKey(String tzScheduleId);

    int updateByPrimaryKeySelective(PxTeaScheduleT record);

    int updateByPrimaryKey(PxTeaScheduleT record);
}