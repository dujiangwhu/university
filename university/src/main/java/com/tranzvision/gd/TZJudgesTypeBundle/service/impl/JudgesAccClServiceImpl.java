package com.tranzvision.gd.TZJudgesTypeBundle.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZJudgesTypeBundle.dao.PsTzPwHyTblMapper;
import com.tranzvision.gd.TZJudgesTypeBundle.model.PsTzPwHyTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzPwExtTMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzPwExtT;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzPwExtTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author xzx
 * 功能说明：面试评委账号管理列表页面;
 * 原PS类：TZ_GD_JUDEG_PKG:TZ_JUGINFO_CLS
 */
@Service("com.tranzvision.gd.TZJudgesTypeBundle.service.impl.JudgesAccClServiceImpl")
public class JudgesAccClServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzPwHyTblMapper psTzPwHyTblMapper;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private PsTzPwExtTMapper psTzPwExtTMapper;
	
	
	/* 查询面试评委账号列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		//当前机构
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_DLZH_ID", "ASC" } };

		// json数据要的结果字段;
//		String[] resultFldArray = { "TZ_JG_ID", "TZ_DLZH_ID", "TZ_REALNAME","TZ_MOBILE", "TZ_EMAIL","TZ_PWHY_DESC","TZ_JUGTYP_NAME","TZ_PWHY_ID","TZ_JUGTYP_ID" };
		String[] resultFldArray = { "TZ_JG_ID", "TZ_DLZH_ID", "TZ_REALNAME","TZ_MOBILE", "TZ_EMAIL","TZ_JUGTYP_NAME","TZ_JUGTYP_ID","OPRID","TZ_CLPS_NUM" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("jgID", rowList[0]);
				mapList.put("accountNo", rowList[1]);
				mapList.put("judgeName", rowList[2]);
				mapList.put("judPhoneNumber", rowList[3]);
				mapList.put("judEmail", rowList[4]);
				//mapList.put("judgeHY", rowList[5]);
				mapList.put("judTypeDesc", rowList[5]);
				//mapList.put("judgeHYID", rowList[7]);
				mapList.put("judType", rowList[6]);
				//评委行业类别
				String judgeHY = "";
				String sqlPwhy = "SELECT A.TZ_PWHY_ID,B.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL B,PS_TZ_PW_HY_TBL A WHERE A.TZ_PWHY_ID=B.TZ_ZHZ_ID AND B.TZ_ZHZJH_ID='TZ_PWHY_ID' AND A.TZ_JG_ID=? AND A.OPRID=?";
				List<Map<String,Object>> listPwhy = jdbcTemplate.queryForList(sqlPwhy,new Object[]{orgId,rowList[7]});
				for(Map<String, Object> mapPwhy : listPwhy) {
					String industryName = mapPwhy.get("TZ_ZHZ_DMS") == null ? "" : mapPwhy.get("TZ_ZHZ_DMS").toString();
					if(!"".equals(industryName)){
						if("".equals(judgeHY)){
							judgeHY = industryName ;
						}else{
							judgeHY = judgeHY + "|" + industryName ;
						}
					}
				}
				
				mapList.put("judgeHY", judgeHY);
				mapList.put("oprid", rowList[7]);
				mapList.put("clpwNum", rowList[8]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	/* 新增 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			String oprID = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				if ("JUDINFO".equals(strFlag)) {
//				if(jacksonUtil.containsKey("data")){
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					// 用户账号;
					String accountNo = (String) infoData.get("accountNo");
					// 所属机构编号;
					String orgID = (String) infoData.get("orgId");

					String sql = "select COUNT(1) from PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { accountNo, orgID }, "Integer");
					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "当前机构下登陆账号为：" + accountNo + "的信息已经存在。";
						return strRet;
					} 
					// 密码;
					String password = (String) infoData.get("password");
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					// 用户描述
					String judgeName = (String) infoData.get("judgeName");
					// 手机
					String judgePhoneNumber = (String) infoData.get("judgePhoneNumber");
					// 邮箱
					String judgeEmail = (String) infoData.get("judgeEmail");
					// 备注
					String str_beizhu=(String)infoData.get("beizhu");
					// 行业类别
					// ArrayList judgeHY = (ArrayList) infoData.get("judgeHY");
					// 用户角色
					// String roleName = (String) infoData.get("roleName");
					// 用户类型
					//String userType = (String) infoData.get("userType");
					// 人员类型，激活状态
					String tzRylx = "NBYH";
					String tzJihuoZt = "Y";
					// 锁定用户;
					String clockFlag = "";

					if (infoData.containsKey("clockFlag")) {
						clockFlag = (String) infoData.get("clockFlag");
					}
					
					oprID = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
					PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
					psTzAqYhxxTbl.setTzDlzhId(accountNo);
					psTzAqYhxxTbl.setTzJgId(orgID);
					psTzAqYhxxTbl.setOprid(oprID);
					psTzAqYhxxTbl.setTzRealname(judgeName);
					psTzAqYhxxTbl.setTzEmail(judgeEmail);
					psTzAqYhxxTbl.setTzMobile(judgePhoneNumber);
					psTzAqYhxxTbl.setTzRylx(tzRylx);;
					psTzAqYhxxTbl.setTzJihuoZt(tzJihuoZt);
					psTzAqYhxxTbl.setTzZhceDt(new Date());
					String updateOperid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				    psTzAqYhxxTbl.setRowAddedDttm(new Date());
					psTzAqYhxxTbl.setRowAddedOprid(updateOperid);
					psTzAqYhxxTbl.setRowLastmantDttm(new Date());
					psTzAqYhxxTbl.setRowLastmantOprid(updateOperid);
					psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);
					
					short clockFlagNum;
					if ("Y".equals(clockFlag) || "on".equals(clockFlag) || "1".equals(clockFlag)) {
						clockFlagNum = 1;
					} else {
						clockFlagNum = 0;
					}
					/*if ("Y".equals(userType)) {
						userType = "2";
					} else {
						userType = "0";
					}*/
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(oprID);
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock(clockFlagNum);
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(updateOperid);
					psoprdefnMapper.insert(psoprdefn);

					// 行业类别;
					Boolean isString=infoData.get("judgeHY") instanceof String;
					if(!isString){
						ArrayList<String> judgeHYArr=new ArrayList<String>();
						judgeHYArr=(ArrayList<String>) infoData.get("judgeHY");
						PsTzPwHyTblKey psTzPwHyTblKey = new PsTzPwHyTblKey();
						for (int i = 0; i < judgeHYArr.size(); i++) {
							String sqlAudnum = "select COUNT(1) from PS_TZ_PW_HY_TBL WHERE TZ_JG_ID=? AND OPRID=? AND TZ_PWHY_ID=?";
							int count1 = jdbcTemplate.queryForObject(sqlAudnum, new Object[]{orgID,oprID,(String) judgeHYArr.get(i)},"Integer");
							if(count1 > 0){
								
							}else{
								
								psTzPwHyTblKey.setTzPwhyId((String) judgeHYArr.get(i));
								psTzPwHyTblKey.setTzJgId(orgID);
								psTzPwHyTblKey.setOprid(oprID);
								psTzPwHyTblMapper.insert(psTzPwHyTblKey);
								
							}
						}
					}
					// 用户类型;
