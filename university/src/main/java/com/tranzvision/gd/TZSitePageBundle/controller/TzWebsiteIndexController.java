/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteIndexServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.session.TzSession;

/**
 * 网站首页展示
 * 
 * @author SHIHUA
 * @since 2016-01-06
 */
@Controller
@RequestMapping(value = { "/site/index" })
public class TzWebsiteIndexController {

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;

	@Autowired
	private MobileWebsiteIndexServiceImpl mobileWebsiteIndexServiceImpl;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	@RequestMapping(value = { "/{orgid}/{siteid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String websiteIndex(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid) {

		orgid = orgid.toLowerCase();
		String strRet = "";

		if (CommonUtils.isMobile(request)) {
			String strParams = "{\"siteId\":\"" + siteid + "\"}";
			strRet = mobileWebsiteIndexServiceImpl.tzGetHtmlContent(strParams);
		} else {
			if (!tzWebsiteLoginServiceImpl.checkUserLogin(request, response)) {
				System.out.println("NO LOGIN");
				String redirectUrl = request.getContextPath() + "/user/login/stuLogin";
				try {
					strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return strRet;
			}
			// 修改做检验，学生和教师的站点不能相互访问
			TzSession tmpSession = new TzSession(request);
			String LoginType = tmpSession.getSession("LoginType") == null ? ""
					: tmpSession.getSession("LoginType").toString();

			String stuSiteid = getHardCodePoint.getHardCodePointVal("TZ_STU_MH");
			String teaSiteid = getHardCodePoint.getHardCodePointVal("TZ_TEA_MH");
			if (LoginType != null && !LoginType.equals("")) {
				System.out.println("LoginType:"+LoginType);
				System.out.println("siteid:"+siteid);
				if (LoginType.equals("STU") && !siteid.equals(stuSiteid)) {
					String redirectUrl = request.getContextPath() + "/user/login/stuLogin";
					try {
						strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
					} catch (TzSystemException e) {
						e.printStackTrace();
					}
					return strRet;

				}
				
				if (LoginType.equals("TEA") && !siteid.equals(teaSiteid)) {
					String redirectUrl = request.getContextPath() + "/user/login/teaLogin";
					try {
						strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
					} catch (TzSystemException e) {
						e.printStackTrace();
					}
					return strRet;

				}
			}

			strRet = tzWebsiteServiceImpl.getIndexPublishCode(request, orgid, siteid);

			if ("errororg".equals(strRet)) {
				System.out.println(strRet);

				String redirectUrl = "";
				if (LoginType.equals("STU")) {
					redirectUrl = request.getContextPath() + "/user/login/stuLogin";
				} else if (LoginType.equals("TEA")) {
					redirectUrl = request.getContextPath() + "/user/login/teaLogin";
				}

				try {
					strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return strRet;
			}
		}

		return strRet;
	}

}
