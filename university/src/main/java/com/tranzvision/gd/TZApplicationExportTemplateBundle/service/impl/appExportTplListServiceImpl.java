package com.tranzvision.gd.TZApplicationExportTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationExportTemplateBundle.dao.PsTzExportTmpTMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：查询报名表导出模板列表;
 * 原PS类：TZ_GD_BMGL_DCMB_PKG:TZ_GD_TPL_LIST_CLS
 */
@Service("com.tranzvision.gd.TZApplicationExportTemplateBundle.service.impl.appExportTplListServiceImpl")
public class appExportTplListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzExportTmpTMapper PsTzExportTmpTMapper;
	
	/* 查询报名表导出模板列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] {};

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_EXPORT_TMP_ID", "TZ_EXPORT_TMP_NAME", "TZ_EXPORT_TMP_TYPE" , "TZ_TMP_TYPE_DESC", "TZ_EXP_TMP_STATUS", "TZ_APP_MODAL_ID"  };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("tplID", rowList[0]);
				mapList.put("tplName", rowList[1]);
				mapList.put("tplType", rowList[2]);
				mapList.put("tplTypeDesc", rowList[3]);
				mapList.put("tplStatus", rowList[4]);
				mapList.put("modalID", rowList[5]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	/**
	 * 删除报名表导出模版
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 流程模版编号;
				String strAppExportTplId = jacksonUtil.getString("tplID");
				
				if (strAppExportTplId != null && !"".equals(strAppExportTplId)) {
					PsTzExportTmpTMapper.deleteByPrimaryKey(strAppExportTplId);
					Object[] args = new Object[] { strAppExportTplId };
					sqlQuery.update("DELETE FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID = ?", args);
					sqlQuery.update("DELETE FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID = ?", args);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}