//					Boolean isArr=infoData.get("judgeType").getClass().isArray();
//					if(isArr==true){
					Boolean isStr=infoData.get("judgeType") instanceof String;
					if(!isStr){
						ArrayList<String> judgeTypeArr=new ArrayList<String>();
						judgeTypeArr=(ArrayList<String>) infoData.get("judgeType");
						for (int i = 0; i < judgeTypeArr.size(); i++) {
							String yhlxSQL = "INSERT INTO PS_TZ_JUSR_REL_TBL(TZ_JG_ID,OPRID,TZ_JUGTYP_ID) VALUES(?,?,?)";
							jdbcTemplate.update(yhlxSQL, new Object[]{orgID, oprID, judgeTypeArr.get(i)});
							//添加用户角色
							//查询用户类型对应的用户角色
							String userTypeSql = "SELECT ROLENAME FROM PS_TZ_JUGTYP_TBL WHERE TZ_JG_ID=? AND TZ_JUGTYP_ID=?";
							Map<String, Object> map1 = jdbcTemplate.queryForMap(userTypeSql, new Object[] { orgID,judgeTypeArr.get(i) });
							if (map1 != null) {
								String roleName = (String) map1.get("ROLENAME");
								if(roleName!=""){
									String roleSQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,?)";
									jdbcTemplate.update(roleSQL, new Object[]{oprID,roleName,"N"});
								}
							}
						}
					}else{
						String judgeType=(String)infoData.get("judgeType");
						String yhlxSQL = "INSERT INTO PS_TZ_JUSR_REL_TBL(TZ_JG_ID,OPRID,TZ_JUGTYP_ID) VALUES(?,?,?)";
						jdbcTemplate.update(yhlxSQL, new Object[]{orgID, oprID, judgeType});
						//添加用户角色
						//查询用户类型对应的用户角色
						String userTypeSql = "SELECT ROLENAME FROM PS_TZ_JUGTYP_TBL WHERE TZ_JG_ID=? AND TZ_JUGTYP_ID=?";
						Map<String, Object> map1 = jdbcTemplate.queryForMap(userTypeSql, new Object[] { orgID,judgeType});
						if (map1 != null) {
							String roleName = (String) map1.get("ROLENAME");
							if(roleName!=""){
								String roleSQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,?)";
								jdbcTemplate.update(roleSQL, new Object[]{oprID,roleName,"N"});
							}
						}
					}
						
					// 备注;
					if ((str_beizhu != null && !"".equals(str_beizhu))) {
						String lsfsSQL = "INSERT INTO PS_TZ_REG_USER_T(OPRID,TZ_REALNAME,TZ_BEIZHU) VALUES(?,?,?)";
						jdbcTemplate.update(lsfsSQL, new Object[]{oprID, judgeName, str_beizhu});
					}
					// 联系方式;
					if ((judgePhoneNumber != null && !"".equals(judgePhoneNumber)) || (judgeEmail != null && !"".equals(judgeEmail))) {
						String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
						jdbcTemplate.update(lsfsSQL, new Object[]{tzRylx, oprID, judgePhoneNumber, judgeEmail});
					}
					// 评委 账号-类型-关系;
//					if ((userType != null && !"".equals(userType))) {
//						String yhlxSQL = "INSERT INTO PS_TZ_JUSR_REL_TBL(TZ_JG_ID,OPRID,TZ_JUGTYP_ID) VALUES(?,?,?)";
//						jdbcTemplate.update(yhlxSQL, new Object[]{orgID, oprID, userType});
//						//添加用户角色;
//						String roleName="";
//						String sqlType = "SELECT ROLENAME FROM PS_TZ_JUGTYP_TBL WHERE TZ_JUGTYP_ID=?";
//						Map<String, Object> map = jdbcTemplate.queryForMap(sqlType, new Object[] { userType });
						/*if (roleName != null) {
//							roleName = (String) map.get("ROLENAME");
						
						String roleSQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,?)";
						jdbcTemplate.update(roleSQL, new Object[]{oprID,roleName,"N"});
						}*/
