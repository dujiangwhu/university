package com.tranzvision.gd.TZApplicationCenterBundle.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartJSSDKSign;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tranzvision.gd.util.base.AnalysisSysVar;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;
import com.tranzvision.gd.TZStuCertificateBundle.dao.PsTzCertOprLogMapper;
import com.tranzvision.gd.TZStuCertificateBundle.model.PsTzCertOprLog;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzUserregMbTMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzUserregMbT;

/**
 * 录取通知书html5展示
 * 
 * @para 录取通知书html
 * @ret 报名表对应的解析证书模板后的html或静态html
 * @author YTT
 * @since 2017-01-22
 */
@Controller
@RequestMapping(value = { "/admission" })
public class TzAppAdmissionController {

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private SqlQuery sqlQuery1;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzUserregMbTMapper psTzUserregMbTMapper;
	@Autowired
	private TzWeChartJSSDKSign tzWeChartJSSDKSign;
	@Autowired
	private PsTzCertOprLogMapper psTzCertOprLogMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzCookie tzCookie;
	@Autowired
	private GetSeqNum getSeqNum;

	// 生成录取通知书html
	@RequestMapping(value = { "/{orgid}/{siteid}/{oprid}/{tzAppInsID}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String viewQrcodeAdmission(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid,
			@PathVariable(value = "oprid") String oprid, @PathVariable(value = "tzAppInsID") String tzAppInsID) {
		try {
			orgid = orgid.toLowerCase();
			String strRet = "";
			String tzCertMergHtml = "";
			
			//mba站点、MSEM、x计划的站点id;
			String mbaSiteId = sqlQuery1.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_MBA_SITE_ID'","String");
			String msemSiteId = sqlQuery1.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_MMBA_SITE_ID'","String");
			String xSiteId = sqlQuery1.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_XMBA_SITE_ID'","String");
			

			//String staticHtmlDirSql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			// 录取通知书静态html路径——"/statics/css/website/m/html"
			//String staticHtmlDir = sqlQuery1.queryForObject(staticHtmlDirSql, new Object[] { "TZ_GD_CERT_STCHTML_DIR" },"String");

			//String dir = request.getServletContext().getRealPath(staticHtmlDir);
			//String fileName = tzAppInsID + ".html";

			//String filePath = "";
			//if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
				//filePath = dir + File.separator + fileName;
			//} else {
				//filePath = dir + fileName;
			//}

			// 【0】面试申请号
			String mshid = sqlQuery1.queryForObject("select TZ_MSH_ID from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID=?",new Object[] { tzAppInsID },"String");
			
			
//			String tzLuquStaSql = "SELECT TZ_LUQU_ZT FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_APP_INS_ID=?";
//			String tzLuquSta = sqlQuery1.queryForObject(tzLuquStaSql, new Object[] { tzAppInsID }, "String");
			
			//mba站点、MSEM流程表完全相同
			if(siteid.equals(mbaSiteId) || siteid.equals(msemSiteId)){
				//查询录取状态;
				String tzLuquSta = sqlQuery1.queryForObject("select TZ_MSJG from PS_TZ_MSJG_DR_T where TZ_MSH_ID=?", new Object[] { mshid },"String");
				// 【1】查询证书模板id
				String tzCertTplIdSql = "SELECT B.TZ_CERT_TMPL_ID FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B ,PS_TZ_CLASS_INF_T C,PS_TZ_FORM_WRK_T D WHERE A.TZ_APP_INS_ID=? AND D.TZ_APP_INS_ID=A.TZ_APP_INS_ID AND C.TZ_CLASS_ID=D.TZ_CLASS_ID AND B.TZ_PRJ_ID=C.TZ_PRJ_ID";
				String tzCertTplId = sqlQuery1.queryForObject(tzCertTplIdSql, new Object[] { tzAppInsID },
						"String");
				if(tzCertTplId == null || "".equals(tzCertTplId)){
					return "<html style='font-size:40px'>抱歉，暂不提供查看预录取通知书</html>";
				}
				
				if("保留预录取".equals(tzLuquSta)){
					tzLuquSta="预录取";
				}
				if ("有条件预录取".equals(tzLuquSta) || "预录取".equals(tzLuquSta)) {// 条件录取
					// 判断静态文件是否已存在
					//File file = new File(filePath);
					//if (!file.exists()) {	
						// 【2】获取证书模板默认套打模板html
						String tzCertMergHtmlSql = "SELECT TZ_CERT_MERG_HTML1 FROM PS_TZ_CERTTMPL_TBL WHERE TZ_CERT_TMPL_ID=? AND TZ_JG_ID=? AND TZ_USE_FLAG='Y'";
						tzCertMergHtml = sqlQuery1.queryForObject(tzCertMergHtmlSql, new Object[] { tzCertTplId, orgid },
								"String");

						// 【3】解析系统变量、返回解析后的html
						int syavarStartIndex = tzCertMergHtml.indexOf("[SYSVAR-");
						while (syavarStartIndex != -1) {
							int syavarEndIndex = tzCertMergHtml.indexOf(']', syavarStartIndex);
							String sysvarId = tzCertMergHtml.substring(syavarStartIndex + 8, syavarEndIndex);
							System.out.println(sysvarId);
							String[] sysVarParam = { orgid, siteid, oprid, tzAppInsID };
							AnalysisSysVar analysisSysVar = new AnalysisSysVar();
							analysisSysVar.setM_SysVarID(sysvarId);
							analysisSysVar.setM_SysVarParam(sysVarParam);
							Object sysvarValue = analysisSysVar.GetVarValue();
							System.out.println((String) sysvarValue);
							System.out.println("[SYSVAR-" + sysvarId + "]");
							tzCertMergHtml = tzCertMergHtml.replace("[SYSVAR-" + sysvarId + "]", (String) sysvarValue);

							System.out.println(tzCertMergHtml);
							syavarStartIndex = tzCertMergHtml.indexOf("[SYSVAR-");
						}

						// 【4】生成静态录取通知书html
						//boolean bl = this.staticFile(tzCertMergHtml, dir, fileName);
						//if (!bl) {
							//errMsg[0] = "1";
							//errMsg[1] = "静态化html失败！";
						//}
						
						/*
					} else {
						strRet = request.getScheme() + "://" + request.getServerName() + ":"
								+ String.valueOf(request.getServerPort()) + request.getContextPath() + staticHtmlDir;

						if ((strRet.lastIndexOf(File.separator) + 1) != strRet.length()) {
							strRet = strRet + File.separator + fileName;
						} else {
							strRet = strRet + fileName;
						}

						response.sendRedirect(strRet);
					}
					*/
				} else {
					tzCertMergHtml = "<html style='font-size:40px'>抱歉，该考生未录取，无法查看预录取通知书</html>";
				}
			}else if(siteid.equals(xSiteId)){
				String tzCertTplIdSql = "SELECT B.TZ_CERT_TMPL_ID FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B ,PS_TZ_CLASS_INF_T C,PS_TZ_FORM_WRK_T D WHERE A.TZ_APP_INS_ID=? AND D.TZ_APP_INS_ID=A.TZ_APP_INS_ID AND C.TZ_CLASS_ID=D.TZ_CLASS_ID AND B.TZ_PRJ_ID=C.TZ_PRJ_ID";
				String tzCertTplId = sqlQuery1.queryForObject(tzCertTplIdSql, new Object[] { tzAppInsID },
						"String");
				if(tzCertTplId == null || "".equals(tzCertTplId)){
					return "<html style='font-size:40px'>抱歉，暂不提供查看预录取通知书</html>";
				}
				
				//查询录取状态;
				String tzLuquSta = sqlQuery1.queryForObject("select TZ_MSJG from PS_TZ_XMSJG_DR_T where TZ_MSH_ID=?", new Object[] { mshid },"String");
				if("通过".equals(tzLuquSta)){
					// 【0】检查考生报名表目前在读阶段【本科、硕士研究生、博士研究生】
					String zdjdFieldName = sqlQuery1.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_X_MBA_ZD_FLD'","String");
					//本科;
					String bk = sqlQuery1.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_X_MBA_BK'","String");
					//硕士
					String ss = sqlQuery1.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_X_MBA_SS'","String");
					//博士
					String bs = sqlQuery1.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_X_MBA_BS'","String");
					//报名表中是否有对应的值;
					int count = sqlQuery1.queryForObject("SELECT COUNT(1) FROM PS_TZ_APP_DHCC_T where TZ_APP_INS_ID= ? and TZ_XXX_BH = ? and TZ_IS_CHECKED='Y' AND (TZ_XXXKXZ_MC=? OR TZ_XXXKXZ_MC=? OR TZ_XXXKXZ_MC=?) ",new Object[]{tzAppInsID,zdjdFieldName,bk,ss,bs},"int");
					
					if(count != 1){
						return "<html style='font-size:40px'>抱歉，报名表中【目前在读阶段】没有对应的值，无法生成对应预录取通知书</html>";
					}
					
					

					// 【2】获取证书模板默认套打模板html
					String tzCertMergHtmlSql = "SELECT TZ_CERT_MERG_HTML1 FROM PS_TZ_CERTTMPL_TBL WHERE TZ_CERT_TMPL_ID=? AND TZ_JG_ID=? AND TZ_USE_FLAG='Y'";
					tzCertMergHtml = sqlQuery1.queryForObject(tzCertMergHtmlSql, new Object[] { tzCertTplId, orgid },
							"String");

					// 【3】解析系统变量、返回解析后的html
					int syavarStartIndex = tzCertMergHtml.indexOf("[SYSVAR-");
					while (syavarStartIndex != -1) {
						int syavarEndIndex = tzCertMergHtml.indexOf(']', syavarStartIndex);
						String sysvarId = tzCertMergHtml.substring(syavarStartIndex + 8, syavarEndIndex);
						System.out.println(sysvarId);
						String[] sysVarParam = { orgid, siteid, oprid, tzAppInsID };
						AnalysisSysVar analysisSysVar = new AnalysisSysVar();
						analysisSysVar.setM_SysVarID(sysvarId);
						analysisSysVar.setM_SysVarParam(sysVarParam);
						Object sysvarValue = analysisSysVar.GetVarValue();
						System.out.println((String) sysvarValue);
						System.out.println("[SYSVAR-" + sysvarId + "]");
						tzCertMergHtml = tzCertMergHtml.replace("[SYSVAR-" + sysvarId + "]", (String) sysvarValue);

						System.out.println(tzCertMergHtml);
						syavarStartIndex = tzCertMergHtml.indexOf("[SYSVAR-");
					}
				}else{
					tzCertMergHtml = "<html style='font-size:40px'>抱歉，该考生未录取，无法查看预录取通知书</html>";
				}
			}else{
				tzCertMergHtml = "<html style='font-size:40px'>访问的链接无效</html>";
			}
			

			strRet = tzCertMergHtml;
			return strRet;

		} catch (Exception e) {
			e.printStackTrace();
			//errMsg[0] = "2";
			//errMsg[1] = "晒录取通知书异常！";
			return "";
		}
	}

	/***
	 * 生成微信签名信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "wxSign", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String wxSign(HttpServletRequest request, HttpServletResponse response) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("result", "");
		try {
			String url = request.getParameter("url");

			String appId = getHardCodePoint.getHardCodePointVal("TZ_WX_CORPID");
			String secret = getHardCodePoint.getHardCodePointVal("TZ_WX_SECRET");
			String wxType = getHardCodePoint.getHardCodePointVal("TZ_WX_TYPE");

			Map<String, String> signMap = tzWeChartJSSDKSign.sign(appId, secret, wxType, url);

			if (signMap != null) {
				jsonMap.replace("result", "success");
				jsonMap.put("appId", appId);
				jsonMap.put("timestamp", signMap.get("timestamp"));
				jsonMap.put("nonceStr", signMap.get("nonceStr"));
				jsonMap.put("signature", signMap.get("signature"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.replace("result", "failure");
		}
		return jacksonUtil.Map2json(jsonMap);
	}
	
	/***
	 * 查看录取通知书
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "logCount", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String logCount(HttpServletRequest request, HttpServletResponse response) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("result", "");
		try {
			
			boolean writeLogFlag = false;
			
			
			String opType = request.getParameter("opType");
			
			String appInsId = request.getParameter("appInsId");
			
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			// 【0】查询录取状态

			String tzGetKshSql = "SELECT TZ_MSH_ID,TZ_CLASS_ID,OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=? LIMIT 0,1";
			Map<String, Object> tzGetKshMap = sqlQuery1.queryForMap(tzGetKshSql, new Object[] { appInsId });
			
			String mshid = "";
			String opridZs = "";
			String tzLuquSta = "";
			String tzClassId = "";
			
			if(tzGetKshMap!=null){
				mshid = tzGetKshMap.get("TZ_MSH_ID")==null?"":String.valueOf(tzGetKshMap.get("TZ_MSH_ID"));
				tzClassId = tzGetKshMap.get("TZ_CLASS_ID")==null?"":String.valueOf(tzGetKshMap.get("TZ_CLASS_ID"));
				opridZs = tzGetKshMap.get("OPRID")==null?"":String.valueOf(tzGetKshMap.get("OPRID"));
				
				tzLuquSta = sqlQuery1.queryForObject("select TZ_MSJG from PS_TZ_MSJG_DR_T where TZ_MSH_ID=?", new Object[] { mshid },"String");
				String strJgId = sqlQuery1.queryForObject("SELECT TZ_JG_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ? LIMIT 0,1",
						new Object[] { tzClassId },"String");
				
				/*统计查看次数；页面加载判断是否有该证书对应的cookie(cookieBitCertView)，如果有，不计数；否则创建cookie，并计数.转发后需要重新刷新页面*/
				String cookieBitCertViewName = "cookieBitCertView" + appInsId;
				String cookieBitCertView = tzCookie.getStringCookieVal(request, cookieBitCertViewName);
				if("".equals(cookieBitCertView) || cookieBitCertView == null || "ZF".equals(opType)){
					writeLogFlag = true;
				}
				if("保留预录取".equals(tzLuquSta)){
					tzLuquSta="预录取";
				}
				if(("有条件预录取".equals(tzLuquSta) || "预录取".equals(tzLuquSta)) && writeLogFlag){
					tzCookie.addCookie(response, cookieBitCertViewName, "Y");
					PsTzCertOprLog psTzCertOprLog = new PsTzCertOprLog();
					int numZslsh = getSeqNum.getSeqNum("PS_TZ_CERT_OPR_LOG", "TZ_CERT_LSH");
					
					psTzCertOprLog.setTzCertLsh((long)numZslsh);
					psTzCertOprLog.setTzJgId(strJgId);
					psTzCertOprLog.setTzCzType(opType);
					psTzCertOprLog.setOprid(opridZs);
					psTzCertOprLog.setTzCertTypeId("CERT_1");
					psTzCertOprLog.setTzZhshId(appInsId);
					psTzCertOprLog.setRowAddedOprid(oprid);
					psTzCertOprLog.setRowAddedDttm(new Date());
					psTzCertOprLog.setRowLastmantOprid(oprid);
					psTzCertOprLog.setRowLastmantDttm(new Date());
					psTzCertOprLogMapper.insertSelective(psTzCertOprLog);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.replace("result", "failure");
		}
		return jacksonUtil.Map2json(jsonMap);
	}

	public boolean staticFile(String strReleasContent, String dir, String fileName) {
		try {
			System.out.println(dir);
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			String filePath = "";
			if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
				filePath = dir + File.separator + fileName;
			} else {
				filePath = dir + fileName;
			}

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strReleasContent);
			bw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//errMsg[0] = "3";
			//errMsg[1] = "静态化文件时异常！";
			return false;
		}
	}

}
