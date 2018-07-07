package com.tranzvision.gd.TZPXBundle.dao;

import com.tranzvision.gd.TZPXBundle.model.PxStuFocus;
import com.tranzvision.gd.TZPXBundle.model.PxStuFocusKey;

public interface PxStuFocusMapper {
    int deleteByPrimaryKey(PxStuFocusKey key);

    int insert(PxStuFocus record);

    int insertSelective(PxStuFocus record);

    PxStuFocus selectByPrimaryKey(PxStuFocusKey key);

    int updateByPrimaryKeySelective(PxStuFocus record);

    int updateByPrimaryKey(PxStuFocus record);
}