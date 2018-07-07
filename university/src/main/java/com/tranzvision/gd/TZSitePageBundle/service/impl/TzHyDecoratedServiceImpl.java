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
 * 原PS：TZ_SITE_DECORATED_APP:TZ_HY_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-16
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzHyDecoratedServiceImpl")
public class TzHyDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;

	@Autowired
	private TzSiteMgServiceImpl tzSiteMgServiceImpl;

	@SuppressWarnings("unchecked")
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
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strAreaType });
				strAreaId = mapData.get("TZ_AREA_ID") == null ? "" : String.valueOf(mapData.get("TZ_AREA_ID"));
				strColuId = mapData.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapData.get("TZ_COLU_ID"));
				strColuName = mapData.get("TZ_AREA_NAME") == null ? "" : String.valueOf(mapData.get("TZ_AREA_NAME"));
				strShowPhoneFlg = mapData.get("TZ_SHOW_MOBILE_FLG") == null ? "" : String.valueOf(mapData.get("TZ_SHOW_MOBILE_FLG"));
				strShowOrder = mapData.get("TZ_SHOWM_ORDER") == null ? "" : String.valueOf(mapData.get("TZ_SHOWM_ORDER"));
			} else {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetColuidAreaNameBySiteidAreaid2");
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strAreaId });
				strColuId = mapData.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapData.get("TZ_COLU_ID"));
				strColuName = mapData.get("TZ_AREA_NAME") == null ? "" : String.valueOf(mapData.get("TZ_AREA_NAME"));
				strShowPhoneFlg = mapData.get("TZ_SHOW_MOBILE_FLG") == null ? "" : String.valueOf(mapData.get("TZ_SHOW_MOBILE_FLG"));
				strShowOrder = mapData.get("TZ_SHOWM_ORDER") == null ? "" : String.valueOf(mapData.get("TZ_SHOWM_ORDER"));
			}

			String strCheck1 = "false",strCheck2 = "false";
			if("Y".equals(strShowPhoneFlg)){
				strCheck1 = "true";
			}else{
				strCheck2 = "true";
			}
			
			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteAreaType");
			List<Object> listAreaType = sqlQuery.queryForList(sql, new Object[] { strSiteId });

			ArrayList<ArrayList<String>> listJsonAreaType = new ArrayList<>();

			for (Object obj : listAreaType) {
				Map<String, Object> mapAreaType = (Map<String, Object>) obj;

				ArrayList<String> listJson = new ArrayList<>();

				listJson.add(
						mapAreaType.get("TZ_AREA_TYPE") == null ? "" : String.valueOf(mapAreaType.get("TZ_AREA_TYPE")));
				listJson.add(mapAreaType.get("TZ_AREA_TYPE_NAME") == null ? ""
						: String.valueOf(mapAreaType.get("TZ_AREA_TYPE_NAME")));

				listJsonAreaType.add(listJson);
			}
			String strAreaJson = jacksonUtil.List2json(listJsonAreaType);

			sql = "select TZ_CONT_TYPE from PS_TZ_SITEI_COLU_T where TZ_COLU_ID=?";
			String strColuType = sqlQuery.queryForObject(sql, new Object[] { strColuId }, "String");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteColusByColuType");
			List<Object> listColus = sqlQuery.queryForList(sql, new Object[] { strSiteId, strColuType });

			ArrayList<ArrayList<String>> listJsonColus = new ArrayList<>();

			for (Object obj : listColus) {
				Map<String, Object> mapColus = (Map<String, Object>) obj;

				ArrayList<String> listJson = new ArrayList<>();

				listJson.add(mapColus.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapColus.get("TZ_COLU_ID")));
				listJson.add(mapColus.get("TZ_COLU_NAME") == null ? "" : String.valueOf(mapColus.get("TZ_COLU_NAME")));

				listJsonColus.add(listJson);
			}
			String strColusJson = jacksonUtil.List2json(listJsonColus);

			strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzHyForm", strSiteId, strAreaId, strAreaZone,
					strAreaType, strAreaJson, strColuId, strColusJson, strColuName, strEleId,strCheck1,strCheck2,strShowOrder);

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

				// String strAreaZone = jacksonUtil.getString("areaZone");

				// String strAreaType = jacksonUtil.getString("areaType");

				String strAreaCode = jacksonUtil.getString("areaCode");

				// String strHeadCode = jacksonUtil.getString("headCode");

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

				if (null != strBodyCode && !"".equals(strBodyCode)) {
					boolean boolResult = false;
					switch (strPageType) {
					case "homepage":
						boolResult = tzSiteMgServiceImpl.saveHomepage(strBodyCode, strSiteId, errMsg);
						break;
					case "loginpage":
						boolResult = tzSiteMgServiceImpl.saveLoginpage(strBodyCode, strSiteId, errMsg);
						break;
					}

					if (boolResult) {
						errMsg[0] = "0";
						errMsg[1] = "站点保存完成！";
					} else {
						mapRet.put("success", false);
						errMsg[0] = "1";
						errMsg[1] = "站点保存失败！";
					}

				}

			}

			if (!mapRet.containsKey("success")) {
				mapRet.put("success", true);
			}

			errMsg[0] = "0";
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
			errMsg[0] = "1";
			errMsg[1] = "请求编辑的区域异常！";
		}

		return strRet;
	}

}
