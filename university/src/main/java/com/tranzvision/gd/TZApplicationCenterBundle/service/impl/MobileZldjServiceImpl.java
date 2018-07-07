package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.MobileZldjServiceImpl")
public class MobileZldjServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	
	@Override
	public String tzGetHtmlContent(String strParams)  {
		String clqdHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String ctxRoot = request.getContextPath();

		try {
			jacksonUtil.json2Map(strParams);
			String siteId = "";
			if(jacksonUtil.containsKey("siteId")){
				siteId = jacksonUtil.getString("siteId");
			}else{
				siteId = request.getParameter("siteId");
			}
			
			String instanceIdStr = "";
			if(jacksonUtil.containsKey("instanceId")){
				instanceIdStr = jacksonUtil.getString("instanceId");
			}else{
				instanceIdStr = request.getParameter("instanceId");
			}

			int instanceId = 0;
			if(!"".equals(instanceIdStr) && StringUtils.isNumeric(instanceIdStr)){
				instanceId = Integer.parseInt(instanceIdStr);
			}

			String classId = "";
			if(jacksonUtil.containsKey("bmClassId")){
				classId = jacksonUtil.getString("bmClassId");
			}else{
				classId = request.getParameter("bmClassId");
			}
			
			//获取语言和机构;
			String siteSQL = "select TZ_SITE_LANG,TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiInfo = jdbcTemplate.queryForMap(siteSQL,new Object[]{siteId});
			String strLangID = "";
			String orgId = "";
			if (mapSiteiInfo != null) {
				strLangID = mapSiteiInfo.get("TZ_SITE_LANG") == null ? "ZHS": String.valueOf(mapSiteiInfo.get("TZ_SITE_LANG"));
				orgId = mapSiteiInfo.get("TZ_JG_ID") == null ? ""	: String.valueOf(mapSiteiInfo.get("TZ_JG_ID"));
			}

			//资料递交;
		    String zldj = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "111", strLangID, "资料递交", "Materials Submitted");
		    // 不通过原因;
		    String btgYy = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "113", strLangID, "不通过原因", "Reason why not accepted");
		    //资料清单
			String zlqdDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "ZLQD", strLangID,"资料清单", "资料清单");
			
	        String TZ_CONT_INTRO = "", TZ_ZL_AUDIT_STATUS = "", TZ_AUDIT_NOPASS_RS = "";
	        String TZ_GD_ZLSQ_TR = "";
	        if(instanceId > 0 && classId != null && !"".equals(classId)){
	        	int total = 0;
	        	String sql = "select a.TZ_SORT_NUM,a.TZ_CONT_INTRO,b.TZ_ZL_AUDIT_STATUS,b.TZ_AUDIT_NOPASS_RS from PS_TZ_CLS_DJZL_T a LEFT JOIN (select * from PS_TZ_FORM_ZLSH_T where TZ_APP_INS_ID=?) b ON a.TZ_SBMINF_ID=b.TZ_SBMINF_ID WHERE  a.TZ_CLASS_ID = ? order by a.TZ_SORT_NUM asc";
	        	List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[]{instanceId, classId});
	        	if(list != null  && list.size() > 0){
	        		for(int i = 0; i < list.size();i++){
	        			
	        			TZ_CONT_INTRO =  (String)list.get(i).get("TZ_CONT_INTRO") == null ? "":(String)list.get(i).get("TZ_CONT_INTRO");
	        			TZ_ZL_AUDIT_STATUS =  (String)list.get(i).get("TZ_ZL_AUDIT_STATUS")== null ? "":(String)list.get(i).get("TZ_ZL_AUDIT_STATUS");
	        			TZ_AUDIT_NOPASS_RS =  (String)list.get(i).get("TZ_AUDIT_NOPASS_RS")== null ? "":(String)list.get(i).get("TZ_AUDIT_NOPASS_RS");
	        			
	        			total = total + 1;
	        			
	        			if(TZ_ZL_AUDIT_STATUS == null || "".equals(TZ_ZL_AUDIT_STATUS)){
	        				TZ_ZL_AUDIT_STATUS = "A";
	        			}
	        			
	        			// 查找审核状态描述值;
	        			/*
	        			String zlSpZtDesc = "";
	        			if("ZHS".equals(language)){
	        				zlSpZtDesc = jdbcTemplate.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BMB_DJWJSPZT' AND TZ_ZHZ_ID=?", new Object[]{TZ_ZL_AUDIT_STATUS},"String");
	        			}else{
	        				zlSpZtDesc = jdbcTemplate.queryForObject("SELECT COALESCE(B.TZ_ZHZ_DMS,A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (SELECT * FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_LANGUAGE_ID=?) B ON  A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID=B.TZ_ZHZ_ID WHERE A.TZ_ZHZJH_ID='TZ_BMB_DJWJSPZT' AND A.TZ_ZHZ_ID=?", new Object[]{language,TZ_ZL_AUDIT_STATUS},"String");
	        			}
	        			*/
	        			
	        			String imgeName = "";
	        			if("A".equals(TZ_ZL_AUDIT_STATUS)){
	        				imgeName = "dsh.png";
	        			}
	        			
	        			if("B".equals(TZ_ZL_AUDIT_STATUS)){
	        				imgeName = "tg1.png";
	        			}
	        			
	        			if("C".equals(TZ_ZL_AUDIT_STATUS)){
	        				imgeName = "wtg.png";
	        			}
	        			
	        			TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_CLQD_LI",zldj ,TZ_CONT_INTRO,btgYy,TZ_AUDIT_NOPASS_RS,ctxRoot,imgeName);
	                    
	        		}
	        	}

	        }
	        clqdHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_CLQD_CONTENT",zlqdDesc,TZ_GD_ZLSQ_TR); 
	        //需要引入的js和css
	    	String jsCss = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_JS_CSS",ctxRoot);
	        clqdHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",zlqdDesc,ctxRoot,siteId,orgId,"",jsCss,"style=\"background-color:#f1f1f1;\"",clqdHtml,"");
	        clqdHtml = objRep.repPhoneCss(clqdHtml, siteId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return clqdHtml;
	}
}
