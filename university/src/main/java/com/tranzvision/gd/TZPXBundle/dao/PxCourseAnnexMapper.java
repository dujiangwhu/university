package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxCourseAnnex;
import com.tranzvision.gd.TZPXBundle.model.PxCourseAnnexKey;

public interface PxCourseAnnexMapper {
    int deleteByPrimaryKey(PxCourseAnnexKey key);

    int insert(PxCourseAnnex record);

    int insertSelective(PxCourseAnnex record);

    PxCourseAnnex selectByPrimaryKey(PxCourseAnnexKey key);

    int updateByPrimaryKeySelective(PxCourseAnnex record);

    int updateByPrimaryKey(PxCourseAnnex record);
}