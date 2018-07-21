/**
 * 
 */
package com.tranzvision.gd.TZPxStudentBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;
import com.tranzvision.gd.TZPXBundle.dao.PxStudentTMapper;
import com.tranzvision.gd.TZPXBundle.model.PxStudentT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxStudentBundle.service.impl.PxStudentMgServiceImpl")
public class PxStudentMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;
	
	@Autowired
	private PxStudentTMapper pxStudentMapper;
	
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;

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
			String[] resultFldArray = { "OPRID","TZ_JG_ID", "TZ_REALNAME", "SEX","AGE","TZ_MOBILE",
					"TIMECARD_REMAIND","STU_STATUS","STU_STATUS_DMS","TZ_SEX_VALUE","TZ_JG_NAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("oprid", rowList[0]);
					mapList.put("orgid", rowList[1]);
					mapList.put("name", rowList[2]);
					mapList.put("sex", rowList[3]);
					mapList.put("age", rowList[4]);
					mapList.put("phone", rowList[5]);
					mapList.put("timecardRemaind", rowList[6]);
					mapList.put("statu", rowList[7]);
					mapList.put("stuStatusDms", rowList[8]);
					mapList.put("tzSexValue", rowList[9]);
					mapList.put("tzJgName", rowList[10]);
			       
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
				String orgid=jacksonUtil.getString("orgid");
				String oprid=jacksonUtil.getString("OPRID");
				// 头像地址;
				String titleImageUrlSQL = "SELECT B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A , PS_TZ_OPR_PHOTO_T B WHERE A.OPRID=? AND A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA";
				Map<String, Object> imgMap = jdbcTemplate.queryForMap(titleImageUrlSQL, new Object[] { oprid });
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
				
				
				titleImageUrlSQL = "SELECT TZ_REALNAME,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? ";
				Map<String, Object> userMap = jdbcTemplate.queryForMap(titleImageUrlSQL, new Object[] { oprid });

				if (userMap == null) {
					errMsg[0] = "1";
					errMsg[1] = "不存在该用户！";
				} else {
					String name = (String) userMap.get("TZ_REALNAME");
					String phone = (String) userMap.get("TZ_MOBILE");
					
					PxStudentT pxStudent = pxStudentMapper.selectByPrimaryKey(oprid);
					
					if (pxStudent == null) {
						errMsg[0] = "1";
						errMsg[1] = "不存在该用户！";
					} else {
						Map<String, Object> jsonMap2 = new HashMap<String, Object>();
						jsonMap2.put("titleImageUrl", titleImageUrl);
						jsonMap2.put("oprid", pxStudent.getOprid());
						jsonMap2.put("orgid", pxStudent.getTzJgId());
						jsonMap2.put("name", name);
						jsonMap2.put("phone", phone);
						jsonMap2.put("sex", pxStudent.getSex());
						jsonMap2.put("age", pxStudent.getAge());
						jsonMap2.put("qq", pxStudent.getQq());
						jsonMap2.put("email", pxStudent.getEmail());
						jsonMap2.put("contactor", pxStudent.getContact());
						jsonMap2.put("contactorPhone", pxStudent.getContactPhone());
						jsonMap2.put("contactorAddress", pxStudent.getContactAddress());
						jsonMap2.put("statu", pxStudent.getStuStatus());
						jsonMap2.put("timecardRemaind", pxStudent.getTimecardRemaind());

						PsTzJgBaseT psTzJgBaseT=psTzJgBaseTMapper.selectByPrimaryKey(pxStudent.getTzJgId());
						jsonMap2.put("tzJgName", psTzJgBaseT.getTzJgName());
						
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
				String orgid=jacksonUtil.getString("orgid");
				String oprid=jacksonUtil.getString("oprid");
				System.out.println(jacksonUtil.getString("sex"));
				if(oprid==null){
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}else{
					PxStudentT pxStudent = pxStudentMapper.selectByPrimaryKey(oprid);
					if(pxStudent==null){
						errMsg[0] = "1";
						errMsg[1] = "用户不存在！";
					}else{
						pxStudent.setSex(jacksonUtil.getString("sex"));
						pxStudent.setAge(jacksonUtil.getInt("age"));
						pxStudent.setQq(jacksonUtil.getString("qq"));
						pxStudent.setEmail(jacksonUtil.getString("email"));
						pxStudent.setContact(jacksonUtil.getString("contactor"));
						pxStudent.setContactPhone(jacksonUtil.getString("contactorPhone"));
						pxStudent.setContactAddress(jacksonUtil.getString("contactorAddress"));
						pxStudent.setStuStatus(jacksonUtil.getString("statu"));
						
						pxStudentMapper.updateByPrimaryKey(pxStudent);
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