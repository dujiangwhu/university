package com.tranzvision.gd.TZStuCenterBundle.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPXBundle.dao.PkStuAppCourseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PkStuCourseChangeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseTypeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeaScheduleTMapper;
import com.tranzvision.gd.TZPXBundle.model.PkStuAppCourseT;
import com.tranzvision.gd.TZPXBundle.model.PkStuAppCourseTKey;
import com.tranzvision.gd.TZPXBundle.model.PkStuCourseChangeT;
import com.tranzvision.gd.TZPXBundle.model.PxTeaScheduleT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 学生门户在线预约
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZStuCenterBundle.service.impl.OnlineAPPImlp")
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

	@Override
	public String tzGetHtmlContent(String strParams) {
		String applicationCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		jacksonUtil.json2Map(strParams);

		// 查询类型：0所有查询 1预约课程 2正在上课 3上完课程 4即将开课 5取消课程
		String opType = "";

		if (jacksonUtil.containsKey("opType")) {
			opType = jacksonUtil.getString("opType");
		}

		if (opType == null || "".equals(opType)) {
			opType = request.getParameter("opType");
		}

		String strSiteId = jdbcTemplate.queryForObject(
				"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", new Object[] { "TZ_STU_MH" },
				"String");

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
			cd.add(Calendar.DATE, 1);
			_year = cd.get(Calendar.YEAR);
			_month = cd.get(Calendar.MONTH);
			_day = cd.get(Calendar.DAY_OF_MONTH);
			now = cd.getTime();
			strDate = DateUtil.ISOSECDateString(now);
			selectDate.append("<option value =\"" + strDate + "\">" + strDate + "</option>");
		} while (!(_year == year && _month == month && day == _day));

		// 通用链接;
		String ZSGL_URL = request.getContextPath() + "/dispatcher";
		String classSelectHtml = "";
		try {
			classSelectHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_ONLINE_APP",
					select.toString(), ZSGL_URL, strCssDir, "我的课表", str_jg_id, strSiteId, request.getContextPath(),
					selectDate.toString(), "预约时间从第二天开始到下周的周日");
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

	/**
	 * 拍该课程的老师列表
	 * 
	 * @param opType
	 * @param oprid
	 * @return
	 */
	private String getTable(String courseId, String oprid, String date) {

		// 指定时间 所有已经 排这本课程的老师列表
		String starDate = date + " 00:00:00";
		String endDate = date + " 23:59:59";

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("	A.TZ_SCHEDULE_ID, ");
		sb.append("	A.OPRID, ");
		sb.append("	date_format(A.TZ_CLASS_START_TIME,'%Y-%m-%d %H:%I:%S') AS A.TZ_CLASS_START_TIME, ");
		sb.append("	date_format(A.TZ_CLASS_END_TIME,'%Y-%m-%d %H:%I:%S') AS A.TZ_CLASS_END_TIME, ");
		sb.append("	B.TZ_REALNAME, ");
		sb.append("	C.SCHOOL_AGE, ");
		sb.append("	C.EDUCATION_BG ");
		sb.append("FROM ");
		sb.append("	PX_TEA_SCHEDULE_T A ");
		sb.append("LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON A.OPRID = B.OPRID ");
		sb.append("LEFT JOIN PX_TEACHER_T C ON A.OPRID = C.OPRID ");
		sb.append("WHERE ");
		sb.append(" A.TZ_COURSE_ID =? ");
		sb.append("AND A.TZ_SCHEDULE_TYPE = ? ");
		sb.append("AND A.TZ_CLASS_START_TIME >=? ");
		sb.append("AND A.TZ_CLASS_START_TIME <=? ");

		String sql = "SELECT COUNT(*) FROM PX_STU_APP_COURSE_T WHERE TZ_SCHEDULE_ID=? AND TZ_APP_STATUS='0'";

		try {

			List<Map<String, Object>> l = jdbcTemplate.queryForList(sb.toString(),
					new Object[] { courseId, '0', starDate, endDate });
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
						"<td colspan=\"5\" valign=\"middle\" width=\"100%\" align=\"left\" style=\"padding-left:5px;\" >没有数据</td>");
				sb.append("</tr>");
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
					SCHOOL_AGE = (String) l.get(i).get("SCHOOL_AGE");
					EDUCATION_BG = (String) l.get(i).get("EDUCATION_BG");
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
							+ TZ_CLASS_START_TIME + "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ TZ_CLASS_END_TIME + "</td>");

					int count = jdbcTemplate.queryForObject(sql, new Object[] { TZ_SCHEDULE_ID }, "Integer");

					if (count > 0) {
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

			if ("search".equals(oprType)) {
				String courseId = jacksonUtil.getString("courseId");
				String date = jacksonUtil.getString("date");
				strRet = this.getTable(courseId, oprid, date);
			} else if ("select".equals(oprType)) {
				// 根据课程类型，获取课程列表
				String courseTypeId = jacksonUtil.getString("courseTypeId");
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
				strRet = select.toString();

			} else if ("onLineApp".equals(oprType)) {
				// 在线预约
				// 先查询预定表是否有记录，如果有记录并且状态正常，返回错误，
				String TZ_SCHEDULE_ID = jacksonUtil.getString("TZ_SCHEDULE_ID");

				int flag = 0;

				PxTeaScheduleT pxTeaScheduleT = pxTeaScheduleTMapper.selectByPrimaryKey(TZ_SCHEDULE_ID);

				if (pxTeaScheduleT == null) {
					strRet = "{\"false\":\"排课不存在\"}";
					flag = 1;
				}

				// 0 正常 1撤销 2已上课
				if (flag == 0) {
					if (pxTeaScheduleT.getTzScheduleType().equals("1")) {
						strRet = "{\"false\":\"排课已经撤销\"}";
						flag = 1;
					}
				}

				if (flag == 0) {
					if (pxTeaScheduleT.getTzScheduleType().equals("2")
							|| pxTeaScheduleT.getTzClassEndTime().compareTo(new Date()) <= 0) {
						strRet = "{\"false\":\"排课已经结束\"}";
						flag = 1;
					}
				}

				if (flag == 0) {
					if (pxTeaScheduleT.getTzClassStartTime().compareTo(new Date()) <= 0) {
						strRet = "{\"false\":\"排课已经开始，不能预定\"}";
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
							strRet = "{\"false\":\"没有剩余课时卡\"}";
							flag = 1;
						}
						if (flag == 0) {

							int rs = jdbcTemplate.update(
									"UPDATE PX_STUDENT_T SET TIMECARD_REMAIND=TIMECARD_REMAIND-1  WHERE OPRID=? and TIMECARD_REMAIND>=1",
									new Object[] { oprid });

							if (rs > 0) {

								int tzAfterChange = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "Integer");

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
								strRet = "{\"success\":\"预约课程成功\"}";
							} else {
								strRet = "{\"false\":\"没有剩余课时卡\"}";
							}
						}
					}
					if (TZ_APP_STATUS != null && TZ_APP_STATUS.equals("0")) {
						strRet = "{\"false\":\"已经预定改课程，不能重复预定\"}";
					}
					if (TZ_APP_STATUS != null && TZ_APP_STATUS.equals("1")) {
						// 查询剩余课时卡，看看是否够扣除
						sql = "SELECT TIMECARD_REMAIND FROM PX_STUDENT_T WHERE OPRID=?";

						int tzBeforeChange = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "Integer");

						if (tzBeforeChange < 1) {
							strRet = "{\"false\":\"没有剩余课时卡\"}";
							flag = 1;
						}
						if (flag == 0) {

							int rs = jdbcTemplate.update(
									"UPDATE PX_STUDENT_T SET TIMECARD_REMAIND=TIMECARD_REMAIND-1  WHERE OPRID=? and TIMECARD_REMAIND>=1",
									new Object[] { oprid });

							if (rs > 0) {

								int tzAfterChange = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "Integer");

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
								strRet = "{\"success\":\"预约课程成功\"}";
							} else {
								strRet = "{\"false\":\"没有剩余课时卡\"}";
							}
						}
					}
					if (TZ_APP_STATUS != null && TZ_APP_STATUS.equals("2")) {
						strRet = "{\"false\":\"课程已经结束无法预定\"}";
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