//					}
					
					
					// 院内院外，卢艳添加，2017-7-13
					String judgeYnyw = (String) infoData.get("judgeYnyw");
					if(judgeYnyw!=null && !"".equals(judgeYnyw)) {
						PsTzPwExtTKey psTzPwExtTKey = new PsTzPwExtTKey();
						psTzPwExtTKey.setTzJgId(orgID);
						psTzPwExtTKey.setOprid(oprID);
						
						PsTzPwExtT psTzPwExtT = psTzPwExtTMapper.selectByPrimaryKey(psTzPwExtTKey);
						if(psTzPwExtT==null) {
							psTzPwExtT = new PsTzPwExtT();
							psTzPwExtT.setTzJgId(orgID);
							psTzPwExtT.setOprid(oprID);
							psTzPwExtT.setTzYnyw(judgeYnyw);
							psTzPwExtTMapper.insertSelective(psTzPwExtT);
						} else {
							psTzPwExtT.setTzYnyw(judgeYnyw);
							psTzPwExtTMapper.updateByPrimaryKeySelective(psTzPwExtT);
						}
					}
					
			    	
				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	// 查询评委信息表单
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
		
			if (jacksonUtil.containsKey("accountNo") && jacksonUtil.containsKey("orgId")) {
				String accountNo = jacksonUtil.getString("accountNo");
				String userOrg = jacksonUtil.getString("orgId");

				// 用户ID,姓名，电子邮件，手机号码，人员类型，邮箱绑定标志，手机绑定标志，激活状态，激活方式;
				String oprID = "", name = "", perType = "",
						jhState = "", jhMethod = "";
				String email = "", mobile = "";
				String originOrgId = "";
				// String userType = "0";
				// String[]  judgeType = {"1","3","4"};
				String[]  judgeType = {};
				String roleNameDesc="",roleName="";
				// 密码;
				String password = "";
				String rylx = "";
				String acctLock = "0";
				// 行业类别
				String judgeHYID="";
				String judgeHY="";
				//List<String>hylxName=new ArrayList<String>();
			    //List<String>hylxID=new ArrayList<String>();
				// 备注
				String str_beizhu="";

				if (accountNo != null && !"".equals(accountNo)) {
					PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
					psTzAqYhxxTblKey.setTzDlzhId(accountNo);
					psTzAqYhxxTblKey.setTzJgId(userOrg);
					PsTzAqYhxxTbl psTzAqYhxxTbl = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);
					
					if (psTzAqYhxxTbl != null) {
						oprID = psTzAqYhxxTbl.getOprid();
						name = psTzAqYhxxTbl.getTzRealname();
//						perType = psTzAqYhxxTbl.getTzRylx();
						jhState = psTzAqYhxxTbl.getTzJihuoZt();
//						jhMethod = psTzAqYhxxTbl.getTzJihuoFs();
						rylx = psTzAqYhxxTbl.getTzRylx();
					}

					// 备注
					String bzSQL = "select TZ_BEIZHU FROM PS_TZ_REG_USER_T WHERE OPRID =?";
					str_beizhu = jdbcTemplate.queryForObject(bzSQL, new Object[] { oprID },"String");
				
					// 用户类型
//					String userTypeSql = "SELECT TZ_JUGTYP_ID FROM PS_TZ_JUSR_REL_TBL WHERE OPRID=?";
//					Map<String, Object> map1 = jdbcTemplate.queryForMap(userTypeSql, new Object[] { oprID });
					String sql2 = "SELECT TZ_ZY_SJ,TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql2, new Object[] { rylx, oprID });
					// 角色名称
					// String sqlRole = "SELECT A.ROLENAME,DESCR FROM PSROLEUSER A, PSROLEDEFN_VW B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER=? ";
					// Map<String, Object> mapRole = jdbcTemplate.queryForMap(sqlRole, new Object[] { oprID });
					//行业类别
					//String hylbSql = "select group_concat(TZ_PWHY_ID) TZ_PWHY_ID from PS_TZ_PW_HY_TBL H, PS_TZ_AQ_YHXX_TBL A WHERE H.OPRID=? AND H.OPRID=A.OPRID GROUP BY H.OPRID";
					//Map<String, Object> map2 = jdbcTemplate.queryForMap(hylbSql, new Object[] { oprID });
					//String hylbDescSql = "SELECT group_concat(TZ_ZHZ_DMS) TZ_PWHY_DESC FROM PS_TZ_PT_ZHZXX_TBL C,PS_TZ_PW_HY_TBL H,PS_TZ_AQ_YHXX_TBL A  WHERE C.TZ_ZHZJH_ID = 'TZ_PWHY_ID' AND C.TZ_ZHZ_ID = H.TZ_PWHY_ID AND H.OPRID=? AND H.OPRID=A.OPRID GROUP BY H.OPRID";
					//Map<String, Object> map3 = jdbcTemplate.queryForMap(hylbDescSql, new Object[] { oprID });
					String hylbSql = "SELECT group_concat(TZ_PWHY_ID) TZ_PWHY_ID,group_concat(TZ_ZHZ_DMS) TZ_PWHY_DESC FROM PS_TZ_PT_ZHZXX_TBL C,PS_TZ_PW_HY_TBL H,PS_TZ_AQ_YHXX_TBL A  WHERE C.TZ_ZHZJH_ID = 'TZ_PWHY_ID' AND C.TZ_ZHZ_ID = H.TZ_PWHY_ID AND H.OPRID=? AND H.OPRID=A.OPRID GROUP BY H.OPRID";
					Map<String, Object> map2 = jdbcTemplate.queryForMap(hylbSql, new Object[] { oprID });
					/*List<Map<String, Object>>hylxList=new ArrayList<Map<String, Object>>();
					hylxList=jdbcTemplate.queryForList(hylbSql, new Object[]{oprID});
					if (hylxList != null && hylxList.size() > 0) {
						for (int i = 0; i < hylxList.size(); i++) {
							hylxName.add((String) hylxList.get(i).get("TZ_PWHY_DESC"));
							hylxID.add((String) hylxList.get(i).get("TZ_PWHY_ID"));	
						}
					}*/
					// 用户类型
					String userTypeSql = "SELECT group_concat(TZ_JUGTYP_ID)TZ_JUGTYP_ID FROM PS_TZ_JUSR_REL_TBL WHERE TZ_JG_ID=? AND OPRID=?";
					Map<String, Object> map1 = jdbcTemplate.queryForMap(userTypeSql, new Object[] { userOrg,oprID });
					if (map1 != null) {
						String tmp="";
						tmp = (String) map1.get("TZ_JUGTYP_ID");
						judgeType= tmp.split(",");
					}
					
					if (map2 != null) {
						judgeHYID = (String) map2.get("TZ_PWHY_ID");
						judgeHY = (String) map2.get("TZ_PWHY_DESC");
					}
					
//					if (map1 != null) {
//						userType = (String) map1.get("TZ_JUGTYP_ID");
//					}
					if (map != null) {
						mobile = (String) map.get("TZ_ZY_SJ");
						email = (String) map.get("TZ_ZY_EMAIL");
					}
					/*if (mapRole != null) {
						roleName = (String) mapRole.get("ROLENAME");
						roleNameDesc = (String) mapRole.get("DESCR");
					}*/
					
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn = psoprdefnMapper.selectByPrimaryKey(oprID);
					short localNum = 0;
				
					if (psoprdefn != null) {
						password = psoprdefn.getOperpswd();
						if (!"".equals(password)) {
							password = DESUtil.decrypt(password, "TZGD_Tranzvision");
						}
						localNum = (short) psoprdefn.getAcctlock();
					}
					if (localNum == 1) {
						acctLock = "1";
					}else{
						acctLock = "0";
					}
//					if (userType!="1") {
//						userType = "1";
//					}else{
//						userType = "0";
//					}
					originOrgId = userOrg;

				} else {
					jhState = "Y";
//					jhMethod = "R";
					rylx = "NBYH";
					originOrgId = "";
				}
				
				
				//评委的院内院外，卢艳添加，2017-7-13
				String judgeYnyw = jdbcTemplate.queryForObject("SELECT TZ_YNYW FROM PS_TZ_PW_EXT_T WHERE TZ_JG_ID=? AND OPRID=?", new Object[]{userOrg,oprID}, "String");
				
				returnJsonMap.put("accountNo", accountNo);
				returnJsonMap.put("orgId", userOrg);
				returnJsonMap.put("oprid", oprID);
				
				returnJsonMap.put("judgeName", name);
				returnJsonMap.put("judgeEmail", email);
				returnJsonMap.put("judgePhoneNumber", mobile);
				
				returnJsonMap.put("jhState", jhState);
