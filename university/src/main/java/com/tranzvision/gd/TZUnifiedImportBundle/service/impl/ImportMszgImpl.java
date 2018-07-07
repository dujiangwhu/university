package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import javax.servlet.http.HttpServletRequest;
/**
 * 保存面试资格导入的数据
 * tmt
 * 2017-04-10
 */
@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportMszgImpl")
public class ImportMszgImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery jdbcTemplate;
	/**
	 * 保存导入的数据
	 */
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String filename,String[] errMsg) {
		String strRet = "";
		try {
			
			//查询SQL
			String sqlSelectByKey = "SELECT 'Y' FROM PS_TZ_MSZG_DR_T WHERE TZ_MSH_ID=?";
			
			//更新SQL
			String updateSql = "UPDATE PS_TZ_MSZG_DR_T SET NAME=?,TZ_MSZG=?,TZ_MSPC=?,TZ_FLAG=?,TZ_BATCH_ID=? WHERE TZ_MSH_ID=?";
			
			//插入导入批次表
			String insertbatch ="INSERT INTO PS_TZ_DRBATCH_T(TZ_JG_ID,TZ_BATCH_ID,TZ_TPL_ID,TZ_TPL_NAME,TZ_DR_FILENAME,ROW_ADDED_DTTM,ROW_ADDED_OPRID) VALUES(?,?,?,?,?,?,?)";
			
			//查询 导入模板编号、模板名称
			String selectTpl="select TZ_TPL_ID,TZ_TPL_NAME from TZ_IMP_TPL_DFN_T where TZ_TARGET_TBL=?";
			//插入SQL
			String insertSql = "INSERT INTO PS_TZ_MSZG_DR_T(TZ_MSH_ID,NAME,TZ_MSZG,TZ_MSPC,TZ_FLAG,TZ_BATCH_ID) VALUES(?,?,?,?,?,?)";

			//开始保存数据
			if (data != null && data.size()>0){
				int batchId = getSeqNum.getSeqNum("PS_TZ_MSZG_DR_T","TZ_BATCH_ID");
				String tzbatchId=String.valueOf(batchId);
				String orgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
				for(int i=0;i<data.size();i++){
					String strMshId = (String)data.get(i).get("TZ_MSH_ID")==null?"":(String)data.get(i).get("TZ_MSH_ID");
					String strName = (String)data.get(i).get("NAME")==null?"":(String)data.get(i).get("NAME");
					String strMszg = (String)data.get(i).get("TZ_MSZG")==null?"":(String)data.get(i).get("TZ_MSZG");
					String strMspc= (String)data.get(i).get("TZ_MSPC")==null?"":(String)data.get(i).get("TZ_MSPC");
					
					if(strMshId!=null&&!"".equals(strMshId)){
						
						//查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{strMshId}, "String");
						//System.out.println("dataExist:"+dataExist);
						
						if(dataExist!=null){
							//更新模式
							sqlQuery.update(updateSql, new Object[]{strName,strMszg,strMspc,"N",tzbatchId,strMshId});
							sqlQuery.update("UPDATE PS_TZ_STU_EXT_T SET TZ_MSHF_FLAG='' ,TZ_IS_ABANDON='' WHERE TZ_MSH_ID = ?",new Object[]{strMshId});
							sqlQuery.update("DELETE FROM PS_TZ_XMSJG_DR_T WHERE TZ_MSH_ID=?",new Object[]{strMshId});
							sqlQuery.update("DELETE FROM PS_TZ_MSJG_DR_T WHERE TZ_MSH_ID=?",new Object[]{strMshId});
							sqlQuery.update("DELETE FROM PS_TZ_LQJG_DR_T WHERE TZ_MSH_ID=?",new Object[]{strMshId});
						}else{
							//新增模式
							sqlQuery.update(insertSql, new Object[]{strMshId,strName,strMszg,strMspc,"N",tzbatchId});
						}
					}else{
						continue;
					}	
					result[1] = ++result[1];
					
				}
				Map<String , Object> imgMap=jdbcTemplate.queryForMap(selectTpl,new Object[]{"PS_TZ_MSZG_DR_T"});
				String tplId=(String)imgMap.get("TZ_TPL_ID");
				String tplName=(String)imgMap.get("TZ_TPL_NAME");
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				sqlQuery.update(insertbatch,new Object[]{orgId,tzbatchId,tplId,tplName,filename,new Date(),oprid});
				
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	@Override
	public void tzValidate(List<Map<String, Object>> data, List<String> fields, String targetTbl, Object[] result,
			String[] errMsg) {
		// TODO Auto-generated method stub
		try {
			
            ArrayList<String> resultMsg = new ArrayList<String>();
            Map<String, Object> sqlMap = null;
            String oldName="";
            String oldMszg="";
            String oldMspc="";
            String html01="";
            String html02="";
            String html03="";
            String returnHtml ="";
            ArrayList<String> resultMsg2 = new ArrayList<String>();//用于存储面试申请号为空的信息
            //开始校验数据
            if (data != null && data.size()>0){
            	 result[2]="";
                	int countCf=0;
                   for(int i=0;i<data.size();i++){
                	  
                          String TZ_MSH_ID = ((String)data.get(i).get("TZ_MSH_ID")==null?"":(String)data.get(i).get("TZ_MSH_ID"));
                          String NAME = ((String)data.get(i).get("NAME")==null?"":(String)data.get(i).get("NAME"));
                          String TZ_MSZG = ((String)data.get(i).get("TZ_MSZG")==null?"":(String)data.get(i).get("TZ_MSZG"));
                          String TZ_MSPC = ((String)data.get(i).get("TZ_MSPC")==null?"":(String)data.get(i).get("TZ_MSPC"));
                          if(TZ_MSH_ID==null||"".equals(TZ_MSH_ID)){
                                 result[0] = false;
                                 resultMsg2.add("第["+(i+1)+"]行面试申请号为空");   
                          }else{
                        	  
                        	//检查面试申请号在系统中是否存在
                       	   String TZ_MSH_ID_EXIST2=sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_FORM_WRK_T WHERE TZ_MSH_ID=?", 
                                      new Object[]{TZ_MSH_ID}, "String");
                      //System.out.println("TZ_MSZG"+TZ_MSZG +" TZ_MSPC "+TZ_MSPC+ TZ_MSPC.indexOf("次"));
                       	   String TZ_MSPC2=TZ_MSPC+"%";
						int isExt=sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_CLS_BATCH_T WHERE TZ_BATCH_NAME LIKE ?", 
                                new Object[]{TZ_MSPC2}, "Integer");
						//System.out.println(TZ_MSPC2+" "+isExt+",,");
                       	   if (!"Y".equals(TZ_MSH_ID_EXIST2)) {
							result[0] = false;
							html01 += "<tr style='background:#FFF'><td valign='middle' align='center'>" + TZ_MSH_ID
									+ "</td><td valign='middle' align='center'>" + NAME
									+ "</td><td valign='middle' align='center'>" + TZ_MSZG
									+ "</td><td valign='middle' align='center'>" + TZ_MSPC + "</td></tr>";
						} else if (!"是".equals(TZ_MSZG) && !"否".equals(TZ_MSZG) && !"等待".equals(TZ_MSZG)
								&& !"".equals(TZ_MSZG)) {
							result[0] = false;
							result[2] = "ZG";
							html03 += "<tr style='background:#FFF'><td valign='middle' align='center'>" + TZ_MSH_ID
									+ "</td><td valign='middle' align='center'>" + NAME
									+ "</td><td valign='middle' align='center'>" + TZ_MSZG
									+ "</td><td valign='middle' align='center'>" + TZ_MSPC + "</td></tr>";
						}else if (TZ_MSPC.indexOf("次")>0 ||isExt<1 ) {
							result[0] = false;
							result[2] = "PC";
							html03 += "<tr style='background:#FFF'><td valign='middle' align='center'>" + TZ_MSH_ID
									+ "</td><td valign='middle' align='center'>" + NAME
									+ "</td><td valign='middle' align='center'>" + TZ_MSZG
									+ "</td><td valign='middle' align='center'>" + TZ_MSPC + "</td></tr>";
						}else{
                           	String TZ_MSH_ID_EXIST = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_MSZG_DR_T WHERE TZ_MSH_ID=?", 
                                       new Object[]{TZ_MSH_ID}, "String");

		                          if("Y".equals(TZ_MSH_ID_EXIST)){
		                        	  countCf=countCf+1;
		                                 result[0] = true;
		                                 sqlMap = jdbcTemplate.queryForMap("SELECT  NAME,TZ_MSZG,TZ_MSPC FROM PS_TZ_MSZG_DR_T where TZ_MSH_ID=?",  
		             							new Object[] { TZ_MSH_ID });
		                                 oldName= sqlMap.get("NAME")==null?"":String.valueOf(sqlMap.get("NAME"));
		                                 html02 +="<tr style='background:#FFF'><td valign='middle' width='20px' align='center'>"+countCf +"</td><td valign='middle' align='center'>"+TZ_MSH_ID +"</td>";
		                                 if (oldName.equals(NAME)){
		                                	 html02 +="<td valign='middle' align='center'>"+NAME +"</td>";
		                                 }else{
		                                	 html02 +="<td valign='middle' style='background:#d52b4d'align='center'>"+NAME +"</td>";
		                                 }
		                                 oldMszg= sqlMap.get("TZ_MSZG")==null?"":String.valueOf(sqlMap.get("TZ_MSZG"));
		                                 if (oldMszg.equals(TZ_MSZG)){
		                                	 html02 +="<td valign='middle' align='center'>"+TZ_MSZG +"</td>";
		                                 }else{
		                                	 html02 +="<td valign='middle' style='background:#d52b4d' align='center'>"+TZ_MSZG +"</td>";
		                                 }
		                                 oldMspc= sqlMap.get("TZ_MSPC")==null?"":String.valueOf(sqlMap.get("TZ_MSPC"));
		                                 if (oldMspc.equals(TZ_MSPC)){
		                                	 html02 +="<td valign='middle' align='center'>"+TZ_MSPC +"</td>";
		                                 }else{
		                                	 html02 +="<td valign='middle' style='background:#d52b4d'align='center'>"+TZ_MSPC +"</td>";
		                                 }
		                                 html02 +="<td valign='middle' align='center'>"+TZ_MSH_ID +"</td><td valign='middle' align='center'>"+oldName +"</td><td valign='middle' align='center'>"+oldMszg +"</td><td valign='middle' align='center'>"+oldMspc +"</td></tr>";		                               
		                          }
                           }
                           	
                          }
                   }
                  if(!"".equals(html01)){
                	  result[0] = false;
                	  result[2]="";
                	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_SJDR_VALID_01",html01);
                  }else if(!"".equals(html03)) {
                	  result[0] = false;
                	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_SJDR_VALID_01",html03);
                  } else if(!"".equals(html02)){
                	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_SJDR_VALID_02",html02);
                  }
                  resultMsg.add(returnHtml);
            }else{
                   resultMsg.add("您没有导入任何数据！");
            }
            
            result[1] = String.join("，", (String[])resultMsg.toArray(new String[resultMsg.size()]));
            result[3] = String.join("，", (String[])resultMsg2.toArray(new String[resultMsg2.size()]));
	     } catch (Exception e) {
	            e.printStackTrace();
	            errMsg[0] = "1";
	            errMsg[1] = e.toString();
	     }
	}
	
}
