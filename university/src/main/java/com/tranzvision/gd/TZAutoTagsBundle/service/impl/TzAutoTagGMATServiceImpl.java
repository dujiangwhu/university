package com.tranzvision.gd.TZAutoTagsBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutoTagsBundle.service.TzAutoTagsInterface;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 【GMAT】
700分以上提供了正式成绩单的同学
 * @author zhanglang
 *
 */
@Service("com.tranzvision.gd.TZAutoTagsBundle.service.impl.TzAutoTagGMATServiceImpl")
public class TzAutoTagGMATServiceImpl implements TzAutoTagsInterface {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	
	@Override
	public boolean execute(String classId, Long appInsId, String oprId) {
		boolean bool = false;
		try{
			//英语水平控件itemid
			String engLevelItemId = getHardCodePoint.getHardCodePointVal("TZ_BMB_ENGLEVEL_ITEM_ID");
			
			if(engLevelItemId != null && !"".equals(engLevelItemId)){
				//考试名称的编号;
				String engKsTypeBh = engLevelItemId + "exam_type";
				//考试成绩编号;
				String engKsCjBh = engLevelItemId + "exam_score";
				//成绩单扫描件编号
				String scanImgBh = engLevelItemId + "exam_upload";
				
				String GMATsql = "select TZ_XXX_BH from PS_TZ_APP_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH like ? and TZ_APP_S_TEXT='GMAT'";
				List<Map<String,Object>> GMATList = sqlQuery.queryForList(GMATsql, new Object[]{ appInsId, engKsTypeBh+"%"});
				if(GMATList != null && GMATList.size() > 0){
					for(Map<String,Object> GMATMap: GMATList){
						String GMATItemID = GMATMap.get("TZ_XXX_BH").toString();
						String suffix = GMATItemID.substring(engKsTypeBh.length());
						
						//查询GMAT成绩
						String scoreSql = "select TZ_APP_S_TEXT from PS_TZ_APP_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?";
						Integer score = sqlQuery.queryForObject(scoreSql, new Object[]{ appInsId, engKsCjBh+suffix }, "Integer");
						
						//查询成绩单扫描件
						String scanImgSql = "select 'Y' from PS_TZ_FORM_ATT_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?";
						String hasScanImg = sqlQuery.queryForObject(scanImgSql, new Object[]{ appInsId, scanImgBh+suffix }, "String");
						
						//700分以上提供了正式成绩单的同学
						if(score != null && score > 700 
								&& "Y".equals(hasScanImg)){
							bool = true;
							break;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return bool;
	}

}
