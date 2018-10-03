package com.tranzvision.gd.TZTeaCenterBundle.service.impl;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 学员评价管理
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZTeaCenterBundle.service.impl.TeaPLImpl")
public class TeaPLImpl extends FrameworkImpl {
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
			String page = jacksonUtil.getString("pageNo");
			return this.getTable(opType, oprid, page);
		} else {

			// 查询类型：0所有 评论 1 好评 2 中评 3
			String opType = "";

			if (jacksonUtil.containsKey("opType")) {
				opType = jacksonUtil.getString("opType");
			}

			if (opType == null || "".equals(opType)) {
				opType = request.getParameter("opType");
			}

			opType = "0";

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
				classSelectHtml = tzGDObject.getHTMLText("HTML.TZTeaCenterBundle.TZ_GD_TEA_PL", true, table, ZSGL_URL,
						strCssDir, "学员评价", str_jg_id, strSiteId, request.getContextPath());
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
	private String getTable(String opType, String oprid, String pageNo) {
		StringBuffer sb = new StringBuffer();
		// 查询类型：0所有 评论 1 好评 2 中评 3 差评

		List<Map<String, Object>> l = null;
		String sql = "";
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

			case "0":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETALLPL");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, beginH, pageLimit });
				
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETALLPLCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "Integer");
				break;
			case "1":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETPL");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "0", beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETPLCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, "0"  }, "Integer");
				break;
			case "2":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETPL");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "1", beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETPLCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, "1"  }, "Integer");
				break;
			case "3":
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETPL");
				l = jdbcTemplate.queryForList(sql, new Object[] { oprid, "2", beginH, pageLimit });
				sql = tzGDObject.getSQLText("SQL.TZTeaCenterBundle.TZ_GETPLCount");
				count = jdbcTemplate.queryForObject(sql, new Object[] { oprid, "2"  }, "Integer");
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
			sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >评论学生</td>");
			sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >评论类型</td>");
			sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >评论时间</td>");
			sb.append("<td valign=\"middle\" width=\"55%\" align=\"left\" style=\"padding-left:5px;\" >评论</td>");
			sb.append("</tr>");
			if (l == null || l.size() == 0) {
				sb.append("<tr>");
				sb.append(
						"<td colspan=\"4\" valign=\"middle\" width=\"100%\" align=\"left\" style=\"padding-left:5px;\" >没有数据</td>");
				sb.append("</tr>");
				sb.append("</tbody></table>");
			} else {
				String Name = "";
				String PLType = "";
				String PLDate = "";
				String pl = "";

				for (int i = 0; i < l.size(); i++) {
					Name = (String) l.get(i).get("TZ_REALNAME");
					PLType = (String) l.get(i).get("TZ_REVIEW_TYPE");
					// 0好评，1中评，2差评
					switch (PLType) {

					case "0":
						PLType = "好评";
						break;
					case "1":
						PLType = "中评";
						break;
					case "2":
						PLType = "差评";
						break;
					}

					PLDate = (String) l.get(i).get("TZ_REVIEW_TIME");
					pl = (String) l.get(i).get("TZ_REVIEW_DESC");
					sb.append("<tr>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >" + Name
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ PLType + "</td>");
					sb.append("<td valign=\"middle\" width=\"25%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ PLDate.substring(0, 16) + "</td>");
					sb.append("<td valign=\"middle\" width=\"55%\" align=\"left\" style=\"padding-left:5px;\" >" + pl
							+ "</td>");
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

}
