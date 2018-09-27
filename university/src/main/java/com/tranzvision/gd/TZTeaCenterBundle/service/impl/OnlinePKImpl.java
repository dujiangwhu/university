package com.tranzvision.gd.TZTeaCenterBundle.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.tranzvision.gd.TZPXBundle.model.PkStuAppCourseT;
import com.tranzvision.gd.TZPXBundle.model.PkStuCourseChangeT;
import com.tranzvision.gd.TZPXBundle.model.PxTeaScheduleT;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 教师排课，排课 展现 本周时间，以及下周时间，显示 时间段内 已经排课的课程，如果没有课程，可以排课，排课的时候 选课程类型，然后在选课程
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZTeaCenterBundle.service.impl.OnlinePKImpl")
public class OnlinePKImpl extends FrameworkImpl {

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
	private PxTeaScheduleTMapper pxTeaScheduleTMapper;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PkStuAppCourseTMapper pkStuAppCourseTMapper;

	@Autowired
	private PkStuCourseChangeTMapper pkStuCourseChangeTMapper;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	/**
	 * 查看某天 某时间，是否排课
	 * 
	 * @param i
	 * @param data
	 * @return
	 */
	private String getPK(int i, String data, Map<String, String> hasmap) {

		String retrunStr = "";
		String key = data + "@" + String.valueOf(i);
		if (hasmap.get(key) != null && hasmap.get(key).toString().equals("OK")) {
			retrunStr = "<font color=\"red\">已排课</font>";
		} else {
			// 需要和当前时间做比较
			String xx = data;
			if (i < 10) {
				xx = data + " 0" + String.valueOf(i) + ":00:00";
			} else {
				xx = data + " " + String.valueOf(i) + ":00:00";
			}
			Date xxD = DateUtil.parseTimeStamp(xx);
			Date now = new Date();
			if (xxD.compareTo(now) > 0) {
				retrunStr = "<a href=\"javaScript: void(0)\" onClick='TeaPK(\"" + i + "\",\"" + data + "\")'>排课</a>";
			} else {
				retrunStr = "<font color=\"red\">过期</font>";
			}

		}
		return retrunStr;
	}

	private String getTable(String starDate, String oprid, String starTime, String endTime) {
		StringBuffer sb = new StringBuffer();

		// String w1 = DateUtil.getWeekStartTime("yyyy-MM-dd");
		String w1 = starDate;
		String w2 = DateUtil.getAddOneDay(w1, "yyyy-MM-dd");
		String w3 = DateUtil.getAddOneDay(w2, "yyyy-MM-dd");
		String w4 = DateUtil.getAddOneDay(w3, "yyyy-MM-dd");
		String w5 = DateUtil.getAddOneDay(w4, "yyyy-MM-dd");
		String w6 = DateUtil.getAddOneDay(w5, "yyyy-MM-dd");
		String w7 = DateUtil.getAddOneDay(w6, "yyyy-MM-dd");

		List<Map<String, Object>> l = null;
		String sql = "";
		try {
			sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETPK");
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		l = jdbcTemplate.queryForList(sql, new Object[] { oprid, endTime, starTime });

		Map<String, String> hasmap = new HashMap<String, String>();
		if (l != null && l.size() > 0) {
			String sdata = "";
			String hour = "";
			for (int i = 0; i < l.size(); i++) {
				sdata = (String) l.get(i).get("TZ_CLASS_START_TIME");
				hour = l.get(i).get("TZ_HOUR").toString();
				hasmap.put(sdata + "@" + hour, "OK");
			}
		}

		sb.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"index-bm-border\">");
		sb.append("<tbody><tr class=\"index_hd\">");
		sb.append(
				"<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >周一<p>" + w1 + "</td>");
		sb.append(
				"<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >周二<p>" + w2 + "</td>");
		sb.append(
				"<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >周三<p>" + w3 + "</td>");
		sb.append(
				"<td valign=\"middle\" width=\"15%\" align=\"left\" style=\"padding-left:5px;\" >周四<p>" + w4 + "</td>");
		sb.append(
				"<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >周五<p>" + w5 + "</td>");
		sb.append(
				"<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >周六<p>" + w6 + "</td>");
		sb.append(
				"<td valign=\"middle\" width=\"15%\" align=\"left\" style=\"padding-left:5px;\" >周日<p>" + w7 + "</td>");
		sb.append("</tr>");
		// 排课从9点-到 21点
		for (int i = 9; i <= 21; i++) {
			sb.append("<tr>");
			sb.append("<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >" + i + "点<p>"
					+ getPK(i, w1, hasmap) + "</td>");
			sb.append("<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >" + i + "点<p>"
					+ getPK(i, w2, hasmap) + "</td>");
			sb.append("<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >" + i + "点<p>"
					+ getPK(i, w3, hasmap) + "</td>");
			sb.append("<td valign=\"middle\" width=\"15%\" align=\"left\" style=\"padding-left:5px;\" >" + i + "点<p>"
					+ getPK(i, w4, hasmap) + "</td>");
			sb.append("<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >" + i + "点<p>"
					+ getPK(i, w5, hasmap) + "</td>");
			sb.append("<td valign=\"middle\" width=\"14%\" align=\"left\" style=\"padding-left:5px;\" >" + i + "点<p>"
					+ getPK(i, w6, hasmap) + "</td>");
			sb.append("<td valign=\"middle\" width=\"15%\" align=\"left\" style=\"padding-left:5px;\" >" + i + "点<p>"
					+ getPK(i, w7, hasmap) + "</td>");

			sb.append("</tr>");
		}
		sb.append("</tbody></table>");
		return sb.toString();
	}

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

