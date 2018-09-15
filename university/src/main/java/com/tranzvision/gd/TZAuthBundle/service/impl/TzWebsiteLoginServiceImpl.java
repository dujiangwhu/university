/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartJSSDKSign;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 机构网站用户登录业务
 * 
 * @author SHIHUA
 * @since 2015-12-21
 */
@Service("com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl")
public class TzWebsiteLoginServiceImpl implements TzWebsiteLoginService {

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;

	@Autowired
	private GetCookieSessionProps getCookieSessionProps;

	@Autowired
	private TzCookie tzCookie;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private TzWeChartJSSDKSign tzWeChartJSSDKSign;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	/**
	 * Session存储的用户信息变量名称
	 */
	// public final String userSessionName = "loginUser";
	public final String userSessionName = "loginManager";

	/**
	 * Session存储的用户当前登录语言
	 */
	// public final String sysWebLanguage = "sysWebLanguage";
	public final String sysWebLanguage = "sysLanguage";

	/**
	 * Cookie存储的系统语言信息
	 */
	// public final String cookieWebLang = "tzweblang";
	public final String cookieWebLang = "tzlang";

	/**
	 * Cookie存储的当前访问站点的机构id
	 */
	// public final String cookieWebOrgId = "tzwo";
	public final String cookieWebOrgId = "tzmo";

	/**
	 * Cookie存储的当前访问站点的站点id
	 */
	public final String cookieWebSiteId = "tzws";

	/**
	 * Cookie存储的当前登录网站的用户登录账号
	 */
	// public final String cookieWebLoginedUserName = "tzwu";
	public final String cookieWebLoginedUserName = "tzmu";

	/**
	 * 记录登录类型，后台 - GLY；前台 - SQR；
	 */
	public final String cookieContextLoginType = "TZGD_CONTEXT_LOGIN_TYPE";

	/**
	 * Cookie存储当前登录网站的链接地址
	 */
	public final String cookieWebLoginUrl = "TZGD_LOGIN_URL";

	/**
	 * 记录免密自动登录密钥的cookie名称变量，李刚添加，2017.04.20
	 */
	public final String cookieWebLoginName = "TZGD_AUTO_LOGIN";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#doLogin(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.util.ArrayList)
	 */
	@Override
	public boolean doLogin(HttpServletRequest request, HttpServletResponse response, String orgid, String siteid,
			String userName, String userPwd, String code, String language, String LoginType,
			ArrayList<String> errorMsg) {
		// 20170222，yuds，手机版暂不进行验证码校验
		boolean isMobile = CommonUtils.isMobile(request);
		if (!isMobile) {
			// 校验验证码
			Patchca patchca = new Patchca();
			if (!patchca.verifyToken(request, code)) {
				errorMsg.add("3");
				errorMsg.add(gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
						"TZGD_FWINIT_00040", language, "输入的验证码不正确。", "Security code is incorrect."));
				return false;
			}
		}

		try {

			// 校验机构和站点的有效性
			String sql = "select TZ_JG_EFF_STA from PS_TZ_JG_BASE_T where TZ_JG_ID=?";
			String tzJgEffStu = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");
			if (!"Y".equals(tzJgEffStu)) {
				errorMsg.add("2");
				errorMsg.add("登录失败，无效的机构。");
				return false;
			}

			Map<String, Object> dataMap;
			// 校验用户名
			Object[] args = null;
			if (LoginType.equals("STU")) {
				args = new Object[] { userName, orgid, "PXXY" };
			} else if (LoginType.equals("TEA")) {
				args = new Object[] { userName, orgid, "JGJS" };
			}

			dataMap = sqlQuery.queryForMap(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzGetWebsiteUserOpridByID"), args);

			if (null == dataMap) {
				errorMsg.add("2");
				errorMsg.add(gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
						"TZGD_FWINIT_00103", language, "登录失败，请确认用户名是否存在。",
						"Login failed, whether the username exists."));
				return false;
			}

			String strJhzt = dataMap.get("TZ_JIHUO_ZT") == null ? "" : String.valueOf(dataMap.get("TZ_JIHUO_ZT"));
			if (!"Y".equals(strJhzt)) {
				errorMsg.add("1");
				errorMsg.add(gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
						"TZGD_FWINIT_00050", language, "此帐号暂未激活，请激活后重试。", "The account is not activated yet."));
				return false;
			}

