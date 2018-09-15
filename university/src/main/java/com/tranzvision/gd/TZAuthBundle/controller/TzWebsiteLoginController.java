/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;
import com.tranzvision.gd.TZWeChatBundle.dao.PsTzOpenidTblMapper;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzOpenidTbl;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzOpenidTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;

/**
 * 机构网站登录前端控制器
 * 
 * @author SHIHUA
 * @since 2015-12-17
 */
@Controller
@RequestMapping(value = { "/user/login" })
public class TzWebsiteLoginController {

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzCookie tzCookie;

	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	/**
	 * 学生登录页面，登录页面并没有机构ID和站点ID，站点ID是配置在Hcode里面的，机构ID写死，应为站点的机构ID 是一样的
	 * 
	 * @param request
	 * @param response
	 * @param orgid
	 * @return
	 */
	@RequestMapping(value = { "/stuLogin" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String stuLoginWebsite(HttpServletRequest request, HttpServletResponse response) {

		String strRet = "";
		String siteid = getHardCodePoint.getHardCodePointVal("TZ_STU_MH");
		String orgid = getHardCodePoint.getHardCodePointVal("TZ_SITE_JGID");
		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			if (null != orgid && !"".equals(orgid) && null != siteid && !"".equals(siteid)) {

				String loginHtml = "";
				Boolean isMobile = CommonUtils.isMobile(request);
				if (isMobile) {
					String openid = request.getParameter("OPENID");
					if (openid == null) {
						openid = "";
					}
					loginHtml = tzWebsiteServiceImpl.getMLoginPublishCode(request, orgid, siteid, openid);
					strRet = loginHtml;
				} else {
					loginHtml = tzWebsiteServiceImpl.getLoginPublishCode(request, orgid, siteid);
					strRet = loginHtml;
				}
				strRet = loginHtml;
				strRet = strRet.replace("aabbccddeaccf", orgid);

			} else {

				strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "访问站点异常，请检查您访问的地址是否正确。",
						"Can not visit the site.Please check the url.");

			}
		} catch (Exception e) {
			e.printStackTrace();
			strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "访问站点异常，请检查您访问的地址是否正确。",
					"Can not visit the site.Please check the url.");
		}

