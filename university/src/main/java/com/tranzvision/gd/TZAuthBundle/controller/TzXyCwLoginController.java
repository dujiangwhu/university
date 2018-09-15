package com.tranzvision.gd.TZAuthBundle.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.httpclient.HttpClientService;
import com.tranzvision.gd.util.security.AES;
import com.tranzvision.gd.util.security.MD5;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Controller
@RequestMapping("/")
public class TzXyCwLoginController {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	
	private final String OpenClassroomURL="http://global.talk-cloud.net/WebAPI/roomcreate";
	
	
	private final String IntoClassroomURL="https://global.talk-cloud.net/WebAPI/entry";
	
	private final String key= "VTMffDPrVDKvUthM";
	
	private final String requestType = "GET";
	
	private final String charset = "UTF-8";
	
	private final String  domain ="njxywh";
	
	@RequestMapping(value = "/stuLogin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String stuLogin(String strUsername, String strPassword,String strType) {
		
		//DEMO 假登录
		System.out.println("strUsername:"+strUsername);
		System.out.println("strPassword:"+strPassword);
		System.out.println("strType:"+strType);
		if (strUsername == null || strPassword == null || strUsername.equals("") || 
				strPassword.equals("")|| strType.equals("") || strType.equals("")) {
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
		
		
		// LYB 登录逻辑
		/*boolean flag = false;
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (strUsername == null || strPassword == null || strUsername.equals("") || strPassword.equals("")) {
			String reHtml = "";
			String proPath = "";
			proPath = request.getContextPath();
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZWorkManageBundle.TZ_STU_LOGIN", true, proPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reHtml;
		} else {
			String opridSql;
			String errorMessage = "";
			opridSql = "SELECT A.OPRID FROM PS_TZ_AQ_YHXX_TBL A LEFT JOIN PS_TZ_STU_EXTD_TBL B ON A.OPRID=B.OPRID WHERE   ( A.TZ_DLZH_ID=? OR B.TZ_STU_XH=?)";
			String oprid = sqlQuery.queryForObject(opridSql, new Object[] { strUsername, strUsername }, "String");
			if (oprid == null || oprid.equals("")) {
				errorMessage = "账号或密码错误";
				flag = false;
			} else {
				String getRealPasswordSql = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=?";
				String realPassword = sqlQuery.queryForObject(getRealPasswordSql, new Object[] { oprid }, "String");
				strPassword = DESUtil.encrypt(strPassword, "TZGD_Tranzvision");
				if (realPassword.equals(strPassword)) {
					flag = true;
				} else {
					errorMessage = "账号或密码错误";
					flag = false;
				}
			}

			if (flag) {
				String sql = "select ACCTLOCK from TZ_ZYGL_STUACC_VW where TZ_DLZH_ID=? or TZ_STU_XH=?";
				int acctlock = sqlQuery.queryForObject(sql, new Object[] { strUsername, strUsername }, "Integer");
				if (acctlock == 1) {
					errorMessage = "该账号已被锁定";
					flag = false;
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			if (flag) {
				request.getSession().setAttribute("currentLoginUser", strUsername);
			}
			map.put("strFlag", flag);
			map.put("strErrorMessage", errorMessage);
			return jacksonUtil.Map2json(map);
		}
		*/
	}

	@RequestMapping(value = "/stuLogout", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String stuLogout() {
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		request.getSession().removeAttribute("currentLoginUser");
		try {
			reHtml = tzGdObject.getHTMLText("HTML.TZWorkManageBundle.TZ_STU_LOGIN", true, proPath);
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return reHtml;
	}
	
	

	@RequestMapping(value = "/coureWorkHtml", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String coureWorkHtml(String strUsername) {

		if (strUsername == null || strUsername.equals("")) {
			String reHtml = "";
			String proPath = "";
			proPath = request.getContextPath();
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZWorkManageBundle.TZ_STU_LOGIN", true, proPath);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return reHtml;
		}
		if (request.getSession().getAttribute("currentLoginUser") != null) {
			String loginCheck = (String) request.getSession().getAttribute("currentLoginUser");
			if (loginCheck.equals(strUsername)) {
				String getOpridSql, OPRID = "";

				getOpridSql = "SELECT A.OPRID FROM PS_TZ_AQ_YHXX_TBL A LEFT JOIN PS_TZ_STU_EXTD_TBL B ON A.OPRID=B.OPRID WHERE  A.TZ_DLZH_ID=? OR B.TZ_STU_XH=?";
				OPRID = sqlQuery.queryForObject(getOpridSql, new Object[] { strUsername, strUsername }, "String");
	

				String TZ_REALNAME = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?",
						new Object[] { OPRID }, "String");
				String proPath = "";
				proPath = request.getContextPath();
				String reHtml = "";
				try {
					reHtml = tzGdObject.getHTMLText("HTML.TZWorkManageBundle.TZ_STU_COURSE_WORK_MANAGE", true, proPath,
							TZ_REALNAME, OPRID);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return reHtml;
			} else {
				String reHtml = "";
				String proPath = "";
				proPath = request.getContextPath();
				try {
					reHtml = tzGdObject.getHTMLText("HTML.TZWorkManageBundle.TZ_STU_LOGIN", true, proPath);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return reHtml;
			}
		} else {
			String reHtml = "";
			String proPath = "";
			proPath = request.getContextPath();
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZWorkManageBundle.TZ_STU_LOGIN", true, proPath);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return reHtml;
		}
	}

	/**
	 * 学员登录页面
	 */
	@RequestMapping(value = "/stuLoginHtml", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String stuLoginHtml() {
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		//System.out.println("地址" + proPath);
		try {
			reHtml = tzGdObject.getHTMLText("HTML.TZStuCenterBundle.TZ_STU_LOGIN",true, proPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reHtml;
	}

	/**
	 * 教师登录页面
	 */
	@RequestMapping(value = "/teaLoginHtml", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String teaLoginHtml() {
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		//System.out.println("地址" + proPath);
		try {
			reHtml = tzGdObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_TEA_LOGIN",true, proPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reHtml;
	}
	
	/**
	 * 教师页面DEMO
	 */
	@RequestMapping(value = "/coureTeaHtml", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String coureStuHtml() {
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		if (request.getSession().getAttribute("currentLoginTEA") != null) {
			String name = (String) request.getSession().getAttribute("currentLoginTEA");
			System.out.println("name:"+name);
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_TEA_INDEX",true, proPath,name);
			} catch (Exception e) {
			e.printStackTrace();
			}
		} else {
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_TEA_LOGIN",true, proPath);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return reHtml;
		}
			
		return reHtml;
	}
	
	
	/**
	 * 学生页面DEMO
	 */
	@RequestMapping(value = "/coureStuHtml", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String coureTeaHtml() {
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		if (request.getSession().getAttribute("currentLoginSTU") != null) {
			String name = (String) request.getSession().getAttribute("currentLoginSTU");
			System.out.println("name:"+name);
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZStuCenterBundle.TZ_STU_INDEX",true, proPath,name);
			} catch (Exception e) {
			e.printStackTrace();
			}
		} else {
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZStuCenterBundle.TZ_STU_LOGIN",true, proPath);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return reHtml;
		}
			
		return reHtml;
	}
	
	public  int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
        } else {
            return 0;
        }
    }
	
	/**
	 * 教师创建教师，并且进入
	 */
	@RequestMapping(value = "/teaToClass", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String teaToClass(String strType) {
		System.out.println("strType:"+strType);
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		if (request.getSession().getAttribute("currentLoginTEA") != null) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			JacksonUtil jacksonUtil = new JacksonUtil();
			String name = (String) request.getSession().getAttribute("currentLoginTEA");
			System.out.println("name:"+name);
			//预定房间
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			int srat =  getSecondTimestamp(new Date());
			int end = srat+45*60;
			paramsMap.put("key", key);
			paramsMap.put("roomname", name+"的教室");
			paramsMap.put("roomtype", 3);
			paramsMap.put("starttime",srat);
			paramsMap.put("endtime", end);
			paramsMap.put("chairmanpwd", "12345");
			paramsMap.put("assistantpwd", "abcdef");
			paramsMap.put("patrolpwd", "54321");
			paramsMap.put("passwordrequired", 1);
			paramsMap.put("confuserpwd", "135790");
			//paramsMap.put("videotype", 1);
			//paramsMap.put("videoframerate", 20);
			//paramsMap.put("autoopenav", 0);
			String content="";
			try {
				HttpClientService hcs = new  HttpClientService(OpenClassroomURL,requestType,paramsMap,charset);
				
				content=hcs.sendRequest();
				int responseCode = hcs.getResponseCode();
				System.out.println("content:"+content);
				System.out.println("responseCode:"+responseCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			//此方法若调用成功则会返回 json 数据： {"result":0,"serial":"房间号"}
			//可根据 result 是否是 0 来判断是否成功 0：表示成功 -1 表示失败
			//若调用失败,会返回错误码, 请查看相应错误码表。
			if (content == null || content.equals("")) {
				map.put("strFlag", false);
				map.put("strErrorMessage", "创建房间失败");
				return jacksonUtil.Map2json(map);
			} else {
				jacksonUtil.json2Map(content);
				String result=jacksonUtil.getString("result");
				String serial = jacksonUtil.getString("serial");
				
				if (result !=null && result.equals("0")) {
					//房间号，存入数据库
					System.out.println("创建房间号插入数据库");
					String sql ="insert into CAOY_TEMP (serial,createTime) values (?,now())";
					sqlQuery.update(sql, new Object[] { serial});
					MD5 md5 = new MD5();
					//必填，auth 值为 MD5(key + ts +serial + usertype)
					//其中 key 为双方协商的接口密钥：默
					//认值为：5NIWjlgmvqwbt494
					int ts = getSecondTimestamp(new Date());
					String sstr=key+String.valueOf(ts)+serial+"0";
					
					System.out.println("sstr:"+sstr);
					String auth=md5.getMD5(sstr.getBytes());
					System.out.println("auth"+auth);
					//进去房间
					paramsMap = new HashMap<String, Object>();
					paramsMap.put("domain", domain);
					paramsMap.put("serial", serial);
					paramsMap.put("username", name);
					paramsMap.put("usertype",0);
					paramsMap.put("ts", ts);
					paramsMap.put("auth", auth);
					paramsMap.put("userpassword", "12345");
					System.out.println("auth"+auth);
				

					try {
						name = URLEncoder.encode(name, "utf-8");
						auth = URLEncoder.encode(auth, "utf-8");
						System.out.println("name:"+name);
						System.out.println("auth:"+auth);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String ifurl =IntoClassroomURL+"?domain="+domain+"&serial="+serial+"&username="
							+name+"&usertype=0&ts="+String.valueOf(ts)+"&auth="+auth+"&userpassword="+AES.encrypt("12345", key);
					
					System.out.println("ifurl:"+ifurl);
					map.put("strFlag", true);
					map.put("SKurl", ifurl);
					return jacksonUtil.Map2json(map);
					
				} else {
					map.put("strFlag", false);
					map.put("strErrorMessage", "创建房间失败,失败代码："+result);
					return jacksonUtil.Map2json(map);
				}
			}
			
			
		} else {
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_TEA_LOGIN",true, proPath);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return reHtml;
		}
	}
	
	
	/**
	 * 学员并且进入
	 */
	@RequestMapping(value = "/stuToClass", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String stuToClass(String strType) {
		System.out.println("strType:"+strType);
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		if (request.getSession().getAttribute("currentLoginSTU") != null) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			JacksonUtil jacksonUtil = new JacksonUtil();
			String name = (String) request.getSession().getAttribute("currentLoginSTU");
			System.out.println("name:"+name);

					//房间号，存入数据库
			//String sql ="insert into CAOY_TEMP (serial,createTime) values (?,now())";
			
			String sql = "select serial from CAOY_TEMP order by createTime desc limit 1";
			
			String serial = sqlQuery.queryForObject(sql, "String");
				
			MD5 md5 = new MD5();

			int ts = getSecondTimestamp(new Date());
			String sstr=key+String.valueOf(ts)+serial+"2";
			String auth=md5.getMD5(sstr.getBytes());

			System.out.println("auth"+auth);
				

			try {
				name = URLEncoder.encode(name, "utf-8");
				auth = URLEncoder.encode(auth, "utf-8");
				System.out.println("name:"+name);
				System.out.println("auth:"+auth);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
					
			String ifurl =IntoClassroomURL+"?domain="+domain+"&serial="+serial+"&username="
							+name+"&usertype=2&ts="+String.valueOf(ts)+"&auth="+auth+"&userpassword="+AES.encrypt("135790", key);
					
			System.out.println("ifurl:"+ifurl);
			map.put("strFlag", true);
			map.put("SKurl", ifurl);
			return jacksonUtil.Map2json(map);
								
		} else {
			try {
				reHtml = tzGdObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_TEA_LOGIN",true, proPath);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return reHtml;
		}
	}
	

	@RequestMapping(value = "/forgetPassword", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String forgetPassword() {
		String reHtml = "";
		String proPath = "";
		proPath = request.getContextPath();
		try {
			reHtml = tzGdObject.getHTMLText("HTML.TZWorkManageBundle.TZ_FORGET_PASSWORD", true, proPath);
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return reHtml;
	}

	@RequestMapping(value = "/sendEmail", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String sendEmail(String tokensign) {
		String reHtml = "";
		String proPath = "";
		String selectDlzh = "SELECT TZ_DLZH_ID FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_TOKEN_CODE=?";
		String dlzh = sqlQuery.queryForObject(selectDlzh, new Object[] { tokensign }, "String");
		proPath = request.getContextPath();
		try {
			reHtml = tzGdObject.getHTMLText("HTML.TZWorkManageBundle.TZ_RESET_PASSWORD", true, proPath, dlzh);
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return reHtml;
	}

	@RequestMapping(value = "/resetPassword", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String resetPassword(String oprID, String newPassWord, String checkPassWord) {
		// 更新密码sql
		/*String flag = "";
		String errorMessage = "";
		Map<String, Object> map = new HashMap<String, Object>();

		// 检查密码强度
		String[] result = passwordServiceImpl.checkPwdValid(newPassWord);
		if (!"0".equals(result[0])) {
			flag = "false";
			errorMessage = result[1];
		} else {
			newPassWord = DESUtil.encrypt(newPassWord, "TZGD_Tranzvision");
			String uppwdSql = "UPDATE PSOPRDEFN SET OPERPSWD=?,ACCTLOCK=?,LASTUPDDTTM=?,LASTUPDOPRID=? WHERE OPRID=?";
			Integer isSuccess = sqlQuery.update(uppwdSql, new Object[] { newPassWord, "0", new Date(), oprID, oprID });

			if (isSuccess > 0) {
				flag = "true";
			} else {
				flag = "false";
				errorMessage = "重置密码失败";
			}
		}

		map.put("result", flag);
		map.put("msg", errorMessage);
		String StrMap = JSONUtils.toJSONString(map);
		return StrMap; */
		return null;
	}

	@RequestMapping(value = "/xgPassword", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String xgPassword(String oprID, String OldPassword, String NewPassword) {
		// 更新密码sql
		/*String flag = "";
		String errorMessage = "";
		String selectPwdSql = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=?";
		String selectPwd = sqlQuery.queryForObject(selectPwdSql, new Object[] { oprID }, "String");
		OldPassword = DESUtil.encrypt(OldPassword, "TZGD_Tranzvision");
		Map<String, Object> map = new HashMap<String, Object>();
		if (OldPassword.equals(selectPwd)) {
			// 检查密码强度
			String[] result = passwordServiceImpl.checkPwdValid(NewPassword);
			if (!"0".equals(result[0])) {
				flag = "false";
				errorMessage = result[1];
			} else {
				NewPassword = DESUtil.encrypt(NewPassword, "TZGD_Tranzvision");
				String uppwdSql = "UPDATE PSOPRDEFN SET OPERPSWD=?,ACCTLOCK=?,LASTUPDDTTM=?,LASTUPDOPRID=? WHERE OPRID=?";
				Integer isSuccess = sqlQuery.update(uppwdSql,
						new Object[] { NewPassword, "0", new Date(), oprID, oprID });
				if (isSuccess > 0) {
					flag = "true";
				} else {
					flag = "false";
					errorMessage = "重置密码失败";
				}
			}
		} else {
			errorMessage = "原密码输入错误";
			flag = "false";
			map.put("result", flag);
			map.put("msg", errorMessage);
		}
		map.put("result", flag);
		map.put("msg", errorMessage);
		String StrMap = JSONUtils.toJSONString(map);
		return StrMap;
		*/
		return null;
	}

}
