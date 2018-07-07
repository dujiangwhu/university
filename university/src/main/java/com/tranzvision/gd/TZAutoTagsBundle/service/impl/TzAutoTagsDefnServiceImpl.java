package com.tranzvision.gd.TZAutoTagsBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutoTagsBundle.dao.PsTzZdbqDfnTblMapper;
import com.tranzvision.gd.TZAutoTagsBundle.model.PsTzZdbqDfnTbl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

@Service("com.tranzvision.gd.TZAutoTagsBundle.service.impl.TzAutoTagsDefnServiceImpl")
public class TzAutoTagsDefnServiceImpl extends FrameworkImpl{
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzZdbqDfnTblMapper psTzZdbqDfnTblMapper;

	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("total", 0);
		mapRet.put("root", listData);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			// 排序字段
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_ZDBQ_ID", "TZ_ZDBQ_NAME", "TZ_ISVALID", "TZ_JAVA_CLASS", "TZ_VIEW_CLPWD"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("tagId", rowList[0]);
					mapList.put("tagName", rowList[1]);
					mapList.put("isValid", rowList[2]);
					mapList.put("javaCls", rowList[3]);
					mapList.put("isViewClPwd", rowList[4]);
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	@Override
	public String tzQuery(String strParams, String[] errorMsg) {
		String strReturn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("formData", new HashMap<String,Object>());
		try{
			jacksonUtil.json2Map(strParams);
			
			String tagId = jacksonUtil.getString("tagId");
			
			if(tagId!= null && !"".equals(tagId)){
				PsTzZdbqDfnTbl psTzZdbqDfnTbl = psTzZdbqDfnTblMapper.selectByPrimaryKey(tagId);
				if(psTzZdbqDfnTbl != null){
					Map<String,Object> formMap = new HashMap<String,Object>();
					formMap.put("tagId", tagId);
					formMap.put("tagsName", psTzZdbqDfnTbl.getTzZdbqName());
					formMap.put("tagDescr", psTzZdbqDfnTbl.getTzDescr());
					formMap.put("isValid", psTzZdbqDfnTbl.getTzIsvalid());
					formMap.put("javaCls", psTzZdbqDfnTbl.getTzJavaClass());
					formMap.put("isViewClPwd", psTzZdbqDfnTbl.getTzViewClpwd());
					
					rtnMap.replace("formData", formMap);
				}else{
					errorMsg[0] = "1";
					errorMsg[1] = "数据不存在";
				}
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "参数不正确";
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}
		strReturn = jacksonUtil.Map2json(rtnMap);
		return strReturn;
	}
	
	
	
	@Override
	public String tzAdd(String[] actData, String[] errorMsg) {
		String strRetrun = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		try{
			if(actData.length < 1){
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误";
			}
			
			for(String strActData : actData){
				jacksonUtil.json2Map(strActData);
				
				String tagName = jacksonUtil.getString("tagsName");
				String tagDescr = jacksonUtil.getString("tagDescr");
				String isValid = jacksonUtil.getString("isValid");
				String javaCls = jacksonUtil.getString("javaCls");
				String isViewClPwd = jacksonUtil.getString("isViewClPwd");
				
				String tagId = "TAG_"+getSeqNum.getSeqNum("PS_TZ_ZDBQ_DFN_TBL", "TZ_ZDBQ_ID");
				if("TAG_0".equals(tagId)){
					errorMsg[0] = "1";
					errorMsg[1] = "系统繁忙，请稍后再试";
				}else{
					PsTzZdbqDfnTbl psTzZdbqDfnTbl = new PsTzZdbqDfnTbl();
					psTzZdbqDfnTbl.setTzZdbqId(tagId);
					psTzZdbqDfnTbl.setTzZdbqName(tagName);
					psTzZdbqDfnTbl.setTzDescr(tagDescr);
					psTzZdbqDfnTbl.setTzIsvalid(isValid);
					psTzZdbqDfnTbl.setTzJavaClass(javaCls);
					psTzZdbqDfnTbl.setTzViewClpwd(isViewClPwd);
					
					int rtn = psTzZdbqDfnTblMapper.insert(psTzZdbqDfnTbl);
					if(rtn <= 0){
						errorMsg[0] = "1";
						errorMsg[1] = "保存失败";
					}else{
						rtnMap.put("tagId", tagId);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}
		strRetrun = jacksonUtil.Map2json(rtnMap);
		return strRetrun;
	}
	
	
	
	@Override
	public String tzUpdate(String[] actData, String[] errorMsg) {
		String strRetrun = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		try{
			if(actData.length < 1){
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误";
			}

			for(String strActData : actData){
				jacksonUtil.json2Map(strActData);

				String tagId = jacksonUtil.getString("tagId");
				String tagName = jacksonUtil.getString("tagsName");
				String tagDescr = jacksonUtil.getString("tagDescr");
				String isValid = jacksonUtil.getString("isValid");
				String javaCls = jacksonUtil.getString("javaCls");
				String isViewClPwd = jacksonUtil.getString("isViewClPwd");

				PsTzZdbqDfnTbl psTzZdbqDfnTbl = psTzZdbqDfnTblMapper.selectByPrimaryKey(tagId);
				if(psTzZdbqDfnTbl != null){
					psTzZdbqDfnTbl.setTzZdbqName(tagName);
					psTzZdbqDfnTbl.setTzDescr(tagDescr);
					psTzZdbqDfnTbl.setTzIsvalid(isValid);
					psTzZdbqDfnTbl.setTzJavaClass(javaCls);
					psTzZdbqDfnTbl.setTzViewClpwd(isViewClPwd);
					
					int rtn = psTzZdbqDfnTblMapper.updateByPrimaryKey(psTzZdbqDfnTbl);
					if(rtn <= 0){
						errorMsg[0] = "1";
						errorMsg[1] = "保存失败";
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}
		strRetrun = jacksonUtil.Map2json(rtnMap);
		return strRetrun;
	}
	
	
	@Override
	public String tzDelete(String[] actData, String[] errorMsg) {
		String strRetrun = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		try{
			if(actData.length < 1){
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误";
			}

			for(String strActData : actData){
				jacksonUtil.json2Map(strActData);

				String tagId = jacksonUtil.getString("tagId");

				PsTzZdbqDfnTbl psTzZdbqDfnTbl = psTzZdbqDfnTblMapper.selectByPrimaryKey(tagId);
				if(psTzZdbqDfnTbl != null){
					psTzZdbqDfnTblMapper.deleteByPrimaryKey(tagId);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}
		strRetrun = jacksonUtil.Map2json(rtnMap);
		return strRetrun;
	}
}
