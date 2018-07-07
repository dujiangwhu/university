package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemSkinT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemSkinTKey;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemSkinTWithBLOBs;

public interface PsTzSitemSkinTMapper {
    int deleteByPrimaryKey(PsTzSitemSkinTKey key);

    int insert(PsTzSitemSkinTWithBLOBs record);

    int insertSelective(PsTzSitemSkinTWithBLOBs record);

    PsTzSitemSkinTWithBLOBs selectByPrimaryKey(PsTzSitemSkinTKey key);

    int updateByPrimaryKeySelective(PsTzSitemSkinTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzSitemSkinTWithBLOBs record);

    int updateByPrimaryKey(PsTzSitemSkinT record);
}