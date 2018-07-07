package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxJgKsOrderT;
import com.tranzvision.gd.TZPXBundle.model.PxJgKsOrderTKey;

public interface PxJgKsOrderTMapper {
    int deleteByPrimaryKey(PxJgKsOrderTKey key);

    int insert(PxJgKsOrderT record);

    int insertSelective(PxJgKsOrderT record);

    PxJgKsOrderT selectByPrimaryKey(PxJgKsOrderTKey key);

    int updateByPrimaryKeySelective(PxJgKsOrderT record);

    int updateByPrimaryKey(PxJgKsOrderT record);
}