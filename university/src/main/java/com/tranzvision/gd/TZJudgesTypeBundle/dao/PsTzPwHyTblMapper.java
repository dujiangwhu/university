package com.tranzvision.gd.TZJudgesTypeBundle.dao;

import com.tranzvision.gd.TZJudgesTypeBundle.model.PsTzPwHyTblKey;

public interface PsTzPwHyTblMapper {
    int deleteByPrimaryKey(PsTzPwHyTblKey key);

    int insert(PsTzPwHyTblKey record);

    int insertSelective(PsTzPwHyTblKey record);
}