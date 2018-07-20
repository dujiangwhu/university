/**
 * 
 */
package com.tranzvision.gd.TZJgStudentMgBundle.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseTWithBLOBs;
import com.tranzvision.gd.TZPXBundle.dao.PkStuCourseChangeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxJgKsLogTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxStudentTMapper;
import com.tranzvision.gd.TZPXBundle.model.PkStuCourseChangeT;
import com.tranzvision.gd.TZPXBundle.model.PxJgKsLogT;
import com.tranzvision.gd.TZPXBundle.model.PxStudentT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
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
	private PsroleuserMapper psroleuserMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	
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
			String[] resultFldArray = { "TZ_JG_ID", "OPRID", "TZ_REALNAME","SEX","AGE","QQ","PHONE", "EMAIL","TIMECARD_REMAIND" ,"TIMECARD_USED" };

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
					mapList.put("stuUsedTimeCard", rowList[9]);
					
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
					mapData.put("orgAuditStatus", psTzJgBaseT.getTzJgAuditSta());
	
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
					String tzJgId = infoData.get("orgid").toString().toUpperCase();
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
							PxStudentT pxStudentT=pxStudentTMapper.selectByPrimaryKey(tzStuId);
							

							Map<String, Object> stuInfo = sqlQuery.queryForMap("SELECT TIMECARD_REMAIND FROM PX_STUDENT_T WHERE OPRID = ? AND TZ_JG_ID = ?", 
									new Object[] { tzStuId,tzJgId });
							
						    		
						    int stuTimeCardRemaind = stuInfo.get("TIMECARD_REMAIND") ==null?0:Integer.parseInt(String.valueOf(stuInfo.get("TIMECARD_REMAIND")));
							//int stuTimeCardUsed = stuInfo.get("TIMECARD_USED") ==null?0:Integer.parseInt(String.valueOf(stuInfo.get("TIMECARD_USED")));
						    
						    int stuTimeCardRemaindAfter = stuTimeCardRemaind + stuTimeCardNumber;
						    pxStudentT.setTimecardRemaind(stuTimeCardRemaindAfter);
							pxStudentTMapper.updateByPrimaryKey(pxStudentT);
							
							/*记录学员课时变更记录表*/
							String strChangeID =	String.valueOf(getSeqNum.getSeqNum("PK_STU_COURSE_CHANGE_T", "TZ_CHANGE_ID"));
							PkStuCourseChangeT pkStuCourseChangeT = new PkStuCourseChangeT();
							pkStuCourseChangeT.setTzChangeId(strChangeID);
							pkStuCourseChangeT.setOprid(tzStuId);
							pkStuCourseChangeT.setTzChangeType("3");
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
	
	//解析Excel;
		@Override
		public String tzOther(String oprType, String strParams, String[] errorMsg) {
			String strRet = "{}";
			Map<String, Object> returnJsonMap = new HashMap<String, Object>();
			returnJsonMap.put("success", "");
			JacksonUtil jacksonUtil = new JacksonUtil();
			try{
				//修改密码;
				if("tzAnalyzeExcel".equals(oprType)){
					this.tzAnalyzeExcel(strParams, errorMsg);
				}
			}catch(Exception e){
				errorMsg[0] = "1";
				errorMsg[1] = e.toString();
			}
			strRet = jacksonUtil.Map2json(returnJsonMap);
			return strRet;
		}
		
		public void tzAnalyzeExcel(String strParams, String[] errorMsg) {
			JacksonUtil jacksonUtil = new JacksonUtil();
			
			try {
				jacksonUtil.json2Map(strParams);
				
				//项目编号
				String strJgId = jacksonUtil.getString("orgId");
				// Excel路径;
				String strPath = jacksonUtil.getString("path");
				// Excel系统文件名;
				String strFileName = jacksonUtil.getString("sysFileName");

				// Excel用户文件名;
				String strUserFileName = jacksonUtil.getString("userFileName");
				
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				String strRealPath = request.getServletContext().getRealPath(strPath);
				
				System.out.println("机构编号："+strJgId);
				System.out.println("excel系统文件名："+strFileName);
				System.out.println("excel用户文件名："+strUserFileName);
				System.out.println("excel路径："+strRealPath);

				String dataFilePath = strRealPath + strFileName;

				List<String> dataListCellKeys = new ArrayList<String>();

				ExcelHandle2 excelHandle2 = new ExcelHandle2();
				excelHandle2.readExcel(dataFilePath, dataListCellKeys, false);

				ArrayList<Map<String, Object>> listData = excelHandle2.getExcelListData();
				
				Map<String, Object> listMapData = null;
				
				String strStuName = "";
				String strStuPhone = "";
				
				String impStuMessage = "";
				String impStuMessages = "";
				
				for (int i = 0; i < listData.size(); i++) {
					if(i>0){
						listMapData = listData.get(i);
						strStuName = (String) listMapData.get("0");
						strStuName = strStuName.trim();
						strStuPhone = (String) listMapData.get("1");
						strStuPhone = strStuPhone.trim();
						
						System.out.println(strStuName);
						System.out.println(strStuPhone);
						//有空格行忽略，无效数据
						if(strStuName==null||strStuPhone==null||strStuName.equals("")||strStuPhone.equals(""))
						{
							continue;
						}
						impStuMessage = "";
						impStuMessage = impStuMessage + this.tzAddStudent(strJgId,strStuName,strStuPhone) + ",";
						
						errorMsg[0] = "0";
						errorMsg[1] = impStuMessage;
						System.out.println("success");
						
					}
				}
				//删除临时文件
				File file = new File(dataFilePath);
			    if (file.isFile() && file.exists()) {  
			        file.delete();
			    }
				
			    
			} catch (Exception e) {
				errorMsg[0] = "1";
				errorMsg[1] = e.toString();
			}
			
		}
		
		public String tzAddStudent(String tzJgId,String strStuName,String strStuPhone){
			String strRet = "";
			if(strStuName==null||"".equals(strStuPhone)){
				strRet = "手机和姓名不能为空";
			}else{
				
				int count = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID = ? AND TZ_JG_ID = ?", 
						new Object[] { strStuPhone,tzJgId }, "Integer");
						
				if(count>0){
					strRet= "手机号为" + strStuPhone + "的学员已存在。";
				}else{
					// 账号密码;
					String password = "123456";
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					/*创建用户信息*/
					String oprID = "";
					oprID = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
					PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
					psTzAqYhxxTbl.setTzDlzhId(strStuPhone);
					psTzAqYhxxTbl.setTzJgId(tzJgId);
					psTzAqYhxxTbl.setOprid(oprID);
					psTzAqYhxxTbl.setTzRealname(strStuName);
					psTzAqYhxxTbl.setTzMobile(strStuPhone);
					psTzAqYhxxTbl.setTzRylx("PXXY");
					psTzAqYhxxTbl.setTzYxbdBz("N");
					psTzAqYhxxTbl.setTzSjbdBz("Y");
					psTzAqYhxxTbl.setTzJihuoZt("Y");
					psTzAqYhxxTbl.setTzJihuoFs("R");
					psTzAqYhxxTbl.setTzZhceDt(new Date());
					psTzAqYhxxTbl.setTzBjsEml("");
					psTzAqYhxxTbl.setTzBjsSms("");
					
					String updateOperid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzAqYhxxTbl.setRowAddedDttm(new Date());
					psTzAqYhxxTbl.setRowAddedOprid(updateOperid);
					psTzAqYhxxTbl.setRowLastmantDttm(new Date());
					psTzAqYhxxTbl.setRowLastmantOprid(updateOperid);
					psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);
					
					short acctLockNum;

					acctLockNum = 0;
			
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(oprID);
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock(acctLockNum);
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(updateOperid);
					psoprdefnMapper.insert(psoprdefn);
					
					/*添加角色*/
					Psroleuser psroleuser = new Psroleuser();
					psroleuser.setRoleuser(oprID);
					psroleuser.setRolename("PK_STU");
					psroleuser.setDynamicSw("N");
					psroleuserMapper.insert(psroleuser);
					
					/* 联系方式;
					if ((phone != null && !"".equals(phone)) || (email != null && !"".equals(email))) {
						String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
						sqlQuery.update(lsfsSQL, new Object[]{"PXXY", oprID, phone, email});
					}*/
					
					/*添加学员信息表*/
					PxStudentT pxStudentT = new PxStudentT();
					pxStudentT.setOprid(oprID);
					pxStudentT.setTzJgId(tzJgId);
					pxStudentT.setSex("");
					pxStudentT.setAge(0);
					pxStudentT.setQq("");
					pxStudentT.setEmail("");
					pxStudentT.setContact("");
					pxStudentT.setContactPhone("");
					pxStudentT.setContactAddress("");
					pxStudentT.setTimecardRemaind(0);
					pxStudentT.setTimecardUsed(0);
					pxStudentT.setStuStatus("A");
					pxStudentTMapper.insert(pxStudentT);
					
				}						
			}
			return strRet;
			
		}

}