//				returnJsonMap.put("jhMethod", jhMethod);
				returnJsonMap.put("password", password);
				returnJsonMap.put("rePassword", password);
//				returnJsonMap.put("perType", perType);
				
				returnJsonMap.put("originOrgId", originOrgId);
				returnJsonMap.put("rylx", rylx);
				returnJsonMap.put("clockFlag", acctLock);
//				returnJsonMap.put("bdEmail", bdEmail);
				//returnJsonMap.put("userType", judgeType);
				returnJsonMap.put("judgeType", judgeType);
				returnJsonMap.put("roleName", roleName);
				returnJsonMap.put("roleNameDesc", roleNameDesc);
				
				returnJsonMap.put("judgeHY", judgeHY);
				returnJsonMap.put("judgeHYID", judgeHYID);
				
				returnJsonMap.put("beizhu", str_beizhu);
				
				returnJsonMap.put("judgeYnyw", judgeYnyw);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取用户信息";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/*删除*/
	@SuppressWarnings("unchecked")
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
				String oprID = "";
				// 内容页信息;
				String strUserInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strUserInfo);
				// 登录账号;
				String accountNo = jacksonUtil.getString("accountNo");
				// 机构编号;
				String orgId = jacksonUtil.getString("jgID");
				// 用户类ID;
				String judTypeId = jacksonUtil.getString("judType");
				
			    if(accountNo != null && !"".equals(accountNo)&& orgId != null && !"".endsWith(orgId)){
			    	/*获取用户ID*/
					PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
					psTzAqYhxxTblKey.setTzDlzhId(accountNo);
					psTzAqYhxxTblKey.setTzJgId(orgId);
					PsTzAqYhxxTbl psTzAqYhxxTbl= psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);
					oprID = psTzAqYhxxTbl.getOprid();
					if(oprID != null && !"".equals(oprID)){
						// 如果一个账号有多个用户类型
						String sql = "select COUNT(1) from PS_TZ_PWZHGL_VW WHERE OPRID=?";
						int count = jdbcTemplate.queryForObject(sql, new Object[] { oprID }, "Integer");
						if (count > 1) {
							//查询用户类型对应的用户角色
							String userTypeSql = "SELECT ROLENAME FROM PS_TZ_JUGTYP_TBL WHERE TZ_JG_ID=? AND TZ_JUGTYP_ID=?";
							Map<String, Object> map1 = jdbcTemplate.queryForMap(userTypeSql, new Object[] { orgId,judTypeId });
							if (map1 != null) {
								String roleName = (String) map1.get("ROLENAME");
								if(roleName!=""){
									String roleSQL = "DELETE FROM PSROLEUSER WHERE ROLEUSER=? AND ROLENAME=?";
									jdbcTemplate.update(roleSQL, new Object[]{oprID,roleName});
								}
							}
							String yhlxSQL = "DELETE FROM PS_TZ_JUSR_REL_TBL WHERE TZ_JG_ID = ? AND OPRID=? AND TZ_JUGTYP_ID=?";
							jdbcTemplate.update(yhlxSQL, new Object[]{orgId,oprID,judTypeId});
							return strRet;
						} 
						// 删除行业类别关系表;
						/*List<?> judgeHYIDArr=new ArrayList<String>();
						judgeHYIDArr=jacksonUtil.getList("judgeHYID");
						PsTzPwHyTblKey psTzPwHyTblKey = new PsTzPwHyTblKey();
						for (int i = 0; i < judgeHYIDArr.size(); i++) {
							String sqlAudnum = "select COUNT(1) from PS_TZ_PW_HY_TBL WHERE TZ_JG_ID=? AND OPRID=? AND TZ_PWHY_ID=?";
							int count1 = jdbcTemplate.queryForObject(sqlAudnum, new Object[]{orgId,oprID,(String) judgeHYIDArr.get(i)},"Integer");
							if(count1< 0){
								
							}else{
								
								psTzPwHyTblKey.setTzPwhyId((String) judgeHYIDArr.get(i));
								psTzPwHyTblKey.setTzJgId(orgId);
								psTzPwHyTblKey.setOprid(oprID);
								psTzPwHyTblMapper.deleteByPrimaryKey(psTzPwHyTblKey);
							}
						}*/
						String hylbSQL = "DELETE FROM PS_TZ_PW_HY_TBL  WHERE TZ_JG_ID = ? AND OPRID=?";
						jdbcTemplate.update(hylbSQL, new Object[]{orgId,oprID});
						// 删除用户类型
						String yhlxSQL = "DELETE FROM PS_TZ_JUSR_REL_TBL WHERE TZ_JG_ID = ? AND OPRID=?";
						jdbcTemplate.update(yhlxSQL, new Object[]{orgId,oprID});
						// 删除评委 账号-类型-关系;
//						String yhlxSQL = "DELETE FROM PS_TZ_JUSR_REL_TBL WHERE OPRID=?";
//						jdbcTemplate.update(yhlxSQL, new Object[]{oprID});
						// 删除联系方式;
						String lsfsSQL = "DELETE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LYDX_ID=?";
						jdbcTemplate.update(lsfsSQL, new Object[]{oprID});
						//删除用户信息记录信息;
				    	String deleteYHXXSQL = "DELETE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
				    	jdbcTemplate.update(deleteYHXXSQL, new Object[]{accountNo,orgId} );
				    	
				    	//删除会员用户的注册信息;
				    	String deleteHYSQL = "DELETE FROM PS_TZ_REG_USER_T WHERE OPRID=?";
				    	jdbcTemplate.update(deleteHYSQL, new Object[]{oprID});
				    	
				    	//删除用户;
				    	String deleteOPDSQL = "DELETE FROM PSOPRDEFN WHERE OPRID=?";
				    	jdbcTemplate.update(deleteOPDSQL, new Object[]{oprID} );
				    	//删除用户角色;
				    	String deleteROLESQL = "DELETE FROM PSROLEUSER WHERE ROLEUSER=?";
				    	jdbcTemplate.update(deleteROLESQL, new Object[]{oprID});
				    	
				    	//删除评委扩展表，卢艳添加，2017-7-13
				    	String deleteExtSQL = "DELETE FROM PS_TZ_PW_EXT_T WHERE TZ_JG_ID=? AND OPRID=?";
				    	jdbcTemplate.update(deleteExtSQL, new Object[]{orgId,oprID});
				    	
					}
			    }
					
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	/*  */
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			String oprID = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				if ("JUDINFO".equals(strFlag)) {
//				if(jacksonUtil.containsKey("data")){
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					// 用户账号;
					String accountNo = (String) infoData.get("accountNo");
					// 所属机构编号;
					String orgID = (String) infoData.get("orgId");
					// 密码;
					String password = (String) infoData.get("password");
					password = DESUtil.encrypt(password, "TZGD_Tranzvision");
					// 用户描述
					String judgeName = (String) infoData.get("judgeName");
					// 手机、邮箱
					String judgePhoneNumber = (String) infoData.get("judgePhoneNumber");
					String judgeEmail = (String) infoData.get("judgeEmail");
					// 用户角色
//					String roleName = (String) infoData.get("roleName");
//					String roleNameDesc = (String) infoData.get("roleNameDesc");
					// 用户类型
//					String userType = (String) infoData.get("userType");
					// 人员类型，激活状态
//					String tzRylx = (String) infoData.get("rylx");
//					String tzJihuoZt = "Y";
					// 备注
					String str_beizhu = (String) infoData.get("beizhu");
					// 锁定用户;
					String clockFlag = "";
					
					String tzRylxSql = "SELECT TZ_RYLX from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
					String tzRylx = jdbcTemplate.queryForObject(tzRylxSql, new Object[] { orgID, accountNo }, "String");
					
					if (infoData.containsKey("clockFlag")) { clockFlag = (String) infoData.get("clockFlag");}
					String originOpridSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_DLZH_ID=?";
					oprID = jdbcTemplate.queryForObject(originOpridSql, new Object[] { orgID, accountNo }, "String");
//FDSFASFSDFDSGDFGFD
					// 备注;
					int isNum = 0;
					String isExistSQL = "SELECT COUNT(1) FROM PS_TZ_REG_USER_T WHERE OPRID=?";
					isNum = jdbcTemplate.queryForObject(isExistSQL, new Object[] { oprID },"Integer");
					if (isNum <= 0) {
						String lsfsSQL = "INSERT INTO PS_TZ_REG_USER_T(OPRID,TZ_REALNAME,TZ_BEIZHU) VALUES(?,?,?)";
						jdbcTemplate.update(lsfsSQL, new Object[]{oprID, judgeName, str_beizhu});
					}else{
						if ((str_beizhu != null && !"".equals(str_beizhu))) {
							String updateBeizhuSql = "UPDATE PS_TZ_REG_USER_T SET TZ_REALNAME=?,TZ_BEIZHU=? WHERE OPRID=?";
							jdbcTemplate.update(updateBeizhuSql, new Object[]{judgeName,str_beizhu, oprID});
						}
					}
					String updateOprdSql = "update PS_TZ_AQ_YHXX_TBL set TZ_DLZH_ID=?, TZ_REALNAME = ?,TZ_RYLX = ?, TZ_EMAIL  = ?,TZ_MOBILE = ?,ROW_LASTMANT_DTTM = curdate(),ROW_LASTMANT_OPRID = ? where OPRID=?";
					String updateOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					jdbcTemplate.update(updateOprdSql, new Object[]{accountNo,judgeName, tzRylx,judgeEmail, judgePhoneNumber,updateOprid, oprID});

					/*short userTypeNum;
					if ("Y".equals(userType) || "on".equals(userType)) {
						userTypeNum = 2;
					} else {
						userTypeNum = 0;
					}*/
					short clockFlagNum;
					if ("Y".equals(clockFlag) || "on".equals(clockFlag) || "1".equals(clockFlag)) {
						clockFlagNum = 1;
					} else {
						clockFlagNum = 0;
					}
					
					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(oprID);
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock(clockFlagNum);
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(updateOprid);
					psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
					Boolean isString=infoData.get("judgeHY") instanceof String;
					if(!isString){
						// 先删除行业类别
						String hylbSQL = "DELETE FROM PS_TZ_PW_HY_TBL  WHERE TZ_JG_ID = ? AND OPRID=?";
						jdbcTemplate.update(hylbSQL, new Object[]{orgID,oprID});
						// 行业类别;
						ArrayList<String> judgeHYArr=new ArrayList<String>();
						judgeHYArr=(ArrayList<String>) infoData.get("judgeHY");
						PsTzPwHyTblKey psTzPwHyTblKey = new PsTzPwHyTblKey();
						for (int i = 0; i < judgeHYArr.size(); i++) {
							String tmp=judgeHYArr.get(i);
							boolean matches=true;
							
							if (tmp != null && !"".equals(tmp.trim())) {
								matches = tmp.matches("^[0-9]*$");
							}    
							if(matches==false){
								String isExist = "select TZ_ZHZ_ID FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_PWHY_ID' AND TZ_ZHZ_DMS=?";
								tmp = jdbcTemplate.queryForObject(isExist, new Object[] { tmp },"String");
							}
							String sqlAudnum = "select COUNT(1) from PS_TZ_PW_HY_TBL WHERE TZ_JG_ID=? AND OPRID=? AND TZ_PWHY_ID=?";
							int count1 = jdbcTemplate.queryForObject(sqlAudnum, new Object[]{orgID,oprID,(String) judgeHYArr.get(i)},"Integer");
							if(count1 > 0){
								
							}else{	
								psTzPwHyTblKey.setTzPwhyId(tmp);
								psTzPwHyTblKey.setTzJgId(orgID);
								psTzPwHyTblKey.setOprid(oprID);
								psTzPwHyTblMapper.insert(psTzPwHyTblKey);
							}
						}
					}else{
						String hylbSQL = "DELETE FROM PS_TZ_PW_HY_TBL  WHERE TZ_JG_ID = ? AND OPRID=?";
						jdbcTemplate.update(hylbSQL, new Object[]{orgID,oprID});
					}
					// 先删除用户类型和用户角色
					String yhlxDSQL = "DELETE FROM PS_TZ_JUSR_REL_TBL  WHERE TZ_JG_ID = ? AND OPRID=?";
					jdbcTemplate.update(yhlxDSQL, new Object[]{orgID,oprID});
					String roleSQL = "DELETE FROM PSROLEUSER WHERE ROLEUSER=? ";
					jdbcTemplate.update(roleSQL, new Object[]{oprID});
					// 用户类型;
					//Boolean isArr=infoData.get("judgeType").getClass().isArray();
					Boolean isStr=infoData.get("judgeType") instanceof String;
					if(!isStr){
						ArrayList<String> judgeTypeArr=new ArrayList<String>();
						judgeTypeArr=(ArrayList<String>) infoData.get("judgeType");
						for (int i = 0; i < judgeTypeArr.size(); i++) {
							String yhlxSQL = "INSERT INTO PS_TZ_JUSR_REL_TBL(TZ_JG_ID,OPRID,TZ_JUGTYP_ID) VALUES(?,?,?)";
							jdbcTemplate.update(yhlxSQL, new Object[]{orgID, oprID, judgeTypeArr.get(i)});
							//添加用户角色
							//查询用户类型对应的用户角色
							String userTypeSql = "SELECT ROLENAME FROM PS_TZ_JUGTYP_TBL WHERE TZ_JG_ID=? AND TZ_JUGTYP_ID=?";
							Map<String, Object> map1 = jdbcTemplate.queryForMap(userTypeSql, new Object[] { orgID,judgeTypeArr.get(i) });
							if (map1 != null) {
								String roleName = (String) map1.get("ROLENAME");
								if(roleName!=""){
									String role1SQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,?)";
									jdbcTemplate.update(role1SQL, new Object[]{oprID,roleName,"N"});
								}
							}
						}
					}else{
						String judgeType=(String)infoData.get("judgeType");
						String yhlxSQL = "INSERT INTO PS_TZ_JUSR_REL_TBL(TZ_JG_ID,OPRID,TZ_JUGTYP_ID) VALUES(?,?,?)";
						jdbcTemplate.update(yhlxSQL, new Object[]{orgID, oprID, judgeType});
						//添加用户角色
						//查询用户类型对应的用户角色
						String userTypeSql = "SELECT ROLENAME FROM PS_TZ_JUGTYP_TBL WHERE TZ_JG_ID=? AND TZ_JUGTYP_ID=?";
						Map<String, Object> map1 = jdbcTemplate.queryForMap(userTypeSql, new Object[] { orgID,judgeType});
						if (map1 != null) {
							String roleName = (String) map1.get("ROLENAME");
							if(roleName!=""){
								String role1SQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,?)";
								jdbcTemplate.update(role1SQL, new Object[]{oprID,roleName,"N"});
							}
						}
					}
					
					// 联系方式;
					int isExistNum = 0;
					String isExistLXFS = "SELECT COUNT(1) FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY=? and TZ_LYDX_ID=?";
					isExistNum = jdbcTemplate.queryForObject(isExistLXFS, new Object[] { tzRylx, oprID },
								"Integer");
					
					if (isExistNum <= 0) {
						if ((judgePhoneNumber != null && !"".equals(judgePhoneNumber)) || (judgeEmail != null && !"".equals(judgeEmail))) {
							String lsfsSQL = "INSERT INTO PS_TZ_LXFSINFO_TBL(TZ_LXFS_LY,TZ_LYDX_ID,TZ_ZY_SJ,TZ_ZY_EMAIL) VALUES(?,?,?,?)";
							jdbcTemplate.update(lsfsSQL, new Object[]{tzRylx, oprID, judgePhoneNumber, judgeEmail});
							
						}
					} else {
						String updatelSFSSql = "UPDATE PS_TZ_LXFSINFO_TBL SET TZ_ZY_SJ=?,TZ_ZY_EMAIL=? WHERE TZ_LXFS_LY=? AND TZ_LYDX_ID=?";
						jdbcTemplate.update(updatelSFSSql, new Object[]{judgePhoneNumber, judgeEmail, tzRylx, oprID});

					}
					// 用户角色;
					/*int roleNum = 0;
					String isExistRole = "SELECT COUNT(1) FROM PSROLEUSER WHERE ROLEUSER=? ";
					roleNum = jdbcTemplate.queryForObject(isExistRole, new Object[] { oprID },
								"Integer");
					
					if (roleNum <= 0) {
						if ((judgePhoneNumber != null && !"".equals(judgePhoneNumber)) || (judgeEmail != null && !"".equals(judgeEmail))) {
							String roleSQL = "INSERT INTO PSROLEUSER(ROLEUSER,ROLENAME,DYNAMIC_SW) VALUES(?,?,?)";
							jdbcTemplate.update(roleSQL, new Object[]{oprID,roleName,"N"});
						}
					} else {
						String roleSQL = "UPDATE PSROLEUSER SET ROLENAME=?,DYNAMIC_SW=? WHERE ROLEUSER=? ";
						jdbcTemplate.update(roleSQL, new Object[]{roleName, "N", oprID});

					}*/
					// 评委 账号-类型-关系;
