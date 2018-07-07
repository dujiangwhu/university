package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 手机版招生网站首页
 * classId: mIndex
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteIndexServiceImpl")
public class MobileWebsiteIndexServiceImpl extends FrameworkImpl  {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TzCookie tzCookie;
	@Autowired
	private ApplicationContext ctx;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	/*手机版招生网站首页*/
	@Override
	public String tzGetHtmlContent(String strParams) {
		String indexHtml = "";
		
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		//m_curOPRID = "TZ_14082";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
		}else{
			siteId = request.getParameter("siteId");
		}
		
		try {
			
			//rootPath;
			String ctxPath = request.getContextPath();
			
			//获取语言和机构;
			String siteSQL = "select TZ_SITE_LANG,TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiInfo = sqlQuery.queryForMap(siteSQL,new Object[]{siteId});
			String strLangID = "";
			String orgId = "";
			if (mapSiteiInfo != null) {
				strLangID = mapSiteiInfo.get("TZ_SITE_LANG") == null ? "ZHS": String.valueOf(mapSiteiInfo.get("TZ_SITE_LANG"));
				orgId = mapSiteiInfo.get("TZ_JG_ID") == null ? ""	: String.valueOf(mapSiteiInfo.get("TZ_JG_ID"));
			}
			
			//得到登录链接的url
			String loginOutUrl = "";

			//得到登录地址;
			loginOutUrl = tzCookie.getStringCookieVal(request,"TZGD_LOGIN_URL");
			
			if(loginOutUrl == null || "".equals(loginOutUrl)){
				loginOutUrl = ctxPath + "/user/login/" + orgId.toLowerCase() + "/" + siteId;
			}
			
			
			//网页标题
			String title = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "SY", strLangID, "首页", "首页");
			
			//需要引入的js和css
			String jsCss = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_JS_CSS",ctxPath);
			//主体部分
			String headImgUrl = sqlQuery.queryForObject("select TZ_MLOGO_PATH from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?", new Object[]{siteId},"String");
			if(headImgUrl != null){
				headImgUrl = ctxPath + headImgUrl;
			}
			
			//个人信息区域;
			String isShowPhoto = "";//是否显示个人照片;
			String photoUrl = "";//个人照片路径;
			Map<String, Object> showPerInfoPhotoMap = sqlQuery.queryForMap("select TZ_IS_SHOW_PHOTO,TZ_MLOGO_PATH from PS_TZ_USERREG_MB_T where TZ_SITEI_ID = ?",new Object[]{siteId});
			if(showPerInfoPhotoMap != null){
				isShowPhoto = showPerInfoPhotoMap.get("TZ_IS_SHOW_PHOTO") == null ? "" : String.valueOf(showPerInfoPhotoMap.get("TZ_IS_SHOW_PHOTO"));
				photoUrl = showPerInfoPhotoMap.get("TZ_MLOGO_PATH") == null ? "" : String.valueOf(showPerInfoPhotoMap.get("TZ_MLOGO_PATH"));
			}
			
			//姓名,邮箱,手机;
			String name = "",email = "", phone="";
			Map<String, Object> personMap = sqlQuery.queryForMap("SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=?",new Object[]{orgId,m_curOPRID});
			if(personMap != null){
				name = personMap.get("TZ_REALNAME") == null ? "" : String.valueOf(personMap.get("TZ_REALNAME"));
				email = personMap.get("TZ_EMAIL") == null ? "" : String.valueOf(personMap.get("TZ_EMAIL"));
				phone = personMap.get("TZ_MOBILE") == null ? "" : String.valueOf(personMap.get("TZ_MOBILE"));
			}
			//需要显示的个人信息;
			//照片;
			String imgSql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserHeadImg");
			Map<String, Object> mapUserHeadImg = sqlQuery.queryForMap(imgSql, new Object[] { m_curOPRID });
			String strPhoto = "";
			if (null != mapUserHeadImg) {
				String strPhotoDir = mapUserHeadImg.get("TZ_ATT_A_URL") == null ? ""
						: String.valueOf(mapUserHeadImg.get("TZ_ATT_A_URL"));
				String strPhotoName = mapUserHeadImg.get("TZ_ATTACHSYSFILENA") == null ? ""
						: String.valueOf(mapUserHeadImg.get("TZ_ATTACHSYSFILENA"));

				if (!"".equals(strPhotoDir) && !"".equals(strPhotoName)) {
					if(strPhotoDir.lastIndexOf("/") == strPhotoDir.length() - 1 ){
						strPhoto = ctxPath + strPhotoDir + strPhotoName;
					}else{
						strPhoto = ctxPath + strPhotoDir + "/" + strPhotoName;
					}
					
				}

			}
			if(strPhoto != null && !"".equals(strPhoto)){
				strPhoto = "<img src=\"" + strPhoto + "\" />";
			}
			
