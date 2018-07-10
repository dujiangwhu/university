/**
 * 
 */
package com.tranzvision.gd.TZPxCourseTimeMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxJgKsLogTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxCourseTimeMgBundle.service.impl.PxCourseTimeMgServiceImpl")
public class PxCourseTimeMgServiceImpl extends FrameworkImpl {

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
	private PxJgKsLogTMapper pxJgKsLogTMapper;
	

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
			String[] resultFldArray = { "TZ_JG_ID", "TZ_LOG_INS_ID", "TZ_KS_MODIFY_TYPE",
					"TZ_TIMECARD_BEFORE","TZ_TIMECARD_AFTER","TZ_TIMECARD_MODIFY","OPRID"
					,"ROW_LASTMANT_DTTM"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("tzJgId", rowList[0]);
					mapList.put("tzLogInsId", rowList[1]);
					mapList.put("tzKsModifyType", rowList[2]);
					mapList.put("tzTimecardBefore", rowList[3]);
					mapList.put("tzTimecardAfter", rowList[4]);
					mapList.put("tzTimecardModify", rowList[5]);
					mapList.put("oprid", rowList[6]);
					mapList.put("rowLastmantDttm", rowList[7]);
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

}