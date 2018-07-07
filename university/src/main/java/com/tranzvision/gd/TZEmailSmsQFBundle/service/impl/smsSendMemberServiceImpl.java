package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.imp.smsSendMemberServiceImpl")
public class smsSendMemberServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 邮件历史查看 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{"TZ_FS_DT", "ASC" }};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EML_SMS_TASK_ID", "TZ_RWSL_ID", "TZ_AUD_XM", "TZ_SJ_HM", "TZ_FSSJ_DT", "TZ_FS_ZTMS"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				@SuppressWarnings("unchecked")
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("taskId", rowList[0]);
					mapList.put("rwInsId", rowList[1]);
					mapList.put("name", rowList[2]);
					mapList.put("addresseePhone", rowList[3]);
					mapList.put("sendDt", rowList[4]);
					mapList.put("sendStatus", rowList[5]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	
	//查询错误原因;
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		//返回值;
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("errorType", "");
		returnMap.put("errorMsg", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		
		String taskId = jacksonUtil.getString("taskId");
		String rwInsId = jacksonUtil.getString("rwInsId");
		
		Map<String, Object> map = jdbcTemplate.queryForMap("SELECT TZ_YJCW_LX,TZ_RZJL_MS FROM PS_TZ_YJFSRIZH_TBL WHERE TZ_EML_SMS_TASK_ID=? AND TZ_RWSL_ID=?",new Object[]{taskId,rwInsId});
		if(map != null){
			String errType = map.get("TZ_YJCW_LX") == null ? "" : (String)map.get("TZ_YJCW_LX");
			String errMessage = map.get("TZ_RZJL_MS") == null ? "" : (String)map.get("TZ_RZJL_MS");
			returnMap.replace("errorType", errType);
			returnMap.replace("errorMsg", errMessage);
		}
		
		return jacksonUtil.Map2json(returnMap);
	}
}
