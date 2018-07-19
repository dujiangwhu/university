package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxStudentT;

public interface PxStudentTMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(PxStudentT record);

    int insertSelective(PxStudentT record);

    PxStudentT selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(PxStudentT record);

    int updateByPrimaryKey(PxStudentT record);
}