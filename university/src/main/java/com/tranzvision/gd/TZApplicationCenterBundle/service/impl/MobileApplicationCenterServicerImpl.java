package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobleMiddleAreaInterface;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.MobileApplicationCenterServicerImpl")
public class MobileApplicationCenterServicerImpl implements MobleMiddleAreaInterface {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	@Override
	public String tzGetHtmlContent(String strSiteId, String areaId,String columnId) {
		String applicationCenterHtml = "";

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		//oprid = "TZ_14082";
		String ctxRoot = request.getContextPath();

		try {
			// 查询报名中心显示规则 ,A: 为显示所有开放的班级，B:只显示报名人已经报名的班级； 无值当 A 处理;
			String bmClassShow = jdbcTemplate.queryForObject(
					"select TZ_BM_CLASS_SHOW from PS_SITE_BMCLASS_SZ_T where TZ_SITEI_ID=?", new Object[] { strSiteId },
					"String");
			if (bmClassShow == null || "".equals(bmClassShow)) {
				bmClassShow = "A";
			}

			// 得到当前年份;
			Calendar a = Calendar.getInstance();
			int currentYear = a.get(Calendar.YEAR);// 得到年

			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";

			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				language = (String) siteMap.get("TZ_SITE_LANG");
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}

			// 开始新申请;
			String addNewSqBtDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "KSSXQ", language, "开始新申请", "开始新申请");
			// 在线申请;
			String applayDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "ZXSQ", language,"在线申请", "在线申请");
			//申请表
			String sqbDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "SQB", language,"申请表", "申请表");
			//查看
			String viewDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "VIEW", language,"查看", "查看");
			//资料清单
			String zlqdDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "ZLQD", language,"资料清单", "资料清单");
			//推荐信
			String tjxDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "TJX", language,"推荐信", "推荐信");
			//申请进度
			String sqjdDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "SQJD", language,"申请进度", "申请进度");
			//已支付(查看)
			String isPaynDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "ISPAY", language,"已支付(查看)", "已支付(查看)");
			//去支付
			String payDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "PAY", language,"去支付", "去支付");
			//暂无项目可报名
			String noPrjDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "NOPRJ", language,"暂无项目可报名", "暂无项目可报名");
			//历史报名
			String historyApplyDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_MOBILE_SITE_MESSAGE", "LSBM", language,"历史报名>>", "历史报名>>");
			
			// 通用链接;
			String ZSGL_URL = ctxRoot + "/dispatcher";
			//报考方向选择链接;
			String bkfxUrl = ZSGL_URL + "?classid=ChosBatch&siteId=" + strSiteId;
			
			//查看该登录人是否有历史报名表;
			int hasAppIns = jdbcTemplate.queryForObject("select count(1) from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN  (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?)  AND TZ_JG_ID=?)",new Object[] { oprid,strSiteId,str_jg_id  },"int");
			//历史报名url;
			String historyApplyUrl = ZSGL_URL + "?classid=mAppHis&siteId=34";
			
			// 是否开通了申请班级;
			// 是否只显示已经报名的班级;
			String totalSQL = "";
			int totalNum = 0;
			if ("B".equals(bmClassShow)) {
				//totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=? and YEAR(ROW_LASTMANT_DTTM)=?)";
				totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?)";
				totalNum = jdbcTemplate.queryForObject(totalSQL,
						new Object[] { strSiteId, str_jg_id, oprid }, "Integer");
			} else {
				//totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and ((TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()) or (TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=? and YEAR(ROW_LASTMANT_DTTM)=?)))";
				totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and (( TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()) or (TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?)))";
				totalNum = jdbcTemplate.queryForObject(totalSQL,
						new Object[] { strSiteId, str_jg_id, oprid }, "Integer");
			}
			
			if (totalNum == 0) {
				if ("B".equals(bmClassShow)) {
					// 如果只显示报名的报名班级,当没有报名时，需要判断有没有开通的班级来控制“开始新申请”按钮是否显示;
					String sfKtBjSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()";
					int ktBjTotalNum = jdbcTemplate.queryForObject(sfKtBjSQL, new Object[] { strSiteId, str_jg_id },
							"Integer");

					if (ktBjTotalNum == 0) {
						String addClass = tzGDObject.getHTMLText(
								"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_NO_HTML", ctxRoot,noPrjDesc);
						if(hasAppIns > 0){
							applicationCenterHtml = tzGDObject
									.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HTML", applayDesc, addClass,historyApplyUrl,historyApplyDesc);
						}else{
							applicationCenterHtml = tzGDObject
									.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HTML", applayDesc, addClass,"#","");
						}
						
						return applicationCenterHtml;
					} else {
						// 有开通的班级就要显示“开始新申请”按钮;
						String addClass = tzGDObject.getHTMLText(
								"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_BUTTON_HTML", addNewSqBtDesc, ctxRoot,bkfxUrl);
						if(hasAppIns > 0){
							applicationCenterHtml = tzGDObject
									.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HTML", applayDesc, addClass,historyApplyUrl,historyApplyDesc);
						}else{
							applicationCenterHtml = tzGDObject
									.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HTML", applayDesc, addClass,"#","");
						}

						return applicationCenterHtml;
					}

				} else {
					// 没开通;
					String addClass = tzGDObject.getHTMLText(
							"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_NO_HTML", ctxRoot,noPrjDesc);
					if(hasAppIns > 0){
						applicationCenterHtml = tzGDObject
								.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HTML", applayDesc, addClass,historyApplyUrl,historyApplyDesc);
					}else{
						applicationCenterHtml = tzGDObject
								.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HTML", applayDesc, addClass,"#","");
					}
					
					return applicationCenterHtml;
				}
			}

			// 查询所有可以报名的班级;
			String classId = "", className = "", classDesc = "";
			// 查看已经报名的班级总数;
			/*
			int applyNum = 0;
			String applyNumSQL = "select count(distinct b.TZ_CLASS_ID) APPLY_NUM from PS_TZ_APP_INS_T a,PS_TZ_FORM_WRK_T b where a.TZ_APP_INS_ID=b.TZ_APP_INS_ID and b.TZ_CLASS_ID in (select c.TZ_CLASS_ID from PS_TZ_CLASS_INF_T c where c.TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND c.TZ_JG_ID=? and c.TZ_IS_APP_OPEN='Y' and c.TZ_APP_START_DT IS NOT NULL AND c.TZ_APP_START_TM IS NOT NULL AND c.TZ_APP_END_DT IS NOT NULL AND c.TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(c.TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(c.TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(c.TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(c.TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()) and b.OPRID=?";
			applyNum = jdbcTemplate.queryForObject(applyNumSQL, new Object[] { strSiteId, str_jg_id, oprid },
					"Integer");
					*/
			// 班级使用的报名表模板;
			String TZ_APP_MODAL_ID = "";

			

			// 循环开通的班级;
			// 是否只显示已经报名的班级;
			String sql = "";
			List<Map<String, Object>> classList;
			if ("B".equals(bmClassShow)) {
				sql = "SELECT TZ_CLASS_ID,TZ_CLASS_NAME,DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d') TZ_APP_START_DT,DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d') TZ_APP_END_DT,TZ_CLASS_DESC,TZ_APP_MODAL_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?) ORDER BY TZ_APP_START_DT,TZ_APP_END_DT ASC";
				classList = jdbcTemplate.queryForList(sql, new Object[] { strSiteId, str_jg_id, oprid });
			} else {
				sql = "SELECT TZ_CLASS_ID,TZ_CLASS_NAME,DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d') TZ_APP_START_DT,DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d') TZ_APP_END_DT,TZ_CLASS_DESC,TZ_APP_MODAL_ID from  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and (( TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()) or (TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?))) ORDER BY TZ_APP_START_DT,TZ_APP_END_DT ASC";
				classList = jdbcTemplate.queryForList(sql, new Object[] { strSiteId, str_jg_id, oprid });
			}

			// boolean ispay = false;
			String classHtml = "";
			if (classList != null && classList.size() > 0) {
				for (int i = 0; i < classList.size(); i++) {
					classId = (String) classList.get(i).get("TZ_CLASS_ID");
					className = (String) classList.get(i).get("TZ_CLASS_NAME");
					if (className == null) {
						className = "";
					}

					classDesc = (String) classList.get(i).get("TZ_CLASS_DESC");
					if (classDesc == null) {
						classDesc = "";
					}
					TZ_APP_MODAL_ID = (String) classList.get(i).get("TZ_APP_MODAL_ID");

					// 报名表链接;
					String applyFromUrl = ZSGL_URL + "?classid=appId&TZ_CLASS_ID=" + classId + "&SITE_ID=" + strSiteId;

					// 支付连接
					String payUrl = ZSGL_URL + "?classid=tzPay&PRG_ID=" + classId + "&PRG_TYPE=002&OTHER=XX";

					// 查看该人员是否已经申请了该班级的报名表;
					long TZ_APP_INS_ID = 0;
					try {
						TZ_APP_INS_ID = jdbcTemplate.queryForObject(
								"select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T where TZ_CLASS_ID=? and OPRID=?",
								new Object[] { classId, oprid }, "Long");
					} catch (NullPointerException nullException) {
						TZ_APP_INS_ID = 0;
					}

					//打印;
					//String applyFormPrint = request.getContextPath() + "/PrintPdfServlet?instanceID=" + TZ_APP_INS_ID;

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

					// 是否开启支付
					String isPay = jdbcTemplate.queryForObject(
							"select TZ_IS_PAY from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?", new Object[] { classId },
							"String");
					if (isPay == null) {
						isPay = "";
					}

					if (TZ_APP_INS_ID > 0) {
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
						
						//查看申请表
						String applyBmLi = tzGDObject.getHTMLText(
								"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLY_BM_LI", ctxRoot,appFormStatusDesc,sqbDesc,viewDesc,applyFromUrl);
						
						// 未提交时资料显示的文字;
						String unsubmitWz = "N/A";
						//查看资料清单
						String appDjzlLi = "";
						if (djzlTotalNum > 0) {
							if ("U".equals(TZ_APP_FORM_STA)) {
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
								String clqdUrl = ZSGL_URL + "?classid=mClqdList&siteId=" + strSiteId + "&instanceId=" + TZ_APP_INS_ID + "&bmClassId=" + classId;
								appDjzlLi = tzGDObject.getHTMLText(
										"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLY_DJZL_LI", ctxRoot,zlSpZtDesc,zlqdDesc,viewDesc,clqdUrl);
							}else{
								appDjzlLi = tzGDObject.getHTMLText(
										"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLY_DJZL_NA_LI", ctxRoot,unsubmitWz,zlqdDesc);
							}
							
						}
						
						//查看推荐信;
						String tjxLi = "";
						if(tjxkjNum > 0){
							// 报名人填写的推荐人数;
							int totalTjxNum = jdbcTemplate.queryForObject(
									"select COUNT(1) from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_MBA_TJX_YX='Y'",
									new Object[] { TZ_APP_INS_ID }, "Integer");
							if (totalTjxNum > 0) {
								// 推荐人已经提交推荐信的人数;
								int totalTjxTjNum = jdbcTemplate.queryForObject(
										"SELECT COUNT(1) FROM PS_TZ_KS_TJX_TBL A WHERE ((A.ATTACHSYSFILENAME <> '' AND A.ATTACHUSERFILE <> '' AND A.ATTACHSYSFILENAME IS NOT NULL AND A.ATTACHUSERFILE IS NOT NULL) OR EXISTS (SELECT 'Y' FROM PS_TZ_APP_INS_T B WHERE A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U')) AND A.TZ_APP_INS_ID = ? and A.TZ_MBA_TJX_YX='Y'",
										new Object[] { TZ_APP_INS_ID }, "Integer");
								String tjxUrl = ZSGL_URL + "?classid=mtjxList&siteId=" + strSiteId + "&instanceId=" + TZ_APP_INS_ID;
								tjxLi = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLY_TJX_LI", ctxRoot,String.valueOf(totalTjxTjNum) , String.valueOf(totalTjxNum),tjxDesc,viewDesc,tjxUrl);
								
							} else {
								tjxLi = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLY_TJX_NA_LI", ctxRoot,tjxDesc);
							}

						}
						
						// 循环报名流程;
						String sqjdHtml = "";
						if (bmlcTotalNum > 0) {
							String sqjdUrl = ZSGL_URL + "?classid=mAppstatus&siteId=" + strSiteId + "&appInsId=" + TZ_APP_INS_ID;
							sqjdHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_SQJD", sqjdDesc,ctxRoot,sqjdUrl);
						}
						
						//支付
						String payHtml = "";
						if (isPay.equals("Y")) {
							String payId = jdbcTemplate.queryForObject(
									"SELECT TZ_PAY_ID FROM PS_TZ_PAYMENTINFO_T a,PS_PRJ_PAY_T b WHERE a.TZ_PAY_PRJ_ID =b.TZ_PAY_PRJ_ID and a.OPRID=? and a.TZ_PAYMENTSTATUS=? and a.TZ_JG_ID=? and b.TZ_PRJ_ID=? and b.TZ_PAY_TYPE=? limit 1",
									new Object[] { oprid, "02", str_jg_id, classId, "002" }, "String");
							if (payId != null && !"".equals(payId)) {
								// 用户已经支付，显示查看
								// 查看用户订
								String showPay = ZSGL_URL + "?classid=showPay&payId=" + payId;
								/*
								
								payTd_A = tzGDObject.getHTMLText(
										"HTML.TZApplicationCenterBundle.TZ_GD_BM_PAY_SHOW_A", view, showPay);
								payTd = tzGDObject.getHTMLText(
										"HTML.TZApplicationCenterBundle.TZ_GD_BM_STATUS_SHOW_TD", bmWidth, pay,
										payTd_A, pay);
										*/
								payHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_PAY", isPaynDesc,ctxRoot,showPay);
							} else {
								// 用户没有支付，显示支付
								/*
								payTd_A = tzGDObject.getHTMLText(
										"HTML.TZApplicationCenterBundle.TZ_GD_BM_PAY_SHOW_A", pay, payUrl);
								payTd = tzGDObject.getHTMLText(
										"HTML.TZApplicationCenterBundle.TZ_GD_BM_STATUS_SHOW_TD", bmWidth, pay,
										view, payTd_A);
									*/
								payHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_PAY", payDesc,ctxRoot,payUrl);
							}

						}

						classHtml = classHtml + tzGDObject.getHTMLText(
								"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPAY_YET_CLASS", className,applyBmLi+appDjzlLi+tjxLi,sqjdHtml,payHtml);
						
						
					} else {
						classHtml = classHtml + tzGDObject.getHTMLText(
								"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_CLASS_BUTTON_HTML", className,addNewSqBtDesc, ctxRoot);

					}
				}
			}
			if ("B".equals(bmClassShow)) {
				// 查看是否所有的班级都被申请过了,如果是则不显示新增
				int noAddBmbCount = jdbcTemplate.queryForObject(
						"SELECT count(1) from  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() and  TZ_CLASS_ID not in (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?)",
						new Object[] { strSiteId, str_jg_id, oprid }, "Integer");
				if (noAddBmbCount > 0) {
					String addClass = tzGDObject.getHTMLText(
							"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_BUTTON_HTML", addNewSqBtDesc, ctxRoot,bkfxUrl);
					classHtml = classHtml + addClass;
				} 
			}
			
			if(hasAppIns > 0){
				applicationCenterHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HTML", applayDesc, classHtml,historyApplyUrl,historyApplyDesc);
			}else{
				applicationCenterHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APPLAY_HTML", applayDesc, classHtml,"#","");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return applicationCenterHtml;
		}

		return applicationCenterHtml;
	}
}
