package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxStudent;

public interface PxStudentMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(PxStudent record);

    int insertSelective(PxStudent record);

    PxStudent selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(PxStudent record);

    int updateByPrimaryKey(PxStudent record);
}