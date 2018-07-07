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

@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.MobileTjxServiceImpl")
public class MobileTjxServiceImpl extends FrameworkImpl {
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
		String tjxHtml = "";
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
			
			//获取语言和机构;
			String siteSQL = "select TZ_SITE_LANG,TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiInfo = jdbcTemplate.queryForMap(siteSQL,new Object[]{siteId});
			String language = "";
			String orgId = "";
			if (mapSiteiInfo != null) {
				language = mapSiteiInfo.get("TZ_SITE_LANG") == null ? "ZHS": String.valueOf(mapSiteiInfo.get("TZ_SITE_LANG"));
				orgId = mapSiteiInfo.get("TZ_JG_ID") == null ? ""	: String.valueOf(mapSiteiInfo.get("TZ_JG_ID"));
			}

			// 推荐人;
			String tjr = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "114", language, "推荐人", "Referee");
		    // 发送状态;
			String fszt = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "115", language, "发送状态", "Email Status");
		    // 推荐信提交状态;
			//String tjxtjzt = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "116", language, "推荐信提交状态", "Refes Status");
		    // 已经提交;
			//String tjsSubmit = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "117", language, "已提交", "Submitted");
		    // 未提交;
			//String tjsUSubmit = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "118", language, "未提交", "Unsubmitted");
		    // 发送;
			String send = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "121", language, "发送", "Send");
		    // 未发送;
			//String unsend = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "120", language, "未发送", "Unsent");
		      
		    // 推荐人已在线填写;
			String fillOnline = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "125", language, "推荐人已在线填写", "Filling in online");
		      
		    // 推荐人上传附件;
			String uploadTjx = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "126", language, "手工上传推荐信附件", "Upload");
			//推荐信
			String tjxDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "TJX", language,"推荐信", "推荐信");
			
			String TZ_GD_ZLSQ_TR = "";
			// 报名人填写的推荐人数;
		    int totalTjxNum = jdbcTemplate.queryForObject("select COUNT(1) from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_MBA_TJX_YX='Y'", new Object[]{instanceId},"Integer");
		    if(totalTjxNum > 0){
		    	 String tjrxx = "";
		         long tjxInstId = 0L;
		         String TZ_REF_LETTER_ID = "";
		         String TZ_REFLETTERTYPE = "";
		         String tjxSql = "select TZ_REFERRER_NAME,TZ_TJX_APP_INS_ID,TZ_REF_LETTER_ID,TZ_REFLETTERTYPE from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_MBA_TJX_YX='Y' order by TZ_TJR_ID asc";
		         List<Map<String, Object>> tjxList = jdbcTemplate.queryForList(tjxSql,new Object[]{instanceId});
		         if(tjxList != null && tjxList.size() > 0){
		        	for(int i = 0 ; i < tjxList.size(); i++){
		        		tjrxx = (String)tjxList.get(i).get("TZ_REFERRER_NAME")==null?"":(String)tjxList.get(i).get("TZ_REFERRER_NAME");
		        		try{
		        			tjxInstId = Long.parseLong(tjxList.get(i).get("TZ_TJX_APP_INS_ID").toString());
		        		}catch(Exception e){
		        			tjxInstId = 0L;
		        		}
		        		TZ_REF_LETTER_ID = (String)tjxList.get(i).get("TZ_REF_LETTER_ID")==null?"":(String)tjxList.get(i).get("TZ_REF_LETTER_ID"); 
		        		TZ_REFLETTERTYPE = (String)tjxList.get(i).get("TZ_REFLETTERTYPE")==null?"":(String)tjxList.get(i).get("TZ_REFLETTERTYPE"); 

		        		// 是否提交;
		                int isTj = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_TZ_KS_TJX_TBL A WHERE ((A.ATTACHSYSFILENAME <> ' ' AND A.ATTACHUSERFILE <> ' ') OR  EXISTS (SELECT 'Y' FROM PS_TZ_APP_INS_T B WHERE A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U' AND A.TZ_TJX_APP_INS_ID > 0)) AND A.TZ_APP_INS_ID = ? and A.TZ_REF_LETTER_ID=? and A.TZ_MBA_TJX_YX='Y'", new Object[]{instanceId, TZ_REF_LETTER_ID},"Integer");
		                if(isTj > 0){
		                	String imgeName = "ytj.png";
		                	if("S".equals(TZ_REFLETTERTYPE)){
		                		/*已发送推荐信邮件，并完成了推荐信*/
		                		TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_CLQD_LI",tjr ,tjrxx,fszt,fillOnline,ctxRoot,imgeName);
		                	
		                	}else{
		                		TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_CLQD_LI",tjr ,tjrxx,fszt,uploadTjx,ctxRoot,imgeName);
		                	}
		                }else{
		                	String imgeName = "wtj.png";
		                	if("S".equals(TZ_REFLETTERTYPE)){
		                		if(tjxInstId > 0){ 
		                			TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_CLQD_LI",tjr ,tjrxx,fszt,fillOnline,ctxRoot,imgeName);
		                		}else{
		                			TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_CLQD_LI",tjr ,tjrxx,fszt,send,ctxRoot,imgeName);
		                		}
		                	}else{
		                		TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_CLQD_LI",tjr ,tjrxx,fszt,uploadTjx,ctxRoot,imgeName);
		                	}
		                }
		        	}
		         }
		    }
			
		    tjxHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_CLQD_CONTENT",tjxDesc,TZ_GD_ZLSQ_TR); 
	        //需要引入的js和css
	    	String jsCss = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_JS_CSS",ctxRoot);
	    	tjxHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",tjxDesc,ctxRoot,siteId,orgId,"",jsCss,"style=\"background-color:#f1f1f1;\"",tjxHtml,"");
	    	tjxHtml = objRep.repPhoneCss(tjxHtml, siteId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return tjxHtml;
	}
}
