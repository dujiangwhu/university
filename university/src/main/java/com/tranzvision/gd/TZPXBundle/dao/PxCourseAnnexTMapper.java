package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxCourseAnnexT;
import com.tranzvision.gd.TZPXBundle.model.PxCourseAnnexTKey;

public interface PxCourseAnnexTMapper {
    int deleteByPrimaryKey(PxCourseAnnexTKey key);

    int insert(PxCourseAnnexT record);

    int insertSelective(PxCourseAnnexT record);

    PxCourseAnnexT selectByPrimaryKey(PxCourseAnnexTKey key);

    int updateByPrimaryKeySelective(PxCourseAnnexT record);

    int updateByPrimaryKey(PxCourseAnnexT record);
}