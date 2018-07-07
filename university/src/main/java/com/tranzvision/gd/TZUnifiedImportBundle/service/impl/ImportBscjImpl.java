package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.InferenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import javax.servlet.http.HttpServletRequest;

@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportBscjImpl")
public class ImportBscjImpl implements UnifiedImportBase {
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
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result,String filename , String[] errMsg) {
		String strRet = "";
		try {
			
			//查询SQL
			String sqlSelectByKey = "SELECT 'Y' FROM PS_TZ_BSCJ_DR_T WHERE TZ_MSH_ID=?";
			
			//更新SQL
			String updateSql = "UPDATE PS_TZ_BSCJ_DR_T SET TZ_NATIONAL_ID=?, NAME=?,TZ_ZF=?,TZ_ZHF=?,TZ_YY=?,TZ_ZZ=?,TZ_ZKZH=?,TZ_C_RQZT=?,TZ_FLAG=?,TZ_BATCH_ID=? WHERE TZ_MSH_ID=?";
			
			//插入SQL
			String insertSql = "INSERT INTO PS_TZ_BSCJ_DR_T(TZ_MSH_ID,TZ_NATIONAL_ID,NAME,TZ_ZF,TZ_ZHF,TZ_YY,TZ_ZZ,TZ_ZKZH,TZ_C_RQZT,TZ_FLAG,TZ_BATCH_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			
			//插入导入批次表
			String insertbatch ="INSERT INTO PS_TZ_DRBATCH_T(TZ_JG_ID,TZ_BATCH_ID,TZ_TPL_ID,TZ_TPL_NAME,TZ_DR_FILENAME,ROW_ADDED_DTTM,ROW_ADDED_OPRID) VALUES(?,?,?,?,?,?,?)";
			
			//查询 导入模板编号、模板名称
			String selectTpl="select TZ_TPL_ID,TZ_TPL_NAME from TZ_IMP_TPL_DFN_T where TZ_TARGET_TBL=?";
			
			//开始保存数据
			if (data != null && data.size()>0){
				int batchId = getSeqNum.getSeqNum("PS_TZ_BSCJ_DR_T","TZ_BATCH_ID");
				String tzbatchId=String.valueOf(batchId);
				// 当前机构id;
				String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				for(int i=0;i<data.size();i++){
					
					String strMshId = ((String)data.get(i).get("TZ_MSH_ID"));
					String strNationid = ((String)data.get(i).get("TZ_NATIONAL_ID"));
					String strName = ((String)data.get(i).get("NAME"));
					String strZf= ((String)data.get(i).get("TZ_ZF"));
					String strZhf = (String)data.get(i).get("TZ_ZHF");
					String stryy = ((String)data.get(i).get("TZ_YY"));
					String strZZ= ((String)data.get(i).get("TZ_ZZ"));
					String strZkzh = (String)data.get(i).get("TZ_ZKZH");
					String strCRqzt = (String)data.get(i).get("TZ_C_RQZT");
					if(strMshId!=null&&!"".equals(strMshId)){
						//查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{strMshId}, "String");
						
						if(dataExist!=null){
							//更新模式
							sqlQuery.update(updateSql, new Object[]{strNationid,strName,strZf,strZhf,stryy,strZZ,strZkzh,strCRqzt,"N",tzbatchId,strMshId});
						}else{
							//新增模式
							sqlQuery.update(insertSql, new Object[]{strMshId,strNationid,strName,strZf,strZhf,stryy,strZZ,strZkzh,strCRqzt,"N",tzbatchId});
						}
						
					}else{
						continue;
					}					
					
					result[1] = ++result[1];
				}
				Map<String , Object> imgMap=jdbcTemplate.queryForMap(selectTpl,new Object[]{"PS_TZ_BSCJ_DR_T"});
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
             String oldNation="";
             String oldZf="";String oldZhf="";String oldYy="";String oldZz="";String oldZkzh="";String oldRqzt="";
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
       					String TZ_ZF= (String)data.get(i).get("TZ_ZF")==null?"":(String)data.get(i).get("TZ_ZF");
       					String TZ_ZHF = (String)data.get(i).get("TZ_ZHF")==null?"":(String)data.get(i).get("TZ_ZHF");
       					String TZ_YY = (String)data.get(i).get("TZ_YY")==null?"":(String)data.get(i).get("TZ_YY");
       					String TZ_ZZ= (String)data.get(i).get("TZ_ZZ")==null?"":(String)data.get(i).get("TZ_ZZ");
       					String TZ_ZKZH = (String)data.get(i).get("TZ_ZKZH")==null?"":(String)data.get(i).get("TZ_ZKZH");
       					String TZ_C_RQZT = (String)data.get(i).get("TZ_C_RQZT")==null?"":(String)data.get(i).get("TZ_C_RQZT");
                           if(TZ_MSH_ID==null||"".equals(TZ_MSH_ID)){
                                  result[0] = false;
                                  resultMsg2.add("第["+(i+1)+"]行面试申请号为空"); 
                           }else{
                        	   //检查面试申请号在系统中是否存在
                        	   String TZ_MSH_ID_EXIST2=sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_FORM_WRK_T WHERE TZ_MSH_ID=?", 
                                       new Object[]{TZ_MSH_ID}, "String");
                        	   if(!"Y".equals(TZ_MSH_ID_EXIST2)){
                                   result[0] = false;
                                   html01 += "<tr  style='background:#FFF'><td valign='middle' align='center'>" + TZ_MSH_ID
           								+ "</td><td valign='middle' align='center'>" + TZ_NATIONAL_ID
           								+ "</td><td valign='middle' align='center'>" + NAME
           								+ "</td><td valign='middle' align='center'>" + TZ_ZF
           								+ "</td><td valign='middle' align='center'>" + TZ_ZHF
           								+ "</td><td valign='middle' align='center'>" + TZ_YY
           								+ "</td><td valign='middle' align='center'>" + TZ_ZZ 
           								+ "</td><td valign='middle' align='center'>" + TZ_ZKZH
           								+ "</td><td valign='middle' align='center'>" + TZ_C_RQZT + "</td></tr>";
                                  // resultMsg.add("第["+(i+1)+"]行面试申请号在系统中不存在");   
						} else if (!"".equals(TZ_C_RQZT) && !"初录取".equals(TZ_C_RQZT) 
								&& !"保留面试资格".equals(TZ_C_RQZT) && !"保留预录取资格".equals(TZ_C_RQZT)&&!"未初录取".equals(TZ_C_RQZT)) {
							result[0] = false;
							result[2] = "BS";
							html03+= "<tr  style='background:#FFF'><td valign='middle' align='center'>" + TZ_MSH_ID
       								+ "</td><td valign='middle' align='center'>" + TZ_NATIONAL_ID
       								+ "</td><td valign='middle' align='center'>" + NAME
       								+ "</td><td valign='middle' align='center'>" + TZ_ZF
       								+ "</td><td valign='middle' align='center'>" + TZ_ZHF
       								+ "</td><td valign='middle' align='center'>" + TZ_YY
       								+ "</td><td valign='middle' align='center'>" + TZ_ZZ 
       								+ "</td><td valign='middle' align='center'>" + TZ_ZKZH
       								+ "</td><td valign='middle' align='center'>" + TZ_C_RQZT + "</td></tr>";
                            }else{
                            	String TZ_MSH_ID_EXIST = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_BSCJ_DR_T WHERE TZ_MSH_ID=?", 
                                        new Object[]{TZ_MSH_ID}, "String");
		                          if("Y".equals(TZ_MSH_ID_EXIST)){
		                                 result[0] = true;
		                                 countCf=countCf+1;
								sqlMap = jdbcTemplate.queryForMap(
										"SELECT TZ_NATIONAL_ID,NAME,TZ_ZF,TZ_ZHF,TZ_YY,TZ_ZZ,TZ_ZKZH,TZ_C_RQZT FROM PS_TZ_BSCJ_DR_T WHERE TZ_MSH_ID=?",
										new Object[] { TZ_MSH_ID });
								html02 += "<tr  style='background:#FFF'><td valign='middle' width='40px' align='center'>" + countCf+ "</td><td valign='middle' align='center'>" + TZ_MSH_ID+ "</td>";
		                                 oldNation=sqlMap.get("TZ_NATIONAL_ID")==null?"":String.valueOf(sqlMap.get("TZ_NATIONAL_ID"));
		                                 if (oldNation.equals(TZ_NATIONAL_ID)){
		                                	 html02 += "<td valign='middle' align='center'>" + TZ_NATIONAL_ID+ "</td>";
		                                 }else{
		                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" + TZ_NATIONAL_ID+ "</td>";
		                                 }
		                                 oldName=sqlMap.get("NAME")==null?"":String.valueOf(sqlMap.get("NAME"));
		                                 if (oldName.equals(NAME)){
		                                	 html02 += "<td valign='middle' align='center'>" + NAME+ "</td>";
		                                 }else{
		                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" + NAME+ "</td>";
		                                 }
		                                 oldZf=sqlMap.get("TZ_ZF")==null?"":String.valueOf(sqlMap.get("TZ_ZF"));
		                                 if (oldZf.equals(TZ_ZF)){
		                                	 html02 += "<td valign='middle' align='center'>" + TZ_ZF+ "</td>";
		                                 }else{
		                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" +TZ_ZF+ "</td>";
		                                 }
		                                 oldZhf=sqlMap.get("TZ_ZHF")==null?"":String.valueOf(sqlMap.get("TZ_ZHF"));
		                                 if (oldZhf.equals(TZ_ZHF)){
		                                	 html02 += "<td valign='middle' align='center'>" + TZ_ZHF+ "</td>";
		                                 }else{
		                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" +TZ_ZHF+ "</td>";
		                                 }
		                                 oldYy=sqlMap.get("TZ_YY")==null?"":String.valueOf(sqlMap.get("TZ_YY"));
		                                 if (oldYy.equals(TZ_YY)){
		                                	 html02 += "<td valign='middle' align='center'>" + TZ_YY+ "</td>";
		                                 }else{
		                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" +TZ_YY+ "</td>";
		                                 }
		                                 oldZz=sqlMap.get("TZ_ZZ")==null?"":String.valueOf(sqlMap.get("TZ_ZZ"));
		                                 if (oldZz.equals(TZ_ZZ)){
		                                	 html02 += "<td valign='middle' align='center'>" + TZ_ZZ+ "</td>";
		                                 }else{
		                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" +TZ_ZZ+ "</td>";
		                                 }
		                                 oldZkzh=sqlMap.get("TZ_ZKZH")==null?"":String.valueOf(sqlMap.get("TZ_ZKZH"));
		                                 if (oldZkzh.equals(TZ_ZKZH)){
		                                	 html02 += "<td valign='middle' align='center'>" + TZ_ZKZH+ "</td>";
		                                 }else{
		                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" +TZ_ZKZH+ "</td>";
		                                 }
		                                 oldRqzt=sqlMap.get("TZ_C_RQZT")==null?"":String.valueOf(sqlMap.get("TZ_C_RQZT"));
		                                 if (oldRqzt.equals(TZ_C_RQZT)){
		                                	 html02 += "<td valign='middle' align='center'>" + TZ_C_RQZT+ "</td>";
		                                 }else{
		                                	 html02 += "<td valign='middle' style='background:#d52b4d' align='center'>" +TZ_C_RQZT+ "</td>";
		                                 }
		                                 html02 += "<td valign='middle' align='center'>" + TZ_MSH_ID
				         						+ "</td><td valign='middle' align='center'>" + oldNation
				         						+ "</td><td valign='middle' align='center'>" + oldName
				         						+ "</td><td valign='middle' align='center'>" + oldZf
				         						+ "</td><td valign='middle' align='center'>" + oldZhf
				         						+ "</td><td valign='middle' align='center'>" + oldYy
				         						+ "</td><td valign='middle' align='center'>" + oldZz 
				         						+ "</td><td valign='middle' align='center'>" + oldZkzh
				         						+ "</td><td valign='middle' align='center'>" + oldRqzt + "</td></tr>";
		                               //  resultMsg.add("第["+(i+1)+"]行面试申请号数据已经存在");
		                          }
                            }
                            	
                           //检查面试申请号是否已经存在
                           
                           }
                    }
                    if(!"".equals(html01)){
                  	  result[0] = false;
                  	  result[2] = "";
                  	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_BSCJ_VALID_HTML_01",html01);
                    }else if(!"".equals(html03)){
                    	result[0] = false;
                    	returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_BSCJ_VALID_HTML_01",html03);
                    }else if(!"".equals(html02)){
                  	  returnHtml = tzGdObject.getHTMLText("HTML.TZUnifiedImportBundle.TZ_BSCJ_VALID_HTML_02",html02);
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
