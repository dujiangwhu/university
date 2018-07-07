package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import javax.servlet.http.HttpServletRequest;

@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportSqcljsqkImpl")
public class ImportSqcljsqkImpl implements UnifiedImportBase {

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
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result,String filename, String[] errMsg) {
		String strRet = "";
		try {
			
			//查询SQL
			String sqlSelectByKey = "SELECT 'Y' FROM PS_TZ_SQCL_JSZT_T WHERE TZ_MSH_ID=?";
			
			//更新SQL
			String updateSql = "UPDATE PS_TZ_SQCL_JSZT_T SET NAME=?,TZ_CLJSQK=?,TZ_CLJS_RQ=?,TZ_FLAG=?,TZ_BATCH_ID=? WHERE TZ_MSH_ID=?";
			
			//插入SQL
			String insertSql = "INSERT INTO PS_TZ_SQCL_JSZT_T(TZ_MSH_ID,NAME,TZ_CLJSQK,TZ_CLJS_RQ,TZ_FLAG,TZ_BATCH_ID) VALUES(?,?,?,?,?,?)";
			
			//插入导入批次表
			String insertbatch ="INSERT INTO PS_TZ_DRBATCH_T(TZ_JG_ID,TZ_BATCH_ID,TZ_TPL_ID,TZ_TPL_NAME,ROW_ADDED_DTTM,ROW_ADDED_OPRID) VALUES(?,?,?,?,?,?)";
			
			//查询 导入模板编号、模板名称
			String selectTpl="select TZ_TPL_ID,TZ_TPL_NAME from TZ_IMP_TPL_DFN_T where TZ_TARGET_TBL=?";
			
			//开始保存数据
			if (data != null && data.size()>0){
				int batchId = getSeqNum.getSeqNum("PS_TZ_SQCL_JSZT_T","TZ_BATCH_ID");
				String tzbatchId=String.valueOf(batchId);
				String orgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
				for(int i=0;i<data.size();i++){
					
					String strMshId = ((String)data.get(i).get("TZ_MSH_ID"));
					String strName = ((String)data.get(i).get("NAME"));
					String strCljsqk= ((String)data.get(i).get("TZ_CLJSQK"));
					String strCljsRq = (String)data.get(i).get("TZ_CLJS_RQ");
					
					if(strMshId!=null&&!"".equals(strMshId)){
						//查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{strMshId}, "String");
						
						if(dataExist!=null){
							//更新模式
							sqlQuery.update(updateSql, new Object[]{strName,strCljsqk,strCljsRq,"N",tzbatchId,strMshId});
						}else{
							//新增模式
							sqlQuery.update(insertSql, new Object[]{strMshId,strName,strCljsqk,strCljsRq,"N",tzbatchId});
						}
						
					}else{
						continue;
					}					
					
					result[1] = ++result[1];
				}
				Map<String , Object> imgMap=jdbcTemplate.queryForMap(selectTpl,new Object[]{"PS_TZ_SQCL_JSZT_T"});
				String tplId=(String)imgMap.get("TZ_TPL_ID");
				String tplName=(String)imgMap.get("TZ_TPL_NAME");
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				sqlQuery.update(insertbatch,new Object[]{orgId,tzbatchId,tplId,tplName,new Date(),oprid});
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
            
            //开始校验数据
            if (data != null && data.size()>0){
                   for(int i=0;i<data.size();i++){
                          String TZ_MSH_ID = ((String)data.get(i).get("TZ_MSH_ID"));
                          
                          if(TZ_MSH_ID==null||"".equals(TZ_MSH_ID)){
                                 result[0] = false;
                                 resultMsg.add("第["+(i+1)+"]行面试申请号为空");
                          }else{
                       	   //检查面试申请号在系统中是否存在
                       	   String TZ_MSH_ID_EXIST2=sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_FORM_WRK_T WHERE TZ_MSH_ID=?", 
                                      new Object[]{TZ_MSH_ID}, "String");
                       	   if(!"Y".equals(TZ_MSH_ID_EXIST2)){
                                  result[0] = false;
                                  resultMsg.add("第["+(i+1)+"]行面试申请号在系统中不存在");   
                           }else{
                           	String TZ_MSH_ID_EXIST = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_SQCL_JSZT_T WHERE TZ_MSH_ID=?", 
                                       new Object[]{TZ_MSH_ID}, "String");
		                          if("Y".equals(TZ_MSH_ID_EXIST)){
		                                 result[0] = true;
		                                 resultMsg.add("第["+(i+1)+"]行面试申请号数据已经存在");
		                          }
                           }
                           	
                          //检查面试申请号是否已经存在
                          
                          }
                   }
            }else{
                   resultMsg.add("您没有导入任何数据！");
            }
            
            result[1] = String.join("，", (String[])resultMsg.toArray(new String[resultMsg.size()]));
            
	     } catch (Exception e) {
	            e.printStackTrace();
	            errMsg[0] = "1";
	            errMsg[1] = e.toString();
	     }
	}
	
}
