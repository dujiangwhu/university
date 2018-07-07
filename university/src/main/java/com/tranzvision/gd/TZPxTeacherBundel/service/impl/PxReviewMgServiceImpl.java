/**
 * 
 */
package com.tranzvision.gd.TZPxTeacherBundel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxStuReviewMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxTeacherBundel.service.impl.PxReviewMgServiceImpl")
public class PxReviewMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;
	
	@Autowired
	private PxStuReviewMapper pxStuReviewMapper;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_REVIEW_ID", "STU_OPRID", "TEA_OPRID","TZ_REVIEW_TYPE","TZ_REVIEW_DESC","TZ_REVIEW_TIME",
					"TZ_REVIEW_STATUS","ROW_LASTMANT_DTTM","ROW_LASTMANT_OPRID","STU_NAME", "TEA_NAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("tzReviewId", rowList[0]);
					mapList.put("stuOprid", rowList[1]);
					mapList.put("teaOprid", rowList[2]);
					mapList.put("tzReviewType", rowList[3]);
					mapList.put("tzReviewDesc", rowList[4]);
					mapList.put("tzReviewTime", rowList[5]);
					mapList.put("tzReviewStatus", rowList[6]);
					mapList.put("rowLastmantDttm", rowList[7]);
					mapList.put("rowLastmantOprid", rowList[8]);
					mapList.put("stuName", rowList[9]);
					mapList.put("teaName", rowList[10]);
			        
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

				String tzReviewId = jacksonUtil.getString("tzReviewId");
				if(tzReviewId!=null){
					pxStuReviewMapper.deleteByPrimaryKey(tzReviewId);
				}
				
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("tzReviewId", tzReviewId);
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