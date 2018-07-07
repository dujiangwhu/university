package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.AnalysisSysVar;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * PS:TZ_GD_BJXZQ_APP:ClassApplication
 * 
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.ViewApplicationHistoryServiceImpl")
public class ViewApplicationHistoryServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
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

	/****** 班级选择器 ********/
	@Override
	public String tzGetHtmlContent(String strParams) {
		String applicationCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			// String oprate = "";
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}
			// if (jacksonUtil.containsKey("oprate")) {
			// oprate = jacksonUtil.getString("oprate");
			// }

			if (strSiteId == null || "".equals(strSiteId)) {
				strSiteId = request.getParameter("siteId");
			}
			// 查询报名中心显示规则 ,A: 为显示所有开放的班级，B:只显示报名人已经报名的班级； 无值当 A 处理;
			String bmClassShow = jdbcTemplate.queryForObject(
					"select TZ_BM_CLASS_SHOW from PS_SITE_BMCLASS_SZ_T where TZ_SITEI_ID=?", new Object[] { strSiteId },
					"String");
			if (bmClassShow == null || "".equals(bmClassShow)) {
				bmClassShow = "A";
			}

			// if (strSiteId == null || "".equals(strSiteId)) {
			// oprate = request.getParameter("oprate");
			// }

			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";
			String strCssDir = "";
			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				String skinstor = (String) siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
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

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}

			// 报名中心;
			String ApplicationCenter = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "101",
					language, "报名中心", "Application Center");

			// 步骤;
			String step = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "104", language, "步骤",
					"step");
			// 报名状态;
			String ApplicationStatus = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "105",
					language, "报名状态", "Application Status");
			// 查看;
			String view = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "106", language, "查看",
					"view");
			// 报名表提交状态;
			String bmbTjZt = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "107", language,
					"申请表", "Apply Form");
			// 资料审核情况;
			String zlshQk = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "108", language,
					"材料清单", "Doc(s) List");
			// 推荐信信息;
			String tjxxx = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "109", language,
					"推荐信", "Refs");
			// 打印;
			String print = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "110", language, "打印",
					"print");
			
			//历史报名
			String historyDesc =  messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "lsbmjl",
					language, "历史报名记录", "历史报名记录");

			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";

			String classId = "", className = "", classDesc = "";
			// 报名开始时间,报名结束时间;
			// String applyStartDt = "", applyEndDt = "";
			// 显示的流程;
			String isSetpDisplay = "none";
			boolean isShow = false;
			// 流程展开或收缩的css;
			String expandOrShrink = "application_expand";
			String expandOrShrinkActive = "";
			// 流程展示;
			String TZ_SQLC_TABLE = "";

			// 查看已经报名的班级总数;
			int applyNum = 0;
			// 班级使用的报名表模板;
			String TZ_APP_MODAL_ID = "";

			String applyNumSQL = "select count(1) from PS_TZ_FORM_WRK_T a, PS_TZ_CLASS_INF_T b where a.OPRID=? and a.TZ_CLASS_ID=b.TZ_CLASS_ID and b.TZ_PRJ_ID in (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) and b.TZ_JG_ID=?";
			applyNum = jdbcTemplate.queryForObject(applyNumSQL, new Object[] { oprid,strSiteId, str_jg_id },
					"Integer");

			// 显示历史报名;
			String sql = "select a.TZ_APP_INS_ID,b.TZ_CLASS_ID,b.TZ_CLASS_NAME,DATE_FORMAT(b.TZ_APP_START_DT,'%Y/%m/%d') TZ_APP_START_DT,DATE_FORMAT(b.TZ_APP_END_DT,'%Y/%m/%d') TZ_APP_END_DT,b.TZ_CLASS_DESC,b.TZ_APP_MODAL_ID  from PS_TZ_FORM_WRK_T a, PS_TZ_CLASS_INF_T b where a.OPRID=? and a.TZ_CLASS_ID=b.TZ_CLASS_ID and b.TZ_PRJ_ID in (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) and b.TZ_JG_ID=? order by a.ROW_LASTMANT_DTTM desc";
			List<Map<String, Object>> classList = jdbcTemplate.queryForList(sql,
					new Object[] { oprid, strSiteId, str_jg_id });
			long TZ_APP_INS_ID = 0;
			if (classList != null && classList.size() > 0) {
				for (int i = 0; i < classList.size(); i++) {
					classId = (String) classList.get(i).get("TZ_CLASS_ID");
					className = (String) classList.get(i).get("TZ_CLASS_NAME");
					if (className == null) {
						className = "";
					}
					TZ_APP_INS_ID = Long.parseLong(String.valueOf(classList.get(i).get("TZ_APP_INS_ID")));
					// applyStartDt = (String)
					// classList.get(i).get("TZ_APP_START_DT");
					// applyEndDt = (String)
					// classList.get(i).get("TZ_APP_END_DT");
					classDesc = (String) classList.get(i).get("TZ_CLASS_DESC");
					if (classDesc == null) {
						classDesc = "";
					}
					TZ_APP_MODAL_ID = (String) classList.get(i).get("TZ_APP_MODAL_ID");

					// 报名表链接;
					String applyFromUrl = ZSGL_URL + "?classid=appId&TZ_CLASS_ID=" + classId + "&SITE_ID=" + strSiteId;

					// String appFormHtml = "";

					// 打印报名表链接;
					/*
					 * String applyFormPrint = ZSGL_URL +
					 * "?tzParams={\"ComID\":\"TZ_ONLINE_REG_COM\",\"PageID\":\"TZ_PRINT_ADMIN_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"appInsID\":\""
					 * + TZ_APP_INS_ID + "\"}}";
					 */

					String applyFormPrint = request.getContextPath() + "/PrintPdfServlet?instanceID=" + TZ_APP_INS_ID;

					// 递交资料模型实例是否存在;
					int djzlTotalNum = jdbcTemplate.queryForObject(
							"select count(1) from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=?", new Object[] { classId },
							"Integer");

					// 报名表中是否有推荐信控件;
					int tjxkjNum = jdbcTemplate.queryForObject(
							"SELECT count(1) FROM PS_TZ_APP_XXXPZ_T A WHERE TZ_APP_TPL_ID=? and TZ_COM_LMC='recommendletter'",
							new Object[] { TZ_APP_MODAL_ID }, "Integer");

					// 报名流程模型实例是否存在;
					int bmlcTotalNum = jdbcTemplate.queryForObject(
							"select count(1) from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?", new Object[] { classId },
							"Integer");

					// String bmlcZtHtml = "";
					int TZ_SORT_NUM = 0;
					String TZ_APPPRO_ID = "", TZ_APPPRO_NAME = "", TZ_APPPRO_HF_BH = "", TZ_APPPRO_RST = "";

					// 报名流程step HTML;
					String TZ_BJ_BM_LC_STEP_DIV = "";
					String bmlcStepClass = "";

					// 查询面试申请号;
					String mssqhId = jdbcTemplate.queryForObject(
							"select TZ_MSH_ID from PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=?",
							new Object[] { TZ_APP_INS_ID }, "String");
					if (mssqhId != null && !"".equals(mssqhId)) {
						mssqhId = "<br>面试申请号:" + mssqhId;
					} else {
						mssqhId = "";
					}

					// 查看当前人员报名表的状态;
					String TZ_APP_FORM_STA = jdbcTemplate.queryForObject(
							"select TZ_APP_FORM_STA from PS_TZ_APP_INS_T where TZ_APP_INS_ID=?",
							new Object[] { TZ_APP_INS_ID }, "String");

					// 显示的描述值;
					String appFormStatusDesc = "";
					if ("ZHS".equals(language)) {
						appFormStatusDesc = jdbcTemplate.queryForObject(
								"SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_APPFORM_STATE' AND TZ_ZHZ_ID=?",
								new Object[] { TZ_APP_FORM_STA }, "String");
					} else {
						appFormStatusDesc = jdbcTemplate.queryForObject(
								"SELECT COALESCE(B.TZ_ZHZ_DMS,A.TZ_ZHZ_DMS)  TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (SELECT * FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_LANGUAGE_ID=?) B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID=B.TZ_ZHZ_ID WHERE A.TZ_ZHZJH_ID='TZ_APPFORM_STATE' AND A.TZ_ZHZ_ID=?",
								new Object[] { language, TZ_APP_FORM_STA }, "String");
					}
					if (appFormStatusDesc == null) {
						appFormStatusDesc = "";
					}

					if (applyNum > 0 && isShow == false && bmlcTotalNum > 0) {
						isShow = true;
						isSetpDisplay = "block";
					}

					if ("block".equals(isSetpDisplay)) {
						expandOrShrink = "application_shrink";
						expandOrShrinkActive = "";
					} else {
						expandOrShrink = "application_expand";
						expandOrShrinkActive = "active";
					}
					
					// 循环报名流程;
					if (bmlcTotalNum > 0) {
						TZ_SORT_NUM = 0;
						// String bmlcSql = "select
						// a.TZ_APPPRO_ID,a.TZ_APPPRO_NAME,b.TZ_APPPRO_HF_BH,b.TZ_APPPRO_RST
						// from PS_TZ_CLS_BMLC_T a left join (select * from
						// PS_TZ_APPPRO_RST_T where TZ_APP_INS_ID=? and
						// TZ_CLASS_ID=?) b on a.TZ_APPPRO_ID=b.TZ_APPPRO_ID
						// where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
						String bmlcSql = "select a.TZ_APPPRO_ID,a.TZ_APPPRO_NAME from PS_TZ_CLS_BMLC_T a where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
						List<Map<String, Object>> bmlcList = jdbcTemplate.queryForList(bmlcSql,
								new Object[] { classId });
						if (bmlcList != null && bmlcList.size() > 0) {
							for (int j = 0; j < bmlcList.size(); j++) {
								TZ_APPPRO_ID = (String) bmlcList.get(j).get("TZ_APPPRO_ID");
								TZ_APPPRO_NAME = (String) bmlcList.get(j).get("TZ_APPPRO_NAME");

								// 是否发布;
								TZ_APPPRO_RST = "";
								String isFb = "";
								Map<String, Object> map = jdbcTemplate.queryForMap(
										"select TZ_SYSVAR,TZ_APPPRO_CONTENT from PS_TZ_CLS_BMLCHF_T where TZ_WFB_DEFALT_BZ='on' and TZ_CLASS_ID=? and TZ_APPPRO_ID=?",
										new Object[] { classId, TZ_APPPRO_ID });
								if (map != null) {
									String sysvarId = map.get("TZ_SYSVAR") == null ? "" : (String) map.get("TZ_SYSVAR");

									if (!"".equals(sysvarId)) {
										try {
											String[] sysVarParam = { String.valueOf(TZ_APP_INS_ID), sysvarId,"N",strSiteId };
											AnalysisSysVar analysisSysVar = new AnalysisSysVar();
											analysisSysVar.setM_SysVarID(sysvarId);
											analysisSysVar.setM_SysVarParam(sysVarParam);
											Object obj = analysisSysVar.GetVarValue();
											if (obj != null && !"".equals(obj)) {
												String[] sysVarList = (String[]) obj;
												isFb = sysVarList[0];
												TZ_APPPRO_RST = sysVarList[1];
											}
										} catch (Exception e) {
											e.printStackTrace();
											TZ_APPPRO_RST = "";
										}
									}

									if (TZ_APPPRO_RST == null || "".equals(TZ_APPPRO_RST)) {
										TZ_APPPRO_RST = map.get("TZ_APPPRO_CONTENT") == null ? ""
												: (String) map.get("TZ_APPPRO_CONTENT");
									}
								}
								TZ_SORT_NUM = TZ_SORT_NUM + 1;
								String timelineClass = "";
								if ("Y".equals(isFb)) {
									timelineClass = "timeline-yuan2_i";
								} else {
									timelineClass = "timeline-yuan1_i";
								}
								/*
								 * TZ_APPPRO_HF_BH = (String)
								 * bmlcList.get(j).get("TZ_APPPRO_HF_BH");
								 * TZ_APPPRO_RST = (String)
								 * bmlcList.get(j).get("TZ_APPPRO_RST");
								 * if(TZ_APPPRO_NAME == null){ TZ_APPPRO_NAME =
								 * ""; } if(TZ_APPPRO_HF_BH == null){
								 * TZ_APPPRO_HF_BH = ""; } if(TZ_APPPRO_RST ==
								 * null){ TZ_APPPRO_RST = ""; }
								 * 
								 * TZ_SORT_NUM = TZ_SORT_NUM + 1; String
								 * timelineClass = "timeline-yuan2_i"; if
								 * (TZ_APPPRO_HF_BH == null ||
								 * "".equals(TZ_APPPRO_HF_BH)) { TZ_APPPRO_RST =
								 * jdbcTemplate.queryForObject(
								 * "select TZ_APPPRO_CONTENT from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=? and TZ_WFB_DEFALT_BZ='on'"
								 * , new Object[] { classId, TZ_APPPRO_ID },
								 * "String"); if(TZ_APPPRO_RST == null){
								 * TZ_APPPRO_RST = ""; } timelineClass =
								 * "timeline-yuan1_i"; } else { //
								 * 如果有值，查看是不是默认回复语; String isDef =
								 * jdbcTemplate.queryForObject(
								 * "select 'Y' from PS_TZ_CLS_BMLCHF_T where TZ_WFB_DEFALT_BZ='on' and TZ_CLASS_ID=? and TZ_APPPRO_HF_BH=?"
								 * , new Object[] { classId, TZ_APPPRO_HF_BH },
								 * "String"); if ("Y".equals(isDef)) {
								 * timelineClass = "timeline-yuan1_i"; }
								 * 
								 * }
								 */
								if ((TZ_SORT_NUM % 2) == 1) {
									bmlcStepClass = "indexbm-lc-step1";
								} else {
									bmlcStepClass = "indexbm-lc-step2";
								}

								TZ_BJ_BM_LC_STEP_DIV = TZ_BJ_BM_LC_STEP_DIV + tzGDObject.getHTMLText(
										"HTML.TZApplicationCenterBundle.TZ_BJ_BMSQ_LC_STEP_DIV", bmlcStepClass,
										step + "&nbsp;" + TZ_SORT_NUM, TZ_APPPRO_NAME, TZ_APPPRO_RST, timelineClass);

							}
						}
					}

					// 报名状态下需要分成几列;
					int colspan = 1;
					// 未提交时资料\推荐信显示的文字;
					String unsubmitWz = "N/A";
					// 报名状态下几列的width;
					int bmWidthNum = 70;
					if (djzlTotalNum > 0) {
						colspan = colspan + 1;
					}
					if (tjxkjNum > 0) {
						colspan = colspan + 1;
					}

					switch (colspan) {
					case 1:
						// bmWidthNum = bmWidthNum;
						break;
					case 2:
						bmWidthNum = bmWidthNum / 2;
						break;
					case 3:
						bmWidthNum = 23;
						break;
					}

					String bmWidth = String.valueOf(bmWidthNum) + "%";
					// 报名状态td;
					String bmztTd = "";
					// 报名状态下显示查看，还是查看和打印的html;
					String bmztTd_A = "";
					String print_A = "";
					// 资料递交td;
					String zltjTd = "";
					// 推荐信td;
					String tjxTd = "";

					// 未提交报名表;
					if (!"U".equals(TZ_APP_FORM_STA)) {
						bmztTd_A = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_STATUS_SHOW_A", view,
								applyFromUrl);
						bmztTd = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_STATUS_SHOW_TD",
								bmWidth, bmbTjZt, appFormStatusDesc, bmztTd_A);
						// 存在资料模型;
						if (djzlTotalNum > 0) {
							zltjTd = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_ZLDJ_USHOW_TD",
									bmWidth, zlshQk, unsubmitWz);
						}

						if (tjxkjNum > 0) {
							// 报名人填写的推荐人数;
							int totalTjxNum = jdbcTemplate.queryForObject(
									"select COUNT(1) from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_MBA_TJX_YX='Y'",
									new Object[] { TZ_APP_INS_ID }, "Integer");
							if (totalTjxNum > 0) {
								// 推荐人已经提交推荐信的人数;
								int totalTjxTjNum = jdbcTemplate.queryForObject(
										"SELECT COUNT(1) FROM PS_TZ_KS_TJX_TBL A WHERE ((A.ATTACHSYSFILENAME <> '' AND A.ATTACHUSERFILE <> '' AND A.ATTACHSYSFILENAME IS NOT NULL AND A.ATTACHUSERFILE IS NOT NULL) OR EXISTS (SELECT 'Y' FROM PS_TZ_APP_INS_T B WHERE A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U')) AND A.TZ_APP_INS_ID = ? and A.TZ_MBA_TJX_YX='Y'",
										new Object[] { TZ_APP_INS_ID }, "Integer");
								// 查看推荐信信息页面超链接;
								String openTjx_A = tzGDObject.getHTMLText(
										"HTML.TZApplicationCenterBundle.TZ_OPEN_WINDOW_VIEW_A", view, classId,
										String.valueOf(TZ_APP_INS_ID), language, "TJX");
								if (colspan == 3) {
									tjxTd = tzGDObject.getHTMLText(
											"HTML.TZApplicationCenterBundle.TZ_GD_BM_TJX_SHOW_TD", "24%", tjxxx,
											"推荐信:" + totalTjxTjNum + "/" + totalTjxNum, openTjx_A);
								} else {
									tjxTd = tzGDObject.getHTMLText(
											"HTML.TZApplicationCenterBundle.TZ_GD_BM_TJX_SHOW_TD", bmWidth, tjxxx,
											"推荐信:" + totalTjxTjNum + "/" + totalTjxNum, openTjx_A);
								}
							} else {
								if (colspan == 3) {
									tjxTd = tzGDObject.getHTMLText(
											"HTML.TZApplicationCenterBundle.TZ_GD_BM_TJX_USHOW_TD", "24%", tjxxx,
											"推荐信:" + unsubmitWz);
								} else {
									tjxTd = tzGDObject.getHTMLText(
											"HTML.TZApplicationCenterBundle.TZ_GD_BM_TJX_USHOW_TD", bmWidth, tjxxx,
											"推荐信:" + unsubmitWz);
								}
							}
						}
					} else {
						// 已提交报名表;
						bmztTd_A = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_STATUS_SHOW_A", view,
								applyFromUrl);
						print_A = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_PRINT_A", print,
								applyFormPrint);
						bmztTd = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_STATUS_SHOW_TD",
								bmWidth, bmbTjZt, appFormStatusDesc, bmztTd_A + "&nbsp;&nbsp;" + print_A);

						if (djzlTotalNum > 0) {
							int num = 0;
							String zlSpZt = "";

							num = jdbcTemplate.queryForObject(
									"select count(1) from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=? and TZ_SBMINF_ID not in (select TZ_SBMINF_ID from PS_TZ_FORM_ZLSH_T where TZ_APP_INS_ID=?)",
									new Object[] { classId, TZ_APP_INS_ID }, "Integer");
							if (num == 0) {
								num = jdbcTemplate.queryForObject(
										"select count(1) from PS_TZ_FORM_ZLSH_T where TZ_APP_INS_ID=? and TZ_ZL_AUDIT_STATUS='B' and TZ_SBMINF_ID in (select TZ_SBMINF_ID from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=?)",
										new Object[] { TZ_APP_INS_ID, classId }, "Integer");
								if (num == djzlTotalNum) {
									zlSpZt = "B";
								} else {
									num = jdbcTemplate.queryForObject(
											"select count(1) from PS_TZ_FORM_ZLSH_T where TZ_APP_INS_ID=? and TZ_ZL_AUDIT_STATUS='C' and TZ_SBMINF_ID in (select TZ_SBMINF_ID from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=?)",
											new Object[] { TZ_APP_INS_ID, classId }, "Integer");
									if (num > 0) {
										zlSpZt = "C";
									} else {
										zlSpZt = "A";
									}
								}
							} else {
								zlSpZt = "A";
							}

							// 查找审核状态描述值;
							String zlSpZtDesc = "";
							if ("ZHS".equals(language)) {
								zlSpZtDesc = jdbcTemplate.queryForObject(
										"SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BMB_DJWJSPZT' AND TZ_ZHZ_ID=?",
										new Object[] { zlSpZt }, "String");
							} else {
								zlSpZtDesc = jdbcTemplate.queryForObject(
										"SELECT COALESCE(B.TZ_ZHZ_DMS,A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (SELECT * FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_LANGUAGE_ID=?) B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID=B.TZ_ZHZ_ID WHERE A.TZ_ZHZJH_ID='TZ_BMB_DJWJSPZT' AND A.TZ_ZHZ_ID=?",
										new Object[] { language, zlSpZt }, "String");
							}

							// 查看资料进度超链接;
							String openZlspjd_A = tzGDObject.getHTMLText(
									"HTML.TZApplicationCenterBundle.TZ_OPEN_WINDOW_VIEW_A", view, classId,
									String.valueOf(TZ_APP_INS_ID), language, "DJZL");
							zltjTd = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_ZLDJ_SHOW_TD",
									bmWidth, zlshQk, zlSpZtDesc, openZlspjd_A);

						}

						if (tjxkjNum > 0) {
							// 报名人填写的推荐人数;
							int totalTjxNum = jdbcTemplate.queryForObject(
									"select COUNT(1) from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_MBA_TJX_YX='Y'",
									new Object[] { TZ_APP_INS_ID }, "Integer");
							// 推荐人已经提交推荐信的人数;
							int totalTjxTjNum = jdbcTemplate.queryForObject(
									"SELECT COUNT(1) FROM PS_TZ_KS_TJX_TBL A WHERE ((A.ATTACHSYSFILENAME <> '' AND A.ATTACHUSERFILE <> '' AND A.ATTACHSYSFILENAME IS NOT NULL AND A.ATTACHUSERFILE IS NOT NULL) OR EXISTS (SELECT 'Y' FROM PS_TZ_APP_INS_T B WHERE A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U')) AND A.TZ_APP_INS_ID = ? and A.TZ_MBA_TJX_YX='Y'",
									new Object[] { TZ_APP_INS_ID }, "Integer");
							// 查看推荐信信息页面超链接;
							String openTjx_A = tzGDObject.getHTMLText(
									"HTML.TZApplicationCenterBundle.TZ_OPEN_WINDOW_VIEW_A", view, classId,
									String.valueOf(TZ_APP_INS_ID), language, "TJX");
							if (colspan == 3) {
								tjxTd = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_TJX_SHOW_TD",
										"24%", tjxxx, "推荐信:" + totalTjxTjNum + "/" + totalTjxNum, openTjx_A);
							} else {
								tjxTd = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_BM_TJX_SHOW_TD",
										bmWidth, tjxxx, "推荐信:" + totalTjxTjNum + "/" + totalTjxNum, openTjx_A);
							}

						}
					}

					if (bmlcTotalNum > 0) {
						TZ_SQLC_TABLE = TZ_SQLC_TABLE + tzGDObject.getHTMLText(
								"HTML.TZApplicationCenterBundle.TZ_GD_ZXSQ_APPLY_YES_LC_TABLE",
								ApplicationStatus + mssqhId, className, expandOrShrink, expandOrShrinkActive, "block",
								TZ_BJ_BM_LC_STEP_DIV, String.valueOf(colspan), bmztTd + zltjTd + tjxTd);
					} else {
						TZ_SQLC_TABLE = TZ_SQLC_TABLE
								+ tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_ZXSQ_APPLY_NO_LC_TABLE",
										ApplicationStatus + mssqhId, className, expandOrShrinkActive,
										String.valueOf(colspan), bmztTd + zltjTd + tjxTd);
					}

					isSetpDisplay = "none";

				}
			}

			// 新增报名班级;
			// TZ_SQLC_TABLE = TZ_SQLC_TABLE +
			// tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_ADD_BM_CLASS_TABLE","新增",language,
			// "CLASS",strSiteId);

			String classSelectHtml = "";
			classSelectHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_CLASS_SELECT_HTML",
					TZ_SQLC_TABLE, ZSGL_URL, strCssDir, historyDesc, str_jg_id, strSiteId,
					request.getContextPath());
			applicationCenterHtml = classSelectHtml;
			applicationCenterHtml = siteRepCssServiceImpl.repTitle(applicationCenterHtml, strSiteId);
			applicationCenterHtml = siteRepCssServiceImpl.repCss(applicationCenterHtml, strSiteId);

		} catch (Exception e) {
			e.printStackTrace();
			return "无法获取相关数据";
		}
		return applicationCenterHtml;
	}
}
