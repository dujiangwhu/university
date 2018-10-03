package com.tranzvision.gd.TZTeaCenterBundle.service.impl;

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
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZTeaCenterBundle.service.impl.TeaCourseImpl")
public class TeaCourseImpl extends FrameworkImpl {

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

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

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

			String page = jacksonUtil.getString("pageNo");
			String opType = jacksonUtil.getString("opType");

			return this.getTable(opType, oprid, page);
		} else if (htmlTpye != null && htmlTpye.equals("showFile")) {
			// 显示文档
			String SCHEDULE_ID = jacksonUtil.getString("SCHEDULE_ID");
			SCHEDULE_ID = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(SCHEDULE_ID);
			String sql = "select A.TZ_ATTACHFILE_NAME,A.TZ_PKSK_XH,A.TZ_COURSE_ID FROM PX_COURSE_ANNEX_T A,PX_TEA_SCHEDULE_T B where A.TZ_COURSE_ID = B.TZ_COURSE_ID and B.TZ_SCHEDULE_ID=? and B.OPRID=?";
			String TZ_ATTACHFILE_NAME = "";
			String TZ_PKSK_XH = "";
			String TZ_COURSE_ID = "";
			List<Map<String, Object>> l = jdbcTemplate.queryForList(sql, new Object[] { SCHEDULE_ID, oprid });
			StringBuffer sb = new StringBuffer();
			if (l != null && l.size() > 0) {
				for (int i = 0; i < l.size(); i++) {
					TZ_ATTACHFILE_NAME = l.get(i).get("TZ_ATTACHFILE_NAME") == null ? ""
							: l.get(i).get("TZ_ATTACHFILE_NAME").toString();
					TZ_PKSK_XH = l.get(i).get("TZ_PKSK_XH") == null ? "" : l.get(i).get("TZ_PKSK_XH").toString();
					TZ_COURSE_ID = l.get(i).get("TZ_COURSE_ID") == null ? "" : l.get(i).get("TZ_COURSE_ID").toString();
					sb.append("<tr class='font666'>");
					sb.append("<td valign='middle' width='30' align='center'>");
					sb.append(
							"<input style='width: 15px;' type='radio' name='classidradio' value='" + TZ_PKSK_XH + "'>");
					sb.append("</td> ");
					sb.append("<td valign='middle' width='417' align='left' onclick='selectRadioClassId(" + i + ")'>");

					sb.append(TZ_ATTACHFILE_NAME);
					sb.append("</td></tr> ");
				}
			}
			String returnHtml = "";
			try {
				returnHtml = tzGDObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_GD_CHOOSE_FILE", true, sb.toString(),
						SCHEDULE_ID);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnHtml;

		} else {

			// 查询类型：0本周课程 1下周课程 2即将上课 3正在上课 4上完课程 5取消课程
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

			// 批量重置已经上过课的课程,状态为2
			// 0 正常 1撤销 2已上课
			String ppsql = "update PX_TEA_SCHEDULE_T set TZ_SCHEDULE_TYPE=?,ROW_LASTMANT_DTTM=now() where TZ_SCHEDULE_TYPE=? and OPRID=? and TZ_ROOM_ID !='' and TZ_ROOM_KEY !=''";
			jdbcTemplate.update(ppsql, new Object[] { "2", "1", oprid });

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

			String table = this.getTable(opType, oprid, "1");
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			String classSelectHtml = "";
			try {
				classSelectHtml = tzGDObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_GD_TEA_COURSE", true, table,
						ZSGL_URL, strCssDir, "我的排课课表", str_jg_id, strSiteId, request.getContextPath());
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
	 * @param pageNo
	 *            第几页
	 * @return
	 */
	private String getTable(String opType, String oprid, String pageNo) {
		// 距离多少小时属于即将上课
		String limitHour = jdbcTemplate.queryForObject(
				"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
				new Object[] { "TZ_LIMIT_MINUTE" }, "String");
		StringBuffer sb = new StringBuffer();
		Date now = new Date();
		String formatString = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);

		String starTime = dateFormat.format(now);

		long currentTime = System.currentTimeMillis();

		// 加N分钟
		currentTime += Integer.parseInt(limitHour) * 60 * 1000;

		Date date = new Date(currentTime);

		String endTime = dateFormat.format(date);

		List<Map<String, Object>> l = null;
		String sql = "";

		// 状态显示 结束时间 小于当前时间，课程已经结束
		// 结束时间 大于当前时间，开始时间小于当前时间，课程正在进行
		// 开始时间大于当前时间，课程未开始
		// 本周开始时间，结束时间 从周1开始
		String WeekStartTime = DateUtil.getWeekStartTime(formatString);
		String WeekEndTime = DateUtil.getWeekEndTime(formatString);
		// 下周开始时间，结束时间 从周1开始
		String NextWeekStartTime = DateUtil.getNextWeekStartTime(formatString);
		String NextWeekEndTime = DateUtil.getNextWeekEndTime(formatString);
		// 总条数
		int count = 0;

		// 当前页数
		int page = Integer.parseInt(pageNo);

		// 分页总数
		int pagesize = 0;

		// 每页的行数
		int pageLimit = Integer.parseInt(
				jdbcTemplate.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "TZ_PAGE_LIMIT" }, "String"));

		// sql 查询的开始行数
		int beginH = (page - 1) * pageLimit;

		try {
			switch (opType) {
			// 查询类型：0本周课程 1下周课程 2即将上课 3正在上课 4上完课程 5取消课程
			case "0":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETWEEK");
				l = jdbcTemplate.queryForList(sql,
						new Object[] { oprid, WeekEndTime, WeekStartTime, beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETWEEKCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, WeekEndTime, WeekStartTime }, "Integer");
				break;
			case "1":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETWEEK");
				l = jdbcTemplate.queryForList(sql,
						new Object[] { oprid, NextWeekEndTime, NextWeekStartTime, beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETWEEKCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, NextWeekEndTime, NextWeekStartTime },
						"Integer");
				break;
			case "2":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETWEEK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, endTime, starTime, beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETWEEKCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, endTime, starTime }, "Integer");
				break;
			case "3":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETZZSK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "0", beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETZZSKCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, "0" }, "Integer");
				break;
			case "4":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETSWK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "2", beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETSWKCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, "2" }, "Integer");
				break;
			case "5":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETSWK");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "1", beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETSWKCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, "1" }, "Integer");
				break;
			}

			if (count > pageLimit) {
				pagesize = count / pageLimit;
				if (count % pageLimit != 0) {
					pagesize = pagesize + 1;
				}
			} else {
				pagesize = 1;
			}

			sb.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"index-bm-border\">");
			sb.append("<tbody><tr class=\"index_hd\">");
			sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >课程级别</td>");
			sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >上课时间</td>");
			sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >课程名称</td>");
			sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >操作</td>");
			sb.append("</tr>");
			if (l == null || l.size() == 0) {
				sb.append("<tr>");
				sb.append(
						"<td colspan=\"4\" valign=\"middle\" width=\"100%\" align=\"left\" style=\"padding-left:5px;\" >没有数据</td>");
				sb.append("</tr>");
				sb.append("</tbody></table>");
			} else {
				String KCJB = "";
				String SKSJ = "";
				String JSSJ = "";
				String KEMC = "";
				// String SKJS = "";
				String status = "";
				String KKID = "";
				for (int i = 0; i < l.size(); i++) {
					KCJB = (String) l.get(i).get("TZ_COURSE_TYPE_NAME");
					SKSJ = (String) l.get(i).get("TZ_CLASS_START_TIME");
					JSSJ = (String) l.get(i).get("TZ_CLASS_END_TIME");
					KEMC = (String) l.get(i).get("TZ_COURSE_NAME");
					status = (String) l.get(i).get("TZ_SCHEDULE_TYPE");
					KKID = (String) l.get(i).get("TZ_SCHEDULE_ID");

					System.out.println(KCJB);
					System.out.println(SKSJ);
					System.out.println(JSSJ);
					System.out.println(KEMC);
					System.out.println(status);
					System.out.println(KKID);
					sb.append("<tr>");
					sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >" + KCJB
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ SKSJ.substring(0, 16) + "</td>");
					sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >" + KEMC
							+ "</td>");
					// 查询类型：0本周课程 1下周课程 2即将上课 3正在上课 4上完课程 5取消课程
					switch (opType) {
					// 0本周课程
					case "0":
						// 本周课程的逻辑，如果状态是 2，显示已经上过课，如果状态是 1 ，显示已撤销
						// 如果状态是 0 ，看看当前时间，是否在 课程开始时间和结束时间之类，如果是
						// 正在上课，如果当前时间大于结束时间，缺课
						// 如果当前时间小于 开始时间，显示等待上课

						if (status.equals("0")) {
							Date starDate = DateUtil.parseTimeStamp(SKSJ);
							Date endDate = DateUtil.parseTimeStamp(JSSJ);
							// 结束时间小于 当前时间，缺课
							if (endDate.compareTo(now) < 0) {
								sb.append(
										"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >缺课</td>");
							} else
							// 开始时间 小于当前时间 结束时间大于当前时间 正在上课
							if (starDate.compareTo(now) <= 0 && endDate.compareTo(now) > 0) {
								sb.append(
										"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='TeashangK(\""
												+ KKID + "\")'>正在上课</a></td>");
							} else
							// 开始时间 大于当前时间，开始时间小于当前时间+N小时 即将开课
							if (starDate.compareTo(now) > 0 && starDate.compareTo(date) < 0) {
								sb.append(
										"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='TeashangK(\""
												+ KKID + "\")'>即将开课</a></td>");
							} else {
								sb.append(
										"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='Teacancel(\""
												+ KKID + "\")'>取消</a></td>");
							}
						} else if (status.equals("2")) {
							sb.append(
									"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >正常结束</td>");
						} else if (status.equals("1")) {
							sb.append(
									"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >已取消</td>");
						}

						break;
					// 1下周课程
					case "1":
						if (status.equals("0")) {
							sb.append(
									"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='Teacancel(\""
											+ KKID + "\")'>取消</a></td>");
						} else if (status.equals("1")) {
							sb.append(
									"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >已取消</td>");
						}
						break;
					// 2即将上课
					case "2":
						sb.append(
								"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='TeashangK(\""
										+ KKID + "\")'>即将上课</a></td>");
						break;
					// 3正在上课
					case "3":
						sb.append(
								"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='TeashangK(\""
										+ KKID + "\")'>正在上课</a></td>");
						break;
					// 4上完课程
					case "4":
						sb.append(
								"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >正常结束</td>");
						break;

					// 5取消课程
					case "5":
						sb.append(
								"<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >已取消</td>");
						break;
					}
					sb.append("</tr>");
				}
				sb.append("</tbody></table>");
			}
			

			// 分页设置
			System.out.println(count);
			System.out.println(page);
			System.out.println(pagesize);

			// 上一页
			int lastPage = 0;
			// 下一页
			int nextPage = 0;

			lastPage = page - 1;

			nextPage = page + 1;

			if (lastPage < 1) {
				lastPage = 1;
			}

			if (nextPage > pagesize) {
				nextPage = pagesize;
			}

			int index = page;

			System.out.println(lastPage);
			System.out.println(nextPage);

			sb.append("<div style=\"clear: both;\"></div>");
			sb.append("<div class=\"main_article_nav\">");
			sb.append("<div class=\"main_article_nav_left2\" style=\"width:465px\">");
			sb.append("<ul>");
			sb.append("<li onclick=\"loadPage(1," + opType + ")\">首页</li>");
			sb.append("<li onclick=\"loadPage(" + lastPage + "," + opType + ")\">&lt;&lt;</li>");
			sb.append("<li class=\"now\" onclick=\"loadPage(" + page + "," + opType + ")\">" + page + "</li>");
			for (int i = 0; i < 4; i++) {
				index = index + 1;
				if (index <= pagesize) {
					sb.append("<li onclick=\"loadPage(" + index + "," + opType + ")\">" + index + "</li>");
				} else {
					break;
				}
			}
			sb.append("<li onclick=\"loadPage(" + nextPage + "," + opType + ")\">&gt;&gt;</li>");
			sb.append("<li onclick=\"loadPage(" + pagesize + "," + opType + ")\">尾页</li>");
			sb.append("</div>");
			sb.append("<div class=\"main_article_nav_right2\">第 <span>" + page + "</span>/<span>" + pagesize
					+ "</span> 页</div>");
			sb.append("</div>");

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

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return strRet;
	}

}
