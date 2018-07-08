package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PkStuAppCourseT;
import com.tranzvision.gd.TZPXBundle.model.PkStuAppCourseTKey;

public interface PkStuAppCourseTMapper {
    int deleteByPrimaryKey(PkStuAppCourseTKey key);

    int insert(PkStuAppCourseT record);

    int insertSelective(PkStuAppCourseT record);

    PkStuAppCourseT selectByPrimaryKey(PkStuAppCourseTKey key);

    int updateByPrimaryKeySelective(PkStuAppCourseT record);

    int updateByPrimaryKey(PkStuAppCourseT record);
}