		if (htmlTpye != null && htmlTpye.equals("select")) {
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
		} else if (htmlTpye != null && htmlTpye.equals("PKShow")) {
			String hour = jacksonUtil.getString("hour");
			String starDate = jacksonUtil.getString("starDate");
			hour = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(hour);
			starDate = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(starDate);
			String sql = "SELECT A.TZ_COURSE_TYPE_ID,A.TZ_COURSE_TYPE_NAME FROM PX_COURSE_TYPE_T A,PX_TEA_COURSE_TYPE_T B where A.TZ_COURSE_TYPE_ID=B.TZ_COURSE_TYPE_ID and B.OPRID=?";

			List<Map<String, Object>> l = jdbcTemplate.queryForList(sql, new Object[] { oprid });

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
			try {
				return tzGDObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_GD_CHOOSE_KE", true, select.toString(), hour,
						starDate);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";

		} else {

			// 查询类型：0所有查询 1预约课程 2正在上课 3上完课程 4即将开课 5取消课程
			String opType = "";

			if (jacksonUtil.containsKey("opType")) {
				opType = jacksonUtil.getString("opType");
			}

			if (opType == null || "".equals(opType)) {
				opType = request.getParameter("opType");
			}

			if (opType == null || "".equals(opType)) {
				opType = "0";
			}

			String strSiteId = jdbcTemplate.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
					new Object[] { "TZ_TEA_MH" }, "String");

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

			String formatString = "yyyy-MM-dd HH:mm:ss";
			String WeekStartTime = DateUtil.getWeekStartTime(formatString);
			String WeekEndTime = DateUtil.getWeekEndTime(formatString);
			String weekstartDate = DateUtil.getWeekStartTime("yyyy-MM-dd");

			String NextWeekStartTime = DateUtil.getNextWeekStartTime(formatString);
			String NextWeekEndTime = DateUtil.getNextWeekEndTime(formatString);
			String NextweekstartDate = DateUtil.getNextWeekStartTime("yyyy-MM-dd");

			String week = getTable(weekstartDate, oprid, WeekStartTime, WeekEndTime);
			String nextWeek = getTable(NextweekstartDate, oprid, NextWeekStartTime, NextWeekEndTime);

			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			String classSelectHtml = "";
			try {
				classSelectHtml = tzGDObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_GD_ONLINE_PK", true, week, ZSGL_URL,
						strCssDir, "在线排课", str_jg_id, strSiteId, request.getContextPath(), nextWeek);
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

