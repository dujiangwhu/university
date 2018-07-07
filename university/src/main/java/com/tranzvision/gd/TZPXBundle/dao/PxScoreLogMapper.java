package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxScoreLog;
import com.tranzvision.gd.TZPXBundle.model.PxScoreLogKey;

public interface PxScoreLogMapper {
    int deleteByPrimaryKey(PxScoreLogKey key);

    int insert(PxScoreLog record);

    int insertSelective(PxScoreLog record);

    PxScoreLog selectByPrimaryKey(PxScoreLogKey key);

    int updateByPrimaryKeySelective(PxScoreLog record);

    int updateByPrimaryKey(PxScoreLog record);
}