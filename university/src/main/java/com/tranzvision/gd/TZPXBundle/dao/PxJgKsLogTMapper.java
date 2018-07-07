package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxJgKsLogT;
import com.tranzvision.gd.TZPXBundle.model.PxJgKsLogTKey;

public interface PxJgKsLogTMapper {
    int deleteByPrimaryKey(PxJgKsLogTKey key);

    int insert(PxJgKsLogT record);

    int insertSelective(PxJgKsLogT record);

    PxJgKsLogT selectByPrimaryKey(PxJgKsLogTKey key);

    int updateByPrimaryKeySelective(PxJgKsLogT record);

    int updateByPrimaryKey(PxJgKsLogT record);
}