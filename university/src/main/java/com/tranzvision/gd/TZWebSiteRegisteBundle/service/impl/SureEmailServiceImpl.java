package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteRegisteBundle.dao.PsTzDzyxYzmTblMapper;
import com.tranzvision.gd.TZWebSiteRegisteBundle.model.PsTzDzyxYzmTbl;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang
 * 确认邮箱
 * 原： Record.WEBLIB_GD_USER, Field.TZ_GD_USER, "FieldFormula", "Iscript_SureEmail"
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.SureEmailServiceImpl")
public class SureEmailServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzDzyxYzmTblMapper psTzDzyxYzmTblMapper;
	@Autowired
	private GetHardCodePoint GetHardCodePoint;
	
	@Override
	//确认修改邮箱
	public String tzGetHtmlContent(String strParams) {
		
		//确认修改后跳转页面;跳转到com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.RegEmailSuccessServiceImpl.tzGetHtmlContent;
		String serv = "http://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/dispatcher";
		String RegEmailSuccess = serv+"?classid=regemailsuccess";
		//String RegEmailSuccess = GenerateScriptContentURL(%Portal, %Node, Record.WEBLIB_GD_USER, Field.TZ_GD_USER, "FieldFormula", "Iscript_RegEmailSuccess");
		
		//验证是否通过;
		Date cntlogAddtiem;
		Date curDate = new Date();
		String dlzhId = "", tzJgId = "", tzEmail = "";
		//JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			//jacksonUtil.json2Map(strParams);
			//String tokenCode = jacksonUtil.getString("TZ_TOKEN_CODE");
			String tokenCode = request.getParameter("TZ_TOKEN_CODE");
			String siteid = request.getParameter("siteid"); 
			RegEmailSuccess = RegEmailSuccess + "&siteid=" + siteid; 
			String strTokenFLg = "";
			PsTzDzyxYzmTbl psTzDzyxYzmTbl = psTzDzyxYzmTblMapper.selectByPrimaryKey(tokenCode);
			if(psTzDzyxYzmTbl != null){
				strTokenFLg = psTzDzyxYzmTbl.getTzEffFlag();
				tzEmail = psTzDzyxYzmTbl.getTzEmail();
				dlzhId = psTzDzyxYzmTbl.getTzDlzhId();
				tzJgId = psTzDzyxYzmTbl.getTzJgId();
			
				//根据账号得到注册人员的注册的siteid;
				
				if("Y".equals(strTokenFLg)){
					cntlogAddtiem = psTzDzyxYzmTbl.getTzYzmYxq();
					if(cntlogAddtiem.before(curDate)){
						// 验证码超时;
			            RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId + "&FLAGE=N&errorFlg=overtime";
					}else{
						// 查看是否绑定了邮箱，如果绑定了邮箱，则要同时修改绑定邮箱，同时要判断新的绑定邮箱是否在该机构下重复，如果重复，则修改失败，同时要提示用户;
				        String bindSQL = "select 'Y' from PS_TZ_AQ_YHXX_TBL A,PS_TZ_REG_USER_T B WHERE A.OPRID=B.OPRID AND A.TZ_JG_ID=? and A.TZ_EMAIL=? and A.TZ_YXBD_BZ='Y' and B.TZ_SITEI_ID=?";
						String strBindEmailFlg = jdbcTemplate.queryForObject(bindSQL, new Object[]{tzJgId,tzEmail,siteid},"String");
						if("Y".equals(strBindEmailFlg)){
							RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId + "&FLAGE=N&errorFlg=repeat";
						}else{
							String updateSQL = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_EMAIL=?, TZ_YXBD_BZ='Y' WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
							jdbcTemplate.update(updateSQL,new Object[]{tzEmail,dlzhId,tzJgId});
							//20180103,yuds,为特殊站点添加特殊处理方式
							String isSpecialSite = GetHardCodePoint.getHardCodePointVal("TZ_ISPECIAL_SITE");
							if("Y".equals(isSpecialSite)){
								updateSQL = "UPDATE PS_TZ_AQ_YHXX_TMP SET TZ_EMAIL=?, TZ_YXBD_BZ='Y' WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
								jdbcTemplate.update(updateSQL, new Object[] { tzEmail, dlzhId, tzJgId, "ZCYH" });
							}
							
							updateSQL = "UPDATE PS_TZ_DZYX_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_TOKEN_CODE=? and TZ_EFF_FLAG='Y'";
							jdbcTemplate.update(updateSQL,new Object[]{tokenCode});
							
				            RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId + "&FLAGE=Y&siteId=" + siteid;
						}
					}
				}else{
					RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId +  "&FLAGE=N&errorFlg=codeerror";
				}
			}else{
				RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId +  "&FLAGE=N&errorFlg=codeerror";
			}
		}catch(Exception e ){
			RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId +  "&FLAGE=N&errorFlg=codeerror";
		}
		return "<script>window.location.href='" + RegEmailSuccess + "';</script>";
	}
}
