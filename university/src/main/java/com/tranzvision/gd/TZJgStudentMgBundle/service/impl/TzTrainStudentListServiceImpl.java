/**
 * 
 */
package com.tranzvision.gd.TZJgStudentMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseTWithBLOBs;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgLoginbjT;
import com.tranzvision.gd.TZPXBundle.dao.PkStuCourseChangeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxJgKsLogTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxJgKsOrderTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxStudentTMapper;
import com.tranzvision.gd.TZPXBundle.model.PkStuCourseChangeT;
import com.tranzvision.gd.TZPXBundle.model.PxJgKsLogT;
import com.tranzvision.gd.TZPXBundle.model.PxJgKsOrderT;
import com.tranzvision.gd.TZPXBundle.model.PxStudentT;
import com.tranzvision.gd.TZPXBundle.model.PxStudentTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZJgStudentMgBundle.service.impl.TzTrainStudentListServiceImpl")
public class TzTrainStudentListServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;
	
	@Autowired
	private PxStudentTMapper pxStudentTMapper;
	
	@Autowired
	private PxJgKsLogTMapper pxJgKsLogTMapper;
	
	@Autowired
	private PkStuCourseChangeTMapper pkStuCourseChangeTMapper;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSeqNum getSeqNum;

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
			String[] resultFldArray = { "TZ_JG_ID", "OPRID", "TZ_REALNAME","SEX","AGE","QQ","PHONE", "EMAIL","TIMECARD_REMAIND" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("orgid", rowList[0]);
					mapList.put("oprid", rowList[1]);
					mapList.put("stuName", rowList[2]);
					mapList.put("sex", rowList[3]);
					mapList.put("age", rowList[4]);
					mapList.put("stuQQ", rowList[5]);
					mapList.put("stuPhone", rowList[6]);
					mapList.put("stuEmail", rowList[7]);
					mapList.put("stuRemaindTimeCard", rowList[8]);

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
	 * 获取机构账号信息
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("ORGID")) {
				String tzJgId = jacksonUtil.getString("ORGID");
				if ("".equals(tzJgId)) {
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
					return strRet;
				}
	
				PsTzJgBaseTWithBLOBs psTzJgBaseT = psTzJgBaseTMapper.selectByPrimaryKey(tzJgId);
	
				if (psTzJgBaseT != null) {
	
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("orgId", psTzJgBaseT.getTzJgId());
					mapData.put("orgName", psTzJgBaseT.getTzJgName());
	
					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);
	
					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "[" + tzJgId + "]" + "该机构数据不存在";
				}
	
				errMsg[0] = "0";
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/**
	 * 给学员分配课时卡
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String errorMsg = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				if ("ADDTIMECARD".equals(typeFlag)) {
					// 机构编号;
					String tzJgId = infoData.get("orgId").toString().toUpperCase();
					TzFilterIllegalCharacter tzFilterIllegalCharacter = new TzFilterIllegalCharacter();
					tzJgId = tzFilterIllegalCharacter.filterAllIllegalCharacter(tzJgId);
					
					String tzStuId = infoData.get("oprid").toString().toUpperCase();
					tzStuId = tzFilterIllegalCharacter.filterAllIllegalCharacter(tzStuId);
					
					/*给学生分配课时卡数*/
					int stuTimeCardNumber = Integer.parseInt(String.valueOf(infoData.get("addTimeCardNumber")));

					String sql = "select TZ_JG_TIMECARD_YY,TZ_JG_TIMECARD_SY from PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
					Map<String, Object> orgInfo = sqlQuery.queryForMap(sql, new Object[] { tzJgId });
					if (orgInfo!=null) {
						/*机构拥有的课时卡*/
						int orgTimeCardHave = orgInfo.get("TZ_JG_TIMECARD_YY") ==null?0:Integer.parseInt(String.valueOf(orgInfo.get("TZ_JG_TIMECARD_YY")));
						int orgTimeCardAssign = orgInfo.get("TZ_JG_TIMECARD_SY") ==null?0:Integer.parseInt(String.valueOf(orgInfo.get("TZ_JG_TIMECARD_SY")));
						
						if(stuTimeCardNumber > orgTimeCardHave){
							errMsg[0] = "1";
							errMsg[1] = "分配失败，机构剩余课时卡不足。";
						}else{
							// 机构名称;
							PsTzJgBaseTWithBLOBs psTzJgBaseT = new PsTzJgBaseTWithBLOBs();
							psTzJgBaseT.setTzJgId(tzJgId);
							psTzJgBaseT.setRowLastmantDttm(dateNow);
							psTzJgBaseT.setRowLastmantOprid(oprid);
							/*分配后充值后的数据*/
							int orgTimeCardHaveAfter = orgTimeCardHave - stuTimeCardNumber; 
							int orgTimeCardAssignAfter = orgTimeCardAssign + stuTimeCardNumber;
							psTzJgBaseT.setTzJgTimecardYy(orgTimeCardHaveAfter);
							psTzJgBaseT.setTzJgTimecardSy(orgTimeCardAssignAfter);
							psTzJgBaseTMapper.updateByPrimaryKeySelective(psTzJgBaseT);
							
							
							/*记录机构课时变更情况*/
							String strLogInsID =	String.valueOf(getSeqNum.getSeqNum("PX_JG_KS_LOG_T", "TZ_LOG_INS_ID"));
							PxJgKsLogT pxJgKsLogT = new PxJgKsLogT();
							pxJgKsLogT.setTzJgId(tzJgId);
							pxJgKsLogT.setTzLogInsId(strLogInsID);
							pxJgKsLogT.setTzKsModifyType("B");
							pxJgKsLogT.setTzTimecardBefore(orgTimeCardHave);
							pxJgKsLogT.setTzTimecardAfter(orgTimeCardHaveAfter);
							pxJgKsLogT.setTzTimecardModify(stuTimeCardNumber);
							pxJgKsLogT.setOprid(tzStuId);
							pxJgKsLogT.setRowLastmantOprid(oprid);
							pxJgKsLogT.setRowLastmantDttm(dateNow);
							pxJgKsLogTMapper.insert(pxJgKsLogT);
							
							
							/*给学员添加课时卡*/
							PxStudentTKey pxStudentTKey= new PxStudentTKey();
							pxStudentTKey.setOprid(tzStuId);
							pxStudentTKey.setTzJgId(tzJgId);
							PxStudentT pxStudentT=pxStudentTMapper.selectByPrimaryKey(pxStudentTKey);
							
						    int stuTimeCardRemaind = sqlQuery.queryForObject("SELECT TIMECARD_REMAIND FROM PX_STUDENT_T WHERE OPRID = ? AND TZ_JG_ID = ?",
						    		new Object[] { tzStuId,tzJgId },"Integer");
						    
						    int stuTimeCardRemaindAfter = stuTimeCardRemaind + stuTimeCardNumber;
						    pxStudentT.setTimecardRemaind(stuTimeCardRemaindAfter);
							pxStudentTMapper.updateByPrimaryKey(pxStudentT);
							
							/*记录学员课时变更记录表*/
							String strChangeID =	String.valueOf(getSeqNum.getSeqNum("PK_STU_COURSE_CHANGE_T", "TZ_CHANGE_ID"));
							PkStuCourseChangeT pkStuCourseChangeT = new PkStuCourseChangeT();
							pkStuCourseChangeT.setTzChangeId(strChangeID);
							pkStuCourseChangeT.setOprid(tzStuId);
							pkStuCourseChangeT.setTzChangeType("C");
							pkStuCourseChangeT.setTzBeforeChange(stuTimeCardRemaind);
							pkStuCourseChangeT.setTzAfterChange(stuTimeCardRemaindAfter);
							pkStuCourseChangeT.setTzChange(stuTimeCardNumber);
							pkStuCourseChangeT.setTzChangeTime(dateNow);
							pkStuCourseChangeT.setRowLastmantDttm(dateNow);
							pkStuCourseChangeT.setRowLastmantOprid(oprid);
							pkStuCourseChangeTMapper.insert(pkStuCourseChangeT);
							
							//将机构ID返回
							Map<String, Object> mapRet = new HashMap<String, Object>();
							mapRet.put("orgid", tzJgId);
							mapRet.put("oprid", tzStuId);
							
							strRet = jacksonUtil.Map2json(mapRet);
						}

					} else {
						errorMsg += comma + tzJgId;
						comma = ",";
					}
					
				}
				
			}
			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "机构：" + errorMsg + "，不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}