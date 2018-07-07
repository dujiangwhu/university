/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * MBA招生网站联系我们
 * 
 * @author jufeng
 * @since 2017-04-14
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzContactUsServicelImpl")
public class TzContactUsServicelImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Override
	public String tzGetHtmlContent(String strParams) {
		String contactUsHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			String strMenuId = "";
			// String oprate = "";
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}

			if (strSiteId == null || "".equals(strSiteId)) {
				strSiteId = request.getParameter("siteId");
			}

			if (jacksonUtil.containsKey("menuId")) {
				strMenuId = jacksonUtil.getString("menuId");
			}

			if (strMenuId == null || "".equals(strMenuId)) {
				strMenuId = request.getParameter("menuId");
			}
			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";
			String strCssDir = "";
			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				String skinstor = (String) siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
				String websitePath = getSysHardCodeVal.getWebsiteCssPath();

				String strRandom = String.valueOf(10 * Math.random());
				if ("".equals(skinstor) || skinstor == null) {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				} else {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + skinstor + "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				}
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}
			// 联系我们;
			String ContactUs = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "300",
					language, "联系我们", "Contact Us");
			String columnSql = "select TZ_MENU_COLUMN from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID =? and TZ_MENU_ID =? and TZ_MENU_STATE = 'Y'";
			String contentSql = "select TZ_ART_CONENT from PS_TZ_ART_REC_TBL where TZ_ART_ID = (select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_COLU_ID =("+columnSql+"));";
			String appGuidecontent = jdbcTemplate.queryForObject(contentSql, new Object[] { strSiteId,strMenuId},"String");
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			// 展示页面;
			contactUsHtml = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzContactUs",
					appGuidecontent, ZSGL_URL, strCssDir,
					ContactUs, str_jg_id, strSiteId, request.getContextPath());
			contactUsHtml = siteRepCssServiceImpl.repTitle(contactUsHtml, strSiteId);
			contactUsHtml = siteRepCssServiceImpl.repCss(contactUsHtml, strSiteId);
		} catch (Exception e) {
			e.printStackTrace();
			return "无法获取相关数据";
		}
		return contactUsHtml;
	}
}
