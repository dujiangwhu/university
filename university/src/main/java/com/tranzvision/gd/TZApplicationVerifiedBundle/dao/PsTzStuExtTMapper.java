package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzStuExtT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzStuExtTKey;

public interface PsTzStuExtTMapper {
    int deleteByPrimaryKey(PsTzStuExtTKey key);

    int insert(PsTzStuExtT record);

    int insertSelective(PsTzStuExtT record);

    PsTzStuExtT selectByPrimaryKey(PsTzStuExtTKey key);

    int updateByPrimaryKeySelective(PsTzStuExtT record);

    int updateByPrimaryKey(PsTzStuExtT record);
}