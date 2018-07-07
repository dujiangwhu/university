/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_MENU_SHOW_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMenuShowServiceImpl")
public class TzMenuShowServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	
	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		//是否移动设备访问
		boolean isMobile = CommonUtils.isMobile(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");
			String strMenuId = jacksonUtil.getString("menuId");
			String ctxPath = request.getContextPath();
			if ((null != strSiteId && !"".equals(strSiteId)) && (null != strMenuId && !"".equals(strMenuId))) {
			   //资料下载-手机版
               if(isMobile){
            	   String indexHtml="";
					//获取语言和机构;
					String siteSQL = "select TZ_SITE_LANG,TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
					Map<String, Object> mapSiteiInfo = sqlQuery.queryForMap(siteSQL,new Object[]{strSiteId});
					//String strLangID = "";
					String orgId = "";
					if (mapSiteiInfo != null) {
						//strLangID = mapSiteiInfo.get("TZ_SITE_LANG") == null ? "ZHS": String.valueOf(mapSiteiInfo.get("TZ_SITE_LANG"));
						orgId = mapSiteiInfo.get("TZ_JG_ID") == null ? ""	: String.valueOf(mapSiteiInfo.get("TZ_JG_ID"));
					}
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetColuidBySiteidMenuidMenuStateY");
					String strColuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");
				
					//获取栏目名称;
					String columnNameSQL = "SELECT TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? and TZ_COLU_ID=?";
					String title = sqlQuery.queryForObject(columnNameSQL, new Object[] { strSiteId,strColuId }, "String");
					
					//需要引入的js和css
					String jsCss = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZL_TZ_LIST_JS_CSS",ctxPath,strSiteId,strColuId);
					//主体部分
					String content = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_HDLIST_HTML",title,"");
		            
					indexHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",title,ctxPath,strSiteId,orgId,strMenuId,jsCss,"",content,"");
					indexHtml = objRep.repPhoneCss(indexHtml, strSiteId);
					return indexHtml;
               }else{
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteiTempPCcode");
				Map<String, Object> mapSiteiCode = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strMenuId });
				String strResultContent = mapSiteiCode.get("TZ_TEMP_PCCODE") == null ? ""
						: String.valueOf(mapSiteiCode.get("TZ_TEMP_PCCODE"));
				String strScriptHtmlName = mapSiteiCode.get("TZ_PCTEMP_SCRIPT_HTML") == null ? ""
						: String.valueOf(mapSiteiCode.get("TZ_PCTEMP_SCRIPT_HTML"));

				if (null != strResultContent) {

					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetOrgidSiteLangBySiteid");
					Map<String, Object> mapSiteOrg = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
					String orgid = "";
					if (mapSiteOrg != null) {
						orgid = mapSiteOrg.get("TZ_JG_ID") == null ? "" : String.valueOf(mapSiteOrg.get("TZ_JG_ID"));
					}

					sql = "select TZ_MENU_NAME from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=? and TZ_MENU_ID=? and TZ_MENU_STATE='Y'";
					String strMenuName = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");

					strResultContent = siteRepCssServiceImpl.repContextPath(strResultContent);
					strResultContent = siteRepCssServiceImpl.repTitle(strResultContent, strSiteId);
					strResultContent = siteRepCssServiceImpl.repCss(strResultContent, strSiteId);
					strResultContent = siteRepCssServiceImpl.repMenuName(strResultContent, strMenuName);

					String strSelfJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle." + strScriptHtmlName,
							ctxPath);

					strResultContent = siteRepCssServiceImpl.repJavascriptTags(strResultContent, strSelfJavascripts,
							orgid, strSiteId, "Y");
					
					return strResultContent;
				} else {
					return "内容不存在！";
				}
              }/*isMobile-end*/
			} else {
				return "参数错误！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "异常退出！";
		}

		return strRet;

	}

}
