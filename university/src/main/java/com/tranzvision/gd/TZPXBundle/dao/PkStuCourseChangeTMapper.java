package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PkStuCourseChangeT;

public interface PkStuCourseChangeTMapper {
    int deleteByPrimaryKey(String tzChangeId);

    int insert(PkStuCourseChangeT record);

    int insertSelective(PkStuCourseChangeT record);

    PkStuCourseChangeT selectByPrimaryKey(String tzChangeId);

    int updateByPrimaryKeySelective(PkStuCourseChangeT record);

    int updateByPrimaryKey(PkStuCourseChangeT record);
}