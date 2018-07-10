package com.tranzvision.gd.TZPxCourseBundel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqComzcTbl;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseTMapper;
import com.tranzvision.gd.TZPXBundle.model.PxCourse;
import com.tranzvision.gd.TZPXBundle.model.PxCourseT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 功能说明：功能组件注册管理相关类
 * @author tang
 * 2015-10-9
 * ps类：TZ_GD_COMREGMG_PKG:TZ_GD_COMREGMG_CLS
 */
@Service("com.tranzvision.gd.TZPxCourseBundel.service.impl.PxCourseMgServiceImpl")
public class PxCourseMgServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PxCourseTMapper pxCourseMapper;
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询组件注册管理列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_COURSE_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_COURSE_ID", "TZ_COURSE_TYPE_ID", "TZ_COURSE_NUMBER",
					"TZ_COURSE_NAME", "TZ_COURSE_DESC","TYPE_NAME","TYPE_DMS" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("tzCourseId", rowList[0]);
					mapList.put("tzCourseTypeId", rowList[1]);
					mapList.put("tzCourseNumber", rowList[2]);
					mapList.put("tzCourseName", rowList[3]);
					mapList.put("tzCourseDesc", rowList[4]);
					mapList.put("typeName", rowList[5]);
					mapList.put("typeDms", rowList[6]);
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 获取组件注册信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("tzCourseId")) {
				String strComID = jacksonUtil.getString("tzCourseId");
				if (strComID != null && !"".equals(strComID)) {

					PxCourseT pxCourse = pxCourseMapper.selectByPrimaryKey(strComID);
					if (pxCourse != null) {
						// 组件注册信息;
						Map<String, Object> jsonMap = new HashMap<>();
						jsonMap.put("tzCourseId", pxCourse.getTzCourseId());
						jsonMap.put("tzCourseTypeId", pxCourse.getTzCourseTypeId());
						jsonMap.put("tzCourseNumber", pxCourse.getTzCourseNumber());
						jsonMap.put("tzCourseName", pxCourse.getTzCourseName());
						jsonMap.put("tzCourseDesc", pxCourse.getTzCourseDesc());
						returnJsonMap.replace("formData", jsonMap);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "无法获取组件信息";
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取组件信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 删除组件注册信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;
				String tzCourseId = jacksonUtil.getString("tzCourseId");

				//删除课程
				pxCourseMapper.deleteByPrimaryKey(tzCourseId);
				//删除课程附件
				String comPageSql = "DELETE FROM PX_COURSE_ANNEX_T WHERE TZ_COURSE_ID=?";
				jdbcTemplate.update(comPageSql,new Object[]{tzCourseId});		
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

}