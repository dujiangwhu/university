/**
 * 
 */
package com.tranzvision.gd.TZPxTeacherBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeaCourseTypeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PxTeaCourseTypeTKey;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxTeacherBundle.service.impl.PxTeaCourseTypeMgServiceImpl")
public class PxTeaCourseTypeMgServiceImpl extends FrameworkImpl {

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
	private PxTeaCourseTypeTMapper pxTeaCourseTypeMapper;

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
			String[] resultFldArray = { "OPRID", "TZ_REALNAME", "TZ_COURSE_TYPE_ID","COURSE_TYPE_NAME","TYPE_DMS"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("oprid", rowList[0]);
					mapList.put("name", rowList[1]);
					mapList.put("tzCourseTypeId", rowList[2]);
					mapList.put("typeName", rowList[3]);
					mapList.put("typeDms", rowList[4]);
					//mapList.put("score", rowList[5]);
					//mapList.put("statu", rowList[6]);
			        
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
	/* 修改组件注册信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		
		System.out.println("to updata");
		String strRet = "{}";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				//Map<String, Object> infoData  = jacksonUtil.getMap("update");
				String oprid=jacksonUtil.getString("oprid");
				System.out.println(oprid);
				/*if(oprid==null){
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}else{
					PxTeacher pxTeacher=pxTeacherMapper.selectByPrimaryKey(oprid);
					if(pxTeacher==null){
						errMsg[0] = "1";
						errMsg[1] = "用户不存在！";
					}else{
						pxTeacher.setSex(jacksonUtil.getString("sex"));
						pxTeacher.setAge(jacksonUtil.getInt("age"));
						pxTeacher.setLevel(jacksonUtil.getString("level"));
						pxTeacher.setSchool(jacksonUtil.getString("school"));
						pxTeacher.setEducationBg(jacksonUtil.getString("educationBg"));
						pxTeacher.setSchoolAge(jacksonUtil.getInt("schoolAge"));
						pxTeacher.setTeacherCard(jacksonUtil.getString("teacherCard"));
						pxTeacher.setIntroduce(jacksonUtil.getString("introduce"));
						pxTeacher.setAccountType(jacksonUtil.getString("accountType"));
						pxTeacher.setAccountNum(jacksonUtil.getString("accountNum"));
						pxTeacher.setScore(jacksonUtil.getInt("score"));
						pxTeacher.setQq(jacksonUtil.getString("qq"));
						pxTeacher.setEmail(jacksonUtil.getString("email"));
						pxTeacher.setContactor(jacksonUtil.getString("contactor"));
						pxTeacher.setContactorPhone(jacksonUtil.getString("contactorPhone"));
						pxTeacher.setContactorAddress(jacksonUtil.getString("contactorAddress"));
						pxTeacher.setStatu(jacksonUtil.getString("statu"));
						pxTeacher.setIdCard(jacksonUtil.getString("idCard"));
						
						pxTeacherMapper.updateByPrimaryKey(pxTeacher);
					}					
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
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
				String oprid = jacksonUtil.getString("oprid");
				String tzCourseTypeId = jacksonUtil.getString("tzCourseTypeId");
				PxTeaCourseTypeTKey key=new PxTeaCourseTypeTKey();
				key.setOprid(oprid);
				key.setTzCourseTypeId(tzCourseTypeId);
				pxTeaCourseTypeMapper.deleteByPrimaryKey(key);
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	/* 新增用户账号信息 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String oprid = jacksonUtil.getString("oprid");
				String tzCourseTypeId = jacksonUtil.getString("tzCourseTypeId");
				//String typeName = jacksonUtil.getString("typeName");
				
				
				PxTeaCourseTypeTKey key=new PxTeaCourseTypeTKey();
				key.setOprid(oprid);
				key.setTzCourseTypeId(tzCourseTypeId);
				// 查看是否已经存在;
				int isExistNum = 0;
				String isExistSQL = "SELECT COUNT(1) FROM PX_TEA_COURSE_TYPE_T WHERE OPRID=? AND TZ_COURSE_TYPE_ID=? ";
				isExistNum = jdbcTemplate.queryForObject(isExistSQL, new Object[] { oprid ,tzCourseTypeId }, "Integer");
				if (isExistNum > 0) {
					errMsg[0] = "1";
					errMsg[1] = "课程类型编号为：" + tzCourseTypeId + "的信息已经存在。";
					return strRet;
				}else{
					int i=pxTeaCourseTypeMapper.insertSelective(key);
				}
				
				

					

						
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}