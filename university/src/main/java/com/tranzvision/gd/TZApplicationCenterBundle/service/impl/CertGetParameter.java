package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxJSSDKSign;
import javax.servlet.http.HttpServletRequest;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import java.util.Map;

/**
 * 
 * @author YTT
 * @Time 2017-2-14
 * @paramtersm 机构id,站点id,申请人oprid,报名表编号
 * @return 晒录取通知书证书模板中的系统变量解析值
 * 
 */
public class CertGetParameter {

	// 清华MBA录取通知书-分享缩略图;
	public String getCertLogo(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			//String CertLogoSql = "SELECT CONCAT(A.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA) FROM PS_TZ_CERTIMAGE_TBL A,PS_TZ_CERTTMPL_TBL B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND B.TZ_JG_ID=? AND B.TZ_CERT_TMPL_ID=(SELECT B.TZ_CERT_TMPL_ID FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_MODAL_ID)";
			String CertLogoSql = "SELECT CONCAT(A.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA) FROM PS_TZ_CERTIMAGE_TBL A,PS_TZ_CERTTMPL_TBL B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND B.TZ_CERT_TMPL_ID=(SELECT C.TZ_CERT_TMPL_ID FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_PRJ_INF_T C WHERE A.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_PRJ_ID=C.TZ_PRJ_ID)";
			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String CertLogo = jdbcTemplate.queryForObject(CertLogoSql, String.class, new Object[] {  appIns });
			String ServerUrlSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			String ServerUrl = jdbcTemplate.queryForObject(ServerUrlSql, String.class, new Object[] { "TZ_SERVER_URL" });

			/*HttpServletRequest httpServletRequest = (HttpServletRequest) getSpringBeanUtil
					.getSpringBeanByID("httpServletRequest");
			CertLogo=  httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":"
					+ String.valueOf(httpServletRequest.getServerPort()) + httpServletRequest.getContextPath()+CertLogo;*/
			CertLogo= ServerUrl+CertLogo;
			
			return CertLogo;
		
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 清华MBA录取通知书-个人照片url;
	public String getCertImg(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String CertImgSql = "SELECT CONCAT(B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA) FROM PS_TZ_OPR_PHT_GL_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND A.OPRID=?";

			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String CertImg = jdbcTemplate.queryForObject(CertImgSql, String.class, new Object[] { oprid });
			return CertImg;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 清华MBA录取通知书-姓名;
	public String getCertNAME(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String CertNameSql = "SELECT B.TZ_REALNAME from PS_TZ_APP_INS_T A,PS_TZ_REG_USER_T B WHERE A.TZ_APP_INS_ID = ? AND B.OPRID=A.ROW_ADDED_OPRID";

			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String CertName = jdbcTemplate.queryForObject(CertNameSql, String.class, new Object[] { appIns });
			if(CertName == null){
				CertName = "";
			}

			return CertName;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	// 清华MBA录取通知书-姓名;
		public String getCertNAMEBR(String[] paramters) {
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

				String CertNameSql = "SELECT B.TZ_REALNAME from PS_TZ_APP_INS_T A,PS_TZ_REG_USER_T B WHERE A.TZ_APP_INS_ID = ? AND B.OPRID=A.ROW_ADDED_OPRID";

				String jgId = paramters[0];
				String siteId = paramters[1];
				String oprid = paramters[2];
				String appIns = paramters[3];

				String CertName = jdbcTemplate.queryForObject(CertNameSql, String.class, new Object[] { appIns });
				if(CertName == null){
					CertName = "";
				}
				
				int nameLen = CertName.length();
		        if(nameLen >= 2){
					String[] CertNameArr = CertName.split("");
					if(CertNameArr.length == 2){
						CertName = CertNameArr[0]+"<br><br>"+CertNameArr[1];
					}else{
						for(int i = 0; i< CertNameArr.length; i++){
							if(i == 0){
								CertName = CertNameArr[i];
							}else{
								CertName = CertName +"<br>"+ CertNameArr[i];
							}
						}
					}
				}
				return CertName;

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	
	// 清华MBA录取通知书-入学年份;
	public String getCertYEAR(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String CertYearSql = "SELECT YEAR(A.TZ_RX_DT) from PS_TZ_CLASS_INF_T A,PS_TZ_FORM_WRK_T B WHERE B.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID";

			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String CertYear = jdbcTemplate.queryForObject(CertYearSql, String.class, new Object[] { appIns });
			return CertYear;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 项目名称;
	public String getPrgName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			//String PrgNameSql = "SELECT B.TZ_PRJ_NAME FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_MODAL_ID";
			String PrgNameSql = "SELECT C.TZ_PRJ_NAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_PRJ_INF_T C WHERE A.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_PRJ_ID=C.TZ_PRJ_ID";
			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String PrgName = jdbcTemplate.queryForObject(PrgNameSql, String.class, new Object[] { appIns });
			return PrgName;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 企业Id / 公众号appId;
	public String getWxCorpid(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String WxCorpidSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			String WxCorpid = jdbcTemplate.queryForObject(WxCorpidSql, String.class, new Object[] { "TZ_WX_CORPID" });
			return WxCorpid;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	//微信 ServerUrl;
	public String getServerUrl(String[] paramters) {
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

				String ServerUrlSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				String ServerUrl = jdbcTemplate.queryForObject(ServerUrlSql, String.class, new Object[] { "TZ_SERVER_URL" });

				return ServerUrl;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	
	//微信 ServerUrl;
	public String getXServerUrl(String[] paramters) {
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

				String ServerUrlSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				String ServerUrl = jdbcTemplate.queryForObject(ServerUrlSql, String.class, new Object[] { "TZ_X_SERVER_URL" });

				return ServerUrl;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	
	
	//得到各个项目的证书图片;
	public String getZsImage(String[] paramters){
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String PrgIdSql = "SELECT C.TZ_PRJ_ID FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_PRJ_INF_T C WHERE A.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_PRJ_ID=C.TZ_PRJ_ID";
			
			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];
			String prjId = jdbcTemplate.queryForObject(PrgIdSql, String.class,new Object[]{appIns});
			if(prjId == null || "".equals(prjId)){
				return "";
			}
			
			// 【0】查询录取状态
			String mshid = jdbcTemplate.queryForObject("select TZ_MSH_ID from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID=?",String.class,new Object[] { appIns });
			String tzLuquSta = jdbcTemplate.queryForObject("select TZ_MSJG from PS_TZ_MSJG_DR_T where TZ_MSH_ID=?",String.class, new Object[] { mshid });
			String hardcodeId = "";
			if ("有条件预录取".equals(tzLuquSta) ) {
				hardcodeId = prjId + "_" + "YTJ";
			}
			if ("预录取".equals(tzLuquSta)||"保留预录取".equals(tzLuquSta)) {
				hardcodeId = prjId + "_" + "WTJ";
			}
			if("".equals(hardcodeId)){
				return "";
			}else{
				String imageUrlSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				String imageUrl = jdbcTemplate.queryForObject(imageUrlSql, String.class, new Object[] {hardcodeId});

				return imageUrl;
			}
		}catch(Exception e){
			return "";
		}
		
	}
	
	//得到各个项目的证书图片;
	public String getXZsImage(String[] paramters){
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String PrgIdSql = "SELECT C.TZ_PRJ_ID FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_PRJ_INF_T C WHERE A.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_PRJ_ID=C.TZ_PRJ_ID";
				
			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];
			String prjId = jdbcTemplate.queryForObject(PrgIdSql, String.class,new Object[]{appIns});
			if(prjId == null || "".equals(prjId)){
				return "";
			}
				
			// 【0】查询录取状态
			String mshid = jdbcTemplate.queryForObject("select TZ_MSH_ID from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID=?",String.class,new Object[] { appIns });
			String tzLuquSta = jdbcTemplate.queryForObject("select TZ_MSJG from PS_TZ_XMSJG_DR_T where TZ_MSH_ID=?",String.class, new Object[] { mshid });
			String hardcodeId = "";
			if ("通过".equals(tzLuquSta) ) {
				//hardcodeId = prjId + "_" + "YTJ";
				// 【0】检查考生报名表目前在读阶段【本科、硕士研究生、博士研究生】
				String zdjdFieldName = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_X_MBA_ZD_FLD'",String.class);
				//本科;
				String bk = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_X_MBA_BK'",String.class);
				//硕士
				String ss = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_X_MBA_SS'",String.class);
				//博士
				String bs = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_X_MBA_BS'",String.class);
				//报名表【目前在读阶段】值
				String zdjdValue = jdbcTemplate.queryForObject("SELECT TZ_XXXKXZ_MC FROM PS_TZ_APP_DHCC_T where TZ_APP_INS_ID= ? and TZ_XXX_BH = ? and TZ_IS_CHECKED='Y' AND (TZ_XXXKXZ_MC=? OR TZ_XXXKXZ_MC=? OR TZ_XXXKXZ_MC=?) ",new Object[]{appIns,zdjdFieldName,bk,ss,bs},String.class);
				
				//本科
				if(bk.equals(zdjdValue)){
					hardcodeId = prjId + "_" + "BK";
				}
				
				//硕士或博士
				if(ss.equals(zdjdValue) || bs.equals(zdjdValue)){
					hardcodeId = prjId + "_" + "SB";
				}
				
			}
			if("".equals(hardcodeId)){
				return "";
			}else{
				String imageUrlSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				String imageUrl = jdbcTemplate.queryForObject(imageUrlSql, String.class, new Object[] {hardcodeId});

				return imageUrl;
			}
		}catch(Exception e){
			return "";
		}
			
	}
	
	//取面试结果
	public String getMsJg(String[] paramters){
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String PrgIdSql = "SELECT C.TZ_PRJ_ID FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_PRJ_INF_T C WHERE A.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_PRJ_ID=C.TZ_PRJ_ID";
			
			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];
			String prjId = jdbcTemplate.queryForObject(PrgIdSql, String.class,new Object[]{appIns});
			if(prjId == null || "".equals(prjId)){
				return "";
			}
			
			// 【0】查询录取状态
			String mshid = jdbcTemplate.queryForObject("select TZ_MSH_ID from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID=?",String.class,new Object[] { appIns });
			String tzLuquSta = jdbcTemplate.queryForObject("select TZ_MSJG from PS_TZ_MSJG_DR_T where TZ_MSH_ID=?",String.class, new Object[] { mshid });
			if("保留预录取".equals(tzLuquSta)){
				return "预录取";
			}else{
				return tzLuquSta;
			}
			
		}catch(Exception e){
			return "";
		}
		
	}
	
	// 报名表实例编号;
	public String getAppInsId(String[] paramters) {
		try {
			String appIns = paramters[3];
			return appIns;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
