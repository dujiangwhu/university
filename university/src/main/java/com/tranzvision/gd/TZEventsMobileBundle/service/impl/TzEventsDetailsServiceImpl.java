package com.tranzvision.gd.TZEventsMobileBundle.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

import freemarker.core.ParseException;

/**
 * 手机版活动详情页面
 * @author zhanglang
 * 2017/03/01
 */
@Service("com.tranzvision.gd.TZEventsMobileBundle.service.impl.TzEventsDetailsServiceImpl")
public class TzEventsDetailsServiceImpl extends FrameworkImpl{
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private PsTzNaudlistTMapper psTzNaudlistTMapper;

	@Autowired
	private MySqlLockService mySqlLockService;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	
	
	/*@Override
	public String tzGetHtmlContent(String strParams) {
		String eventsDetailsHtml = "";
		try {
			
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			//活动ID
			String actID = request.getParameter("actId");
			
			eventsDetailsHtml = tzGDObject.getHTMLText("HTML.TZEventsMobileBundle.TZ_M_EVENTS_DETAILS_HTML",contextPath,ZSGL_URL);
			

			String siteId = request.getParameter("siteId");
			String columnId = request.getParameter("columnId");
			String artId = request.getParameter("artId");

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 校验 用户是否已经登录，如果未登录 则 跳到登录页面，用户登录完成以后在跳转回来
			if (siteId != null && !"".equals(siteId) && columnId != null && !"".equals(columnId) && artId != null
					&& !"".equals(artId)) {

				// 检查是否有听众控制
				String AudError = "N";
				String sqlAud = "select C.TZ_PROJECT_LIMIT,'Y' AUDFLG from PS_TZ_ART_AUDIENCE_T A,PS_TZ_AUD_LIST_T B,PS_TZ_ART_REC_TBL C WHERE B.TZ_DXZT='A' AND A.TZ_AUD_ID=B.TZ_AUD_ID AND A.TZ_ART_ID=C.TZ_ART_ID AND B.OPRID=? AND A.TZ_ART_ID=? LIMIT 1";
				Map<String, Object> mapAud = sqlQuery.queryForMap(sqlAud, new Object[] { oprid, artId });
				if (mapAud != null) {

					// 发布对象
					String pubFlg = (String) mapAud.get("TZ_PROJECT_LIMIT");
					// 是否当前人存在于该听众
					String audFlg = (String) mapAud.get("AUDFLG");

					if (pubFlg == "B" && audFlg != "Y") {
						// 如果设置发布对象为听众，不在听众中，不能访问
						eventsDetailsHtml = " 您无权限查看";
						AudError = "Y";
					} else {
						AudError = "N";
					}

				} else {
					AudError = "N";
				}

				if (AudError == "N") {
					// 查看是否是外部链接;
					String sql = "SELECT TZ_ART_TYPE1,TZ_OUT_ART_URL FROM PS_TZ_ART_REC_TBL WHERE TZ_ART_ID = ?";
					Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] { artId });
					if (map != null) {
						String artType = (String) map.get("TZ_ART_TYPE1");
						if ("B".equals(artType)) {
							String outurl = (String) map.get("TZ_OUT_ART_URL");
							if (outurl == null || "".equals(outurl)) {
								eventsDetailsHtml = "未定义外部链接";
							} else {
								eventsDetailsHtml = "<script type=\"text/javascript\">;location.href=\"" + outurl + "\"</script>";
							}
						} else {
							String htmlSQL = "select TZ_ART_SJ_CONT_SCR from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=? and TZ_ART_NEWS_DT <= now()";
							Map<String, Object> contentMap = sqlQuery.queryForMap(htmlSQL,new Object[] { siteId, columnId, artId });
							if (contentMap == null) {
								eventsDetailsHtml = "当前时间不可查看该内容";
							} else {
								eventsDetailsHtml = (String) contentMap.get("TZ_ART_SJ_CONT_SCR");
							}
						}

					} else {
						eventsDetailsHtml = "参数错误，请联系系统管理员";
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "无法获取相关数据";
		}
		return eventsDetailsHtml;
	}*/
	
	
	
