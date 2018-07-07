package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import javax.servlet.http.HttpServletRequest;

@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportLuqjgImpl")
public class ImportLuqjgImpl implements UnifiedImportBase {
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
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
			String sqlSelectByKey = "SELECT 'Y' FROM PS_TZ_LQJG_DR_T WHERE TZ_MSH_ID=?";
			String sqlSelectByBj ="SELECT 'Y' FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_NAME=?";
			//更新SQL
			String updateSql = "UPDATE PS_TZ_LQJG_DR_T SET TZ_NATIONAL_ID=?,NAME=?,TZ_LQZT=?,TZ_BJ=? ,TZ_FLAG=?,TZ_BATCH_ID=? WHERE TZ_MSH_ID=?";
			
			//插入SQL
			String insertSql = "INSERT INTO PS_TZ_LQJG_DR_T(TZ_MSH_ID,TZ_NATIONAL_ID,NAME,TZ_LQZT,TZ_BJ,TZ_FLAG,TZ_BATCH_ID) VALUES(?,?,?,?,?,?,?)";
			
			//插入导入批次表
			String insertbatch ="INSERT INTO PS_TZ_DRBATCH_T(TZ_JG_ID,TZ_BATCH_ID,TZ_TPL_ID,TZ_TPL_NAME,TZ_DR_FILENAME,ROW_ADDED_DTTM,ROW_ADDED_OPRID) VALUES(?,?,?,?,?,?,?)";
			
			//查询 导入模板编号、模板名称
			String selectTpl="select TZ_TPL_ID,TZ_TPL_NAME from TZ_IMP_TPL_DFN_T where TZ_TARGET_TBL=?";
			
			String flag="";
			
			for(int i=0;i<data.size();i++){
				String strBj = (String)data.get(i).get("TZ_BJ");
				//查询班级是否在系统中存在
				String classExist = sqlQuery.queryForObject(sqlSelectByBj,new Object[]{strBj}, "String");
				if(classExist!=null)
					{
					flag="0";
					}
				else
					{
					flag="1";
						errMsg[0]="1";
						i++;
						errMsg[1]="第"+i+"行班级不存在！";
					}
			}
			//开始保存数据
			if (flag=="0"&&data != null && data.size()>0){
				int batchId = getSeqNum.getSeqNum("PS_TZ_LQJG_DR_T","TZ_BATCH_ID");
				String tzbatchId=String.valueOf(batchId);
				String orgid=tzLoginServiceImpl.getLoginedManagerOrgid(request);
				for(int i=0;i<data.size();i++){
					
					String strMshId = ((String)data.get(i).get("TZ_MSH_ID"));
					String strNationId = ((String)data.get(i).get("TZ_NATIONAL_ID"));
					String strName = ((String)data.get(i).get("NAME"));
					String strLqzt = ((String)data.get(i).get("TZ_LQZT"));
					String strBj = (String)data.get(i).get("TZ_BJ");
						if(strMshId!=null&&!"".equals(strMshId)){							
							//查询数据是否存在
							String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{strMshId}, "String");
							
							if(dataExist!=null){
								//更新模式
								sqlQuery.update(updateSql, new Object[]{strNationId,strName,strLqzt,strBj,"N",tzbatchId,strMshId});
							}else{
								//新增模式
								sqlQuery.update(insertSql, new Object[]{strMshId,strNationId,strName,strLqzt,strBj,"N",tzbatchId});
							}
						}else{
							continue;
						}	
							
					result[1] = ++result[1];
				}
				Map<String , Object> imgMap=jdbcTemplate.queryForMap(selectTpl,new Object[]{"PS_TZ_LQJG_DR_T"});
				String tplId=(String)imgMap.get("TZ_TPL_ID");
				String tplName=(String)imgMap.get("TZ_TPL_NAME");
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				sqlQuery.update(insertbatch,new Object[]{orgid,tzbatchId,tplId,tplName,filename,new Date(),oprid});
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
	            String oldNation="";
	            String oldLqzt="";
	            String oldBj="";
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
	                          String TZ_MSH_ID = (String)data.get(i).get("TZ_MSH_ID")==null?"":(String)data.get(i).get("TZ_MSH_ID");
	                          String TZ_NATIONAL_ID = (String)data.get(i).get("TZ_NATIONAL_ID")==null?"":(String)data.get(i).get("TZ_NATIONAL_ID");
	                          String NAME = (String)data.get(i).get("NAME")==null?"":(String)data.get(i).get("NAME");
	                          String TZ_LQZT = (String)data.get(i).get("TZ_LQZT")==null?"":(String)data.get(i).get("TZ_LQZT");
	                          String TZ_BJ = (String)data.get(i).get("TZ_BJ")==null?"":(String)data.get(i).get("TZ_BJ");
	                          
