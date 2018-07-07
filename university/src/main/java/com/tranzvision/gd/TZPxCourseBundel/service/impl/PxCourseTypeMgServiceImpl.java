/**
 * 
 */
package com.tranzvision.gd.TZPxCourseBundel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqComzcTbl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseTypeMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PxCourseType;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxTeacherBundel.service.impl.PxCourseTypeMgServiceImpl")
public class PxCourseTypeMgServiceImpl extends FrameworkImpl {

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
	private PxCourseTypeMapper pxCourseTypeMapper;

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
			String[] resultFldArray = { "TZ_COURSE_TYPE_ID", "TYPE_NAME", 
					"TZ_COURSE_TYPE","TZ_MAX_AGE","TZ_MIN_AGE","TYPE_DMS"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("tzCourseTypeId", rowList[0]);
					mapList.put("tzCourseTypeName", rowList[1]);
					mapList.put("tzCourseType", rowList[2]);
					mapList.put("tzMaxAge", rowList[3]);
					mapList.put("tzMinAge", rowList[4]);
					mapList.put("typeDms", rowList[5]);
			        
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

	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {

		// 返回值;
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("OPRID")) {

				// oprid;
				String str_oprid = jacksonUtil.getString("OPRID");
				// 头像地址;
				String titleImageUrlSQL = "SELECT B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A , PS_TZ_OPR_PHOTO_T B WHERE A.OPRID=? AND A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA";
				Map<String, Object> imgMap = jdbcTemplate.queryForMap(titleImageUrlSQL, new Object[] { str_oprid });
				String titleImageUrl = "";
				if (imgMap != null) {
					String tzAttAUrl = (String) imgMap.get("TZ_ATT_A_URL");
					String sysImgName = (String) imgMap.get("TZ_ATTACHSYSFILENA");
					if (tzAttAUrl != null && !"".equals(tzAttAUrl) && sysImgName != null && !"".equals(sysImgName)) {
						if (tzAttAUrl.lastIndexOf("/") + 1 == tzAttAUrl.length()) {
							titleImageUrl = tzAttAUrl + sysImgName;
						} else {
							titleImageUrl = tzAttAUrl + "/" + sysImgName;
						}
					}
				}
				
				// 头像地址;
				titleImageUrlSQL = "SELECT TZ_REALNAME,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? ";
				Map<String, Object> userMap = jdbcTemplate.queryForMap(titleImageUrlSQL, new Object[] { str_oprid });

				if (userMap == null) {
					errMsg[0] = "1";
					errMsg[1] = "不存在该用户！";
				} else {
					String name = (String) userMap.get("TZ_REALNAME");
					String phone = (String) userMap.get("TZ_MOBILE");
					
					PxTeacher pxTeacher = pxTeacherMapper.selectByPrimaryKey(str_oprid);
					if (pxTeacher == null) {
						errMsg[0] = "1";
						errMsg[1] = "不存在该用户！";
					} else {
						Map<String, Object> jsonMap2 = new HashMap<String, Object>();
						jsonMap2.put("titleImageUrl", titleImageUrl);
						jsonMap2.put("oprid", pxTeacher.getOprid());
						jsonMap2.put("name", name);
						jsonMap2.put("phone", phone);
						jsonMap2.put("sex", pxTeacher.getSex());
						jsonMap2.put("age", pxTeacher.getAge());
						jsonMap2.put("level", pxTeacher.getLevel());
						jsonMap2.put("school", pxTeacher.getSchool());
						jsonMap2.put("educationBg", pxTeacher.getEducationBg());
						jsonMap2.put("schoolAge", pxTeacher.getSchoolAge());
						jsonMap2.put("teacherCard", pxTeacher.getTeacherCard());
						jsonMap2.put("introduce", pxTeacher.getIntroduce());
						jsonMap2.put("accountType", pxTeacher.getAccountType());
						jsonMap2.put("accountNum", pxTeacher.getAccountNum());
						jsonMap2.put("score", pxTeacher.getScore());
						jsonMap2.put("qq", pxTeacher.getQq());
						jsonMap2.put("email", pxTeacher.getEmail());
						jsonMap2.put("contactor", pxTeacher.getContactor());
						jsonMap2.put("contactorPhone", pxTeacher.getContactorPhone());
						jsonMap2.put("contactorAddress", pxTeacher.getContactorAddress());
						jsonMap2.put("statu", pxTeacher.getStatu());
						jsonMap2.put("idCard", pxTeacher.getIdCard());

						returnJsonMap.replace("formData", jsonMap2);
					}
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 新增组件注册信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 组件编号;
					String tzCourseTypeId = (String) infoData.get("tzCourseTypeId");
					// 组件名称;
					String tzCourseTypeName = (String) infoData.get("tzCourseTypeName");

					PxCourseType pxCourseType=pxCourseTypeMapper.selectByPrimaryKey(tzCourseTypeId);
					// 是否已经存在;
					if (pxCourseType!=null) {
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + tzCourseTypeId + "的信息已经注册，请修改组件ID。";
						return strRet;
					}
					
					pxCourseType=new PxCourseType();
					pxCourseType.setTzCourseTypeId(tzCourseTypeId);
					pxCourseType.setTzCourseTypeName(tzCourseTypeName);
					
					int i = pxCourseTypeMapper.insert(pxCourseType);
					/*if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
					}*/
				}

				/*if ("PAGE".equals(strFlag)) {
					strRet = this.tzUpdatePageInfo(infoData, errMsg);
				}*/

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	/* 修改组件注册信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 组件编号;
					String tzCourseTypeId = (String) infoData.get("tzCourseTypeId");
					// 组件名称;
					String tzCourseTypeName = (String) infoData.get("tzCourseTypeName");
					String tzCourseType = (String) infoData.get("tzCourseType");
					Integer tzMaxAge = Integer.valueOf((String)infoData.get("tzMaxAge"));
					Integer tzMinAge = Integer.valueOf((String)infoData.get("tzMinAge"));

					PxCourseType pxCourseType=pxCourseTypeMapper.selectByPrimaryKey(tzCourseTypeId);
					// 是否已经存在;
					if (pxCourseType==null) {
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + tzCourseTypeId + "的信息不存在。";
						return strRet;
					}
					
					pxCourseType.setTzCourseTypeId(tzCourseTypeId);
					pxCourseType.setTzCourseTypeName(tzCourseTypeName);
					pxCourseType.setTzCourseType(tzCourseType);
					pxCourseType.setTzMaxAge(tzMaxAge);
					pxCourseType.setTzMinAge(tzMinAge);
					
					int i = pxCourseTypeMapper.updateByPrimaryKey(pxCourseType);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

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
				String tzCourseTypeId = jacksonUtil.getString("tzCourseTypeId");
				pxCourseTypeMapper.deleteByPrimaryKey(tzCourseTypeId);
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}