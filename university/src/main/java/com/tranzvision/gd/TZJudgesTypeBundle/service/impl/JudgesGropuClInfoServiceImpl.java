package com.tranzvision.gd.TZJudgesTypeBundle.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZJudgesTypeBundle.dao.PsTzClpsGrTblMapper;
import com.tranzvision.gd.TZJudgesTypeBundle.model.PsTzClpsGrTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author xzx; 
 * 功能说明：材料评委组定义;
 * 原PS类：
 */
@Service("com.tranzvision.gd.TZJudgesTypeBundle.service.impl.JudgesGropuClInfoServiceImpl")
public class JudgesGropuClInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzClpsGrTblMapper PsTzClpsGrTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	/* 获取指定评审类型下的评审组 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		//{"batchID":"45","classID":"76","oprID":"TZ_14052"}
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(comParams);
			// 评委类型ID;
			String strJugtypId = jacksonUtil.getString("selValue");
			// 评审类型下的评审组 ;
			String sql1="select TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME,TZ_JUGTYP_NAME from PS_TZ_CLPS_GR_VW where TZ_JUGTYP_ID=?";
			List<Map<String, Object>> tagList = jdbcTemplate.queryForList(sql1,new Object[]{strJugtypId});
		    /*if(tagList != null){
		    	for(int i = 0; i < tagList.size(); i++){
		    		strBatchID = (String)tagList.get(i).get("TZ_BATCH_ID");
		    		strBatchName = (String)tagList.get(i).get("TZ_BATCH_NAME");
		    		// strComStoreMap.put("classID", strClassID);
		    		// strComStoreMap.put("oprID", strOprID);
		    		strComStoreMap.put("batchID", strBatchID);
		    		strComStoreMap.put("batchName", strBatchName);
		    		listData.add(strComStoreMap);
		    	}
		    }
			*/

			// 总数;
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_CLPS_GR_VW WHERE TZ_JUGTYP_ID=?";
			int numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { strJugtypId }, "Integer");
			mapRet.replace("total", numTotal);
