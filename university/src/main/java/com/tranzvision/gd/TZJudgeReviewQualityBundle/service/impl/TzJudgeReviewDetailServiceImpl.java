package com.tranzvision.gd.TZJudgeReviewQualityBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableKeys;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 材料评委评审质量查看
 * 
 * @author LuYan 2017-5-23
 *
 */

@Service("com.tranzvision.gd.TZJudgeReviewQualityBundle.service.impl.TzJudgeReviewDetailServiceImpl")

public class TzJudgeReviewDetailServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private HttpServletRequest request;
	
	
	/*材料评委列表信息*/
	@Override
	@SuppressWarnings({ "unchecked", "unused" })
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
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
			
			/*为了使用平均面试通过率进行排序*/
			/*
			//评审意见成绩项ID
			String psyjCjxId = getHardCodePoint.getHardCodePointVal("TZ_CLPS_PSYJ_CJX_ID");
			//计算为材料评议结果的评审意见值
			String psyjValue = getHardCodePoint.getHardCodePointVal("TZ_CLPS_PSYJ_VALUE");
			String[] psyjValueArr = psyjValue.trim().split(",");
			String psyjDesc = "";
			for(String psyj:psyjValueArr) {
				if(psyj!=null && !"".equals(psyj)) {
					if(!"".equals(psyjDesc)) {
						psyjDesc += "," + "'" + psyj + "'";
					} else {
						psyjDesc = "'" + psyj + "'";
					}
				}
			}
			
			
			String sql = "";
			sql = "SELECT A.TZ_CLASS_ID,B.TZ_CLASS_NAME,A.TZ_APPLY_PC_ID,C.TZ_BATCH_NAME";
			sql += " FROM PS_TZ_CLPS_PW_TBL A,PS_TZ_CLASS_INF_T B,PS_TZ_CLS_BATCH_T C";
			sql += " WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_BATCH_ID AND A.TZ_PWEI_OPRID=?";
			sql += " LIMIT ?,?";
			*/
			
			String sql = "SELECT TZ_CLASS_ID,TZ_CLASS_NAME,TZ_APPLY_PC_ID,TZ_BATCH_NAME,TZ_CLPS_NUM,TZ_MS_YLQ_NUM,TZ_AVG_MSPS FROM PS_TZ_CLPW_MSTGL_VW WHERE TZ_PWEI_OPRID=?";
			
			//排序字段
			String orderBySql = "";
			String sort = request.getParameter("sort");
			if(sort!=null && !"".equals(sort)) {
				sort = "{\"sort\":"+sort+"}";
				jacksonUtil.json2Map(sort);
				List<Map<String, String>> sortList = (List<Map<String,String>>) jacksonUtil.getList("sort");
				List<String[]> orderList = new ArrayList<String[]>();
				for(Map<String, String> sortMap : sortList) {
					String columnField = sortMap.get("property");
					String sortMethod = sortMap.get("direction");
					
					if("clpsDesc".equals(columnField)) {
						orderBySql = " TZ_CLASS_NAME " + sortMethod + ",TZ_BATCH_NAME " + sortMethod;
					}
					
					if("clpsNum".equals(columnField)) {
						orderBySql = " TZ_CLPS_NUM " + sortMethod;
					}
					
					if("tjryMsNum".equals(columnField)) {
						orderBySql = " TZ_MS_YLQ_NUM " + sortMethod;
					}
					
					if("interviewPass".equals(columnField)) {
						orderBySql = " TZ_AVG_MSPS " + sortMethod;
					}
				}
			}
			
			if(!"".equals(orderBySql)) {
				sql += " ORDER BY "+ orderBySql;
			}
 
			sql += " LIMIT ?,?";
			
			List<Map<String,Object>> listPsxx = sqlQuery.queryForList(sql, new Object[]{judgeOprid,numStart*numLimit,(numStart*numLimit+numLimit)});
			
			if(listPsxx != null) {
				
				for(Map<String, Object> mapPsxx : listPsxx) {
					String classId = mapPsxx.get("TZ_CLASS_ID") == null ? "" : mapPsxx.get("TZ_CLASS_ID").toString();
					String className = mapPsxx.get("TZ_CLASS_NAME") == null ? "" : mapPsxx.get("TZ_CLASS_NAME").toString();
					String batchId = mapPsxx.get("TZ_APPLY_PC_ID") == null ? "" : mapPsxx.get("TZ_APPLY_PC_ID").toString();
					String batchName = mapPsxx.get("TZ_BATCH_NAME") == null ? "" : mapPsxx.get("TZ_BATCH_NAME").toString();
					Integer clpsNum = mapPsxx.get("TZ_CLPS_NUM") == null ? 0 : Integer.valueOf(mapPsxx.get("TZ_CLPS_NUM").toString());
					Integer tjryMsNum = mapPsxx.get("TZ_MS_YLQ_NUM") == null ? 0 : Integer.valueOf(mapPsxx.get("TZ_MS_YLQ_NUM").toString());
					Integer interviewPass = mapPsxx.get("TZ_AVG_MSPS") == null ? 0 : Integer.valueOf(mapPsxx.get("TZ_AVG_MSPS").toString());
					
					String clpsDesc = className+batchName;
					
					
					//平均面试通过率
					//String interviewPass = "0";
					/*
					 * 面试通过率=推荐人员面试情况/材料评议结果
					 * 材料评议结果：评委每个评审批次强烈推荐和推荐进入面试的人数M
					 * 推荐人员面试情况：M人中 面试后预录取人数N
					 */
					/*
					Integer interviewPassAvg = 0;
					Integer tjryMsNum = 0;
					Integer clpsNum = 0;
					
					//材料评议结果
					sql = "SELECT COUNT(1)"; 
					sql += " FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CJX_TBL B";
					sql += " WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";
					sql += " AND A.TZ_SCORE_INS_ID=B.TZ_SCORE_INS_ID AND B.TZ_SCORE_ITEM_ID=? AND B.TZ_CJX_XLK_XXBH IN (" + psyjDesc + ")";
					
					String clpsjg = sqlQuery.queryForObject(sql,  new Object[]{classId,batchId,judgeOprid,psyjCjxId},"String");
							
					if(!"".equals(clpsjg) && clpsjg!=null && !"0".equals(clpsjg)) {
						clpsNum = Integer.valueOf(clpsjg);
					}
						
					//推荐人员面试情况
					sql = "SELECT COUNT(1)";
					sql += " FROM PS_TZ_MSJG_DR_T A , PS_TZ_FORM_WRK_T B,PS_TZ_CP_PW_KS_TBL C";
					sql += " WHERE C.TZ_CLASS_ID=? AND C.TZ_APPLY_PC_ID=? AND C.TZ_PWEI_OPRID=? AND A.TZ_MSH_ID=B.TZ_MSH_ID AND B.TZ_APP_INS_ID=C.TZ_APP_INS_ID AND A.TZ_MSJG IN ('预录取','有条件预录取')";
					
					String tjryMs = sqlQuery.queryForObject(sql,  new Object[]{classId,batchId,judgeOprid},"String");
					if(!"".equals(tjryMs) && tjryMs!=null && !"0".equals(tjryMs)) {
						tjryMsNum = Integer.valueOf(tjryMs);
					} 
						
					if(clpsNum>0){
						//interviewPass = String.valueOf((int)((Float.valueOf(tjryMsNum)/clpsNum)*100));
						interviewPassAvg = (int)((Float.valueOf(tjryMsNum)/clpsNum)*100);
						if(interviewPassAvg!=0) {
							interviewPass = interviewPassAvg+"%";
						}
					}
					*/
			
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classId", classId);
					mapList.put("batchId", batchId);
					mapList.put("judgeOprid", judgeOprid);
					mapList.put("clpsDesc", clpsDesc);
					mapList.put("clpsNum", clpsNum);
					mapList.put("tjryMsNum", tjryMsNum);
					mapList.put("interviewPass", interviewPass);
					listData.add(mapList);

				}
				
				//总数
				sql = "SELECT COUNT(1) FROM PS_TZ_CLPW_MSTGL_VW WHERE TZ_PWEI_OPRID=?";
				String total = sqlQuery.queryForObject(sql, new Object[]{judgeOprid},"String");
 				
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
