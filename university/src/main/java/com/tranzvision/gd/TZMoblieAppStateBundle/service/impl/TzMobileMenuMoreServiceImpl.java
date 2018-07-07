package com.tranzvision.gd.TZMoblieAppStateBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 清华mba招生手机版我的 classid: mMore
 * 
 * @author hjl
 *
 */

@Service("com.tranzvision.gd.TZMoblieAppStateBundle.service.impl.TzMobileMenuMoreServiceImpl")
public class TzMobileMenuMoreServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private SiteRepCssServiceImpl objRep;

	/* 手机版招生网站首页 */
	@Override
	public String tzGetHtmlContent(String strParams) {

		String indexHtml = "";
		String title = "更多";
		
		int m=0;
		String menuId="";
		String menuName="";
		String menuIconUrl="/statics/css/website/m/images/my_hd.png";
		String menuConetHtml="";
		String menuHtml="";
		String menuhtml1="";
		String menuhtml2="";
		String menuNewWin="";

		String ctxPath = request.getContextPath();
		
		String OrgID=tzLoginServiceImpl.getLoginedManagerOrgid(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		if (jacksonUtil.containsKey("siteId")) {
			siteId = jacksonUtil.getString("siteId");
		} else {
			siteId = request.getParameter("siteId");
		}
		
		

		try {
	/*		// 首页
			String indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + siteId;
			// 已报名活动;
			String myActivityYetUrl = ctxPath + "/dispatcher?classid=myActivity&siteId=" + siteId + "&lx=back";
			// 系统站内信;
			String znxListUrl = ctxPath + "/dispatcher?classid=znxList&siteId=" + siteId + "&lx=back";
			// 查看历史报名
			String lsbmUrl = ctxPath + "/dispatcher?classid=mAppHistory&siteId=" + siteId;
			// 申请奖学金;
			String sqJxjUrlb = ctxPath + "/dispatcher?classid=schlrView&siteId=" + siteId + "&oprate=R";
			// 账户管理;
			String accountMngUrl = ctxPath + "/dispatcher?classid=phZhgl&siteId=" + siteId;*/
			String sql="SELECT COUNT(*) FROM  PS_TZ_SITEI_MENU_T WHERE  TZ_SITEI_ID=?  AND TZ_SHOW_MOBILE_FLG<>'N'";
			int count =sqlQuery.queryForObject(sql, new Object[]{siteId}, "Integer");
			if (count>3) {
			
			List<Map<String, Object>> list = null;
			list=sqlQuery.queryForList(tzGDObject.getSQLText("SQL.TZMobileWebsiteIndexBundle.TZ_M_MORE_MENU_LIMIT"), new Object[]{siteId,count});

			
			
			for (Map<String, Object> map : list) {
				//菜单ID
				menuId=map.get("TZ_MENU_ID")==null?"":map.get("TZ_MENU_ID").toString();
				//菜单名称
				menuName=map.get("TZ_MENU_NAME")==null?"":map.get("TZ_MENU_NAME").toString();
				//菜单图标
				menuIconUrl=map.get("TZ_MTYPE_IMG")==null?"":map.get("TZ_MTYPE_IMG").toString();
				//是否新开窗口
				menuNewWin=map.get("TZ_MOBILE_NEWFLG")==null?"":map.get("TZ_MOBILE_NEWFLG").toString();
				String actionDkfs = "";
				if(menuNewWin != null && "A".equals(menuNewWin)){
					actionDkfs = "_blank";
				}
				if (m<=2) {

					menuhtml1+=tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_MORE_MENU_I_HTML",siteId,menuId,ctxPath,menuIconUrl,menuName,actionDkfs);
				}else{
					
					menuhtml2+=tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_MORE_MENU_I_HTML",siteId,menuId,ctxPath,menuIconUrl,menuName,actionDkfs);
					
				}
				m++;
			  }
			}
			menuHtml="<div class='mine'>"+menuhtml1+"</div>"+"<div class='mine' style='margin-top: 2%;'>"+menuhtml2+"</div>";
			
			
			menuConetHtml=tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_MORE_MENU_HTML",title,menuHtml);
		
			String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_JS_CSS",ctxPath);
			
			
			indexHtml =tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",title,ctxPath,siteId,OrgID,"more",jsCss,"",menuConetHtml,"" );
			
			indexHtml=objRep.repPhoneCss(indexHtml, siteId);
		} catch (Exception e) {
			e.printStackTrace();
			indexHtml = "";
		}

		return indexHtml;
	}

}