		return strRet;

	}

	/**
	 * 教师登录页面 登录页面并没有机构ID和站点ID，站点ID是配置在Hcode里面的，机构ID写死，应为站点的机构ID 是一样的
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/teaLogin" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String teaLoginWebsite(HttpServletRequest request, HttpServletResponse response) {

		String strRet = "";

		try {
			String siteid = getHardCodePoint.getHardCodePointVal("TZ_TEA_MH");
			String orgid = getHardCodePoint.getHardCodePointVal("TZ_SITE_JGID");

			if (null != orgid && !"".equals(orgid) && null != siteid && !"".equals(siteid)) {

				String loginHtml = "";
				Boolean isMobile = CommonUtils.isMobile(request);
				if (isMobile) {
					String openid = request.getParameter("OPENID");
					if (openid == null) {
						openid = "";
					}
					loginHtml = tzWebsiteServiceImpl.getMLoginPublishCode(request, orgid, siteid, openid);
					strRet = loginHtml;
				} else {
					loginHtml = tzWebsiteServiceImpl.getLoginPublishCode(request, orgid, siteid);
					strRet = loginHtml;
				}
				strRet = loginHtml;
				strRet = strRet.replace("aabbccddeaccf", orgid);

			} else {

				strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "访问站点异常，请检查您访问的地址是否正确。",
						"Can not visit the site.Please check the url.");

			}
		} catch (Exception e) {
			e.printStackTrace();
			strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "访问站点异常，请检查您访问的地址是否正确。",
					"Can not visit the site.Please check the url.");
		}

		return strRet;

	}

	/**
	 * 教师和学生登录处理
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dologin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(request.getParameter("tzParams"));

		Map<String, Object> mapData = jacksonUtil.getMap("comParams");
		String LoginType = mapData.get("LoginType") == null ? "" : String.valueOf(mapData.get("LoginType"));

		System.out.println("LoginType：" + LoginType);

		String strUserName = mapData.get("userName") == null ? "" : String.valueOf(mapData.get("userName")).trim();
		String strPassWord = mapData.get("passWord") == null ? "" : String.valueOf(mapData.get("passWord"));
		String strYzmCode = mapData.get("yzmCode") == null ? "" : String.valueOf(mapData.get("yzmCode"));
		String strLang = mapData.get("lang") == null ? "" : String.valueOf(mapData.get("lang"));
		String strSiteId = mapData.get("siteid") == null ? "" : String.valueOf(mapData.get("siteid"));

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String orgid = getHardCodePoint.getHardCodePointVal("TZ_SITE_JGID");
		try {
			if (LoginType.equals("STU")) {

				String sql = "";

				if (!"".equals(strUserName)) {
					strUserName = strUserName.toLowerCase();

				}

				if (null != strSiteId && !"".equals(strSiteId)) {

					sql = "SELECT TZ_DLZH_ID,TZ_JG_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MOBILE=? AND TZ_RYLX=?";
					List<Map<String, Object>> l = sqlQuery.queryForList(sql, new Object[] { strUserName, "PXXY" });

					if (l != null && l.size() > 0) {

						ArrayList<String> aryErrorMsg = new ArrayList<String>();
						Map<String, Object> map = null;
						boolean boolResult = false;
						String strOrgId = "";
						for (Object objNode : l) {
							map = (Map<String, Object>) objNode;
							strUserName = map.get("TZ_DLZH_ID").toString();
							strOrgId = map.get("TZ_JG_ID").toString();
							System.out.println("strUserName:" + strUserName);
							System.out.println("strOrgId:" + strOrgId);
							boolResult = tzWebsiteLoginServiceImpl.doLogin(request, response, strOrgId, strSiteId,
									strUserName, strPassWord, strYzmCode, strLang, LoginType, aryErrorMsg);
							System.out.println("boolResult:" + boolResult);
							// 只要有成功的就返回
							if (boolResult) {
								break;
							}
						}

						String loginStatus = aryErrorMsg.get(0);
						String errorMsg = aryErrorMsg.get(1);
						if (boolResult) {
							jsonMap.put("success", "true");
						} else {
							jsonMap.put("success", "false");
						}

						jsonMap.put("errorCode", loginStatus);
						jsonMap.put("errorDesc", errorMsg);

						if (boolResult) {
							String ctxPath = request.getContextPath();
							String indexUrl = "";
							Boolean isMobile = CommonUtils.isMobile(request);

							if (isMobile) {
								indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + strSiteId;
							} else {
								indexUrl = ctxPath + "/site/index/" + orgid.toLowerCase() + "/" + strSiteId;
							}

							jsonMap.put("url", indexUrl);
							System.out.println("url:" + indexUrl);
							TzSession tmpSession = new TzSession(request);
							tmpSession.addSession("LoginType", LoginType);

							tmpSession.addSession("STUJG", strOrgId);
						}

					} else {

						int errorCode = 2;
						String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00103", strLang, "登录失败，请确认用户名是否存在。",
								"Login failed, whether the username exists.");
						jsonMap.put("success", "false");
						jsonMap.put("errorCode", errorCode);
						jsonMap.put("errorDesc", strErrorDesc);

					}

				} else {
					int errorCode = 5;
					String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE",
							"44", strLang, "站点异常", "The website is abnormal .");
					jsonMap.put("success", "false");
					jsonMap.put("errorCode", errorCode);
					jsonMap.put("errorDesc", strErrorDesc);
				}

			} else if (LoginType.equals("TEA")) {
				String strOrgId = "ADMIN";
				if (null != strOrgId && !"".equals(strOrgId)) {
					strOrgId = strOrgId.toUpperCase();
					String sql = "";

					if (!"".equals(strUserName)) {
						strUserName = strUserName.toLowerCase();

					}

					if (null != strSiteId && !"".equals(strSiteId)) {
						System.out.println("strUserName:" + strUserName);
						sql = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE (TZ_MOBILE=? or TZ_EMAIL=?) AND TZ_RYLX=? AND TZ_JG_ID=?";
						strUserName = sqlQuery.queryForObject(sql,
								new Object[] { strUserName, strUserName, "JGJS", strOrgId }, "String");
						System.out.println("strUserName:" + strUserName);
						if (null != strUserName && !"".equals(strUserName)) {
							ArrayList<String> aryErrorMsg = new ArrayList<String>();

							boolean boolResult = tzWebsiteLoginServiceImpl.doLogin(request, response, strOrgId,
									strSiteId, strUserName, strPassWord, strYzmCode, strLang, LoginType, aryErrorMsg);

							String loginStatus = aryErrorMsg.get(0);
							String errorMsg = aryErrorMsg.get(1);
							if (boolResult) {
								jsonMap.put("success", "true");
							} else {
								jsonMap.put("success", "false");
							}

							jsonMap.put("errorCode", loginStatus);
							jsonMap.put("errorDesc", errorMsg);

							if (boolResult) {

								String ctxPath = request.getContextPath();

								String indexUrl = "";
								Boolean isMobile = CommonUtils.isMobile(request);

								if (isMobile) {
									indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + strSiteId;
								} else {
									indexUrl = ctxPath + "/site/index/" + strOrgId.toLowerCase() + "/" + strSiteId;
								}

								jsonMap.put("url", indexUrl);
								TzSession tmpSession = new TzSession(request);
								tmpSession.addSession("LoginType", LoginType);

							}

						} else {

							int errorCode = 2;
							String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
									"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00103", strLang, "登录失败，请确认用户名是否存在。",
									"Login failed, whether the username exists.");
							jsonMap.put("success", "false");
							jsonMap.put("errorCode", errorCode);
							jsonMap.put("errorDesc", strErrorDesc);

						}

					} else {
						int errorCode = 5;
						String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
								"TZ_SITE_MESSAGE", "44", strLang, "站点异常", "The website is abnormal .");
						jsonMap.put("success", "false");
						jsonMap.put("errorCode", errorCode);
						jsonMap.put("errorDesc", strErrorDesc);
					}
				} else {
					int errorCode = 5;
					String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE",
							"45", strLang, "机构异常", "The organization is abnormal .");
					jsonMap.put("success", "false");
					jsonMap.put("errorCode", errorCode);
					jsonMap.put("errorDesc", strErrorDesc);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jsonMap.get("url"));
		System.out.println(jsonMap.get("success"));
		return jacksonUtil.Map2json(jsonMap);
	}

	@RequestMapping(value = "logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {



		TzSession tmpSession = new TzSession(request);
		String LoginType = tmpSession.getSession("LoginType") == null ? "STU"
				: tmpSession.getSession("LoginType").toString();
		String redirect = "";
		if (LoginType.equals("STU")) {
			redirect = "redirect:" + "/user/login/stuLogin";
		} else if (LoginType.equals("TEA")) {
			redirect = "redirect:" + "/user/login/teaLogin";
		}

		tzWebsiteLoginServiceImpl.doLogout(request, response);

		return redirect;
	}

}
