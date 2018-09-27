/**
 * 
 */
package com.tranzvision.gd.TZPxTeacherBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxTeacherBundle.service.impl.PxTeacherMgServiceImpl")
public class PxTeacherMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;
	
	@Autowired
	private PxTeacherMapper pxTeacherMapper;
	
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;

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
			String[] resultFldArray = { "OPRID", "TZ_REALNAME", "TZ_SEX_VALUE","PX_TEACHER_LEVEL","TZ_MOBILE","SCORE","PX_TEACHER_STATU"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("oprid", rowList[0]);
					mapList.put("name", rowList[1]);
					mapList.put("sex", rowList[2]);
					mapList.put("level", rowList[3]);
					mapList.put("phone", rowList[4]);
					mapList.put("score", rowList[5]);
					mapList.put("statu", rowList[6]);
			        
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
				
				//教师证件
				String terCardSql = "SELECT TZ_ATTACHSYSFILENA,TZ_ATTACHFILE_NAME,TZ_ATT_P_URL,TZ_ATT_A_URL FROM PX_TEA_CERT_T WHERE OPRID = ?";
				Map<String, Object> teaCardMap = jdbcTemplate.queryForMap(terCardSql, new Object[] { str_oprid });
				

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
						
						if(teaCardMap!=null){
							jsonMap2.put("teacherCardFileName", teaCardMap.get("TZ_ATTACHFILE_NAME"));
							jsonMap2.put("teacherCardSysFileName", teaCardMap.get("TZ_ATTACHSYSFILENA"));
							jsonMap2.put("teacherCardUrl", teaCardMap.get("TZ_ATT_A_URL"));
							jsonMap2.put("teacherCardPath", teaCardMap.get("TZ_ATT_P_URL"));
						}else{
							jsonMap2.put("teacherCardFileName", "");
							jsonMap2.put("teacherCardSysFileName", "");
							jsonMap2.put("teacherCardUrl", "");
							jsonMap2.put("teacherCardPath", "");
						}

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
				//System.out.println(jacksonUtil.getString("sex"));
				System.out.println(jacksonUtil.getString("teacherCardSysFileName"));
				if(oprid==null){
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
						
						
						/*添加教师注册信息表*/
						PsTzRegUserT psTzRegUserT = psTzRegUserTMapper.selectByPrimaryKey(oprid);
						if(psTzRegUserT!=null){
							psTzRegUserT.setTzRealname(jacksonUtil.getString("name"));
							psTzRegUserT.setTzGender(jacksonUtil.getString("sex"));
							psTzRegUserT.setNationalId(jacksonUtil.getString("idCard"));
							psTzRegUserT.setTzHighestEdu(jacksonUtil.getString("educationBg"));
							psTzRegUserT.setTzComment1(String.valueOf(jacksonUtil.getString("age")));
							psTzRegUserT.setTzComment2(String.valueOf(jacksonUtil.getString("schoolAge")));
							psTzRegUserT.setTzComment3(jacksonUtil.getString("qq"));
							psTzRegUserT.setTzComment4(jacksonUtil.getString("contactor"));
							psTzRegUserT.setTzComment5(jacksonUtil.getString("contactorPhone"));
							psTzRegUserT.setTzComment6(jacksonUtil.getString("contactorAddress"));
							psTzRegUserT.setTzComment7(jacksonUtil.getString("teacherCard"));
							psTzRegUserT.setTzComment8(jacksonUtil.getString("introduce"));
							psTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
						}else{
							psTzRegUserT = new PsTzRegUserT();
							psTzRegUserT.setOprid(oprid);
							psTzRegUserT.setTzRealname(jacksonUtil.getString("name"));
							psTzRegUserT.setTzGender(jacksonUtil.getString("sex"));
							psTzRegUserT.setNationalId(jacksonUtil.getString("idCard"));
							psTzRegUserT.setTzHighestEdu(jacksonUtil.getString("educationBg"));
							psTzRegUserT.setTzComment1(String.valueOf(jacksonUtil.getString("age")));
							psTzRegUserT.setTzComment2(String.valueOf(jacksonUtil.getString("schoolAge")));
							psTzRegUserT.setTzComment3(jacksonUtil.getString("qq"));
							psTzRegUserT.setTzComment4(jacksonUtil.getString("contactor"));
							psTzRegUserT.setTzComment5(jacksonUtil.getString("contactorPhone"));
							psTzRegUserT.setTzComment6(jacksonUtil.getString("contactorAddress"));
							psTzRegUserT.setTzComment7(jacksonUtil.getString("teacherCard"));
							psTzRegUserT.setTzComment8(jacksonUtil.getString("introduce"));
							psTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
						}
						
						String filename = jacksonUtil.getString("teacherCardFileName")==null?"":jacksonUtil.getString("teacherCardFileName");
						String sysfilename = jacksonUtil.getString("teacherCardSysFileName")==null?"":jacksonUtil.getString("teacherCardSysFileName");
						String pUrl = jacksonUtil.getString("teacherCardPath")==null?"":jacksonUtil.getString("teacherCardPath");
						String aUrl = jacksonUtil.getString("teacherCardUrl")==null?"":jacksonUtil.getString("teacherCardUrl");
						System.out.println("文件名："+filename);
						System.out.println("文件名："+sysfilename);
						System.out.println("文件名："+pUrl);
						System.out.println("文件名："+aUrl);
						/*更新图片信息*/
						if("".equals(sysfilename) || "".equals(filename)||"".equals(pUrl)||"".equals(aUrl)){
							String deleteTerCard = "DELETE FROM PX_TEA_CERT_T WHERE OPRID = ?";
							jdbcTemplate.update(deleteTerCard, new Object[]{oprid});
						}else{
							String selectTerCard = "SELECT COUNT(1) FROM PX_TEA_CERT_T WHERE OPRID = ?";
							int count = jdbcTemplate.queryForObject(selectTerCard, new Object[]{oprid},"Integer");
							if(count==0){
								String insertTerCard = "INSERT INTO PX_TEA_CERT_T VALUES(?,?,?,?,?,?,?)";
								jdbcTemplate.update(insertTerCard, new Object[]{
										oprid,sysfilename,filename,pUrl,aUrl,new Date(),tzLoginServiceImpl.getLoginedManagerOprid(request)
								});
							}else{
								String updateTerCard = "UPDATE PX_TEA_CERT_T SET TZ_ATTACHSYSFILENA = ?,TZ_ATTACHFILE_NAME = ?,TZ_ATT_P_URL = ?,"
										+ " TZ_ATT_A_URL = ?,ROW_LASTMANT_DTTM = ?,ROW_LASTMANT_OPRID = ? WHERE OPRID = ?";
								jdbcTemplate.update(updateTerCard, new Object[]{
										sysfilename,filename,pUrl,aUrl,new Date(),tzLoginServiceImpl.getLoginedManagerOprid(request),oprid
								});
							}
							
						}
						
						/*机构角色表*/
						String PS_TZ_AQ_YHXX_TBL = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_REALNAME = ?,TZ_EMAIL = ? WHERE TZ_DLZH_ID=?";
						jdbcTemplate.update(PS_TZ_AQ_YHXX_TBL, new Object[]{
								jacksonUtil.getString("name").trim(),
								jacksonUtil.getString("email").trim(),
								jacksonUtil.getString("phone").trim()});
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