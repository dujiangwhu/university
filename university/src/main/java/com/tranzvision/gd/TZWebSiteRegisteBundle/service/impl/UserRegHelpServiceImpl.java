package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 帮助中心
 * @author yuds
 *
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.UserRegHelpServiceImpl")
public class UserRegHelpServiceImpl extends FrameworkImpl{
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SiteRepCssServiceImpl objRep;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String strRet = "";
		
		try {
			
			String strSiteId = request.getParameter("siteId");
			String strAreaId = request.getParameter("areaId");
			String strAreaType =  request.getParameter("areaType");
			
			if (null == strAreaId || "".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}
			
			String strResultContent = "";
			String sql = "select TZ_AREA_SAVECODE from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
			strResultContent = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
			strResultContent = objRep.repContextPath(strResultContent);
			strResultContent = objRep.repPhoneCss(strResultContent, strSiteId);
			
			return strResultContent;
			
		}catch(Exception e){
			e.printStackTrace();
			strRet = "false";
		}
		return strRet;
	}
}
