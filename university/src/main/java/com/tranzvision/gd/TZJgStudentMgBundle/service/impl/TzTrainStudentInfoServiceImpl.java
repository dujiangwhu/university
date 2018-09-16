/**
 * 
 */
package com.tranzvision.gd.TZJgStudentMgBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxStudentTMapper;
import com.tranzvision.gd.TZPXBundle.model.PxStudentT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZJgStudentMgBundle.service.impl.TzTrainStudentInfoServiceImpl")
public class TzTrainStudentInfoServiceImpl extends FrameworkImpl {

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
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsroleuserMapper psroleuserMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	/**
	 * 获取机构学员信息
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
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("OPRID") && jacksonUtil.containsKey("ORGID")) {

				// oprid;
				String str_oprid = jacksonUtil.getString("OPRID");
				String str_orgid = jacksonUtil.getString("ORGID");
				// 头像地址;
				String titleImageUrlSQL = "SELECT B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A , "
						+ "PS_TZ_OPR_PHOTO_T B WHERE A.OPRID=? AND A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA";
				Map<String, Object> imgMap = sqlQuery.queryForMap(titleImageUrlSQL, new Object[] { str_oprid });
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
				
				System.out.println(str_oprid);
				
				System.out.println(str_orgid);
				
				Map<String, Object> userMap = sqlQuery.queryForMap("SELECT TZ_REALNAME,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? AND TZ_JG_ID = ?",
						new Object[] { str_oprid ,str_orgid});
				
				

				if (userMap == null) {
					errMsg[0] = "1";
					errMsg[1] = "不存在该用户！";
				} else {
					String name = (String) userMap.get("TZ_REALNAME");
					String phone = (String) userMap.get("TZ_MOBILE");
									
					PxStudentT pxStudentT = pxStudentTMapper.selectByPrimaryKey(str_oprid);
					
					if (pxStudentT == null) {
						errMsg[0] = "1";
						errMsg[1] = "不存在该用户！";
					} else {
						
						int stuUseTimeCard = sqlQuery.queryForObject("SELECT IFNULL(SUM(X.TZ_CHANGE),0) FROM PK_STU_COURSE_CHANGE_T X WHERE X.OPRID = ? AND X.TZ_CHANGE_TYPE = '1'",
								new Object[] { str_oprid },"Integer");
						
						int stuBackTimeCard = sqlQuery.queryForObject("SELECT IFNULL(SUM(X.TZ_CHANGE),0) FROM PK_STU_COURSE_CHANGE_T X WHERE X.OPRID = ? AND X.TZ_CHANGE_TYPE = '2'",
								new Object[] { str_oprid },"Integer");
						
						stuUseTimeCard = stuUseTimeCard - stuBackTimeCard;
						
						Map<String, Object> jsonMap2 = new HashMap<String, Object>();
						jsonMap2.put("titleImageUrl", titleImageUrl);
						jsonMap2.put("oprid", pxStudentT.getOprid());
						jsonMap2.put("orgid", pxStudentT.getTzJgId());
						jsonMap2.put("name", name);
						jsonMap2.put("phone", phone);
						jsonMap2.put("sex", pxStudentT.getSex());
						jsonMap2.put("age", pxStudentT.getAge());
						jsonMap2.put("qq", pxStudentT.getQq());
						jsonMap2.put("email", pxStudentT.getEmail());
						jsonMap2.put("contactor", pxStudentT.getContact());
						jsonMap2.put("contactorPhone", pxStudentT.getContactPhone());
						jsonMap2.put("contactorAddress", pxStudentT.getContactAddress());
						jsonMap2.put("statu", pxStudentT.getStuStatus());
						jsonMap2.put("timecardRemaind", pxStudentT.getTimecardRemaind());
						
						jsonMap2.put("timecardUsed", stuUseTimeCard);

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

	/**
	 * 添加学员
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
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
				String tzJgId= jacksonUtil.getString("orgid").trim();
				String phone= jacksonUtil.getString("phone").trim();
				String usName = jacksonUtil.getString("name").trim();
				String usStatu = jacksonUtil.getString("statu").trim();
				String email = jacksonUtil.getString("email").trim();
				if(phone==null||"".equals(phone)){
					errMsg[0] = "1";
					errMsg[1] = "保存失败,手机必须填写";
					
				}else{
					
					int count = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID = ? AND TZ_JG_ID = ?", 
							new Object[] { phone,tzJgId }, "Integer");
				
					
					if(count>0){
						errMsg[0] = "1";
						errMsg[1] = "手机号为" + phone + "的学员已存在。";
					}else{
						// 账号密码;
						String password = "123456";
						password = DESUtil.encrypt(password, "TZGD_Tranzvision");
						/*创建用户信息*/
						String oprID = "";
						oprID = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
						PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
						psTzAqYhxxTbl.setTzDlzhId(phone);
						psTzAqYhxxTbl.setTzJgId(tzJgId);
						psTzAqYhxxTbl.setOprid(oprID);
						psTzAqYhxxTbl.setTzRealname(usName);
						psTzAqYhxxTbl.setTzMobile(phone);
						psTzAqYhxxTbl.setTzEmail(email);
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
						if ("Y".equals(usStatu)) {
							acctLockNum = 0;
						} else {
							acctLockNum = 1;
						}
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
						pxStudentT.setSex(jacksonUtil.getString("sex"));
						pxStudentT.setAge(jacksonUtil.getInt("age"));
						pxStudentT.setQq(jacksonUtil.getString("qq"));
						pxStudentT.setEmail(jacksonUtil.getString("email"));
						pxStudentT.setPhone(phone);
						pxStudentT.setContact(jacksonUtil.getString("contactor"));
						pxStudentT.setContactPhone(jacksonUtil.getString("contactorPhone"));
						pxStudentT.setContactAddress(jacksonUtil.getString("contactorAddress"));
						pxStudentT.setTimecardRemaind(0);
						pxStudentT.setTimecardUsed(0);
						pxStudentT.setStuStatus(usStatu);
						pxStudentTMapper.insert(pxStudentT);
						