			String personInfoLi = "";
			String personInfoSQL = "select a.TZ_REG_FIELD_ID,a.TZ_REG_FIELD_NAME,a.TZ_FIELD_TYPE,b.TZ_REG_FIELD_NAME as TZ_REG_FIELD_ENG_NAME from (select * from PS_TZ_REG_FIELD_T  where TZ_SITEI_ID=? and TZ_IS_SHOWWZSY='Y') a left join PS_TZ_REGFIELD_ENG b on a.TZ_SITEI_ID=b.TZ_SITEI_ID AND a.TZ_REG_FIELD_ID=b.TZ_REG_FIELD_ID ORDER BY a.TZ_ORDER";
			List<Map<String, Object>> personInfoList = sqlQuery.queryForList(personInfoSQL,new Object[]{siteId});
			if(personInfoList != null && personInfoList.size() > 0){
				for(int i = 0; i < personInfoList.size(); i++){
					//中文名称;
					Map<String, Object> personInfoMap = personInfoList.get(i);
					String fieldId = personInfoMap.get("TZ_REG_FIELD_ID") == null ? "" : String.valueOf(personInfoMap.get("TZ_REG_FIELD_ID"));
					//如果显示个人照片则姓名是直接显示在照片区域内的;
					if("Y".equals(isShowPhoto) && "TZ_REALNAME".equals(fieldId)){
						continue;
					}
					String chnFieldName = personInfoMap.get("TZ_REG_FIELD_NAME") == null ? "" : String.valueOf(personInfoMap.get("TZ_REG_FIELD_NAME"));
					String engFieldName = personInfoMap.get("TZ_REG_FIELD_ENG_NAME") == null ? "" : String.valueOf(personInfoMap.get("TZ_REG_FIELD_ENG_NAME"));
					//是否下拉框;
					String isDrop = personInfoMap.get("TZ_FIELD_TYPE") == null ? "" : String.valueOf(personInfoMap.get("TZ_FIELD_TYPE"));
					String showName = "";
					if("ENG".equals(strLangID)){
						if("".equals(engFieldName)){
							showName = chnFieldName;
						}else{
							showName = engFieldName;
						}
					}else{
						showName = chnFieldName;
					}
					
					
					String fieldValue = "";
					if("TZ_REALNAME".equals(fieldId) || "TZ_EMAIL".equals(fieldId) || "TZ_MOBILE".equals(fieldId)){
						if("TZ_REALNAME".equals(fieldId)){
							fieldValue = name;
						}
						if("TZ_EMAIL".equals(fieldId)){
							fieldValue = email;
						}
						if("TZ_MOBILE".equals(fieldId)){
							fieldValue = phone;
						}
					}else{
						fieldValue = sqlQuery.queryForObject("SELECT " + fieldId + " FROM PS_TZ_REG_USER_T where OPRID=?", new Object[]{m_curOPRID},"String");
						//下拉值;
						if("DROP".equals(isDrop)){
							if(fieldValue != null && !"".equals(fieldValue)){
								if("ENG".equals(strLangID)){
									fieldValue = sqlQuery.queryForObject("select TZ_OPT_VALUE from PS_TZ_YHZC_XXZ_ENG where TZ_SITEI_ID = ? and LANGUAGE_CD = ? and TZ_REG_FIELD_ID = ? and TZ_OPT_ID = ?", new Object[]{siteId,strLangID,fieldId,fieldValue},"String");
									if(fieldValue == null || "".equals(fieldValue)){
										fieldValue = sqlQuery.queryForObject("select TZ_OPT_VALUE from PS_TZ_YHZC_XXZ_TBL where TZ_SITEI_ID=? and TZ_REG_FIELD_ID=? and TZ_OPT_ID=?", new Object[]{siteId,fieldId,fieldValue},"String");
									}
								}else{
									fieldValue = sqlQuery.queryForObject("select TZ_OPT_VALUE from PS_TZ_YHZC_XXZ_TBL where TZ_SITEI_ID=? and TZ_REG_FIELD_ID=? and TZ_OPT_ID=?", new Object[]{siteId,fieldId,fieldValue},"String");
								}
							}
						}
					}
					if(fieldValue == null){
						fieldValue = "";
					}
					fieldValue = StringEscapeUtils.escapeHtml(fieldValue);
					personInfoLi = personInfoLi + tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_PERINFO_LI_HTML",showName,fieldValue);
				}
			}
			
