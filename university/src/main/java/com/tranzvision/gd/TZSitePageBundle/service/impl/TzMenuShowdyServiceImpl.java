/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.session.TzGetSetSessionValue;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_MENU_SHOWDY_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMenuShowdyServiceImpl")
public class TzMenuShowdyServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzGetSetSessionValue tzGetSetSessionValue;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private SiteRepCssServiceImpl objRep;
	
	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		//是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);
		try {

			String ctxPath = request.getContextPath();

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");
			String strMenuId = jacksonUtil.getString("menuId");
			String strOrgid = "";
			String strLang = "";

			tzGetSetSessionValue.setTzSiteGloMenuId(strMenuId);

			if ((null != strSiteId && !"".equals(strSiteId)) && (null != strMenuId && !"".equals(strMenuId))) {

				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetOrgidSiteLangBySiteid");
				Map<String, Object> mapSiteInfo = sqlQuery.queryForMap(sql, new Object[] { strSiteId });

				if (null != mapSiteInfo) {
					strOrgid = mapSiteInfo.get("TZ_JG_ID") == null ? "" : String.valueOf(mapSiteInfo.get("TZ_JG_ID"));
					strLang = mapSiteInfo.get("TZ_SITE_LANG") == null ? ""
							: String.valueOf(mapSiteInfo.get("TZ_SITE_LANG"));
				}

				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetColuidBySiteidMenuid");
				String strColuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");

				if (null != strColuId && !"".equals(strColuId)) {

					Date dateNow = new Date();

					sql = "select TZ_ART_ID,TZ_ART_CONENT_SCR,TZ_ART_NEWS_DT from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_PUB_STATE='Y' order by TZ_ART_NEWS_DT desc limit 0,1";
					Map<String, Object> mapArtInfo = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strColuId });

					String str_art_id = mapArtInfo.get("TZ_ART_ID") == null ? ""
							: String.valueOf(mapArtInfo.get("TZ_ART_ID"));
					String str_html = mapArtInfo.get("TZ_ART_CONENT_SCR") == null ? ""
							: String.valueOf(mapArtInfo.get("TZ_ART_CONENT_SCR"));

					String dtFormat = getSysHardCodeVal.getDateTimeFormat();
					SimpleDateFormat format = new SimpleDateFormat(dtFormat);
					Date dt = mapArtInfo.get("TZ_ART_NEWS_DT") == null ? dateNow
							: format.parse(String.valueOf(mapArtInfo.get("TZ_ART_NEWS_DT")));

					if (null != str_art_id && !"".equals(str_art_id)) {

						sql = "select TZ_ART_TYPE1,TZ_OUT_ART_URL from PS_TZ_ART_REC_TBL where TZ_ART_ID = ?";
						Map<String, Object> mapArtUrl = sqlQuery.queryForMap(sql, new Object[] { str_art_id });

						String str_art_type = mapArtUrl.get("TZ_ART_TYPE1") == null ? ""
								: String.valueOf(mapArtUrl.get("TZ_ART_TYPE1"));
						String str_art_out_url = mapArtUrl.get("TZ_OUT_ART_URL") == null ? ""
								: String.valueOf(mapArtUrl.get("TZ_OUT_ART_URL"));
						//移动端访问
                        if(isMobile){
                        	
                        	String strUrl = ctxPath + "/dispatcher?classid=art_view&operatetype=HTML&siteId=" + strSiteId + "&columnId="
        							+ strColuId + "&artId=" + str_art_id + "&oprate=R&from=m";
                        	//String indexHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML","",ctxPath,strSiteId,strOrgid,strMenuId,"","","<script>window.location.href='"+strUrl+"'</script>","");
                			//手机版样式替换
                			//indexHtml = objRep.repPhoneCss(indexHtml, strSiteId);
                        	return "<script>window.location.href='"+strUrl+"'</script>";
                        }else{
							if ("B".equals(str_art_type)) {
								if (null != str_art_out_url && !"".equals(str_art_out_url)) {
									// 此处需要跳转
									// %Response.RedirectURL(&str_art_out_url);
									strRet = tzGDObject.getHTMLText("SQL.TZSitePageBundle.TzWinLocationAndOpenWin", "_self",
											str_art_out_url);
								} else {
									strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE",
											"66", strLang, "未找到内容", "Not found content");
									strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzNoContent", ctxPath, strRet);
								}
							} else {
								if (dt.getTime() <= dateNow.getTime()) {
									strRet = str_html;
									String strSelfJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsContent", ctxPath);
									strRet = siteRepCssServiceImpl.repJavascriptTags(strRet, strSelfJavascripts, strOrgid, strSiteId, "Y");
	
								} else {
									strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE",
											"66", strLang, "未找到内容", "Not found content");
									strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzNoContent", ctxPath, strRet);
								}
							}
                       }
					} else {
						strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "66",
								strLang, "未找到内容", "Not found content");
						strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzNoContent", ctxPath, strRet);
					}

				} else {
					strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "66", strLang,
							"未找到内容", "Not found content");
					strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzNoContent", ctxPath, strRet);
				}

			} else {
				strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "66", strLang,
						"未找到内容", "Not found content");
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzNoContent", ctxPath, strRet);
			}

			
			strRet = siteRepCssServiceImpl.repContextPath(strRet);
			strRet = siteRepCssServiceImpl.repTitle(strRet, strSiteId);
			//strRet = siteRepCssServiceImpl.repCss(strRet, strSiteId);
			strRet = siteRepCssServiceImpl.repSiteid(strRet, strSiteId);
			strRet = siteRepCssServiceImpl.repJgid(strRet, strOrgid.toUpperCase());
			strRet = siteRepCssServiceImpl.repLang(strRet, strLang.toUpperCase());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;

	}

}
