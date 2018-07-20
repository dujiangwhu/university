/**
 * 
 */
package com.tranzvision.gd.TZPxFocusBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxStuFocusTeaTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PxStuFocusTeaTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxFocusBundle.service.impl.PxFocusMgServiceImpl")
public class PxFocusMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;
	
	@Autowired
	private PxTeacherMapper pxTeacherMapper;
	
	@Autowired
	private PxStuFocusTeaTMapper pxStuFocusMapper;
	

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
System.out.println(strParams);
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "STU_OPRID", "TEA_OPRID", "TZ_FOCUS_TIME","ROW_LASTMANT_DTTM","ROW_LASTMANT_OPRID","STU_NAME", "TEA_NAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("stuOprid", rowList[0]);
					mapList.put("teaOprid", rowList[1]);
					mapList.put("tzFocusTime", rowList[2]);
					mapList.put("rowLastmantDttm", rowList[3]);
					mapList.put("rowLastmantOprid", rowList[4]);
					mapList.put("stuName", rowList[5]);
					mapList.put("teaName", rowList[6]);
		
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

	/**
	 * 删除信息
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

				String stuOprid = jacksonUtil.getString("stuOprid");
				String teaOprid = jacksonUtil.getString("teaOprid");
				if(stuOprid!=null&&teaOprid!=null){
					
					PxStuFocusTeaTKey pxStuFocusKey=new PxStuFocusTeaTKey();
					pxStuFocusKey.setStuOprid(stuOprid);
					pxStuFocusKey.setTeaOprid(teaOprid);
					pxStuFocusMapper.deleteByPrimaryKey(pxStuFocusKey);
				}
				
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("teaOprid", teaOprid);
				strRet = jacksonUtil.Map2json(mapJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	

}