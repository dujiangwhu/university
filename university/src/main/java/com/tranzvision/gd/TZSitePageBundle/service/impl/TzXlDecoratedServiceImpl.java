/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_XL_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-21
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzXlDecoratedServiceImpl")
public class TzXlDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;
	
	@Autowired
	private TzSiteMgServiceImpl tzSiteMgServiceImpl;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private HttpServletRequest request;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			String strAreaId = jacksonUtil.getString("areaId");

			String strAreaZone = jacksonUtil.getString("areaZone");

			String strAreaType = jacksonUtil.getString("areaType");

			String strEleId = jacksonUtil.getString("eleid");

			String strColuId = "";

			String strColuName = "";

			String sql = "";

			String strShowPhoneFlg="";
			
			String strShowOrder = "";
			
			if (null == strAreaId || "".equals(strAreaId)) {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetColuidAreaidNameBySiteidAreatype2");
				Map<String, Object> mapColun = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strAreaType });
				strAreaId = mapColun.get("TZ_AREA_ID") == null ? "" : String.valueOf(mapColun.get("TZ_AREA_ID"));
				strColuId = mapColun.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapColun.get("TZ_COLU_ID"));
				strColuName = mapColun.get("TZ_AREA_NAME") == null ? "" : String.valueOf(mapColun.get("TZ_AREA_NAME"));
				strShowPhoneFlg = mapColun.get("TZ_SHOW_MOBILE_FLG") == null ? "" : String.valueOf(mapColun.get("TZ_SHOW_MOBILE_FLG"));
				strShowOrder = mapColun.get("TZ_SHOWM_ORDER") == null ? "" : String.valueOf(mapColun.get("TZ_SHOWM_ORDER"));
			} else {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetColuidAreaNameBySiteidAreaid2");
				Map<String, Object> mapColun = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strAreaId });
				strColuId = mapColun.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapColun.get("TZ_COLU_ID"));
				strColuName = mapColun.get("TZ_AREA_NAME") == null ? "" : String.valueOf(mapColun.get("TZ_AREA_NAME"));
				strShowPhoneFlg = mapColun.get("TZ_SHOW_MOBILE_FLG") == null ? "" : String.valueOf(mapColun.get("TZ_SHOW_MOBILE_FLG"));
				strShowOrder = mapColun.get("TZ_SHOWM_ORDER") == null ? "" : String.valueOf(mapColun.get("TZ_SHOWM_ORDER"));
			}
			String strCheck1 = "false",strCheck2 = "false";
			if("Y".equals(strShowPhoneFlg)){
				strCheck1 = "true";
			}else{
				strCheck2 = "true";
			}
			
			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteAreaType");
			List<Map<String, Object>> listAreaTypes = sqlQuery.queryForList(sql, new Object[] { strSiteId });

			ArrayList<ArrayList<String>> listAreaJson = new ArrayList<ArrayList<String>>();

			for (Map<String, Object> mapAreaType : listAreaTypes) {

				String areaTypeid = mapAreaType.get("TZ_AREA_TYPE") == null ? ""
						: String.valueOf(mapAreaType.get("TZ_AREA_TYPE"));
				String areaTypeName = mapAreaType.get("TZ_AREA_TYPE_NAME") == null ? ""
						: String.valueOf(mapAreaType.get("TZ_AREA_TYPE_NAME"));

				ArrayList<String> listArea = new ArrayList<String>();

				listArea.add(areaTypeid);
				listArea.add(areaTypeName);

				listAreaJson.add(listArea);

			}

			String strArea0 = jacksonUtil.List2json(listAreaJson);

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetContTypeByColuid");
			String strColuContType = sqlQuery.queryForObject(sql, new Object[] { strColuId }, "String");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteColusByColuType");
			List<Map<String, Object>> listColus = sqlQuery.queryForList(sql,
					new Object[] { strSiteId, strColuContType });

			ArrayList<ArrayList<String>> listColuJson = new ArrayList<ArrayList<String>>();

			for (Map<String, Object> mapColu : listColus) {

				String coluid = mapColu.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapColu.get("TZ_COLU_ID"));
				String coluName = mapColu.get("TZ_COLU_NAME") == null ? ""
						: String.valueOf(mapColu.get("TZ_COLU_NAME"));

				ArrayList<String> listColu = new ArrayList<String>();

				listColu.add(coluid);
				listColu.add(coluName);

				listColuJson.add(listColu);

			}

			String strColu0 = jacksonUtil.List2json(listColuJson);			
			strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzXlForm", strSiteId, strAreaId, strAreaZone,
					strAreaType, strArea0, strColuId, strColu0, strColuName, strEleId,strCheck1,strCheck2,strShowOrder);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "取数失败！" + e.getMessage();
		}

		return strRet;

	}

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strSiteId = jacksonUtil.getString("siteId");

				String strAreaId = jacksonUtil.getString("areaId");

				String strAreaName = jacksonUtil.getString("areaName");

				//String strAreaZone = jacksonUtil.getString("areaZone");

				//String strAreaType = jacksonUtil.getString("areaType");

				String strColuId = jacksonUtil.getString("coluId");

				String strAreaCode = jacksonUtil.getString("areaCode");

				//String strHeadCode = jacksonUtil.getString("headCode");

				String strBodyCode = jacksonUtil.getString("bodyCode");
				
				String strPageType = jacksonUtil.getString("pagetype");

				String strOperate = jacksonUtil.getString("oprate");
				if (null == strOperate || "".equals(strOperate)) {
					strOperate = "D";
				}
				//是否显示在手机端
				String strIsSHowPhone = jacksonUtil.getString("showMobile");
				
				String strShowOrder = jacksonUtil.getString("showOrder");
				
				if(strShowOrder==null||strShowOrder==""){
					strShowOrder = "0";
				}

				if ((null == strSiteId || "".equals(strSiteId)) || (null == strAreaId || "".equals(strAreaId))) {
					mapRet.put("success", false);
					errMsg[0] = "1";
					errMsg[1] = "请求编辑的区域异常！";
					return jacksonUtil.Map2json(mapRet);
				}

				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

				psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
				psTzSiteiAreaTWithBLOBs.setTzAreaName(strAreaName);
				psTzSiteiAreaTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzSiteiAreaTWithBLOBs.setTzLastmantOprid(oprid);
				if(null!=strColuId&&!"".equals(strColuId)){
					psTzSiteiAreaTWithBLOBs.setTzColuId(strColuId);
				}
				if(null!=strIsSHowPhone&&!"".equals(strIsSHowPhone)){
					psTzSiteiAreaTWithBLOBs.setTzShowMobileFlg(strIsSHowPhone);
				}
				if(null!=strShowOrder&&!"".equals(strShowOrder)){
					psTzSiteiAreaTWithBLOBs.setTzShowmOrder(Integer.valueOf(strShowOrder));
				}
				if (null != strAreaCode && !"".equals(strAreaCode)) {
					if ("D".equals(strOperate)) {
						psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strAreaCode);
					} else {
						psTzSiteiAreaTWithBLOBs.setTzAreaPubcode(strAreaCode);
					}					
				}
				

				psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);
				
				

				if (null != strBodyCode&&!"".equals(strBodyCode)) {
					
					boolean boolResult = false;
					switch (strPageType) {
					case "homepage":
						boolResult = tzSiteMgServiceImpl.saveHomepage(strBodyCode, strSiteId, errMsg);
						break;

					case "loginpage":
						boolResult = tzSiteMgServiceImpl.saveLoginpage(strBodyCode, strSiteId, errMsg);
						break;
					}
					
					if(boolResult){
						errMsg[0] = "0";
						errMsg[1] = "站点保存完成！";
						mapRet.put("success", true);
					}else{
						errMsg[0] = "1";
						errMsg[1] = "保存失败！";
						mapRet.put("success", false);
					}

				}else{
					errMsg[0] = "0";
					mapRet.put("success", true);
				}
				
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "请求编辑的区域异常！";
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;
	}

}
