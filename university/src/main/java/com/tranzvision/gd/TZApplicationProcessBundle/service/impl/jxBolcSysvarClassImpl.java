package com.tranzvision.gd.TZApplicationProcessBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

public class jxBolcSysvarClassImpl 
{
	//系统变量不能使用注入
	

	public String getinfo(String lcName, String  mshId,long appIns,String isMobile,String siteId) {
		// 查询显示的字段;
		String tr = "";
		String tableHtml = "";
		String span="";
		// 查询表名;
	
	GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
	JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
	try{
		String tableName = jdbcTemplate.queryForObject(
				"select TZ_TARGET_TBL from TZ_IMP_TPL_DFN_T where TZ_TPL_ID=?", String.class,
				new Object[] { lcName });
		
		if (tableName == null || "".equals(tableName)) {
			return "";
		}		
			
			// 字段id;
			ArrayList<String> fieldIdList = new ArrayList<>();
			// 名称;
			ArrayList<String> fieldNameList = new ArrayList<>();
			// 查询的表字段;
			String fieldSelectSQL = "";
			
			String sql = "select TZ_FIELD,TZ_FIELD_NAME from TZ_IMP_TPL_FLD_T where TZ_TPL_ID=? and TZ_DISPLAY='Y' order by TZ_SEQ ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { lcName });
			
			for (int i = 0; i < list.size(); i++) {
				String fieldId = (String) list.get(i).get("TZ_FIELD");
				String fieldName = (String) list.get(i).get("TZ_FIELD_NAME");
				if (fieldName == null) {
					fieldName = "";
				}
				if ("".equals(fieldSelectSQL)) {
					fieldSelectSQL = fieldId;
				} else {
					fieldSelectSQL = fieldSelectSQL + " , " + fieldId;
				}
				fieldIdList.add(fieldId);
				fieldNameList.add(fieldName);
			}
			Map<String, Object> valueMap = null;
			try {
				fieldSelectSQL = " select " + fieldSelectSQL + " from " + tableName
						+ " where TZ_MSH_ID = ? ";
			
				valueMap = jdbcTemplate.queryForMap(fieldSelectSQL, new Object[] { mshId });
			} catch (Exception e1) {
				valueMap = null;
			}
			int count = 0;
			// 行数;
			int rows = 0;
			
			for (int i = 0; i < fieldNameList.size(); i++) {
					count = count + 1;
					String xxxId = fieldIdList.get(i);
					String xxxmc = fieldNameList.get(i);
					String xxxValue = "";
					if (valueMap != null) {
						xxxValue = valueMap.get(xxxId) == null ? "" : String.valueOf(valueMap.get(xxxId));
					}
					if (xxxValue == null) {
						xxxValue = "";
					}
					//手机版解析样式
					if("Y".equals(isMobile)){
						
						tr = "<span class=\"fl width_40\">" + xxxmc + "</span>";
						tr+="<span class=\"fl\">"+xxxValue+"</span>";
						span = span +  "<div class=\"overhidden\">" + tr + "</div>";
												
					}else{
						if(xxxValue.equals("保留预录取")&&xxxId.equals("TZ_MSJG")){
							xxxValue="预录取";
						}
						tr=tr+"<tr><td>"+xxxmc+"</td><td>"+xxxValue+"</td></tr>";
						
					}
					
				}
			String classId = jdbcTemplate.queryForObject(
					"SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T  WHERE TZ_MSH_ID=?", String.class,
					new Object[] { mshId });
			//System.out.println("流程："+lcName+"   报名表编号："+appIns+"   面试申请号："+mshId+"   班级："+classId);
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			String viewHtml="";
			String returnUrl="";
			String tzFlag="";
		int isExist2= jdbcTemplate.queryForObject(
				"select count(TZ_FLAG) FROM PS_TZ_MSZG_DR_T where TZ_MSH_ID=?", Integer.class,
				new Object[] { mshId });
		if("0".equals(isExist2)||isExist2==0){
			System.out.println("isExist"+"0");
		}else{
			tzFlag = jdbcTemplate.queryForObject(
					"select TZ_FLAG FROM PS_TZ_MSZG_DR_T where TZ_MSH_ID=?", String.class,
					new Object[] { mshId });
		}
		int isExist= jdbcTemplate.queryForObject(
				"select count(TZ_FLAG) FROM PS_TZ_MSRC_DR_T where TZ_MSH_ID=?", Integer.class,
				new Object[] { mshId });
		String flag ="";
		if("0".equals(isExist)||isExist==0){
			System.out.println("isExist"+"0");
		}else{
			 flag = jdbcTemplate.queryForObject(
						"select TZ_FLAG FROM PS_TZ_MSRC_DR_T where TZ_MSH_ID=?", String.class,
						new Object[] { mshId });
		}
			//手机版解析样式
		
			if("Y".equals(isMobile)){
				if("TZ_INTER_QUAL".equals(lcName)){//面试资格
					if("Y".equals(flag)){
						tableHtml=span;
					}else{
						System.out.println("面试资格flag+++"+flag+" isExist==="+isExist);
						returnUrl=ZSGL_URL+"?classid=viewmstz&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
						viewHtml="<div style='float: right;margin: -17px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;' target='_blank'>点击查看面试通知</a></div><div class='clear'></div>";
						tableHtml=viewHtml+span;
					}
				}else  if("TZ_MSRC_DR".equals(lcName)){//面试日程
					returnUrl=ZSGL_URL+"?classid=viewmstz&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
					viewHtml="<div style='float: right;margin: -17px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;' target='_blank'>点击查看面试通知</a></div><div class='clear'></div>";
					tableHtml=viewHtml+span;
				}else if("TZ_MSJG_DR".equals(lcName)){//面试结果
					returnUrl=ZSGL_URL+"?classid=viewmsjg&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
					viewHtml="<div style='float: right;margin: -17px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;'target='_blank'>点击查看面试结果</a></div><div class='clear'></div>";
					//是否录取或则预录取
					String viewLqtzsHtml = "";
					String msJg = "";
					try{
						msJg = jdbcTemplate.queryForObject("select TZ_MSJG from PS_TZ_MSJG_DR_T where TZ_MSH_ID=?", String.class,new Object[] { mshId });
					}catch(Exception e){
						msJg = "";
					}
					if(msJg != null && ("有条件预录取".equals(msJg) || "预录取".equals(msJg))){
						Map<String, Object> jgOpridMap = null;
						try{
							jgOpridMap = jdbcTemplate.queryForMap("select A.OPRID,B.TZ_JG_ID from PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID", new Object[]{appIns});
						}catch(DataAccessException dae){
							jgOpridMap = null;
						}
						
//						QrcodeHtml = "<div class=\"overhidden\" onclick='openRqQrcode(\""+appIns+"\")'><i class=\"add_icon\"></i><span class=\"fl\" style=\"color:#666;\">查看电子版条件录取通知书</span></div>";
						String jgId = "",oprid = "";
						if(jgOpridMap != null){
							oprid = jgOpridMap.get("OPRID") == null ? "" : String.valueOf(jgOpridMap.get("OPRID"));
							jgId = jgOpridMap.get("TZ_JG_ID") == null ? "" : String.valueOf(jgOpridMap.get("TZ_JG_ID"));
						
							if(oprid != null && !"".equals(oprid)){
								if(siteId != null && !"".equals(siteId)){
									String lqUrl = "/admission/" +jgId+"/" + siteId + "/" + oprid + "/" + appIns;
									viewLqtzsHtml = "<div class=\"overhidden\"><a href=\""+lqUrl+"\" target=\"_blank\"><i class=\"add_icon\"></i><span class=\"fl\" style=\"color:#666;\">查看电子版通知书</span></a></div>";	
								}
							}
						}
						
					}
					tableHtml=viewHtml+viewLqtzsHtml+span;
				}else if("TZ_XMSJG_DR".equals(lcName)){
					//x计划面试结果
					returnUrl=ZSGL_URL+"?classid=viewxmsjg&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
					viewHtml="<div style='float: right;margin: -17px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;'target='_blank'>点击查看面试结果</a></div><div class='clear'></div>";
					//是否通过
					String viewLqtzsHtml = "";
					String msJg = "";
					try{
						msJg = jdbcTemplate.queryForObject("select TZ_MSJG from PS_TZ_XMSJG_DR_T where TZ_MSH_ID=?", String.class,new Object[] { mshId });
					}catch(Exception e){
						msJg = "";
					}
					if(msJg != null && ("通过".equals(msJg))){
						Map<String, Object> jgOpridMap = null;
						try{
							jgOpridMap = jdbcTemplate.queryForMap("select A.OPRID,B.TZ_JG_ID from PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID", new Object[]{appIns});
						}catch(DataAccessException dae){
							jgOpridMap = null;
						}
						
//						QrcodeHtml = "<div class=\"overhidden\" onclick='openRqQrcode(\""+appIns+"\")'><i class=\"add_icon\"></i><span class=\"fl\" style=\"color:#666;\">查看电子版条件录取通知书</span></div>";
						String jgId = "",oprid = "";
						if(jgOpridMap != null){
							oprid = jgOpridMap.get("OPRID") == null ? "" : String.valueOf(jgOpridMap.get("OPRID"));
							jgId = jgOpridMap.get("TZ_JG_ID") == null ? "" : String.valueOf(jgOpridMap.get("TZ_JG_ID"));
						
							if(oprid != null && !"".equals(oprid)){
								if(siteId != null && !"".equals(siteId)){
									String lqUrl = "/admission/" +jgId+"/" + siteId + "/" + oprid + "/" + appIns;
									viewLqtzsHtml = "<div class=\"overhidden\"><a href=\""+lqUrl+"\" target=\"_blank\"><i class=\"add_icon\"></i><span class=\"fl\" style=\"color:#666;\">查看电子版通知书</span></a></div>";	
								}
							}
						}
						
					}
					tableHtml=viewHtml+viewLqtzsHtml+span;
				}else if("TZ_WRITTEN_RES".equals(lcName)){//笔试成绩
					returnUrl=ZSGL_URL+"?classid=viewlqjg&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
					viewHtml="<div style='float: right;margin: -17px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;'target='_blank'>点击查看初录取结果</a></div><div class='clear'></div>";
					tableHtml=viewHtml+span;
				//System.out.println("笔试成绩returnUrl"+returnUrl);
				}else{
					tableHtml=span;
					//System.out.println("没有查看链接");
				}
								
			}else{
					if("TZ_INTER_QUAL".equals(lcName)){//面试资格
						if("Y".equals(flag)){
							tableHtml=" <table class=\"new_table\" style=\"border-collapse: collapse;text-align: center;background: #fff;width: 100%;\"><tbody>"+tr+"</tbody></table>";
						}else{
							System.out.println("面试资格flag+++"+flag+" isExist==="+isExist);
							returnUrl=ZSGL_URL+"?classid=viewmstz&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
							viewHtml="<div style='float: right;margin: -31px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;' target='_blank'>点击查看面试通知</a></div>";
							tableHtml=viewHtml+" <table class=\"new_table\" style=\"border-collapse: collapse;text-align: center;background: #fff;width: 100%;\"><tbody>"+tr+"</tbody></table>";
						}
					}else if("TZ_MSRC_DR".equals(lcName)){//面试日程
						returnUrl=ZSGL_URL+"?classid=viewmstz&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
						viewHtml="<div style='float: right;margin: -31px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;' target='_blank'>点击查看面试通知</a></div>";
						tableHtml=viewHtml+" <table class=\"new_table\" style=\"border-collapse: collapse;text-align: center;background: #fff;width: 100%;\"><tbody>"+tr+"</tbody></table>";
					}else if("TZ_MSJG_DR".equals(lcName)){//面试结果
					returnUrl=ZSGL_URL+"?classid=viewmsjg&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
					viewHtml="<div style='float: right;margin: -31px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;'target='_blank'>点击查看面试结果</a></div>";
					//是否录取或则预录取
					String viewLqtzsHtml = "";
					try{
						String msJg = jdbcTemplate.queryForObject("select TZ_MSJG from PS_TZ_MSJG_DR_T where TZ_MSH_ID=?", String.class,new Object[] { mshId });
						if(msJg != null && ("有条件预录取".equals(msJg) || "预录取".equals(msJg)|| "保留预录取".equals(msJg))){
								viewLqtzsHtml = "<div class='overhidden'><a onclick='openRqQrcode("+appIns+")' href='javascript:void(0);'><img style='float:left; margin-right:5px;' src='/statics/css/website/images/table_search.png'><p>查看电子版通知书</p></a></div>";
						}
					}catch(Exception e){
						viewLqtzsHtml = "";
					}
					
					tableHtml=viewHtml + viewLqtzsHtml +" <table class=\"new_table\" style=\"border-collapse: collapse;text-align: center;background: #fff;width: 100%;\"><tbody>"+tr+"</tbody></table>";
				}else if("TZ_XMSJG_DR".equals(lcName)){//面试结果
					returnUrl=ZSGL_URL+"?classid=viewxmsjg&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
					viewHtml="<div style='float: right;margin: -31px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;'target='_blank'>点击查看面试结果</a></div>";
					String viewLqtzsHtml = "";
					try{
						String msJg = jdbcTemplate.queryForObject("select TZ_MSJG from PS_TZ_XMSJG_DR_T where TZ_MSH_ID=?", String.class,new Object[] { mshId });
						if(msJg != null &&  "通过".equals(msJg)){
								viewLqtzsHtml = "<div class='overhidden'><a onclick='openRqQrcode("+appIns+")' href='javascript:void(0);'><img style='float:left; margin-right:5px;' src='/statics/css/website/images/table_search.png'><p>查看电子版通知书</p></a></div>";
						}
					}catch(Exception e){
						viewLqtzsHtml = "";
					}
					
					tableHtml=viewHtml + viewLqtzsHtml +" <table class=\"new_table\" style=\"border-collapse: collapse;text-align: center;background: #fff;width: 100%;\"><tbody>"+tr+"</tbody></table>";
				}else if("TZ_WRITTEN_RES".equals(lcName)){//笔试成绩
					returnUrl=ZSGL_URL+"?classid=viewlqjg&TZ_APP_INS_ID="+appIns+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId;
					viewHtml="<div style='float: right;margin: -31px 0 0 0;'><a href='"+returnUrl+"'style='color: #b1193b;text-decoration: underline;'target='_blank'>点击查看初录取结果</a></div>";
					tableHtml=viewHtml+" <table class=\"new_table\" style=\"border-collapse: collapse;text-align: center;background: #fff;width: 100%;\"><tbody>"+tr+"</tbody></table>";
				
				}else{
					tableHtml=" <table class=\"new_table\" style=\"border-collapse: collapse;text-align: center;background: #fff;width: 100%;\"><tbody>"+tr+"</tbody></table>";
					//System.out.println("没有查看链接");
				}
				
			}
		
			System.out.println("tableHtml:"+tableHtml);
			return tableHtml;
			
		}
		catch(Exception e ){
			e.printStackTrace();
			return "";
		   }
	}
	public String getinfoBy(String mshId,String Sysvar) {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		Map<String, Object> lcmap = jdbcTemplate.queryForMap("select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where TZ_MSH_ID=?  ",new Object[] { mshId});
		String TZ_CLASS_ID=lcmap.get("TZ_CLASS_ID")== null ? "" : String.valueOf(lcmap.get("TZ_CLASS_ID"));
		String TZ_APPPRO_CONTENT="";
		if (!"".equals(TZ_CLASS_ID)){
			Map<String, Object> lcmaps = jdbcTemplate.queryForMap("select TZ_APPPRO_CONTENT from PS_TZ_CLS_BMLCHF_T where TZ_SYSVAR=? and TZ_CLASS_ID=? ",new Object[] { Sysvar,TZ_CLASS_ID});
			TZ_APPPRO_CONTENT=lcmap.get("TZ_APPPRO_CONTENT")== null ? "" : String.valueOf(lcmap.get("TZ_APPPRO_CONTENT"));
		}
		
		return TZ_APPPRO_CONTENT;
	}
	public String[] tzGetHtmlContent(String[] para) {
		System.out.println("===============>进入系统变量解析tzGetHtmlContent");
		//返回html
		String returnHtml="";
		//发布状态
		String tzFlag="";
		String[] returnPara={"",""};

		Long appIns = Long.parseLong(para[0]);
		String sysvar = para[1];
		//添加手机版
		String isMobile=para[2];
		String siteId = para[3];
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String mshid = jdbcTemplate.queryForObject(
					"select TZ_MSH_ID from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID=?", String.class,
					new Object[] { appIns });
			System.out.println("mshid+++"+mshid);
			if(mshid==null||"".equals(mshid)){
				returnPara[0]="N";
				returnPara[1]="";
			}else{
				switch(sysvar){
					//档案信息
					case "TZ_DAXX_DR":
						 tzFlag = jdbcTemplate.queryForObject(
								"select TZ_FLAG from PS_TZ_DAXX_DR_T where TZ_MSH_ID=?", String.class,
								new Object[] { mshid });
						//是否发布
						if("Y".equals(tzFlag)){
							returnHtml=this.getinfo("TZ_DAXX_DR",mshid,appIns,isMobile,siteId);
							returnPara[0]="Y";
							returnPara[1]=returnHtml;
						}else{
							returnHtml=this.getinfoBy(mshid,"TZ_DAXX_DR");
							returnPara[0]="N";
							returnPara[1]=returnHtml;
						}
						break;
					//面试资格
					case "TZ_INTER_QUAL":
						tzFlag = jdbcTemplate.queryForObject(
							"select TZ_FLAG FROM PS_TZ_MSZG_DR_T where TZ_MSH_ID=?", String.class,
							new Object[] { mshid });
						//是否发布
						if("Y".equals(tzFlag)){
							returnHtml=this.getinfo("TZ_INTER_QUAL",mshid,appIns,isMobile,siteId);
							returnPara[0]="Y";
							returnPara[1]=returnHtml;
						}else{
							returnHtml=this.getinfoBy(mshid,"TZ_INTER_QUAL");
							returnPara[0]="N";
							returnPara[1]=returnHtml;
						}
						break;
					//录取结果
					case "TZ_LQJG_DR":
						tzFlag = jdbcTemplate.queryForObject(
								"select TZ_FLAG from PS_TZ_LQJG_DR_T where TZ_MSH_ID=?", String.class,
								new Object[] { mshid });
						//是否发布
						if("Y".equals(tzFlag)){
							returnHtml=this.getinfo("TZ_LQJG_DR",mshid,appIns,isMobile,siteId);
							returnPara[0]="Y";
							returnPara[1]=returnHtml;
						}else{
							returnHtml=this.getinfoBy(mshid,"TZ_LQJG_DR");
							returnPara[0]="N";
							returnPara[1]=returnHtml;
						}
						break;
					//面试结果
					case "TZ_MSJG_DR":
						tzFlag = jdbcTemplate.queryForObject(
							"select TZ_FLAG from PS_TZ_MSJG_DR_T where TZ_MSH_ID=?", String.class,
							new Object[] { mshid });
						//是否发布
						if("Y".equals(tzFlag)){
							returnHtml=getinfo("TZ_MSJG_DR",mshid,appIns,isMobile,siteId);
							returnPara[0]="Y";
							returnPara[1]=returnHtml;
						}else{
							returnHtml=this.getinfoBy(mshid,"TZ_MSJG_DR");
							returnPara[0]="N";
							returnPara[1]=returnHtml;
						}
						
						
						break;
					//面试日程	
					case "TZ_MSRC_DR":
						tzFlag = jdbcTemplate.queryForObject(
							"select TZ_FLAG from PS_TZ_MSRC_DR_T where TZ_MSH_ID=?", String.class,
							new Object[] { mshid });
						//是否发布
						if("Y".equals(tzFlag)){
							returnHtml=this.getinfo("TZ_MSRC_DR",mshid,appIns,isMobile,siteId);
							returnPara[0]="Y";
							returnPara[1]=returnHtml;
						}else{
							returnHtml=this.getinfoBy(mshid,"TZ_MSRC_DR");
							returnPara[0]="N";
							returnPara[1]=returnHtml;
						}
						break;
					//申请材料提交
					case "TZ_SQCLJS_DR":
							tzFlag = jdbcTemplate.queryForObject(
									"select TZ_FLAG from PS_TZ_SQCL_JSZT_T where TZ_MSH_ID=?", String.class,
									new Object[] { mshid });
							//是否发布
							if("Y".equals(tzFlag)){
								returnHtml=this.getinfo("TZ_SQCLJS_DR",mshid,appIns,isMobile,siteId);
								returnPara[0]="Y";
								returnPara[1]=returnHtml;
							}else{
								returnHtml=this.getinfoBy(mshid,"TZ_SQCLJS_DR");
								returnPara[0]="N";
								returnPara[1]=returnHtml;
								}
						break;
					//笔试成绩	
					case "TZ_WRITTEN_RES":
						tzFlag = jdbcTemplate.queryForObject(
								"select TZ_FLAG from PS_TZ_BSCJ_DR_T where TZ_MSH_ID=?", String.class,
								new Object[] { mshid });
						//是否发布
						if("Y".equals(tzFlag)){
							returnHtml=this.getinfo("TZ_WRITTEN_RES",mshid,appIns,isMobile,siteId);
							returnPara[0]="Y";
							returnPara[1]=returnHtml;
						}else{
							returnHtml=this.getinfoBy(mshid,"TZ_WRITTEN_RES");
							returnPara[0]="N";
							returnPara[1]=returnHtml;
						}
						break;
						//新计划面试结果导入
					case "TZ_XMSJG_DR":
						tzFlag = jdbcTemplate.queryForObject(
								"select TZ_FLAG from PS_TZ_XMSJG_DR_T where TZ_MSH_ID=?", String.class,
								new Object[] { mshid });
						//是否发布
						if("Y".equals(tzFlag)){
							returnHtml=this.getinfo("TZ_XMSJG_DR",mshid,appIns,isMobile,siteId);
							returnPara[0]="Y";
							returnPara[1]=returnHtml;
						}else{
							returnHtml=this.getinfoBy(mshid,"TZ_XMSJG_DR");
							returnPara[0]="N";
							returnPara[1]=returnHtml;
						}
						break;
				}		
			}
			return returnPara;
		
	}
}
