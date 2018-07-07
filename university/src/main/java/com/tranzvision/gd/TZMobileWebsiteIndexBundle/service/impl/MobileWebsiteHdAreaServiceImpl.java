package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.Calendar.CalendarUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteHdAreaServiceImpl")
public class MobileWebsiteHdAreaServiceImpl implements MobleMiddleAreaInterface{
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
		String dispatcherUrl = request.getContextPath() + "/dispatcher";
		
		
		
		String moreUrl = dispatcherUrl + "?classid=hdList&siteId=" + strSiteId + "&columnId=" + strColuId ;
		try{
			String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteLang");
			String strLang = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");
			
			//新闻
			String hdDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "HD", strLang,"活动", "活动");
			//更多
			String moreDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "MORE", strLang,"更多", "更多");
			//暂时没有相关活动！
			String noHdDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "NOHD", strLang,"暂时没有相关活动！", "暂时没有相关活动！");
			
			String dtFormat = getSysHardCodeVal.getDateTimeFormat();
			SimpleDateFormat datetimeformat = new SimpleDateFormat(dtFormat);
			String strDateNow = datetimeformat.format(new Date());
			
			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDListByDateTimeGT" );
			List<Map<String, Object>> listSiteActivities = new ArrayList<Map<String, Object>>();
			listSiteActivities = sqlQuery.queryForList(sql,new Object[] { strSiteId, strColuId,strDateNow, 0, 3 });
			
			String strDateFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			if(listSiteActivities != null && listSiteActivities.size() > 0){
				String liHtml = "";
				for (Map<String, Object> mapActivity : listSiteActivities) {
					strColuId = mapActivity.get("TZ_COLU_ID") == null ? ""
							: String.valueOf(mapActivity.get("TZ_COLU_ID"));
					String strArtId = mapActivity.get("TZ_ART_ID") == null ? ""
							: String.valueOf(mapActivity.get("TZ_ART_ID"));
					String strActName = mapActivity.get("TZ_NACT_NAME") == null ? ""
							: String.valueOf(mapActivity.get("TZ_NACT_NAME"));
					String dtAct = mapActivity.get("TZ_START_DT") == null ? ""
							: String.valueOf(mapActivity.get("TZ_START_DT"));
					String strActCity = mapActivity.get("TZ_HD_CS") == null ? ""
							: String.valueOf(mapActivity.get("TZ_HD_CS"));
					//String strKqbm = mapActivity.get("TZ_QY_ZXBM") == null ? ""
					//		: String.valueOf(mapActivity.get("TZ_QY_ZXBM"));
					// String dtBmStart = mapActivity.get("TZ_APPF_DT") == null
					// ? "": String.valueOf(mapActivity.get("TZ_APPF_DT"));
					//String timeBmStart = mapActivity.get("TZ_APPF_TM") == null ? ""
					//		: String.valueOf(mapActivity.get("TZ_APPF_TM"));
					// String dtBmEnd = mapActivity.get("TZ_APPE_DT") == null ?
					// "": String.valueOf(mapActivity.get("TZ_APPE_DT"));
					//String timeBmEnd = mapActivity.get("TZ_APPE_TM") == null ? ""
					//		: String.valueOf(mapActivity.get("TZ_APPE_TM"));
					
//					String strUrl = dispatcherUrl + "?classid=art_view&operatetype=HTML&siteId=" + strSiteId
//							+ "&columnId=" + strColuId + "&artId=" + strArtId + "&oprate=R";
					String strUrl = sqlQuery.queryForObject("select TZ_ART_URL from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?",new Object[]{strSiteId,strColuId,strArtId},"String");
					if(strUrl == null){
						strUrl = "";
					}
					
					CalendarUtil calendarUtil = new CalendarUtil(dateFormat.parse(dtAct));


					liHtml = liHtml + tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HD_LI",strUrl ,calendarUtil.getDateMonthWord(strLang),calendarUtil.getDateDay(),strActName,strActCity);
					
				}
				strResultContent = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HD_LIST_HTML",hdDesc,moreDesc,liHtml ,moreUrl);
			}else{
				strResultContent = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_NO_HD_LIST_HTML",hdDesc,noHdDesc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return strResultContent;
	}
}