						/*添加学员注册信息表*/
						PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
						psTzRegUserT.setOprid(oprID);
						psTzRegUserT.setTzRealname(usName);
						psTzRegUserT.setTzGender(jacksonUtil.getString("sex"));
						psTzRegUserT.setTzComment1(String.valueOf(jacksonUtil.getInt("age")));
						psTzRegUserT.setTzComment2(jacksonUtil.getString("qq"));
						psTzRegUserT.setTzComment3(jacksonUtil.getString("contactor"));
						psTzRegUserT.setTzComment4(jacksonUtil.getString("contactorPhone"));
						psTzRegUserT.setTzComment5(jacksonUtil.getString("contactorAddress"));
						psTzRegUserTMapper.insertSelective(psTzRegUserT);
						
						//将机构ID返回
						Map<String, Object> mapRet = new HashMap<String, Object>();
						mapRet.put("orgid", tzJgId);
						mapRet.put("oprid", oprID);
						
						strRet = jacksonUtil.Map2json(mapRet);
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
	
	/**
	 * 修改修改学员信息信息
	 */
	@Override
	@Transactional
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
				String orgid=jacksonUtil.getString("orgid");
				String phone = jacksonUtil.getString("phone").trim();
				System.out.println(jacksonUtil.getString("sex"));
				if(oprid==null){
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}else{
					
					PxStudentT pxStudentT=pxStudentTMapper.selectByPrimaryKey(oprid);
					if(pxStudentT==null){
						errMsg[0] = "1";
						errMsg[1] = "用户不存在！";
					}else{
						pxStudentT.setSex(jacksonUtil.getString("sex"));
						pxStudentT.setAge(jacksonUtil.getInt("age"));
						pxStudentT.setQq(jacksonUtil.getString("qq"));
						pxStudentT.setEmail(jacksonUtil.getString("email"));
						pxStudentT.setContact(jacksonUtil.getString("contactor"));
						pxStudentT.setContactPhone(jacksonUtil.getString("contactorPhone"));
						pxStudentT.setContactAddress(jacksonUtil.getString("contactorAddress"));
						pxStudentT.setStuStatus(jacksonUtil.getString("statu"));
						pxStudentTMapper.updateByPrimaryKeySelective(pxStudentT);
						
						PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
						psTzAqYhxxTblKey.setTzDlzhId(phone);
						psTzAqYhxxTblKey.setTzJgId(orgid);
						
						PsTzAqYhxxTbl psTzAqYhxxTbl = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);
						psTzAqYhxxTbl.setTzEmail(jacksonUtil.getString("email"));
						String updateOperid = tzLoginServiceImpl.getLoginedManagerOprid(request);
						psTzAqYhxxTbl.setRowLastmantDttm(new Date());
						psTzAqYhxxTbl.setRowLastmantOprid(updateOperid);
						psTzAqYhxxTblMapper.updateByPrimaryKey(psTzAqYhxxTbl);
						
						/*添加学员注册信息表*/
						PsTzRegUserT psTzRegUserT = psTzRegUserTMapper.selectByPrimaryKey(oprid);
						if(psTzRegUserT!=null){
							System.out.println(jacksonUtil.getString("sex"));
							System.out.println(jacksonUtil.getString("qq"));
							System.out.println(jacksonUtil.getString("age"));
							System.out.println(jacksonUtil.getString("contactor"));
							System.out.println(jacksonUtil.getString("contactorPhone"));
							psTzRegUserT.setTzGender(jacksonUtil.getString("sex"));
							psTzRegUserT.setTzComment1(String.valueOf(jacksonUtil.getInt("age")));
							psTzRegUserT.setTzComment2(jacksonUtil.getString("qq"));
							psTzRegUserT.setTzComment3(jacksonUtil.getString("contactor"));
							psTzRegUserT.setTzComment4(jacksonUtil.getString("contactorPhone"));
							psTzRegUserT.setTzComment5(jacksonUtil.getString("contactorAddress"));
							psTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
						}else{
							psTzRegUserT = new PsTzRegUserT();
							psTzRegUserT.setOprid(oprid);
							psTzRegUserT.setTzRealname(jacksonUtil.getString("name"));
							psTzRegUserT.setTzGender(jacksonUtil.getString("sex"));
							psTzRegUserT.setTzComment1(String.valueOf(jacksonUtil.getInt("age")));
							psTzRegUserT.setTzComment2(jacksonUtil.getString("qq"));
							psTzRegUserT.setTzComment3(jacksonUtil.getString("contactor"));
							psTzRegUserT.setTzComment4(jacksonUtil.getString("contactorPhone"));
							psTzRegUserT.setTzComment5(jacksonUtil.getString("contactorAddress"));
							psTzRegUserTMapper.insertSelective(psTzRegUserT);
						}
						
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