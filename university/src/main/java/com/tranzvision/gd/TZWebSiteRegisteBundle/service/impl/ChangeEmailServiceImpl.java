package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZWebSiteRegisteBundle.dao.PsTzDzyxYzmTblMapper;
import com.tranzvision.gd.TZWebSiteRegisteBundle.model.PsTzDzyxYzmTbl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang 修改邮箱 PS:TZ_GD_USERMG_PKG:TZ_CHANGE_EMAIL
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.ChangeEmailServiceImpl")
public class ChangeEmailServiceImpl extends FrameworkImpl {
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private ValidateUtil validateUtil;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	@Autowired
	private PsTzDzyxYzmTblMapper psTzDzyxYzmTblMapper;

	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 站点ID;
			String siteId = jacksonUtil.getString("siteId");
			String siteSQL = "SELECT TZ_JG_ID,TZ_SITE_LANG,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?";
			Map<String, Object> map = jdbcTemplate.queryForMap(siteSQL, new Object[] { siteId });
			if (map != null) {
				String strJgid = (String) map.get("TZ_JG_ID");
				String strLang = (String) map.get("TZ_SITE_LANG");
				String skinId = (String) map.get("TZ_SKIN_ID");
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

				String emailSQL = "select TZ_ZY_EMAIL from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
				String email = jdbcTemplate.queryForObject(emailSQL, new Object[] { oprid }, "String");
				if (email == null) {
					email = "";
				}

				String contextPath = request.getContextPath();
				String strBeginUrl = contextPath + "/dispatcher";
				String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath ();
				imgPath = contextPath + imgPath + "/" + skinId;

				String tzWdzhEmail = "";
				if ("ENG".equals(strLang)) {
					tzWdzhEmail = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_EN_EMAIL",
							strBeginUrl, strJgid, contextPath, imgPath,email,siteId);
				} else {
					tzWdzhEmail = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_EMAIL",
							strBeginUrl, strJgid, contextPath, imgPath,email,siteId);
				}

				tzWdzhEmail = objRep.repTitle(tzWdzhEmail, siteId);
				tzWdzhEmail = objRep.repCss(tzWdzhEmail, siteId);
				return tzWdzhEmail;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "修改失败";
	}

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String mess = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strEmail = jacksonUtil.getString("Email");
			String strJgid = jacksonUtil.getString("strJgid");
			String strSiteId = jacksonUtil.getString("siteid");
			
			// 判断邮箱格式
			boolean boolEmail = validateUtil.validateEmail(strEmail);
			if (boolEmail) {
				// 邮箱是否被占用;
				String emailZySQL = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL A,PS_TZ_REG_USER_T B WHERE A.OPRID=B.OPRID AND LOWER(TZ_EMAIL) = LOWER(?) AND TZ_JG_ID=? AND TZ_YXBD_BZ='Y' AND B.TZ_SITEI_ID=?";
				int count = jdbcTemplate.queryForObject(emailZySQL, new Object[] { strEmail, strJgid, strSiteId }, "Integer");
				if (count > 0) {
					//mess = "该邮箱已被占用，请重新输入！";
					mess = validateUtil.getMessageTextWithLanguageCd(strJgid, "ZHS","TZ_SITE_MESSAGE","138", "该邮箱已被占用，请重新输入", "The email has been bound,Please reenter it.");
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", mess);
				}

				String yzmSQL = " SELECT TZ_YZM_YXQ,TZ_TOKEN_CODE FROM PS_TZ_DZYX_YZM_TBL WHERE  TZ_EFF_FLAG='Y'  AND TZ_JG_ID=? AND TZ_EMAIL=? ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
				Map<String, Object> map = jdbcTemplate.queryForMap(yzmSQL, new Object[] { strJgid, strEmail });
				if (map != null) {
					String tokenCode = (String) map.get("TZ_TOKEN_CODE");
					Date yzmYxq = (Date) map.get("TZ_YZM_YXQ");
					if (tokenCode != null && !"".equals(tokenCode)) {
						Date curDate = new Date();
						if (curDate.before(yzmYxq)) {
							mess = "shtime";
							return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", mess);
						} else {
							String updateYzmSQL = "UPDATE PS_TZ_DZYX_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_TOKEN_CODE=?";
							jdbcTemplate.update(updateYzmSQL, new Object[] { tokenCode });

							mess = this.registerESave(strEmail, strJgid);
							return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", mess);
						}
					}
				}

				mess = this.registerESave(strEmail, strJgid);
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", mess);

			} else {
				mess = "请输入正确的邮箱！";
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				mess = "修改失败";
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", mess);
			} catch (Exception e1) {
				e1.printStackTrace();
				return e1.toString();
			}

		}
	}

	public String registerESave(String strEmail, String strJgid) {
		// 生成邮件发送令牌;
		String mess = "";
		try {
			String strSeq = "00000000" + String.valueOf(getSeqNum.getSeqNum("TZ_GD_AUTHCODE", "TZ_GD_EMAIL"));
			strSeq = strSeq.substring(strSeq.length() - 8, strSeq.length());
			SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String strYZM = String.valueOf((int) (Math.random() * 100000)) + datetimeFormate.format(new Date())
					+ strSeq;

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String dlzhSQL = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
			String strDlzhid = jdbcTemplate.queryForObject(dlzhSQL, new Object[] { strJgid, oprid }, "String");

			PsTzDzyxYzmTbl psTzDzyxYzmTbl = new PsTzDzyxYzmTbl();
			psTzDzyxYzmTbl.setTzDlzhId(strDlzhid);
			psTzDzyxYzmTbl.setTzJgId(strJgid);
			psTzDzyxYzmTbl.setTzTokenCode(strYZM);
			psTzDzyxYzmTbl.setTzTokenType("EDIT");
			psTzDzyxYzmTbl.setTzEmail(strEmail);
			Date currentDate = new Date();
			psTzDzyxYzmTbl.setTzCntlogAddtime(currentDate);

			Calendar ca = Calendar.getInstance();
			ca.setTime(new Date());
			ca.add(Calendar.MINUTE, 30);
			Date yxqDate = ca.getTime();
			psTzDzyxYzmTbl.setTzYzmYxq(yxqDate);

			psTzDzyxYzmTbl.setTzEffFlag("Y");
			psTzDzyxYzmTblMapper.insert(psTzDzyxYzmTbl);

			// 得到注册用户姓名;
			String strUserName = "";
			String siteid = "";
			String relenameSQL = "SELECT TZ_REALNAME,TZ_SITEI_ID FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1";
			Map<String, Object> userMap = jdbcTemplate.queryForMap(relenameSQL, new Object[] { oprid });
			//String strUserName = jdbcTemplate.queryForObject(relenameSQL, new Object[] { oprid }, "String");
			if(userMap != null){
				strUserName = (String)userMap.get("TZ_REALNAME");
				siteid = (String)userMap.get("TZ_SITEI_ID");
			}
			if (strUserName == null) {
				strUserName = "";
			}
			if (siteid == null) {
				siteid = "";
			}
			
			String EmlServiceSite="";
			switch(siteid){
			case"34":
				EmlServiceSite="mbazhaosheng@gsm.pku.edu.cn";
				break;
			case"35":
				EmlServiceSite="msemzhaosheng@gsm.pku.edu.cn";
				break;
			case"36":
				EmlServiceSite="";
				break;
					
			}
			
			// 确认修改邮箱 ;
			//com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.SureEmailServiceImpl;
			String serv = "http://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
			String sureEmail = serv + "/dispatcher";
			sureEmail  = sureEmail+"?classid=sureemail&siteid="+siteid+"&TZ_TOKEN_CODE="+strYZM;

			// 给当前填写的邮箱发送激活邮件---开始;
			// 发送内容;
			String content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_QUEREN_HTML", strUserName,
					sureEmail);

			// 发送邮件;
			String taskId = createTaskServiceImpl.createTaskIns(strJgid, "TZ_EML_N_001",siteid, "MAL", "A");
//			String taskId = createTaskServiceImpl.createTaskIns(strJgid, "TZ_MAL_NAME_CN",siteid, "MAL", "A");

			if (taskId == null || "".equals(taskId)) {
				mess = "创建邮件发送任务失败！";
				return mess;
			}

			// 创建短信、邮件发送的听众;
			String createAudience = createTaskServiceImpl.createAudience(taskId,strJgid,"光华用户邮箱修改", "JSRW");
			if (createAudience == null || "".equals(createAudience)) {
				mess = "创建邮件发送的听众失败！";
				return mess;
			}

			// 为听众添加听众成员;
			boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,strUserName, strUserName, "", "", strEmail, "", "", oprid,
					"", "", "");
			if (addAudCy == false) {
				mess = "为听众添加听众成员失败！";
				return mess;
			}

			// 修改主题;
			boolean bl = createTaskServiceImpl.updateEmailSendTitle(taskId,"光华管理学院-账户管理邮箱修改");
			if (bl == false) {
				mess = "修改发送主题失败！";
				return mess;
			}
			// 修改内容;
			bl = createTaskServiceImpl.updateEmailSendContent(taskId,content);
			if (bl == false) {
				mess = "修改发送内容失败！";
				return mess;
			}

			// 得到创建的任务ID;
			if (taskId == null || "".equals(taskId)) {
				mess = "创建任务ID失败！";
				return mess;
			}

			sendSmsOrMalServiceImpl.send(taskId, "");
			mess = "邮件已发送到您邮箱，请注意查收！";
			return mess;
		} catch (Exception e) {
			mess = "发送失败！";
			return mess;
		}
	}
}
