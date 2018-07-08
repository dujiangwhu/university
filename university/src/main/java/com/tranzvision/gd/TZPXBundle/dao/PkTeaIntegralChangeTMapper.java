package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PkTeaIntegralChangeT;

public interface PkTeaIntegralChangeTMapper {
    int deleteByPrimaryKey(String tzChangeId);

    int insert(PkTeaIntegralChangeT record);

    int insertSelective(PkTeaIntegralChangeT record);

    PkTeaIntegralChangeT selectByPrimaryKey(String tzChangeId);

    int updateByPrimaryKeySelective(PkTeaIntegralChangeT record);

    int updateByPrimaryKey(PkTeaIntegralChangeT record);
}