//					String updateZhlxSql = "UPDATE PS_TZ_JUSR_REL_TBL SET TZ_JUGTYP_ID=? WHERE TZ_JG_ID=? AND OPRID=?";
//					jdbcTemplate.update(updateZhlxSql, new Object[]{userTypeNum, orgID, oprID});
					
					// 院内院外，卢艳添加，2017-7-13
					String judgeYnyw = (String) infoData.get("judgeYnyw");
					if(judgeYnyw!=null && !"".equals(judgeYnyw)) {
						PsTzPwExtTKey psTzPwExtTKey = new PsTzPwExtTKey();
						psTzPwExtTKey.setTzJgId(orgID);
						psTzPwExtTKey.setOprid(oprID);
						
						PsTzPwExtT psTzPwExtT = psTzPwExtTMapper.selectByPrimaryKey(psTzPwExtTKey);
						if(psTzPwExtT==null) {
							psTzPwExtT = new PsTzPwExtT();
							psTzPwExtT.setTzJgId(orgID);
							psTzPwExtT.setOprid(oprID);
							psTzPwExtT.setTzYnyw(judgeYnyw);
							psTzPwExtTMapper.insertSelective(psTzPwExtT);
						} else {
							psTzPwExtT.setTzYnyw(judgeYnyw);
							psTzPwExtTMapper.updateByPrimaryKeySelective(psTzPwExtT);
						}
					}

				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	@Override
	public String tzOther(String operateType, String strParams, String[] errMsg) {
		String strRet = "";
		
		try {
			//获取评委类型
			if("tzGetJudgeType".equals(operateType)) {
				strRet = this.getJudgeType(strParams, errMsg);
			}
			
			//给评委发送邮件
			if("tzSendEmail".equals(operateType)) {
				strRet = sendEmail(strParams,errMsg);
			}
			
			//给评委发送短信
			if("tzSendMessage".equals(operateType)) {
				strRet = sendMessage(strParams,errMsg);
			}
			
			//获取可配置搜索sql
			if("getSearchSql".equals(operateType)){
				strRet = this.tzGetSearchSql(strParams, errMsg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	//获取可配置搜索sql语句
	private String tzGetSearchSql(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("searchSql", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			/*可配置搜索查询语句*/
			String[] resultFldArray = {"OPRID"};
			
			String[][] orderByArr=null;
			
			String strParamsTmp = strParams.replaceAll("'", "\"");
			String searchSql = fliterForm.getQuerySQL(resultFldArray,orderByArr,strParamsTmp,errorMsg);
			
			rtnMap.replace("searchSql", searchSql);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	//获取评委类型
	public String getJudgeType(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			
			//当前机构
			String jgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			String sql1="select TZ_JUGTYP_ID,TZ_JUGTYP_NAME FROM PS_TZ_JUGTYP_TBL WHERE TZ_JG_ID=?";
			List<Map<String, Object>> tagList = jdbcTemplate.queryForList(sql1,new Object[]{jgId});

			// 总数;
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_JUGTYP_TBL WHERE TZ_JG_ID=?";
			int numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { jgId }, "Integer");
			mapRet.replace("total", numTotal);
			mapRet.replace("root", tagList);
			
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	/*给评委发送邮件*/
	public String sendEmail(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			jacksonUtil.json2Map(strParams);
			String judgeOpridDesc = jacksonUtil.getString("judgeOpridDesc");
			String searchCfg = jacksonUtil.getString("searchCfg");
			
			List<String> listJudgeOprid = new ArrayList<String>();
			if(!"".equals(judgeOpridDesc)) {
				String[] strOprid = judgeOpridDesc.split(",");
				listJudgeOprid = Arrays.asList(strOprid);
			} else {
				List<Map<String, Object>> listOprids = new ArrayList<Map<String,Object>>();
				if(!"".equals(searchCfg)){
					jacksonUtil.json2Map(this.tzGetSearchSql(searchCfg, errMsg));
					String searchSql = jacksonUtil.getString("searchSql");
					listOprids = jdbcTemplate.queryForList(searchSql);
					for(Map<String, Object> mapOprids : listOprids) {
						String orpidTmp = mapOprids.get("OPRID") == null ? "" : mapOprids.get("OPRID").toString();
						listJudgeOprid.add(orpidTmp);
					}
				}
			}
			
			
			//创建邮件发送听众
			String crtAudi = createTaskServiceImpl.createAudience("",currentOrgId,"评委账号邮件发送", "PWZH");
			
			if (!"".equals(crtAudi)) {
				
				//添加听众成员
				for(Object judgeOprid : listJudgeOprid) {
					
					String sql = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
					Map<String, Object> mapData = jdbcTemplate.queryForMap(sql,new Object[]{currentOrgId,judgeOprid});
					if(mapData!=null) {
						String name = mapData.get("TZ_REALNAME") == null ? "" : mapData.get("TZ_REALNAME").toString();
						String email = mapData.get("TZ_EMAIL") == null ? "" : mapData.get("TZ_EMAIL").toString();
						String mobile = mapData.get("TZ_MOBILE") == null ? "" : mapData.get("TZ_MOBILE").toString();
				
						createTaskServiceImpl.addAudCy(crtAudi,name, "", mobile, "", email, "", "", String.valueOf(judgeOprid), "",
							"", "");
					}
				}
				
				
				mapRet.put("audienceId", crtAudi);
				strRet = jacksonUtil.Map2json(mapRet);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*给评委发送短信*/
	public String sendMessage(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			
			jacksonUtil.json2Map(strParams);
			String judgeOpridDesc = jacksonUtil.getString("judgeOpridDesc");
			String searchCfg = jacksonUtil.getString("searchCfg");
			
			List<String> listJudgeOprid = new ArrayList<String>();
			if(!"".equals(judgeOpridDesc)) {
				String[] strOprid = judgeOpridDesc.split(",");
				listJudgeOprid = Arrays.asList(strOprid);
			} else {
				List<Map<String, Object>> listOprids = new ArrayList<Map<String,Object>>();
				if(!"".equals(searchCfg)){
					jacksonUtil.json2Map(this.tzGetSearchSql(searchCfg, errMsg));
					String searchSql = jacksonUtil.getString("searchSql");
					listOprids = jdbcTemplate.queryForList(searchSql);
					for(Map<String, Object> mapOprids : listOprids) {
						String orpidTmp = mapOprids.get("OPRID") == null ? "" : mapOprids.get("OPRID").toString();
						listJudgeOprid.add(orpidTmp);
					}
				}
			}
			
			//创建邮件发送听众
			String crtAudi = createTaskServiceImpl.createAudience("",currentOrgId,"评委账号短信发送", "PWZH");
			
			if (!"".equals(crtAudi)) {
				
				//添加听众成员
				for(Object judgeOprid : listJudgeOprid) {
					
					String sql = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
					Map<String, Object> mapData = jdbcTemplate.queryForMap(sql,new Object[]{currentOrgId,judgeOprid});
					if(mapData!=null) {
						String name = mapData.get("TZ_REALNAME") == null ? "" : mapData.get("TZ_REALNAME").toString();
						String email = mapData.get("TZ_EMAIL") == null ? "" : mapData.get("TZ_EMAIL").toString();
						String mobile = mapData.get("TZ_MOBILE") == null ? "" : mapData.get("TZ_MOBILE").toString();
				
						createTaskServiceImpl.addAudCy(crtAudi,name, "", mobile, "", email, "", "", String.valueOf(judgeOprid), "",
							"", "");
					}
				}
				
				
				mapRet.put("audienceId", crtAudi);
				strRet = jacksonUtil.Map2json(mapRet);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}

}


