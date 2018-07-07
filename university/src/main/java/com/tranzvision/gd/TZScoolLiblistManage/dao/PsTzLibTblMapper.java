package com.tranzvision.gd.TZScoolLiblistManage.dao;

import com.tranzvision.gd.TZScoolLiblistManage.model.PsTzLibTbl;

public interface PsTzLibTblMapper {
    int deleteByPrimaryKey(String tzJgId);

    int insert(PsTzLibTbl record);

    int insertSelective(PsTzLibTbl record);

    PsTzLibTbl selectByPrimaryKey(String tzJgId);

    int updateByPrimaryKeySelective(PsTzLibTbl record);

    int updateByPrimaryKey(PsTzLibTbl record);
}