/**
 * 
 */
package com.tranzvision.gd.TZPxTeacherBundel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxScheduleCancelTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeaScheduleTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PxScheduleCancelT;
import com.tranzvision.gd.TZPXBundle.model.PxTeaScheduleT;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxTeacherBundel.service.impl.PxScheduleMgServiceImpl")
public class PxScheduleMgServiceImpl extends FrameworkImpl {

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
	private PxScheduleCancelTMapper pxScheduleCancelMapper;
	
	@Autowired
	private PxTeaScheduleTMapper pxTeaScheduleMapper;

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
			String[] resultFldArray = { "TZ_SCHEDULE_ID", "OPRID", 
					"TZ_COURSE_TYPE_ID","TZ_COURSE_ID","TZ_APP_STATUS",
					"TZ_CLASS_START_TIME","TZ_CLASS_END_TIME",
					"TZ_SCHEDULE_TYPE","TZ_SCHEDULE_DATE","COURSE_TYPE_NAME","TZ_COURSE_NAME","TEA_NAME","TEA_PHONE","ROW_LASTMANT_DTTM"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("tzScheduleId", rowList[0]);
					mapList.put("oprid", rowList[1]);
					mapList.put("tzCourseTypeId", rowList[2]);
					mapList.put("tzCourseId", rowList[3]);
					mapList.put("tzAppStatus", rowList[4]);
					mapList.put("tzClassStartTime", rowList[5]);
					mapList.put("tzClassEndTime", rowList[6]);
					mapList.put("tzScheduleType", rowList[7]);
					mapList.put("tzScheduleDate", rowList[8]);
					mapList.put("courseTypeName", rowList[9]);
					mapList.put("tzCourseName", rowList[10]);
					mapList.put("teaName", rowList[11]);
					mapList.put("teaPhone", rowList[12]);
					mapList.put("rowLastmant", rowList[13]);
			        
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
		System.out.println("tzUpdate");
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
				String tzScheduleId=jacksonUtil.getString("tzScheduleId");
				String cancelReason=jacksonUtil.getString("cancelReason");
				String oprid=jacksonUtil.getString("oprid");
				System.out.println(tzScheduleId+":"+cancelReason+":"+oprid);
				if(tzScheduleId==null){
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}else{
					PxTeaScheduleT pxTeaSchedule=pxTeaScheduleMapper.selectByPrimaryKey(tzScheduleId);
					if(pxTeaSchedule==null){
						errMsg[0] = "1";
						errMsg[1] = "用户不存在！";
					}else{
						pxTeaSchedule.setTzScheduleType("B");
						pxTeaScheduleMapper.updateByPrimaryKey(pxTeaSchedule);
						
						PxScheduleCancelT pxScheduleCancel=new PxScheduleCancelT();
						pxScheduleCancel.setTzScheduleId(tzScheduleId);
						pxScheduleCancel.setOprid(pxTeaSchedule.getOprid());
						pxScheduleCancel.setTzCancelDesc(cancelReason);
						pxScheduleCancel.setTzCancelTime(new Date());
						//只取消
						if(pxTeaSchedule.getTzAppStatus()=="A"){
							
						}else{
							//pxTeaSchedule.setTzScheduleType("B");
							//pxTeaSchedule.setOprid(oprid);
							//pxTeaScheduleMapper.updateByPrimaryKey(pxTeaSchedule);
							String newTzScheduleId=new Date().getTime()+"";
							pxTeaSchedule.setTzScheduleId(newTzScheduleId);
							pxTeaSchedule.setOprid(oprid);
							pxTeaScheduleMapper.insert(pxTeaSchedule);
							
							pxScheduleCancel.setNewOprid(oprid);
							pxScheduleCancel.setNewTzScheduleId(newTzScheduleId);
							
						}
						pxScheduleCancelMapper.insert(pxScheduleCancel);
					}					
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