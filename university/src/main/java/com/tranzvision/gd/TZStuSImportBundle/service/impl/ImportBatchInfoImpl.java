package com.tranzvision.gd.TZStuSImportBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 导入信息
 * @author shaweyet
 */
@Service("com.tranzvision.gd.TZStuSImportBundle.service.impl.ImportBatchInfoImpl")
public class ImportBatchInfoImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	
	
	/*加载导入模板字段列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try{
			
			jacksonUtil.json2Map(comParams);
			
			// 批次编号;
			String strBatchID = jacksonUtil.getString("batchId");
			// 模版编号;
			String strTplID = jacksonUtil.getString("tplId");
			
			String strJgID = jacksonUtil.getString("jgId");
	
			/*得到记录名称*/
			String getTargetRecord = "SELECT TZ_TARGET_TBL FROM TZ_IMP_TPL_DFN_T WHERE TZ_TPL_ID = ? limit 0,1";
			String targetRecord = jdbcTemplate.queryForObject(getTargetRecord, new Object[] { strTplID }, "String");
			
			String strGridTplIDSQL = "SELECT TZ_FIELD FROM TZ_IMP_TPL_FLD_T WHERE TZ_TPL_ID = ? ORDER BY TZ_SEQ";
			List<Map<String, Object>> gridColumnList = jdbcTemplate.queryForList(strGridTplIDSQL, new Object[] { strTplID });
			if (gridColumnList != null) {
				String getDataSql = "SELECT ";
				for (int i = 0; i < gridColumnList.size(); i++) {
					String sColumnID = "";
					sColumnID = (String) gridColumnList.get(i).get("TZ_FIELD");
					
					
					getDataSql = getDataSql + sColumnID + ",";
					
					
				}
				
				
				getDataSql = getDataSql + "TZ_FLAG FROM " + targetRecord + " WHERE TZ_BATCH_ID = ?";

				List<Map<String, Object>> gridDataList = jdbcTemplate.queryForList(getDataSql, new Object[] { strBatchID });
				if (gridColumnList != null) {
					for (int i = 0; i < gridDataList.size(); i++) {
						Map<String, Object> mapList = new HashMap<String, Object>();
						Map<String, Object> gridDataMap = gridDataList.get(i);
						for (int j = 0; j < gridColumnList.size(); j++) {
							String sColumnID = "";
							sColumnID = (String) gridColumnList.get(j).get("TZ_FIELD");
							mapList.put(sColumnID,gridDataMap.get(sColumnID));
						}
						String pubState = gridDataMap.get("TZ_FLAG") == null ? "":(String) gridDataMap.get("TZ_FLAG");
						if("Y".equals(pubState)){
							mapList.put("TZ_FLAG","已发布");
						}else{
							mapList.put("TZ_FLAG","未发布");
						}
						
						listData.add(mapList);
					}
				}
				mapRet.replace("total", gridDataList.size());
				mapRet.replace("root", listData);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		/*

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("batchId", rowList[0]);
				mapList.put("tplId", rowList[1]);
				mapList.put("tplName", rowList[2]);
				mapList.put("drDate", rowList[3]);
				mapList.put("drrName", rowList[4]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}
		*/

		return jacksonUtil.Map2json(mapRet);
	}
	
	//获取批次信息
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("batchId")&&
					jacksonUtil.containsKey("tplId")&&
					jacksonUtil.containsKey("jgId")) {
				// 批次编号;
				String strBatchID = jacksonUtil.getString("batchId");
				// 模版编号;
				String strTplID = jacksonUtil.getString("tplId");
				
				String strJgID = jacksonUtil.getString("jgId");
				String strTplName = "",filename="";

				// 获取模板名称;
				String sql = "SELECT TZ_TPL_NAME ,TZ_DR_FILENAME FROM PS_TZ_DRBATCH_VW WHERE TZ_BATCH_ID=? AND TZ_TPL_ID = ? AND TZ_JG_ID = ? LIMIT 0,1";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, 
						new Object[] { strBatchID,strTplID,strJgID });
				if (map != null) {
					strTplName = (String) map.get("TZ_TPL_NAME");
					filename = (String) map.get("TZ_DR_FILENAME");
					Map<String, Object> hMap = new HashMap<>();
					hMap.put("batchId", strBatchID);
					hMap.put("tplId", strTplID);
					hMap.put("tplName", strTplName);
					hMap.put("filename", filename);
					returnJsonMap.replace("formData", hMap);
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择批次";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String reString = "";
		if("tzLoadGridColumns".equals(oprType)){
			reString = this.tzLoadGridColumns(strParams, errorMsg);
		}
		/*
		if("tzLoadExpandData".equals(oprType)){
			reString = this.tzLoadExpandData(strParams, errorMsg);
		}*/
		
		return reString;
	}
	
	/* 加载配置的列表项 */
	private String tzLoadGridColumns(String comParams, String[] errorMsg) {
		// 返回值;
		ArrayList<Map<String, Object>> listData  = new ArrayList<Map<String, Object>>();

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(comParams);
			// 模版编号;
			String strTplID = jacksonUtil.getString("tplId");

			String strGridTplIDSQL = "SELECT TZ_FIELD,TZ_FIELD_NAME FROM TZ_IMP_TPL_FLD_T WHERE TZ_TPL_ID = ? ORDER BY TZ_SEQ";
			List<Map<String, Object>> gridColumnList = jdbcTemplate.queryForList(strGridTplIDSQL, new Object[] { strTplID });
			if (gridColumnList != null) {
				for (int i = 0; i < gridColumnList.size(); i++) {
					String sColumnID = "", sColumnName = "";
					sColumnID = (String) gridColumnList.get(i).get("TZ_FIELD");
					sColumnName =gridColumnList.get(i).get("TZ_FIELD_NAME")==null?"":(String)gridColumnList.get(i).get("TZ_FIELD_NAME");
					if(!"".equals(sColumnName)){
						Map<String, Object> dynamicColumnsMap = new HashMap<>();
						dynamicColumnsMap.put("dataIndex", sColumnID);
						dynamicColumnsMap.put("text",sColumnName);
						dynamicColumnsMap.put("minWidth", 150);
						if(i== gridColumnList.size() - 1){
							dynamicColumnsMap.put("flex", 2);
						}else{
							dynamicColumnsMap.put("flex", 1);
						}
						listData.add(dynamicColumnsMap);
					}
					
				}
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
				Map<String, Object> dynamicColumnsMap = new HashMap<>();
				dynamicColumnsMap.put("dataIndex", "null");
				dynamicColumnsMap.put("text","" );
				dynamicColumnsMap.put("width", 600);
				listData.add(dynamicColumnsMap);

		}
		return jacksonUtil.List2json(listData);
	}
}
