package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 加载手机招生网站底部公用菜单
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MoblieWebsiteMenuServiceImpl")
public class MoblieWebsiteMenuServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;

	@Override
	// 加载手机招生网站底部公用菜单
	public String tzGetHtmlContent(String strParams) {
		// rootPath;
		String ctxPath = request.getContextPath();
		String menuHtml = "";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		String menuId = "";
		if (jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("menuId")) {
			siteId = jacksonUtil.getString("siteId");
			menuId = jacksonUtil.getString("menuId");
		} else {
			siteId = request.getParameter("siteId");
			menuId = request.getParameter("menuId");
		}
		if (menuId == null) {
			menuId = "";
		}
		try {
			//查看使用的皮肤;
			String skinSql = "SELECT TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?";
			String skinId = sqlQuery.queryForObject(skinSql, new Object[] { siteId },"String");
			if(skinId == null){
				skinId = "";
			}
			//手机端要显示的菜单
			String totalMenuSql = "select count(1) from PS_TZ_SITEI_MENU_T WHERE TZ_SITEI_ID=? AND TZ_SHOW_MOBILE_FLG='Y'";
			int count = sqlQuery.queryForObject(totalMenuSql, new Object[] { siteId },"int" );
			String menuSql = "select TZ_MENU_ID,TZ_MENU_NAME,TZ_MOBILE_NEWFLG from PS_TZ_SITEI_MENU_T WHERE TZ_SITEI_ID=? AND TZ_SHOW_MOBILE_FLG='Y' ORDER BY TZ_SHOWM_ORDER limit 0,3";
			List<Map<String, Object>> menuList = sqlQuery.queryForList(menuSql, new Object[] { siteId });
			String mehuLiHtml = "";
			if (menuList != null && menuList.size() > 0) {
				for (int i = 0; i < menuList.size(); i++) {
					String mId = menuList.get(i).get("TZ_MENU_ID") == null ? ""
							: String.valueOf(menuList.get(i).get("TZ_MENU_ID"));
					String mName = menuList.get(i).get("TZ_MENU_NAME") == null ? ""
							: String.valueOf(menuList.get(i).get("TZ_MENU_NAME"));
					
					String strOpenType = menuList.get(i).get("TZ_MOBILE_NEWFLG") == null ? ""
							: String.valueOf(menuList.get(i).get("TZ_MOBILE_NEWFLG"));
					// 得到菜单图标;
					Map<String, Object> menuImgMap = new HashMap<>();

					menuImgMap = sqlQuery.queryForMap("select TZ_MTYPE_IMG,TZ_MNOW_IMG from PS_TZ_SITEI_MNPF_T where TZ_SITEI_ID=? and TZ_MENU_ID =? and TZ_SKIN_ID=?", new Object[]{siteId,mId,skinId});
					String defalutMenuImageUrl = "";
					String selectMenuImageUrl = "";
					if(menuImgMap != null){
						defalutMenuImageUrl = menuImgMap.get("TZ_MTYPE_IMG") == null ? "" : String.valueOf(menuImgMap.get("TZ_MTYPE_IMG"));
						selectMenuImageUrl = menuImgMap.get("TZ_MNOW_IMG") == null ? "" : String.valueOf(menuImgMap.get("TZ_MNOW_IMG"));
						if(ctxPath.length() > 1 && defalutMenuImageUrl.indexOf(ctxPath) < 0){
							defalutMenuImageUrl = ctxPath + defalutMenuImageUrl;
						}
						if(ctxPath.length() > 1 && selectMenuImageUrl.indexOf(ctxPath) < 0){
							selectMenuImageUrl = ctxPath + selectMenuImageUrl;
						}
					}
					
					//首页,新开窗口等动作;
					String menuAction = "";
					if(i == 0){
						menuAction = "index";
					}else{
						if(strOpenType != null && "A".equals(strOpenType)){
							menuAction = "_blank";
						}
					}
					
					if (mId.equals(menuId) || ("index".equals(menuId) && i==0)) {
						String menuImage = "style=\"background:url("+ selectMenuImageUrl
								+ ") center center; background-size:24px;\"";
						mehuLiHtml = mehuLiHtml + tzGDObject.getHTMLTextForDollar(
								"HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_MENU_LI", "on", mId,siteId,menuAction, menuImage, mName);
					} else {
						String menuImage = "style=\"background:url("+defalutMenuImageUrl
								+ ") center center; background-size:24px;\"";
						mehuLiHtml = mehuLiHtml + tzGDObject.getHTMLTextForDollar(
								"HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_MENU_LI", "", mId,siteId,menuAction, menuImage, mName);
					}

				}
			}
			if(count > 3){
				String isSelect = "";
				String img = "";
				if("more".equals(menuId)){
					isSelect = "on";
					img = "eg8.png";
				}else{
					img = "eg7.png";
				}
				String moreUrl = ctxPath + "/dispatcher?classid=mMore&siteId=" + siteId;
				String menuImage = "style=\"background:url("+ctxPath+"/statics/css/website/m/images/" + img
						+ ") center center; background-size:24px;\"";
				mehuLiHtml = mehuLiHtml + tzGDObject.getHTMLTextForDollar(
						"HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_MENU_LI_MORE", isSelect, moreUrl, menuImage, "更多");
			}

			menuHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_MENU_HTML",
					mehuLiHtml);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * //首页地址; String indexUrl = ctxPath +
		 * "/dispatcher?classid=mIndex&siteId="+siteId; //招生日历 String columnId =
		 * sqlQuery.queryForObject(
		 * "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?"
		 * ,new Object[]{"TZ_M_WEB_ZSRL"},"String"); String rlUrl = ctxPath +
		 * "/dispatcher?classid=mZsrl&siteId="+siteId+"&columnId="+columnId;
		 * //状态查询; String statusUrl = ctxPath +
		 * "/dispatcher?classid=mAppstatus&siteId="+siteId; //联系我们; String lxUrl
		 * = sqlQuery.queryForObject(
		 * "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?"
		 * , new Object[]{"TZ_M_LXWM_URL"},"String"); //我的 String myUrl =
		 * ctxPath + "/dispatcher?classid=mMy&siteId="+siteId;
		 * 
		 * String[] url = {indexUrl,rlUrl,statusUrl,lxUrl,myUrl};
		 * //url[menuNum-1] = "javascript:void(0);";
		 * 
		 * String menuHtml = ""; try { menuHtml =
		 * tzGDObject.getHTMLTextForDollar(
		 * "HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_MENU_HTML",classOn[0]
		 * ,classOn[1],classOn[2],classOn[3],classOn[4],url[0],url[1],url[2],url
		 * [3],url[4]); } catch (TzSystemException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		return menuHtml;
	}
}
