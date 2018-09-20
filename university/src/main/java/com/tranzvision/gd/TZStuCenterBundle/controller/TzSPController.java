package com.tranzvision.gd.TZStuCenterBundle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZPXBundle.dao.PkTeaIntegralChangeTMapper;
import com.tranzvision.gd.TZPXBundle.model.PkTeaIntegralChangeT;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.httpclient.HttpClientService;
import com.tranzvision.gd.util.httpclient.HttpUtils;
import com.tranzvision.gd.util.security.AES;
import com.tranzvision.gd.util.security.MD5;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 调用视频接口
 * 
 * @author feifei
 *
 */

@Controller
@RequestMapping(value = { "/viewInface" })
public class TzSPController {

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SqlQuery sqlQuery;

	private final String OpenClassroomURL = "http://global.talk-cloud.net/WebAPI/roomcreate";

	private final String IntoClassroomURL = "https://global.talk-cloud.net/WebAPI/entry";

	private final String upfileURL = "https://global.talk-cloud.net/WebAPI/uploadfile";

	private final String key = "VTMffDPrVDKvUthM";

	private final String requestType = "GET";

	private final String charset = "UTF-8";

	private final String domain = "njxywh";

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PkTeaIntegralChangeTMapper pkTeaIntegralChangeTMapper;

