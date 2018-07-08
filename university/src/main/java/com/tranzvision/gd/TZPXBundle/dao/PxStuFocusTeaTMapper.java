package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxStuFocusTeaT;
import com.tranzvision.gd.TZPXBundle.model.PxStuFocusTeaTKey;

public interface PxStuFocusTeaTMapper {
    int deleteByPrimaryKey(PxStuFocusTeaTKey key);

    int insert(PxStuFocusTeaT record);

    int insertSelective(PxStuFocusTeaT record);

    PxStuFocusTeaT selectByPrimaryKey(PxStuFocusTeaTKey key);

    int updateByPrimaryKeySelective(PxStuFocusTeaT record);

    int updateByPrimaryKey(PxStuFocusTeaT record);
}