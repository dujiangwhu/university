package com.tranzvision.gd.TZStuCenterBundle.service.impl;

import java.util.Date;
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
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
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

	private PxTeacherMapper pxTeacherMapper;
	
	private PxStuReviewTeaTMapper pxStuReviewTeaTMapper;

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

		String table = this.getTable(opType, oprid);
		// 通用链接;
		String ZSGL_URL = request.getContextPath() + "/dispatcher";
		String classSelectHtml = "";
		try {
			classSelectHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_My_COURSE", true, table,
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

	/**
	 * 描述 预约课程表格
	 * 
	 * @param opType
	 * @param oprid
	 * @return
	 */
	private String getTable(String opType, String oprid) {

		List<Map<String, Object>> l = null;
		// String sql = "";
		StringBuffer sql = null;
		StringBuffer sb = new StringBuffer();
		try {
			switch (opType) {
			// 0 上课老师 1。关注老师
			case "0":
				sql = new StringBuffer();
				sql.append("SELECT C.NAME,C.SEX,C.AGE,C.SCHOOL_AGE,C.`LEVEL`,C.SCHOOL,C.EDUCATION_BG,C.OPRID ");
				sql.append("FROM PX_TEACHER_T C WHERE C.OPRID IN ");
				sql.append("(SELECT DISTINCT B.OPRID ");
				sql.append("FROM PX_STU_APP_COURSE_T A,PX_TEA_SCHEDULE_T B ");
				sql.append("WHERE A.TZ_SCHEDULE_ID =B.TZ_SCHEDULE_ID AND A.OPRID=?) ");
				l = jdbcTemplate.queryForList(sql.toString(), new Object[] { oprid });
				break;
			// 关注老师
			case "1":
				sql = new StringBuffer();
				sql.append("SELECT C.NAME,C.SEX,C.AGE,C.SCHOOL_AGE,C.`LEVEL`,C.SCHOOL,C.EDUCATION_BG,C.OPRID ");
				sql.append("FROM PX_TEACHER_T C WHERE C.OPRID IN ");
				sql.append("(SELECT  B.TEA_OPRID ");
				sql.append("FROM PX_STU_FOCUS_TEA_T B ");
				sql.append("WHERE B.STU_OPRID=?) ");
				l = jdbcTemplate.queryForList(sql.toString(), new Object[] { oprid });
				break;
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
					SCHOOL_AGE = (String) l.get(i).get("SCHOOL_AGE");
					AGE = (String) l.get(i).get("AGE");
					SCHOOL = (String) l.get(i).get("SCHOOL");
					tcOPRID = (String) l.get(i).get("tcOPRID");
					sb.append("<tr>");
					sb.append("<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" >" + NAME
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >" + SEX
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >" + AGE
							+ "</td>");
					sb.append("<td valign=\"middle\" width=\"10%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ SCHOOL_AGE + "</td>");

					sb.append("<td valign=\"middle\" width=\"30%\" align=\"left\" style=\"padding-left:5px;\" >"
							+ SCHOOL + "</td>");

					switch (opType) {
					// 0 上课老师 1。关注老师
					case "0":
						sb.append(
								"<td valign=\"middle\" width=\"20%\" align=\"left\" style=\"padding-left:5px;\" ><a href=\"javaScript: void(0)\" onClick='showT(\""
										+ tcOPRID
										+ "\")'>查看</a>&nbsp;&nbsp;&nbsp;<a href=\"javaScript: void(0)\" onClick='focusT(\""
										+ tcOPRID + "\")'>关注</a></td>");

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
				String contextPath = request.getContextPath();
				// 查看教师信息和评论
				String tcOPRID = jacksonUtil.getString("tcOPRID");
				// 教师信息
				PxTeacher pxTeacher = pxTeacherMapper.selectByPrimaryKey(tcOPRID);
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
						"SELECT TZ_REVIEW_TYPE,TZ_REVIEW_DESC FROM PX_STU_REVIEW_TEA_T WHERE TEA_OPRID=? AND TZ_REVIEW_STATUS=0 ",
						new Object[] { tcOPRID });

				// 载入页面

			} else if ("focusT".equals(oprType)) {
				String tcOPRID = jacksonUtil.getString("tcOPRID");
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
				
				strRet = "{\"success\":\"关注成功\"}";
			} else if ("focusTcancle".equals(oprType)) {
				String tcOPRID = jacksonUtil.getString("tcOPRID");
				PxStuFocusTeaTKey key = new PxStuFocusTeaTKey();
				key.setStuOprid(oprid);
				key.setTeaOprid(tcOPRID);
				pxStuFocusTeaTMapper.deleteByPrimaryKey(key);
				
				strRet = "{\"success\":\"取消关注成功\"}";
			}else if ("reviewT".equals(oprType)) {
				String tcOPRID = jacksonUtil.getString("tcOPRID");
				String type = jacksonUtil.getString("type");
				String DESC = jacksonUtil.getString("DESC");
				
				PxStuReviewTeaT pxStuReviewTeaT = new PxStuReviewTeaT();
				pxStuReviewTeaT.setTzReviewId("" + getSeqNum.getSeqNum("PX_STU_REVIEW_TEA_T", "TZ_REVIEW_ID"));
				pxStuReviewTeaT.setRowLastmantDttm(new Date());
				pxStuReviewTeaT.setRowLastmantOprid(oprid);
				pxStuReviewTeaT.setTeaOprid(tcOPRID);
				pxStuReviewTeaT.setStuOprid(oprid);
				pxStuReviewTeaT.setTzReviewDesc(DESC);
				pxStuReviewTeaT.setTzReviewStatus("0");
				pxStuReviewTeaT.setTzReviewTime(new Date());
				pxStuReviewTeaT.setTzReviewType(type);
				
				pxStuReviewTeaTMapper.insert(pxStuReviewTeaT);
				strRet = "{\"success\":\"评论成功\"}";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return strRet;
	}

}