			String personInfoHtml = "";
			if("Y".equals(isShowPhoto)){
				personInfoHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_PERINFO_IMG_HTML",ctxPath + photoUrl,StringEscapeUtils.escapeHtml(name),personInfoLi,strPhoto);
			}else{
				personInfoHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_PERINFO_NO_IMG_HTML",personInfoLi);
			}
			
			//中间区域:报名中心,新闻,活动等;
			String applayCenterHtml = "";
			String middleAreaSql = "SELECT A.TZ_AREA_ID,A.TZ_COLU_ID,B.TZ_PHONE_HTML_CODE FROM PS_TZ_SITEI_AREA_T A ,PS_TZ_SITEI_ATYP_T B WHERE A.TZ_SITEI_ID=? AND A.TZ_SHOW_MOBILE_FLG='Y' AND A.TZ_AREA_POSITION='C' AND A.TZ_SITEI_ID = B.TZ_SITEI_ID AND A.TZ_AREA_TYPE_ID=B.TZ_AREA_TYPE_ID ORDER BY A.TZ_SHOWM_ORDER ASC";
			List<Map<String, Object>> middleAreaList = new ArrayList<>();
			middleAreaList = sqlQuery.queryForList(middleAreaSql,new Object[]{siteId});
			if(middleAreaList != null && middleAreaList.size() > 0){
				for(int areai = 0; areai < middleAreaList.size(); areai++){
					String areaId = middleAreaList.get(areai).get("TZ_AREA_ID") == null ? "" : String.valueOf(middleAreaList.get(areai).get("TZ_AREA_ID"));
					String columId = middleAreaList.get(areai).get("TZ_COLU_ID") == null ? "" : String.valueOf(middleAreaList.get(areai).get("TZ_COLU_ID"));
					String strAppClass = middleAreaList.get(areai).get("TZ_PHONE_HTML_CODE") == null ? "" : String.valueOf(middleAreaList.get(areai).get("TZ_PHONE_HTML_CODE"));
					if(!"".equals(strAppClass)){
						MobleMiddleAreaInterface obj = (MobleMiddleAreaInterface) ctx.getBean(strAppClass);
						applayCenterHtml = applayCenterHtml + obj.tzGetHtmlContent(siteId, areaId,columId);
					}
				}
			}
			
			//关于学习;
			String gySchoolHtml = sqlQuery.queryForObject("select TZ_MPHN_DESCR from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?", new Object[]{siteId},"String");
			if(gySchoolHtml == null){
				gySchoolHtml = "";
			}
			String content = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_CONTENT_HTML",headImgUrl,personInfoHtml,applayCenterHtml,gySchoolHtml);
			//其他;
			String otherHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_OTHER_HTML",ctxPath);
			indexHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",title,ctxPath,siteId,orgId,"index",jsCss,"",content,otherHtml);
			indexHtml = objRep.repPhoneCss(indexHtml, siteId);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			indexHtml = "";
			e.printStackTrace();
		}

		return indexHtml;
	}

}
