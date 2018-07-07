package com.tranzvision.gd.TZJudgesTypeBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author luyan 2017-7-11
 * 功能说明：评委账号评审人数详细信息
 */

@Service("com.tranzvision.gd.TZJudgesTypeBundle.service.impl.TzJudgeReviewNumServiceImpl")
public class TzJudgeReviewNumServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;

	@Override
	@SuppressWarnings({"unchecked","unused"})
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("root", listData);
		
		try {
			
			jacksonUtil.json2Map(strParams);
			
			//评委账号oprid
			String judgeOprid = jacksonUtil.getString("judgeOprid");
			
			String sql = "SELECT TZ_CLASS_ID,TZ_CLASS_NAME,TZ_APPLY_PC_ID,TZ_BATCH_NAME,TZ_REVIEW_NUM FROM PS_TZ_CLPS_PWNUM_VW WHERE TZ_PWEI_OPRID=? LIMIT ?,?";
			
			List<Map<String,Object>> listNum = sqlQuery.queryForList(sql,new Object[]{judgeOprid,numStart*numLimit,(numStart*numLimit+numLimit)});
			
			if (listNum!=null) {
				for(Map<String, Object> mapNum : listNum) {
					String classId = mapNum.get("TZ_CLASS_ID") == null ? "" : mapNum.get("TZ_CLASS_ID").toString();
					String className = mapNum.get("TZ_CLASS_NAME") == null ? "" : mapNum.get("TZ_CLASS_NAME").toString();
					String batchId = mapNum.get("TZ_APPLY_PC_ID") == null ? "" : mapNum.get("TZ_APPLY_PC_ID").toString();
					String batchName = mapNum.get("TZ_BATCH_NAME") == null ? "" : mapNum.get("TZ_BATCH_NAME").toString();
					String reviewNum = mapNum.get("TZ_REVIEW_NUM") == null ? "" : mapNum.get("TZ_REVIEW_NUM").toString();
				
					String clpsDesc = className+batchName;
					
					Map<String, Object> mapList = new HashMap<String,Object>();
					mapList.put("classId", classId);
					mapList.put("batchId", batchId);
					mapList.put("judgeOprid", judgeOprid);
					mapList.put("className",className);
					mapList.put("batchName", batchName);
					mapList.put("clpsDesc", clpsDesc);
					mapList.put("clpsNum", reviewNum);
					
					listData.add(mapList);
				}
				
				String total = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_CLPS_PWNUM_VW WHERE TZ_PWEI_OPRID=?", new Object[]{judgeOprid},"String");
				mapRet.replace("total", total);
				mapRet.replace("root", listData);
				
 			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		
		return strRet;
	}
}
