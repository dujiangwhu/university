package com.tranzvision.gd.TZStudentBbTjBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZStudentBbTjBundle.service.impl.TzGdPrjTreeServiceImpl")
public class TzGdPrjTreeServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String returnTreeJson = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		//得到当前登录人机构;
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		ArrayList<Map<String, Object>> prjTypeTreeList = new ArrayList<>();
		
		//项目分类;
		String prjTypeSQL = "select TZ_PRJ_TYPE_ID,TZ_PRJ_TYPE_NAME from PS_TZ_PRJ_TYPE_T WHERE TZ_JG_ID=? AND TZ_PRJ_TYPE_STATUS='Y'";
		List<Map<String, Object>> prjTypeList = jdbcTemplate.queryForList(prjTypeSQL,new Object[]{orgId});
		if(prjTypeList != null && prjTypeList.size() > 0){
			
			for(int i = 0; i < prjTypeList.size();i++){
				//得到分类Id和名称;
				String prjTypeId = prjTypeList.get(i).get("TZ_PRJ_TYPE_ID") == null ? "" : String.valueOf(prjTypeList.get(i).get("TZ_PRJ_TYPE_ID")); 
				String prjTypeName = prjTypeList.get(i).get("TZ_PRJ_TYPE_NAME") == null ? "" : String.valueOf(prjTypeList.get(i).get("TZ_PRJ_TYPE_NAME")); 
				//查看是否有项目使用这个分类
				int prjCount = jdbcTemplate.queryForObject("select count(1) from PS_TZ_PRJ_INF_T WHERE TZ_JG_ID=? AND TZ_PRJ_TYPE_ID=?", new Object[]{orgId,prjTypeId},"Integer");
				if(prjCount > 0){
					Map<String, Object> prjTypeHasLeafMap = new HashMap<>();
					prjTypeHasLeafMap.put("nodeId", "TYP_" + prjTypeId);
					prjTypeHasLeafMap.put("nodeDesc", prjTypeName);
					prjTypeHasLeafMap.put("text", prjTypeName);
					prjTypeHasLeafMap.put("leaf", false);
					prjTypeHasLeafMap.put("checked", false);
					ArrayList<Map<String, Object>> prjTreeList = new ArrayList<>();
					//循环所有项目;
					String prjSQL = "select TZ_PRJ_ID,TZ_PRJ_NAME from PS_TZ_PRJ_INF_T WHERE TZ_JG_ID=? AND TZ_PRJ_TYPE_ID=?";
					List<Map<String, Object>> prjList = jdbcTemplate.queryForList(prjSQL,new Object[]{orgId,prjTypeId});
					for(int j = 0; j < prjList.size(); j++){
						String prjId = prjList.get(j).get("TZ_PRJ_ID") == null ? "" : String.valueOf(prjList.get(j).get("TZ_PRJ_ID")); 
						String prjName = prjList.get(j).get("TZ_PRJ_NAME") == null ? "" : String.valueOf(prjList.get(j).get("TZ_PRJ_NAME")); 
						//查看是否有班级使用这个项目
						int classCount = jdbcTemplate.queryForObject("select count(1) from PS_TZ_CLASS_INF_T WHERE TZ_JG_ID=? AND TZ_PRJ_ID=?", new Object[]{orgId,prjId},"Integer");
						if(classCount > 0){
							Map<String, Object> prjHasLeafMap = new HashMap<>();
							prjHasLeafMap.put("nodeId","PRJ_" + prjId);
							prjHasLeafMap.put("nodeDesc", prjName);
							prjHasLeafMap.put("text", prjName);
							prjHasLeafMap.put("leaf", false);
							prjHasLeafMap.put("checked", false);
							ArrayList<Map<String, Object>> classTreeList = new ArrayList<>();
							
							//循环所有班级;
							String classSQL = "select TZ_CLASS_ID,TZ_CLASS_NAME from PS_TZ_CLASS_INF_T WHERE TZ_JG_ID=? AND TZ_PRJ_ID=?";
							List<Map<String, Object>> classList = jdbcTemplate.queryForList(classSQL,new Object[]{orgId,prjId});
							for(int k=0; k < classList.size();k++){
								String classId = classList.get(k).get("TZ_CLASS_ID") == null ? "" : String.valueOf(classList.get(k).get("TZ_CLASS_ID")); 
								String className = classList.get(k).get("TZ_CLASS_NAME") == null ? "" : String.valueOf(classList.get(k).get("TZ_CLASS_NAME")); 
								//查看是否有班级使用这个项目
								int bathCount = jdbcTemplate.queryForObject("select count(1) from PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?", new Object[]{classId},"Integer");
								if(bathCount > 0){
									Map<String, Object> classHasLeafMap = new HashMap<>();
									classHasLeafMap.put("nodeId", "CLS_" + classId);
									classHasLeafMap.put("nodeDesc", className);
									classHasLeafMap.put("text", className);
									classHasLeafMap.put("leaf", false);
									classHasLeafMap.put("checked", false);
									ArrayList<Map<String, Object>> bathTreeList = new ArrayList<>();
									
									List<Map<String, Object>> batchList = jdbcTemplate.queryForList("select TZ_BATCH_ID,TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?", new Object[]{classId});
									for(int h=0; h < batchList.size(); h++){
										String batchId = batchList.get(h).get("TZ_BATCH_ID") == null ? "" : String.valueOf(batchList.get(h).get("TZ_BATCH_ID")); 
										String batchName = batchList.get(h).get("TZ_BATCH_NAME") == null ? "" : String.valueOf(batchList.get(h).get("TZ_BATCH_NAME")); 
										Map<String, Object> batchLeafMap = new HashMap<>();
										batchLeafMap.put("nodeId", "BAT_" + batchId);
										batchLeafMap.put("nodeDesc", batchName);
										batchLeafMap.put("text", batchName);
										batchLeafMap.put("leaf", true);
										batchLeafMap.put("checked", false);
										bathTreeList.add(batchLeafMap);
									}
									
									classHasLeafMap.put("children", bathTreeList);
									classTreeList.add(classHasLeafMap);
								}else{
									Map<String, Object> classLeafMap = new HashMap<>();
									classLeafMap.put("nodeId", "CLS_" + classId);
									classLeafMap.put("nodeDesc", className);
									classLeafMap.put("text", className);
									classLeafMap.put("leaf", true);
									classLeafMap.put("checked", false);
									classTreeList.add(classLeafMap);
								}
							}
							prjHasLeafMap.put("children", classTreeList);
							prjTreeList.add(prjHasLeafMap);
						}else{
							Map<String, Object> prjLeafMap = new HashMap<>();
							prjLeafMap.put("nodeId", "PRJ_" + prjId);
							prjLeafMap.put("nodeDesc", prjName);
							prjLeafMap.put("text", prjName);
							prjLeafMap.put("leaf", true);
							prjLeafMap.put("checked", false);
							prjTreeList.add(prjLeafMap);
						}
					}
					
					prjTypeHasLeafMap.put("children", prjTreeList);
					prjTypeTreeList.add(prjTypeHasLeafMap);
				}else{
					Map<String, Object> prjTypeLeafMap = new HashMap<>();
					prjTypeLeafMap.put("nodeId", "TYP_"+ prjTypeId);
					prjTypeLeafMap.put("nodeDesc", prjTypeName);
					prjTypeLeafMap.put("text", prjTypeName);
					prjTypeLeafMap.put("leaf", true);
					prjTypeLeafMap.put("checked", false);
					prjTypeTreeList.add(prjTypeLeafMap);
				}
			}
		}
		
		//输出根节点;
		Map<String, Object> hasLeafsMap = new HashMap<>();
		hasLeafsMap.put("nodeId", "root");
		hasLeafsMap.put("nodeDesc", "统计项目选择");
		hasLeafsMap.put("text", "统计项目选择");
		hasLeafsMap.put("expanded", true);
		hasLeafsMap.put("leaf", false);
		hasLeafsMap.put("children", prjTypeTreeList);
		Map<String, Object> returnTreeJsonMap = new HashMap<>();
		returnTreeJsonMap.put("root", hasLeafsMap);
		returnTreeJson = jacksonUtil.Map2json(returnTreeJsonMap);
		
		/*
		ArrayList<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> leafMap = new HashMap<>();
		leafMap.put("nodeId", "leaf1");
		leafMap.put("nodeDesc", "第一批次");
		leafMap.put("text", "第一批次");
		leafMap.put("leaf", true);
		leafMap.put("checked", false);
		list.add(leafMap);
		
		Map<String, Object> leafMap2 = new HashMap<>();
		leafMap2.put("nodeId", "leaf2");
		leafMap2.put("nodeDesc", "第二批次");
		leafMap2.put("text", "第二批次");
		leafMap2.put("leaf", true);
		leafMap2.put("checked", false);
		list.add(leafMap2);
		
		Map<String, Object> hasLeafsMap = new HashMap<>();
		hasLeafsMap.put("nodeId", "prj1");
		hasLeafsMap.put("nodeDesc", "2017Mba全日制项目");
		hasLeafsMap.put("text", "2017Mba全日制项目");
		hasLeafsMap.put("expanded", true);
		hasLeafsMap.put("leaf", false);
		hasLeafsMap.put("children", list);
		
		Map<String, Object> returnTreeJsonMap = new HashMap<>();
		returnTreeJsonMap.put("root", hasLeafsMap);
		returnTreeJson = jacksonUtil.Map2json(returnTreeJsonMap);
		*/
		return returnTreeJson;
	}
}
