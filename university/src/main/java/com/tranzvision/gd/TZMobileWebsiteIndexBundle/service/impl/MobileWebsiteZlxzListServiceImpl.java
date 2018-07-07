package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteZlxzListServiceImpl")
public class MobileWebsiteZlxzListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	
	/*//活动列表
	@Override
	public String tzGetHtmlContent(String strParams) {
		String indexHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		String columnId="";
		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
		}else{
			siteId = request.getParameter("siteId");
		}
		if(jacksonUtil.containsKey("columnId")){
			columnId = jacksonUtil.getString("columnId");
		}else{
			columnId = request.getParameter("columnId");
		}
		try {
			
			//rootPath;
			String ctxPath = request.getContextPath();
			
			//获取语言和机构;
			String siteSQL = "select TZ_SITE_LANG,TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiInfo = sqlQuery.queryForMap(siteSQL,new Object[]{siteId});
			//String strLangID = "";
			String orgId = "";
			if (mapSiteiInfo != null) {
				//strLangID = mapSiteiInfo.get("TZ_SITE_LANG") == null ? "ZHS": String.valueOf(mapSiteiInfo.get("TZ_SITE_LANG"));
				orgId = mapSiteiInfo.get("TZ_JG_ID") == null ? ""	: String.valueOf(mapSiteiInfo.get("TZ_JG_ID"));
			}
		
			//获取栏目名称;
			String columnNameSQL = "SELECT TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? and TZ_COLU_ID=?";
			String title = sqlQuery.queryForObject(columnNameSQL, new Object[] { siteId,columnId }, "String");
			
			//需要引入的js和css
			String jsCss = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZL_TZ_LIST_JS_CSS",ctxPath,siteId,columnId);
			//主体部分
			String content = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_HDLIST_HTML",title,"");
            
			indexHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",title,ctxPath,siteId,orgId,"",jsCss,"",content,"");
			indexHtml = objRep.repPhoneCss(indexHtml, siteId);
		} catch (TzSystemException e) {
			indexHtml = "";
			e.printStackTrace();
		}

		return indexHtml;
	}*/
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("resultNum", 0);
		returnMap.put("result", "");
		int resultNum = 0;
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(strParams);
		String siteId = "",currentColumnId = "";
		int pagenum = 0;
		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
			try {
				pagenum = jacksonUtil.getInt("pagenum");
			} catch (Exception e) {
				e.printStackTrace();
				pagenum = 1;
			}
			currentColumnId = jacksonUtil.getString("columnId");
		}
	
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String dispPath = request.getContextPath()+"/dispatcher";
		String titleLi = "";
		try {
			int limit = 10;
			int startNum = (pagenum - 1) * limit;
			//查看当前用户有没有设置范围;
			//如果没有设置范围，且没有报报名表则显示全部的;
			//其他的显示并集;
			//String jgId = tzLoginServiceImpl.getLoginedManagerOrgid(request); 
			//String isPrjShowWW = sqlQuery.queryForObject("select TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_JG_ID=? AND TZ_REG_FIELD_ID='TZ_PROJECT'", new Object[]{jgId},"String");
			String isPrjShowWW = sqlQuery.queryForObject("select TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_SITEI_ID=? AND TZ_REG_FIELD_ID='TZ_PROJECT'", new Object[]{siteId},"String");
			int haveBmCount = 0;
			int selectShowCount = 0;
			
			if("Y".equals(isPrjShowWW)){
				String haveBmCountSql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetCurBmbCountByOprid");
				//有没有报名已经开放班级;
				haveBmCount = sqlQuery.queryForObject(haveBmCountSql, new Object[]{m_curOPRID,siteId},"Integer");
				//有没有选择查看的范围;
				selectShowCount = sqlQuery.queryForObject("SELECT count(1) FROM PS_SHOW_PRJ_NEWS_T where OPRID=?", new Object[]{m_curOPRID}, "Integer");
			}
			
			List<Map<String, Object>> artList;
			String sql="";
			if(haveBmCount == 0 && selectShowCount==0){
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteArtsList");
				artList = sqlQuery.queryForList(sql,
						new Object[] { siteId, currentColumnId, startNum, limit });
			}else{
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteArtsListByProject");
				artList = sqlQuery.queryForList(sql,
						new Object[] { siteId, currentColumnId,m_curOPRID,m_curOPRID, startNum, limit });
			}
			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat datetimeformat = new SimpleDateFormat(dtFormat);
			if (artList != null && artList.size()>0){
				for(int j=0;j<artList.size();j++){	
 
					String strColuId = artList.get(j).get("TZ_COLU_ID") == null ? "" : String.valueOf(artList.get(j).get("TZ_COLU_ID"));
					String strArtId = artList.get(j).get("TZ_ART_ID") == null ? ""
							: String.valueOf(artList.get(j).get("TZ_ART_ID"));
					String strArtTitle = artList.get(j).get("TZ_ART_TITLE") == null ? ""
							: String.valueOf(artList.get(j).get("TZ_ART_TITLE"));
					String strArtTime = artList.get(j).get("TZ_ART_NEWS_DT") == null ? ""
							: datetimeformat.format(artList.get(j).get("TZ_ART_NEWS_DT"));
					
					String strUrl = dispPath + "?classid=art_view&operatetype=HTML&siteId=" + siteId + "&columnId="
							+ strColuId + "&artId=" + strArtId + "&oprate=R&from=m";
					
					titleLi = titleLi + tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZL_TZ_LI_HTML", strUrl,strArtTitle,strArtTime);
					resultNum = resultNum +1;
				}
			}
		} catch (TzSystemException e) {
			titleLi = "";
			e.printStackTrace();
		} 
		
		returnMap.replace("resultNum", resultNum);
		returnMap.replace("result", titleLi);
		return jacksonUtil.Map2json(returnMap);
	}

}
