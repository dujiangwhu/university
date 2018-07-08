package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxStuReviewTeaT;

public interface PxStuReviewTeaTMapper {
    int deleteByPrimaryKey(String tzReviewId);

    int insert(PxStuReviewTeaT record);

    int insertSelective(PxStuReviewTeaT record);

    PxStuReviewTeaT selectByPrimaryKey(String tzReviewId);

    int updateByPrimaryKeySelective(PxStuReviewTeaT record);

    int updateByPrimaryKeyWithBLOBs(PxStuReviewTeaT record);

    int updateByPrimaryKey(PxStuReviewTeaT record);
}