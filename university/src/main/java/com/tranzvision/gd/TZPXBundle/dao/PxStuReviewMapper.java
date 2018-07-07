package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxStuReview;

public interface PxStuReviewMapper {
    int deleteByPrimaryKey(String tzReviewId);

    int insert(PxStuReview record);

    int insertSelective(PxStuReview record);

    PxStuReview selectByPrimaryKey(String tzReviewId);

    int updateByPrimaryKeySelective(PxStuReview record);

    int updateByPrimaryKeyWithBLOBs(PxStuReview record);

    int updateByPrimaryKey(PxStuReview record);
}