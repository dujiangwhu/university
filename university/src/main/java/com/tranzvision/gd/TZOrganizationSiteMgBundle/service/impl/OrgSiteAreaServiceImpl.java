package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 区域设置；原：TZ_GD_ZDDY_PKG:TZ_GD_AREAGL_CLS
 * 
 * @author tang
 * @since 2015-11-18
 * 
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteAreaServiceImpl")
public class OrgSiteAreaServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;
	
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
				String areasavecode = jacksonUtil.getString("areasavecode");
				String areaid = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_AREA_T", "TZ_AREA_ID"));
				//yuds添加移动版
				String mobileShow = jacksonUtil.getString("showmobile");
				String mobileShowOrder = jacksonUtil.getString("mobileShowOrder");
				if(mobileShow==null||"".equals(mobileShow)){
					mobileShow = "N";
				}
				
				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaT = new PsTzSiteiAreaTWithBLOBs();
				psTzSiteiAreaT.setTzSiteiId(siteId);
				psTzSiteiAreaT.setTzAreaId(areaid);
				psTzSiteiAreaT.setTzAreaName(areaname);
				psTzSiteiAreaT.setTzAreaState(areastate);
				psTzSiteiAreaT.setTzAreaTypeId(areatypeid);
				psTzSiteiAreaT.setTzAreaPosition(areaposition);
				if(strAreasxh != null && !"".equals(strAreasxh.trim())){
					int areasxh = Integer.parseInt(strAreasxh);
					psTzSiteiAreaT.setTzAreaXh(areasxh);
				}
				psTzSiteiAreaT.setTzColuId(arealm);
				psTzSiteiAreaT.setTzAreaCode(areacode);
				psTzSiteiAreaT.setTzAreaSavecode(areasavecode);
				psTzSiteiAreaT.setTzShowMobileFlg(mobileShow);
				if(mobileShowOrder != null && !"".equals(mobileShowOrder.trim())){
					int order = Integer.parseInt(mobileShowOrder);
					psTzSiteiAreaT.setTzShowmOrder(order);
				}
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				psTzSiteiAreaT.setTzAddedDttm(new Date());
				psTzSiteiAreaT.setTzAddedOprid(oprid);
				psTzSiteiAreaT.setTzLastmantDttm(new Date());
				psTzSiteiAreaT.setTzLastmantOprid(oprid);
				
				int i = psTzSiteiAreaTMapper.insert(psTzSiteiAreaT);
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
	
	
	/* 添加区域设置 */
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
				String strAreasxh = jacksonUtil.getString("areasxh");
				String arealm = jacksonUtil.getString("arealm");
				String areacode = jacksonUtil.getString("areacode");
				String areasavecode = jacksonUtil.getString("areasavecode");
				//yuds添加移动版
				String mobileShow = jacksonUtil.getString("showmobile");
				String mobileShowOrder = jacksonUtil.getString("mobileShowOrder");
				if(mobileShow==null||"".equals(mobileShow)){
					mobileShow = "N";
				}
				
				
				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaT = new PsTzSiteiAreaTWithBLOBs();
				psTzSiteiAreaT.setTzSiteiId(siteId);
				psTzSiteiAreaT.setTzAreaId(areaid);
				psTzSiteiAreaT.setTzAreaName(areaname);
				psTzSiteiAreaT.setTzAreaState(areastate);
				psTzSiteiAreaT.setTzAreaTypeId(areatypeid);
				psTzSiteiAreaT.setTzAreaPosition(areaposition);
				if(strAreasxh != null && !"".equals(strAreasxh.trim())){
					int areasxh = Integer.parseInt(strAreasxh);
					psTzSiteiAreaT.setTzAreaXh(areasxh);
				}
				psTzSiteiAreaT.setTzColuId(arealm);
				psTzSiteiAreaT.setTzAreaCode(areacode);
				psTzSiteiAreaT.setTzAreaSavecode(areasavecode);
				psTzSiteiAreaT.setTzShowMobileFlg(mobileShow);
				if(mobileShowOrder != null && !"".equals(mobileShowOrder.trim())){
					int order = Integer.parseInt(mobileShowOrder);
					psTzSiteiAreaT.setTzShowmOrder(order);
				}
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				psTzSiteiAreaT.setTzLastmantDttm(new Date());
				psTzSiteiAreaT.setTzLastmantOprid(oprid);
				
				int i = psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaT);
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
				
				PsTzSiteiAreaTKey psTzSiteiAreaTKey = new PsTzSiteiAreaTKey();
				psTzSiteiAreaTKey.setTzSiteiId(siteId);
				psTzSiteiAreaTKey.setTzAreaId(areaid);
	
				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaT = psTzSiteiAreaTMapper.selectByPrimaryKey(psTzSiteiAreaTKey);
				if(psTzSiteiAreaT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("areaid", psTzSiteiAreaT.getTzAreaId());
					jsonMap.put("areaname",psTzSiteiAreaT.getTzAreaName() );
					jsonMap.put("areastate", psTzSiteiAreaT.getTzAreaState());
					jsonMap.put("areatypeid",psTzSiteiAreaT.getTzAreaTypeId() );
					jsonMap.put("areaposition", psTzSiteiAreaT.getTzAreaPosition());
					jsonMap.put("areasxh", psTzSiteiAreaT.getTzAreaXh());
					jsonMap.put("arealm", psTzSiteiAreaT.getTzColuId());
					jsonMap.put("areacode",psTzSiteiAreaT.getTzAreaCode());
					jsonMap.put("areasavecode",psTzSiteiAreaT.getTzAreaSavecode());
					//yuds增加手机显示内容
					jsonMap.put("showmobile",psTzSiteiAreaT.getTzShowMobileFlg());
					jsonMap.put("mobileShowOrder",psTzSiteiAreaT.getTzShowmOrder());
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
					PsTzSiteiAreaTKey psTzSiteiAreaTKey = new PsTzSiteiAreaTKey();
					psTzSiteiAreaTKey.setTzSiteiId(siteId);
					psTzSiteiAreaTKey.setTzAreaId(areaid);
					psTzSiteiAreaTMapper.deleteByPrimaryKey(psTzSiteiAreaTKey);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
}
