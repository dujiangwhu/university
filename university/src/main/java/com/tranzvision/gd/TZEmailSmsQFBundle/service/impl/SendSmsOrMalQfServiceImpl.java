package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.visitor.functions.Insert;
import com.tranzvision.gd.TZEmailParameterBundle.dao.PsTzEmlsDefTblMapper;
import com.tranzvision.gd.TZEmailParameterBundle.model.PsTzEmlsDefTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxyjQfDyTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxAttchTBLMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxMsgTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxRecTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxfslshiTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxzwlshiTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjQfDyTWithBLOBs;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxAttchTBL;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxMsgT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxRecT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxfslshiTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxzwlshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxfslshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxmbshliTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxyjfsrwTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxyjrwmxTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxzwlshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzRwzxshilTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjfjlshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjfslshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjfsrizhTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjzwlshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxfslshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxmbshliTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjfsrwTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjrwmxTblKey;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxzwlshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzRwzxshilTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfslshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfsrizhTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjzwlshiTbl;
import com.tranzvision.gd.util.base.AnalysisSysVar;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.gh.sms.SendGhSmsService;
import com.tranzvision.gd.util.mailer.TranzvisionMail;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfjlshiTbl;

/**
 * 邮件短信发送；原：TZ_SMSMAL_QF_PKG:SendSmsOrMal
 * 
 * @author tang
 * @since 2015-11-30
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.SendSmsOrMalQfServiceImpl")
public class SendSmsOrMalQfServiceImpl {
	// private TranzvisionMail mailer;

	// private String senderEmail;

	// private String PRCSINSTANCE;

	// 邮件发送主题
	// private String malSubjectContent;

	// 邮件发送内容
	// private String malContent;

	// 短信发送内容
	// private String smsContent;

	// 机构id
	// private String strJgId;

	// 模板id
	// private String strMbId;

	// 发送邮件短信元模板Id
	// private String strYmbId;

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SendGhSmsService sendSmsService;
	@Autowired
	private PsTzEmlsDefTblMapper psTzEmlsDefTblMapper;
	@Autowired
	private PsTzYjfsrizhTblMapper psTzYjfsrizhTblMapper;
	@Autowired
	private PsTzDxyjfsrwTblMapper psTzDxyjfsrwTblMapper;
	@Autowired
	private PsTzYjfslshiTblMapper psTzYjfslshiTblMapper;
	@Autowired
	private PsTzYjzwlshiTblMapper psTzYjzwlshiTblMapper;
	@Autowired
	private PsTzYjfjlshiTblMapper psTzYjfjlshiTblMapper;
	@Autowired
	private PsTzDxyjrwmxTblMapper psTzDxyjrwmxTblMapper;
	@Autowired
	private PsTzRwzxshilTblMapper psTzRwzxshilTblMapper;
	@Autowired
	private PsTzDxmbshliTblMapper psTzDxmbshliTblMapper;
	@Autowired
	private PsTzDxfslshiTblMapper psTzDxfslshiTblMapper;
	@Autowired
	private PsTzDxzwlshiTblMapper psTzDxzwlshiTblMapper;
	@Autowired
	private PsTzDxyjQfDyTMapper psTzDxyjQfDyTMapper;
	@Autowired
	private AyalysisMbSysVar ayalysisMbSysVar;
	@Autowired
	private PsTzZnxfslshiTblMapper psTzZnxfslshiTblMapper;
	@Autowired
	private PsTzZnxzwlshiTblMapper psTzZnxzwlshiTblMapper;

	@Autowired
	private PsTzZnxMsgTMapper psTzZnxMsgTMapper;
	@Autowired
	private PsTzZnxRecTMapper psTzZnxRecTMapper;
	@Autowired
	private PsTzZnxAttchTBLMapper psTzZnxAttchTBLMapper;

	// 连接邮件服务器
	public boolean connectToMailServer(TranzvisionMail mailer, String emailServerId, String strTaskId) {
		boolean isconectSuccess = false;
		try {
			PsTzEmlsDefTbl psTzEmlsDefTbl = psTzEmlsDefTblMapper.selectByPrimaryKey(emailServerId);
			if (psTzEmlsDefTbl == null) {
				this.writeTaskLog(strTaskId, "", "E", "读取邮件服务配置信息失败");
				return false;
			}

			String smtpAddr = psTzEmlsDefTbl.getTzSmtpAddr();
			String userName = psTzEmlsDefTbl.getTzUsrName();
			String password = psTzEmlsDefTbl.getTzUsrPwd();
			// 设置邮件服务器名称;
			mailer.setMailHost(smtpAddr);
			// 设置用户名;
			mailer.setUserName(userName);
			// 设置密码;
			mailer.setUserPwd(password);

			// 设置发送地址
			String emailSender, senderAlias;
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
			emailSender = psTzDxyjfsrwTbl.getTzEmailSender();
			senderAlias = psTzDxyjfsrwTbl.getTzSenderAlias();
			if (emailSender != null && !"".equals(emailSender)) {
				// senderEmail = emailSender;
				if (senderAlias == null || "".equals(senderAlias)) {
					mailer.setFromAddress(emailSender);
				} else {
					mailer.setFromAddress(emailSender, senderAlias);
				}

				/* 设置字符集 */
				mailer.setCharset("UTF-8", "UTF-8", "UTF-8", "UTF-8");
				isconectSuccess = mailer.openConnect();
				if (isconectSuccess == false) {
					this.writeTaskLog(strTaskId, "", "E", "邮件服务器链接失败");
				}
			} else {
				this.writeTaskLog(strTaskId, "", "E", "发件人邮箱为空");
			}
		} catch (Exception e) {
			return isconectSuccess;
		}
		return isconectSuccess;
	}

	// 写入日志错误信息,参数：任务ID:strTaskId,任务实例ID：strRwSlId,错误类型：strLogType,错误内容：strError
	public void writeTaskLog(String strTaskId, String strRwSlId, String strLogType, String strError) {
		String tzRzjlId = String.valueOf(getSeqNum.getSeqNum("TZ_YJFSRIZH_TBL", "TZ_RZJL_ID"));
		PsTzYjfsrizhTbl psTzYjfsrizhTbl = new PsTzYjfsrizhTbl();
		psTzYjfsrizhTbl.setTzRzjlId(tzRzjlId);
		psTzYjfsrizhTbl.setTzEmlSmsTaskId(strTaskId);
		psTzYjfsrizhTbl.setTzRwslId(strRwSlId);
		psTzYjfsrizhTbl.setTzRzjlDt(new Date());
		psTzYjfsrizhTbl.setTzYjcwLx(strLogType);
		psTzYjfsrizhTbl.setTzRzjlMs(strError);

		psTzYjfsrizhTblMapper.insert(psTzYjfsrizhTbl);
	}

	public void send(String strTaskId, String prcsinstanceId) {
		// PRCSINSTANCE = prcsinstanceId;

		PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = new PsTzDxyjfsrwTbl();
		psTzDxyjfsrwTbl.setTzEmlSmsTaskId(strTaskId);
		psTzDxyjfsrwTbl.setTzRwzxZt("B");
		psTzDxyjfsrwTbl.setTzRwzxDt(new Date());
		psTzDxyjfsrwTblMapper.updateByPrimaryKeySelective(psTzDxyjfsrwTbl);

		psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
		String strJgId = psTzDxyjfsrwTbl.getTzJgId();
		String strMbId = psTzDxyjfsrwTbl.getTzTmplId();
		String strTaskLx = psTzDxyjfsrwTbl.getTzTaskLx();
		// 邮件内容;
		String malContent = psTzDxyjfsrwTbl.getTzMalContent();
		// 批次;
		String picId = psTzDxyjfsrwTbl.getTzMlsmQfpcId();

		// 如果是任务为邮件则链接邮件服务器，否则连接短信服务器;
		if ("MAL".equals(strTaskLx)) {
			// 如果历史表中不存在，则把听众写入短信邮件任务明细表;
			String existNumSQL = "select count(1) from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID=?";
			int yjlsExistNum = jdbcTemplate.queryForObject(existNumSQL, new Object[] { strTaskId }, "Integer");
			if (yjlsExistNum == 0) {
				String deleteSQL = "delete from PS_TZ_DXYJRWMX_TBL where TZ_EML_SMS_TASK_ID=?";
				jdbcTemplate.update(deleteSQL, new Object[] { strTaskId });
				String inserSQL = "insert into PS_TZ_DXYJRWMX_TBL (select a.TZ_EML_SMS_TASK_ID,a.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID from PS_TZ_DXYJFSRW_TBL a,PS_TZ_AUDIENCE_T b,PS_TZ_AUDCYUAN_T c where a.TZ_EML_SMS_TASK_ID=? and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDIENCE_ID=c.TZ_AUDIENCE_ID )";
				jdbcTemplate.update(inserSQL, new Object[] { strTaskId });
			}

			// 原模板
			String ymbSQL = "select TZ_YMB_ID from PS_TZ_EMALTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
			String strYmbId = jdbcTemplate.queryForObject(ymbSQL, new Object[] { strJgId, strMbId }, "String");

			// PsTzYjmbshliTbl psTzYjmbshliTbl =
			// psTzYjmbshliTblMapper.selectByPrimaryKey(strTaskId);
			// 邮件主题;
			PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(picId);
			String malSubjectContent = psTzDxyjQfDyT.getTzMalSubjuect();

			// if (psTzYjmbshliTbl != null) {
			// malSubjectContent = psTzYjmbshliTbl.getTzMalSubjuect();
			// malContent = psTzYjmbshliTbl.getTzMalContent();
			// }
			this.sendEmail(strTaskId, prcsinstanceId, strJgId, strYmbId, malSubjectContent, malContent);
		} else {
			if ("SMS".equals(strTaskLx)) {
				// 如果历史表中不存在，则把听众写入短信邮件任务明细表;
				String existNumSQL = "select count(1) from PS_TZ_DXFSLSHI_TBL where TZ_EML_SMS_TASK_ID=?";
				int dxlsExistNum = jdbcTemplate.queryForObject(existNumSQL, new Object[] { strTaskId }, "Integer");
				if (dxlsExistNum == 0) {
					String deleteSQL = "delete from PS_TZ_DXYJRWMX_TBL where TZ_EML_SMS_TASK_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { strTaskId });
					String inserSQL = "insert into PS_TZ_DXYJRWMX_TBL (select a.TZ_EML_SMS_TASK_ID,a.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID from PS_TZ_DXYJFSRW_TBL a,PS_TZ_AUDIENCE_T b,PS_TZ_AUDCYUAN_T c where a.TZ_EML_SMS_TASK_ID=? and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDIENCE_ID=c.TZ_AUDIENCE_ID )";
					jdbcTemplate.update(inserSQL, new Object[] { strTaskId });
				}

				// 原模板
				String ymbSQL = "select TZ_YMB_ID from PS_TZ_SMSTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
				String strYmbId = jdbcTemplate.queryForObject(ymbSQL, new Object[] { strJgId, strMbId }, "String");

				// 短信内容;
				PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(picId);
				String smsContent = psTzDxyjQfDyT.getTzSmsContent();

				/*
				 * String smsContent = ""; PsTzDxmbshliTbl psTzDxmbshliTbl =
				 * psTzDxmbshliTblMapper.selectByPrimaryKey(strTaskId); if
				 * (psTzDxmbshliTbl != null) { smsContent =
				 * psTzDxmbshliTbl.getTzSmsContent(); }
				 */
				this.sendSms(strTaskId, prcsinstanceId, strJgId, strYmbId, smsContent);
			} else if ("ZNX".equals(strTaskLx)) {
				// 如果历史表中不存在，则把听众写入短信邮件任务明细表;
				String existNumSQL = "select count(1) from PS_TZ_ZNXFSLSHI_TBL where TZ_EML_SMS_TASK_ID=?";
				int yjlsExistNum = jdbcTemplate.queryForObject(existNumSQL, new Object[] { strTaskId }, "Integer");
				if (yjlsExistNum == 0) {
					String deleteSQL = "delete from PS_TZ_DXYJRWMX_TBL where TZ_EML_SMS_TASK_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { strTaskId });
					String inserSQL = "insert into PS_TZ_DXYJRWMX_TBL (select a.TZ_EML_SMS_TASK_ID,a.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID from PS_TZ_DXYJFSRW_TBL a,PS_TZ_AUDIENCE_T b,PS_TZ_AUDCYUAN_T c where a.TZ_EML_SMS_TASK_ID=? and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDIENCE_ID=c.TZ_AUDIENCE_ID )";
					jdbcTemplate.update(inserSQL, new Object[] { strTaskId });
				}

				// 原模板
				String ymbSQL = "select TZ_YMB_ID from PS_TZ_ZNXTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
				String strYmbId = jdbcTemplate.queryForObject(ymbSQL, new Object[] { strJgId, strMbId }, "String");

				// 邮件主题;
				PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(picId);
				String znxSubjectContent = psTzDxyjQfDyT.getTzMalSubjuect();
				String znxConjectContent = psTzDxyjQfDyT.getTzMalContent();

				this.sendZnx(strTaskId, prcsinstanceId, strJgId, strYmbId, znxSubjectContent, znxConjectContent);
			}
		}

	}

	public void sendSms(String strTaskId, String prcsinstanceId, String strJgId, String strYmbId, String smsContent) {
		// 任务发送状态，TZ_RWZX_ZT： A:未处理， B：正在处理， C:成功； D：失败;
		String updateStateSQL = "update PS_TZ_DXYJFSRW_TBL set TZ_RWZX_ZT=? WHERE TZ_EML_SMS_TASK_ID=?";

		int totalSendNum = jdbcTemplate.queryForObject(
				"select count(1) from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b where a.TZ_EML_SMS_TASK_ID=b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?",
				new Object[] { strTaskId }, "Integer");

		// 发送实例开始时间
		Date slStartTime = new Date();
		// 成功发送数;
		int successNum = 0;
		try {
			// 群发批次;
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
			String strPicID = psTzDxyjfsrwTbl.getTzMlsmQfpcId();

			String isDynamicFlg = "", sendSmsType = "", mainPhone = "", secondphone = "", audId = "", audCyId = "";
			// 循环听众任务信息;
			String sql = "select a.TZ_DYNAMIC_FLAG,a.TZ_SYSJ_LX,a.TZ_JG_ID,c.TZ_AUD_XM,c.TZ_ZY_SJ,c.TZ_CY_SJ,c.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b, PS_TZ_AUDCYUAN_T c WHERE a.TZ_EML_SMS_TASK_ID = b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDCY_ID=c.TZ_AUDCY_ID and b.TZ_AUDIENCE_ID = c.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strTaskId });
			// 是否已经解析;
			boolean bl = false;
			// 发送内容;
			String content = "";
			// 存储发送组的信息；
			List<String> audIdList = new ArrayList<>();
			List<String> audCyIdList = new ArrayList<>();
			List<String> phoneList = new ArrayList<>();

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					isDynamicFlg = (String) list.get(i).get("TZ_DYNAMIC_FLAG");
					sendSmsType = (String) list.get(i).get("TZ_SYSJ_LX");
					// jgId = (String)list.get(i).get("TZ_JG_ID");
					// xm = (String)list.get(i).get("TZ_AUD_XM");
					mainPhone = (String) list.get(i).get("TZ_ZY_SJ");
					secondphone = (String) list.get(i).get("TZ_CY_SJ");
					audId = (String) list.get(i).get("TZ_AUDIENCE_ID");
					audCyId = (String) list.get(i).get("TZ_AUDCY_ID");

					// 是否发送成功;
					boolean sendSuccess = false;
					// 发送的手机号码;
					String sendPhone = "";

					boolean blRept = false;

					if ("Y".equals(isDynamicFlg)) {
						if (bl == false) {
							content = this.analysisEmlOrSmsContent(strPicID, strJgId, strYmbId, audId, audCyId, "SMS",
									"", smsContent, "");
							// 查看最后有没有【北大光华】签名;
							/*
							 * if(content != null){ if(content.length() > 6){
							 * String qm = content.substring(
							 * content.length()-6, content.length());
							 * if(!"【北大光华】".equals(qm)){ content = content +
							 * "【北大光华】"; } }else{ content = content + "【北大光华】";
							 * } }
							 */
							bl = true;
						}
					} else {
						content = this.analysisEmlOrSmsContent(strPicID, strJgId, strYmbId, audId, audCyId, "SMS", "",
								smsContent, "");

						// 查看最后有没有【北大光华】签名;
						/*
						 * 北大短信自动加【北京大学】程序不自动加标签 if(content != null){
						 * if(content.length() > 6){ String qm =
						 * content.substring( content.length()-6,
						 * content.length()); if(!"【北大光华】".equals(qm)){ content
						 * = content + "【北大光华】"; } }else{ content = content +
						 * "【北大光华】"; } }
						 */
					}
					Map<String, String> mapRst = new HashMap<String, String>();
					String errCode = "", errMsg = "";
					// 群发都是主要手机;
					// 是否为空;
					if (mainPhone == null && "".equals(mainPhone)) {
						// 为空;
						// 任务实例编号;
						String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_DXFSLSHI_TBL", "TZ_RWSL_ID"));
						this.writeLsSmsData(strRwSlId, mainPhone, "", "NULL", strTaskId, prcsinstanceId, audCyId);
						this.deleteTaskAud(strTaskId, audId, audCyId);
						// 如果是最后一个了，且内容是相同的，则前面可能还有未发送的，则不continue;
						if (i == (list.size() - 1) && "Y".equals(isDynamicFlg) && !"".equals(sendPhone)) {

						} else {
							continue;
						}

					} else {
						// 是否重复;
						blRept = this.checkIsSendSms(strTaskId, mainPhone);
						if (blRept) {
							// 重复写邮件发送历史表,删除【TZ_DXYJRWMX_TBL】中的发送听众;
							// 任务实例编号;
							String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_DXFSLSHI_TBL", "TZ_RWSL_ID"));
							this.writeLsSmsData(strRwSlId, mainPhone, "", "RPT", strTaskId, prcsinstanceId, audCyId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							// 如果是最后一个了，且内容是相同的，则前面可能还有未发送的，则不continue;
							if (i == (list.size() - 1) && "Y".equals(isDynamicFlg) && !"".equals(sendPhone)) {

							} else {
								continue;
							}
						} else {

							if (phoneList.contains(mainPhone)) {
								// 重复写邮件发送历史表,删除【TZ_DXYJRWMX_TBL】中的发送听众;
								// 任务实例编号;
								String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_DXFSLSHI_TBL", "TZ_RWSL_ID"));
								this.writeLsSmsData(strRwSlId, mainPhone, "", "RPT", strTaskId, prcsinstanceId,
										audCyId);
								this.deleteTaskAud(strTaskId, audId, audCyId);
								// 如果是最后一个了，且内容是相同的，则前面可能还有未发送的，则不continue;
								if (i == (list.size() - 1) && "Y".equals(isDynamicFlg) && !"".equals(sendPhone)) {

								} else {
									continue;
								}
							} else {
								audIdList.add(audId);
								audCyIdList.add(audCyId);
								phoneList.add(mainPhone);
								if ("".equals(sendPhone)) {
									sendPhone = mainPhone;
								} else {
									sendPhone = sendPhone + "," + mainPhone;
								}
							}

						}
					}

					if ("Y".equals(isDynamicFlg)) {
						// 内容一样;
						// 50个手机为一组发送,等于最后一个的也发送;
						if (phoneList.size() == 50 || i == (list.size() - 1)) {
							Map<String, String> smsRstMap = sendSmsService.doSendSms(sendPhone, content);
							System.out.println("1=============>" + sendPhone + "====>" + content);
							String successCode = smsRstMap.get("code");
							String msg = smsRstMap.get("msg");
							System.out.println(successCode + "===========1=====>" + msg);
							// 成功
							if ("00".equals(successCode)) {
								for (int sendI = 0; sendI < phoneList.size(); sendI++) {
									// 任务实例编号;
									String strRwSlId = String
											.valueOf(getSeqNum.getSeqNum("TZ_DXFSLSHI_TBL", "TZ_RWSL_ID"));
									this.writeLsSmsData(strRwSlId, phoneList.get(sendI), content, "SUC", strTaskId,
											prcsinstanceId, audCyIdList.get(sendI));
									this.deleteTaskAud(strTaskId, audIdList.get(sendI), audCyIdList.get(sendI));
									successNum = successNum + 1;
								}
							} else {
								// 失败
								for (int sendI = 0; sendI < phoneList.size(); sendI++) {
									// 任务实例编号;
									String strRwSlId = String
											.valueOf(getSeqNum.getSeqNum("TZ_DXFSLSHI_TBL", "TZ_RWSL_ID"));
									this.writeLsSmsData(strRwSlId, phoneList.get(sendI), content, "FAIL", strTaskId,
											prcsinstanceId, audCyIdList.get(sendI));
									this.writeTaskLog(strTaskId, strRwSlId, successCode, msg);
								}
							}
							audIdList = new ArrayList<>();
							audCyIdList = new ArrayList<>();
							phoneList = new ArrayList<>();
						}
					} else {
						// 每次内容不一样,单个发送;
						Map<String, String> smsRstMap = sendSmsService.doSendSms(mainPhone, content);
						System.out.println("2=============>" + sendPhone + "====>" + content);
						String successCode = smsRstMap.get("code");
						String msg = smsRstMap.get("msg");
						System.out.println(successCode + "===========2=====>" + msg);
						// 任务实例编号;
						String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_DXFSLSHI_TBL", "TZ_RWSL_ID"));
						if ("00".equals(successCode)) {
							// 成功
							this.writeLsSmsData(strRwSlId, mainPhone, content, "SUC", strTaskId, prcsinstanceId,
									audCyId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							successNum = successNum + 1;
						} else {
							// 失败
							this.writeLsSmsData(strRwSlId, mainPhone, content, "FAIL", strTaskId, prcsinstanceId,
									audCyId);
							this.writeTaskLog(strTaskId, strRwSlId, successCode, msg);
						}
					}

					/*
					 * if ("A".equals(sendSmsType)) { blRept =
					 * this.checkIsSendSms(strTaskId, mainPhone); if (blRept) {
					 * // 重复写邮件发送历史表,删除【TZ_DXYJRWMX_TBL】中的发送听众;
					 * this.writeLsSmsData(strRwSlId, mainPhone, "", "RPT",
					 * strTaskId, prcsinstanceId,audCyId);
					 * this.deleteTaskAud(strTaskId, audId, audCyId); continue;
					 * } else { if (mainPhone != null && !"".equals(mainPhone))
					 * { sendPhone = mainPhone; // 发送短信; mapRst =
					 * sendSmsService.doSendSms(mainPhone, content); if
					 * (mapRst.get("msg") != null &&
					 * !"".equals(mapRst.get("msg")) &&
					 * !"发送成功".equals(mapRst.get("msg"))) { errCode =
					 * mapRst.get("code"); errMsg = mapRst.get("msg");
					 * 
					 * this.writeTaskLog(strTaskId, strRwSlId, errCode, errMsg);
					 * } else { sendSuccess = true; }
					 * 
					 * } else { // 为空; this.writeLsSmsData(strRwSlId, mainPhone,
					 * "", "NULL", strTaskId, prcsinstanceId,audCyId);
					 * this.deleteTaskAud(strTaskId, audId, audCyId); continue;
					 * } } }
					 * 
					 * // 次要手机;; if ("B".equals(sendSmsType)) { blRept =
					 * this.checkIsSendSms(strTaskId, secondphone); if (blRept)
					 * { // 重复写邮件发送历史表,删除【TZ_DXYJRWMX_TBL】中的发送听众
					 * this.writeLsSmsData(strRwSlId, secondphone, "", "RPT",
					 * strTaskId, prcsinstanceId,audCyId);
					 * this.deleteTaskAud(strTaskId, audId, audCyId); continue;
					 * } else { if (secondphone != null &&
					 * !"".equals(secondphone)) { sendPhone = secondphone; //
					 * 发送短信 mapRst = sendSmsService.doSendSms(secondphone,
					 * content); if (mapRst.get("msg") != null &&
					 * !"".equals(mapRst.get("msg"))) { errCode =
					 * mapRst.get("code"); errMsg = mapRst.get("msg");
					 * this.writeTaskLog(strTaskId, strRwSlId, errCode, errMsg);
					 * } else { sendSuccess = true; } } else { // 为空;
					 * this.writeLsSmsData(strRwSlId, secondphone, "", "NULL",
					 * strTaskId, prcsinstanceId,audCyId);
					 * this.deleteTaskAud(strTaskId, audId, audCyId); continue;
					 * } } }
					 * 
					 * // 所有手机; if ("C".equals(sendSmsType)) { // 主要手机是否重复;
					 * boolean blRept1 = this.checkIsSendSms(strTaskId,
					 * mainPhone); // 次要手机是否重复; boolean blRept2 =
					 * this.checkIsSendSms(strTaskId, secondphone); if (blRept1
					 * && blRept2) { // 重复写邮件发送历史表,删除【TZ_DXYJRWMX_TBL】中的发送听众
					 * this.writeLsSmsData(strRwSlId, mainPhone + "," +
					 * secondphone, "", "RPT", strTaskId,
					 * prcsinstanceId,audCyId); this.deleteTaskAud(strTaskId,
					 * audId, audCyId); continue; } else { if (mainPhone != null
					 * && !"".equals(mainPhone) && secondphone != null &&
					 * !"".equals(secondphone)) { if (mainPhone != null &&
					 * !"".equals(mainPhone)) { sendPhone = mainPhone; // 发送短信接口
					 * mapRst = sendSmsService.doSendSms(mainPhone, content); if
					 * (mapRst.get("msg") != null &&
					 * !"".equals(mapRst.get("msg"))) { errMsg =
					 * mapRst.get("msg"); } else { sendSuccess = true; } }
					 * 
					 * if (secondphone != null && !"".equals(secondphone)) { if
					 * ("".equals(sendPhone)) { sendPhone = secondphone; } else
					 * { sendPhone = sendPhone + "," + secondphone; }
					 * 
					 * // 发送短信; mapRst = sendSmsService.doSendSms(secondphone,
					 * content); if (mapRst.get("msg") != null &&
					 * !"".equals(mapRst.get("msg"))) { if ("".equals(errCode))
					 * { errCode = mapRst.get("code"); } else { errCode =
					 * errCode + ";" + mapRst.get("code"); } if
					 * ("".equals(errMsg)) { errMsg = mapRst.get("msg"); } else
					 * { errMsg = errMsg + ";" + mapRst.get("msg"); }
					 * 
					 * } else { sendSuccess = true; }
					 * 
					 * } if (sendSuccess != true) { this.writeTaskLog(strTaskId,
					 * strRwSlId, errCode, errMsg); }
					 * 
					 * } else { // 为空; this.writeLsSmsData(strRwSlId,
					 * secondphone, "", "NULL", strTaskId,
					 * prcsinstanceId,audCyId); this.deleteTaskAud(strTaskId,
					 * audId, audCyId); continue; }
					 * 
					 * } }
					 * 
					 * 
					 * if (sendSuccess == true) { this.writeLsSmsData(strRwSlId,
					 * sendPhone, content, "SUC", strTaskId,
					 * prcsinstanceId,audCyId); this.deleteTaskAud(strTaskId,
					 * audId, audCyId); successNum = successNum + 1; } else {
					 * this.writeLsSmsData(strRwSlId, sendPhone, content,
					 * "FAIL", strTaskId, prcsinstanceId,audCyId); }
					 */
				}
			}

			// 成功;
			jdbcTemplate.update(updateStateSQL, new Object[] { "C", strTaskId });

			// 写发送历史表;
			PsTzRwzxshilTbl psTzRwzxshilTbl = new PsTzRwzxshilTbl();
			psTzRwzxshilTbl.setTzZdBh(String.valueOf(getSeqNum.getSeqNum("TZ_RWZXSHIL_TBL", "TZ_ZD_BH")));
			psTzRwzxshilTbl.setTzJcslId(prcsinstanceId);
			psTzRwzxshilTbl.setTzJcslZt("SUC");
			psTzRwzxshilTbl.setTzEmlSmsTaskId(strTaskId);
			psTzRwzxshilTbl.setTzSltjDt(slStartTime);
			psTzRwzxshilTbl.setTzSlksDt(slStartTime);
			psTzRwzxshilTbl.setTzSljsDt(new Date());
			psTzRwzxshilTbl.setTzSuccNum(successNum);
			psTzRwzxshilTbl.setTzFailNum(totalSendNum - successNum);
			psTzRwzxshilTbl.setTzJgId(strJgId);
			psTzRwzxshilTblMapper.insert(psTzRwzxshilTbl);

		} catch (Exception e) {
			// 错误日志;
			this.writeTaskLog(strTaskId, "", "D", e.toString());
			// 失败;
			jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
		}
	}

	public void sendEmail(String strTaskId, String prcsinstanceId, String strJgId, String strYmbId,
			String malSubjectContent, String malContent) {

		// 任务发送状态，TZ_RWZX_ZT： A:未处理， B：正在处理， C:成功； D：失败;
		String updateStateSQL = "update PS_TZ_DXYJFSRW_TBL set TZ_RWZX_ZT=? WHERE TZ_EML_SMS_TASK_ID=?";
		try {
			// 得到发送的任务;
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
			if (psTzDxyjfsrwTbl != null) {
				// 群发批次;
				String strPicID = psTzDxyjfsrwTbl.getTzMlsmQfpcId();

				// 查询抄送地址和密送地址;
				/*
				 * String cc_Address = "", bc_Address = ""; PsTzMalCcAddT
				 * psTzMalCcAddT =
				 * psTzMalCcAddTMapper.selectByPrimaryKey(strTaskId);
				 * PsTzMalBcAddT psTzMalBcAddT =
				 * psTzMalBcAddTMapper.selectByPrimaryKey(strTaskId); if
				 * (psTzMalCcAddT != null && psTzMalCcAddT.getTzMalCcAddr() !=
				 * null) { cc_Address = psTzMalCcAddT.getTzMalCcAddr(); } if
				 * (psTzMalBcAddT != null && psTzMalBcAddT.getTzMalBcAddr() !=
				 * null) { bc_Address = psTzMalBcAddT.getTzMalBcAddr(); }
				 */
				/* 抄送 */
				String cc_Address = "";
				/* 强制推送 */
				String forcedPush = "";
				/* 启用频发策略 */
				String useFrequent = "";
				/* 每小时发送邮件数量 */
				int hourSendNum = 0;
				// 发送: 一般发送 or excel导入
				String sendModel = "";

				int count = 0;

				long totalSleepTime = 0; /* 程序睡眠时间 */

				PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(strPicID);
				cc_Address = psTzDxyjQfDyT.getTzMailCc();
				forcedPush = psTzDxyjQfDyT.getTzQztsFlag();
				useFrequent = psTzDxyjQfDyT.getTzQypfclFlag();
				hourSendNum = psTzDxyjQfDyT.getTzXsfssl();
				sendModel = psTzDxyjQfDyT.getTzSendModel();
				// 查询总的发送数量;
				String totalSQL = "select count(1) from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b where a.TZ_EML_SMS_TASK_ID=b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
				int totalSendNum = jdbcTemplate.queryForObject(totalSQL, new Object[] { strTaskId }, "Integer");
				// 实例开始时间;
				Date slStartTime = new Date();

				TranzvisionMail mailer = new TranzvisionMail();
				// 邮件服务器;
				String strYjFwqId = psTzDxyjfsrwTbl.getTzEmlservId();
				// 链接邮件服务器
				boolean isConnect = this.connectToMailServer(mailer, strYjFwqId, strTaskId);
				if (isConnect == false) {
					// 失败;
					jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
					return;
				}

				// 是否中断发送;
				boolean isStopSend = false;
				// 是否已经解析;
				boolean bl = false;
				// 发送内容;
				String content = "";
				// 发送成功数;
				int successNum = 0;

				Date startDateTime = null;
				// 循环听众任务信息;
				String sql = "select a.TZ_WEBMAL_FLAG,a.TZ_DYNAMIC_FLAG,a.TZ_SYYX_LX,a.TZ_JG_ID,c.TZ_AUD_XM,c.TZ_ZY_EMAIL,c.TZ_CY_EMAIL,c.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID,a.TZ_EML_IF_PRT from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b, PS_TZ_AUDCYUAN_T c WHERE a.TZ_EML_SMS_TASK_ID = b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDCY_ID=c.TZ_AUDCY_ID and b.TZ_AUDIENCE_ID = c.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strTaskId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {

						// 群发任务是否终止发送，如果终止发送退出循环;
						String stopFlag = jdbcTemplate.queryForObject(
								"SELECT TZ_OFF_SEND FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?",
								new Object[] { strPicID }, "String");
						if ("Y".equals(stopFlag)) {
							isStopSend = true;
							break;
						}

						// 听众id;
						String audId = (String) list.get(i).get("TZ_AUDIENCE_ID");
						// 听众参与人ID;
						String audCyId = (String) list.get(i).get("TZ_AUDCY_ID");
						// 是否以HTML格式发送;
						String strIsSendAsHtml = (String) list.get(i).get("TZ_WEBMAL_FLAG");
						// 是否动态内容;
						String isDynamicFlg = (String) list.get(i).get("TZ_DYNAMIC_FLAG");
						// 发送给什么邮箱: 主要邮箱，次要邮箱，所有邮箱;
						String sendEmailType = (String) list.get(i).get("TZ_SYYX_LX");
						// 机构ID;
						// String jgId = (String) list.get(i).get("TZ_JG_ID");
						// 姓名;
						// String xm = (String) list.get(i).get("TZ_AUD_XM");
						// 主要邮箱;
						String mainEmail = (String) list.get(i).get("TZ_ZY_EMAIL");
						// 次要邮箱;
						String secondEmail = (String) list.get(i).get("TZ_CY_EMAIL");
						// 是否判重;
						String emlIfRpt = (String) list.get(i).get("TZ_EML_IF_PRT");
						// 任务实例编号;
						String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_YJFSLSHI_TBL", "TZ_RWSL_ID"));

						boolean blRept = false;

						// 是否强制推送，如果不是，退订邮箱不发送;
						if (!"Y".equals(forcedPush)) {
							// 退订标识;
							int debookFlagCount = jdbcTemplate.queryForObject(
									"SELECT count(1) FROM PS_TZ_YJQFTD_TBL WHERE upper(TZ_EMAIL)=upper(?) AND TZ_QXDY_FLAG='Y'",
									new Object[] { mainEmail }, "Integer");
							if (debookFlagCount > 0) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, mainEmail, "", "", "FAIL", strTaskId, prcsinstanceId,
										audCyId);
								this.writeTaskLog(strTaskId, strRwSlId, "D", "退订用户");
								this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
								this.deleteTaskAud(strTaskId, audId, audCyId);
								continue;
							}

						}

						/** 记录发送开始时间 **/
						long totalTimeConsuming = 0; /* 发送邮件总耗时(单位：秒),不包含睡眠时间 */
						long preTimeConsuming = 0; /* 发送一封邮件的平均耗时 */
						if (count == 0) {
							startDateTime = new Date();
						} else {
							if ("Y".equals(useFrequent)) {
								Date currDateTime = new Date();

								totalTimeConsuming = currDateTime.getTime() - startDateTime.getTime() - totalSleepTime;
								preTimeConsuming = (long) totalTimeConsuming / count;

								/* 计算1小时内发送的数量 */
								if (preTimeConsuming == 0) {
									preTimeConsuming = 1;
								}

								long maybeSendNum = (long) (60 * 60 * 1000 / preTimeConsuming);
								if (maybeSendNum > hourSendNum) {
									long sleepTime = (long) ((3600 * 1000 / hourSendNum - preTimeConsuming) * 0.9);

									Thread.sleep(sleepTime);
									totalSleepTime = totalSleepTime + sleepTime;
								}
							}
						}

						// 邮件接收地址
						String emailAddrAdd = "";
						// 主要邮箱;
						if ("A".equals(sendEmailType)) {
							emailAddrAdd = "";
							if ("Y".equals(emlIfRpt)) {
								blRept = this.checkIsSendEmail(strTaskId, mainEmail);
							}
							if (blRept) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, mainEmail, "", "", "RPT", strTaskId, prcsinstanceId,
										audCyId);
								this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
								this.deleteTaskAud(strTaskId, audId, audCyId);
							} else {
								emailAddrAdd = mainEmail;
							}
						}

						// 次要邮箱;
						if ("B".equals(sendEmailType)) {
							emailAddrAdd = "";
							if ("Y".equals(emlIfRpt)) {
								blRept = this.checkIsSendEmail(strTaskId, secondEmail);
							}
							if (blRept) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, secondEmail, "", "", "RPT", strTaskId, prcsinstanceId,
										audCyId);
								this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
								this.deleteTaskAud(strTaskId, audId, audCyId);
							} else {
								emailAddrAdd = secondEmail;
							}
						}

						// 所有邮箱;
						if ("C".equals(sendEmailType)) {
							emailAddrAdd = "";
							if ("Y".equals(emlIfRpt)) {
								blRept = this.checkIsSendEmail(strTaskId, mainEmail);
							}
							if (blRept) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, mainEmail, "", "", "RPT", strTaskId, prcsinstanceId,
										audCyId);
								this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
								this.deleteTaskAud(strTaskId, audId, audCyId);
							} else {
								emailAddrAdd = mainEmail;
							}

							if ("Y".equals(emlIfRpt)) {
								blRept = this.checkIsSendEmail(strTaskId, secondEmail);
							}
							if (blRept) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, secondEmail, "", "", "RPT", strTaskId, prcsinstanceId,
										audCyId);
								this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
								this.deleteTaskAud(strTaskId, audId, audCyId);
							} else {
								if (emailAddrAdd == null || "".equals(emailAddrAdd)) {
									emailAddrAdd = secondEmail;
								} else {
									emailAddrAdd = emailAddrAdd + ";" + secondEmail;
								}
							}
						}

						if (emailAddrAdd == null || "".equals(emailAddrAdd)) {
							this.writeLsMalData(strRwSlId, "", "", "", "FAIL", strTaskId, prcsinstanceId, audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						}

						// 添加附件;
						// String fjSQL = "SELECT
						// TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FJIAN_LJ from
						// PS_TZ_RW_FJIAN_TBL where TZ_EML_SMS_TASK_ID=?";
						String fjSQL = "SELECT TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FJIAN_LJ FROM PS_TZ_YJQFFJXX_TBL WHERE TZ_MLSM_QFPC_ID=?";
						List<Map<String, Object>> fjList = jdbcTemplate.queryForList(fjSQL, new Object[] { strPicID });
						if (fjList != null && fjList.size() > 0) {
							for (int fjnum = 0; fjnum < fjList.size(); fjnum++) {
								String fjlj = (String) fjList.get(fjnum).get("TZ_FJIAN_LJ");
								String fjmc = (String) fjList.get(fjnum).get("TZ_FJIAN_MC");
								if (fjlj != null && !"".equals(fjlj) && fjmc != null && !"".equals(fjmc)) {
									// fjlj =
									// request.getSession().getServletContext().getRealPath(fjlj);
									File file = new File(fjlj);
									if (file.exists() && file.isFile()) {
										mailer.setAttachment(fjlj, fjmc);
									} else {
										// 错误日志
										this.writeTaskLog(strTaskId, strRwSlId, "D", "不存在该路径:" + fjlj);
									}
								}
							}
						}

						if ("Y".equals(isDynamicFlg)) {
							if (bl == false) {
								content = this.analysisEmlOrSmsContent(strPicID, strJgId, strYmbId, audId, audCyId,
										"MAL", malContent, "", "");
								bl = true;
							}
						} else {
							content = this.analysisEmlOrSmsContent(strPicID, strJgId, strYmbId, audId, audCyId, "MAL",
									malContent, "", "");
						}

						// 邮件内容链接中听众占位符替换;
						content = content.replace("[邮件群发.听众成员编号占位符]", audCyId);
						// 收件人邮箱加密;
						String emailAddrAddEncrypt = DESUtil.encrypt(emailAddrAdd, "Tranzvision_Mail");
						content = content.replace("[TRANZVISION_YJQF_PC_ID_BZ.邮件群发.加密收件邮箱地址]",
								emailAddrAddEncrypt);

						// 设置收件人
						mailer.setToAddress(emailAddrAdd);
						// 设置抄送人
						if (cc_Address != null && !"".equals(cc_Address)) {
							mailer.setToCCAddr(cc_Address);
						}
						// 设置密送人
						// if (bc_Address != null && !"".equals(bc_Address)) {
						// mailer.setToBCCAddr(bc_Address);
						// }

						mailer.setMailSubject(malSubjectContent);

						if ("Y".equals(strIsSendAsHtml)) {
							mailer.setMailBody(content, true);
						} else {
							mailer.setMailBody(content, false);
						}

						boolean ismail = mailer.sendMail();
						if (ismail) {
							// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
							this.writeLsMalData(strRwSlId, emailAddrAdd, malSubjectContent, content, "SUC", strTaskId,
									prcsinstanceId, audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							successNum = successNum + 1;
						} else {
							String logEmailSendFalseMsg = mailer.getErrorInfo();
							this.writeTaskLog(strTaskId, strRwSlId, "D", logEmailSendFalseMsg);
							// 发送失败写邮件发送历史表，附件历史表
							this.writeLsMalData(strRwSlId, emailAddrAdd, malSubjectContent, content, "FAIL", strTaskId,
									prcsinstanceId, audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
						}

						count = count + 1;

					}
				}

				// 是否中断;
				if (isStopSend) {
					// 中断;
					jdbcTemplate.update(updateStateSQL, new Object[] { "E", strTaskId });
				} else {
					// 成功;
					jdbcTemplate.update(updateStateSQL, new Object[] { "C", strTaskId });
				}

				// 写发送历史表;
				PsTzRwzxshilTbl psTzRwzxshilTbl = new PsTzRwzxshilTbl();
				psTzRwzxshilTbl.setTzZdBh(String.valueOf(getSeqNum.getSeqNum("TZ_RWZXSHIL_TBL", "TZ_ZD_BH")));
				psTzRwzxshilTbl.setTzJcslId(prcsinstanceId);
				psTzRwzxshilTbl.setTzJcslZt("SUC");
				psTzRwzxshilTbl.setTzEmlSmsTaskId(strTaskId);
				psTzRwzxshilTbl.setTzSltjDt(slStartTime);
				psTzRwzxshilTbl.setTzSlksDt(slStartTime);
				psTzRwzxshilTbl.setTzSljsDt(new Date());
				psTzRwzxshilTbl.setTzSuccNum(successNum);
				psTzRwzxshilTbl.setTzFailNum(totalSendNum - successNum);
				psTzRwzxshilTbl.setTzJgId(strJgId);
				psTzRwzxshilTblMapper.insert(psTzRwzxshilTbl);

			} else {
				// 错误日志
				this.writeTaskLog(strTaskId, "", "D", "为找到任务id为：[" + strTaskId + "],相应的邮件发送任务");
				// 失败;
				jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 错误日志;
			this.writeTaskLog(strTaskId, "", "D", e.toString());
			// 失败;
			jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
		}

	}

	public void sendZnx(String strTaskId, String prcsinstanceId, String strJgId, String strYmbId,
			String znxSubjectContent, String znxContent) {

		// 任务发送状态，TZ_RWZX_ZT： A:未处理， B：正在处理， C:成功； D：失败;
		String updateStateSQL = "update PS_TZ_DXYJFSRW_TBL set TZ_RWZX_ZT=? WHERE TZ_EML_SMS_TASK_ID=?";
		// 任务添加人;
		String sendOPRID = "", TZ_MLSM_QFPC_ID = "";
		Map<String, Object> qfxxMap = jdbcTemplate.queryForMap(
				"select TZ_MLSM_QFPC_ID,ROW_ADDED_OPRID from PS_TZ_DXYJFSRW_TBL where TZ_EML_SMS_TASK_ID=?",
				new Object[] { strTaskId });
		if (qfxxMap == null) {
			return;
		} else {
			TZ_MLSM_QFPC_ID = (String) qfxxMap.get("TZ_MLSM_QFPC_ID");
			sendOPRID = (String) qfxxMap.get("ROW_ADDED_OPRID");
		}
		try {
			// 得到发送的任务;
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
			if (psTzDxyjfsrwTbl != null) {
				// 群发批次;
				String strPicID = psTzDxyjfsrwTbl.getTzMlsmQfpcId();

				int count = 0;

				// 查询总的发送数量;
				String totalSQL = "select count(1) from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b where a.TZ_EML_SMS_TASK_ID=b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
				int totalSendNum = jdbcTemplate.queryForObject(totalSQL, new Object[] { strTaskId }, "Integer");
				// 实例开始时间;
				Date slStartTime = new Date();

				// 是否中断发送;
				boolean isStopSend = false;
				// 是否已经解析;
				boolean bl = false;
				// 发送内容;
				String content = "";
				// 发送成功数;
				int successNum = 0;

				Date startDateTime = null;

				String tzZnxMsgid = String.valueOf(getSeqNum.getSeqNum("PS_TZ_ZNX_MSG_T", "TZ_ZNX_MSGID"));
				// 循环听众任务信息;
				String sql = "select a.TZ_DYNAMIC_FLAG,a.TZ_JG_ID,c.TZ_AUD_XM,c.OPRID,c.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID,a.TZ_EML_IF_PRT from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b, PS_TZ_AUDCYUAN_T c WHERE a.TZ_EML_SMS_TASK_ID = b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDCY_ID=c.TZ_AUDCY_ID and b.TZ_AUDIENCE_ID = c.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strTaskId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {

						// 群发任务是否终止发送，如果终止发送退出循环;
						String stopFlag = jdbcTemplate.queryForObject(
								"SELECT TZ_OFF_SEND FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?",
								new Object[] { strPicID }, "String");
						if ("Y".equals(stopFlag)) {
							isStopSend = true;
							break;
						}

						// 听众id;
						String audId = (String) list.get(i).get("TZ_AUDIENCE_ID");
						// 听众参与人ID;
						String audCyId = (String) list.get(i).get("TZ_AUDCY_ID");
						// 是否动态内容;
						String isDynamicFlg = (String) list.get(i).get("TZ_DYNAMIC_FLAG");
						// 机构ID;
						// String jgId = (String) list.get(i).get("TZ_JG_ID");
						// 姓名;
						// String xm = (String) list.get(i).get("TZ_AUD_XM");
						// 收件人;
						String oprid = (String) list.get(i).get("OPRID");
						// 任务实例编号;
						String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_YJFSLSHI_TBL", "TZ_RWSL_ID"));

						boolean blRept = false;
						if (oprid == null || "".equals(oprid)) {
							this.writeLsZnxData(strRwSlId, oprid, "", "", "NULL", strTaskId, prcsinstanceId, audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						}

						blRept = this.checkIsSendZnx(strTaskId, oprid);
						if (blRept) {
							// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
							this.writeLsZnxData(strRwSlId, oprid, "", "", "RPT", strTaskId, prcsinstanceId, audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						}

						if ("Y".equals(isDynamicFlg)) {
							if (bl == false) {
								content = this.analysisEmlOrSmsContent(strPicID, strJgId, strYmbId, audId, audCyId,
										"ZNX", "", "", znxContent);
								bl = true;
							}
						} else {
							content = this.analysisEmlOrSmsContent(strPicID, strJgId, strYmbId, audId, audCyId, "ZNX",
									"", "", znxContent);
						}

						// 写站内信信息表；
						PsTzZnxMsgT psTzZnxMsgT = psTzZnxMsgTMapper.selectByPrimaryKey(tzZnxMsgid);
						if (psTzZnxMsgT == null) {
							psTzZnxMsgT = new PsTzZnxMsgT();

							psTzZnxMsgT.setTzZnxMsgid(tzZnxMsgid);
							psTzZnxMsgT.setTzZnxSendid(sendOPRID);
							psTzZnxMsgT.setTzMsgSubject(znxSubjectContent);
							psTzZnxMsgT.setTzMlsmQfpcId(TZ_MLSM_QFPC_ID);
							psTzZnxMsgT.setRowAddedDttm(new Date());
							psTzZnxMsgT.setRowAddedOprid(sendOPRID);
							psTzZnxMsgT.setRowLastmantDttm(new Date());
							psTzZnxMsgT.setRowLastmantOprid(sendOPRID);
							psTzZnxMsgTMapper.insert(psTzZnxMsgT);
						}

						PsTzZnxRecT psTzZnxRecT = new PsTzZnxRecT();
						psTzZnxRecT.setTzZnxMsgid(tzZnxMsgid);
						psTzZnxRecT.setTzZnxRecid(oprid);
						psTzZnxRecT.setTzZnxStatus("N");
						psTzZnxRecT.setTzRecDelstatus("N");
						psTzZnxRecT.setTzMsgText(content);
						int insert = psTzZnxRecTMapper.insert(psTzZnxRecT);

						// 添加附件;
						// String fjSQL = "SELECT
						// TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FJIAN_LJ from
						// PS_TZ_RW_FJIAN_TBL where TZ_EML_SMS_TASK_ID=?";
						String fjSQL = "SELECT TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FILE_PATH FROM PS_TZ_YJQFFJXX_TBL WHERE TZ_MLSM_QFPC_ID=?";
						List<Map<String, Object>> fjList = jdbcTemplate.queryForList(fjSQL, new Object[] { strPicID });
						if (fjList != null && fjList.size() > 0) {
							for (int fjnum = 0; fjnum < fjList.size(); fjnum++) {
								String fjId = (String) fjList.get(fjnum).get("TZ_FJIAN_ID");
								String fjlj = (String) fjList.get(fjnum).get("TZ_FILE_PATH");
								String fjmc = (String) fjList.get(fjnum).get("TZ_FJIAN_MC");
								if (fjlj != null && !"".equals(fjlj) && fjmc != null && !"".equals(fjmc)) {
									PsTzZnxAttchTBL psTzZnxAttchTBL = new PsTzZnxAttchTBL();
									psTzZnxAttchTBL.setTzZnxMsgid(tzZnxMsgid);
									psTzZnxAttchTBL.setTzFjianId(fjId);
									psTzZnxAttchTBL.setTzFjianMc(fjmc);
									psTzZnxAttchTBL.setTzFjianLj(fjlj);
									psTzZnxAttchTBLMapper.insert(psTzZnxAttchTBL);
								}
							}
						}

						if (insert > 0) {
							// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
							this.writeLsZnxData(strRwSlId, oprid, znxSubjectContent, content, "SUC", strTaskId,
									prcsinstanceId, audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							successNum = successNum + 1;
						} else {
							this.writeTaskLog(strTaskId, strRwSlId, "D", "数据插入失败");
							// 发送失败写邮件发送历史表，附件历史表
							this.writeLsZnxData(strRwSlId, oprid, znxSubjectContent, content, "FAIL", strTaskId,
									prcsinstanceId, audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId, strPicID);
						}

						count = count + 1;

					}
				}

				// 是否中断;
				if (isStopSend) {
					// 中断;
					jdbcTemplate.update(updateStateSQL, new Object[] { "E", strTaskId });
				} else {
					// 成功;
					jdbcTemplate.update(updateStateSQL, new Object[] { "C", strTaskId });
				}

				// 写发送历史表;
				PsTzRwzxshilTbl psTzRwzxshilTbl = new PsTzRwzxshilTbl();
				psTzRwzxshilTbl.setTzZdBh(String.valueOf(getSeqNum.getSeqNum("TZ_RWZXSHIL_TBL", "TZ_ZD_BH")));
				psTzRwzxshilTbl.setTzJcslId(prcsinstanceId);
				psTzRwzxshilTbl.setTzJcslZt("SUC");
				psTzRwzxshilTbl.setTzEmlSmsTaskId(strTaskId);
				psTzRwzxshilTbl.setTzSltjDt(slStartTime);
				psTzRwzxshilTbl.setTzSlksDt(slStartTime);
				psTzRwzxshilTbl.setTzSljsDt(new Date());
				psTzRwzxshilTbl.setTzSuccNum(successNum);
				psTzRwzxshilTbl.setTzFailNum(totalSendNum - successNum);
				psTzRwzxshilTbl.setTzJgId(strJgId);
				psTzRwzxshilTblMapper.insert(psTzRwzxshilTbl);

			} else {
				// 错误日志
				this.writeTaskLog(strTaskId, "", "D", "为找到任务id为：[" + strTaskId + "],相应的邮件发送任务");
				// 失败;
				jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 错误日志;
			this.writeTaskLog(strTaskId, "", "D", e.toString());
			// 失败;
			jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
		}

	}

	// *检查在某个任务中某个邮箱是否已经发送过邮件;
	private boolean checkIsSendEmail(String strTaskId, String emailAddress) {
		String sql = "select count(1) from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID=? and TZ_JS_EMAIL like ? and TZ_FS_ZT = 'SUC'";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { strTaskId, "%" + emailAddress + "%" }, "Integer");
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	// *检查在某个任务中某个人员是否已经发送过站内信;
	private boolean checkIsSendZnx(String strTaskId, String oprid) {
		String sql = "select count(1) from PS_TZ_ZNXFSLSHI_TBL where TZ_EML_SMS_TASK_ID=? and OPRID = ? and TZ_FS_ZT = 'SUC'";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { strTaskId, oprid }, "Integer");
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 写邮件历史数据表
	private void writeLsMalData(String strRwSlId, String sjxEmail, String tj, String content, String strFsZt,
			String strTaskId, String prcsinstanceId, String tzAudcyId) {
		PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
		String senderEmail = "";
		if (psTzDxyjfsrwTbl != null) {
			senderEmail = psTzDxyjfsrwTbl.getTzEmailSender();
		}
		String strRwSlId_old = jdbcTemplate.queryForObject(
				"SELECT TZ_RWSL_ID FROM PS_TZ_YJFSLSHI_TBL WHERE TZ_AUDCY_ID=? AND TZ_EML_SMS_TASK_ID=? limit 0,1",
				new Object[] { tzAudcyId, strTaskId }, "String");
		if (strRwSlId_old != null && !"".equals(strRwSlId_old)) {
			PsTzYjfslshiTbl psTzYjfslshiTbl = psTzYjfslshiTblMapper.selectByPrimaryKey(strRwSlId_old);
			psTzYjfslshiTbl.setTzFsEmail(senderEmail);
			psTzYjfslshiTbl.setTzJsEmail(sjxEmail);
			psTzYjfslshiTbl.setTzEmZhuti(tj);
			psTzYjfslshiTbl.setTzFsDt(new Date());
			psTzYjfslshiTbl.setTzEmlSmsTaskId(strTaskId);
			psTzYjfslshiTbl.setTzFsZt(strFsZt);
			psTzYjfslshiTbl.setTzJcslId(prcsinstanceId);
			psTzYjfslshiTbl.setTzAudcyId(tzAudcyId);
			psTzYjfslshiTblMapper.updateByPrimaryKey(psTzYjfslshiTbl);

			PsTzYjzwlshiTbl psTzYjzwlshiTbl = psTzYjzwlshiTblMapper.selectByPrimaryKey(strRwSlId_old);
			if (psTzYjzwlshiTbl != null) {
				psTzYjzwlshiTbl.setTzYjfsRq(new Date());
				psTzYjzwlshiTbl.setTzYjZhwen(content);
				psTzYjzwlshiTblMapper.updateByPrimaryKeyWithBLOBs(psTzYjzwlshiTbl);
			} else {
				// 邮件发送内容历史表
				psTzYjzwlshiTbl = new PsTzYjzwlshiTbl();
				psTzYjzwlshiTbl.setTzRwslId(strRwSlId);
				psTzYjzwlshiTbl.setTzYjfsRq(new Date());
				psTzYjzwlshiTbl.setTzYjZhwen(content);
				psTzYjzwlshiTblMapper.insert(psTzYjzwlshiTbl);
			}
		} else {
			// 邮件发送历史表;
			PsTzYjfslshiTbl psTzYjfslshiTbl = new PsTzYjfslshiTbl();
			psTzYjfslshiTbl.setTzRwslId(strRwSlId);
			psTzYjfslshiTbl.setTzFsEmail(senderEmail);
			psTzYjfslshiTbl.setTzJsEmail(sjxEmail);
			psTzYjfslshiTbl.setTzEmZhuti(tj);
			psTzYjfslshiTbl.setTzFsDt(new Date());
			psTzYjfslshiTbl.setTzEmlSmsTaskId(strTaskId);
			psTzYjfslshiTbl.setTzFsZt(strFsZt);
			psTzYjfslshiTbl.setTzJcslId(prcsinstanceId);
			psTzYjfslshiTbl.setTzAudcyId(tzAudcyId);
			psTzYjfslshiTblMapper.insert(psTzYjfslshiTbl);

			// 邮件发送内容历史表
			PsTzYjzwlshiTbl psTzYjzwlshiTbl = new PsTzYjzwlshiTbl();
			psTzYjzwlshiTbl.setTzRwslId(strRwSlId);
			psTzYjzwlshiTbl.setTzYjfsRq(new Date());
			psTzYjzwlshiTbl.setTzYjZhwen(content);
			psTzYjzwlshiTblMapper.insert(psTzYjzwlshiTbl);
		}

	}

	// 写站内信历史数据表
	private void writeLsZnxData(String strRwSlId, String oprid, String tj, String content, String strFsZt,
			String strTaskId, String prcsinstanceId, String tzAudcyId) {

		String strRwSlId_old = jdbcTemplate.queryForObject(
				"select TZ_RWSL_ID from PS_TZ_ZNXFSLSHI_TBL WHERE TZ_AUDCY_ID=? AND TZ_EML_SMS_TASK_ID=? limit 0,1",
				new Object[] { tzAudcyId, strTaskId }, "String");
		if (strRwSlId_old != null && !"".equals(strRwSlId_old)) {
			PsTzZnxfslshiTbl psTzZnxfslshiTbl = psTzZnxfslshiTblMapper.selectByPrimaryKey(strRwSlId_old);
			psTzZnxfslshiTbl.setOprid(oprid);
			psTzZnxfslshiTbl.setTzZnxZt(tj);
			psTzZnxfslshiTbl.setTzFsDt(new Date());
			psTzZnxfslshiTbl.setTzEmlSmsTaskId(strTaskId);
			psTzZnxfslshiTbl.setTzFsZt(strFsZt);
			psTzZnxfslshiTbl.setTzJcslId(prcsinstanceId);
			psTzZnxfslshiTbl.setTzAudcyId(tzAudcyId);
			psTzZnxfslshiTblMapper.updateByPrimaryKey(psTzZnxfslshiTbl);

			// 站内信发送内容历史表
			PsTzZnxzwlshiTbl psTzZnxzwlshiTbl = psTzZnxzwlshiTblMapper.selectByPrimaryKey(strRwSlId_old);
			if (psTzZnxzwlshiTbl != null) {
				psTzZnxzwlshiTbl.setTzYjfsRq(new Date());
				psTzZnxzwlshiTbl.setTzZnxZhwen(content);
				psTzZnxzwlshiTblMapper.updateByPrimaryKeyWithBLOBs(psTzZnxzwlshiTbl);
			} else {
				psTzZnxzwlshiTbl = new PsTzZnxzwlshiTbl();
				psTzZnxzwlshiTbl.setTzRwslId(strRwSlId);
				psTzZnxzwlshiTbl.setTzYjfsRq(new Date());
				psTzZnxzwlshiTbl.setTzZnxZhwen(content);
				psTzZnxzwlshiTblMapper.insert(psTzZnxzwlshiTbl);
			}
		} else {
			// 站内信发送历史表;
			PsTzZnxfslshiTbl psTzZnxfslshiTbl = new PsTzZnxfslshiTbl();
			psTzZnxfslshiTbl.setTzRwslId(strRwSlId);
			psTzZnxfslshiTbl.setOprid(oprid);
			psTzZnxfslshiTbl.setTzZnxZt(tj);
			psTzZnxfslshiTbl.setTzFsDt(new Date());
			psTzZnxfslshiTbl.setTzEmlSmsTaskId(strTaskId);
			psTzZnxfslshiTbl.setTzFsZt(strFsZt);
			psTzZnxfslshiTbl.setTzJcslId(prcsinstanceId);
			psTzZnxfslshiTbl.setTzAudcyId(tzAudcyId);
			psTzZnxfslshiTblMapper.insert(psTzZnxfslshiTbl);

			// 站内信发送内容历史表
			PsTzZnxzwlshiTbl psTzZnxzwlshiTbl = new PsTzZnxzwlshiTbl();
			psTzZnxzwlshiTbl.setTzRwslId(strRwSlId);
			psTzZnxzwlshiTbl.setTzYjfsRq(new Date());
			psTzZnxzwlshiTbl.setTzZnxZhwen(content);
			psTzZnxzwlshiTblMapper.insert(psTzZnxzwlshiTbl);
		}

	}

	// 写附件历史数据表
	private void writeLsMalAttchData(String strRwSlId, String strTaskId, String pcid) {
		// String sql = "select TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FJIAN_LJ from
		// PS_TZ_RW_FJIAN_TBL WHERE TZ_EML_SMS_TASK_ID=? AND TZ_FJIAN_ID NOT IN
		// (SELECT TZ_FJIAN_ID FROM PS_TZ_YJFJLSHI_TBL WHERE
		// TZ_EML_SMS_TASK_ID=?)";
		String sql = "SELECT TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FJIAN_LJ FROM PS_TZ_YJQFFJXX_TBL WHERE TZ_MLSM_QFPC_ID=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { pcid });
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				PsTzYjfjlshiTbl psTzYjfjlshiTbl = new PsTzYjfjlshiTbl();
				psTzYjfjlshiTbl.setTzRwslId(strRwSlId);
				psTzYjfjlshiTbl.setTzFjianId((String) list.get(i).get("TZ_FJIAN_ID"));
				psTzYjfjlshiTbl.setTzFjianLj((String) list.get(i).get("TZ_FJIAN_LJ"));
				psTzYjfjlshiTbl.setTzFjianMc((String) list.get(i).get("TZ_FJIAN_MC"));
				psTzYjfjlshiTblMapper.insert(psTzYjfjlshiTbl);
			}
		}
	}

	// 发送成功后删除【短信邮件任务明细表】中的听众;
	private void deleteTaskAud(String strTaskId, String audId, String audCyId) {
		PsTzDxyjrwmxTblKey psTzDxyjrwmxTblKey = new PsTzDxyjrwmxTblKey();
		psTzDxyjrwmxTblKey.setTzEmlSmsTaskId(strTaskId);
		psTzDxyjrwmxTblKey.setTzAudienceId(audId);
		psTzDxyjrwmxTblKey.setTzAudcyId(audCyId);
		psTzDxyjrwmxTblMapper.deleteByPrimaryKey(psTzDxyjrwmxTblKey);
	}

	// 解析发送短信邮件的内容:参数：任务ID，audId：听众成员ID, msgType发送的类型： SMS为短信， MAL为电子邮件;
	private String analysisEmlOrSmsContent(String pcid, String strJgId, String strYmbId, String audId, String audCyrId,
			String msgType, String malContent, String smsContent, String znxContent) {
		String content = "";
		ArrayList<String[]> arrayList = new ArrayList<>();
		if ("ZNX".equals(msgType)) {
			content = znxContent;
			arrayList = ayalysisMbSysVar.ayalyMbVar(strJgId, strYmbId, audId, audCyrId);
		} else {
			if ("MAL".equals(msgType)) {
				content = malContent;
			}

			if ("SMS".equals(msgType)) {
				content = smsContent;
			}

			PsTzDxyjQfDyTWithBLOBs psTzDxyjQfDyT = psTzDxyjQfDyTMapper.selectByPrimaryKey(pcid);
			String sendModel = psTzDxyjQfDyT.getTzSendModel();

			if ("NOR".equals(sendModel)) {
				arrayList = ayalysisMbSysVar.ayalyMbVar(strJgId, strYmbId, audId, audCyrId);

			} else {
				if ("EXC".equals(sendModel)) {
					arrayList = this.ayalyExcVar(pcid, audCyrId);
				}
			}

		}

		if (arrayList != null && arrayList.size() > 0) {
			for (int i = 0; i < arrayList.size(); i++) {
				String[] str = arrayList.get(i);
				String name = str[0];
				String value = str[1];

				content = content.replace(name, value);
			}
		}

		return content;
	}

	// excel导入解析;
	public ArrayList<String[]> ayalyExcVar(String strPicId, String audCyId) {
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		String sql = "SELECT TZ_XXX_NAME,TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strPicId });
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {

				String itemName = list.get(i).get("TZ_XXX_NAME") == null ? "" : (String) list.get(i).get("TZ_XXX_NAME") ;
				String StoreFieldName = list.get(i).get("TZ_FIELD_NAME") == null ? "" : (String) list.get(i).get("TZ_FIELD_NAME");
				
				if(itemName != null && !"".equals(itemName) && StoreFieldName != null && !"".equals(StoreFieldName) ){
					String selectSql = "SELECT " + StoreFieldName
							+ " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=? AND TZ_AUDCY_ID=?";
					String fieldValue = jdbcTemplate.queryForObject(selectSql, new Object[] { strPicId, audCyId },
							"String");
					if(fieldValue==null){
						fieldValue = "";
					}

					String name = "[" + itemName + "]";
					String[] returnString = { name, fieldValue };
					arrayList.add(returnString);
				}
				
			}
		}
		return arrayList;
	}

	private boolean checkIsSendSms(String strTaskId, String phoneNum) {
		String sql = "select count(1) from PS_TZ_DXFSLSHI_TBL where TZ_EML_SMS_TASK_ID=? and TZ_SJ_HM like ? and TZ_FS_ZT='SUC'";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { strTaskId, "%" + phoneNum + "%" }, "Integer");
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	private void writeLsSmsData(String strRwSlId, String phone, String content, String strFsZt, String strTaskId,
			String prcsinstanceId, String tzAudcyId) {
		String strRwSlId_old = jdbcTemplate.queryForObject(
				"SELECT TZ_RWSL_ID FROM PS_TZ_DXFSLSHI_TBL WHERE TZ_AUDCY_ID=? AND TZ_EML_SMS_TASK_ID=? limit 0,1",
				new Object[] { tzAudcyId, strTaskId }, "String");
		if (strRwSlId_old != null && !"".equals(strRwSlId_old)) {
			PsTzDxfslshiTbl psTzDxfslshiTbl = psTzDxfslshiTblMapper.selectByPrimaryKey(strRwSlId_old);
			psTzDxfslshiTbl.setTzSjHm(phone);
			psTzDxfslshiTbl.setTzFsDt(new Date());
			psTzDxfslshiTbl.setTzFsZt(strFsZt);
			psTzDxfslshiTbl.setTzEmlSmsTaskId(strTaskId);
			psTzDxfslshiTbl.setTzJcslId(prcsinstanceId);
			psTzDxfslshiTbl.setTzAudcyId(tzAudcyId);
			psTzDxfslshiTblMapper.insert(psTzDxfslshiTbl);

			// 短信发送内容表;
			PsTzDxzwlshiTbl psTzDxzwlshiTbl = psTzDxzwlshiTblMapper.selectByPrimaryKey(strRwSlId_old);
			if (psTzDxzwlshiTbl != null) {
				psTzDxzwlshiTbl.setTzYjfsRq(new Date());
				psTzDxzwlshiTbl.setTzDxZhwen(content);
				psTzDxzwlshiTblMapper.updateByPrimaryKeyWithBLOBs(psTzDxzwlshiTbl);
			} else {
				psTzDxzwlshiTbl = new PsTzDxzwlshiTbl();
				psTzDxzwlshiTbl.setTzRwslId(strRwSlId);
				psTzDxzwlshiTbl.setTzYjfsRq(new Date());
				psTzDxzwlshiTbl.setTzDxZhwen(content);
				psTzDxzwlshiTblMapper.insert(psTzDxzwlshiTbl);
			}

		} else {
			// 短信发送历史表;
			PsTzDxfslshiTbl psTzDxfslshiTbl = new PsTzDxfslshiTbl();
			psTzDxfslshiTbl.setTzRwslId(strRwSlId);
			psTzDxfslshiTbl.setTzSjHm(phone);
			psTzDxfslshiTbl.setTzFsDt(new Date());
			psTzDxfslshiTbl.setTzFsZt(strFsZt);
			psTzDxfslshiTbl.setTzEmlSmsTaskId(strTaskId);
			psTzDxfslshiTbl.setTzJcslId(prcsinstanceId);
			psTzDxfslshiTbl.setTzAudcyId(tzAudcyId);
			psTzDxfslshiTblMapper.insert(psTzDxfslshiTbl);
			// 短信发送内容表;
			PsTzDxzwlshiTbl psTzDxzwlshiTbl = new PsTzDxzwlshiTbl();
			psTzDxzwlshiTbl.setTzRwslId(strRwSlId);
			psTzDxzwlshiTbl.setTzYjfsRq(new Date());
			psTzDxzwlshiTbl.setTzDxZhwen(content);
			psTzDxzwlshiTblMapper.insert(psTzDxzwlshiTbl);
		}

	}

}
