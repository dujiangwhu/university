package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.InferenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

import freemarker.core.ParseException;

import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import javax.servlet.http.HttpServletRequest;

@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportMsrcImpl")
public class ImportMsrcImpl implements UnifiedImportBase {

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
			String sqlSelectByKey = "SELECT 'Y' FROM PS_TZ_MSRC_DR_T WHERE TZ_MSH_ID=?";
			
			//更新SQL
			String updateSql = "UPDATE PS_TZ_MSRC_DR_T SET NAME=?,TZ_MSRQ=?,TZ_BDSJ=?,TZ_MS_DD=?,TZ_FLAG=?,TZ_BATCH_ID=? WHERE TZ_MSH_ID=?";
			
			//插入SQL
			String insertSql = "INSERT INTO PS_TZ_MSRC_DR_T(TZ_MSH_ID,NAME,TZ_MSRQ,TZ_BDSJ,TZ_MS_DD,TZ_FLAG,TZ_BATCH_ID) VALUES(?,?,?,?,?,?,?)";
			
			//插入导入批次表
			String insertbatch ="INSERT INTO PS_TZ_DRBATCH_T(TZ_JG_ID,TZ_BATCH_ID,TZ_TPL_ID,TZ_TPL_NAME,TZ_DR_FILENAME,ROW_ADDED_DTTM,ROW_ADDED_OPRID) VALUES(?,?,?,?,?,?,?)";
			
			//查询 导入模板编号、模板名称
			String selectTpl="select TZ_TPL_ID,TZ_TPL_NAME from TZ_IMP_TPL_DFN_T where TZ_TARGET_TBL=?";
			