	                          if(TZ_MSH_ID==null||"".equals(TZ_MSH_ID)){
	                                 result[0] = false;
	                                 resultMsg2.add("第["+(i+1)+"]行面试申请号为空");   
	                          }else{
	                       	   //检查面试申请号在系统中是否存在
	                       	   String TZ_MSH_ID_EXIST2=sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_FORM_WRK_T WHERE TZ_MSH_ID=?", 
	                                      new Object[]{TZ_MSH_ID}, "String");
	                       	   if(!"Y".equals(TZ_MSH_ID_EXIST2)){
	                                  result[0] = false;
	                                  html01 += "<tr style='background:#FFF'><td valign='middle' align='center'>" + TZ_MSH_ID
	          								+ "</td><td valign='middle' align='center'>" + TZ_NATIONAL_ID
	          								+ "</td><td valign='middle' align='center'>" + NAME
	          								+ "</td><td valign='middle' align='center'>" + TZ_LQZT
	          								+ "</td><td valign='middle' align='center'>" + TZ_BJ + "</td></tr>";
	                                  continue;
	                           } else if(!"已录取".equals(TZ_LQZT)&&!"未录取".equals(TZ_LQZT)&&!"".equals(TZ_LQZT)){
	                        	   result[0] = false;
	                        	   result[2] = "JG";
	                        	   html03 += "<tr style='background:#FFF'><td valign='middle' align='center'>" + TZ_MSH_ID
	          								+ "</td><td valign='middle' align='center'>" + TZ_NATIONAL_ID
	          								+ "</td><td valign='middle' align='center'>" + NAME
	          								+ "</td><td valign='middle' align='center'>" + TZ_LQZT
	          								+ "</td><td valign='middle' align='center'>" + TZ_BJ + "</td></tr>";
	                       	   }else{
	                           	String TZ_MSH_ID_EXIST = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_LQJG_DR_T WHERE TZ_MSH_ID=?", 
	                                       new Object[]{TZ_MSH_ID}, "String");
	                           	//System.out.println(TZ_MSH_ID+"    "+TZ_MSH_ID_EXIST);
			                          if("Y".equals(TZ_MSH_ID_EXIST)){
			                        	  countCf=countCf+1;
			                                 result[0] = true;
			                                 sqlMap=jdbcTemplate.queryForMap("SELECT TZ_NATIONAL_ID,NAME,TZ_LQZT,TZ_BJ from PS_TZ_LQJG_DR_T WHERE TZ_MSH_ID=?",new Object[]{TZ_MSH_ID});
			                                 html02 += "<tr style='background:#FFF'><td valign='middle' width='40px'align='center'>" + countCf+ "</td><td valign='middle' align='center'>" + TZ_MSH_ID+ "</td>";
			                                 
			                                 oldNation= sqlMap.get("TZ_NATIONAL_ID")==null?"":String.valueOf(sqlMap.get("TZ_NATIONAL_ID"));
			                                 if(oldNation.equals(TZ_NATIONAL_ID)){
			                                	 html02 += "<td valign='middle' align='center'>" + TZ_NATIONAL_ID+ "</td>";
			                                 }else{
			                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" + TZ_NATIONAL_ID+ "</td>";
			                                 }
			                                 oldName= sqlMap.get("NAME")==null?"":String.valueOf(sqlMap.get("NAME"));
			                                 if(oldName.equals(NAME)){
			                                	 html02 += "<td valign='middle' align='center'>" + NAME+ "</td>";
			                                 }else{
			                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" + NAME+ "</td>";
			                                 }
			                                 oldLqzt= sqlMap.get("TZ_LQZT")==null?"":String.valueOf(sqlMap.get("TZ_LQZT"));
			                                 if(oldLqzt.equals(TZ_LQZT)){
			                                	 html02 += "<td valign='middle' align='center'>" + TZ_LQZT+ "</td>";
			                                 }else{
			                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" + TZ_LQZT+ "</td>";
			                                 }
			                                 oldBj= sqlMap.get("TZ_BJ")==null?"":String.valueOf(sqlMap.get("TZ_BJ"));
			                                 if(oldBj.equals(TZ_BJ)){
			                                	 html02 += "<td valign='middle' align='center'>" + TZ_BJ+ "</td>";
			                                 }else{
			                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" + TZ_BJ+ "</td>";
			                                 }
			                                 html02 += "<td valign='middle' align='center'>" + TZ_MSH_ID
			         								+ "</td><td valign='middle' align='center'>" + oldNation
			         								+ "</td><td valign='middle' align='center'>" + oldName
			         								+ "</td><td valign='middle' align='center'>" + oldLqzt
			         								+ "</td><td valign='middle' align='center'>" + oldBj + "</td></tr>";
			                                
			                                 //resultMsg.add("第["+(i+1)+"]行面试申请号数据已经存在");
			                          }
	                           }
	                           	
	                          //检查面试申请号是否已经存在
	                          
	                          }
	                   }
	                   if(!"".equals(html01)){
	                 	  result[0] = false;
	                 	  result[2] = "";
	                 	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_LQJG_VALID_HTML_01",html01);
	                   }else if(!"".equals(html03)){
	                	   result[0] = false;
	                	   returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_LQJG_VALID_HTML_01",html03);
	                   }else if(!"".equals(html02)){
	                 	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_LQJG_VALID_HTML_02",html02);
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
