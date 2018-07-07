package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemAreaTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemAreaT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemAreaTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 区域设置；原：TZ_GD_ZDGL_PKG:TZ_GD_ZDGL_CLS
 * 
 * @author tang
 * @since 2015-11-16
 * 
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelAreaServiceImpl")
public class TemplateModelAreaServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSitemAreaTMapper psTzSitemAreaTMapper;
	
	/* 添加区域设置 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("areaId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String areaname = jacksonUtil.getString("areaname");
				String areastate = jacksonUtil.getString("areastate");
				String areatypeid = jacksonUtil.getString("areatypeid");
				String areaposition = jacksonUtil.getString("areaposition");
				String strAreasxh = jacksonUtil.getString("areasxh");
				String arealm = jacksonUtil.getString("arealm");
				String areacode = jacksonUtil.getString("areacode");
				String areaid = String.valueOf(getSeqNum.getSeqNum("TZ_SITEM_AREA_T", "TZ_AREA_ID"));
				//yuds添加移动版
				String mobileShow = jacksonUtil.getString("showmobile");
				String mobileShowOrder = jacksonUtil.getString("mobileShowOrder");
				if(mobileShow==null||"".equals(mobileShow)){
					mobileShow = "N";
				}

				PsTzSitemAreaT psTzSitemAreaT = new PsTzSitemAreaT();
				psTzSitemAreaT.setTzSitemId(siteId);
				psTzSitemAreaT.setTzAreaId(areaid);
				psTzSitemAreaT.setTzAreaName(areaname);
				psTzSitemAreaT.setTzAreaState(areastate);
				psTzSitemAreaT.setTzAreaTypeId(areatypeid);
				psTzSitemAreaT.setTzAreaPosition(areaposition);
				if(strAreasxh != null && !"".equals(strAreasxh.trim())){
					int areasxh = Integer.parseInt(strAreasxh);
					psTzSitemAreaT.setTzAreaXh(areasxh);
				}
				psTzSitemAreaT.setTzShowMobileFlg(mobileShow);
				if(mobileShowOrder != null && !"".equals(mobileShowOrder.trim())){
					int order = Integer.parseInt(mobileShowOrder);
					psTzSitemAreaT.setTzShowmOrder(order);
				}
				psTzSitemAreaT.setTzColuId(arealm);
				psTzSitemAreaT.setTzAreaCode(areacode);
				
				int i = psTzSitemAreaTMapper.insert(psTzSitemAreaT);
				if(i > 0){
					returnJsonMap.replace("areaId", areaid);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点区域设置信息保存失败";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 修改区域设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("areaId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String areaid = jacksonUtil.getString("areaid");
				String areaname = jacksonUtil.getString("areaname");
				String areastate = jacksonUtil.getString("areastate");
				String areatypeid = jacksonUtil.getString("areatypeid");
				String areaposition = jacksonUtil.getString("areaposition");
				String strAreasxh = jacksonUtil.getString("areasxh").trim();
				String arealm = jacksonUtil.getString("arealm");
				String areacode = jacksonUtil.getString("areacode");
				//yuds添加移动版
				String mobileShow = jacksonUtil.getString("showmobile");
				String mobileShowOrder = jacksonUtil.getString("mobileShowOrder");
				if(mobileShow==null||"".equals(mobileShow)){
					mobileShow = "N";
				}
				
				
				PsTzSitemAreaT psTzSitemAreaT = new PsTzSitemAreaT();
				psTzSitemAreaT.setTzSitemId(siteId);
				psTzSitemAreaT.setTzAreaId(areaid);
				psTzSitemAreaT.setTzAreaName(areaname);
				psTzSitemAreaT.setTzAreaState(areastate);
				psTzSitemAreaT.setTzAreaTypeId(areatypeid);
				psTzSitemAreaT.setTzAreaPosition(areaposition);
				if(strAreasxh != null && !"".equals(strAreasxh.trim())){
					int areasxh = Integer.parseInt(strAreasxh);
					psTzSitemAreaT.setTzAreaXh(areasxh);
				}
				psTzSitemAreaT.setTzShowMobileFlg(mobileShow);
				if(mobileShowOrder != null && !"".equals(mobileShowOrder.trim())){
					int order = Integer.parseInt(mobileShowOrder);
					psTzSitemAreaT.setTzShowmOrder(order);
				}
				psTzSitemAreaT.setTzColuId(arealm);
				psTzSitemAreaT.setTzAreaCode(areacode);
				
				int i = psTzSitemAreaTMapper.updateByPrimaryKeyWithBLOBs(psTzSitemAreaT);
				if(i > 0){
					returnJsonMap.replace("areaId", areaid);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点区域设置信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId")) {
				// 站点id, 区域ID;
				String siteId = jacksonUtil.getString("siteId");
				String areaid = jacksonUtil.getString("areaid");
				
				PsTzSitemAreaTKey psTzSitemAreaTKey = new PsTzSitemAreaTKey();
				psTzSitemAreaTKey.setTzSitemId(siteId);
				psTzSitemAreaTKey.setTzAreaId(areaid);
	
				PsTzSitemAreaT psTzSitemAreaT = psTzSitemAreaTMapper.selectByPrimaryKey(psTzSitemAreaTKey);
				if(psTzSitemAreaT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("areaid", psTzSitemAreaT.getTzAreaId());
					jsonMap.put("areaname",psTzSitemAreaT.getTzAreaName() );
					jsonMap.put("areastate", psTzSitemAreaT.getTzAreaState());
					jsonMap.put("areatypeid",psTzSitemAreaT.getTzAreaTypeId() );
					jsonMap.put("areaposition", psTzSitemAreaT.getTzAreaPosition());
					jsonMap.put("areasxh", psTzSitemAreaT.getTzAreaXh());
					jsonMap.put("arealm", psTzSitemAreaT.getTzColuId());
					jsonMap.put("areacode",psTzSitemAreaT.getTzAreaCode());
					//yuds增加手机显示内容
					jsonMap.put("showmobile",psTzSitemAreaT.getTzShowMobileFlg());
					jsonMap.put("mobileShowOrder",psTzSitemAreaT.getTzShowmOrder());
					returnJsonMap.replace("formData", jsonMap);
					returnJsonMap.replace("formData", jsonMap);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应区域信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 删除站点区域设置 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 站点ID;
				String siteId = jacksonUtil.getString("siteId");
				// 区域ID;
				String areaid = jacksonUtil.getString("areaid");
				if (siteId != null && !"".equals(siteId) && areaid != null && !"".equals(areaid)) {
					PsTzSitemAreaTKey psTzSitemAreaTKey = new PsTzSitemAreaTKey();
					psTzSitemAreaTKey.setTzSitemId(siteId);
					psTzSitemAreaTKey.setTzAreaId(areaid);
					psTzSitemAreaTMapper.deleteByPrimaryKey(psTzSitemAreaTKey);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
