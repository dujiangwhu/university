package com.tranzvision.gd.TZImportTplMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZImportTplMgBundle.dao.TzImpTplDfnTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 导入模板管理
 * @author shaweyet
 */
@Service("com.tranzvision.gd.TZImportTplMgBundle.service.impl.ImportTplMgImpl")
public class ImportTplMgImpl extends FrameworkImpl {
	@Autowired
	private TzImpTplDfnTMapper TzImpTplDfnTMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery sqlQuery;
	
	/*加载导入模板列表 */
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
		String[][] orderByArr = new String[][] { { "TZ_ORDER", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_TPL_ID", "TZ_TPL_NAME", "TZ_TARGET_TBL", "TZ_JAVA_CLASS", "TZ_EXCEL_TPL", "TZ_ENABLE_MAPPING","TZ_ORDER"};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
				numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("tplId", rowList[0]);
				mapList.put("tplName", rowList[1]);
				mapList.put("targetTbl", rowList[2]);
				mapList.put("javaClass", rowList[3]);
				mapList.put("excelTpl", rowList[4]);
				mapList.put("enableMapping", rowList[5]);
				mapList.put("orderNum", rowList[6]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	// 功能说明：删除导入模板定义;
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

			try {
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					//提交信息
					String strForm = actData[num];
					jacksonUtil.json2Map(strForm);

					String tplId = jacksonUtil.getString("tplId");
					if (tplId != null && !"".equals(tplId)) {
						TzImpTplDfnTMapper.deleteByPrimaryKey(tplId);
						/*模板字段表*/
						String TzImpTplFldTSql = "DELETE FROM TZ_IMP_TPL_FLD_T WHERE TZ_TPL_ID=?";
						jdbcTemplate.update(TzImpTplFldTSql, new Object[]{tplId});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg[0] = "1";
				errMsg[1] = e.toString();
				return strRet;
			}

			return strRet;
		}
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

			try {
				int num = 0;
				for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				Map<String, Object> dataMap = jacksonUtil.getMap("data");
				// 保存数据;
				
				String tplId = (String) dataMap.get("tplId");
				Integer orderNum = (Integer)dataMap.get("orderNum");
				System.out.println("orderNum"+orderNum);
				sqlQuery.update("UPDATE TZ_IMP_TPL_DFN_T SET TZ_ORDER=? WHERE TZ_TPL_ID=?",new Object[] { orderNum,tplId});
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}
