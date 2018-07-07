package com.tranzvision.gd.TZSingleLoginBundle.service.impl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * OA系统单点登录进入后台管理系统
 * 卢艳 
 * 2017-5-10
 */

@Service("com.tranzvision.gd.TZSingleLoginBundle.service.impl.TzSingleLoginServiceImpl")

public class TzSingleLoginServiceImpl extends FrameworkImpl {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private TzCookie tzCookie;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	

	@SuppressWarnings("unchecked")
	@Override
	public String tzGetHtmlContent(String comParams) {
		String strRet = "";
		
		try {
			
			//rootPath;
			String ctxPath = request.getContextPath();
			String transferUrl = "";
			boolean boolLogin = false;
			
			//机构id
			String orgId = request.getParameter("orgId");
			
			String sqlText = "";
			
			//从cookie中取加密的用户名称
			String tmpUserName = tzCookie.getStringCookieVal(request, "SignOnToken");
				
			if(!"".equals(tmpUserName) && tmpUserName!=null) {
				String userName = DESUtil.decrypt(tmpUserName, "SEMSSOKEY");
				
				//查询用户的机构
				sqlText = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
				int numExist = sqlQuery.queryForObject(sqlText, new Object[]{userName,orgId},"Integer");
				
				if(numExist>0) {
					sqlText = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=(SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?)";
					String tmpPassword = sqlQuery.queryForObject(sqlText, new Object[]{userName,orgId} ,"String");
					String password = "";
					if(!"".equals(tmpPassword)) {
						password = DESUtil.decrypt(tmpPassword, "TZGD_Tranzvision");
					}
					
					if(!"".equals(password)) {
						//验证码
						Patchca patchca = new Patchca();
						String tmpTokenValue = patchca.getToken(request);
						if(tmpTokenValue == null)
						{
							tmpTokenValue = "AbCd";
							TzSession tmpSession = new TzSession(request);
							tmpSession.addSession(patchca.getTokenName(),tmpTokenValue);
						}
						
						ArrayList<String> aryErrorMsg = new ArrayList<String>();
						
						boolean boolAutoLogin = tzLoginServiceImpl.doLogin(request, response, orgId, userName, password, tmpTokenValue, aryErrorMsg);

						if(boolAutoLogin) {
							//登录成功
							transferUrl = ctxPath + "/index";	
						} else {
							//登录失败
							boolLogin = true;
						}
						
					} else {
						boolLogin = true;
					}
				} else {
					boolLogin = true;
				} 
			} else {
				boolLogin = true;
			}
			
			if(boolLogin) {
				transferUrl =  ctxPath + "/login/" + orgId.toLowerCase();	
			}
			
			try
			{
				response.sendRedirect(transferUrl);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strRet;
	}
	
}
