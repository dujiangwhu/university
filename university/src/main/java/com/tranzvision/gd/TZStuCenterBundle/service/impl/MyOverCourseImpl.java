package com.tranzvision.gd.TZStuCenterBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPXBundle.dao.PkStuCourseChangeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseAnnexTMapper;
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
 * 学生门户 我的课程菜单，附件下载
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZStuCenterBundle.service.impl.MyOverCourseImpl")
public class MyOverCourseImpl extends FrameworkImpl {

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

			// 1预约课程 2正在上课 3上完课程 4即将开课 5取消课程 6 缺课
			String opType = "";

			if (jacksonUtil.containsKey("opType")) {
				opType = jacksonUtil.getString("opType");
			}

			if (opType == null || "".equals(opType)) {
				opType = request.getParameter("opType");
			}

			if (opType == null || "".equals(opType)) {
				opType = "1";
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
				classSelectHtml = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_GD_My_Over_COURSE", true, table,
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
				new Object[] { "TZ_LIMIT_MINUTE" }, "String");
		StringBuffer sb = new StringBuffer();
		Date now = new Date();
		String path = request.getContextPath();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String starTime = dateFormat.format(now);

		long currentTime = System.currentTimeMillis();

		// 加N小时
		currentTime += Integer.parseInt(limitHour) * 60 * 1000;

		Date date = new Date(currentTime);

		String endTime = dateFormat.format(date);

		List<Map<String, Object>> l = null;
		String sql = "";

		String annexSQL = "SELECT TZ_ATTACHFILE_NAME,TZ_ATT_A_URL,TZ_ATTACHSYSFILENA FROM PX_COURSE_ANNEX_T WHERE TZ_COURSE_ID=?";
		List<Map<String, Object>> annex = null;
		String TZ_ATTACHFILE_NAME = "";
		String TZ_ATT_A_URL = "";
		String TZ_ATTACHSYSFILENA = "";
		try {
			switch (opType) {
			// 1预约课程 2正在上课 3上完课程 4即将开课 5取消课程 6 缺课
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
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >下载</td>");
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
				String TZ_COURSE_ID = "";

				String flies = "";
				for (int i = 0; i < l.size(); i++) {
					flies = "";
					KCJB = (String) l.get(i).get("TZ_COURSE_TYPE_NAME");
					SKSJ = (String) l.get(i).get("TZ_CLASS_START_TIME");
					JSSJ = (String) l.get(i).get("TZ_CLASS_END_TIME");
					KEMC = (String) l.get(i).get("TZ_COURSE_NAME");
					SKJS = (String) l.get(i).get("TZ_REALNAME");
					status = (String) l.get(i).get("TZ_APP_STATUS");
					KKID = (String) l.get(i).get("TZ_SCHEDULE_ID");
					TZ_COURSE_ID = (String) l.get(i).get("TZ_COURSE_ID");
					sb.append("<tr>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + KCJB
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ SKSJ.substring(0, 16) + "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + KEMC
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + SKJS
							+ "</td>");

					// 获取文件名，文件路径

					annex = jdbcTemplate.queryForList(annexSQL, new Object[] { TZ_COURSE_ID });
					if (annex != null && annex.size() > 0) {
						for (int x = 0; x < annex.size(); x++) {
							TZ_ATTACHFILE_NAME = annex.get(i).get("TZ_ATTACHFILE_NAME").toString();
							TZ_ATT_A_URL = annex.get(i).get("TZ_ATT_A_URL").toString();
							TZ_ATTACHSYSFILENA = annex.get(i).get("TZ_ATTACHSYSFILENA").toString();

							if (TZ_ATT_A_URL.startsWith("/")) {
								TZ_ATT_A_URL = path + TZ_ATT_A_URL;
							} else {
								TZ_ATT_A_URL = path + "/" + TZ_ATT_A_URL;
							}

							if (TZ_ATT_A_URL.endsWith("/")) {
								TZ_ATT_A_URL = TZ_ATT_A_URL + TZ_ATTACHSYSFILENA;
							} else {
								TZ_ATT_A_URL = TZ_ATT_A_URL + "/" + TZ_ATTACHSYSFILENA;
							}
							if (x == 0) {
								flies = "<a href=\"" + TZ_ATT_A_URL + "\">" + TZ_ATTACHFILE_NAME + "</a>";
							} else {
								flies = flies + "&nbsp;&nbsp;,&nbsp;&nbsp;" + "<a href=\"" + TZ_ATT_A_URL + "\">"
										+ TZ_ATTACHFILE_NAME + "</a>";
							}

						}
					} else {
						flies = "没有课件";
					}

					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + flies
							+ "</td>");

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

}
