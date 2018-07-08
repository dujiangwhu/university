package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxStudentT;
import com.tranzvision.gd.TZPXBundle.model.PxStudentTKey;

public interface PxStudentTMapper {
    int deleteByPrimaryKey(PxStudentTKey key);

    int insert(PxStudentT record);

    int insertSelective(PxStudentT record);

    PxStudentT selectByPrimaryKey(PxStudentTKey key);

    int updateByPrimaryKeySelective(PxStudentT record);

    int updateByPrimaryKey(PxStudentT record);
}