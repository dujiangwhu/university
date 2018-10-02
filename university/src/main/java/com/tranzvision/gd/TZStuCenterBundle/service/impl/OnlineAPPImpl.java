package com.tranzvision.gd.TZStuCenterBundle.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPXBundle.dao.PkStuAppCourseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PkStuCourseChangeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseTypeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeaScheduleTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PkStuAppCourseT;
import com.tranzvision.gd.TZPXBundle.model.PkStuAppCourseTKey;
import com.tranzvision.gd.TZPXBundle.model.PkStuCourseChangeT;
import com.tranzvision.gd.TZPXBundle.model.PxTeaScheduleT;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.gh.sms.SmsService;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 学生门户在线预约
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZStuCenterBundle.service.impl.OnlineAPPImpl")
public class OnlineAPPImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private PxCourseTMapper pxCourseTMapper;

	@Autowired
	private PxCourseTypeTMapper pxCourseTypeTMapper;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PkStuAppCourseTMapper pkStuAppCourseTMapper;

	@Autowired
	private PkStuCourseChangeTMapper pkStuCourseChangeTMapper;
	@Autowired
	private PxTeaScheduleTMapper pxTeaScheduleTMapper;
	@Autowired
	private PxTeacherMapper pxTeacherMapper;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private SmsService smsService;

	@Override
	public String tzGetHtmlContent(String strParams) {
		String applicationCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		jacksonUtil.json2Map(strParams);

		String htmlTpye = "";

		if (jacksonUtil.containsKey("htmlTpye")) {
			htmlTpye = jacksonUtil.getString("htmlTpye");
		}

		if (htmlTpye != null && htmlTpye.equals("search")) {
			String courseId = jacksonUtil.getString("courseId");
			String date = jacksonUtil.getString("date");
			return this.getTable(courseId, oprid, date);
		} else if (htmlTpye != null && htmlTpye.equals("selectTE")) {
			String strRet = "";
			String contextPath = request.getContextPath();
			// 查看教师信息和评论
			String tcOPRID = jacksonUtil.getString("tcOPRID");
			tcOPRID = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(tcOPRID);
			// 教师信息
			PxTeacher pxTeacher = pxTeacherMapper.selectByPrimaryKey(tcOPRID);

			if (pxTeacher == null) {
				strRet = "教师不存在！";
			} else {

				// 教师头像
				String TZ_ATT_A_URL = "", TZ_ATTACHSYSFILENA = "";
				String userPhoto = "";
				String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
				String photoSQL = "SELECT TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND A.OPRID=?";
				Map<String, Object> photoMap = jdbcTemplate.queryForMap(photoSQL, new Object[] { tcOPRID });
				if (photoMap != null) {
					TZ_ATT_A_URL = (String) photoMap.get("TZ_ATT_A_URL");
					TZ_ATTACHSYSFILENA = (String) photoMap.get("TZ_ATTACHSYSFILENA");
					if (TZ_ATT_A_URL != null && !"".equals(TZ_ATT_A_URL) && TZ_ATTACHSYSFILENA != null
							&& !"".equals(TZ_ATTACHSYSFILENA)) {
						if ((TZ_ATT_A_URL.lastIndexOf("/") + 1) == TZ_ATT_A_URL.length()) {
							userPhoto = TZ_ATT_A_URL + TZ_ATTACHSYSFILENA;
						} else {
							userPhoto = TZ_ATT_A_URL + "/" + TZ_ATTACHSYSFILENA;
						}
						userPhoto = contextPath + userPhoto;
					}
				}
				if (userPhoto == null || "".equals(userPhoto)) {
					userPhoto = imgPath + "/bjphoto.jpg";
				}

				// 评论信息
				List<Map<String, Object>> l = jdbcTemplate.queryForList(
						"SELECT TZ_REVIEW_TYPE,TZ_REVIEW_DESC, date_format(TZ_REVIEW_TIME,'%Y-%m-%d %H:%i:%s') AS TZ_REVIEW_TIME FROM PX_STU_REVIEW_TEA_T WHERE TEA_OPRID=? AND TZ_REVIEW_STATUS=0 ",
						new Object[] { tcOPRID });

				String PLTable = "";
				StringBuffer sb = new StringBuffer();

				if (l != null && l.size() > 0) {
					String TZ_REVIEW_TYPE = "";
					String TZ_REVIEW_DESC = "";
					String TZ_REVIEW_TIME = "";
					for (int i = 0; i < l.size(); i++) {
						TZ_REVIEW_TYPE = l.get(i).get("TZ_REVIEW_TYPE") == null ? ""
								: l.get(i).get("TZ_REVIEW_TYPE").toString();
						TZ_REVIEW_DESC = l.get(i).get("TZ_REVIEW_DESC") == null ? ""
								: l.get(i).get("TZ_REVIEW_DESC").toString();
						TZ_REVIEW_TIME = l.get(i).get("TZ_REVIEW_TIME") == null ? ""
								: l.get(i).get("TZ_REVIEW_TIME").toString();
						sb.append("<div stype=\"padding:15px;border-bottom:1px solid #ddd\">");
						sb.append("<div>");
						sb.append("<div style=\"width:78px;height:14px;\">");
						// 0好评，1中评，2差评
						if (TZ_REVIEW_TYPE.equals("0")) {
							sb.append("<span style=\"color:red\">好评</span>");
						} else if (TZ_REVIEW_TYPE.equals("1")) {
							sb.append("<span style=\"color:red\">中评</span>");
						} else if (TZ_REVIEW_TYPE.equals("2")) {
							sb.append("<span style=\"color:red\">差评</span>");
						} else {
							sb.append("<span style=\"color:red\"></span>");
						}
						sb.append("</div>");
						sb.append("<p style=\"font-size:14px;padding:10px 0;line-height:180%;color:#333\">");
						sb.append(TZ_REVIEW_DESC);
						sb.append("</p>");
						sb.append("<div>");
						sb.append("<div style=\"float:left;color:#999\">");
						sb.append("<span>" + TZ_REVIEW_TIME + "</span>");
						sb.append("</div>");
						sb.append("</div>");
						sb.append("</div>");
						sb.append("</div>");
					}
				}
				PLTable = sb.toString();

				// 载入页面 TZ_GD_SHOW_TAR
				String sex = "";
				if (pxTeacher.getSex() != null) {
					if (pxTeacher.getSex().equals("M")) {
						sex = "男";
					} else {
						sex = "女";
					}
				}
				try {
					String EDUCATION_BG = "";
					if (pxTeacher.getEducationBg() != null) {
						EDUCATION_BG = pxTeacher.getEducationBg();
						if (EDUCATION_BG.equals("D")) {
							EDUCATION_BG = "博士";
						} else if (EDUCATION_BG.equals("S")) {
							EDUCATION_BG = "硕士";
						} else if (EDUCATION_BG.equals("X")) {
							EDUCATION_BG = "学士";
						}
					}
					strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_GD_SHOW_TAR", true, userPhoto,
							pxTeacher.getName(), sex, String.valueOf(pxTeacher.getAge()), pxTeacher.getLevel(),
							pxTeacher.getSchool(), EDUCATION_BG, String.valueOf(pxTeacher.getSchoolAge()),
							pxTeacher.getTeacherCard(), pxTeacher.getIntroduce(), PLTable, pxTeacher.getIdCard());
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "无法获取相关数据";
				}
			}
			return strRet;
		} else if (htmlTpye != null && htmlTpye.equals("select")) {
			// 根据课程类型，获取课程列表
			String courseTypeId = jacksonUtil.getString("courseTypeId");
			courseTypeId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(courseTypeId);
			String sql = "SELECT TZ_COURSE_ID,TZ_COURSE_NAME FROM PX_COURSE_T WHERE TZ_COURSE_TYPE_ID=?";

			List<Map<String, Object>> l = jdbcTemplate.queryForList(sql, new Object[] { courseTypeId });

			StringBuffer select = new StringBuffer();

			// 获取所有的课程列表
			if (l != null && l.size() > 0) {
				String TZ_COURSE_ID = "";
				String TZ_COURSE_NAME = "";
				for (int i = 0; i < l.size(); i++) {
					TZ_COURSE_ID = (String) l.get(i).get("TZ_COURSE_ID");
					TZ_COURSE_NAME = (String) l.get(i).get("TZ_COURSE_NAME");
					select.append("<option value =\"" + TZ_COURSE_ID + "\">" + TZ_COURSE_NAME + "</option>");
				}
			}
			return select.toString();

		} else {

			String strSiteId = jdbcTemplate.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
					new Object[] { "TZ_STU_MH" }, "String");

			String str_jg_id = "";
			String strCssDir = "";
			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				String skinstor = (String) siteMap.get("TZ_SKIN_STOR");
				String websitePath = getSysHardCodeVal.getWebsiteCssPath();

				String strRandom = String.valueOf(10 * Math.random());
				if ("".equals(skinstor) || skinstor == null) {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				} else {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + skinstor + "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				}
			}
			String sql = "SELECT TZ_COURSE_TYPE_ID,TZ_COURSE_TYPE_NAME FROM PX_COURSE_TYPE_T";

			List<Map<String, Object>> l = jdbcTemplate.queryForList(sql);

			StringBuffer select = new StringBuffer();

			// 获取所有的课程列表
			if (l != null && l.size() > 0) {
				String TZ_COURSE_TYPE_ID = "";
				String TZ_COURSE_TYPE_NAME = "";
				for (int i = 0; i < l.size(); i++) {
					TZ_COURSE_TYPE_ID = (String) l.get(i).get("TZ_COURSE_TYPE_ID");
					TZ_COURSE_TYPE_NAME = (String) l.get(i).get("TZ_COURSE_TYPE_NAME");
					select.append("<option value =\"" + TZ_COURSE_TYPE_ID + "\">" + TZ_COURSE_TYPE_NAME + "</option>");
				}
			}

			// 预约时间从第二天开始-到下周的周日
			StringBuffer selectDate = new StringBuffer();
			// 首先计算结束日
			Calendar cd = Calendar.getInstance();
			Date lastDate = DateUtil.getNextWeekEndTime();
			cd.setTime(lastDate);
			int year = cd.get(Calendar.YEAR);
			int month = cd.get(Calendar.MONTH);
			int day = cd.get(Calendar.DAY_OF_MONTH);

			int _year = 0;
			int _month = 0;
			int _day = 0;

			Date now = new Date();
			String strDate = "";
			do {
				cd.setTime(now);

				_year = cd.get(Calendar.YEAR);
				_month = cd.get(Calendar.MONTH);
				_day = cd.get(Calendar.DAY_OF_MONTH);

				strDate = DateUtil.ISOSECDateString(now);
				selectDate.append("<option value =\"" + strDate + "\">" + strDate + "</option>");
				cd.add(Calendar.DATE, 1);
				now = cd.getTime();
			} while (!(_year == year && _month == month && day == _day));

			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			String classSelectHtml = "";
			try {
				classSelectHtml = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_GD_ONLINE_APP", true,
						select.toString(), ZSGL_URL, strCssDir, "我的课表", str_jg_id, strSiteId, request.getContextPath(),
						selectDate.toString(), "预约时间从当天开始到下周的周日");
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "无法获取相关数据";
			}
			applicationCenterHtml = classSelectHtml;
			applicationCenterHtml = siteRepCssServiceImpl.repTitle(applicationCenterHtml, strSiteId);
			applicationCenterHtml = siteRepCssServiceImpl.repCss(applicationCenterHtml, strSiteId);
			return applicationCenterHtml;
		}
	}

	/**
	 * 拍该课程的老师列表
	 * 
	 * @param opType
	 * @param oprid
	 * @return
	 */
	private String getTable(String courseId, String oprid, String date) {
		// String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// 指定时间 所有已经 排这本课程的老师列表
		String starDate = date + " 00:00:00";
		String endDate = date + " 23:59:59";

		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("	A.TZ_SCHEDULE_ID, ");
		sql.append("	A.OPRID, ");
		sql.append("	date_format(A.TZ_CLASS_START_TIME,'%Y-%m-%d %H:%i:%S') AS TZ_CLASS_START_TIME, ");
		sql.append("	date_format(A.TZ_CLASS_END_TIME,'%Y-%m-%d %H:%i:%S') AS TZ_CLASS_END_TIME, ");
		sql.append("	B.TZ_REALNAME, ");
		sql.append("	C.SCHOOL_AGE, ");
		sql.append("	C.EDUCATION_BG ");
		sql.append("FROM ");
		sql.append("	PX_TEA_SCHEDULE_T A ");
		sql.append("LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON A.OPRID = B.OPRID ");
		sql.append("LEFT JOIN PX_TEACHER_T C ON A.OPRID = C.OPRID ");
		sql.append("WHERE ");
		sql.append(" A.TZ_COURSE_ID =? ");
		sql.append("AND A.TZ_SCHEDULE_TYPE = ? ");
		sql.append("AND A.TZ_CLASS_START_TIME >=? ");
		sql.append("AND A.TZ_CLASS_START_TIME <=? and A.TZ_CLASS_START_TIME>now()");

		String sqlxx = "SELECT COUNT(*) FROM PX_STU_APP_COURSE_T WHERE TZ_SCHEDULE_ID=? AND TZ_APP_STATUS='0' AND OPRID=?";

		try {
			System.out.println(courseId);
			System.out.println(starDate);
			System.out.println(endDate);
			List<Map<String, Object>> l = jdbcTemplate.queryForList(sql.toString(),
					new Object[] { courseId, "0", starDate, endDate });
			sb.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"index-bm-border\">");
			sb.append("<tbody><tr class=\"index_hd\">");
			sb.append("<td valign=\"middle\" width=\"15%\" align=\"left\" style=\"padding-left:5px;\" >授课教师</td>");
			sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >教龄</td>");
			sb.append("<td valign=\"middle\" width=\"15%\" align=\"left\" style=\"padding-left:5px;\" >学历</td>");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >上课时间</td>");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >结束时间</td>");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >操作</td>");
			sb.append("</tr>");
			if (l == null || l.size() == 0) {
				sb.append("<tr>");
				sb.append(
						"<td colspan=\"6\" valign=\"middle\" width=\"100%\" align=\"left\" style=\"padding-left:5px;\" >没有数据</td>");
				sb.append("</tr>");
				sb.append("</tbody></table>");
			} else {
				String TZ_REALNAME = "";
				String SCHOOL_AGE = "";
				String EDUCATION_BG = "";
				String TZ_CLASS_START_TIME = "";
				String TZ_CLASS_END_TIME = "";
				String TZ_SCHEDULE_ID = "";
				String OPRID = "";
				for (int i = 0; i < l.size(); i++) {
					TZ_REALNAME = (String) l.get(i).get("TZ_REALNAME");
					SCHOOL_AGE = l.get(i).get("SCHOOL_AGE").toString();
					EDUCATION_BG = (String) l.get(i).get("EDUCATION_BG");
					if (EDUCATION_BG != null) {
						if (EDUCATION_BG.equals("D")) {
							EDUCATION_BG = "博士";
						} else if (EDUCATION_BG.equals("S")) {
							EDUCATION_BG = "硕士";
						} else if (EDUCATION_BG.equals("X")) {
							EDUCATION_BG = "学士";
						}
					}

					TZ_CLASS_START_TIME = (String) l.get(i).get("TZ_CLASS_START_TIME");
					TZ_CLASS_END_TIME = (String) l.get(i).get("TZ_CLASS_END_TIME");
					TZ_SCHEDULE_ID = (String) l.get(i).get("TZ_SCHEDULE_ID");
					OPRID = (String) l.get(i).get("OPRID");

					sb.append("<tr>");
					sb.append("<td valign=\"middle\" width=\"15%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ TZ_REALNAME + "</td>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ SCHOOL_AGE + "</td>");
					sb.append("<td valign=\"middle\" width=\"15%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ EDUCATION_BG + "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ TZ_CLASS_START_TIME.substring(0, 16) + "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ TZ_CLASS_END_TIME.substring(0, 16) + "</td>");

					int count = jdbcTemplate.queryForObject(sqlxx, new Object[] { TZ_SCHEDULE_ID, oprid }, "Integer");

					if (count == 0) {
						sb.append(
								"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='showT(\""
										+ OPRID
										+ "\")'>查看</a>&nbsp;&nbsp;&nbsp;<a href=\"javaScript: void(0)\" onClick='yuyue(\""
										+ TZ_SCHEDULE_ID + "\")'>预约</a></td>");
					} else {
						sb.append(
								"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='showT(\""
										+ OPRID
										+ "\")'>查看</a>&nbsp;&nbsp;&nbsp;<span style=\"color:red\">已预约</span></td>");
					}

					sb.append("</tr>");
				}
				sb.append("</tbody></table>");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	/* 执行SQL */
	@Override
	public String tzOther(String oprType, String comParams, String[] errorMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(comParams);

			if ("onLineApp".equals(oprType)) {
				// 在线预约
				// 先查询预定表是否有记录，如果有记录并且状态正常，返回错误，
				String TZ_SCHEDULE_ID = jacksonUtil.getString("TZ_SCHEDULE_ID");
				TZ_SCHEDULE_ID = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(TZ_SCHEDULE_ID);
				int flag = 0;

				PxTeaScheduleT pxTeaScheduleT = pxTeaScheduleTMapper.selectByPrimaryKey(TZ_SCHEDULE_ID);

				if (pxTeaScheduleT == null) {
					strRet = "{\"yyRs\":\"排课不存在\"}";
					flag = 1;
				}

				// 0 正常 1撤销 2已上课
				if (flag == 0) {
					if (pxTeaScheduleT.getTzScheduleType().equals("1")) {
						strRet = "{\"yyRs\":\"排课已经撤销\"}";
						flag = 1;
					}
				}

				if (flag == 0) {
					if (pxTeaScheduleT.getTzScheduleType().equals("2")
							|| pxTeaScheduleT.getTzClassEndTime().compareTo(new Date()) <= 0) {
						strRet = "{\"yyRs\":\"排课已经结束\"}";
						flag = 1;
					}
				}

				if (flag == 0) {
					if (pxTeaScheduleT.getTzClassStartTime().compareTo(new Date()) <= 0) {
						strRet = "{\"yyRs\":\"排课已经开始，不能预定\"}";
						flag = 1;
					}
				}

				if (flag == 0) {
					// 检查同一时间是否有 其他已经约了的课程
					String starTime = DateUtil.formatShortDate(pxTeaScheduleT.getTzClassStartTime());

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(pxTeaScheduleT.getTzClassStartTime());
					int hour = calendar.get(Calendar.HOUR_OF_DAY);

					System.out.println("hour:" + hour);
					System.out.println("starTime:" + starTime);
					String sql = "select count(*) from PX_STU_APP_COURSE_T where TZ_APP_STATUS=? and OPRID=? and TZ_SCHEDULE_ID ";
					sql = sql
							+ "in (select TZ_SCHEDULE_ID from PX_TEA_SCHEDULE_T where TZ_SCHEDULE_TYPE=? and date(TZ_CLASS_START_TIME)=? AND hour(TZ_CLASS_START_TIME)=?)";

					int count = jdbcTemplate.queryForObject(sql, new Object[] { "0", oprid, "0", starTime, hour },
							"Integer");
					if (count > 0) {
						// return "{\"pkRs\":\"该时间已经排过课程了，不能重复排课！\"}";
						strRet = "{\"yyRs\":\"同一时间 已经预约了其他课程，该时间不可预约\"}";
						flag = 1;
					}
				}

				if (flag == 0) {

					String sql = "SELECT TZ_APP_STATUS FROM PX_STU_APP_COURSE_T WHERE TZ_SCHEDULE_ID=? AND OPRID=?";
					String TZ_APP_STATUS = jdbcTemplate.queryForObject(sql, new Object[] { TZ_SCHEDULE_ID, oprid },
							"String");

					// 不存在预定记录
					// 0 正常 1 撤销 2已上课
					if (TZ_APP_STATUS == null || TZ_APP_STATUS.equals("")) {

						// 查询剩余课时卡，看看是否够扣除
						sql = "SELECT TIMECARD_REMAIND FROM PX_STUDENT_T WHERE OPRID=?";

						int tzBeforeChange = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "Integer");

						if (tzBeforeChange < 1) {
							strRet = "{\"yyRs\":\"没有剩余课时卡\"}";
							flag = 1;
						}
						if (flag == 0) {

							TransactionStatus status = tzGDObject.getTransaction();
							try {
								int rs = jdbcTemplate.update(
										"UPDATE PX_STUDENT_T SET TIMECARD_REMAIND=TIMECARD_REMAIND-1  WHERE OPRID=? and TIMECARD_REMAIND>=1",
										new Object[] { oprid });

								if (rs > 0) {

									int tzAfterChange = tzBeforeChange - 1;

									// 不存在预定，插入预定表，并且扣除用户剩余课时
									PkStuAppCourseT record = new PkStuAppCourseT();
									record.setOprid(oprid);
									record.setTzScheduleId(TZ_SCHEDULE_ID);
									record.setTzAppData(new Date());
									record.setTzAppStatus("0");
									record.setRowLastmantDttm(new Date());
									record.setRowLastmantOprid(oprid);
									pkStuAppCourseTMapper.insert(record);

									// 插入学生剩余课时卡变动情况表
									PkStuCourseChangeT pkStuCourseChangeT = new PkStuCourseChangeT();
									pkStuCourseChangeT.setTzChangeId(
											"" + getSeqNum.getSeqNum("PK_STU_COURSE_CHANGE_T", "TZ_CHANGE_ID"));
									pkStuCourseChangeT.setOprid(oprid);
									pkStuCourseChangeT.setTzBeforeChange(tzBeforeChange);
									pkStuCourseChangeT.setTzAfterChange(tzAfterChange);
									pkStuCourseChangeT.setTzChangeType("1");
									pkStuCourseChangeT.setTzChange(new Integer(-1));
									pkStuCourseChangeT.setTzChangeTime(new Date());
									pkStuCourseChangeT.setTzScheduleId(TZ_SCHEDULE_ID);
									pkStuCourseChangeT.setRowLastmantOprid(oprid);
									pkStuCourseChangeT.setRowLastmantDttm(new Date());
									pkStuCourseChangeTMapper.insert(pkStuCourseChangeT);

									// 修改教师排课表预约状态
									rs = jdbcTemplate.update(
											"UPDATE PX_TEA_SCHEDULE_T SET TZ_APP_STATUS='1'  WHERE TZ_SCHEDULE_ID=?",
											new Object[] { TZ_SCHEDULE_ID });
									tzGDObject.commit(status);
									strRet = "{\"yyRs\":\"预约课程成功\"}";

									// 约课成功，发送短信
									String getSmsSendTmpSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
									String strSmsSendTmp = jdbcTemplate.queryForObject(getSmsSendTmpSql,
											new Object[] { "TZ_SMS_SEND_YK_TPL" }, "String");
									if (strSmsSendTmp == null) {
										strSmsSendTmp = "SMS_146804571";
									}

									String name = "";
									String strPhone = "";
									String kec = "";
									String datetime = "";

									sql = "select TZ_REALNAME,TZ_MOBILE from  PS_TZ_AQ_YHXX_TBL where OPRID=?";
									Map<String, Object> userInfo = jdbcTemplate.queryForMap(sql,
											new Object[] { oprid });
									if (userInfo != null) {
										name = userInfo.get("TZ_REALNAME") == null ? ""
												: String.valueOf(userInfo.get("TZ_REALNAME"));
										strPhone = userInfo.get("TZ_MOBILE") == null ? ""
												: String.valueOf(userInfo.get("TZ_MOBILE"));
									}

									sql = "select A.TZ_COURSE_NAME,date_format(B.TZ_CLASS_START_TIME,'%Y-%m-%d %H:%i:%S') AS TZ_CLASS_START_TIME FROM PX_COURSE_T A,PX_TEA_SCHEDULE_T B WHERE A.TZ_COURSE_ID=B.TZ_COURSE_ID AND B.TZ_SCHEDULE_ID=?";

									Map<String, Object> PKInfo = jdbcTemplate.queryForMap(sql,
											new Object[] { TZ_SCHEDULE_ID });
									if (userInfo != null) {
										kec = PKInfo.get("TZ_COURSE_NAME") == null ? ""
												: String.valueOf(PKInfo.get("TZ_COURSE_NAME"));
										datetime = PKInfo.get("TZ_CLASS_START_TIME") == null ? ""
												: String.valueOf(PKInfo.get("TZ_CLASS_START_TIME"));
									}
									// ${name}同学，你已成功预约${kec}课程，上课时间：${datetime}。上课请提前登陆兴语系统，老师在等您呦！
									String json = "{\"name\":\"" + name + "\", \"kec\":\"" + kec + "\",\"datetime\":\""
											+ datetime + "\"}";
									boolean sendrs = smsService.sendSmsAndLog(strPhone, strSmsSendTmp, json);
									// 失败后，重新发送一次
									if (!sendrs) {
										smsService.sendSmsAndLog(strPhone, strSmsSendTmp, json);
									}

								} else {
									tzGDObject.commit(status);
									strRet = "{\"yyRs\":\"没有剩余课时卡\"}";
								}
							} catch (Exception e) {
								// 回滚事务
								tzGDObject.rollback(status);
								strRet = "{\"yyRs\":\"预约失败，错误原因:" + e + "\"}";
							}
						}
					}
					if (TZ_APP_STATUS != null && TZ_APP_STATUS.equals("0")) {
						strRet = "{\"yyRs\":\"已经预定改课程，不能重复预定\"}";
					}
					if (TZ_APP_STATUS != null && TZ_APP_STATUS.equals("1")) {
						// 查询剩余课时卡，看看是否够扣除
						sql = "SELECT TIMECARD_REMAIND FROM PX_STUDENT_T WHERE OPRID=?";

						int tzBeforeChange = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "Integer");

						if (tzBeforeChange < 1) {
							strRet = "{\"yyRs\":\"没有剩余课时卡\"}";
							flag = 1;
						}
						if (flag == 0) {
							TransactionStatus status = tzGDObject.getTransaction();
							try {
								int rs = jdbcTemplate.update(
										"UPDATE PX_STUDENT_T SET TIMECARD_REMAIND=TIMECARD_REMAIND-1  WHERE OPRID=? and TIMECARD_REMAIND>=1",
										new Object[] { oprid });

								if (rs > 0) {

									int tzAfterChange = tzBeforeChange - 1;

									// 修改记录
									PkStuAppCourseT record = new PkStuAppCourseT();
									record.setOprid(oprid);
									record.setTzScheduleId(TZ_SCHEDULE_ID);
									record.setTzAppData(new Date());
									record.setTzAppStatus("0");
									record.setRowLastmantDttm(new Date());
									record.setRowLastmantOprid(oprid);
									pkStuAppCourseTMapper.updateByPrimaryKeySelective(record);

									// 插入学生剩余课时卡变动情况表
									PkStuCourseChangeT pkStuCourseChangeT = new PkStuCourseChangeT();
									pkStuCourseChangeT.setTzChangeId(
											"" + getSeqNum.getSeqNum("PK_STU_COURSE_CHANGE_T", "TZ_CHANGE_ID"));
									pkStuCourseChangeT.setOprid(oprid);
									pkStuCourseChangeT.setTzBeforeChange(tzBeforeChange);
									pkStuCourseChangeT.setTzAfterChange(tzAfterChange);
									pkStuCourseChangeT.setTzChangeType("1");
									pkStuCourseChangeT.setTzChange(new Integer(-1));
									pkStuCourseChangeT.setTzChangeTime(new Date());
									pkStuCourseChangeT.setTzScheduleId(TZ_SCHEDULE_ID);
									pkStuCourseChangeT.setRowLastmantOprid(oprid);
									pkStuCourseChangeT.setRowLastmantDttm(new Date());
									pkStuCourseChangeTMapper.insert(pkStuCourseChangeT);

									// 修改教师排课表预约状态
									rs = jdbcTemplate.update(
											"UPDATE PX_TEA_SCHEDULE_T SET TZ_APP_STATUS='1'  WHERE TZ_SCHEDULE_ID=?",
											new Object[] { TZ_SCHEDULE_ID });
									tzGDObject.commit(status);
									strRet = "{\"yyRs\":\"预约课程成功\"}";

									// 约课成功，发送短信
									String getSmsSendTmpSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
									String strSmsSendTmp = jdbcTemplate.queryForObject(getSmsSendTmpSql,
											new Object[] { "TZ_SMS_SEND_YK_TPL" }, "String");
									if (strSmsSendTmp == null) {
										strSmsSendTmp = "SMS_146804571";
									}
									String name = "";
									String strPhone = "";
									String kec = "";
									String datetime = "";

									sql = "select TZ_REALNAME,TZ_MOBILE from  PS_TZ_AQ_YHXX_TBL where OPRID=?";
									Map<String, Object> userInfo = jdbcTemplate.queryForMap(sql,
											new Object[] { oprid });
									if (userInfo != null) {
										name = userInfo.get("TZ_REALNAME") == null ? ""
												: String.valueOf(userInfo.get("TZ_REALNAME"));
										strPhone = userInfo.get("TZ_MOBILE") == null ? ""
												: String.valueOf(userInfo.get("TZ_MOBILE"));
									}

									sql = "select A.TZ_COURSE_NAME,date_format(B.TZ_CLASS_START_TIME,'%Y-%m-%d %H:%i:%S') AS TZ_CLASS_START_TIME FROM PX_COURSE_T A,PX_TEA_SCHEDULE_T B WHERE A.TZ_COURSE_ID=B.TZ_COURSE_ID AND B.TZ_SCHEDULE_ID=?";

									Map<String, Object> PKInfo = jdbcTemplate.queryForMap(sql,
											new Object[] { TZ_SCHEDULE_ID });
									if (userInfo != null) {
										kec = PKInfo.get("TZ_COURSE_NAME") == null ? ""
												: String.valueOf(PKInfo.get("TZ_COURSE_NAME"));
										datetime = PKInfo.get("TZ_CLASS_START_TIME") == null ? ""
												: String.valueOf(PKInfo.get("TZ_CLASS_START_TIME"));
									}
									// ${name}同学，你已成功预约${kec}课程，上课时间：${datetime}。上课请提前登陆兴语系统，老师在等您呦！
									String json = "{\"name\":\"" + name + "\", \"kec\":\"" + kec + "\",\"datetime\":\""
											+ datetime + "\"}";
									boolean sendrs = smsService.sendSmsAndLog(strPhone, strSmsSendTmp, json);
									// 失败后，重新发送一次
									if (!sendrs) {
										smsService.sendSmsAndLog(strPhone, strSmsSendTmp, json);
									}
								} else {
									tzGDObject.commit(status);
									strRet = "{\"yyRs\":\"没有剩余课时卡\"}";
								}
							} catch (Exception e) {
								// 回滚事务
								tzGDObject.rollback(status);
								strRet = "{\"yyRs\":\"预约失败，错误原因:" + e + "\"}";
							}
						}
					}
					if (TZ_APP_STATUS != null && TZ_APP_STATUS.equals("2")) {
						strRet = "{\"yyRs\":\"课程已经结束无法预定\"}";
					}
				}
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return strRet;
	}
}
