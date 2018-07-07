package com.tranzvision.gd.TZOrganizationMgBundle.dao;

import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseTWithBLOBs;

public interface PsTzJgBaseTMapper {
    int deleteByPrimaryKey(String tzJgId);

    int insert(PsTzJgBaseTWithBLOBs record);

    int insertSelective(PsTzJgBaseTWithBLOBs record);

    PsTzJgBaseTWithBLOBs selectByPrimaryKey(String tzJgId);

    int updateByPrimaryKeySelective(PsTzJgBaseTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzJgBaseTWithBLOBs record);

    int updateByPrimaryKey(PsTzJgBaseT record);
}