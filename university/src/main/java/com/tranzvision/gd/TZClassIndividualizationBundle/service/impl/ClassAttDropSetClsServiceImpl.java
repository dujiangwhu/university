package com.tranzvision.gd.TZClassIndividualizationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassIndividualizationBundle.dao.PsTzCAttrOptTMapper;
import com.tranzvision.gd.TZClassIndividualizationBundle.model.PsTzCAttrOptT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * @author WRL TZ_GD_CLSATTR_PKG:TZ_GD_ATTRDD_CLS
 * 班级个性化属性下拉框属性设置
 *
 */
@Service("com.tranzvision.gd.TZClassIndividualizationBundle.service.impl.ClassAttDropSetClsServiceImpl")
public class ClassAttDropSetClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzCAttrOptTMapper psTzCAttrOptTMapper;
	/**
	 * 班级属性下拉框值列表显示
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
	
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String language = tzLoginServiceImpl.getSysLanaguageCD(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		String attrId = jacksonUtil.getString("attrValue");
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		try {
			
			//列表SQL
			String sql = "SELECT TZ_DROP_DOWN_ID,TZ_DROP_DOWN_VALUE,TZ_IS_USED,TZ_ORDER FROM PS_TZ_C_ATTR_OPT_T WHERE TZ_JG_ID = ? AND TZ_ATTRIBUTE_ID = ? ORDER BY TZ_ORDER";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { orgId, attrId});

			
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String attrDropDownId = result.get("TZ_DROP_DOWN_ID") == null ? "" : String.valueOf(result.get("TZ_DROP_DOWN_ID"));
				String attrDropDownDesc = result.get("TZ_DROP_DOWN_VALUE") == null ? "" : String.valueOf(result.get("TZ_DROP_DOWN_VALUE"));
				String attrDDEnabled = result.get("TZ_IS_USED") == null ? "" : String.valueOf(result.get("TZ_IS_USED"));
				String attrDDEnabledDesc = sqlQuery.queryForObject("SELECT IFNULL ((SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_ZHZJH_ID = A.TZ_ZHZJH_ID AND TZ_ZHZ_ID = A.TZ_ZHZ_ID AND TZ_LANGUAGE_ID = ?), A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE TZ_ZHZJH_ID = 'TZ_ATTR_ENABLED' AND TZ_ZHZ_ID = ?", new Object[] { language,attrDDEnabled },"String");
				String attrOrder = result.get("TZ_ORDER") == null ? "0" : String.valueOf(result.get("TZ_ORDER"));

				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("attrValue",attrId);
				mapRetJson.put("attrDropDownId",attrDropDownId);
				mapRetJson.put("attrDropDownDesc",attrDropDownDesc);
				mapRetJson.put("attrDDEnabled",attrDDEnabled);
				mapRetJson.put("attrDDEnabledDesc",attrDDEnabledDesc);
				mapRetJson.put("attrOrder",attrOrder);

				listData.add(mapRetJson);
			}
		
			mapRet.replace("total", resultlist.size());
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "数据异常，请重试！";
		}

		return jacksonUtil.Map2json(mapRet);

	}
	
	
	/**
	 * 新增或修改班级属性信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if(StringUtils.isBlank(orgId)){
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能添加班级属性！";
			return strRet;
		}
		
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int dataLength = actData.length;

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				String attrValue = jacksonUtil.getString("attrValue");
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				
				String attrDropDownId = (String)infoData.get("attrDropDownId");
				String attrDropDownDesc = (String)infoData.get("attrDropDownDesc");
				String attrDDEnabled = (String)infoData.get("attrDDEnabled");
				int attrOrder = infoData.get("attrOrder") == null ? 0 : Integer.parseInt(String.valueOf(infoData.get("attrOrder")));
		
				if(StringUtils.isBlank(attrDropDownId)){
					continue;
				}
				
				String sql = "SELECT COUNT(*) FROM PS_TZ_C_ATTR_OPT_T WHERE TZ_JG_ID = ? AND TZ_ATTRIBUTE_ID = ? AND TZ_DROP_DOWN_ID = ?";
				int count = sqlQuery.queryForObject(sql, new Object[] { orgId,attrValue,attrDropDownId }, "Integer");
				
				if (count > 0) {
					PsTzCAttrOptT PsTzCAttrOptT = new PsTzCAttrOptT();
					
					PsTzCAttrOptT.setTzJgId(orgId);
					PsTzCAttrOptT.setTzAttributeId(attrValue);
					PsTzCAttrOptT.setTzDropDownId(attrDropDownId);
					PsTzCAttrOptT.setTzDropDownValue(attrDropDownDesc);
					PsTzCAttrOptT.setTzIsUsed(attrDDEnabled);
					PsTzCAttrOptT.setTzOrder(attrOrder);
					psTzCAttrOptTMapper.updateByPrimaryKeySelective(PsTzCAttrOptT);
				}else{
					PsTzCAttrOptT psTzCAttrOptT = new PsTzCAttrOptT();
					
					psTzCAttrOptT.setTzJgId(orgId);
					psTzCAttrOptT.setTzAttributeId(attrValue);
					psTzCAttrOptT.setTzDropDownId(attrDropDownId);
					psTzCAttrOptT.setTzDropDownValue(attrDropDownDesc);
					psTzCAttrOptT.setTzIsUsed(attrDDEnabled);
					psTzCAttrOptT.setTzOrder(attrOrder);
					
					psTzCAttrOptTMapper.insert(psTzCAttrOptT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/***
	 * 删除班级属性下拉框选项信息
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		if(StringUtils.isBlank(orgId)){
			errMsg[0] = "1";
			errMsg[1] = "您不属于任何机构，不能修改班级属性定义！";
			return strRet;
		}
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 班级属性id
				String attrValue = jacksonUtil.getString("attrValue");
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				String attrDropDownId = (String) infoData.get("attrDropDownId");
				
				
				if(StringUtils.isNotBlank(attrValue) && StringUtils.isNotBlank(attrDropDownId)){
					sqlQuery.update("DELETE FROM PS_TZ_C_ATTR_OPT_T WHERE TZ_JG_ID = ? AND TZ_ATTRIBUTE_ID = ? AND TZ_DROP_DOWN_ID = ?", new Object[] {orgId,attrValue,attrDropDownId});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
