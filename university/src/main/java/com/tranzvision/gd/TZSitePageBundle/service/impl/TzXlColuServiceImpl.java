/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_XL_COLU_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzXlColuServiceImpl")
public class TzXlColuServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

	
		String strRet = "";
		//当前登录的用户;
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteLang");

			String strLang = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

			String strNum = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "23", strLang,
					"第", "No.");

			String strTotal = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "24",
					strLang, "共", "Total");

			String strPage = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "25", strLang,
					"页", "Page");

			String strFrom = jacksonUtil.getString("qureyFrom");
		
			String strColuId = "";
			if ("M".equals(strFrom)) {

				String strMenuId = jacksonUtil.getString("menuId");
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetColuidBySiteidMenuidMenuStateY");
				strColuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");

			} else if ("A".equals(strFrom)) {

				String strAreaId = jacksonUtil.getString("areaId");

				// String strAreaZone = jacksonUtil.getString("areaZone");

				String strAreaType = jacksonUtil.getString("areaType");

				if (null == strAreaId || "".equals(strAreaId)) {
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatypeStateY");
					strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}

				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetColuidAreaNameBySiteidAreaid");
				Map<String, Object> mapColu = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strAreaId });
				if (null != mapColu) {
					strColuId = mapColu.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapColu.get("TZ_COLU_ID"));
				}

			}

			int numNowPage = jacksonUtil.getInt("page");

			int numPageSize = jacksonUtil.getInt("pagesize");

			int numPageRow = 0;

			if (numPageSize > 0) {
				numPageRow = numPageSize;
			}

			if (numNowPage == 0) {
				numNowPage = 1;
			}

			int numTotalRow = 0;
			int numTotalPage = 0;
			
			//查看当前用户有没有设置范围;
			//如果没有设置范围，且没有报报名表则显示全部的;
			//其他的显示并集;
			//String jgId = tzLoginServiceImpl.getLoginedManagerOrgid(request); 
			//String isPrjShowWW = sqlQuery.queryForObject("select TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_JG_ID=? AND TZ_REG_FIELD_ID='TZ_PROJECT'", new Object[]{jgId},"String");
			String isPrjShowWW = sqlQuery.queryForObject("select TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_SITEI_ID=? AND TZ_REG_FIELD_ID='TZ_PROJECT'", new Object[]{strSiteId},"String");
			int haveBmCount = 0;
			int selectShowCount = 0;
			
			if("Y".equals(isPrjShowWW)){
				String haveBmCountSql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetCurBmbCountByOprid");
				//有没有报名已经开放班级;
				haveBmCount = sqlQuery.queryForObject(haveBmCountSql, new Object[]{oprid,strSiteId},"Integer");
				//有没有选择查看的范围;
				selectShowCount = sqlQuery.queryForObject("SELECT count(1) FROM PS_SHOW_PRJ_NEWS_T where OPRID=?", new Object[]{oprid}, "Integer");
			}
			
			// 取得总条数
			try {
				if(haveBmCount == 0 && selectShowCount==0){
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetCountOfSiteColu");
					numTotalRow = Integer.parseInt(sqlQuery.queryForObject(sql, new Object[] {strSiteId, strColuId}, "String"));
				}else{
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetCountOfSiteColuByProject");
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] {strSiteId, strColuId,oprid,oprid}, "Integer");
				}
			} catch (Exception ep) {
				numTotalRow = 0;
			}

			// 计算总页数
			if (Math.floorMod(numTotalRow, numPageRow) == 0) {
				numTotalPage = numTotalRow / numPageRow;
			} else {
				numTotalPage = (int) (Math.ceil(numTotalRow / numPageRow) + 1);
			}

			int numIndex = 1;
			String strDivPage = "";
			int maxPageNum = 6;

			if (numNowPage > maxPageNum) {
				numIndex = numNowPage - maxPageNum + 1;
			}

			for (int i = numIndex; i <= numTotalPage; i++) {
				String strPageNowClass = "";

				if (numNowPage == i) {
					strPageNowClass = "class=\"now\"";
				}

				strDivPage = strDivPage + "<li " + strPageNowClass + " onclick=\"QueryColu(" + String.valueOf(i)
						+ ")\">" + String.valueOf(i) + "</li>";

				if (numNowPage > maxPageNum) {
					if (i >= numNowPage) {
						break;
					}
				} else if (i >= maxPageNum) {
					break;
				}

			}

			if (!"".equals(strDivPage)) {
				int setNowPageNum = 1;
				if (numNowPage > 2) {
					setNowPageNum = numNowPage - 1;
				}
				strDivPage = "<li onclick=\"QueryColu(" + String.valueOf(setNowPageNum) + ")\">&lt;&lt;</li>"
						+ strDivPage;

				int setTotalPageNum = numTotalPage;
				if ((numNowPage + 1) < numTotalPage) {
					setTotalPageNum = numNowPage + 1;
				}
				strDivPage = strDivPage + "<li onclick=\"QueryColu(" + String.valueOf(setTotalPageNum)
						+ ")\">&gt;&gt;</li>";

			}

			/*
			 * strDivPage = strDivPage.replace((char) (10), ' '); strDivPage =
			 * strDivPage.replace((char) (13), ' '); strDivPage =
			 * strDivPage.replace("\\", "\\\\"); strDivPage =
			 * strDivPage.replace("'", "\\'"); strDivPage =
			 * strDivPage.replace("\"", "\\\"");
			 */

			// 当前第几页
			String strNowPageDesc = "";
			if ("ENG".equals(strLang)) {
				strNowPageDesc = strPage + " <span id=\"nowpage\">" + String.valueOf(numNowPage) + "</span>  "
						+ strTotal + " " + String.valueOf(numTotalPage);
			} else {
				strNowPageDesc = strNum + " <span id=\"nowpage\">" + String.valueOf(numNowPage) + "</span>/"
						+ String.valueOf(numTotalPage) + " " + strPage;
			}

			/*
			 * strNowPageDesc = strNowPageDesc.replace((char) (10), ' ');
			 * strNowPageDesc = strNowPageDesc.replace((char) (13), ' ');
			 * strNowPageDesc = strNowPageDesc.replace("\\", "\\\\");
			 * strNowPageDesc = strNowPageDesc.replace("'", "\\'");
			 * strNowPageDesc = strNowPageDesc.replace("\"", "\\\"");
			 */

			// 查询的最大行，最小行
			//int numMaxRow = numNowPage * numPageRow;
			//int numMinRow = (numNowPage - 1) * numPageRow + 1;
			int numMinRow = (numNowPage - 1) * numPageRow;
			List<Map<String, Object>> listSiteArts;
			if(haveBmCount == 0 && selectShowCount==0){
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteArtsList");
				listSiteArts = sqlQuery.queryForList(sql,
						new Object[] { strSiteId, strColuId, numMinRow, numPageRow });
			}else{
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteArtsListByProject");
				listSiteArts = sqlQuery.queryForList(sql,
						new Object[] { strSiteId, strColuId,oprid,oprid, numMinRow, numPageRow });
			}

			String strResultContent = "";
			String dispatcherUrl = request.getContextPath() + "/dispatcher";

			String dtFormat = getSysHardCodeVal.getDateTimeHMFormat();
			SimpleDateFormat datetimeformat = new SimpleDateFormat(dtFormat);
			
			for (Map<String, Object> mapSiteArt : listSiteArts) {
				
				strColuId = mapSiteArt.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapSiteArt.get("TZ_COLU_ID"));
				String strArtId = mapSiteArt.get("TZ_ART_ID") == null ? ""
						: String.valueOf(mapSiteArt.get("TZ_ART_ID"));
				String strArtTitle = mapSiteArt.get("TZ_ART_TITLE") == null ? ""
						: String.valueOf(mapSiteArt.get("TZ_ART_TITLE"));
				String strArtTime = mapSiteArt.get("TZ_ART_NEWS_DT") == null ? ""
						: datetimeformat.format(mapSiteArt.get("TZ_ART_NEWS_DT"));
				

				String strUrl = dispatcherUrl + "?classid=art_view&operatetype=HTML&siteId=" + strSiteId + "&columnId="
						+ strColuId + "&artId=" + strArtId + "&oprate=R";

				strResultContent = strResultContent
						+ "<li><div class=\"main_mid_recruit_list_title2\"><a target=\"_blank\" href=" + strUrl + ">"
						+ strArtTitle + "</a></div><div class=\"main_mid_recruit_list_date\">" + strArtTime
						+ "</div></li>";

			}

			if ("".equals(strResultContent)) {
				strResultContent = "<div style='font-size:16px;text-align:center;font-weight:bold;margin-top:20px;'>"
						+ gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "71", strLang,
								"暂时没有相关内容！", "There is no content recently.")
						+ "</div>";
			}

			/*
			 * strResultContent = strResultContent.replace((char) (10), ' ');
			 * strResultContent = strResultContent.replace((char) (13), ' ');
			 * strResultContent = strResultContent.replace("\\", "\\\\");
			 * strResultContent = strResultContent.replace("'", "\\'");
			 * strResultContent = strResultContent.replace("\"", "\\\"");
			 */

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("coluItem", strResultContent);
			mapRet.put("divPage", strDivPage);
			mapRet.put("nowPageDesc", strNowPageDesc);

			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "获取数据失败！";
			strRet = "获取数据失败！";
		}

		return strRet;

	}

}
