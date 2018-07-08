package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxScheduleCancelT;

public interface PxScheduleCancelTMapper {
    int deleteByPrimaryKey(String tzScheduleId);

    int insert(PxScheduleCancelT record);

    int insertSelective(PxScheduleCancelT record);

    PxScheduleCancelT selectByPrimaryKey(String tzScheduleId);

    int updateByPrimaryKeySelective(PxScheduleCancelT record);

    int updateByPrimaryKey(PxScheduleCancelT record);
}