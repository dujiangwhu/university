package com.tranzvision.gd.TZAutoTagsBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutoTagsBundle.service.TzAutoTagsInterface;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * 【需严格审核】
只要考生的教育经历包含下面三点的任何一点  :
1、最高学历是“大专”或“无”
2、最高学位是“无”
3、 最高教育经历的学习方式是“成人教育”、”自考”、”网络教育“、”函授”或”其他” 
 * @author zhanglang
 *
 */
@Service("com.tranzvision.gd.TZAutoTagsBundle.service.impl.TzAutoTagStrictReviewServiceImpl")
public class TzAutoTagStrictReviewServiceImpl implements TzAutoTagsInterface {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	
	@Override
	public boolean execute(String classId, Long appInsId, String oprId) {
		boolean bool = false;
		try{
			//最高学历信息项ID
			String zgxlItemIdKey = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZGXL_ITEMID_KEY");
			//最高学位信息项ID
			String zgxwItemIdKey = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZGXW_ITEMID_KEY");
			//最高教育经历学习方式
			String studyTypeItemIdKey = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZGJY_XXFS_ITEMID_KEY");
			
			//1、最高学历是“大专”或“无”
			String[] zgxlArr = zgxlItemIdKey.split("-");
			if(zgxlArr.length == 2){
				boolean conditionBool = this.isMeetCondition(appInsId, zgxlArr);
				if(conditionBool){
					return true;
				}
			}else{
				throw new TzException("最高学历hardcode【TZ_BMB_ZGXL_ITEMID_KEY】配置错误");
			}
			
			//2、最高学位是“无”
			String[] zgxwArr = zgxwItemIdKey.split("-");
			if(zgxwArr.length == 2){
				boolean conditionBool = this.isMeetCondition(appInsId, zgxwArr);
				if(conditionBool){
					return true;
				}
			}else{
				throw new TzException("最高学历hardcode【TZ_BMB_ZGXW_ITEMID_KEY】配置错误");
			}
			
			//3、 最高教育经历的学习方式是“成人教育”、”自考”、”网络教育“、”函授”或”其他” 
			String[] studyTypeArr = studyTypeItemIdKey.split("-");
			if(studyTypeArr.length == 2){
				boolean conditionBool = this.isMeetCondition(appInsId, studyTypeArr);
				if(conditionBool){
					return true;
				}
			}else{
				throw new TzException("最高学历hardcode【TZ_BMB_ZGJY_XXFS_ITEMID_KEY】配置错误");
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return bool;
	}
	
	
	
	private boolean isMeetCondition(Long appInsId, String[] keyStr){
		boolean bool = false;
		
		String itemId = keyStr[0];
		String key = keyStr[1];
		
		String whereIn = "";
		if(key != null && !"".equals(key)){
			whereIn = "'" + key.replaceAll(",", "','") + "'";
		}
		
		String existsSql = "select 'Y' from PS_TZ_APP_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?";
		if(!"".equals(whereIn)){
			existsSql = existsSql + " and TZ_APP_S_TEXT in(" + whereIn + ")";
		}
		String exists = sqlQuery.queryForObject(existsSql, new Object[]{ appInsId, itemId }, "String");
		if("Y".equals(exists)){
			bool = true;
		}
		
		return bool;
	}

}
