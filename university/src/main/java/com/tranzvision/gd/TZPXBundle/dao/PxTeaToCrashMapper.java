package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxTeaToCrash;
import com.tranzvision.gd.TZPXBundle.model.PxTeaToCrashKey;

public interface PxTeaToCrashMapper {
    int deleteByPrimaryKey(PxTeaToCrashKey key);

    int insert(PxTeaToCrash record);

    int insertSelective(PxTeaToCrash record);

    PxTeaToCrash selectByPrimaryKey(PxTeaToCrashKey key);

    int updateByPrimaryKeySelective(PxTeaToCrash record);

    int updateByPrimaryKey(PxTeaToCrash record);
}