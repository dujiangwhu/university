package com.tranzvision.gd.TZAutoTagsBundle.dao;

import com.tranzvision.gd.TZAutoTagsBundle.model.PsTzZdbqDfnTbl;

public interface PsTzZdbqDfnTblMapper {
    int deleteByPrimaryKey(String tzZdbqId);

    int insert(PsTzZdbqDfnTbl record);

    int insertSelective(PsTzZdbqDfnTbl record);

    PsTzZdbqDfnTbl selectByPrimaryKey(String tzZdbqId);

    int updateByPrimaryKeySelective(PsTzZdbqDfnTbl record);

    int updateByPrimaryKey(PsTzZdbqDfnTbl record);
}