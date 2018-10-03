package com.tranzvision.gd.TZStuCenterBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPXBundle.dao.PkStuCourseChangeTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxStuFocusTeaTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxStuReviewTeaTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PkStuCourseChangeT;
import com.tranzvision.gd.TZPXBundle.model.PxStuFocusTeaT;
import com.tranzvision.gd.TZPXBundle.model.PxStuFocusTeaTKey;
import com.tranzvision.gd.TZPXBundle.model.PxStuReviewTeaT;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 我的教师
 * 
 * @author feifei
 *
 */
@Service("com.tranzvision.gd.TZStuCenterBundle.service.impl.MyTeaImpl")
public class MyTeaImpl extends FrameworkImpl {
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
	private PxStuFocusTeaTMapper pxStuFocusTeaTMapper;

	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PxTeacherMapper pxTeacherMapper;
	@Autowired
	private PxStuReviewTeaTMapper pxStuReviewTeaTMapper;

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

		if (htmlTpye == null || htmlTpye.equals("")) {
			htmlTpye = request.getParameter("htmlTpye");
		}

		if (htmlTpye != null && htmlTpye.equals("search")) {
			String strRet = "";
			String contextPath = request.getContextPath();
			// 查看教师信息和评论
			String tcOPRID = jacksonUtil.getString("tcOPRID");
			if (tcOPRID == null || tcOPRID.equals("")) {
				tcOPRID = request.getParameter("tcOPRID");
			}
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
						sb.append(
								"<p style=\"font-size:14px;padding:10px 0;line-height:180%;color:#333;white-space:pre-wrap;word-wrap:break-word; \">");
						sb.append(TZ_REVIEW_DESC);
						sb.append("</p>");
						sb.append("<div>");
						sb.append("<div style=\"float:left;color:#999\">");
						sb.append("<span>" + TZ_REVIEW_TIME + "</span>");
						sb.append("</div>");
						sb.append("</div>");
						sb.append("</div>");
						sb.append("</div>");
						sb.append("<br>");
						sb.append("<br>");
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
				try {
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
		} else if (htmlTpye != null && htmlTpye.equals("SearchFouse")) {
			String opType = jacksonUtil.getString("opType");
			String page = jacksonUtil.getString("pageNo");
			return this.getTable(opType, oprid, page);
		} else if (htmlTpye != null && htmlTpye.equals("PLHTML")) {
			String strRet = "";
			String tcOPRID = jacksonUtil.getString("tcOPRID");
			if (tcOPRID == null || tcOPRID.equals("")) {
				tcOPRID = request.getParameter("tcOPRID");
			}

			// 校验是否有权限评论，只有拥有课程的才可以评论
			String sql = "SELECT 'Y' FROM PX_STU_APP_COURSE_T A,PX_TEA_SCHEDULE_T B WHERE A.TZ_SCHEDULE_ID=B.TZ_SCHEDULE_ID AND (A.TZ_APP_STATUS='0' OR  A.TZ_APP_STATUS='2') AND (B.TZ_SCHEDULE_TYPE='0' OR B.TZ_SCHEDULE_TYPE='2') AND A.OPRID=? AND B.OPRID=?";

			String isY = jdbcTemplate.queryForObject(sql, new Object[] { oprid, tcOPRID }, "String");

			if (isY == null || !isY.equals("Y")) {
				strRet = "沒有权限评论该教师！";
			} else {
				// 统一URL
				String strUrl = request.getContextPath() + "/dispatcher";
				// 必填项的html标识
				String strBtHtml = "<span style=\"color:#F00\">*</span>";

				// 活动ID的隐藏域
				String str_items_html = "<input type=\"hidden\" id=\"tcOPRID\" name=\"tcOPRID\" value=\"" + tcOPRID
						+ "\"/>";

				String onlineApplyText = "评论老师";
				String timeOut = "服务端请求超时。";
				String serverError = "服务端请求发生错误。";
				String authCode = "验证码";
				String tipsMsg = "";
				String closeBtn = "关闭";
				String backBtn = "返回";
				String submitBtn = "提交";
				String requireTips = "带*号字段必须填写！";
				String changeAuthCode = "看不清楚？点击更换";

				StringBuffer sb = new StringBuffer();
				sb.append("<li>");
				sb.append("<strong>");
				sb.append("<span style=\"color:#F00\">*</span>评分：</strong>");
				sb.append("<select class=\"apply-items seltype\" id=\"pytype\" name=\"pytype\">");
				// 0好评，1中评，2差评
				sb.append("<option value=\"0\">好评</option>");
				sb.append("<option value=\"1\">中评</option>");
				sb.append("<option value=\"2\">差评</option>");
				sb.append("</select></li>");

				sb.append("<li>");
				sb.append("<strong>");
				sb.append("<span style=\"color:#F00\">*</span>评价：</strong>");
				sb.append("<textarea class=\"apply-items inptype\" style=\"height:100px\" id=\"desc\"></textarea>");
				sb.append("</li>");

				// 验证码
				str_items_html = str_items_html + sb.toString() + "<li><strong>" + strBtHtml + authCode + "：</strong>"
						+ "<input type=\"text\" class=\"apply-items inptype\" id=\"tz_regCode\" name=\"tz_regCode\" required style=\"width:120px\" />"
						+ "<img id=\"regCodeImg\" src=\"\" onclick=\"createCode()\" alt=\"" + changeAuthCode
						+ "\" style=\"height:37px; margin-left:20px; margin-right:10px; vertical-align:middle;\"/>"
						+ "</li>";

				try {
					strRet = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_APPLY_REG_FORM_HEAD", true,
							str_items_html, strUrl, "", "", timeOut, serverError, onlineApplyText, tipsMsg, closeBtn,
							backBtn, submitBtn, requireTips, request.getContextPath());
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return strRet;
		} else {

			// 查询类型：0所有查询 1预约课程 2正在上课 3上完课程 4即将开课 5取消课程
			String opType = "";

			if (jacksonUtil.containsKey("opType")) {
				opType = jacksonUtil.getString("opType");
			}

			if (opType == null || "".equals(opType)) {
				opType = request.getParameter("opType");
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

			String table = this.getTable("0", oprid, "1");
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			String classSelectHtml = "";
			try {
				classSelectHtml = tzGDObject.getHTMLText("HTML.TZStuCenterBundle.TZ_GD_MYTEA", true, table, ZSGL_URL,
						strCssDir, "我的老师", str_jg_id, strSiteId, request.getContextPath());
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

		List<Map<String, Object>> l = null;
		// String sql = "";
		StringBuffer sql = new StringBuffer();
		StringBuffer sb = new StringBuffer();

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
			// 0 上课老师 1。关注老师
			case "0":
				// sql = new StringBuffer();
				sql.append("SELECT C.NAME,C.SEX,C.AGE,C.SCHOOL_AGE,C.`LEVEL`,C.SCHOOL,C.EDUCATION_BG,C.OPRID ");
				sql.append("FROM PX_TEACHER_T C WHERE C.OPRID IN ");
				sql.append("(SELECT DISTINCT B.OPRID ");
				sql.append("FROM PX_STU_APP_COURSE_T A,PX_TEA_SCHEDULE_T B ");
				sql.append("WHERE A.TZ_SCHEDULE_ID =B.TZ_SCHEDULE_ID AND A.OPRID=? and A.TZ_APP_STATUS!=1) limit ?,?");
				l = jdbcTemplate.queryForList(sql.toString(), new Object[] { oprid, beginH, pageLimit });

				sql = new StringBuffer();
				sql.append("SELECT count(C.NAME) ");
				sql.append("FROM PX_TEACHER_T C WHERE C.OPRID IN ");
				sql.append("(SELECT DISTINCT B.OPRID ");
				sql.append("FROM PX_STU_APP_COURSE_T A,PX_TEA_SCHEDULE_T B ");
				sql.append("WHERE A.TZ_SCHEDULE_ID =B.TZ_SCHEDULE_ID AND A.OPRID=? and A.TZ_APP_STATUS!=1) ");

				count = jdbcTemplate.queryForObject(sql.toString(), new Object[] { oprid }, "Integer");
				break;
			// 关注老师
			case "1":
				// sql = new StringBuffer();
				sql.append("SELECT C.NAME,C.SEX,C.AGE,C.SCHOOL_AGE,C.`LEVEL`,C.SCHOOL,C.EDUCATION_BG,C.OPRID ");
				sql.append("FROM PX_TEACHER_T C WHERE C.OPRID IN ");
				sql.append("(SELECT  B.TEA_OPRID ");
				sql.append("FROM PX_STU_FOCUS_TEA_T B ");
				sql.append("WHERE B.STU_OPRID=?)  limit ?,?");
				l = jdbcTemplate.queryForList(sql.toString(), new Object[] { oprid, beginH, pageLimit });

				sql = new StringBuffer();
				sql.append("SELECT count(C.NAME) ");
				sql.append("FROM PX_TEACHER_T C WHERE C.OPRID IN ");
				sql.append("(SELECT  B.TEA_OPRID ");
				sql.append("FROM PX_STU_FOCUS_TEA_T B ");
				sql.append("WHERE B.STU_OPRID=?) ");

				count = jdbcTemplate.queryForObject(sql.toString(), new Object[] { oprid }, "Integer");
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
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >授课教师</td>");
			sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >性别</td>");
			sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >年龄</td>");
			sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >教龄</td>");
			sb.append("<td valign=\"middle\" width=\"30%\" align=\"left\" style=\"padding-left:5px;\" >毕业院校</td>");
			sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >操作</td>");
			sb.append("</tr>");
			if (l == null || l.size() == 0) {
				sb.append("<tr>");
				sb.append(
						"<td colspan=\"6\" valign=\"middle\" width=\"100%\" align=\"left\" style=\"padding-left:5px;\" >没有数据</td>");
				sb.append("</tr>");
				sb.append("</tbody></table>");
			} else {
				String NAME = "";
				String SEX = "";
				String SCHOOL_AGE = "";
				String AGE = "";
				String SCHOOL = "";
				String tcOPRID = "";
				for (int i = 0; i < l.size(); i++) {
					NAME = (String) l.get(i).get("NAME");
					SEX = (String) l.get(i).get("SEX");

					if (SEX != null) {
						if (SEX.equals("M")) {
							SEX = "男";
						} else {
							SEX = "女";
						}
					}
					SCHOOL_AGE = l.get(i).get("SCHOOL_AGE").toString();
					AGE = l.get(i).get("AGE").toString();
					SCHOOL = (String) l.get(i).get("SCHOOL");
					tcOPRID = (String) l.get(i).get("OPRID");
					sb.append("<tr>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + NAME
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >" + SEX
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >" + AGE
							+ "年</td>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ SCHOOL_AGE + "年</td>");

					sb.append("<td valign=\"middle\" width=\"30%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ SCHOOL + "</td>");

					switch (opType) {
					// 0 上课老师 1。关注老师
					case "0":
						sb.append(
								"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='showT(\""
										+ tcOPRID
										+ "\")'>查看</a>&nbsp;&nbsp;&nbsp;<a href=\"javaScript: void(0)\" onClick='focusT(\""
										+ tcOPRID
										+ "\")'>关注</a>&nbsp;&nbsp;&nbsp;<a href=\"javaScript: void(0)\" onClick='PL(\""
										+ tcOPRID + "\")'>评论</a></td>");

						break;
					case "1":
						sb.append(
								"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='showT(\""
										+ tcOPRID
										+ "\")'>查看</a>&nbsp;&nbsp;&nbsp;<a href=\"javaScript: void(0)\" onClick='focusTcancel(\""
										+ tcOPRID + "\")'>取消关注</a></td>");
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

			if ("focusT".equals(oprType)) {
				String tcOPRID = jacksonUtil.getString("tcOPRID");
				tcOPRID = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(tcOPRID);
				// 关注教师
				PxStuFocusTeaTKey key = new PxStuFocusTeaTKey();
				key.setStuOprid(oprid);
				key.setTeaOprid(tcOPRID);
				PxStuFocusTeaT pxStuFocusTeaT = pxStuFocusTeaTMapper.selectByPrimaryKey(key);
				if (pxStuFocusTeaT == null) {
					pxStuFocusTeaT = new PxStuFocusTeaT();
					pxStuFocusTeaT.setRowLastmantDttm(new Date());
					pxStuFocusTeaT.setRowLastmantOprid(oprid);
					pxStuFocusTeaT.setStuOprid(oprid);
					pxStuFocusTeaT.setTeaOprid(tcOPRID);
					pxStuFocusTeaT.setTzFocusTime(new Date());
					pxStuFocusTeaTMapper.insert(pxStuFocusTeaT);
				}

				strRet = "{\"fouseRs\":\"关注成功\"}";
			} else if ("focusTcancle".equals(oprType)) {
				String tcOPRID = jacksonUtil.getString("tcOPRID");
				tcOPRID = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(tcOPRID);
				PxStuFocusTeaTKey key = new PxStuFocusTeaTKey();
				key.setStuOprid(oprid);
				key.setTeaOprid(tcOPRID);
				pxStuFocusTeaTMapper.deleteByPrimaryKey(key);

				strRet = "{\"cancelRs\":\"取消关注成功\"}";

			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return strRet;
	}

	/*********************************************************************************
	 * 评论老师
	 *********************************************************************************/
	@Override
	public String tzGetJsonData(String strParams) {

		String strRet = "";
		String strResult = "";
		String strResultMsg = "";

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			// 评价类型
			String PJTYPE = jacksonUtil.getString("PJTYPE");
			// 评论
			String PJDESC = jacksonUtil.getString("PJDESC");
			// 教师ID
			String TOPRID = jacksonUtil.getString("TOPRID");

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			// 验证码
			String strAuthCode = jacksonUtil.getString("CODE");

			PJTYPE = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(PJTYPE);
			PJDESC = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(PJDESC);
			TOPRID = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(TOPRID);
			strAuthCode = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(strAuthCode);

			String authCodeError = "您输入的验证码有误！";

			// 校验验证码
			Patchca patchca = new Patchca();
			if (!patchca.verifyToken(request, strAuthCode)) {
				strResult = "1";
				strResultMsg = authCodeError;
			} else {
				// 校验是否有权限评论，只有拥有课程的才可以评论
				String sql = "SELECT 'Y' FROM PX_STU_APP_COURSE_T A,PX_TEA_SCHEDULE_T B WHERE A.TZ_SCHEDULE_ID=B.TZ_SCHEDULE_ID AND (A.TZ_APP_STATUS='0' OR  A.TZ_APP_STATUS='2') AND (B.TZ_SCHEDULE_TYPE='0' OR B.TZ_SCHEDULE_TYPE='2') AND A.OPRID=? AND B.OPRID=?";

				String isY = jdbcTemplate.queryForObject(sql, new Object[] { oprid, TOPRID }, "String");

				if (isY == null || !isY.equals("Y")) {
					strResult = "1";
					strResultMsg = "沒有权限评论该教师！";
				} else {

					PxStuReviewTeaT pxStuReviewTeaT = new PxStuReviewTeaT();
					pxStuReviewTeaT.setTzReviewId("" + getSeqNum.getSeqNum("PX_STU_REVIEW_TEA_T", "TZ_REVIEW_ID"));
					pxStuReviewTeaT.setRowLastmantDttm(new Date());
					pxStuReviewTeaT.setRowLastmantOprid(oprid);
					pxStuReviewTeaT.setTeaOprid(TOPRID);
					pxStuReviewTeaT.setStuOprid(oprid);
					pxStuReviewTeaT.setTzReviewDesc(PJDESC);
					pxStuReviewTeaT.setTzReviewStatus("0");
					pxStuReviewTeaT.setTzReviewTime(new Date());
					pxStuReviewTeaT.setTzReviewType(PJTYPE);

					pxStuReviewTeaTMapper.insert(pxStuReviewTeaT);
					strResult = "0";
					strResultMsg = "评论成功！";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			strResult = "1";
			strResultMsg = e.getMessage();
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", strResult);
		mapRet.put("resultDesc", strResultMsg);

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;

	}

}