	/**
	 * 撤销活动报名
	 */
	@Override
	public String tzGetJsonData(String strParams) {
		String strRet = "";
		String errorCode = "";
		String errorMsg = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strAPPLYID = "";
		String strBMRID = "";
		String cancelSuccess = "";
		String cancelFailed = "";
		try {

			jacksonUtil.json2Map(strParams);
			strAPPLYID = jacksonUtil.getString("actId");
			strBMRID = jacksonUtil.getString("bmrId");

			// 双语化
			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
			String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strAPPLYID }, "String");

			cancelSuccess = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG08",
					tzSiteLang, "撤销报名成功", "Cancel Application Successful");
			cancelFailed = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG09",
					tzSiteLang, "撤销报名失败：", "Online Application Failed");

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			strAPPLYID = jacksonUtil.getString("actId");
			strBMRID = jacksonUtil.getString("bmrId");

			// 报名状态
			String sql = "select TZ_NREG_STAT from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_HD_BMR_ID=?";
			String strRegStatus = sqlQuery.queryForObject(sql, new Object[] { strAPPLYID, strBMRID }, "String");

			// 报名状态为1-已报名，4-等候
			if ("1".equals(strRegStatus) || "4".equals(strRegStatus)) {

				PsTzNaudlistT psTzNaudlistT = new PsTzNaudlistT();
				psTzNaudlistT.setTzArtId(strAPPLYID);
				psTzNaudlistT.setTzHdBmrId(strBMRID);
				psTzNaudlistT.setTzNregStat("3");
				int updNum = psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
				if (updNum > 0) {
					String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
					String orgId = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
					
					//撤销报名成功站内信
					try{
						sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
						String name = sqlQuery.queryForObject(sql, new Object[]{ oprid }, "String");
						//撤销报名站内信模板
						String znxModel = getHardCodePoint.getHardCodePointVal("TZ_HDBM_CX_ZNX_TMP");
						//创建邮件任务实例
						String taskId = createTaskServiceImpl.createTaskIns(orgId, znxModel, "ZNX", "A");
						// 创建邮件发送听众
						String crtAudi = createTaskServiceImpl.createAudience(taskId,orgId,"活动撤销报名成功站内信通知", "JSRW");
						//添加听众成员
						boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", "", oprid, "", strAPPLYID, "");
						if(bl){
							sendSmsOrMalServiceImpl.send(taskId, "");
						}
					}catch(NullPointerException nullEx){
						//没有配置邮件模板
						nullEx.printStackTrace();
					}

					// 报名最早的状态为“等待”的报名人
					sql = "select TZ_HD_BMR_ID,OPRID from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_NREG_STAT='4' order by TZ_REG_TIME limit 0,1";
					String waitBmr = "";
					String waitOprid = "";
					Map<String,Object> bmrInfoMap = sqlQuery.queryForMap(sql, new Object[]{ strAPPLYID });
					if(bmrInfoMap != null){
						waitBmr = bmrInfoMap.get("TZ_HD_BMR_ID").toString();
						waitOprid = bmrInfoMap.get("OPRID").toString();
					}
					
					if (null != waitBmr && !"".equals(waitBmr) && "1".equals(strRegStatus)) {
						// 若撤销报名的人是已报名状态，则撤销成功后自动进补
						// 查询报名人数前就要锁表，不然同时报名的话，就可能超过允许报名的人数
						mySqlLockService.lockRow(sqlQuery, "TZ_NAUDLIST_T");

						// 活动席位数
						sql = "select TZ_XWS from PS_TZ_ART_HD_TBL where TZ_ART_ID=?";
						int numXW = sqlQuery.queryForObject(sql, new Object[] { strAPPLYID }, "int");

						// 已报名人数
						sql = "select count(1) from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_NREG_STAT='1'";
						int numYbm = sqlQuery.queryForObject(sql, new Object[] { strAPPLYID }, "int");

						if (numYbm < numXW) {
							// 将等待的报名人设置为已报名
							psTzNaudlistT = new PsTzNaudlistT();
							psTzNaudlistT.setTzArtId(strAPPLYID);
							psTzNaudlistT.setTzHdBmrId(waitBmr);
							psTzNaudlistT.setTzNregStat("1");
							psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
							
							if(waitOprid != null && !"".equals(waitOprid)){
								//活动报名成功站内信
								try{
									sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
									String name = sqlQuery.queryForObject(sql, new Object[]{ waitOprid }, "String");
									//报名成功成功站内信模板
									String znxModel = getHardCodePoint.getHardCodePointVal("TZ_HDBM_CG_ZNX_TMP");
									//创建邮件任务实例
									String taskId = createTaskServiceImpl.createTaskIns(orgId, znxModel, "ZNX", "A");
									// 创建邮件发送听众
									String crtAudi = createTaskServiceImpl.createAudience(taskId,orgId,"活动报名成功站内信通知", "JSRW");
									//添加听众成员
									boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", "", waitOprid, "", strAPPLYID, "");
									if(bl){
										sendSmsOrMalServiceImpl.send(taskId, "");
									}
								}catch(NullPointerException nullEx){
									//没有配置邮件模板
									nullEx.printStackTrace();
								}
							}
						}

						// 解锁
						mySqlLockService.unlockRow(sqlQuery);
					}
					errorCode = "0";
					errorMsg = cancelSuccess;
				} else {
					errorCode = "1";
					errorMsg = cancelFailed;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorCode = "1";
			errorMsg = cancelFailed + e.getMessage();
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", errorCode);
		mapRet.put("resultDesc", errorMsg);
		mapRet.put("artid", strAPPLYID);
		mapRet.put("bmrid", strBMRID);

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}

	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "getActInfoData":
					//活动详情页面 
					strRet = this.getActInfoData(strParams,errorMsg);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	
	/**
	 * 活动详情
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String getActInfoData(String strParams, String[] errorMsg) throws ParseException{
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("diaplayAppBar", "N");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date dateNow = new Date();
			jacksonUtil.json2Map(strParams);
			//活动编号
			String actId = jacksonUtil.getString("actId");
			
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
			
			//活动听众判断
			/*boolean isInAud = false;
			String audSql = "select TZ_AUD_ID from PS_TZ_ART_AUDIENCE_T where TZ_ART_ID=? and exists(select 'X' from PS_TZ_ART_REC_TBL where TZ_ART_ID=PS_TZ_ART_AUDIENCE_T.TZ_ART_ID and TZ_PROJECT_LIMIT='B')";
			List<Map<String,Object>> audList = sqlQuery.queryForList(audSql, new Object[]{ actId });
			if(audList != null && audList.size() > 0){
				for(Map<String,Object> audMap: audList){
					String audId = audMap.get("TZ_AUD_ID") == null ? "" : audMap.get("TZ_AUD_ID").toString();
					String inAudSql = "select 'Y' from PS_TZ_AUD_LIST_T where TZ_AUD_ID=? and TZ_DXZT<>'N' and OPRID=? limit 1";
					String inAud = sqlQuery.queryForObject(inAudSql, new Object[]{ audId, oprid }, "String");
					if("Y".equals(inAud)){
						isInAud = true;
					}
				}
			}else{
				isInAud = true;
			}*/
			
			
			// 获取活动显示模式
			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventDisplayMode");
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { actId,dateNow,dateNow });
			
			// 是否有效记录
			String validTD = "";
			// 是否启用在线报名
			String strQy_zxbm = "";
		
			if (mapData != null) {
				validTD = mapData.get("VALID_TD") == null ? "" : String.valueOf(mapData.get("VALID_TD"));
				strQy_zxbm = mapData.get("TZ_QY_ZXBM") == null ? "" : String.valueOf(mapData.get("TZ_QY_ZXBM"));
			}
			
			// 只有启用在线报名并且在有效报名时间内才显示在线报名条
			if ("Y".equals(strQy_zxbm)  && "Y".equals(validTD)) {
				rtnMap.replace("diaplayAppBar","Y");
				
				sql = "select 'Y' REG_FLAG,TZ_HD_BMR_ID,TZ_NREG_STAT FROM PS_TZ_NAUDLIST_T where OPRID=? and TZ_ART_ID=? and TZ_NREG_STAT IN('1','4')";
				Map<String, Object> mapBM = sqlQuery.queryForMap(sql, new Object[] { oprid, actId });

				// 是否已注册报名标识
				String regFlag = "";
				// 报名人ID
				String strBmrId = "";
				//报名状态
				String applySta = "";
				if (mapBM != null) {
					regFlag = mapBM.get("REG_FLAG") == null ? "" : String.valueOf(mapBM.get("REG_FLAG"));
					strBmrId = mapBM.get("TZ_HD_BMR_ID") == null ? "" : String.valueOf(mapBM.get("TZ_HD_BMR_ID"));
					applySta = mapBM.get("TZ_NREG_STAT") == null ? "" : String.valueOf(mapBM.get("TZ_NREG_STAT"));
				}
				
				//已报名活动，撤销报名
				if("Y".equals(regFlag) ){
					validTD = "Y";
				}
				
				
				//显示报名状态
				String statusText = "";
				switch(applySta){
				case "1":
					statusText = "已报名";
					break;
				case "4":
					//等候席位数
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetWaitingNumber");
					int waitNum = sqlQuery.queryForObject(sql, new Object[]{ actId, strBmrId }, "int");
					statusText = "等候席第"+ waitNum +"位";
					break;
				}
				
				rtnMap.put("statusText", statusText);

				rtnMap.put("regFlag", regFlag);
				rtnMap.put("bmrId", strBmrId);
				rtnMap.put("valid_dt", validTD);
				
				
				String strBaseUrl = request.getServletContext().getContextPath() + "/dispatcher?tzParams=";
				//在线报名URL
				String strAppFormUrl = request.getContextPath() + "/dispatcher?classid=eventsAppForm&actId="+actId;
				
				// 构造链接参数
				Map<String, Object> mapComParams = new HashMap<String, Object>();
				Map<String, Object> mapParams = new HashMap<String, Object>();
				
				mapComParams.put("actId", actId);
				mapComParams.put("bmrId", strBmrId);

				mapParams.put("ComID", "TZ_HD_MOBILE_COM");
				mapParams.put("PageID", "TZ_HD_DETAILS_STD");
				mapParams.put("OperateType", "EJSON");
				mapParams.put("comParams", mapComParams);

				String strUrlParams = jacksonUtil.Map2json(mapParams);
				//撤销报名URL
				String cancelApplyUrl = strBaseUrl + URLEncoder.encode(strUrlParams, "UTF-8");
				
				rtnMap.put("appFormUrl", strAppFormUrl);
				rtnMap.put("cancelApplyUrl", cancelApplyUrl);
			}
			
			String actAddr = "";
			String startTime = "";
			String endTime = "";
			SimpleDateFormat simpleDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			sql = "select TZ_NACT_ADDR,concat(TZ_START_DT,' ',TZ_START_TM) as TZ_START_DT,concat(TZ_END_DT,' ',TZ_END_TM) as TZ_END_DT from PS_TZ_ART_HD_TBL where TZ_ART_ID=?";
			Map<String, Object> actMap = sqlQuery.queryForMap(sql, new Object[] { actId });
			if(actMap != null){
				actAddr = actMap.get("TZ_NACT_ADDR") == null ? "" : actMap.get("TZ_NACT_ADDR").toString();
				startTime = actMap.get("TZ_START_DT") == null ? "" : actMap.get("TZ_START_DT").toString();
				endTime = actMap.get("TZ_END_DT") == null ? "" : actMap.get("TZ_END_DT").toString();
				
				SimpleDateFormat simpleDttmFormat = new SimpleDateFormat("MM/dd HH:mm");
				if(!"".equals(startTime)){
					startTime = simpleDttmFormat.format(simpleDatetimeFormat.parse(startTime)); 
				}
				if(!"".equals(endTime)){
					endTime = simpleDttmFormat.format(simpleDatetimeFormat.parse(endTime)); 
				}
			}
			
			if("".equals(actAddr) && "".equals(startTime) && "".equals(endTime)){
				//如果不是活动，地点取内容表中的TZ_LONG1字段，时间就取TZ_DATE1
				sql = "select TZ_LONG1,date_format(TZ_DATE1,'%Y/%m/%d') as TZ_DATE1 from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
				actMap = sqlQuery.queryForMap(sql, new Object[] { actId });
				if(actMap != null){
					actAddr = actMap.get("TZ_LONG1") == null ? "" : actMap.get("TZ_LONG1").toString();
					startTime = actMap.get("TZ_DATE1") == null ? "" : actMap.get("TZ_DATE1").toString();
				}
			}
			
			if(!"".equals(endTime)){
				startTime = startTime+ " - " +endTime;
			}
			
			rtnMap.put("location", actAddr);
			rtnMap.put("dateTime", startTime);

		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
}
