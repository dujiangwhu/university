package com.tranzvision.gd.TZAuthBundle.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 学生门户和教师门户登录
 * 
 * @author CAOY
 *
 */
@Controller
@RequestMapping("/")
public class TzXyCwWebLoginController {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	/**
	 * 学员登录页面
	 */
	@RequestMapping(value = "/stuWebLoginHtml", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String stuLoginHtml() {
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		try {
			reHtml = tzGdObject.getHTMLText("HTML.TZStuCenterBundle.TZ_STU_LOGIN", true, proPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reHtml;
	}

	/**
	 * 教师登录页面
	 */
	@RequestMapping(value = "/teaWebLoginHtml", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String teaLoginHtml() {
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		try {
			reHtml = tzGdObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_TEA_LOGIN", true, proPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reHtml;
	}

	@RequestMapping(value = "/webLogin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String webLogin(String strUsername, String strPassword, String strType) {
		System.out.println("strUsername:" + strUsername);
		System.out.println("strPassword:" + strPassword);
		System.out.println("strType:" + strType);
		if (strUsername == null || strPassword == null || strUsername.equals("") || strPassword.equals("")
				|| strType.equals("") || strType.equals("")) {
			String reHtml = "";
			String proPath = "";
			proPath = request.getContextPath();
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZStuCenterBundle.TZ_STU_LOGIN", true, proPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reHtml;
		} else {
			if (strType.equals("STU")) {
				request.getSession().setAttribute("currentLoginSTU", strUsername);
			} else if (strType.equals("TEA")) {
				request.getSession().setAttribute("currentLoginTEA", strUsername);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("strFlag", true);
			map.put("strErrorMessage", "");
			JacksonUtil jacksonUtil = new JacksonUtil();
			return jacksonUtil.Map2json(map);
		}
	}
}
