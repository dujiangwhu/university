package com.tranzvision.gd.TZStuCenterBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPXBundle.dao.PkStuCourseChangeTMapper;
import com.tranzvision.gd.TZPXBundle.model.PkStuCourseChangeT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 学生门户 我的课程菜单，显示所有的课程
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZStuCenterBundle.service.impl.MyCourseImpl")
public class MyCourseImpl extends FrameworkImpl {

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
	private PkStuCourseChangeTMapper pkStuCourseChangeTMapper;

	@Autowired
	private GetSeqNum getSeqNum;

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
			String opType = jacksonUtil.getString("opType");
			return this.getTable(opType, oprid);
		} else {

			// 查询类型：0所有查询 1 即将上课 2正在上课 3上完课程 4取消课程
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

			String table = this.getTable(opType, oprid);
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			String classSelectHtml = "";
			try {
				classSelectHtml = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_GD_My_COURSE", true, table,
						ZSGL_URL, strCssDir, "我的课表", str_jg_id, strSiteId, request.getContextPath());
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
	 * 描述 预约课程表格
	 * 
	 * @param opType
	 * @param oprid
	 * @return
	 */
	private String getTable(String opType, String oprid) {
		// 距离多少小时属于即将上课
		String limitHour = jdbcTemplate.queryForObject(
				"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
				new Object[] { "TZ_LIMIT_HOUR" }, "String");
		StringBuffer sb = new StringBuffer();
		Date now = new Date();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String starTime = dateFormat.format(now);

		long currentTime = System.currentTimeMillis();

		// 加N小时
		currentTime += Integer.parseInt(limitHour) * 60 * 60 * 1000;

		Date date = new Date(currentTime);

		String endTime = dateFormat.format(date);

		List<Map<String, Object>> l = null;
		String sql = "";
		try {
			switch (opType) {
			// 查询类型：0所有查询 1 即将上课 2正在上课 3上完课程 4取消课程
			// 上完课的课程的状态 有后台crontab执行状态修改
			case "0":
				sql = tzGDObject.getSQLText("SQL.TZStuCenterBundle.TZ_GETALLPK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid });
				break;
			case "1":
				sql = tzGDObject.getSQLText("SQL.TZStuCenterBundle.TZ_GETYYPK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "0" });
				break;
			case "2":
				sql = tzGDObject.getSQLText("SQL.TZStuCenterBundle.TZ_GETZZSK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "0" });
				break;
			case "3":
				sql = tzGDObject.getSQLText("SQL.TZStuCenterBundle.TZ_GETYYPK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "2" });
				break;
			case "4":
				sql = tzGDObject.getSQLText("SQL.TZStuCenterBundle.TZ_GETMSSK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "0", endTime, starTime });
				break;
			case "5":
				sql = tzGDObject.getSQLText("SQL.TZStuCenterBundle.TZ_GETYYPK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "1" });
				break;
			case "6":
				sql = tzGDObject.getSQLText("SQL.TZStuCenterBundle.TZ_GETQK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "0" });
				break;
			}

			sb.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"index-bm-border\">");
			sb.append("<tbody><tr class=\"index_hd\">");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >课程级别</td>");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >上课时间</td>");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >课程名称</td>");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >授课教师</td>");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >操作</td>");
			sb.append("</tr>");
			if (l == null || l.size() == 0) {
				sb.append("<tr>");
				sb.append(
						"<td colspan=\"5\" valign=\"middle\" width=\"100%\" align=\"left\" style=\"padding-left:5px;\" >没有数据</td>");
				sb.append("</tr>");
				sb.append("</tbody></table>");
			} else {
				String KCJB = "";
				String SKSJ = "";
				String JSSJ = "";
				String KEMC = "";
				String SKJS = "";
				String status = "";
				String KKID = "";
				for (int i = 0; i < l.size(); i++) {
					KCJB = (String) l.get(i).get("TZ_COURSE_TYPE_NAME");
					SKSJ = (String) l.get(i).get("TZ_CLASS_START_TIME");
					JSSJ = (String) l.get(i).get("TZ_CLASS_END_TIME");
					KEMC = (String) l.get(i).get("TZ_COURSE_NAME");
					SKJS = (String) l.get(i).get("TZ_REALNAME");
					status = (String) l.get(i).get("TZ_APP_STATUS");
					KKID = (String) l.get(i).get("TZ_SCHEDULE_ID");
					sb.append("<tr>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + KCJB
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + SKSJ
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + KEMC
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + SKJS
							+ "</td>");
					// 0所有查询 1预约课程 2正在上课 3上完课程 4即将开课 5取消课程 6 缺课
					// 正在上课的课程 显示上课按钮
					// 即将上课的课程 显示即将开课,无法取消
					// 上完课程 显示正常结束
					// 取消课程 显示已取消
					// 缺课 显示 学生缺席
					// 其他显示 取消 按钮
					switch (status) {
					// 0 正常 1 撤销 2已上课
					case "0":
						Date starDate = DateUtil.parse(SKSJ);
						Date endDate = DateUtil.parse(JSSJ);
						// 结束时间小于 当前时间，缺课
						if (endDate.compareTo(now) < 0) {
							sb.append(
									"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >学生缺席</td>");
						} else
						// 开始时间 小于当前时间 结束时间大于当前时间 正在上课
						if (starDate.compareTo(now) <= 0 && endDate.compareTo(now) > 0) {
							sb.append(
									"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='shangK(\""
											+ KKID + "\")'>正在上课</a></td>");
						} else
						// 开始时间 大于当前时间，开始时间小于当前时间+N小时 即将开课
						if (starDate.compareTo(now) > 0 && starDate.compareTo(date) < 0) {
							sb.append(
									"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='shangK(\""
											+ KKID + "\")'>即将开课</a></td>");
						} else {
							sb.append(
									"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='cancel(\""
											+ KKID + "\")'>取消</a></td>");
						}

						break;
					case "1":
						sb.append(
								"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >已取消</td>");
						break;
					case "2":
						sb.append(
								"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >正常结束</td>");
						break;
					}
					sb.append("</tr>");
				}
				sb.append("</tbody></table>");

			}

		} catch (TzSystemException e) {
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

			if ("cancel".equals(oprType)) {
				// 预约取消

				String kkid = jacksonUtil.getString("kkid");
				String sql = "SELECT date_format(B.TZ_CLASS_START_TIME,'%Y-%m-%d %H:%I:%S') AS TZ_CLASS_START_TIME,date_format(B.TZ_CLASS_END_TIME,'%Y-%m-%d %H:%I:%S') AS TZ_CLASS_END_TIME FROM PX_TEA_SCHEDULE_T B WHERE B.TZ_SCHEDULE_ID=?";

				String TZ_CLASS_START_TIME = "";
				String TZ_CLASS_END_TIME = "";
				Map<String, Object> siteMap = jdbcTemplate.queryForMap(sql, new Object[] { kkid });
				if (siteMap != null) {
					TZ_CLASS_START_TIME = (String) siteMap.get("TZ_CLASS_START_TIME");
					TZ_CLASS_END_TIME = (String) siteMap.get("TZ_CLASS_END_TIME");
				}

				// 校验是否可以取消
				String limitHour = jdbcTemplate.queryForObject(
						"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "TZ_LIMIT_HOUR" }, "String");
				// StringBuffer sb = new StringBuffer();
				Date now = new Date();

				// SimpleDateFormat dateFormat = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// String starTime = dateFormat.format(now);
				long currentTime = System.currentTimeMillis();
				currentTime += Integer.parseInt(limitHour) * 60 * 60 * 1000;

				Date date = new Date(currentTime);
				Date starDate = DateUtil.parse(TZ_CLASS_START_TIME);
				Date endDate = DateUtil.parse(TZ_CLASS_END_TIME);

				if (starDate.compareTo(now) <= 0 && endDate.compareTo(now) > 0) {
					strRet = "{\"cancelRs\":\"正在上课前不可取消\"}";
				} else if (starDate.compareTo(now) > 0 && starDate.compareTo(date) < 0) {
					strRet = "{\"cancelRs\":\"上课前" + limitHour + "小时不可取消\"}";
				} else
				// 已经上过课了，不可取消
				if (endDate.compareTo(now) <= 0) {
					strRet = "{\"cancelRs\":\"该课程已经完结不可取消\"}";
				} else {
					sql = "SELECT TZ_APP_STATUS FROM PX_STU_APP_COURSE_T WHERE OPRID=? AND TZ_SCHEDULE_ID=?";
					String TZ_APP_STATUS = jdbcTemplate.queryForObject(sql, new Object[] { oprid, kkid }, "String");
					// 0 正常 1 撤销 2已上课
					if (TZ_APP_STATUS != null && TZ_APP_STATUS.equals("1")) {
						strRet = "{\"cancelRs\":\"已经取消不可以重复取消\"}";
					} else if (TZ_APP_STATUS != null && TZ_APP_STATUS.equals("2")) {
						strRet = "{\"cancelRs\":\"已经上过课不可取消\"}";
					} else {

						// 修改学生剩余课时卡
						sql = "SELECT TIMECARD_REMAIND FROM PX_STUDENT_T WHERE OPRID=?";

						int tzBeforeChange = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "Integer");

						TransactionStatus status = tzGDObject.getTransaction();

						try {

							jdbcTemplate.update(
									"UPDATE PX_STU_APP_COURSE_T SET TZ_APP_STATUS=?  WHERE OPRID=? AND TZ_SCHEDULE_ID=?",
									new Object[] { "1", oprid, kkid });

							jdbcTemplate.update(
									"UPDATE PX_STUDENT_T SET TIMECARD_REMAIND=TIMECARD_REMAIND+1  WHERE OPRID=?",
									new Object[] { oprid });

							int tzAfterChange = tzBeforeChange + 1;

							// 插入学生剩余课时卡变动情况表
							PkStuCourseChangeT pkStuCourseChangeT = new PkStuCourseChangeT();
							pkStuCourseChangeT
									.setTzChangeId("" + getSeqNum.getSeqNum("PK_STU_COURSE_CHANGE_T", "TZ_CHANGE_ID"));
							pkStuCourseChangeT.setOprid(oprid);
							pkStuCourseChangeT.setTzBeforeChange(tzBeforeChange);
							pkStuCourseChangeT.setTzAfterChange(tzAfterChange);
							pkStuCourseChangeT.setTzChangeType("2");
							pkStuCourseChangeT.setTzChange(new Integer(1));
							pkStuCourseChangeT.setTzChangeTime(new Date());
							pkStuCourseChangeT.setTzScheduleId(kkid);
							pkStuCourseChangeT.setRowLastmantOprid(oprid);
							pkStuCourseChangeT.setRowLastmantDttm(new Date());
							pkStuCourseChangeTMapper.insert(pkStuCourseChangeT);

							// 修改教师排课表预约状态

							int count = jdbcTemplate.queryForObject(
									"SELECT COUNT(1) FROM PX_STU_APP_COURSE_T WHERE TZ_SCHEDULE_ID=? AND TZ_APP_STATUS=0",
									new Object[] { kkid }, "Integer");
							if (count < 1) {
								jdbcTemplate.update(
										"UPDATE PX_TEA_SCHEDULE_T SET TZ_APP_STATUS=0  WHERE TZ_SCHEDULE_ID=? ",
										new Object[] { kkid });
							}
							// 提交事务
							tzGDObject.commit(status);
							strRet = "{\"cancelRs\":\"取消成功\"}";
						} catch (Exception e) {
							// 回滚事务
							tzGDObject.rollback(status);
							strRet = "{\"cancelRs\":\"取消失败，错误原因:" + e + "\"}";
						}

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return strRet;
	}

}
