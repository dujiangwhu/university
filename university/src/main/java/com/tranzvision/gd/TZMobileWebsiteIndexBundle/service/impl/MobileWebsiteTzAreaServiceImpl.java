package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * 手机招生网站，通知中间栏;
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteTzAreaServiceImpl")
public class MobileWebsiteTzAreaServiceImpl implements MobleMiddleAreaInterface{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	@Override
	public String tzGetHtmlContent(String strSiteId, String areaId,String strColuId) {
		String strResultContent = "";
		try{	
			String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteLang");
			String strLang = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");
			//新闻
			String newsDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "NEWS", strLang,"新闻", "新闻");
			//更多
			String moreDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "MORE", strLang,"更多", "更多");
			//"暂时没有相关内容！"
			String noNrDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "NONR", strLang,"暂时没有相关内容！", "暂时没有相关内容！");
			
			String dispatcherUrl = request.getContextPath() + "/dispatcher";
			String moreUrl = dispatcherUrl +"?classid=newList&siteId=" + strSiteId + "&columnId=" + strColuId;

			String dtFormat = getSysHardCodeVal.getDateTimeHMFormat();
			SimpleDateFormat datetimeformat = new SimpleDateFormat(dtFormat);
			
			List<Map<String, Object>> listSiteArts;
			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteArtsList");
			listSiteArts = sqlQuery.queryForList(sql,new Object[] { strSiteId, strColuId, 0, 3 });
			if(listSiteArts != null && listSiteArts.size() > 0){
				String liHtml = "";
				for (Map<String, Object> mapSiteArt : listSiteArts) {
					
					strColuId = mapSiteArt.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapSiteArt.get("TZ_COLU_ID"));
					String strArtId = mapSiteArt.get("TZ_ART_ID") == null ? ""
							: String.valueOf(mapSiteArt.get("TZ_ART_ID"));
					String strArtTitle = mapSiteArt.get("TZ_ART_TITLE") == null ? ""
							: String.valueOf(mapSiteArt.get("TZ_ART_TITLE"));
					String strArtTime = mapSiteArt.get("TZ_ART_NEWS_DT") == null ? ""
							: datetimeformat.format(mapSiteArt.get("TZ_ART_NEWS_DT"));
					

//					String strUrl = dispatcherUrl + "?classid=art_view&operatetype=HTML&siteId=" + strSiteId + "&columnId="
//							+ strColuId + "&artId=" + strArtId + "&oprate=R";
					String strUrl = sqlQuery.queryForObject("select TZ_ART_URL from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?",new Object[]{strSiteId,strColuId,strArtId},"String");
					if(strUrl == null){
						strUrl = "";
					}
					
					liHtml = liHtml + tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_TZ_LI",strUrl ,strArtTitle,strArtTime);

				}
				strResultContent = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_TZ_LIST_HTML",newsDesc,moreDesc,liHtml ,moreUrl);
			}else{
				strResultContent = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_NO_TZ_LIST_HTML",newsDesc,noNrDesc);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return strResultContent;
	}
}
