/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
	private PsTzOpenidTblMapper psTzOpenidTblMapper;

	@Autowired
	private GetHardCodePoint GetHardCodePoint;

	// 学生登录 包含培训机构ID
	@RequestMapping(value = { "/stu/{orgid}/" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String stuLoginWebsite(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {

		String strRet = "";

		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			String neworgid = "ADMIN";

			// siteid =
			// tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(siteid);
			// 学生站点固定
			String siteid = "45";

			if (null != orgid && !"".equals(orgid) && null != siteid && !"".equals(siteid)) {

				String loginHtml = "";
				Boolean isMobile = CommonUtils.isMobile(request);
				if (isMobile) {
					String openid = request.getParameter("OPENID");
					if (openid == null) {
						openid = "";
					}
					loginHtml = tzWebsiteServiceImpl.getMLoginPublishCode(request, neworgid, siteid, openid);
					strRet = loginHtml;
				} else {
					loginHtml = tzWebsiteServiceImpl.getLoginPublishCode(request, neworgid, siteid);
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

	// @RequestMapping(value = { "/{orgid}/{siteid}" }, produces =
	// "text/html;charset=UTF-8")
	// @ResponseBody
	/*
	 * public String userLoginWebsite(HttpServletRequest request,
	 * HttpServletResponse response,
	 * 
	 * @PathVariable(value = "orgid") String orgid, @PathVariable(value =
	 * "siteid") String siteid) {
	 * 
	 * String strRet = "";
	 * 
	 * try { orgid =
	 * tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).
	 * toUpperCase();
	 * 
	 * siteid =
	 * tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(siteid);
	 * 
	 * if (null != orgid && !"".equals(orgid) && null != siteid &&
	 * !"".equals(siteid)) {
	 * 
	 * String loginHtml = ""; Boolean isMobile = CommonUtils.isMobile(request);
	 * if (isMobile) { String openid = request.getParameter("OPENID"); if
	 * (openid == null) { openid = ""; } loginHtml =
	 * tzWebsiteServiceImpl.getMLoginPublishCode(request, orgid, siteid,
	 * openid); strRet = loginHtml; } else { loginHtml =
	 * tzWebsiteServiceImpl.getLoginPublishCode(request, orgid, siteid); strRet
	 * = loginHtml; } strRet = loginHtml; } else {
	 * 
	 * strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "",
	 * "", "", "访问站点异常，请检查您访问的地址是否正确。",
	 * "Can not visit the site.Please check the url.");
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); strRet =
	 * gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "",
	 * "访问站点异常，请检查您访问的地址是否正确。", "Can not visit the site.Please check the url.");
	 * }
	 * 
	 * return strRet; }
	 */

	@RequestMapping(value = "dologin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(request.getParameter("tzParams"));

		Map<String, Object> mapData = jacksonUtil.getMap("comParams");
		String LoginType = mapData.get("LoginType") == null ? "" : String.valueOf(mapData.get("LoginType"));

		System.out.println("LoginType：" + LoginType);
		String strOrgId = mapData.get("orgid") == null ? "" : String.valueOf(mapData.get("orgid"));
		String strUserName = mapData.get("userName") == null ? "" : String.valueOf(mapData.get("userName")).trim();
		String strPassWord = mapData.get("passWord") == null ? "" : String.valueOf(mapData.get("passWord"));
		String strYzmCode = mapData.get("yzmCode") == null ? "" : String.valueOf(mapData.get("yzmCode"));
		String strLang = mapData.get("lang") == null ? "" : String.valueOf(mapData.get("lang"));
		String strSiteId = mapData.get("siteid") == null ? "" : String.valueOf(mapData.get("siteid"));

		String openid = request.getParameter("OPENID");

		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {
			if (LoginType.equals("STU")) {
				if (null != strOrgId && !"".equals(strOrgId)) {
					strOrgId = strOrgId.toUpperCase();
					String sql = "";

					if (!"".equals(strUserName)) {
						strUserName = strUserName.toLowerCase();
					
					}

					if (null != strSiteId && !"".equals(strSiteId)) {

						sql = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MOBILE=? AND TZ_RYLX=? AND TZ_JG_ID=?";
						strUserName = sqlQuery.queryForObject(sql, new Object[] { strUserName, "PXXY", strOrgId },
								"String");

						if (null != strUserName && !"".equals(strUserName)) {
							ArrayList<String> aryErrorMsg = new ArrayList<String>();

							boolean boolResult = tzWebsiteLoginServiceImpl.doLogin(request, response, strOrgId,
									strSiteId, strUserName, strPassWord, strYzmCode, strLang, aryErrorMsg);

							String loginStatus = aryErrorMsg.get(0);
							String errorMsg = aryErrorMsg.get(1);
							if (boolResult) {
								jsonMap.put("success", "true");

								/********************
								 * 记录openid绑定账户关系*张浪添加
								 ***********************/
								// 记录openid绑定账户关系
								if (!"".equals(openid) && openid != null) {
									PsTzOpenidTblKey psTzOpenidTblKey = new PsTzOpenidTblKey();
									psTzOpenidTblKey.setOpenid(openid);
									psTzOpenidTblKey.setTzDlzhId(strUserName);
									psTzOpenidTblKey.setTzJgId(strOrgId);
									psTzOpenidTblKey.setTzSiteiId(strSiteId);
									PsTzOpenidTbl psTzOpenidTbl = psTzOpenidTblMapper
											.selectByPrimaryKey(psTzOpenidTblKey);
									if (psTzOpenidTbl != null) {
										psTzOpenidTbl.setTzDelFlg("N");
										psTzOpenidTblMapper.updateByPrimaryKey(psTzOpenidTbl);
									} else {
										psTzOpenidTbl = new PsTzOpenidTbl();
										psTzOpenidTbl.setOpenid(openid);
										psTzOpenidTbl.setTzDlzhId(strUserName);
										psTzOpenidTbl.setTzJgId(strOrgId);
										psTzOpenidTbl.setTzSiteiId(strSiteId);
										psTzOpenidTbl.setTzDelFlg("N");
										psTzOpenidTblMapper.insert(psTzOpenidTbl);
									}

									String tmpOpenIDKey = "TZGD_@_!_*_20170420_Tranzvision";
									openid = DESUtil.encrypt(openid, tmpOpenIDKey);
									tzCookie.addCookie(response, "TZGD_WECHART_OPENID", openid);
								}
								/********************
								 * 记录openid绑定账户关系*张浪添加
								 ***********************/
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
			} else if (LoginType.equals("TEA")) {
				int errorCode = 2;
				String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
						"TZGD_FWINIT_00103", strLang, "登录失败，请确认用户名是否存在。", "Login failed, whether the username exists.");
				jsonMap.put("success", "false");
				jsonMap.put("errorCode", errorCode);
				jsonMap.put("errorDesc", strErrorDesc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// {"success":"%bind(:1)","error":"%bind(:2)","indexUrl":"%bind(:3)"}

		return jacksonUtil.Map2json(jsonMap);
	}

	@RequestMapping(value = "logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {

		String orgid = tzCookie.getStringCookieVal(request, tzWebsiteLoginServiceImpl.cookieWebOrgId);

		String siteid = tzCookie.getStringCookieVal(request, tzWebsiteLoginServiceImpl.cookieWebSiteId);

		tzWebsiteLoginServiceImpl.doLogout(request, response);

		// String ctx = request.getContextPath();

		orgid = orgid.toLowerCase();

		String redirect = "redirect:" + "/user/login/" + orgid + "/" + siteid;

		return redirect;
	}

}
