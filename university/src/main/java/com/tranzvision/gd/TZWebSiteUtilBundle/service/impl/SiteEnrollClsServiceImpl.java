package com.tranzvision.gd.TZWebSiteUtilBundle.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsShowPrjNewsTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsShowPrjNewsTKey;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZPXBundle.dao.PkTeaIntegralChangeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxStudentTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PkTeaIntegralChangeT;
import com.tranzvision.gd.TZPXBundle.model.PxStudentT;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.TZWebSiteRegisteBundle.dao.PsTzDzyxYzmTblMapper;
import com.tranzvision.gd.TZWebSiteRegisteBundle.model.PsTzDzyxYzmTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.gh.sms.SmsService;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.httpclient.CommonUtils;

/**
 * 
 * @author tang 原：TZ_SITE_UTIL_APP:TZ_SITE_ENROLL_CLS
 */
@Service("com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteEnrollClsServiceImpl")
public class SiteEnrollClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private ValidateUtil validateUtil;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private RegisteMalServiceImpl registeMalServiceImpl;
	@Autowired
	private RegisteSmsServiceImpl registeSmsServiceImpl;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	@Autowired
	private PsroleuserMapper psroleuserMapper;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzDzyxYzmTblMapper psTzDzyxYzmTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsShowPrjNewsTMapper psShowPrjNewsTMapper;

	@Autowired
	private PxStudentTMapper pxStudentTMapper;

	@Autowired
	private PxTeacherMapper pxTeacherMapper;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	@Autowired
	private SmsService smsService;

	@Autowired
	private PkTeaIntegralChangeTMapper pkTeaIntegralChangeTMapper;

	// 原：WEBLIB_GD_USER.TZ_REG.FieldFormula.Iscript_GetNowField
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);
			// 得到要验证的验证字段
			if ("GETNOWFIELD".equals(oprType)) {
				// String strJgid = jacksonUtil.getString("strJgid");
				String strSiteId = jacksonUtil.getString("siteId");
				// String sql = "SELECT TZ_REG_FIELD_ID,TZ_IS_REQUIRED FROM
				// PS_TZ_REG_FIELD_T WHERE TZ_ENABLE='Y' AND TZ_JG_ID=? ORDER BY
				// TZ_ORDER ASC";
				String sql = "SELECT TZ_REG_FIELD_ID,TZ_IS_REQUIRED FROM PS_TZ_REG_FIELD_T WHERE TZ_ENABLE='Y' AND TZ_SITEI_ID=? ORDER BY TZ_ORDER ASC";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strSiteId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						returnMap.put((String) list.get(i).get("TZ_REG_FIELD_ID"), list.get(i).get("TZ_IS_REQUIRED"));
					}
				}
			}

			// 发送的验证码判断
			if ("CODEVALIDATOR".equals(oprType)) {
				String codeValue = jacksonUtil.getString("yzm").trim();
				String yzfs = jacksonUtil.getString("yzfs").trim();
				String email = jacksonUtil.getString("TZ_EMAIL").trim();
				String mobile = jacksonUtil.getString("TZ_MOBILE").trim();
				String strJgid = jacksonUtil.getString("strJgid").trim();
				String strSiteId = jacksonUtil.getString("siteId").trim();
				if ("M".equals(yzfs)) {
					String sql = "SELECT TZ_SJYZM FROM PS_TZ_SHJI_YZM_TBL WHERE TZ_EFF_FLAG='Y' AND TZ_JG_ID=? AND TZ_MOBILE_PHONE=? AND TZ_SITEI_ID=? ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
					String sysYzm = jdbcTemplate.queryForObject(sql, new Object[] { strJgid, mobile, strSiteId },
							"String");

					if (sysYzm == null || "".equals(sysYzm) || !sysYzm.equals(codeValue)) {
						returnMap.put("resultFlg", "error");
						returnMap.put("errorDescr", "验证码错误");
					} else {
						returnMap.put("resultFlg", "success");
						returnMap.put("errorDescr", "");
					}
				} else {
					String sql = "SELECT TZ_YJYZM FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_EFF_FLAG='Y' AND TZ_JG_ID=? AND TZ_EMAIL=? ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
					String sysYzm = jdbcTemplate.queryForObject(sql, new Object[] { strJgid, email }, "String");
					if (sysYzm == null || "".equals(sysYzm) || !sysYzm.equals(codeValue)) {
						returnMap.put("resultFlg", "error");
						returnMap.put("errorDescr", "验证码错误");
					} else {
						returnMap.put("resultFlg", "success");
						returnMap.put("errorDescr", "");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(returnMap);
	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strSen = "";
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("sen")) {
				strSen = jacksonUtil.getString("sen");
				if ("1".equals(strSen)) {
					strResponse = this.checkCodeVerifyByActive(strParams, errMsg);
				}

				if ("2".equals(strSen)) {
					strResponse = this.saveEnrollInfo(strParams, errMsg);
				}

				if ("3".equals(strSen)) {
					strResponse = this.checkCodeVerifyByPass(strParams, errMsg);
				}

				if ("4".equals(strSen)) {
					strResponse = this.modifyPasswordByPass(strParams, errMsg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = "获取数据失败，请联系管理员";
		}

		return strResponse;
	}

	public String checkCodeVerifyByActive(String strParams, String[] errMsg) {
		String strCheckCode = "";

		String strOrgid = "";
		String strLang = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("checkCode") && jacksonUtil.containsKey("lang")
					&& jacksonUtil.containsKey("orgid"))
				strCheckCode = jacksonUtil.getString("checkCode").trim();
			strLang = jacksonUtil.getString("lang").trim();
			strOrgid = jacksonUtil.getString("orgid").trim();
			// 校验验证码
			Patchca patchca = new Patchca();
			if (!patchca.verifyToken(request, strCheckCode)) {
				errMsg[0] = "2";
				errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "50",
						"验证码不正确", "The security code is incorrect");
				return strResult;
			}

			strResult = "\"success\"";
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}

		return strResult;
	}

	/**
	 * 注册相关修改，只允许老师注册，需要更具站点的信息 来判断
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return
	 */
	public String saveEnrollInfo(String strParams, String[] errMsg) {
		String strResult = "\"failure\"";
		String strJumUrl = "";
		String strOrgId = "";
		String strSiteId = "";
		String strLang = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strOrgId = jacksonUtil.getString("orgid");
			strSiteId = jacksonUtil.getString("siteId");
			strLang = jacksonUtil.getString("lang");
			if (strOrgId == null || "".equals(strOrgId)) {
				errMsg[0] = "100";
				errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE", "55",
						"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
				return strResult;
			}

			String teaSiteID = getHardCodePoint.getHardCodePointVal("TZ_TEA_MH");
			if (teaSiteID.equals("strSiteId")) {
				errMsg[0] = "100";
				errMsg[1] = "只允许教师注册";
				return strResult;
			}

			boolean isMobile = CommonUtils.isMobile(request);

			if (jacksonUtil.containsKey("data")) {
				Map<String, Object> dataMap = jacksonUtil.getMap("data");

				// 姓名;
				String strTZ_REALNAME = "";
				if (dataMap.containsKey("TZ_REALNAME")) {
					strTZ_REALNAME = ((String) dataMap.get("TZ_REALNAME")).trim();
				}

				// FIRST_NAME;
				String strTZ_FIRST_NAME = "";
				if (dataMap.containsKey("TZ_FIRST_NAME")) {
					strTZ_FIRST_NAME = ((String) dataMap.get("TZ_FIRST_NAME")).trim();
				}

				// LAST_NAME;
				String strTZ_LAST_NAME = "";
				if (dataMap.containsKey("TZ_LAST_NAME")) {
					strTZ_LAST_NAME = ((String) dataMap.get("TZ_LAST_NAME")).trim();
				}

				// 性别;
				String strTZ_GENDER = "";
				if (dataMap.containsKey("TZ_GENDER")) {
					strTZ_GENDER = ((String) dataMap.get("TZ_GENDER")).trim();
				}

				// 出生日期;
				String strBIRTHDATE = "";
				if (dataMap.containsKey("BIRTHDATE")) {
					strBIRTHDATE = ((String) dataMap.get("BIRTHDATE")).trim();
				}

				// 密码;
				String strTZ_PASSWORD = "";
				if (dataMap.containsKey("TZ_PASSWORD")) {
					strTZ_PASSWORD = ((String) dataMap.get("TZ_PASSWORD")).trim();
				}

				// 确认密码;
				String strTZ_REPASSWORD = "";
				if (dataMap.containsKey("TZ_REPASSWORD")) {
					strTZ_REPASSWORD = ((String) dataMap.get("TZ_REPASSWORD")).trim();
				}

				// 邮箱;
				String strTZ_EMAIL = "";
				if (dataMap.containsKey("TZ_EMAIL")) {
					strTZ_EMAIL = ((String) dataMap.get("TZ_EMAIL")).trim();
				}

				// 手机;
				String strTZ_MOBILE = "";
				if (dataMap.containsKey("TZ_MOBILE")) {
					strTZ_MOBILE = ((String) dataMap.get("TZ_MOBILE")).trim();
				}

				// SKYPE账号;
				String strTZ_SKYPE = "";
				if (dataMap.containsKey("TZ_SKYPE")) {
					strTZ_SKYPE = ((String) dataMap.get("TZ_SKYPE")).trim();
				}

				// 行业类型;
				String strTZ_COMP_INDUSTRY = "";
				if (dataMap.containsKey("TZ_COMP_INDUSTRY")) {
					strTZ_COMP_INDUSTRY = ((String) dataMap.get("TZ_COMP_INDUSTRY")).trim();
				}

				// 公司名称;
				String strTZ_COMPANY_NAME = "";
				if (dataMap.containsKey("TZ_COMPANY_NAME")) {
					strTZ_COMPANY_NAME = ((String) dataMap.get("TZ_COMPANY_NAME")).trim();
				}

				// 部门;
				String strTZ_DEPTMENT = "";
				if (dataMap.containsKey("TZ_DEPTMENT")) {
					strTZ_DEPTMENT = ((String) dataMap.get("TZ_DEPTMENT")).trim();
				}

				// 职务;
				String strTZ_ZHIWU = "";
				if (dataMap.containsKey("TZ_ZHIWU")) {
					strTZ_ZHIWU = ((String) dataMap.get("TZ_ZHIWU")).trim();
				}

				// 时区;
				String strTZ_TIME_ZONE = "";
				if (dataMap.containsKey("TZ_TIMEZONE")) {
					strTZ_TIME_ZONE = ((String) dataMap.get("TZ_TIMEZONE")).trim();
				}

				// 国籍;
				String strTZ_COUNTRY = "";
				if (dataMap.containsKey("TZ_COUNTRY")) {
					strTZ_COUNTRY = ((String) dataMap.get("TZ_COUNTRY")).trim();
				}

				// 常住省;
				String strTZ_LEN_PROID = "";
				if (dataMap.containsKey("TZ_LEN_PROID")) {
					strTZ_LEN_PROID = ((String) dataMap.get("TZ_LEN_PROID")).trim();
				}

				// 常驻城市;
				String strTZ_LEN_CITY = "";
				if (dataMap.containsKey("TZ_LEN_CITY")) {
					strTZ_LEN_CITY = ((String) dataMap.get("TZ_LEN_CITY")).trim();
				}

				// 证件类型;
				String strNATIONAL_ID_TYPE = "";
				if (dataMap.containsKey("NATIONAL_ID_TYPE")) {
					strNATIONAL_ID_TYPE = ((String) dataMap.get("NATIONAL_ID_TYPE")).trim();
				}

				// 证件编号;
				String strNATIONAL_ID = "";
				if (dataMap.containsKey("NATIONAL_ID")) {
					strNATIONAL_ID = ((String) dataMap.get("NATIONAL_ID")).trim();
				}

				// 毕业院校国家;
				String strTZ_SCH_CNAME_Contry = "";
				if (dataMap.containsKey("TZ_SCH_COUNTRY")) {
					strTZ_SCH_CNAME_Contry = ((String) dataMap.get("TZ_SCH_COUNTRY")).trim();
				}

				// 毕业院校;
				String strTZ_SCH_CNAME = "";
				if (dataMap.containsKey("TZ_SCH_CNAME")) {
					strTZ_SCH_CNAME = ((String) dataMap.get("TZ_SCH_CNAME")).trim();
				}

				// 专业;
				String strTZ_SPECIALTY = "";
				if (dataMap.containsKey("TZ_SPECIALTY")) {
					strTZ_SPECIALTY = ((String) dataMap.get("TZ_SPECIALTY")).trim();
				}

				// 最高学历;
				String strTZ_HIGHEST_EDU = "";
				if (dataMap.containsKey("TZ_HIGHEST_EDU")) {
					strTZ_HIGHEST_EDU = ((String) dataMap.get("TZ_HIGHEST_EDU")).trim();
				}

				// 项目;
				String strTZ_PRJ_ID = "";
				if (dataMap.containsKey("TZ_PROJECT")) {
					strTZ_PRJ_ID = ((String) dataMap.get("TZ_PROJECT")).trim();
				}

				String strTZ_MSSQH = "";
				if (dataMap.containsKey("TZ_MSSQH")) {
					strTZ_MSSQH = ((String) dataMap.get("TZ_MSSQH")).trim();
				}

				// 预留字段1;
				String strTZ_COMMENT1 = "";
				if (dataMap.containsKey("TZ_COMMENT1")) {
					strTZ_COMMENT1 = ((String) dataMap.get("TZ_COMMENT1")).trim();
				}

				// 预留字段2;
				String strTZ_COMMENT2 = "";
				if (dataMap.containsKey("TZ_COMMENT2")) {
					strTZ_COMMENT2 = ((String) dataMap.get("TZ_COMMENT2")).trim();
				}

				// 预留字段3;
				String strTZ_COMMENT3 = "";
				if (dataMap.containsKey("TZ_COMMENT3")) {
					strTZ_COMMENT3 = ((String) dataMap.get("TZ_COMMENT3")).trim();
				}

				// 预留字段4;
				String strTZ_COMMENT4 = "";
				if (dataMap.containsKey("TZ_COMMENT4")) {
					strTZ_COMMENT4 = ((String) dataMap.get("TZ_COMMENT4")).trim();
				}

				// 预留字段5;
				String strTZ_COMMENT5 = "";
				if (dataMap.containsKey("TZ_COMMENT5")) {
					strTZ_COMMENT5 = ((String) dataMap.get("TZ_COMMENT5")).trim();
				}

				// 预留字段6;
				String strTZ_COMMENT6 = "";
				if (dataMap.containsKey("TZ_COMMENT6")) {
					strTZ_COMMENT6 = ((String) dataMap.get("TZ_COMMENT6")).trim();
				}

				// 预留字段7;
				String strTZ_COMMENT7 = "";
				if (dataMap.containsKey("TZ_COMMENT7")) {
					strTZ_COMMENT7 = ((String) dataMap.get("TZ_COMMENT7")).trim();
				}

				// 预留字段8;
				String strTZ_COMMENT8 = "";
				if (dataMap.containsKey("TZ_COMMENT8")) {
					strTZ_COMMENT8 = ((String) dataMap.get("TZ_COMMENT8")).trim();
				}

				// 预留字段9;
				String strTZ_COMMENT9 = "";
				if (dataMap.containsKey("TZ_COMMENT9")) {
					strTZ_COMMENT9 = ((String) dataMap.get("TZ_COMMENT9")).trim();
				}

				// 预留字段10;
				String strTZ_COMMENT10 = "";
				if (dataMap.containsKey("TZ_COMMENT10")) {
					strTZ_COMMENT10 = ((String) dataMap.get("TZ_COMMENT10")).trim();
				}

				// 验证方式;
				String strActivateType = "";
				if (dataMap.containsKey("yzfs")) {
					strActivateType = ((String) dataMap.get("yzfs")).trim();
				}

				// 手机验证码;
				String strCheckCode = "";
				if (dataMap.containsKey("yzm")) {
					strCheckCode = ((String) dataMap.get("yzm")).trim();
				}

				// 邮箱验证码;
				String strCheckedEmail = "";
				if (dataMap.containsKey("yzmEmail")) {
					strCheckedEmail = ((String) dataMap.get("yzmEmail")).trim();
				}

				// 校验机构会员数据项--不能为空;
				String strTemV = "";
				// String sqlMemberDatas = "SELECT
				// TZ_REG_FIELD_ID,TZ_REG_FIELD_NAME,TZ_IS_REQUIRED,TZ_ENABLE
				// FROM PS_TZ_REG_FIELD_T WHERE TZ_ENABLE='Y' AND TZ_JG_ID=?
				// ORDER BY TZ_ORDER ASC";
				String sqlMemberDatas = "SELECT TZ_REG_FIELD_ID,TZ_REG_FIELD_NAME,TZ_IS_REQUIRED,TZ_ENABLE FROM PS_TZ_REG_FIELD_T WHERE TZ_ENABLE='Y' AND TZ_SITEI_ID=? ORDER BY TZ_ORDER ASC";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlMemberDatas, new Object[] { strSiteId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String regFieldId = (String) list.get(i).get("TZ_REG_FIELD_ID");
						String regFieldName = (String) list.get(i).get("TZ_REG_FIELD_NAME");
						String required = (String) list.get(i).get("TZ_IS_REQUIRED");
						String enable = (String) list.get(i).get("TZ_ENABLE");
						if (dataMap.containsKey(regFieldId)) {
							strTemV = (String) dataMap.get(regFieldId) == null ? ""
									: ((String) dataMap.get(regFieldId)).trim();
							if ("".equals(strTemV) && "Y".equals(required) && "Y".equals(enable)
									&& !"TZ_EMAIL".equals(regFieldId)) {
								errMsg[0] = "2";
								errMsg[1] = regFieldName + validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang,
										"TZ_SITE_MESSAGE", "26", "不能为空", "cannot be blank");
								return strResult;
							}
						}

						// 如果字段是手机,增加校验格式;如果手机为非必填，则不需要校验手机格式;
						if ("TZ_MOBILE".equals(regFieldId) && "Y".equals(required)) {
							String strPhoneParas = "{\"phone\":\"" + strTemV + "\",\"orgid\":\"" + strOrgId
									+ "\",\"lang\":\"" + strLang + "\",\"sen\":\"1\",\"siteId\":\"" + strSiteId + "\"}";
							String strPhoneResult = registeSmsServiceImpl.smsVerifyByActive(strPhoneParas, errMsg);
							if (!"0".equals(errMsg[0])) {
								strResult = strPhoneResult;
								return strResult;
							}
						}
					}
				}

				// 验证密码和确认密码是否一致;
				if (strTZ_PASSWORD == null || !strTZ_PASSWORD.equals(strTZ_REPASSWORD)) {
					errMsg[0] = "3";
					errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE", "55",
							"密码和确认密码不一致", "New Password and Confirm Password is not consistent");
					return strResult;
				}

				// 定义激活状态，默认不激活;
				String strActiveStatus = "N";
				if ("M".equals(strActivateType)) {
					// 手机

					String sjYzmSQL = "SELECT TZ_SJYZM,TZ_CNTLOG_ADDTIME FROM PS_TZ_SHJI_YZM_TBL WHERE TZ_EFF_FLAG='Y' AND TZ_JG_ID=? AND TZ_MOBILE_PHONE=? AND TZ_SITEI_ID=? ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
					try {
						Map<String, Object> MapGetYzmInfo = jdbcTemplate.queryForMap(sjYzmSQL,
								new Object[] { strOrgId, strTZ_MOBILE, strSiteId });

						if (MapGetYzmInfo == null) {
							errMsg[0] = "4";
							errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE",
									"55", "验证码不存在，请发送验证码", "The security code is incorrect");
							return strResult;
						} else {
							String tzSjYzm = MapGetYzmInfo.get("TZ_SJYZM") == null ? ""
									: String.valueOf(MapGetYzmInfo.get("TZ_SJYZM"));
							if (tzSjYzm != null && tzSjYzm.equals(strCheckCode)) {
								strActiveStatus = "Y";
							} else {
								errMsg[0] = "4";
								errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang,
										"TZ_SITE_MESSAGE", "50", "验证码不正确", "The security code is incorrect");
								return strResult;
							}
							// 需要修改 增加时间处理,验证码五分钟内有效
							Date dtYzmAddDate = (Date) (MapGetYzmInfo.get("TZ_CNTLOG_ADDTIME"));
							Calendar ca = Calendar.getInstance();
							ca.setTime(dtYzmAddDate);
							ca.add(Calendar.MINUTE, 5);
							Date check = ca.getTime();
							Date nowDate = new Date();
							if (check.before(nowDate)) {
								errMsg[0] = "4";
								errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang,
										"TZ_SITE_MESSAGE", "77", "验证码5分钟内有效，请重新发送验证码",
										"The security code is incorrect");
								return strResult;
							}
						}
					} catch (Exception e) {
						errMsg[0] = "4";
						errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE",
								"50", "验证码不正确", "The security code is incorrect");
						return strResult;
					}
				} else {
					// 邮箱
					if (strCheckedEmail != null && !"".equals(strCheckedEmail)) {
						// 校验验证码
						Patchca patchca = new Patchca();
						if (!patchca.verifyToken(request, strCheckedEmail)) {
							errMsg[0] = "5";
							errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE",
									"50", "验证码不正确", "The security code is incorrect");
							return strResult;
						}

					} else {
						errMsg[0] = "5";
						errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE",
								"50", "验证码不正确", "The security code is incorrect");
						return strResult;
					}
				}

				// TZ_REALNAME或（FIRST_NAME/LAST_NAME) 必填一项;
				if (strTZ_REALNAME == null || "".equals(strTZ_REALNAME)) {
					if (strTZ_FIRST_NAME != null && !"".equals(strTZ_FIRST_NAME) && strTZ_LAST_NAME != null
							&& !"".equals(strTZ_LAST_NAME)) {
						if ("ENG".equals(strLang)) {
							strTZ_REALNAME = strTZ_FIRST_NAME + " " + strTZ_LAST_NAME;
						} else {
							strTZ_REALNAME = strTZ_LAST_NAME + " " + strTZ_FIRST_NAME;
						}
					} else {
						errMsg[0] = "6";
						errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE",
								"65", "姓名或者FIRST_NAME、LAST_NAME 必填一项",
								"Name or LAST_NAME, FIRST_NAME at least one required");
						return strResult;
					}
				}

				// 通过所有校验，开始创建账号;
				String oprId = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
				// 生成登录账号; 登陆账号 和手机号码一直
				// String dlzh = this.tzGenerateAcount(strOrgId, oprId);
				String dlzh = strTZ_MOBILE;

				// 保存用户信息;
				Psoprdefn psoprdefn = new Psoprdefn();
				psoprdefn.setOprid(oprId);
				String password = DESUtil.encrypt(strTZ_REPASSWORD, "TZGD_Tranzvision");
				psoprdefn.setOperpswd(password);
				psoprdefn.setAcctlock((short) 0);
				psoprdefn.setLastupdoprid(oprId);
				psoprdefn.setLastupddttm(new Date());
				psoprdefnMapper.insert(psoprdefn);

				PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
				psTzAqYhxxTbl.setTzDlzhId(dlzh);
				psTzAqYhxxTbl.setTzJgId(strOrgId);
				psTzAqYhxxTbl.setOprid(oprId);
				psTzAqYhxxTbl.setTzRealname(strTZ_REALNAME);
				psTzAqYhxxTbl.setTzEmail(strTZ_EMAIL);
				psTzAqYhxxTbl.setTzMobile(strTZ_MOBILE);
				psTzAqYhxxTbl.setTzRylx("JGJS"); // 类型为 机构教师
				// 应光华要求，注册时候不管什么方式激活的，都可以通过手机或者邮箱都能登录系统-(2017-06-01，还原)
				if ("M".equals(strActivateType)) {
					psTzAqYhxxTbl.setTzSjbdBz("Y");
				}
				if ("E".equals(strActivateType)) {
					psTzAqYhxxTbl.setTzYxbdBz("Y");
				}

				psTzAqYhxxTbl.setTzJihuoZt(strActiveStatus);
				psTzAqYhxxTbl.setTzJihuoFs(strActivateType);
				psTzAqYhxxTbl.setTzZhceDt(new Date());
				psTzAqYhxxTbl.setTzBjsEml("N");
				psTzAqYhxxTbl.setTzBjsSms("N");
				psTzAqYhxxTbl.setRowAddedDttm(new Date());
				psTzAqYhxxTbl.setRowAddedOprid(oprId);
				psTzAqYhxxTbl.setRowLastmantDttm(new Date());
				psTzAqYhxxTbl.setRowLastmantOprid(oprId);
				psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);

				// 增加教师和学员的判断
				if (strSiteId.equals("46")) { // 教师门户

					// 增加教师信息表
					PxTeacher pxTeacher = new PxTeacher();
					pxTeacher.setOprid(oprId);
					pxTeacher.setName(strTZ_REALNAME);
					pxTeacher.setEmail(strTZ_EMAIL);
					pxTeacher.setContactorPhone(strTZ_MOBILE);
					pxTeacher.setStatu("B");
					pxTeacher.setLevel("A");
					// 注册初始积分
					String score = getHardCodePoint.getHardCodePointVal("TZ_TEA_REG");
					pxTeacher.setScore(new Integer(score));
					pxTeacherMapper.insertSelective(pxTeacher);

					// 增加教师积分变动表
					PkTeaIntegralChangeT pkTeaIntegralChangeT = new PkTeaIntegralChangeT();
					pkTeaIntegralChangeT.setOprid(oprId);
					pkTeaIntegralChangeT.setRowLastmantDttm(new Date());
					pkTeaIntegralChangeT.setRowLastmantOprid(oprId);
					pkTeaIntegralChangeT.setTzAfterChange(new Integer(score));
					pkTeaIntegralChangeT.setTzBeforeChange(new Integer(0));
					pkTeaIntegralChangeT.setTzChangeTime(new Date());
					pkTeaIntegralChangeT.setTzChange(new Integer(score));
					// 1：上课产生积分 2：提现消耗积分 3：管理员修改 4:注册产生积分
					pkTeaIntegralChangeT
							.setTzChangeId("" + getSeqNum.getSeqNum("PK_TES_INTEGRAL_CHANGE_T", "TZ_CHANGE_ID"));
					pkTeaIntegralChangeT.setTzChangeType("4");
					pkTeaIntegralChangeTMapper.insertSelective(pkTeaIntegralChangeT);

					// 添加角色; 写死获取教师角色
					Psroleuser psroleuser = new Psroleuser();
					psroleuser.setRoleuser(oprId);
					psroleuser.setRolename("PK_TEA");
					psroleuser.setDynamicSw("N");
					psroleuserMapper.insert(psroleuser);
				} else if (strSiteId.equals("45")) { // 学生门户
					PxStudentT pxStudentT = new PxStudentT();
					pxStudentT.setOprid(oprId);
					pxStudentT.setTzJgId(strOrgId);
					pxStudentT.setPhone(strTZ_MOBILE);
					pxStudentT.setStuStatus("A");

					pxStudentTMapper.insertSelective(pxStudentT);

					// 添加角色; 写死获取学员角色
					Psroleuser psroleuser = new Psroleuser();
					psroleuser.setRoleuser(oprId);
					psroleuser.setRolename("PK_STU");
					psroleuser.setDynamicSw("N");
					psroleuserMapper.insert(psroleuser);
				}

				// 通过所有校验，保存联系方式;
				PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
				psTzLxfsInfoTbl.setTzLxfsLy("JGJS");
				psTzLxfsInfoTbl.setTzLydxId(oprId);
				psTzLxfsInfoTbl.setTzZyEmail(strTZ_EMAIL);
				psTzLxfsInfoTbl.setTzZySj(strTZ_MOBILE);
				psTzLxfsInfoTbl.setTzSkype(strTZ_SKYPE);
				psTzLxfsInfoTblMapper.insert(psTzLxfsInfoTbl);

				// 通过所有校验，保存用户注册信息;
				PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
				psTzRegUserT.setOprid(oprId);
				psTzRegUserT.setTzSiteiId(strSiteId);
				psTzRegUserT.setTzRealname(strTZ_REALNAME);
				psTzRegUserT.setTzGender(strTZ_GENDER);
				psTzRegUserT.setTzSkype(strTZ_SKYPE);
				psTzRegUserT.setTzFirstName(strTZ_FIRST_NAME);
				psTzRegUserT.setTzLastName(strTZ_LAST_NAME);
				psTzRegUserT.setTzCompanyName(strTZ_COMPANY_NAME);
				psTzRegUserT.setTzCompIndustry(strTZ_COMP_INDUSTRY);
				psTzRegUserT.setTzDeptment(strTZ_DEPTMENT);
				psTzRegUserT.setTzZhiwu(strTZ_ZHIWU);
				psTzRegUserT.setTzTimezone(strTZ_TIME_ZONE);
				psTzRegUserT.setTzCountry(strTZ_COUNTRY);
				psTzRegUserT.setTzLenProid(strTZ_LEN_PROID);
				psTzRegUserT.setTzLenCity(strTZ_LEN_CITY);
				psTzRegUserT.setNationalIdType(strNATIONAL_ID_TYPE);
				psTzRegUserT.setNationalId(strNATIONAL_ID);

				SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
				if (strBIRTHDATE != null && !"".equals(strBIRTHDATE)) {
					if (strBIRTHDATE.indexOf("/") > 0) {
						strBIRTHDATE = strBIRTHDATE.replace("/", "-");
						strBIRTHDATE = strBIRTHDATE.replace("/", "-");
					}
					try {
						Date bthDate = dateFormate.parse(strBIRTHDATE);
						psTzRegUserT.setBirthdate(bthDate);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				psTzRegUserT.setTzSchCountry(strTZ_SCH_CNAME_Contry);
				psTzRegUserT.setTzSchCname(strTZ_SCH_CNAME);
				psTzRegUserT.setTzSpecialty(strTZ_SPECIALTY);
				psTzRegUserT.setTzHighestEdu(strTZ_HIGHEST_EDU);
				psTzRegUserT.setTzPrjId(strTZ_PRJ_ID);
				psTzRegUserT.setTzComment1(strTZ_COMMENT1);
				psTzRegUserT.setTzComment2(strTZ_COMMENT2);
				psTzRegUserT.setTzComment3(strTZ_COMMENT3);
				psTzRegUserT.setTzComment4(strTZ_COMMENT4);
				psTzRegUserT.setTzComment5(strTZ_COMMENT5);
				psTzRegUserT.setTzComment6(strTZ_COMMENT6);
				psTzRegUserT.setTzComment7(strTZ_COMMENT7);
				psTzRegUserT.setTzComment8(strTZ_COMMENT8);
				psTzRegUserT.setTzComment9(strTZ_COMMENT9);
				psTzRegUserT.setTzComment10(strTZ_COMMENT10);
				psTzRegUserT.setRowAddedDttm(new Date());
				psTzRegUserT.setRowAddedOprid(oprId);
				psTzRegUserT.setRowLastmantDttm(new Date());
				psTzRegUserT.setRowLastmantOprid(oprId);

				psTzRegUserTMapper.insert(psTzRegUserT);

				// 注册成功发送短信
				String getSmsSendTmpSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
				String strSmsSendTmp = "";
				if (strSiteId.equals("46")) {
					strSmsSendTmp = jdbcTemplate.queryForObject(getSmsSendTmpSql,
							new Object[] { "TZ_SMS_TEA_REGOK_TPL" }, "String");
				} else if (strSiteId.equals("45")) {
					strSmsSendTmp = jdbcTemplate.queryForObject(getSmsSendTmpSql,
							new Object[] { "TZ_SMS_STU_REGOK_TPL" }, "String");
				}

				if (strSmsSendTmp == null) {
					if (strSiteId.equals("46")) {
						strSmsSendTmp = "SMS_146809573";
					} else if (strSiteId.equals("45")) {
						strSmsSendTmp = "SMS_146809574";
					}
				}
				// 发送邮件

				String json = "";
				boolean rs = smsService.sendSmsAndLog(strTZ_MOBILE, strSmsSendTmp, json);
				// 失败后，重新发送一次
				if (!rs) {
					smsService.sendSmsAndLog(strTZ_MOBILE, strSmsSendTmp, json);
				}

				if ("M".equals(strActivateType)) {
					strJumUrl = request.getContextPath() + "/user/login/" + strOrgId.toLowerCase() + "/" + strSiteId;
				} else {
					String strEmailSendParas = "{\"email\":\"" + strTZ_EMAIL + "\",\"orgid\":\"" + strOrgId
							+ "\",\"lang\":\"" + strLang + "\",\"siteid\":\"" + strSiteId + "\",\"dlzhid\":\"" + oprId
							+ "\",\"sen\":\"2\"}";
					String strEmailSendResult = registeMalServiceImpl.tzQuery(strEmailSendParas, errMsg);
					if (!"0".equals(errMsg[0])) {
						strResult = strEmailSendResult;
						return strResult;
					}
					strJumUrl = request.getContextPath() + "/dispatcher";
					String tzParams = URLEncoder
							.encode("{\"ComID\":\"TZ_SITE_UTIL_COM\",\"PageID\":\"TZ_SITE_ENROLL_STD\",\"OperateType\":\"HTML\",\"comParams\": {\"email\":\""
									+ strTZ_EMAIL + "\",\"siteid\":\"" + strSiteId + "\",\"orgid\":\"" + strOrgId
									+ "\",\"sen\":\"1\"}}", "UTF-8");
					strJumUrl = strJumUrl + "?tzParams=" + tzParams;
				}

				Map<String, Object> returnMap = new HashMap<>();
				returnMap.put("result", "success");
				returnMap.put("jumpurl", strJumUrl);
				strResult = jacksonUtil.Map2json(returnMap);
				return strResult;
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgId, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}

		return strResult;
	}

	public String checkCodeVerifyByPass(String strParams, String[] errMsg) {
		String strCheckCode = "";
		String strLang = "";
		String jgid = "";
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strCheckCode = jacksonUtil.getString("checkCode");
			strLang = jacksonUtil.getString("lang");
			jgid = jacksonUtil.getString("jgid");
			// 校验验证码
			Patchca patchca = new Patchca();
			if (!patchca.verifyToken(request, strCheckCode)) {
				errMsg[0] = "2";
				errMsg[1] = validateUtil.getMessageTextWithLanguageCd(jgid, strLang, "TZ_SITE_MESSAGE", "50", "验证码不正确",
						"The security code is incorrect");
				return strResult;
			}
			strResult = "\"success\"";
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = validateUtil.getMessageTextWithLanguageCd(jgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			return strResult;
		}
	}

	public String modifyPasswordByPass(String strParams, String[] errMsg) {

		String strPwd = "";
		String strRePwd = "";
		String strCheckCode = "";
		String strTokenSign = "";
		String strOprid = "";
		String strJgId = "";
		String strLang = "";
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strPwd = jacksonUtil.getString("pwd");
			strRePwd = jacksonUtil.getString("repwd");
			strCheckCode = jacksonUtil.getString("checkCode");
			strLang = jacksonUtil.getString("lang");
			strTokenSign = jacksonUtil.getString("tokensign");

			String sql = "SELECT TZ_DLZH_ID,TZ_JG_ID FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_TOKEN_CODE=? AND TZ_TOKEN_TYPE = 'EDIT' AND TZ_EFF_FLAG = 'Y' ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
			Map<String, Object> map = null;
			try {
				map = jdbcTemplate.queryForMap(sql, new Object[] { strTokenSign });
			} catch (Exception e) {
				map = null;
			}

			if (map != null) {
				strOprid = (String) map.get("TZ_DLZH_ID");
				strJgId = (String) map.get("TZ_JG_ID");

				if (strCheckCode != null && !"".equals(strCheckCode)) {
					String strCheckeCodeParas = "{\"checkCode\":\"" + strCheckCode + "\",\"lang\":\"" + strLang
							+ "\",\"jgid\":\"" + strJgId + "\",\"sen\":\"3\"}";
					String strCheckeCodeResult = this.tzQuery(strCheckeCodeParas, errMsg);
					if (!"0".equals(errMsg[0])) {
						strResult = strCheckeCodeResult;
						return strResult;
					}
				} else {
					errMsg[0] = "5";
					errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strJgId, strLang, "TZ_SITE_MESSAGE", "50",
							"验证码不正确", "The security code is incorrect");
					return strResult;
				}

				if (strPwd == null || "".equals(strPwd.trim())) {
					errMsg[0] = "1";
					errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strJgId, strLang, "TZ_SITE_MESSAGE", "60",
							"新密码不能为空", "The new password can not be empty");
					return strResult;
				}

				if (strRePwd == null || "".equals(strRePwd.trim())) {
					errMsg[0] = "2";
					errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strJgId, strLang, "TZ_SITE_MESSAGE", "61",
							"确认密码不能为空", "The confirm password can not be empty");
					return strResult;
				}
				strPwd = strPwd.trim();
				strRePwd = strRePwd.trim();
				if (!strPwd.equals(strRePwd)) {
					errMsg[0] = "3";
					errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strJgId, strLang, "TZ_SITE_MESSAGE", "28",
							"新密码和确认密码不一致", "New Password and Confirm Password is not consistent");
					return strResult;
				}

				// 修改用户密码
				String password = DESUtil.encrypt(strPwd, "TZGD_Tranzvision");
				Psoprdefn psoprdefn = new Psoprdefn();
				psoprdefn.setOprid(strOprid);
				psoprdefn.setOperpswd(password);
				psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
				// 把验证码失效;
				String yzmSQL = "UPDATE PS_TZ_DZYX_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_TOKEN_CODE=? AND TZ_TOKEN_TYPE = 'EDIT'";
				jdbcTemplate.update(yzmSQL, new Object[] { strTokenSign });

				Map<String, Object> returnMap = new HashMap<>();
				returnMap.put("result", "success");
				// 修改登录链接
				// String siteIdSQL = "SELECT TZ_SITEI_ID FROM
				// PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? limit 0,1";
				// String strSiteId = jdbcTemplate.queryForObject(siteIdSQL, new
				// Object[] { strJgId }, "String");
				String siteIdSQL = "select TZ_SITEI_ID from PS_TZ_REG_USER_T WHERE OPRID=?";
				String strSiteId = jdbcTemplate.queryForObject(siteIdSQL, new Object[] { strOprid }, "String");
				String strJumUrl = request.getContextPath() + "/user/login/" + strJgId.toLowerCase() + "/" + strSiteId;
				returnMap.put("jumpurl", strJumUrl);
				strResult = jacksonUtil.Map2json(returnMap);
				return strResult;
			} else {
				errMsg[0] = "5";
				errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strJgId, strLang, "TZ_SITE_MESSAGE", "62",
						"修改密码失败", "Failed to modify password");
				return strResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = validateUtil.getMessageTextWithLanguageCd(strJgId, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			return strResult;
		}
	}

	public String tzGenerateAcount(String jgId, String oprId) {
		String accountId = oprId;
		// 查看账号是否已经被占用;
		boolean bl = true;
		while (bl) {
			String sql = "select COUNT(1) from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID = ? AND TZ_JG_ID = ?";
			int count = jdbcTemplate.queryForObject(sql, new Object[] { accountId, jgId }, "Integer");
			if (count == 0 && accountId != null && !"".equals(accountId)) {
				bl = false;
			} else {
				accountId = "TZ_" + getSeqNum.getSeqNum("PSOPRDEFN", "OPRID");
			}
		}

		return accountId;
	}

	@Override
	public String tzGetHtmlContent(String strParams) {
		String strSiteid = request.getParameter("siteid");
		String strOrgid = request.getParameter("orgid");
		String strEmail = request.getParameter("email");
		String strTokenSign = request.getParameter("tokensign");
		String strLang = request.getParameter("lang");
		String strSen = request.getParameter("sen");
		String classid = request.getParameter("classid");

		String strResponse = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			/* 对lang参数做限制，只能是ZHS或ENG，不单从URL中获取，卢艳添加，2017-10-9 */
			if (strLang != null && !"".equals(strLang)) {
				if ("ZHS".equals(strLang.toUpperCase()) || "ENG".equals(strLang.toUpperCase())) {
					strLang = strLang.toUpperCase();
				} else {
					strLang = "ZHS";
				}
			}

			if (classid != null && !"".equals(classid)) {
				strParams = "{\"siteid\":\"" + strSiteid + "\",\"orgid\":\"" + strOrgid + "\",\"lang\":\"" + strLang
						+ "\",\"tokensign\":\"" + strTokenSign + "\",\"email\":\"" + strEmail + "\",\"sen\":\"" + strSen
						+ "\"}";
			} else {
				jacksonUtil.json2Map(strParams);
				strOrgid = jacksonUtil.getString("orgid");
				strLang = jacksonUtil.getString("lang");
				strSen = jacksonUtil.getString("sen");
				strSiteid = jacksonUtil.getString("siteid");
			}

			// 注册提交后的激活邮箱页面
			if ("1".equals(strSen)) {
				return this.createPageForEmlLogin(strParams);
			}

			// 发送邮箱中的激活账号
			if ("2".equals(strSen)) {
				return this.activeEmailSuccess(strParams);
			}
			// 激活提示信息;
			if ("3".equals(strSen)) {
				return this.createPageForEmlAct(strParams);
			}

			if ("4".equals(strSen)) {
				return this.createPageForPass(strParams);
			}

			if ("5".equals(strSen)) {
				return this.createPageForFixPass(strParams);
			}

			if ("6".equals(strSen)) {
				return this.createPageForChangeEmail(strParams);
			}

			if ("7".equals(strSen)) {
				return this.createPageForEmailChangeDone(strParams);
			}

			if ("8".contentEquals(strSen)) {
				return this.getEnrollUrl(strParams);
			}
			// 手机版忘记密码页面
			if ("9".equals(strSen)) {
				return this.createMPageForPass(strParams);
			}

			String strMessage = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "119",
					"链接错误，请确认您输入的URL地址无误！", "Url is invalid, please makesure the url is valid!");
			return strMessage;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResponse;
	}

	public String createPageForEmlLogin(String strParams) {
		String strEmail = "";
		String strOrgid = "";
		String strSiteId = "";
		String strLang = "";
		String strResult = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strEmail = jacksonUtil.getString("email");
			strOrgid = jacksonUtil.getString("orgid");
			strSiteId = jacksonUtil.getString("siteid");
			String sql = "SELECT TZ_SITEI_ID ,TZ_SITE_LANG,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strSiteId });
			// String sql = "SELECT TZ_SITEI_ID ,TZ_SITE_LANG,TZ_SKIN_ID FROM
			// PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y' limit
			// 0,1";
			// Map<String, Object> map = jdbcTemplate.queryForMap(sql, new
			// Object[] { strOrgid });
			if (map != null) {
				strSiteId = (String) map.get("TZ_SITEI_ID");
				strLang = (String) map.get("TZ_SITE_LANG");
				String skinId = (String) map.get("TZ_SKIN_ID");
				strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
						"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
				String[] splistEmail = strEmail.split("@");
				String strContent = "";
				if ("ENG".equals(strLang)) {
					strContent = "An email has been sent to the address you provided.please<a style='text-decoration:underline;color:blue' href='http://mail."
							+ splistEmail[1] + "/'> log in </a> to verify your email address.";
				} else {
					strContent = "邮箱激活邮件已发送到您的注册邮箱，请<a href='http://mail." + splistEmail[1] + "/'>登录邮箱</a>激活。";
				}

				String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
				imgPath = request.getContextPath() + imgPath + "/" + skinId;

				String strFloder = "TZWebSiteRegisteBundle";
				boolean isMobile = CommonUtils.isMobile(request);
				if (isMobile) {
					strResult = tzGdObject.getHTMLText("HTML.TZMobileSitePageBundle.TZ_GD_SUCCESSE_HTML", strContent,
							imgPath, request.getContextPath());
				} else {
					strResult = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_SUCCESSE_HTML", strContent,
							imgPath);
				}
				strResult = objRep.repTitle(strResult, strSiteId);
				if (isMobile) {
					strResult = objRep.repPhoneCss(strResult, strSiteId);
				} else {
					strResult = objRep.repCss(strResult, strSiteId);
				}

				return strResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
		return strResult;
	}

	public String activeEmailSuccess(String strParams) {
		String strTokenSign = "";
		String strOprid = "";
		String strOrgid = "";
		String strSiteId = "";
		String strLang = "";
		String siteid = "";
		Date dtYxq = null;
		String strYxzt = "";
		String strResult = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strTokenSign = jacksonUtil.getString("tokensign");
			strOrgid = jacksonUtil.getString("orgid");
			strLang = jacksonUtil.getString("lang");
			siteid = jacksonUtil.getString("siteid");

			// String sql = "SELECT TZ_SITEI_ID ,TZ_SKIN_ID FROM
			// PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y' limit
			// 0,1";
			// Map<String, Object> map = jdbcTemplate.queryForMap(sql, new
			// Object[] { strOrgid });
			String sql = "SELECT TZ_SITEI_ID ,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { siteid });
			if (map != null) {
				strSiteId = (String) map.get("TZ_SITEI_ID");
				String skinId = (String) map.get("TZ_SKIN_ID");
				strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
						"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
				if (strOrgid != null && !"".equals(strOrgid) && strTokenSign != null && !"".equals(strTokenSign)) {
					// 判断验证码是否已经过期；
					String yzmSQL = "SELECT TZ_DLZH_ID,TZ_YZM_YXQ,TZ_EFF_FLAG FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_TOKEN_TYPE='REG' AND TZ_TOKEN_CODE=? AND TZ_JG_ID=? ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
					Map<String, Object> yzmMap = jdbcTemplate.queryForMap(yzmSQL,
							new Object[] { strTokenSign, strOrgid });
					if (yzmMap != null) {
						strOprid = (String) yzmMap.get("TZ_DLZH_ID");
						dtYxq = (Date) yzmMap.get("TZ_YZM_YXQ");
						strYxzt = (String) yzmMap.get("TZ_EFF_FLAG");
						if (strOprid != null && !"".equals(strOprid)) {
							// 是否已经激活,如果已经激活，跳转到登录页面;
							String strJihuo = "";
							String jihuoSQL = "SELECT TZ_JIHUO_ZT FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=? AND TZ_RYLX='ZCYH'";
							try {
								strJihuo = jdbcTemplate.queryForObject(jihuoSQL, new Object[] { strOprid, strOrgid },
										"String");
							} catch (Exception e) {
								strJihuo = "";
							}
							// 申请人登录页面链接;
							String contextPath = request.getContextPath();
							String loginUrl = contextPath + "/user/login/" + strOrgid.toLowerCase() + "/" + strSiteId;
							String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
							imgPath = contextPath + imgPath + "/" + skinId;
							// 注册页面链接
							String dirsql = "SELECT TZ_ENROLL_DIR FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID=? AND TZ_JG_ID=?";
							String dir = jdbcTemplate.queryForObject(dirsql, new Object[] { siteid, strOrgid },
									"String");

							String registerUrl = contextPath + dir + "/enroll.html";
							String strFloder = "TZWebSiteRegisteBundle";
							boolean isMobile = CommonUtils.isMobile(request);
							if (isMobile) {
								registerUrl = contextPath + dir + "/menroll.html";
								strFloder = "TZMobileSitePageBundle";
							}

							// 已经激活
							if ("Y".equals(strJihuo)) {
								if ("ENG".equals(strLang)) {
									strResult = tzGdObject.getHTMLText(
											"HTML." + strFloder + ".TZ_GD_REGSUCCESS_ENG_HTML", loginUrl, contextPath,
											imgPath);
								} else {
									strResult = tzGdObject.getHTMLText("HTML." + strFloder + ".TZ_GD_REGSUCCESS_HTML",
											loginUrl, contextPath, imgPath);
								}

								strResult = objRep.repTitle(strResult, strSiteId);
								if (isMobile) {
									strResult = objRep.repPhoneCss(strResult, strSiteId);
								} else {
									strResult = objRep.repCss(strResult, strSiteId);
								}
								return strResult;
							} else {

								// 激活码的状态是否有效
								if ("Y".equals(strYxzt)) {
									// 判断时间是否过期;
									Date curDate = new Date();
									if (curDate.before(dtYxq)) {
										// 判断身份证号是否重复
										String comExistSql = "SELECT NATIONAL_ID FROM PS_TZ_REG_USER_T  WHERE OPRID=? ";
										String nationId = "";
										nationId = jdbcTemplate.queryForObject(comExistSql, new Object[] { strOprid },
												"String");
										// 判断身份证号是否存在
										int count = 0;
										if (nationId != null && !"".equals(nationId)) {
											String countSql = "SELECT count(a.NATIONAL_ID) FROM PS_TZ_REG_USER_T a,PS_TZ_AQ_YHXX_TBL b WHERE a.NATIONAL_ID = ? AND a.OPRID=b.TZ_DLZH_ID AND b.TZ_JIHUO_ZT='Y' AND a.TZ_SITEI_ID=? ";
											count = jdbcTemplate.queryForObject(countSql,
													new Object[] { nationId, siteid }, "int");
										}
										if (count > 0) {
											if ("ENG".equals(strLang)) {
												strResult = tzGdObject.getHTMLText(
														"HTML.TZWebSiteRegisteBundle.TZ_GD_REGFAILED_ENG_HTML",
														registerUrl, contextPath, imgPath);
											} else {
												strResult = tzGdObject.getHTMLText(
														"HTML.TZWebSiteRegisteBundle.TZ_GD_REGFAILED_HTML", registerUrl,
														contextPath, imgPath);
											}
											strResult = objRep.repTitle(strResult, strSiteId);
											strResult = objRep.repCss(strResult, strSiteId);
											return strResult;
										} else {
											PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
											psTzAqYhxxTbl.setTzDlzhId(strOprid);
											psTzAqYhxxTbl.setTzJgId(strOrgid);
											psTzAqYhxxTbl.setTzJihuoZt("Y");
											psTzAqYhxxTbl.setRowLastmantDttm(new Date());
											psTzAqYhxxTblMapper.updateByPrimaryKeySelective(psTzAqYhxxTbl);

											PsTzDzyxYzmTbl psTzDzyxYzmTbl = new PsTzDzyxYzmTbl();
											psTzDzyxYzmTbl.setTzTokenCode(strTokenSign);
											psTzDzyxYzmTbl.setTzEffFlag("N");
											psTzDzyxYzmTblMapper.updateByPrimaryKeySelective(psTzDzyxYzmTbl);

											if ("ENG".equals(strLang)) {
												strResult = tzGdObject.getHTMLText(
														"HTML." + strFloder + ".TZ_GD_REGSUCCESS_ENG_HTML", loginUrl,
														request.getContextPath(), imgPath);
											} else {
												strResult = tzGdObject.getHTMLText(
														"HTML." + strFloder + ".TZ_GD_REGSUCCESS_HTML", loginUrl,
														request.getContextPath(), imgPath);
											}

											strResult = objRep.repTitle(strResult, strSiteId);
											if (isMobile) {
												strResult = objRep.repPhoneCss(strResult, strSiteId);
											} else {
												strResult = objRep.repCss(strResult, strSiteId);
											}
											return strResult;
										}
									} else {
										String message = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,
												"TZ_SITE_MESSAGE", "64", "激活账号链接已失效，请重新发送激活账号邮件！",
												"Activate the account link is invalid, please re send the activation account mail!");
										String strTipHtml4 = tzGdObject.getHTMLText(
												"HTML.TZWebSiteRegisteBundle.TZ_TIMEOUT_TIP2_HMTL", message);
										return this.createPageForEmlActByParameter(strSiteId, strLang, strOrgid,
												strTipHtml4);
									}
								} else {
									// 激活码的状态无效；
									String message = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,
											"TZ_SITE_MESSAGE", "124", "激活账号链接已失效，请重新发送激活账号邮件！",
											"Activate the account link is invalid, please re send the activation account mail!");
									String strTipHtml2 = tzGdObject
											.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_TIMEOUT_TIP2_HMTL", message);
									return this.createPageForEmlActByParameter(strSiteId, strLang, strOrgid,
											strTipHtml2);
								}
							}

						}

					}
				}
			}
			String message = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "64",
					"激活账号链接已失效，请重新发送激活账号邮件！",
					"Activate the account link is invalid, please re send the activation account mail!");
			String strTipHtml3 = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_TIMEOUT_TIP2_HMTL", message);

			return this.createPageForEmlActByParameter(strSiteId, strLang, strOrgid, strTipHtml3);
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
	}

	public String createPageForEmlActByParameter(String strSiteId, String strLang, String strOrgid, String strTip) {
		String strResult = "获取数据失败，请联系管理员";
		try {
			strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");

			String contextPath = request.getContextPath();
			String strBeginUrl = contextPath + "/dispatcher";
			String sql = "SELECT TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
			String skinId = jdbcTemplate.queryForObject(sql, new Object[] { strSiteId }, "String");
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			String str_content = "";
			if (strOrgid == null) {
				strOrgid = "";
			}
			String strFloder = "TZWebSiteRegisteBundle";
			boolean isMobile = CommonUtils.isMobile(request);
			if (isMobile) {
				strFloder = "TZMobileSitePageBundle";
			}

			if ("ENG".equals(strLang)) {
				str_content = tzGdObject.getHTMLText("HTML." + strFloder + ".TZ_GD_JHYX_EP_ENG_HTML", strBeginUrl,
						strOrgid, strTip, strLang, contextPath, imgPath, strOrgid.toLowerCase() + "/" + strSiteId,
						strSiteId);
			} else {
				str_content = tzGdObject.getHTMLText("HTML." + strFloder + ".TZ_GD_JHYX_EP_HTML", strBeginUrl, strOrgid,
						strTip, strLang, contextPath, imgPath, strOrgid.toLowerCase() + "/" + strSiteId, strSiteId);
			}
			str_content = objRep.repTitle(str_content, strSiteId);
			if (isMobile) {
				str_content = objRep.repPhoneCss(str_content, strSiteId);
			} else {
				str_content = objRep.repCss(str_content, strSiteId);
			}
			return str_content;
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
	}

	public String createPageForPass(String strParams) {
		String strOrgid = "";
		String strSiteId = "";
		String strLang = "";
		String strResult = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strOrgid = jacksonUtil.getString("orgid");
			strSiteId = jacksonUtil.getString("siteid");
			strLang = jacksonUtil.getString("lang");

			strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");

			String contextPath = request.getContextPath();
			String strBeginUrl = contextPath + "/dispatcher";

			String loginUrl = "";
			if (strSiteId.equals("46")) {
				loginUrl = contextPath + "/user/login/teaLogin";
			} else if (strSiteId.equals("45")) {
				loginUrl = contextPath + "/user/login/stuLogin";
			}

			String sql = "SELECT TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
			String skinId = jdbcTemplate.queryForObject(sql, new Object[] { strSiteId }, "String");
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			// 激活方式;
			String strTabType = "";
			// String activeteTypqSQL = "SELECT TZ_ACTIVATE_TYPE FROM
			// PS_TZ_USERREG_MB_T WHERE TZ_JG_ID=?";
			String activeteTypqSQL = "SELECT TZ_ACTIVATE_TYPE FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID=?";
			try {
				strTabType = jdbcTemplate.queryForObject(activeteTypqSQL, new Object[] { strSiteId }, "String");
			} catch (Exception e) {

			}

			String str_content = "";
			if (strTabType.contains("MOBILE") && strTabType.contains("EMAIL")) {
				if ("ENG".equals(strLang)) {
					str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WJMM_EP_ENG_HTML",
							strBeginUrl, strOrgid, strLang, contextPath, imgPath, loginUrl, strSiteId);
				} else {
					str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WJMM_EP_HTML", strBeginUrl,
							strOrgid, strLang, contextPath, imgPath, loginUrl, strSiteId);
				}
			} else {

				if (strTabType.contains("EMAIL")) {
					if ("ENG".equals(strLang)) {
						str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WJMM_EP1_ENG_HTML",
								strBeginUrl, strOrgid, strLang, contextPath, imgPath, loginUrl, strSiteId);
					} else {
						str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WJMM_EP1_HTML",
								strBeginUrl, strOrgid, strLang, contextPath, imgPath, loginUrl, strSiteId);
					}
				} else {
					str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WJMM_EP2_HTML", strBeginUrl,
							strOrgid, strLang, contextPath, imgPath, loginUrl, strSiteId);
				}
			}
			str_content = objRep.repTitle(str_content, strSiteId);
			str_content = objRep.repCss(str_content, strSiteId);
			return str_content;
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
	}

	public String createMPageForPass(String strParams) {
		String strOrgid = "";
		String strSiteId = "";
		String strLang = "";
		String strResult = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strOrgid = jacksonUtil.getString("orgid");
			strSiteId = jacksonUtil.getString("siteid");
			strLang = jacksonUtil.getString("lang");

			strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");

			String contextPath = request.getContextPath();
			String strBeginUrl = contextPath + "/dispatcher";
			String loginUrl = contextPath + "/user/login/" + strOrgid.toLowerCase() + "/" + strSiteId;
			String sql = "SELECT TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
			String skinId = jdbcTemplate.queryForObject(sql, new Object[] { strSiteId }, "String");
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			// 激活方式;
			String strTabType = "";
			// String activeteTypqSQL = "SELECT TZ_ACTIVATE_TYPE FROM
			// PS_TZ_USERREG_MB_T WHERE TZ_JG_ID=?";
			String activeteTypqSQL = "SELECT TZ_ACTIVATE_TYPE FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID=?";
			try {
				strTabType = jdbcTemplate.queryForObject(activeteTypqSQL, new Object[] { strSiteId }, "String");
			} catch (Exception e) {

			}

			String str_content = "";
			if (strTabType.contains("MOBILE") && strTabType.contains("EMAIL")) {
				if ("ENG".equals(strLang)) {
					str_content = tzGdObject.getHTMLText("HTML.TZMobileSitePageBundle.TZ_GD_MWJMM_EP_ENG_HTML",
							contextPath, strOrgid, strLang, strBeginUrl, strSiteId);
				} else {
					str_content = tzGdObject.getHTMLText("HTML.TZMobileSitePageBundle.TZ_GD_MWJMM_EP_HTML", contextPath,
							strOrgid, strLang, strBeginUrl, strSiteId);
				}
			} else {

				if (strTabType.contains("EMAIL")) {
					if ("ENG".equals(strLang)) {
						str_content = tzGdObject.getHTMLText("HTML.TZMobileSitePageBundle.TZ_GD_MWJMM_EP1_ENG_HTML",
								contextPath, strOrgid, strLang, strBeginUrl, strSiteId);
					} else {
						str_content = tzGdObject.getHTMLText("HTML.TZMobileSitePageBundle.TZ_GD_MWJMM_EP1_HTML",
								contextPath, strOrgid, strLang, strBeginUrl, strSiteId);
					}
				} else {
					str_content = tzGdObject.getHTMLText("HTML.TZMobileSitePageBundle.TZ_GD_MWJMM_EP2_HTML",
							contextPath, strOrgid, strLang, strBeginUrl, strSiteId);
				}
			}
			str_content = objRep.repTitle(str_content, strSiteId);
			str_content = objRep.repPhoneCss(str_content, strSiteId);
			return str_content;
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
	}

	public String createPageForFixPass(String strParams) {
		String strOrgid = "";
		String strSiteId = "";
		String strTokenSign = "";
		String strTokenSign2 = "";
		String strLang = "";
		Date dtYxq;
		String strResult = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strOrgid = jacksonUtil.getString("orgid");
			strLang = jacksonUtil.getString("lang");
			strSiteId = jacksonUtil.getString("siteid");
			strTokenSign = jacksonUtil.getString("tokensign");
			strTokenSign2 = strTokenSign;
			strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			String strStrongMsg = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "122",
					"密码强度不够", "Stronger password needed.");
			String strNotice = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "123",
					"请重置新密码", "Please Enter New Password.");

			// String siteidSQL = "SELECT TZ_SITEI_ID,TZ_SKIN_ID FROM
			// PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y'";
			// Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteidSQL,
			// new Object[] { strOrgid });
			// strSiteId = (String) siteMap.get("TZ_SITEI_ID");
			String siteidSQL = "SELECT TZ_SITEI_ID,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteidSQL, new Object[] { strSiteId });
			String skinId = (String) siteMap.get("TZ_SKIN_ID");
			String contextPath = request.getContextPath();
			String strBeginUrl = contextPath + "/dispatcher";
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			String loginUrl = contextPath + "/user/login/" + strOrgid.toLowerCase() + "/" + strSiteId;

			String strSenPara = "";
			String strHtmlFloder = "";
			boolean isMobile = CommonUtils.isMobile(request);
			if (isMobile) {
				strSenPara = "9";
				strHtmlFloder = "TZMobileSitePageBundle";
			} else {
				strSenPara = "4";
				strHtmlFloder = "TZWebSiteRegisteBundle";
			}
			String str_content = "";
			String yzmSQL = "SELECT TZ_DLZH_ID,TZ_YZM_YXQ FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_TOKEN_CODE=? AND TZ_JG_ID=? AND TZ_TOKEN_TYPE = 'EDIT' AND TZ_EFF_FLAG = 'Y' ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
			Map<String, Object> yzmMap = jdbcTemplate.queryForMap(yzmSQL, new Object[] { strTokenSign, strOrgid });
			if (yzmMap != null) {
				dtYxq = (Date) yzmMap.get("TZ_YZM_YXQ");
				Date curDate = new Date();
				if (curDate.before(dtYxq)) {
					// 有效；
					if ("ENG".equals(strLang)) {
						str_content = tzGdObject.getHTMLText("HTML." + strHtmlFloder + ".TZ_GD_UPDATE_PWD_ENG_HTML",
								strBeginUrl, strTokenSign2, strLang, loginUrl, strStrongMsg, strNotice, contextPath,
								imgPath);
					} else {
						str_content = tzGdObject.getHTMLText("HTML." + strHtmlFloder + ".TZ_GD_UPDATE_PWD_HTML",
								strBeginUrl, strTokenSign2, strLang, loginUrl, strStrongMsg, strNotice, contextPath,
								imgPath);
					}

					str_content = objRep.repTitle(str_content, strSiteId);
					if (isMobile) {
						str_content = objRep.repPhoneCss(str_content, strSiteId);
					} else {
						str_content = objRep.repCss(str_content, strSiteId);
					}

					return str_content;
				} else {
					// 无效；
					strBeginUrl = strBeginUrl + "?classid=enrollCls&siteid=" + strSiteId + "&orgid=" + strOrgid
							+ "&lang=" + strLang + "&sen=" + strSenPara;
					String message = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE",
							"58", "重置密码时间为30分钟，已超时，请重新发送忘记密码邮件！",
							"Reset password time for 30 minutes, has timed out, please re send forget password message!");
					str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_TIMEOUT_TIP_HMTL", message,
							strBeginUrl);
					return str_content;
				}
			} else {
				strBeginUrl = strBeginUrl + "?classid=enrollCls&siteid=" + strSiteId + "&orgid=" + strOrgid + "&lang="
						+ strLang + "&sen=" + strSenPara;
				String message = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "59",
						"重置密码链接已失效，请重新发送忘记密码邮件！",
						"Reset password link has failed, please re send forget password mail!");
				str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_TIMEOUT_TIP_HMTL", message,
						strBeginUrl);
				return str_content;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}

	}

	public String createPageForChangeEmail(String strParams) {
		String strOrgid = "";
		String strLang = "";
		String strSiteId = "";
		String strResult = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strOrgid = jacksonUtil.getString("orgid");
			strLang = jacksonUtil.getString("lang");
			strSiteId = jacksonUtil.getString("siteid");
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");

			String contextPath = request.getContextPath();
			String strBeginUrl = contextPath + "/dispatcher";
			String sql = "SELECT TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
			String skinId = jdbcTemplate.queryForObject(sql, new Object[] { strSiteId }, "String");
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			// 获取当前用户联系方式中的邮箱
			sql = "select TZ_ZY_EMAIL from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
			String lxfsEmail = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "String");
			if (lxfsEmail == null) {
				lxfsEmail = "";
			}

			String str_content = "";
			if ("ENG".equals(strLang)) {
				str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_EN_EMAIL", strBeginUrl,
						strOrgid, contextPath, imgPath, lxfsEmail, strSiteId);
			} else {
				str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_EMAIL", strBeginUrl,
						strOrgid, contextPath, imgPath, lxfsEmail, strSiteId);
			}

			str_content = objRep.repTitle(str_content, strSiteId);
			str_content = objRep.repCss(str_content, strSiteId);
			return str_content;
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
	}

	public String createPageForEmailChangeDone(String strParams) {
		String strTokenSign = "";
		String strOprid = "";
		String strOrgid = "";
		String strSiteId = "";
		String strLang = "";
		Date dtYxq;
		String strEmail = "";
		String strYxzt = "";
		String strResult = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strTokenSign = jacksonUtil.getString("tokensign");
			strOrgid = jacksonUtil.getString("orgid");
			strLang = jacksonUtil.getString("lang");
			strSiteId = jacksonUtil.getString("siteid");

			strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");

			if (strTokenSign != null && !"".equals(strTokenSign.trim()) && strOrgid != null && !"".equals(strOrgid)) {
				strTokenSign = strTokenSign.trim();
				/*
				 * String sql =
				 * "SELECT TZ_SITEI_ID,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1"
				 * ; Map<String, Object> siteMap = jdbcTemplate.queryForMap(sql,
				 * new Object[] { strOrgid });
				 */
				String sql = "SELECT TZ_SITEI_ID,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
				Map<String, Object> siteMap = jdbcTemplate.queryForMap(sql, new Object[] { strSiteId });
				strSiteId = (String) siteMap.get("TZ_SITEI_ID");
				String skinId = (String) siteMap.get("TZ_SKIN_ID");
				String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
				imgPath = request.getContextPath() + imgPath + "/" + skinId;
				String wrongPic = "/error_001.jpg";

				String yzmSQL = "SELECT TZ_DLZH_ID,TZ_YZM_YXQ,TZ_EFF_FLAG,TZ_EMAIL FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_TOKEN_TYPE='EDIT' AND TZ_TOKEN_CODE=? AND TZ_JG_ID=? ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
				Map<String, Object> yzmMap = jdbcTemplate.queryForMap(yzmSQL, new Object[] { strTokenSign, strOrgid });
				if (yzmMap != null) {
					strOprid = (String) yzmMap.get("TZ_DLZH_ID");
					dtYxq = (Date) yzmMap.get("TZ_YZM_YXQ");
					strYxzt = (String) yzmMap.get("TZ_EFF_FLAG");
					strEmail = (String) yzmMap.get("TZ_EMAIL");
					if ("Y".equals(strYxzt) && strEmail != null && !"".equals(strEmail)) {
						Date curDate = new Date();
						// 有效;
						if (curDate.before(dtYxq)) {
							PsTzAqYhxxTbl psTzAqYhxxTbl = new PsTzAqYhxxTbl();
							psTzAqYhxxTbl.setTzDlzhId(strOprid);
							psTzAqYhxxTbl.setTzJgId(strOrgid);
							psTzAqYhxxTbl.setTzEmail(strEmail);
							psTzAqYhxxTbl.setTzYxbdBz("Y");
							psTzAqYhxxTbl.setRowLastmantDttm(curDate);
							psTzAqYhxxTblMapper.updateByPrimaryKeySelective(psTzAqYhxxTbl);

							PsTzDzyxYzmTbl psTzDzyxYzmTbl = new PsTzDzyxYzmTbl();
							psTzDzyxYzmTbl.setTzTokenCode(strTokenSign);
							psTzDzyxYzmTbl.setTzEffFlag("N");
							psTzDzyxYzmTblMapper.updateByPrimaryKeySelective(psTzDzyxYzmTbl);

							String message = strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,
									"TZ_SITE_MESSAGE", "29", "修改成功", "The modification is successful.");
							strResult = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_DONE_SUCCESS_ENG_HTML",
									message, imgPath + "/right_green.png");
							strResult = objRep.repTitle(strResult, strSiteId);
							strResult = objRep.repCss(strResult, strSiteId);
							return strResult;
						} else {
							String message = strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,
									"TZ_SITE_MESSAGE", "68", "链接已超时", "Link has timed out.");
							strResult = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_DONE_SUCCESS_ENG_HTML",
									message, imgPath + wrongPic);
							strResult = objRep.repTitle(strResult, strSiteId);
							strResult = objRep.repCss(strResult, strSiteId);
							return strResult;
						}
					} else {
						String message = strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,
								"TZ_SITE_MESSAGE", "69", "链接已失效", "Link has failed.");
						strResult = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_DONE_SUCCESS_ENG_HTML",
								message, imgPath + wrongPic);
						strResult = objRep.repTitle(strResult, strSiteId);
						strResult = objRep.repCss(strResult, strSiteId);
						return strResult;
					}
				} else {
					String message = strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,
							"TZ_SITE_MESSAGE", "69", "链接已失效", "Link has failed.");
					strResult = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_DONE_SUCCESS_ENG_HTML", message,
							imgPath + wrongPic);
					strResult = objRep.repTitle(strResult, strSiteId);
					strResult = objRep.repCss(strResult, strSiteId);
					return strResult;
				}

			}
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
	}

	public String getEnrollUrl(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		String siteid = "";
		String url = "";
		try {
			jacksonUtil.json2Map(strParams);
			siteid = jacksonUtil.getString("siteid");
			url = jdbcTemplate.queryForObject("SELECT TZ_ENROLL_DIR FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID=?",
					new Object[] { siteid }, "String");
			url = url.replaceAll("\\\\", "/");
			Boolean isMobile = CommonUtils.isMobile(request);
			String strEnrollName = "";
			
			if (siteid.equals("46")){
				strEnrollName="enrollTea.html";
			} else if (siteid.equals("45")){
				strEnrollName="enrollStu.html";
			}
			if (isMobile) {
				strEnrollName = "menroll.html";
			}
			if (!"".equals(url)) {
				if ((url.lastIndexOf("/") + 1) == url.length()) {
					url = request.getContextPath() + url + strEnrollName;
				} else {
					url = request.getContextPath() + url + "/" + strEnrollName;
				}
			} else {
				url = request.getContextPath() + "/" + strEnrollName;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("url", url);
		return jacksonUtil.Map2json(map);
	}

	public String createPageForEmlAct(String strParams) {
		String strOrgid = "";
		String strLang = "";
		String strSiteId = "";
		String strResult = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strOrgid = jacksonUtil.getString("orgid");
			strLang = jacksonUtil.getString("lang");
			strSiteId = jacksonUtil.getString("siteid");

			strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			String strTip = "";
			if (jacksonUtil.containsKey("tip")) {
				strTip = jacksonUtil.getString("tip");
			}

			String contextPath = request.getContextPath();
			String strBeginUrl = contextPath + "/dispatcher";
			String sql = "SELECT TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
			String skinId = jdbcTemplate.queryForObject(sql, new Object[] { strSiteId }, "String");
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			String str_content = "";
			if (strTip == null) {
				strTip = "";
			}
			String strHtmlFloder = "";

			boolean isMobile = CommonUtils.isMobile(request);
			if (isMobile) {
				strHtmlFloder = "TZMobileSitePageBundle";
			} else {
				strHtmlFloder = "TZWebSiteRegisteBundle";
			}
			if ("ENG".equals(strLang)) {
				str_content = tzGdObject.getHTMLText("HTML." + strHtmlFloder + ".TZ_GD_JHYX_EP_ENG_HTML", strBeginUrl,
						strOrgid, strTip, strLang, contextPath, imgPath, strOrgid.toLowerCase() + "/" + strSiteId,
						strSiteId);
			} else {
				str_content = tzGdObject.getHTMLText("HTML." + strHtmlFloder + ".TZ_GD_JHYX_EP_HTML", strBeginUrl,
						strOrgid, strTip, strLang, contextPath, imgPath, strOrgid.toLowerCase() + "/" + strSiteId,
						strSiteId);
			}
			str_content = objRep.repTitle(str_content, strSiteId);
			if (isMobile) {
				str_content = objRep.repPhoneCss(str_content, strSiteId);
			} else {
				str_content = objRep.repCss(str_content, strSiteId);
			}

			return str_content;
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
	}

}
