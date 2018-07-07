package com.tranzvision.gd.TZStuSImportBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationProcessBundle.service.impl.proDefnServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 导入模板信息
 * @author shaweyet
 */
@Service("com.tranzvision.gd.TZStuSImportBundle.service.impl.ImportBatchListImpl")
public class ImportBatchListImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
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

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "ROW_ADDED_DTTM", "DESC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID","TZ_BATCH_ID","TZ_TPL_ID", "TZ_TPL_NAME", "ROW_ADDED_DTTM", "TZ_REALNAME","TZ_DR_FILENAME"};
System.out.println(resultFldArray+" "+orderByArr);
		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,numLimit, numStart, errorMsg);
		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("jgId", rowList[0]);
				mapList.put("batchId", rowList[1]);
				mapList.put("tplId", rowList[2]);
				mapList.put("tplName", rowList[3]);
				mapList.put("drDate", rowList[4]);
				mapList.put("drrName", rowList[5]);
				mapList.put("filename", rowList[6]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String reString = "";
		if("publishBatch".equals(oprType)){
			reString = this.tzPublishBatch(strParams, errorMsg);
		}
		if("cancelBatch".equals(oprType)){
			reString = this.tzCancelBatch(strParams, errorMsg);
		}
		/*
		if("tzLoadExpandData".equals(oprType)){
			reString = this.tzLoadExpandData(strParams, errorMsg);
		}*/
		
		return reString;
	}
	
	/* 发布信息 */
	private String tzPublishBatch(String comParams, String[] errorMsg) {
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid=tzLoginServiceImpl.getLoginedManagerOrgid(request);
				jacksonUtil.json2Map(comParams);
				
				if (jacksonUtil.containsKey("batchId")&&
						jacksonUtil.containsKey("tplId")) {

					// 批次编号;
					String strBatchID = jacksonUtil.getString("batchId");
					// 模版编号;
					String strTplID = jacksonUtil.getString("tplId");
					
					/*得到记录名称*/
					String getTargetRecord = "SELECT TZ_TARGET_TBL FROM TZ_IMP_TPL_DFN_T WHERE TZ_TPL_ID = ? limit 0,1";
					String targetRecord = jdbcTemplate.queryForObject(getTargetRecord, new Object[] { strTplID }, "String");
					String updateDataSql = "UPDATE ";

					updateDataSql = updateDataSql + targetRecord +  " SET TZ_FLAG = ? WHERE TZ_BATCH_ID = ?";
					
					jdbcTemplate.update(updateDataSql, new Object[] { "Y", strBatchID });
					jdbcTemplate.update("UPDATE PS_TZ_DRBATCH_T SET ROW_LAST_DTTM=?,ROW_LAST_OPRID=? where TZ_JG_ID=? AND TZ_BATCH_ID=? AND TZ_TPL_ID=?", new Object[] { new Date(),oprid, orgid,strBatchID,strTplID });
				}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	/* 撤销信息 */
	private String tzCancelBatch(String comParams, String[] errorMsg) {
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
				jacksonUtil.json2Map(comParams);
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				String orgid=tzLoginServiceImpl.getLoginedManagerOrgid(request);
				if (jacksonUtil.containsKey("batchId")&&
						jacksonUtil.containsKey("tplId")) {

					// 批次编号;
					String strBatchID = jacksonUtil.getString("batchId");
					// 模版编号;
					String strTplID = jacksonUtil.getString("tplId");
					
					/*得到记录名称*/
					String getTargetRecord = "SELECT TZ_TARGET_TBL FROM TZ_IMP_TPL_DFN_T WHERE TZ_TPL_ID = ? limit 0,1";
					String targetRecord = jdbcTemplate.queryForObject(getTargetRecord, new Object[] { strTplID }, "String");
					String updateDataSql = "UPDATE ";

					updateDataSql = updateDataSql + targetRecord +  " SET TZ_FLAG = ? WHERE TZ_BATCH_ID = ?";

					jdbcTemplate.update(updateDataSql, new Object[] { "N", strBatchID });
					jdbcTemplate.update("UPDATE PS_TZ_DRBATCH_T SET ROW_LAST_DTTM=?,ROW_LAST_OPRID=? where TZ_JG_ID=? AND TZ_BATCH_ID=? AND TZ_TPL_ID=?", new Object[] { new Date(),oprid, orgid,strBatchID,strTplID });
				}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
}