	/**
	 * 学生进去房间
	 * 
	 * @param request
	 * @param response
	 * @param orgid
	 * @return
	 */
	@RequestMapping(value = { "/stu/{pkid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String stuInto(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "pkid") String pkid) {
		pkid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(pkid);
		String strRet = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		// 跳转到登录页面
		if (oprid == null || oprid.equals("")) {
			String redirectUrl = request.getContextPath() + "/user/login/stuLogin";
			try {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;
		}

		TzSession tmpSession = new TzSession(request);
		String LoginType = tmpSession.getSession("LoginType") == null ? ""
				: tmpSession.getSession("LoginType").toString();
		// 不是学生登录，跳转到登录页面
		if (!LoginType.equals("STU")) {
			String redirectUrl = request.getContextPath() + "/user/login/stuLogin";
			try {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;
		}

		String sql = "select 'Y' from PX_STU_APP_COURSE_T where OPRID=? and TZ_SCHEDULE_ID=? and TZ_APP_STATUS=?";
		String isY = sqlQuery.queryForObject(sql, new Object[] { oprid,pkid, "0" }, "String");
		if (isY != null && isY.equals("Y")) {
			sql = "select date_format(TZ_CLASS_START_TIME,'%Y-%m-%d %H:%i:%S') AS TZ_CLASS_START_TIME,date_format(TZ_CLASS_END_TIME,'%Y-%m-%d %H:%i:%S') AS TZ_CLASS_END_TIME,TZ_ROOM_ID,TZ_ROOM_KEY from PX_TEA_SCHEDULE_T where TZ_SCHEDULE_ID=? and TZ_SCHEDULE_TYPE=?";
			String TZ_CLASS_START_TIME = "";
			String TZ_CLASS_END_TIME = "";
			String TZ_ROOM_ID = "";
			String TZ_ROOM_KEY = "";
			Map<String, Object> siteMap = sqlQuery.queryForMap(sql, new Object[] { pkid, "0" });
			if (siteMap == null) {
				try {
					String js = "alert('这个课程并没有排课,或排课已经取消！');window.close();";
					strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return strRet;
			} else {
				TZ_CLASS_START_TIME = (String) siteMap.get("TZ_CLASS_START_TIME");
				TZ_CLASS_END_TIME = (String) siteMap.get("TZ_CLASS_END_TIME");
				TZ_ROOM_ID = siteMap.get("TZ_ROOM_ID") == null ? "" : siteMap.get("TZ_ROOM_ID").toString();
				TZ_ROOM_KEY = siteMap.get("TZ_ROOM_KEY") == null ? "" : siteMap.get("TZ_ROOM_KEY").toString();

				if (TZ_ROOM_ID.equals("") || TZ_ROOM_KEY.equals("")) {
					try {
						String js = "alert('授课老师并没有开始上课，请稍等！');window.close();";
						strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
					} catch (TzSystemException e) {
						e.printStackTrace();
					}
					return strRet;
				} else {
					Date starDate = DateUtil.parseTimeStamp(TZ_CLASS_START_TIME);
					Date endDate = DateUtil.parseTimeStamp(TZ_CLASS_END_TIME);
					Date now = new Date();
					if (endDate.compareTo(now) <= 0) {
						try {
							String js = "alert('授课老师已经上完课了！');window.close();";
							strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
						} catch (TzSystemException e) {
							e.printStackTrace();
						}
						return strRet;
					} else {
						// 打开
						strRet = stuIntoRoom(oprid, TZ_ROOM_ID, TZ_ROOM_KEY, pkid);
						return strRet;
					}
				}
			}
		} else {
			try {
				String js = "alert('你并没有预约这个课程,或预约已经取消！');window.close();";
				strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;
		}

		// return strRet;
	}

	private String stuIntoRoom(String oprid, String serial, String TZ_ROOM_KEY, String pkid) {
		String sql = "select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?";
		String name = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

		MD5 md5 = new MD5();

		int ts = getSecondTimestamp(new Date());
		String sstr = key + String.valueOf(ts) + serial + "2";
		String auth = md5.getMD5(sstr.getBytes());

		System.out.println("auth" + auth);

		try {
			name = URLEncoder.encode(name, "utf-8");
			auth = URLEncoder.encode(auth, "utf-8");
			System.out.println("name:" + name);
			System.out.println("auth:" + auth);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String ifurl = IntoClassroomURL + "?domain=" + domain + "&serial=" + serial + "&username=" + name
				+ "&usertype=2&ts=" + String.valueOf(ts) + "&auth=" + auth + "&userpassword="
				+ AES.encrypt(TZ_ROOM_KEY, key);
		String strRet = "";
		try {
			System.out.println("ifurl:" + ifurl);
			// 插入一条记录
			sqlQuery.update("insert into PK_STU_SK_LOG values (?,?,now())", new Object[] { oprid, pkid });

			strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, ifurl, "");
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return strRet;

	}

	private int getSecondTimestamp(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime());
		int length = timestamp.length();
		if (length > 3) {
			return Integer.valueOf(timestamp.substring(0, length - 3));
		} else {
			return 0;
		}
	}

	/**
	 * 学生 创建房间，关联房间，并进入
	 * 
	 * @param request
	 * @param response
	 * @param orgid
	 * @return
	 */
	@RequestMapping(value = { "/tea/{pkid}/{xuh}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String teaInto(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "pkid") String pkid, @PathVariable(value = "xuh") String xuh) {
		pkid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(pkid);
		xuh = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(xuh);
		String strRet = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		// 跳转到登录页面
		if (oprid == null || oprid.equals("")) {
			String redirectUrl = request.getContextPath() + "/user/login/teaLogin";
			try {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;
		}

		TzSession tmpSession = new TzSession(request);
		String LoginType = tmpSession.getSession("LoginType") == null ? ""
				: tmpSession.getSession("LoginType").toString();
		// 不是学生登录，跳转到登录页面
		if (!LoginType.equals("TEA")) {
			String redirectUrl = request.getContextPath() + "/user/login/teaLogin";
			try {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;
		}

		String sql = "select TZ_APP_STATUS,date_format(TZ_CLASS_START_TIME,'%Y-%m-%d %H:%i:%S') AS TZ_CLASS_START_TIME,date_format(TZ_CLASS_END_TIME,'%Y-%m-%d %H:%i:%S') AS TZ_CLASS_END_TIME from PX_TEA_SCHEDULE_T where TZ_SCHEDULE_ID=? and TZ_SCHEDULE_TYPE=? and OPRID=?";
		String TZ_CLASS_START_TIME = "";
		String TZ_CLASS_END_TIME = "";
		String TZ_APP_STATUS = "";
		Map<String, Object> siteMap = sqlQuery.queryForMap(sql, new Object[] { pkid, "0", oprid });
		if (siteMap == null) {
			try {
				String js = "alert('这个课程并没有排课,或排课已经取消！');window.close();";
				strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;
		} else {
			TZ_CLASS_START_TIME = (String) siteMap.get("TZ_CLASS_START_TIME");
			TZ_CLASS_END_TIME = (String) siteMap.get("TZ_CLASS_END_TIME");
			TZ_APP_STATUS = siteMap.get("TZ_APP_STATUS") == null ? "" : siteMap.get("TZ_APP_STATUS").toString();

			if (TZ_APP_STATUS.equals("0")) {
				try {
					String js = "alert('本次排课没有学员预约，不需要上课！');window.close();";
					strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return strRet;
			} else {
				Date starDate = DateUtil.parseTimeStamp(TZ_CLASS_START_TIME);
				Date endDate = DateUtil.parseTimeStamp(TZ_CLASS_END_TIME);
				Date now = new Date();
				if (endDate.compareTo(now) <= 0) {
					try {
						String js = "alert('已经上完课了,不能再次上课！');window.close();";
						strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
					} catch (TzSystemException e) {
						e.printStackTrace();
					}
					return strRet;
				} else {

					String limitHour = sqlQuery.queryForObject(
							"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
							new Object[] { "TZ_LIMIT_MINUTE" }, "String");

					long currentTime = System.currentTimeMillis();

					// 加N分钟
					currentTime += Integer.parseInt(limitHour) * 60 * 1000;

					Date date = new Date(currentTime);

					if (date.compareTo(starDate) < 0) {
						try {
							String js = "alert('只能提前" + limitHour + "分钟上课！');window.close();";
							strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
						} catch (TzSystemException e) {
							e.printStackTrace();
						}
						return strRet;
					} else {
						// 打开
						strRet = teaIntoRoom(oprid, endDate, xuh, pkid);
						return strRet;
					}
				}
			}
		}
	}

	private String getRandomString(int length) {
		String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(52);// [0,51)
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	private String teaIntoRoom(String oprid, Date endDate, String xuh, String pkid) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strRet = "";
		String sql = "select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?";
		String name = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
		System.out.println("name:" + name);

		// 检查是否已经创建过房间
		sql = "select TZ_ROOM_ID,TZ_ROOM_KEY from PX_TEA_SCHEDULE_T where TZ_SCHEDULE_ID=? and OPRID=? and TZ_SCHEDULE_TYPE=?";
		String TZ_ROOM_ID = "";
		String TZ_ROOM_KEY = "";
		Map<String, Object> mapp = sqlQuery.queryForMap(sql, new Object[] { pkid, oprid, "0" });
		if (mapp != null) {
			TZ_ROOM_ID = mapp.get("TZ_ROOM_ID") == null ? "" : mapp.get("TZ_ROOM_ID").toString();
			TZ_ROOM_KEY = mapp.get("TZ_ROOM_KEY") == null ? "" : mapp.get("TZ_ROOM_KEY").toString();
		}
		String result = "";
		String serial = "";
		String pwd = "";

		// 是否第一次创建房间
		boolean isFirst = false;
		Map<String, Object> paramsMap = null;
		if (!TZ_ROOM_ID.equals("") && !TZ_ROOM_KEY.equals("")) {
			result = "0";
			serial = TZ_ROOM_ID;
			pwd = TZ_ROOM_KEY;
		} else {
			// 创建房间
			paramsMap = new HashMap<String, Object>();
			int srat = getSecondTimestamp(new Date());
			int end = getSecondTimestamp(endDate);

			pwd = getRandomString(6);
			paramsMap.put("key", key);
			paramsMap.put("roomname", name + "的教室");
			paramsMap.put("roomtype", 3);
			paramsMap.put("starttime", srat);
			paramsMap.put("endtime", end);
			paramsMap.put("chairmanpwd", "12345");
			paramsMap.put("assistantpwd", "abcdef");
			paramsMap.put("patrolpwd", "54321");
			paramsMap.put("passwordrequired", 1);
			paramsMap.put("confuserpwd", pwd);
			String content = "";
			try {
				HttpClientService hcs = new HttpClientService(OpenClassroomURL, requestType, paramsMap, charset);

				content = hcs.sendRequest();
				int responseCode = hcs.getResponseCode();
				System.out.println("content:" + content);
				System.out.println("responseCode:" + responseCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 此方法若调用成功则会返回 json 数据： {"result":0,"serial":"房间号"}
			// 可根据 result 是否是 0 来判断是否成功 0：表示成功 -1 表示失败
			// 若调用失败,会返回错误码, 请查看相应错误码表。
			if (content == null || content.equals("")) {
				try {
					String js = "alert('创建房间失败！');window.close();";
					strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return strRet;
			} else {
				jacksonUtil.json2Map(content);
				result = jacksonUtil.getString("result");
				serial = jacksonUtil.getString("serial");
				isFirst = true;
			}
		}

		if (result != null && result.equals("0")) {
			// 房间号，存入数据库
			System.out.println("创建房间号插入数据库");
			sql = "update PX_TEA_SCHEDULE_T set TZ_ROOM_ID=?,TZ_ROOM_KEY=? where TZ_SCHEDULE_ID=?";
			sqlQuery.update(sql, new Object[] { serial, pwd, pkid });

			// 上传附件
			StringBuffer sqlsb = new StringBuffer();
			sqlsb.append("select A.TZ_ATTACHSYSFILENA,A.TZ_ATT_A_URL ");
			sqlsb.append("FROM PX_COURSE_ANNEX_T A,PX_TEA_SCHEDULE_T B  ");
			sqlsb.append("where A.TZ_COURSE_ID = B.TZ_COURSE_ID ");
			sqlsb.append("and B.TZ_SCHEDULE_ID=? and B.OPRID=? AND A.TZ_PKSK_XH=? ");
			Map<String, Object> siteMap = sqlQuery.queryForMap(sqlsb.toString(), new Object[] { pkid, oprid, xuh });
			
			System.out.println(xuh);
			String TZ_ATTACHSYSFILENA = "";
			String TZ_ATT_A_URL = "";
			if (siteMap != null) {

				TZ_ATTACHSYSFILENA = siteMap.get("TZ_ATTACHSYSFILENA") == null ? ""
						: siteMap.get("TZ_ATTACHSYSFILENA").toString();
				TZ_ATT_A_URL = siteMap.get("TZ_ATT_A_URL") == null ? "" : siteMap.get("TZ_ATT_A_URL").toString();
				
				System.out.println(TZ_ATTACHSYSFILENA);
				System.out.println(TZ_ATT_A_URL);
				if (!TZ_ATT_A_URL.equals("") && !TZ_ATTACHSYSFILENA.equals("")) {
					String syspth = sqlQuery.queryForObject(
							"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
							new Object[] { "TZ_SYS_PATH" }, "String");

					if (TZ_ATT_A_URL.endsWith("/")) {
						TZ_ATT_A_URL = TZ_ATT_A_URL + TZ_ATTACHSYSFILENA;
					} else {
						TZ_ATT_A_URL = TZ_ATT_A_URL + "/" + TZ_ATTACHSYSFILENA;
					}

					if (TZ_ATT_A_URL.startsWith("/")) {
						TZ_ATT_A_URL = syspth + TZ_ATT_A_URL;
					} else {
						TZ_ATT_A_URL = syspth + "/" + TZ_ATT_A_URL;
					}
					System.out.println(TZ_ATT_A_URL);
					System.out.println(serial);
					String ee = uploadfile(TZ_ATT_A_URL, serial);
					//System.out.println(ee);
				}
			}

			MD5 md5 = new MD5();
			int ts = getSecondTimestamp(new Date());
			String sstr = key + String.valueOf(ts) + serial + "0";

			System.out.println("sstr:" + sstr);
			String auth = md5.getMD5(sstr.getBytes());
			System.out.println("auth" + auth);
			// 进去房间
			// 第一次 加积分
			if (isFirst) {

	
				sql = "SELECT SCORE FROM PX_STUDENT_T WHERE OPRID=?";

				int tzBeforeChange = sqlQuery.queryForObject(sql, new Object[] { oprid }, "Integer");

				String score = sqlQuery.queryForObject(
						"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "TZ_TEA_SK" }, "String");

				sqlQuery.update("update PX_TEACHER_T set SCORE=SCORE+" + score + " where OPRID=?",
						new Object[] { oprid });

				int tzAfterChange = tzBeforeChange + Integer.parseInt(score);

				// 增加教师积分变动表
				PkTeaIntegralChangeT pkTeaIntegralChangeT = new PkTeaIntegralChangeT();
				pkTeaIntegralChangeT.setOprid(oprid);
				pkTeaIntegralChangeT.setRowLastmantDttm(new Date());
				pkTeaIntegralChangeT.setRowLastmantOprid(oprid);
				pkTeaIntegralChangeT.setTzAfterChange(new Integer(tzAfterChange));
				pkTeaIntegralChangeT.setTzBeforeChange(new Integer(tzBeforeChange));
				pkTeaIntegralChangeT.setTzChange(new Integer(score));
				// 1：上课产生积分 2：提现消耗积分 3：管理员修改 4:注册产生积分
				pkTeaIntegralChangeT
						.setTzChangeId("" + getSeqNum.getSeqNum("PK_TES_INTEGRAL_CHANGE_T", "TZ_CHANGE_ID"));
				pkTeaIntegralChangeT.setTzChangeType("1");
				pkTeaIntegralChangeT.setTzScheduleId(pkid);
				pkTeaIntegralChangeTMapper.insertSelective(pkTeaIntegralChangeT);
			}
			paramsMap = new HashMap<String, Object>();
			paramsMap.put("domain", domain);
			paramsMap.put("serial", serial);
			paramsMap.put("username", name);
			paramsMap.put("usertype", 0);
			paramsMap.put("ts", ts);
			paramsMap.put("auth", auth);
			paramsMap.put("userpassword", "12345");
			System.out.println("auth" + auth);

			try {
				name = URLEncoder.encode(name, "utf-8");
				auth = URLEncoder.encode(auth, "utf-8");
				System.out.println("name:" + name);
				System.out.println("auth:" + auth);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String ifurl = IntoClassroomURL + "?domain=" + domain + "&serial=" + serial + "&username=" + name
					+ "&usertype=0&ts=" + String.valueOf(ts) + "&auth=" + auth + "&userpassword="
					+ AES.encrypt("12345", key);

			System.out.println("ifurl:" + ifurl);
			try {

				strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, ifurl, "");
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;
		} else {
			try {
				String js = "alert('创建房间失败，失败代码：" + result + "');window.close();";
				strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_VIEW_SHOW", true, "", js);
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
			return strRet;
		}
	}

	private String uploadfile(String filepath, String serial) {
		System.out.println("uploadfileuploadfileuploadfileuploadfileuploadfile");
		String urlStr = upfileURL;
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("filedata", filepath);
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("key", key); // 必填 企业id authkey
		textMap.put("serial", serial); // 房间号
		textMap.put("conversion", "1"); // 必填1：转换

		textMap.put("dynamicppt", "0");// 是否是动态ppt 0: 非动态ppt 1: 动态ppt
		textMap.put("isopen", "0"); // 选填 是否是公开文档 0：表示非公开文档
									// 1：表示公开文档公开文档表示公司的其他房间都可以关联此文档，非公开文档表示只对本房间
		textMap.put("isdefault", "0");// 是否是缺省显示文件0:不是 1：是 只有上传到房间的文件才需要设置。
		String ret = HttpUtils.formUpload(urlStr, textMap, fileMap);
		System.out.println(ret);
		return ret;
	}

}