			// 校验用户名、密码
			args = new Object[] { dataMap.get("OPRID"), DESUtil.encrypt(userPwd, "TZGD_Tranzvision") };
			String strFlag = sqlQuery.queryForObject(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzCheckManagerPwd"), args,
					"String");

			if (!"Y".equals(strFlag)) {
				errorMsg.add("2");
				errorMsg.add(gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
						"TZGD_FWINIT_00049", language, "登录失败，请确认用户名和密码是否正确。",
						"Email address or password is incorrect !"));
				return false;
			}

			// 读取用户信息
			PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
			psTzAqYhxxTblKey.setTzDlzhId(dataMap.get("TZ_DLZH_ID").toString());
			psTzAqYhxxTblKey.setTzJgId(dataMap.get("TZ_JG_ID").toString());
			PsTzAqYhxxTbl loginUser = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);

			// 设置Session
			TzSession tzSession = new TzSession(request);
			tzSession.addSession(userSessionName, loginUser);

			// 设置语言环境
			this.switchSysLanguage(request, response, language);

			// 设置cookie参数
			tzCookie.addCookie(response, cookieWebOrgId, psTzAqYhxxTblKey.getTzJgId(), 24 * 3600);
			tzCookie.addCookie(response, cookieWebSiteId, siteid, 24 * 3600);
			tzCookie.addCookie(response, cookieWebLoginedUserName, psTzAqYhxxTblKey.getTzDlzhId());
			tzCookie.addCookie(response, cookieContextLoginType, "SQR");

			// 将当前登录地址也写入cookie中
			String ctxPath = request.getContextPath();
			String strLoginUrl = "";
			if (ctxPath != null && !"".equals(ctxPath)) {
				if (LoginType.equals("STU")) {
					strLoginUrl = ctxPath + "/user/login/stuLogin";
				} else {
					strLoginUrl = ctxPath + "/user/login/teaLogin";
				}

			} else {
				if (LoginType.equals("STU")) {
					strLoginUrl = "/user/login/stuLogin";
				} else {
					strLoginUrl = "/user/login/teaLogin";
				}
			}
			tzCookie.addCookie(response, cookieWebLoginUrl, strLoginUrl);

			// 生产自动免密登录cookie
			String tmpPwdKey = "TZGD_@_!_*_20170420_Tranzvision";
			String tmpWebLoginCookieValue = "";
			String myUUID = "" + UUID.randomUUID() + "-" + UUID.randomUUID();
			String[] myRandomKeys = myUUID.split("-");
			String[] myRandomArray = new String[] { myRandomKeys[0], "A===" + psTzAqYhxxTblKey.getTzJgId(),
					myRandomKeys[1], "B===" + siteid, myRandomKeys[2], "C===" + psTzAqYhxxTblKey.getTzDlzhId(),
					myRandomKeys[3], myRandomKeys[4] };
			// 将数据顺序打乱
			Random tmpRand = new Random();
			String tmpKeyValue = "";
			for (int i = 0; i < 30; i++) {
				int myRandomSeed = tmpRand.nextInt(8);
				tmpKeyValue = myRandomArray[0];
				myRandomArray[0] = myRandomArray[myRandomSeed];
				myRandomArray[myRandomSeed] = tmpKeyValue;
			}
			// 使用“|”拼接数据字符串
			for (int i = 0; i < myRandomArray.length; i++) {
				if (i == 0) {
					tmpWebLoginCookieValue = myRandomArray[i];
				} else {
					tmpWebLoginCookieValue += "|" + myRandomArray[i];
				}
			}
			tmpWebLoginCookieValue = DESUtil.encrypt(tmpWebLoginCookieValue, tmpPwdKey);
			tzCookie.addCookie(response, cookieWebLoginName, tmpWebLoginCookieValue);

			errorMsg.add("0");
			errorMsg.add("");
			return true;

		} catch (TzSystemException tze) {
			System.out.println("写入cookie出现TzSystemException异常，信息为：" + tze.toString());
			tze.printStackTrace();
		} catch (Exception e) {
			System.out.println("写入cookie出现Exception异常，信息为：" + e.toString());
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 根据cookie自动登录，登录失败在根据OPENID登录
	 * 
	 * @param request
	 * @param response
	 */
	public void autoLoginByCookie(HttpServletRequest request, HttpServletResponse response) {
		// 组件配置的类引用ID;
		String tmpClassId = request.getParameter("classid");

		// 用于记录判断当前功能页是否手机版首页结果的变量
		Boolean mbaIndexM = false;
		String tmpSQLText = "";

		// 会话是否有效
		Boolean bool = gdObjectServiceImpl.isSessionValid(request);
		Boolean isAnonymous = false;
		if (bool) {
			String tmpCurrentLoginUser = gdObjectServiceImpl.getOPRID(request);
			if ("TZ_GUEST".equals(tmpCurrentLoginUser) == true) {
				isAnonymous = true;
			}
		}

		// 用于控制是否将用户重定向到登录页的变量
		Boolean logoutFlag = false;

		// 获取用于免登录的站点编号、组织机构编号和用户账号
		String tmpSiteId = "";
		String tmpOrgId = "";
		String tmpUserDlzh = "";

		String tmpPwdKey = "TZGD_@_!_*_20170420_Tranzvision";
		String tmpAutoLoginCookieValue = tzCookie.getStringCookieVal(request, "TZGD_AUTO_LOGIN");

		if (bool == false || isAnonymous == true) {
			tmpAutoLoginCookieValue = tmpAutoLoginCookieValue == null ? ""
					: DESUtil.decrypt(tmpAutoLoginCookieValue, tmpPwdKey);
			if (tmpAutoLoginCookieValue != null && "".equals(tmpAutoLoginCookieValue) == false) {
				String[] tmpLoginKeys = tmpAutoLoginCookieValue.split("\\|");
				for (int i = 0; i < tmpLoginKeys.length; i++) {
					if (tmpLoginKeys[i].startsWith("A===") == true) {
						tmpOrgId = tmpLoginKeys[i].substring(4);
					} else if (tmpLoginKeys[i].startsWith("B===") == true) {
						tmpSiteId = tmpLoginKeys[i].substring(4);
					} else if (tmpLoginKeys[i].startsWith("C===") == true) {
						tmpUserDlzh = tmpLoginKeys[i].substring(4);
					}
				}
			}
			String tmpURLSiteId = request.getParameter("siteId");
			if (tmpSiteId == null || "".equals(tmpSiteId) == true) {
				tmpSiteId = tmpURLSiteId;
			}

			// 判断是否在前端网站首页
			tmpSQLText = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			String mobileUrlParams = sqlQuery.queryForObject(tmpSQLText, new Object[] { "TZ_MBA_BKXT_MURL_PARAMS" },
					"String");
			if (mobileUrlParams != null && !"".equals(mobileUrlParams)) {
				String classIdParam = "", siteIdParam = "", orgIdParam = "";

				String[] params = mobileUrlParams.split("&");
				for (int i = 0; i < params.length; i++) {
					String[] paramsTmp = params[i].split("=");
					if ("classid".equals(paramsTmp[0])) {
						classIdParam = paramsTmp[1];
					}
					if ("siteId".equals(paramsTmp[0])) {
						siteIdParam = paramsTmp[1];
					}
					if ("orgId".equals(paramsTmp[0])) {
						orgIdParam = paramsTmp[1];
					}
				}

				if (classIdParam != null && !"".equals(classIdParam) && siteIdParam != null
						&& !"".equals(siteIdParam)) {
					if (classIdParam.equals(tmpClassId)
							&& (siteIdParam.equals(tmpSiteId) || siteIdParam.equals(tmpURLSiteId))) {
						mbaIndexM = true;
					}
				}
			}
		}

		// 如果当前功能页面为手机版首页且为匿名用户访问，则需要将用户重定向到登录页
		if (mbaIndexM == true && isAnonymous == true) {
			logoutFlag = true;
		}

		// 判断是否需要根据免密cookie记录的信息进行自动登录
		if (logoutFlag == false) {
			// 是否具备用于免登录使用的站点编号、机构编号和用户登录账号
			if (tmpSiteId != null && !"".equals(tmpSiteId)) {
				if (tmpOrgId != null && !"".equals(tmpOrgId)) {
					if (!"".equals(tmpUserDlzh) && tmpUserDlzh != null) {
						ArrayList<String> aryErrorMsg = new ArrayList<String>();

						tmpSQLText = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=(SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?)";
						String passwordJm = sqlQuery.queryForObject(tmpSQLText, new Object[] { tmpUserDlzh }, "String");
						String password = passwordJm == null ? "" : DESUtil.decrypt(passwordJm, "TZGD_Tranzvision");

						Patchca patchca = new Patchca();
						String tmpTokenValue = patchca.getToken(request);
						TzSession tmpSession = new TzSession(request);
						if (tmpTokenValue == null) {
							tmpTokenValue = "AbCd";

							tmpSession.addSession(patchca.getTokenName(), tmpTokenValue);
						}

						String LoginType = tmpSession.getSession("LoginType") == null ? ""
								: tmpSession.getSession("LoginType").toString();

						boolean boolResult = doLogin(request, response, tmpOrgId, tmpSiteId, tmpUserDlzh, password,
								tmpTokenValue, "ZHS", LoginType, aryErrorMsg);

						if (boolResult == false) {
							if (mbaIndexM) {
								logoutFlag = true;
							}
						}
					} else {
						if (mbaIndexM) {
							logoutFlag = true;
						}
					}
				} else {
					if (mbaIndexM) {
						logoutFlag = true;
					}
				}
			}
		}

		// 如果满足将用户重定向到登录的条件，则将用户重定向到登录页
		if (logoutFlag == true) {
			this.autoLoginByOpenId(request, response);
		}
	}

	/**
	 * 根据openid自动登录
	 * 
	 * @param request
	 * @param response
	 */
	public void autoLoginByOpenId(HttpServletRequest request, HttpServletResponse response) {
		// System.out.println("---enter login by openid---");
		String tmpOpenID = "";
		// 会话是否有效
		Boolean bool = gdObjectServiceImpl.isSessionValid(request);
		Boolean isAnonymous = false;
		if (bool) {
			String tmpCurrentLoginUser = gdObjectServiceImpl.getOPRID(request);
			if ("TZ_GUEST".equals(tmpCurrentLoginUser) == true) {
				isAnonymous = true;
			}
		}
		// 用于控制是否将用户重定向到登录页的变量
		Boolean logoutFlag = true;

		// 会话失效或匿名登录,根据openid查询绑定用户
		if (bool == false || isAnonymous == true) {
			// 微信浏览器下访问根据openid自动登录
			Boolean isWeChart = CommonUtils.isWeChartBrowser(request);
			if (isWeChart) {
				String tmpOpenIDKey = "TZGD_@_!_*_20170420_Tranzvision";
				tmpOpenID = tzCookie.getStringCookieVal(request, "TZGD_WECHART_OPENID");
				tmpOpenID = tmpOpenID == null ? "" : DESUtil.decrypt(tmpOpenID, tmpOpenIDKey);

				String classId = request.getParameter("classid");
				String siteId = request.getParameter("siteId");
				String orgId = "";
				if (!"".equals(siteId) && siteId != null) {
					String siteSQL = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
					orgId = sqlQuery.queryForObject(siteSQL, new Object[] { siteId }, "String");
				}

				if ((tmpOpenID == null || "".equals(tmpOpenID)) && "mIndex".equals(classId) && siteId != null
						&& !"".equals(siteId)) {
					// cookie中不存在openid，重新获取openid
					String appid = "";// 微信公众号appid
					String secret = "";
					try {
						appid = getHardCodePoint.getHardCodePointVal("TZ_WX_APPID");
						secret = getHardCodePoint.getHardCodePointVal("TZ_WX_APPSECRET");
					} catch (Exception e) {
						e.printStackTrace();
					}

					String code = request.getParameter("code");
					if (code == null || "".equals(code)) {
						String url = request.getRequestURL().toString();
						String queryString = request.getQueryString();

						if (queryString != null) {
							url += "?" + queryString;
						}

						if (!"".equals(appid)) {
							try {
								url = URLEncoder.encode(url, "utf-8");
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							String ctxPath = request.getContextPath();
							url = ctxPath + "/weChart/getCode?appid=" + appid + "&redirect_uri=" + url;
							try {
								response.sendRedirect(url);
								return;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						// 通过code获取openid
						tmpOpenID = tzWeChartJSSDKSign.getOauthAccessOpenId(appid, secret, code);

						String cookieOpenID = DESUtil.encrypt(tmpOpenID, tmpOpenIDKey);
						tzCookie.addCookie(response, "TZGD_WECHART_OPENID", cookieOpenID);
					}
				}

				// System.out.println("OPENID====== "+tmpOpenID);

				if (!"".equals(siteId) && !"".equals(orgId) && !"".equals(tmpOpenID)) {
					String tmpUserDlzh = "";
					String sql = "select TZ_DLZH_ID from PS_TZ_OPENID_TBL where OPENID=? and TZ_JG_ID=? and TZ_SITEI_ID=? and TZ_DEL_FLG='N'";
					tmpUserDlzh = sqlQuery.queryForObject(sql, new Object[] { tmpOpenID, orgId, siteId }, "String");

					if (tmpUserDlzh != null && !"".equals(tmpUserDlzh)) {
						ArrayList<String> aryErrorMsg = new ArrayList<String>();

						sql = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=(SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? and TZ_JG_ID=?)";
						String passwordJm = sqlQuery.queryForObject(sql, new Object[] { tmpUserDlzh, orgId }, "String");
						String password = passwordJm == null ? "" : DESUtil.decrypt(passwordJm, "TZGD_Tranzvision");

						Patchca patchca = new Patchca();
						String tmpTokenValue = patchca.getToken(request);
						if (tmpTokenValue == null) {
							tmpTokenValue = "AbCd";
						}
						TzSession tmpSession = new TzSession(request);
						String LoginType = tmpSession.getSession("LoginType") == null ? ""
								: tmpSession.getSession("LoginType").toString();

						boolean boolResult = doLogin(request, response, orgId, siteId, tmpUserDlzh, password,
								tmpTokenValue, "ZHS", LoginType, aryErrorMsg);
						if (boolResult) {
							logoutFlag = false;
						}

					}
				}
			}
		} else {
			logoutFlag = false;
		}

		// 如果自动登录成功，则将用户重定向到首页
		if (logoutFlag == true) {
			// rootPath;
			String ctxPath = request.getContextPath();

			// 得到登录地址;
			String loginOutUrl = tzCookie.getStringCookieVal(request, "TZGD_LOGIN_URL");

			if (loginOutUrl == null || "".equals(loginOutUrl)) {
				String tmpSQLText = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				String loginUrl = sqlQuery.queryForObject(tmpSQLText, new Object[] { "TZ_MBA_BKXT_MURL_LOGIN" },
						"String");
				loginOutUrl = ctxPath + loginUrl;
			}

			if (!"".equals(tmpOpenID)) {
				if (loginOutUrl.indexOf("?") > -1) {
					loginOutUrl = loginOutUrl + "&OPENID=" + tmpOpenID;
				} else {
					loginOutUrl = loginOutUrl + "?OPENID=" + tmpOpenID;
				}
			}
			// System.out.println("---to login--->"+loginOutUrl);
			try {
				response.sendRedirect(loginOutUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzLoginService#switchSysLanguage(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void switchSysLanguage(HttpServletRequest request, HttpServletResponse response, String lanaguageCd) {

		if (null == lanaguageCd || "".equals(lanaguageCd)) {

			// 从cookie中获取用户上次使用的系统语言
			lanaguageCd = tzCookie.getStringCookieVal(request, cookieWebLang);
			if (null == lanaguageCd || "".equals(lanaguageCd)) {
				// 若cookie中没有或无效，则从系统记录表中获取用户上次登录的系统语言
				lanaguageCd = gdObjectServiceImpl.getUserGxhLanguage(request, response);
			}

		}

		// 校验语言的有效性
		if (!gdObjectServiceImpl.isLanguageCdValid(lanaguageCd)) {
			lanaguageCd = "";
		}

		// 若没有有效的语言，则取默认语言
		if ("".equals(lanaguageCd)) {
			lanaguageCd = getSysHardCodeVal.getSysDefaultLanguage();
		}

		// 设置session变量
		TzSession tzSession = new TzSession(request);
		tzSession.addSession(sysWebLanguage, lanaguageCd);

		// 设置cookie变量
		int cookieMaxAge = 3600 * 24 * 30; // cookie期限是30天
		tzCookie.addCookie(response, cookieWebLang, lanaguageCd, cookieMaxAge);

		// 更新系统记录
		gdObjectServiceImpl.setUserGXHSXValue(request, response, "LANGUAGECD", lanaguageCd);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzLoginService#doLogout(javax.
	 * servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doLogout(HttpServletRequest request, HttpServletResponse response) {
		// 销毁session，登出
		TzSession tzSession = new TzSession(request);
		tzSession.invalidate(request, response);

		// 删除用户登录账号，张浪添加，2017-7-19
		tzCookie.removeCookie(response, cookieWebLoginName);
		// 删除用户登录密码，张浪添加，2017-7-19

		// 删除用户切换班级的cookie，曹阳添加，2017-7-19
		tzCookie.removeCookie(response, "cookieClassID");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * updateLoginedUserInfo(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean updateLoginedUserInfo(HttpServletRequest request, HttpServletResponse response) {

		boolean boolRet = false;

		String strDlzhId = this.getLoginedUserDlzhid(request);
		String strOrgId = this.getLoginedUserOrgid(request);

		// 读取用户信息
		PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
		psTzAqYhxxTblKey.setTzDlzhId(strDlzhId);
		psTzAqYhxxTblKey.setTzJgId(strOrgId);
		PsTzAqYhxxTbl loginUser = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);

		if (null != loginUser) {
			// 设置Session
			TzSession tzSession = new TzSession(request);
			tzSession.removeSession(userSessionName);
			tzSession.addSession(userSessionName, loginUser);
			boolRet = true;
		}

		return boolRet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * getLoginedUserInfo(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public PsTzAqYhxxTbl getLoginedUserInfo(HttpServletRequest request) {
		// 从Session中获取登录用户信息
		TzSession tzSession = new TzSession(request);
		Object obj = tzSession.getSession(userSessionName);
		PsTzAqYhxxTbl psTzAqYhxxTbl;
		if (null == obj) {
			boolean debugging = getCookieSessionProps.getDebug();
			if (debugging) {
				psTzAqYhxxTbl = new PsTzAqYhxxTbl();
				psTzAqYhxxTbl.setTzRealname("管理员");
			} else {
				psTzAqYhxxTbl = null;
			}
		} else {
			psTzAqYhxxTbl = (PsTzAqYhxxTbl) obj;
		}
		return psTzAqYhxxTbl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * getLoginedUserOprid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedUserOprid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回oprid
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(userSessionName);
		if (null != psTzAqYhxxTbl) {
			return psTzAqYhxxTbl.getOprid();
		} else {
			if (getCookieSessionProps == null) {
				getCookieSessionProps = new GetCookieSessionProps();
			}
			boolean debugging = getCookieSessionProps.getDebug();
			String strRtn = "";
			if (debugging) {
				strRtn = "TZ_7";
			}
			return strRtn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * getLoginedUserDlzhid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedUserDlzhid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回orgid
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(userSessionName);
		if (null != psTzAqYhxxTbl) {
			return psTzAqYhxxTbl.getTzDlzhId();
		} else {
			if (getCookieSessionProps == null) {
				getCookieSessionProps = new GetCookieSessionProps();
			}
			boolean debugging = getCookieSessionProps.getDebug();
			String strRtn = "";
			if (debugging) {
				strRtn = "Admin";
			}
			return strRtn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * getLoginedUserOrgid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedUserOrgid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回orgid
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(userSessionName);
		if (null != psTzAqYhxxTbl) {
			return psTzAqYhxxTbl.getTzJgId();
		} else {
			if (getCookieSessionProps == null) {
				getCookieSessionProps = new GetCookieSessionProps();
			}
			boolean debugging = getCookieSessionProps.getDebug();
			String strRtn = "";
			if (debugging) {
				strRtn = "ADMIN";
			}
			return strRtn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzLoginService#getSysLanaguageCD(
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getSysLanaguageCD(HttpServletRequest request) {
		// 从Session中获取当前用户正在使用的系统语言
		TzSession tzSession = new TzSession(request);
		Object lang = tzSession.getSession(sysWebLanguage);
		if (null != lang) {
			return String.valueOf(lang);
		} else {
			if (getCookieSessionProps == null) {
				getCookieSessionProps = new GetCookieSessionProps();
			}
			boolean debugging = getCookieSessionProps.getDebug();
			String strRtn = "";
			if (debugging) {
				strRtn = getSysHardCodeVal.getSysDefaultLanguage();
			}
			return strRtn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * checkUserLogin(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean checkUserLogin(HttpServletRequest request, HttpServletResponse response) {
		// 判断Session中是否存在登录用户信息
		TzSession tzSession = new TzSession(request);
		return tzSession.checkSession(userSessionName);
	}

}