//			mapRet.replace("root", listData);
			mapRet.replace("root", tagList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 获取材料评委组信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("jugGroupId")) {
				// 材料评审组ID;
				String jugGroupId = jacksonUtil.getString("jugGroupId");
				PsTzClpsGrTbl psTzClpsGrTbl = PsTzClpsGrTblMapper.selectByPrimaryKey(jugGroupId);
				if (psTzClpsGrTbl != null) {
					
					returnJsonMap.put("jgID", psTzClpsGrTbl.getTzJgId());
					returnJsonMap.put("jugGroupId", psTzClpsGrTbl.getTzClpsGrId());
					returnJsonMap.put("jugGroupType", psTzClpsGrTbl.getTzJugtypId());
					returnJsonMap.put("jugGroupName", psTzClpsGrTbl.getTzClpsGrName());
					
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该评委组数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该评委组数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	// 新增材料评委组定义;
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("jugGroupId", "");
//		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 材料评委组编号;
				String jugGroupId = (String) infoData.get("jugGroupId");
				// 评委组类型;
				String jugGroupType = (String) infoData.get("jugGroupType");
				// 材料评委组名称;
				String jugGroupName = (String) infoData.get("jugGroupName");
				PsTzClpsGrTbl psTzClpsGrTbl;
				
				if ("NEXT".equals(jugGroupId)) {
//					strJugTypeId = "PRJ_TYPE_" + String.valueOf(getSeqNum.getSeqNum("TZ_PRJ_TYPE_T", "TZ_PRJ_TYPE_ID"));
					jugGroupId = String.valueOf(getSeqNum.getSeqNum("TZ_CLPS_GR_TBL", "TZ_CLPS_GR_ID"));
					psTzClpsGrTbl = new PsTzClpsGrTbl();
					psTzClpsGrTbl.setTzJgId(orgid);
					psTzClpsGrTbl.setTzClpsGrId(jugGroupId);
					psTzClpsGrTbl.setTzJugtypId(jugGroupType);
					psTzClpsGrTbl.setTzClpsGrName(jugGroupName);
					int i = PsTzClpsGrTblMapper.insert(psTzClpsGrTbl);
					if (i > 0) {
						returnJsonMap.replace("jugGroupId", jugGroupId);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "评委组信息保存失败";
					}
				} else{
					String sql = "select COUNT(1) from PS_TZ_CLPS_GR_TBL WHERE TZ_CLPS_GR_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { jugGroupId }, "Integer");
					if (count > 0) {
						psTzClpsGrTbl = new PsTzClpsGrTbl();
						psTzClpsGrTbl.setTzJgId(orgid);
						psTzClpsGrTbl.setTzClpsGrId(jugGroupId);
						psTzClpsGrTbl.setTzJugtypId(jugGroupType);
						psTzClpsGrTbl.setTzClpsGrName(jugGroupName);
						int i = PsTzClpsGrTblMapper.updateByPrimaryKey(psTzClpsGrTbl);
						if (i > 0) {
							returnJsonMap.replace("jugGroupId", jugGroupId);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "评委组信息保存失败";
						}
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	// 新增项目分类定义;
/*	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("prjID", "");
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 项目分类编号;
				String strProTypeId = (String) infoData.get("proTypeId");
				// 项目分类名称;
				String strProTypeName = (String) infoData.get("proTypeName");
				// 项目分类描述;
				String strProTypeDesc = (String) infoData.get("proTypeDesc");
				//项目分类有效状态
				String strProTypeStatus = (String) infoData.get("proTypeStatus");
				PsTzPrjTypeT psTzPrjTypeT;
				
				if ("NEXT".equals(strProTypeId)) {
					strProTypeId = "PRJ_TYPE_" + String.valueOf(getSeqNum.getSeqNum("TZ_PRJ_TYPE_T", "TZ_PRJ_TYPE_ID"));
					psTzPrjTypeT = new PsTzPrjTypeT();
					psTzPrjTypeT.setTzJgId(orgid);
					psTzPrjTypeT.setTzPrjTypeId(strProTypeId);
					psTzPrjTypeT.setTzPrjTypeName(strProTypeName);
					psTzPrjTypeT.setTzPrjTypeDesc(strProTypeDesc);
					psTzPrjTypeT.setTzPrjTypeStatus(strProTypeStatus);
					psTzPrjTypeT.setRowAddedDttm(new Date());
					psTzPrjTypeT.setRowAddedOprid(oprid);
					psTzPrjTypeT.setRowLastmantDttm(new Date());
					psTzPrjTypeT.setRowLastmantOprid(oprid);
					int i = PsTzPrjTypeTMapper.insert(psTzPrjTypeT);
					if (i > 0) {
						returnJsonMap.replace("prjID", strProTypeId);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "项目分类信息保存失败";
					}
				} else{
					String sql = "select COUNT(1) from PS_TZ_PRJ_TYPE_T WHERE TZ_PRJ_TYPE_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { strProTypeId }, "Integer");
					if (count > 0) {
						psTzPrjTypeT = new PsTzPrjTypeT();
						psTzPrjTypeT.setTzJgId(orgid);
						psTzPrjTypeT.setTzPrjTypeId(strProTypeId);
						psTzPrjTypeT.setTzPrjTypeName(strProTypeName);
						psTzPrjTypeT.setTzPrjTypeDesc(strProTypeDesc);
						psTzPrjTypeT.setTzPrjTypeStatus(strProTypeStatus);
						psTzPrjTypeT.setRowAddedDttm(new Date());
						psTzPrjTypeT.setRowAddedOprid(oprid);
						psTzPrjTypeT.setRowLastmantDttm(new Date());
						psTzPrjTypeT.setRowLastmantOprid(oprid);
						int i = PsTzPrjTypeTMapper.updateByPrimaryKeySelective(psTzPrjTypeT);
						if (i > 0) {
							returnJsonMap.replace("prjID", strProTypeId);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "项目分类信息保存失败";
						}
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}*/
}