			//开始保存数据
			if (data != null && data.size()>0){
				int batchId = getSeqNum.getSeqNum("PS_TZ_MSRC_DR_T","TZ_BATCH_ID");
				String tzbatchId=String.valueOf(batchId);
				String orgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
				for(int i=0;i<data.size();i++){
					
					String strMshId = (String)data.get(i).get("TZ_MSH_ID")==null?"":(String)data.get(i).get("TZ_MSH_ID");
					String strName = (String)data.get(i).get("NAME")==null?"":(String)data.get(i).get("NAME");
					String strMsrq= (String)data.get(i).get("TZ_MSRQ")==null?"":(String)data.get(i).get("TZ_MSRQ");
					String strBdsj = (String)data.get(i).get("TZ_BDSJ")==null?"":(String)data.get(i).get("TZ_BDSJ");
					String strMsDd = (String)data.get(i).get("TZ_MS_DD")==null?"":(String)data.get(i).get("TZ_MS_DD");
					
					if(strMshId!=null&&!"".equals(strMshId)){
						//查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{strMshId}, "String");
						
						if(dataExist!=null){
							//更新模式
							sqlQuery.update(updateSql, new Object[]{strName,strMsrq,strBdsj,strMsDd,"N",tzbatchId,strMshId});
							sqlQuery.update("UPDATE PS_TZ_STU_EXT_T SET TZ_MSHF_FLAG='',TZ_IS_ABANDON='' WHERE TZ_MSH_ID = ?",new Object[]{strMshId});
							sqlQuery.update("DELETE FROM PS_TZ_XMSJG_DR_T WHERE TZ_MSH_ID=?",new Object[]{strMshId});
							sqlQuery.update("DELETE FROM PS_TZ_MSJG_DR_T WHERE TZ_MSH_ID=?",new Object[]{strMshId});
							sqlQuery.update("DELETE FROM PS_TZ_LQJG_DR_T WHERE TZ_MSH_ID=?",new Object[]{strMshId});
						}else{
							//新增模式
							sqlQuery.update(insertSql, new Object[]{strMshId,strName,strMsrq,strBdsj,strMsDd,"N",tzbatchId});
						}
						
					}else{
						continue;
					}					
					
					result[1] = ++result[1];

				}
				Map<String , Object> imgMap=jdbcTemplate.queryForMap(selectTpl,new Object[]{"PS_TZ_MSRC_DR_T"});
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
            ArrayList<String> resultMsg2 = new ArrayList<String>();//用于存储面试申请号为空的信息
            Map<String, Object> sqlMap = null;
            String oldName="";
            String oldMsrq="";
            String oldbdsj="";
            String oldMsdd="";
            String html01="";
            String html02="";
            String returnHtml ="";
            String flag="0";
            result[3]="";
            //开始校验数据
            if (data != null && data.size()>0){
            	result[2]="";//用于校验日期时间格式 控制前台显示
            	int countCf=0;
                   for(int i=0;i<data.size();i++){
                	   		String validateFlag="0";
                	   		
                          String TZ_MSH_ID = (String)data.get(i).get("TZ_MSH_ID")==null?"":(String)data.get(i).get("TZ_MSH_ID");
                          String TZ_MSRQ = (String)data.get(i).get("TZ_MSRQ")==null?"":(String)data.get(i).get("TZ_MSRQ");
                          String TZ_BDSJ = (String)data.get(i).get("TZ_BDSJ")==null?"":(String)data.get(i).get("TZ_BDSJ");
                          String NAME = (String)data.get(i).get("NAME")==null?"":(String)data.get(i).get("NAME");
      					  String TZ_MS_DD = (String)data.get(i).get("TZ_MS_DD")==null?"":(String)data.get(i).get("TZ_MS_DD");
                          if(TZ_MSH_ID==null||"".equals(TZ_MSH_ID)){
                                 result[0] = false;
                                 resultMsg2.add("第["+(i+1)+"]行面试申请号为空");   
                          }else{
                       	   //检查面试申请号在系统中是否存在
                       	   String TZ_MSH_ID_EXIST2=sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_FORM_WRK_T WHERE TZ_MSH_ID=?", 
                                      new Object[]{TZ_MSH_ID}, "String");
                       	   if(!"Y".equals(TZ_MSH_ID_EXIST2)){
                                  result[0] = false;
                                  flag="1";
                                  html01+= "<tr style='background:#FFF'><td valign='middle' align='center'>"+TZ_MSH_ID +"</td><td valign='middle' align='center'>"+NAME +"</td><td valign='middle' align='center'>"+TZ_MSRQ +"</td><td valign='middle' align='center'>"+TZ_BDSJ +"</td><td valign='middle' align='center'>"+TZ_MS_DD+"</td></tr>";
                           }else{
     		                   if(!"".equals(TZ_MSRQ)&&TZ_MSRQ!=null){
  	                              	   boolean date= isValidDate(TZ_MSRQ,"yyyy-MM-dd");
  	                              	System.out.println(date);
  	                              	   if(date==false){
  	                              		   result[0] = false;
  	                              		   validateFlag="1";
  	                              	   }
                               }
                               if(!"".equals(TZ_BDSJ)&&TZ_BDSJ!=null){
                                    	   boolean date= isValidDate(TZ_BDSJ,"HH:mm");
                                    	   if(date==false){
                                    		 result[0] = false;
                                    		 validateFlag="1";
                                             //resultMsg.add("第["+(i+1)+"]行时间格式不正确");   
                                    	   }
                               } 
                               if("1".equals(validateFlag)&&!"1".equals(flag)){
                            	   result[2]="RQ";
                            	   html01+= "<tr style='background:#FFF'><td valign='middle' align='center'>"+TZ_MSH_ID +"</td><td valign='middle' align='center'>"+NAME +"</td><td valign='middle' align='center'>"+TZ_MSRQ +"</td><td valign='middle' align='center'>"+TZ_BDSJ +"</td><td valign='middle' align='center'>"+TZ_MS_DD+"</td></tr>";
                               }
                             //  System.out.println(result[2]+",,,");
                               if ("".equals(html01)){
                            	   String TZ_MSH_ID_EXIST = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_MSRC_DR_T WHERE TZ_MSH_ID=?", 
                                           new Object[]{TZ_MSH_ID}, "String");
    		                          if("Y".equals(TZ_MSH_ID_EXIST)){
    		                        	  countCf= countCf+1;
    		                        	  result[0] = true;
    		                        	  sqlMap = jdbcTemplate.queryForMap("SELECT  NAME,TZ_MSRQ,TZ_BDSJ,TZ_MS_DD FROM PS_TZ_MSRC_DR_T where TZ_MSH_ID=?",  
  		             							new Object[] { TZ_MSH_ID });
    		                        	  html02+="<tr style='background:#FFF'><td valign='middle' width='40px' align='center'>"+countCf +"</td><td valign='middle' align='center'>"+TZ_MSH_ID +"</td>";
    		                        	  oldName=sqlMap.get("NAME")==null?"":String.valueOf( sqlMap.get("NAME"));
    		                        	  if (NAME.equals(oldName)){
    		                        		  html02+="<td valign='middle' align='center'>"+NAME +"</td>";
    		                        	  }else{
    		                        		  html02+="<td valign='middle' style='background:#d52b4d' align='center'>"+NAME +"</td>";
    		                        	  }
    		                        	  oldMsrq=sqlMap.get("TZ_MSRQ")==null?"":String.valueOf( sqlMap.get("TZ_MSRQ"));
    		                        	  if (TZ_MSRQ.equals(oldMsrq)){
    		                        		  html02+="<td valign='middle' align='center'>"+TZ_MSRQ +"</td>";
    		                        	  }else{
    		                        		  html02+="<td valign='middle' style='background:#d52b4d' align='center'>"+TZ_MSRQ +"</td>";
    		                        	  }
    		                        	  oldbdsj=sqlMap.get("TZ_BDSJ")==null?"":String.valueOf( sqlMap.get("TZ_BDSJ"));
    		                        	  if (TZ_BDSJ.equals(oldbdsj)){
    		                        		  html02+="<td valign='middle' align='center'>"+TZ_BDSJ +"</td>";
    		                        	  }else{
    		                        		  html02+="<td valign='middle' style='background:#d52b4d' align='center'>"+TZ_BDSJ +"</td>";
    		                        	  }
    		                        	  oldMsdd=sqlMap.get("TZ_MS_DD")==null?"":String.valueOf( sqlMap.get("TZ_MS_DD"));
    		                        	  if (TZ_MS_DD.equals(oldMsdd)){
    		                        		  html02+="<td valign='middle' align='center'>"+TZ_MS_DD +"</td>";
    		                        	  }else{
    		                        		  html02+="<td valign='middle' style='background:#d52b4d' align='center'>"+TZ_MS_DD +"</td>";
    		                        	  }
    		                              html02+="<td valign='middle' align='center'>"+TZ_MSH_ID +"</td><td valign='middle' align='center'>"+oldName +"</td><td valign='middle' align='center'>"+oldMsrq +"</td><td valign='middle' align='center'>"+oldbdsj +"</td><td valign='middle' align='center'>"+oldMsdd+"</td></tr>";
    		                              System.out.println("html02"+html02);
    		                                
    		                          }
                               }
                               
                           }
                          }
                          
                   }
                   if(!"".equals(html01)){
                 	  result[0] = false;
                 	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_MSRC_VALID_HTML_01",html01);
                   }else  if(!"".equals(html02)){
                 	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_MSRC_VALID_HTML_02",html02);
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
	public static boolean isValidDate(String str,String format) {
	      boolean convertSuccess=true;
	       SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	       try {
	    	   dateFormat.setLenient(false);
	    	   dateFormat.parse(str);
	       } catch (java.text.ParseException e) {
	    	   convertSuccess=false;
		} 
	       return convertSuccess;
	}
	
}
