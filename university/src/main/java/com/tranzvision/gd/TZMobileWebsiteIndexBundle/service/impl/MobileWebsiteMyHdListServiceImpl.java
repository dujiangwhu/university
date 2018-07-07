package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.Calendar.CalendarUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;

@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteMyHdListServiceImpl")
public class MobileWebsiteMyHdListServiceImpl extends FrameworkImpl {

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
	
	
	//活动列表
	@Override
	public String tzGetHtmlContent(String strParams) {
		String indexHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		String menuId="";

		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
		}else{
			siteId = request.getParameter("siteId");
		}
		if(jacksonUtil.containsKey("menuId")){
			menuId = jacksonUtil.getString("menuId");
		}else{
			menuId = request.getParameter("menuId");
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
			//栏目ＩＤ
			String strColuId = sqlQuery.queryForObject("select TZ_MENU_COLUMN from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=? and TZ_MENU_ID=? and (TZ_SHOW_MOBILE_FLG='Y'or TZ_MENU_STATE='Y')", new Object[] { siteId, menuId }, "String");
			//网页标题		
			String	title="已报名的活动";
		
			//需要引入的js和css
			String jsCss = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_MYHD_TZ_LIST_JS_CSS",ctxPath,siteId,strColuId);
			//主体部分
			String content = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_HDLIST_HTML",title,"");
			indexHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",title,ctxPath,siteId,orgId,menuId,jsCss,"",content,"");
			//手机版样式替换
			indexHtml = objRep.repPhoneCss(indexHtml, siteId);
		} catch (TzSystemException e) {
			indexHtml = "";
			e.printStackTrace();
		}

		return indexHtml;
	}
	
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
	    
		String dtFormat = getSysHardCodeVal.getDateTimeFormat();
		SimpleDateFormat datetimeformat = new SimpleDateFormat(dtFormat);
		//String strDateNow = datetimeformat.format(new Date());
		
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		//String dispatcherUrl = request.getContextPath() + "/dispatcher";
		
		String titleLi = "";
		try {
			//String siteLangSql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteLang");
			//String strLang = sqlQuery.queryForObject(siteLangSql, new Object[] { siteId }, "String");
			int limit = 10;
			int startNum = (pagenum - 1) * limit;
			//查看当前用户有没有设置范围;
			//如果没有设置范围，且没有报报名表则显示全部的;
			//其他的显示并集;
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
			
			List<Map<String, Object>> artList = new ArrayList<Map<String, Object>>();
			String sql="";
			if(haveBmCount == 0 && selectShowCount==0){
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDListByOprid");
				artList = sqlQuery.queryForList(sql,
						new Object[] { siteId, currentColumnId, m_curOPRID,startNum, limit });
			}else{
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDListByOpridAndByProject");
				artList = sqlQuery.queryForList(sql,
						new Object[] { siteId, currentColumnId,m_curOPRID,m_curOPRID,m_curOPRID, startNum, limit });
			}
				
			if (artList != null && artList.size()>0){
				for(int j=0;j<artList.size();j++){	
					//String strColuId = artList.get(j).get("TZ_COLU_ID") == null ? "" : String.valueOf(artList.get(j).get("TZ_COLU_ID"));
					String strArtId = artList.get(j).get("TZ_ART_ID") == null ? ""
							: String.valueOf(artList.get(j).get("TZ_ART_ID"));
					String strArtTitle = artList.get(j).get("TZ_NACT_NAME") == null ? ""
							: String.valueOf(artList.get(j).get("TZ_NACT_NAME"));
					String strArtTime = artList.get(j).get("TZ_START_DT") == null ? ""
							: datetimeformat.format(artList.get(j).get("TZ_START_DT"));
					String strActCity = artList.get(j).get("TZ_HD_CS") == null ? ""
							: String.valueOf(artList.get(j).get("TZ_HD_CS"));
					String strActAppfTime=artList.get(j).get("TZ_APPF_TM") == null ? ""
							: String.valueOf(artList.get(j).get("TZ_APPF_TM"));
					String strActAppeTime=artList.get(j).get("TZ_APPE_TM") == null ? ""
							: String.valueOf(artList.get(j).get("TZ_APPE_TM"));
					SimpleDateFormat simpleDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat simpleDttmFormat = new SimpleDateFormat("MM/dd HH:mm");
					
					if(!"".equals(strActAppfTime)){
						strActAppfTime = simpleDttmFormat.format(simpleDatetimeFormat.parse(strActAppfTime)); 
					}
					if(!"".equals(strActAppeTime)){
						strActAppeTime = simpleDttmFormat.format(simpleDatetimeFormat.parse(strActAppeTime)); 
					}
				
					CalendarUtil calendarUtil = new CalendarUtil(simpleDatetimeFormat.parse(strArtTime));

					String strMonth=calendarUtil.getDateMonth0();
					String strDay=calendarUtil.getDateDay();
				
					//String strUrl = dispatcherUrl + "?classid=art_view&operatetype=HTML&siteId=" + siteId + "&columnId="
							//+ strColuId + "&artId=" + strArtId + "&oprate=R&from=m";
				
					String strBmrId = sqlQuery.queryForObject("select TZ_HD_BMR_ID FROM PS_TZ_NAUDLIST_T where OPRID=? and TZ_ART_ID=? and TZ_NREG_STAT IN('1','4')", new Object[] { m_curOPRID, strArtId },"String");
					String strHdDate=strMonth+"月"+strDay+"日";
					titleLi = titleLi + tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_MYHD_TZ_DIV_HTML", strHdDate,strArtTitle,strActAppfTime+"-"+strActAppeTime,strActCity,strArtId,strBmrId);
					
					resultNum = resultNum +1;
				}
			}
		} catch (Exception e) {
			titleLi = "";
			e.printStackTrace();
		} 
		
		returnMap.replace("resultNum", resultNum);
		returnMap.replace("result", titleLi);
		return jacksonUtil.Map2json(returnMap);
	}
}