	/* 执行SQL */
	@Override
	public String tzOther(String oprType, String comParams, String[] errorMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(comParams);

			if ("PK".equals(oprType)) {
				String hour = jacksonUtil.getString("hour");
				String starDate = jacksonUtil.getString("starDate");
				String courseId = jacksonUtil.getString("courseId");
				String courseType = jacksonUtil.getString("courseType");

				hour = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(hour);
				starDate = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(starDate);
				courseId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(courseId);
				courseType = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(courseType);

				// 检查教师是否通过审核，如果不通过，不允许排课
				String sql = "select STATU from PX_TEACHER_T where OPRID=?";
				String STATU = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "String");

				if (STATU == null || !STATU.equals("A")) {
					return "{\"pkRs\":\"您未通过审核或已经违规，不能排课！\"}";
				} else {
					// 检查拍的课程是否有 附件，如果没有 不允许排课
					sql = "select count(*) from PX_COURSE_ANNEX_T where TZ_COURSE_ID=?";
					int isFJ = jdbcTemplate.queryForObject(sql, new Object[] { courseId }, "Integer");
					if (isFJ <= 0) {
						return "{\"pkRs\":\"该课程没有附件，不能排课！\"}";
					} else {

						// 首先检查是否已经排课了
						sql = "select count(*) from PX_TEA_SCHEDULE_T where date(TZ_CLASS_START_TIME)=? AND hour(TZ_CLASS_START_TIME)=? AND TZ_SCHEDULE_TYPE!=-1";
						int recExists = jdbcTemplate.queryForObject(sql, new Object[] { starDate, hour }, "Integer");
						if (recExists > 0) {
							return "{\"pkRs\":\"该时间已经排过课程了，不能重复排课！\"}";
						} else {
							String xx = "";
							int i = Integer.parseInt(hour);
							if (i < 10) {
								xx = starDate + " 0" + String.valueOf(i) + ":00:00";
							} else {
								xx = starDate + " " + String.valueOf(i) + ":00:00";
							}
							Date xxD = DateUtil.parseTimeStamp(xx);
							Date now = new Date();
							if (xxD.compareTo(now) < 0) {
								return "{\"pkRs\":\"时间已经过期，不能排课！\"}";
							}

							// 插入排课表
							PxTeaScheduleT pxTeaScheduleT = new PxTeaScheduleT();
							pxTeaScheduleT
									.setTzScheduleId("" + getSeqNum.getSeqNum("PX_TEA_SCHEDULE_T", "TZ_SCHEDULE_ID"));
							pxTeaScheduleT.setOprid(oprid);
							pxTeaScheduleT.setTzCourseTypeId(courseType);
							pxTeaScheduleT.setTzCourseId(courseId);
							// 1 有预约 0 没有预约
							pxTeaScheduleT.setTzAppStatus("0");
							int inthour = Integer.parseInt(hour);
							if (inthour < 10) {
								hour = "0" + String.valueOf(inthour);
							}
							String classStar = starDate + " " + hour + ":00:00";
							System.out.println("classStar:" + classStar);
							Date dclassStar = DateUtil.parseTimeStamp(classStar);

							// 每堂课多少分钟
							String skHour = jdbcTemplate.queryForObject(
									"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
									new Object[] { "TZ_ONE_Course" }, "String");

							Calendar cal = Calendar.getInstance();
							cal.setTime(dclassStar);
							cal.add(Calendar.MINUTE, Integer.parseInt(skHour));
							Date dclassEnd = cal.getTime();

							pxTeaScheduleT.setTzClassStartTime(dclassStar);
							pxTeaScheduleT.setTzClassEndTime(dclassEnd);
							// 0 正常 1撤销 2已上课
							pxTeaScheduleT.setTzScheduleType("0");
							pxTeaScheduleT.setTzScheduleDate(new Date());

							pxTeaScheduleT.setRowLastmantDttm(new Date());
							pxTeaScheduleT.setRowLastmantOprid(oprid);
							pxTeaScheduleTMapper.insertSelective(pxTeaScheduleT);
							return "{\"pkRs\":\"排课成功！\"}";
						}
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
