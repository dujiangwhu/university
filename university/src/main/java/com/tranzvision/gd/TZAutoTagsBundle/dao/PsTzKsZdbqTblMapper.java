package com.tranzvision.gd.TZAutoTagsBundle.dao;

import com.tranzvision.gd.TZAutoTagsBundle.model.PsTzKsZdbqTblKey;

public interface PsTzKsZdbqTblMapper {
    int deleteByPrimaryKey(PsTzKsZdbqTblKey key);

    int insert(PsTzKsZdbqTblKey record);

    int insertSelective(PsTzKsZdbqTblKey record);
}