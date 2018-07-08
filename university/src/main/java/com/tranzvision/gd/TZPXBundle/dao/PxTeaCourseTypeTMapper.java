package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxTeaCourseTypeTKey;

public interface PxTeaCourseTypeTMapper {
    int deleteByPrimaryKey(PxTeaCourseTypeTKey key);

    int insert(PxTeaCourseTypeTKey record);

    int insertSelective(PxTeaCourseTypeTKey record);
}