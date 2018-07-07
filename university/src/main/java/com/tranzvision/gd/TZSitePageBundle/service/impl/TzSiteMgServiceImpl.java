/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAtypTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiCdpfTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiColuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMenuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMnpfTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMtypTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiTempTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAtypT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiCdpfT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMnpfT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMtypT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempTWithBLOBs;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemDefnTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemDefnTWithBLOBs;
import com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.RegisteServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 站点页面处理程序，原PS：TZ_SITE_DECORATED_APP:TZ_SITE_MG_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-11
 */

@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzSiteMgServiceImpl")
public class TzSiteMgServiceImpl extends FrameworkImpl {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private RegisteServiceImpl registeServiceImpl;

	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;

	@Autowired
	private PsTzSitemDefnTMapper psTzSitemDefnTMapper;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;

	@Autowired
	private PsTzSiteiAtypTMapper psTzSiteiAtypTMapper;

	@Autowired
	private PsTzSiteiMenuTMapper psTzSiteiMenuTMapper;

	@Autowired
	private PsTzSiteiMnpfTMapper psTzSiteiMnpfTMapper;

	@Autowired
	private PsTzSiteiMtypTMapper psTzSiteiMtypTMapper;

	@Autowired
	private PsTzSiteiCdpfTMapper psTzSiteiCdpfTMapper;

	@Autowired
	private PsTzSiteiColuTMapper psTzSiteiColuTMapper;

	@Autowired
	private PsTzSiteiTempTMapper psTzSiteiTempTMapper;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

		try {

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql = "select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ENABLE='Y' and TZ_JG_ID=?";
			String strSiteId = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");

			sql = "select * from PS_TZ_SITE_FUNC_T where STATE='Y'";
			List<?> listData = sqlQuery.queryForList(sql);

			for (Object obj : listData) {

				Map<String, Object> mapData = (Map<String, Object>) obj;

				Map<String, Object> mapJson = new HashMap<String, Object>();

				mapJson.put("siteId", strSiteId);
				mapJson.put("text", String.valueOf(mapData.get("FUNC_NAME")));
				mapJson.put("type", String.valueOf(mapData.get("FUNC_TYPE")));
				mapJson.put("url", String.valueOf(mapData.get("URL")));
				mapJson.put("icon", String.valueOf(mapData.get("ICON")));
				mapJson.put("desc", String.valueOf(mapData.get("DESCR")));

				listJson.add(mapJson);
			}

			mapRet.replace("total", listJson.size());
			mapRet.replace("root", listJson);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.clear();
			mapRet.put("success", false);
			errorMsg[0] = "1";
			errorMsg[1] = "获取功能列表数据出错！";
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
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

				// 类型标志，init初始化，update更新
				String strFlag = jacksonUtil.getString("typeFlag");

				// 信息内容
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				String strSiteId = mapData.get("siteId") == null ? "" : String.valueOf(mapData.get("siteId"));
				String strSkinId = mapData.get("skinId") == null ? "" : String.valueOf(mapData.get("skinId"));
				String strSiteIId = mapData.get("siteIId") == null ? "" : String.valueOf(mapData.get("siteIId"));

				String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				if (null == strJgid || "".equals(strJgid)) {
					errMsg[0] = "1";
					errMsg[1] = "机构编号为空！";
					mapRet.put("success", false);
					return jacksonUtil.Map2json(mapRet);
				}

				// 机构站点实例静态文件路径
				String strJgStaticFilesPath = "/" + strJgid.toLowerCase() + "/" + strSiteIId;
				String sql = "";

				// css文件存储路径
				// sql = "select TZ_SKIN_STOR from PS_TZ_SITEI_DEFN_T where
				// TZ_SITEI_ID=?";
				// String strSkinStor = sqlQuery.queryForObject(sql, new
				// Object[]{strSiteIId}, "String");
				String strSkinSavePath = getSysHardCodeVal.getWebsiteCssPath();
				if (null != strSkinSavePath && !"".equals(strSkinSavePath)) {
					strSkinSavePath = strSkinSavePath + strJgStaticFilesPath;
				}

				// 样式代码
				sql = "select TZ_SKIN_NAME,TZ_SKIN_CODE,TZ_SKIN_MCODE from PS_TZ_SITEM_SKIN_T where TZ_SITEM_ID=? and TZ_SKIN_ID=? and TZ_SKIN_STATE='Y'";
				Map<String, Object> mapSkinM = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strSkinId });
				String strSkinName = mapSkinM.get("TZ_SKIN_NAME") == null ? ""	: String.valueOf(mapSkinM.get("TZ_SKIN_NAME"));
				String strSkinCode = mapSkinM.get("TZ_SKIN_CODE") == null ? ""	: String.valueOf(mapSkinM.get("TZ_SKIN_CODE"));
				String strSkinMCode = mapSkinM.get("TZ_SKIN_MCODE") == null ? ""	: String.valueOf(mapSkinM.get("TZ_SKIN_MCODE"));

				String strSkinCodeCss = siteRepCssServiceImpl.repContextPath(strSkinCode);
				String strSkinMCodeCss = siteRepCssServiceImpl.repContextPath(strSkinMCode);

				if ("update".equals(strFlag)) {
					// 更新样式文件
					if (null != strSkinSavePath && !"".equals(strSkinSavePath)) {
						// 写css文件
						String strCssFile = "style_" + strJgid.toLowerCase() + ".css";
						//写入手机版css文件
						String strMobileCssFile = "mobileStyle_" + strJgid.toLowerCase() + ".css";
						
						boolean boolRst = fileManageServiceImpl.UpdateFile(strSkinSavePath, strCssFile,	strSkinCodeCss.getBytes());
						if (!boolRst) {
							errMsg[0] = "1";
							errMsg[1] = "更新样式文件失败！";
							mapRet.put("success", false);
							return jacksonUtil.Map2json(mapRet);
						}else{
							if(strSkinMCode!=null&&!"".equals(strSkinMCode)){
								boolRst = fileManageServiceImpl.UpdateFile(strSkinSavePath, strMobileCssFile,	strSkinMCodeCss.getBytes());
								if(!boolRst){
									errMsg[0] = "1";
									errMsg[1] = "更新手机版样式文件失败！";
									mapRet.put("success", false);
									return jacksonUtil.Map2json(mapRet);
								}
							}							
						}
					}

					PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();
					psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteIId);
					psTzSiteiDefnTWithBLOBs.setTzSkinId(strSkinId);
					psTzSiteiDefnTWithBLOBs.setTzSkinName(strSkinName);
					psTzSiteiDefnTWithBLOBs.setTzSkinCode(strSkinCode);
					psTzSiteiDefnTWithBLOBs.setTzSkinMcode(strSkinMCode);
					psTzSiteiDefnTWithBLOBs.setTzLastmantDttm(dateNow);
					psTzSiteiDefnTWithBLOBs.setTzLastmantOprid(oprid);

					psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

					mapRet.put("success", true);
					mapRet.put("siteId", strSiteIId);
					strRet = jacksonUtil.Map2json(mapRet);

				} else if ("init".equals(strFlag)) {

					// 站点实例化
					// 1.取出站点模板数据
					PsTzSitemDefnTWithBLOBs psTzSitemDefnTWithBLOBs = psTzSitemDefnTMapper
							.selectByPrimaryKey(strSiteId);

					if (null != psTzSitemDefnTWithBLOBs) {

						// 2.初始化站点实例数据表
						PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();
						// 站点编号
						psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteIId);
						// 站点模板
						psTzSiteiDefnTWithBLOBs.setTzSitemId(strSiteId);

						// 2.1生成各类文件的存储路径
						// 图片存储文件夹
						psTzSiteiDefnTWithBLOBs.setTzImgStor(psTzSitemDefnTWithBLOBs.getTzImgStor());

						// 图片访问文件夹
						psTzSiteiDefnTWithBLOBs.setTzImgView(psTzSitemDefnTWithBLOBs.getTzImgView());

						// 附件存储文件夹
						psTzSiteiDefnTWithBLOBs.setTzAttsStor(psTzSitemDefnTWithBLOBs.getTzAttsStor());

						// 附件访问文件夹
						psTzSiteiDefnTWithBLOBs.setTzAttsView(psTzSitemDefnTWithBLOBs.getTzAttsView());

						// 视频存储文件夹
						psTzSiteiDefnTWithBLOBs.setTzVideoStor(psTzSitemDefnTWithBLOBs.getTzVideoStor());

						// 视频访问文件夹
						psTzSiteiDefnTWithBLOBs.setTzVideoView(psTzSitemDefnTWithBLOBs.getTzVideoView());

						// 皮肤存储文件夹（应该用不到了）
						psTzSiteiDefnTWithBLOBs.setTzSkinStor(psTzSitemDefnTWithBLOBs.getTzSkinStor());

						// 2.2实例化站点样式文件
						psTzSiteiDefnTWithBLOBs.setTzSkinId(strSkinId);
						psTzSiteiDefnTWithBLOBs.setTzSkinName(strSkinName);
						psTzSiteiDefnTWithBLOBs.setTzSkinCode(strSkinCode);
						psTzSiteiDefnTWithBLOBs.setTzSkinMcode(strSkinMCode);
						// 更新样式文件
						if (null != strSkinSavePath && !"".equals(strSkinSavePath)) {
							// 写css文件
							String strCssFile = "style_" + strJgid.toLowerCase() + ".css";
							//写入手机版css文件
							String strMobileCssFile = "mobileStyle_" + strJgid.toLowerCase() + ".css";
							
							boolean boolRst = fileManageServiceImpl.UpdateFile(strSkinSavePath, strCssFile,	strSkinCodeCss.getBytes());
							if (!boolRst) {
								errMsg[0] = "1";
								errMsg[1] = "生成样式文件失败！";
								mapRet.put("success", false);
								return jacksonUtil.Map2json(mapRet);
							}else{
								if(strSkinMCode!=null&&!"".equals(strSkinMCode)){
									boolRst = fileManageServiceImpl.UpdateFile(strSkinSavePath, strMobileCssFile,	strSkinMCodeCss.getBytes());
									if(!boolRst){
										errMsg[0] = "1";
										errMsg[1] = "更新手机版样式文件失败！";
										mapRet.put("success", false);
										return jacksonUtil.Map2json(mapRet);
									}
								}							
							}

						}

						// 2.3实例化其他数据
						String strIndexInitcode = psTzSitemDefnTWithBLOBs.getTzIndexInitcode() == null ? ""
								: psTzSitemDefnTWithBLOBs.getTzIndexInitcode();
						String strIndexSavecode = psTzSitemDefnTWithBLOBs.getTzIndexInitcode() == null ? ""
								: psTzSitemDefnTWithBLOBs.getTzIndexInitcode();
						String strLoginInitcode = psTzSitemDefnTWithBLOBs.getTzLonginInitcode() == null ? ""
								: psTzSitemDefnTWithBLOBs.getTzLonginInitcode();
						String strLoginSavecode = psTzSitemDefnTWithBLOBs.getTzLonginInitcode() == null ? ""
								: psTzSitemDefnTWithBLOBs.getTzLonginInitcode();
						String strEnrollInitcode = psTzSitemDefnTWithBLOBs.getTzEnrollInitcode() == null ? ""
								: psTzSitemDefnTWithBLOBs.getTzEnrollInitcode();
						String strEnrollSavecode = psTzSitemDefnTWithBLOBs.getTzEnrollInitcode() == null ? ""
								: psTzSitemDefnTWithBLOBs.getTzEnrollInitcode();

						psTzSiteiDefnTWithBLOBs.setTzHomeHandPro(psTzSitemDefnTWithBLOBs.getTzHomeHandPro());
						psTzSiteiDefnTWithBLOBs.setTzLoginHandPro(psTzSitemDefnTWithBLOBs.getTzLoginHandPro());
						psTzSiteiDefnTWithBLOBs.setTzRegisHandPro(psTzSitemDefnTWithBLOBs.getTzRegisHandPro());
						psTzSiteiDefnTWithBLOBs.setTzSiteFbzt("N");
						psTzSiteiDefnTWithBLOBs.setTzIndexInitcode(strIndexInitcode);
						psTzSiteiDefnTWithBLOBs.setTzIndexSavecode(strIndexSavecode);
						psTzSiteiDefnTWithBLOBs.setTzLonginInitcode(strLoginInitcode);
						psTzSiteiDefnTWithBLOBs.setTzLonginSavecode(strLoginSavecode);
						psTzSiteiDefnTWithBLOBs.setTzEnrollInitcode(strEnrollInitcode);
						psTzSiteiDefnTWithBLOBs.setTzEnrollSavecode(strEnrollSavecode);
						psTzSiteiDefnTWithBLOBs.setTzLastmantDttm(dateNow);
						psTzSiteiDefnTWithBLOBs.setTzLastmantOprid(oprid);

						psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

						// 3.初始化区域
						// sql = "select * from PS_TZ_SITEM_AREA_T where
						// TZ_SITEM_ID=? and TZ_AREA_STATE='Y' order by
						// TZ_AREA_XH asc";
						sql = "select * from PS_TZ_SITEM_AREA_T where TZ_SITEM_ID=? order by TZ_AREA_XH asc";
						List<Map<String, Object>> listAreas = sqlQuery.queryForList(sql, new Object[] { strSiteId });
						for (Map<String, Object> mapArea : listAreas) {

							String strAreaId = mapArea.get("TZ_AREA_ID") == null ? ""
									: String.valueOf(mapArea.get("TZ_AREA_ID"));

							String strAreaState = mapArea.get("TZ_AREA_STATE") == null ? "N"
									: String.valueOf(mapArea.get("TZ_AREA_STATE"));

							if (!"".equals(strAreaId)) {
								sql = "select 'Y' from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
								String recExists = sqlQuery.queryForObject(sql, new Object[] { strSiteIId, strAreaId },
										"String");

								if ("Y".equals(recExists)) {
									sql = "delete from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
									sqlQuery.update(sql, new Object[] { strSiteIId, strAreaId });
								}

								if (!"Y".equals(strAreaState)) {
									continue;
								}

								String strAreaInitcdoe = mapArea.get("TZ_AREA_CODE") == null ? ""
										: String.valueOf(mapArea.get("TZ_AREA_CODE"));

								String strAreaSavecode = mapArea.get("TZ_AREA_CODE") == null ? ""
										: String.valueOf(mapArea.get("TZ_AREA_CODE"));

								PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

								psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteIId);
								psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);

								psTzSiteiAreaTWithBLOBs.setTzAreaTypeId(mapArea.get("TZ_AREA_TYPE_ID") == null ? ""
										: String.valueOf(mapArea.get("TZ_AREA_TYPE_ID")));
								psTzSiteiAreaTWithBLOBs.setTzColuId(mapArea.get("TZ_COLU_ID") == null ? ""
										: String.valueOf(mapArea.get("TZ_COLU_ID")));
								psTzSiteiAreaTWithBLOBs.setTzAreaState(strAreaState);
								psTzSiteiAreaTWithBLOBs.setTzAreaName(mapArea.get("TZ_AREA_NAME") == null ? ""
										: String.valueOf(mapArea.get("TZ_AREA_NAME")));
								psTzSiteiAreaTWithBLOBs.setTzAreaPosition(mapArea.get("TZ_AREA_POSITION") == null ? ""
										: String.valueOf(mapArea.get("TZ_AREA_POSITION")));
								psTzSiteiAreaTWithBLOBs.setTzAreaXh(mapArea.get("TZ_AREA_XH") == null ? 0
										: Integer.parseInt(String.valueOf(mapArea.get("TZ_AREA_XH"))));
								//yuds增加初始化代码逻辑							
								psTzSiteiAreaTWithBLOBs.setTzShowMobileFlg(mapArea.get("TZ_SHOW_MOBILE_FLG") == null ? ""
										: String.valueOf(mapArea.get("TZ_SHOW_MOBILE_FLG")));
								psTzSiteiAreaTWithBLOBs.setTzShowmOrder(mapArea.get("TZ_SHOWM_ORDER") == null ? 0
										: Integer.parseInt(String.valueOf(mapArea.get("TZ_SHOWM_ORDER"))));

								psTzSiteiAreaTWithBLOBs.setTzAreaCode(strAreaInitcdoe);
								psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strAreaSavecode);

								psTzSiteiAreaTWithBLOBs.setTzAddedDttm(dateNow);
								psTzSiteiAreaTWithBLOBs.setTzAddedOprid(oprid);
								psTzSiteiAreaTWithBLOBs.setTzLastmantDttm(dateNow);
								psTzSiteiAreaTWithBLOBs.setTzLastmantOprid(oprid);

								psTzSiteiAreaTMapper.insertSelective(psTzSiteiAreaTWithBLOBs);

							}

						}

						// 4.初始化区域类型
						// sql = "select * from PS_TZ_SITEM_ATYP_T where
						// TZ_AREA_TYPE_STATE='Y' and TZ_SITEM_ID=?";
						sql = "select * from PS_TZ_SITEM_ATYP_T where TZ_SITEM_ID=?";
						List<Map<String, Object>> listAreaTypes = sqlQuery.queryForList(sql,
								new Object[] { strSiteId });
						for (Map<String, Object> mapAreaType : listAreaTypes) {

							String strAreaTypeId = mapAreaType.get("TZ_AREA_TYPE_ID") == null ? ""
									: String.valueOf(mapAreaType.get("TZ_AREA_TYPE_ID"));

							String strAreaTypeState = mapAreaType.get("TZ_AREA_TYPE_STATE") == null ? "N"
									: String.valueOf(mapAreaType.get("TZ_AREA_TYPE_STATE"));

							sql = "select 'Y' from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
							String recExists = sqlQuery.queryForObject(sql, new Object[] { strSiteIId, strAreaTypeId },
									"String");

							if ("Y".equals(recExists)) {
								sql = "delete from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
								sqlQuery.update(sql, new Object[] { strSiteIId, strAreaTypeId });
							}

							if (!"Y".equals(strAreaTypeState)) {
								continue;
							}

							String strAreaType = mapAreaType.get("TZ_AREA_TYPE") == null ? ""
									: String.valueOf(mapAreaType.get("TZ_AREA_TYPE"));

							PsTzSiteiAtypT psTzSiteiAtypT = new PsTzSiteiAtypT();
							psTzSiteiAtypT.setTzSiteiId(strSiteIId);
							psTzSiteiAtypT.setTzAreaTypeId(strAreaTypeId);
							psTzSiteiAtypT.setTzAreaTypeName(mapAreaType.get("TZ_AREA_TYPE_NAME") == null ? ""
									: String.valueOf(mapAreaType.get("TZ_AREA_TYPE_NAME")));
							psTzSiteiAtypT.setTzAreaType(strAreaType);
							psTzSiteiAtypT.setTzAreaTypeState(strAreaTypeState);
							psTzSiteiAtypT.setTzAreaSetCode(mapAreaType.get("TZ_AREA_SET_CODE") == null ? ""
									: String.valueOf(mapAreaType.get("TZ_AREA_SET_CODE")));
							psTzSiteiAtypT.setTzAreaHtmlCode(mapAreaType.get("TZ_AREA_HTML_CODE") == null ? ""
									: String.valueOf(mapAreaType.get("TZ_AREA_HTML_CODE")));
							//yuds增加初始化逻辑
							psTzSiteiAtypT.setTzPhoneHtmlCode(mapAreaType.get("TZ_PHONE_HTML_CODE") == null ? ""
									: String.valueOf(mapAreaType.get("TZ_PHONE_HTML_CODE")));							
							psTzSiteiAtypT.setTzAddedDttm(dateNow);
							psTzSiteiAtypT.setTzAddedOprid(oprid);
							psTzSiteiAtypT.setTzLastmantDttm(dateNow);
							psTzSiteiAtypT.setTzLastmantOprid(oprid);

							psTzSiteiAtypTMapper.insertSelective(psTzSiteiAtypT);

							// PS程序中有以下语句，觉得应该不需要执行，暂时先注释掉
							// SQLExec("UPDATE PS_TZ_SITEI_AREA_T SET
							// TZ_AREA_TYPE_ID=:1 WHERE TZ_AREA_TYPE_ID=:2 ",
							// &recTZ_SITEI_ATYP_T.TZ_AREA_TYPE_ID.Value,
							// &strAreaTypeId);

							sql = "update PS_TZ_SITEI_AREA_T set TZ_AREA_TYPE=? where TZ_AREA_TYPE_ID=? and TZ_SITEI_ID=?";
							sqlQuery.update(sql, new Object[] { strAreaType, strAreaTypeId, strSiteIId });

						}

						// 5.初始化菜单
						sql = "select * from PS_TZ_SITEM_MENU_T where TZ_SITEM_ID=? and TZ_MENU_STATE='Y' order by TZ_MENU_XH asc";
						List<Map<String, Object>> listMenus = sqlQuery.queryForList(sql, new Object[] { strSiteId });
						int intMenuXH = 0;
						for (Map<String, Object> mapMenu : listMenus) {

							String strMenuTypeId = mapMenu.get("TZ_MENU_TYPE_ID") == null ? ""
									: String.valueOf(mapMenu.get("TZ_MENU_TYPE_ID"));

							PsTzSiteiMenuT psTzSiteiMenuT = new PsTzSiteiMenuT();
							psTzSiteiMenuT.setTzSiteiId(strSiteIId);
							psTzSiteiMenuT
									.setTzMenuId(String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_MENU_T", "TZ_MENU_ID")));

							psTzSiteiMenuT.setTzMenuTypeId(strMenuTypeId);
							psTzSiteiMenuT.setTzMenuName(mapMenu.get("TZ_MENU_NAME") == null ? ""
									: String.valueOf(mapMenu.get("TZ_MENU_NAME")));
							psTzSiteiMenuT.setTzMenuColumn(mapMenu.get("TZ_MENU_COLUMN") == null ? ""
									: String.valueOf(mapMenu.get("TZ_MENU_COLUMN")));
							psTzSiteiMenuT.setTzMenuState(mapMenu.get("TZ_MENU_STATE") == null ? ""
									: String.valueOf(mapMenu.get("TZ_MENU_STATE")));
							psTzSiteiMenuT.setTzIsEditor(mapMenu.get("TZ_IS_EDITOR") == null ? ""
									: String.valueOf(mapMenu.get("TZ_IS_EDITOR")));
							psTzSiteiMenuT.setTzShowMobileFlg(mapMenu.get("TZ_SHOW_MOBILE_FLG") == null ? ""
									: String.valueOf(mapMenu.get("TZ_SHOW_MOBILE_FLG")));
							String strOrder = mapMenu.get("TZ_SHOWM_ORDER") == null ? "0":String.valueOf(mapMenu.get("TZ_SHOWM_ORDER"));
							psTzSiteiMenuT.setTzShowmOrder(Integer.valueOf(strOrder));
							psTzSiteiMenuT.setTzMobileNewflg(mapMenu.get("TZ_MOBILE_NEWFLG") == null ? ""
									: String.valueOf(mapMenu.get("TZ_MOBILE_NEWFLG")));
							/*
							 * 由于模板中的序号没有生成，此处采用记录序号
							 * psTzSiteiMenuT.setTzMenuXh(mapMenu.get(
							 * "TZ_MENU_XH") == null ? 0 :
							 * Integer.parseInt(String.valueOf(mapMenu.get(
							 * "TZ_MENU_XH"))));
							 */
							intMenuXH++;
							psTzSiteiMenuT.setTzMenuXh(intMenuXH);
							psTzSiteiMenuT.setTzIsDel(
									mapMenu.get("TZ_IS_DEL") == null ? "" : String.valueOf(mapMenu.get("TZ_IS_DEL")));

							psTzSiteiMenuT.setTzAddedDttm(dateNow);
							psTzSiteiMenuT.setTzAddedOprid(oprid);
							psTzSiteiMenuT.setTzLastmantDttm(dateNow);
							psTzSiteiMenuT.setTzLastmantOprid(oprid);

							psTzSiteiMenuTMapper.insertSelective(psTzSiteiMenuT);

							// 5.1初始化菜单图片
							sql = "select * from PS_TZ_SITEM_CDPF_T where TZ_SITEM_ID=? and TZ_MENU_TYPE_ID=?";
							List<Map<String, Object>> listMenuPics = sqlQuery.queryForList(sql,
									new Object[] { strSiteId, strMenuTypeId });
							for (Map<String, Object> mapMenuPic : listMenuPics) {

								PsTzSiteiMnpfT psTzSiteiMnpfT = new PsTzSiteiMnpfT();
								psTzSiteiMnpfT.setTzSiteiId(strSiteIId);
								psTzSiteiMnpfT.setTzMenuId(psTzSiteiMenuT.getTzMenuId());
								psTzSiteiMnpfT.setTzSkinId(mapMenuPic.get("TZ_SKIN_ID") == null ? ""
										: String.valueOf(mapMenuPic.get("TZ_SKIN_ID")));
								psTzSiteiMnpfT.setTzSkinState(mapMenuPic.get("TZ_SKIN_STATE") == null ? ""
										: String.valueOf(mapMenuPic.get("TZ_SKIN_STATE")));
								psTzSiteiMnpfT.setTzSkinName(mapMenuPic.get("TZ_SKIN_NAME") == null ? ""
										: String.valueOf(mapMenuPic.get("TZ_SKIN_NAME")));
								psTzSiteiMnpfT.setTzTypeImg(mapMenuPic.get("TZ_TYPE_IMG") == null ? ""
										: String.valueOf(mapMenuPic.get("TZ_TYPE_IMG")));
								psTzSiteiMnpfT.setTzNowImg(mapMenuPic.get("TZ_NOW_IMG") == null ? ""
										: String.valueOf(mapMenuPic.get("TZ_NOW_IMG")));

								psTzSiteiMnpfTMapper.insertSelective(psTzSiteiMnpfT);
							}

						}

						// 6.初始化菜单类型
						sql = "select * from PS_TZ_SITEM_MTYP_T where TZ_SITEM_ID=? and TZ_TYPE_STATE='Y'";
						List<Map<String, Object>> listMenuTypes = sqlQuery.queryForList(sql,
								new Object[] { strSiteId });
						for (Map<String, Object> mapMenuType : listMenuTypes) {

							String strMenuTypeId = mapMenuType.get("TZ_MENU_TYPE_ID") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_MENU_TYPE_ID"));

							PsTzSiteiMtypT psTzSiteiMtypT = new PsTzSiteiMtypT();
							psTzSiteiMtypT.setTzSiteiId(strSiteIId);
							psTzSiteiMtypT.setTzMenuTypeId(
									String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_MTYP_T", "TZ_MENU_TYPE_ID")));

							psTzSiteiMtypT.setTzMenuTypeName(mapMenuType.get("TZ_MENU_TYPE_NAME") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_MENU_TYPE_NAME")));
							psTzSiteiMtypT.setTzTypeState(mapMenuType.get("TZ_TYPE_STATE") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_TYPE_STATE")));
							psTzSiteiMtypT.setTzIsAdd(mapMenuType.get("TZ_IS_ADD") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_IS_ADD")));
							psTzSiteiMtypT.setTzTypeDescr(mapMenuType.get("TZ_TYPE_DESCR") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_TYPE_DESCR")));
							psTzSiteiMtypT.setTzTypeImg(mapMenuType.get("TZ_TYPE_IMG") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_TYPE_IMG")));
							psTzSiteiMtypT.setTzNowImg(mapMenuType.get("TZ_NOW_IMG") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_NOW_IMG")));
							psTzSiteiMtypT.setTzSetMenuCode(mapMenuType.get("TZ_SET_MENU_CODE") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_SET_MENU_CODE")));
							psTzSiteiMtypT.setTzShowMenuCode(mapMenuType.get("TZ_SHOW_MENU_CODE") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_SHOW_MENU_CODE")));
							psTzSiteiMtypT.setTzColuType(mapMenuType.get("TZ_COLU_TYPE") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_COLU_TYPE")));
							psTzSiteiMtypT.setTzTempId(mapMenuType.get("TZ_TEMP_ID") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_TEMP_ID")));
							psTzSiteiMtypT.setTzContType(mapMenuType.get("TZ_CONT_TYPE") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_CONT_TYPE")));
							psTzSiteiMtypT.setTzContTemp(mapMenuType.get("TZ_CONT_TEMP") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_CONT_TEMP")));
							psTzSiteiMtypT.setTzAddColu(mapMenuType.get("TZ_ADD_COLU") == null ? ""
									: String.valueOf(mapMenuType.get("TZ_ADD_COLU")));

							psTzSiteiMtypT.setTzAddedDttm(dateNow);
							psTzSiteiMtypT.setTzAddedOprid(oprid);
							psTzSiteiMtypT.setTzLastmantDttm(dateNow);
							psTzSiteiMtypT.setTzLastmantOprid(oprid);

							psTzSiteiMtypTMapper.insertSelective(psTzSiteiMtypT);

							// 6.1初始化菜单类型图片
							sql = "select * from PS_TZ_SITEM_CDPF_T where TZ_SITEM_ID=? and TZ_MENU_TYPE_ID=?";
							List<Map<String, Object>> listMenuTypePics = sqlQuery.queryForList(sql,
									new Object[] { strSiteId, strMenuTypeId });
							for (Map<String, Object> mapMenuTypePic : listMenuTypePics) {

								PsTzSiteiCdpfT psTzSiteiCdpfT = new PsTzSiteiCdpfT();
								psTzSiteiCdpfT.setTzSiteiId(strSiteIId);
								psTzSiteiCdpfT.setTzMenuTypeId(psTzSiteiMtypT.getTzMenuTypeId());
								psTzSiteiCdpfT.setTzSkinId(mapMenuTypePic.get("TZ_SKIN_ID") == null ? ""
										: String.valueOf(mapMenuTypePic.get("TZ_SKIN_ID")));
								psTzSiteiCdpfT.setTzSkinState(mapMenuTypePic.get("TZ_SKIN_STATE") == null ? ""
										: String.valueOf(mapMenuTypePic.get("TZ_SKIN_STATE")));
								psTzSiteiCdpfT.setTzSkinName(mapMenuTypePic.get("TZ_SKIN_NAME") == null ? ""
										: String.valueOf(mapMenuTypePic.get("TZ_SKIN_NAME")));
								psTzSiteiCdpfT.setTzTypeImg(mapMenuTypePic.get("TZ_TYPE_IMG") == null ? ""
										: String.valueOf(mapMenuTypePic.get("TZ_TYPE_IMG")));
								psTzSiteiCdpfT.setTzNowImg(mapMenuTypePic.get("TZ_NOW_IMG") == null ? ""
										: String.valueOf(mapMenuTypePic.get("TZ_NOW_IMG")));

								psTzSiteiCdpfTMapper.insertSelective(psTzSiteiCdpfT);

							}

							// 更新菜单图片
							sql = "update PS_TZ_SITEI_MENU_T set TZ_MENU_TYPE_ID=?,TZ_TYPE_IMG=?,TZ_NOW_IMG=? where TZ_MENU_TYPE_ID=? and TZ_SITEI_ID=?";
							sqlQuery.update(sql,
									new Object[] { psTzSiteiMtypT.getTzMenuTypeId(), psTzSiteiMtypT.getTzTypeImg(),
											psTzSiteiMtypT.getTzNowImg(), strMenuTypeId, strSiteIId });

						}

						// 7.初始化栏目
						sql = "select * from PS_TZ_SITEM_COLU_T where TZ_SITEM_ID=?";
						//初始化栏目前前插入一个父栏目;
						String SqlsiteName="SELECT TZ_SITEI_NAME FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?";
						String 	siteName=sqlQuery.queryForObject(SqlsiteName, new Object[]{strSiteIId}, "String");
						String  Sqlcount="SELECT COUNT(1) FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=?";
						int loucount=sqlQuery.queryForObject(Sqlcount, new Object[]{strSiteIId}, "Integer");
						if (loucount>0) {
							
						}else{
							PsTzSiteiColuT psTzSiteiColuT1 = new PsTzSiteiColuT();
							
							psTzSiteiColuT1.setTzSiteiId(strSiteIId);
							psTzSiteiColuT1.setTzColuId(String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_COLU_T", "TZ_COLU_ID")));
							psTzSiteiColuT1.setTzColuName(siteName);
							psTzSiteiColuT1.setTzColuState("Y");
							psTzSiteiColuT1.setTzColuLevel(new Integer(0));
							psTzSiteiColuT1.setTzColuType("A");
							psTzSiteiColuTMapper.insertSelective(psTzSiteiColuT1);
						}
						
						
						List<Map<String, Object>> listColus = sqlQuery.queryForList(sql, new Object[] { strSiteId });
						for (Map<String, Object> mapColu : listColus) {

							String strColuId = mapColu.get("TZ_COLU_ID") == null ? ""
									: String.valueOf(mapColu.get("TZ_COLU_ID"));

							PsTzSiteiColuT psTzSiteiColuT = new PsTzSiteiColuT();
							psTzSiteiColuT.setTzSiteiId(strSiteIId);
							psTzSiteiColuT
									.setTzColuId(String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_COLU_T", "TZ_COLU_ID")));

							psTzSiteiColuT.setTzColuName(mapColu.get("TZ_COLU_NAME") == null ? ""
									: String.valueOf(mapColu.get("TZ_COLU_NAME")));
							psTzSiteiColuT.setTzColuType(mapColu.get("TZ_COLU_TYPE") == null ? ""
									: String.valueOf(mapColu.get("TZ_COLU_TYPE")));
							psTzSiteiColuT.setTzTempId(
									mapColu.get("TZ_TEMP_ID") == null ? "" : String.valueOf(mapColu.get("TZ_TEMP_ID")));
							psTzSiteiColuT.setTzContType(mapColu.get("TZ_CONT_TYPE") == null ? ""
									: String.valueOf(mapColu.get("TZ_CONT_TYPE")));
							psTzSiteiColuT.setTzContTemp(mapColu.get("TZ_CONT_TEMP") == null ? ""
									: String.valueOf(mapColu.get("TZ_CONT_TEMP")));
							psTzSiteiColuT.setTzColuState("Y");
							
							/**
							 * 1.添加TZ_ART_TYPE_ID字段的复制;
							 * 2.栏目级别设置成1;
							 * 3.插入父栏目的栏目ID;
							 */
							psTzSiteiColuT.setTzArtTypeId(mapColu.get("TZ_ART_TYPE_ID") == null ? ""
									: String.valueOf(mapColu.get("TZ_ART_TYPE_ID")));
							
							psTzSiteiColuT.setTzColuLevel(new Integer(1));
							
							String sqlflmid="SELECT  TZ_COLU_ID  FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? AND TZ_COLU_LEVEL=0 ";
							String FlmID=sqlQuery.queryForObject(sqlflmid, new Object[]{strSiteIId}, "String");
							psTzSiteiColuT.setTzFColuId(FlmID);
							
							psTzSiteiColuTMapper.insertSelective(psTzSiteiColuT);

							sql = "update PS_TZ_SITEI_AREA_T set TZ_COLU_ID=? where TZ_COLU_ID=? and TZ_SITEI_ID=?";
							sqlQuery.update(sql, new Object[] { psTzSiteiColuT.getTzColuId(), strColuId, strSiteIId });

							sql = "update PS_TZ_SITEI_MENU_T set TZ_MENU_COLUMN=? where TZ_MENU_COLUMN=? and TZ_SITEI_ID=?";
							sqlQuery.update(sql, new Object[] { psTzSiteiColuT.getTzColuId(), strColuId, strSiteIId });

						}

						// 8.初始化模板
						sql = "select * from PS_TZ_SITEM_TEMP_T where TZ_TEMP_STATE='Y' and TZ_SITEM_ID=?";
						List<Map<String, Object>> listTpls = sqlQuery.queryForList(sql, new Object[] { strSiteId });
						for (Map<String, Object> mapTpl : listTpls) {

							String strTempId = mapTpl.get("TZ_TEMP_ID") == null ? ""
									: String.valueOf(mapTpl.get("TZ_TEMP_ID"));

							PsTzSiteiTempTWithBLOBs psTzSiteiTempTWithBLOBs = new PsTzSiteiTempTWithBLOBs();
							psTzSiteiTempTWithBLOBs.setTzSiteiId(strSiteIId);
							psTzSiteiTempTWithBLOBs
									.setTzTempId(String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_TEMP_T", "TZ_TEMP_ID")));

							psTzSiteiTempTWithBLOBs.setTzTempState(mapTpl.get("TZ_TEMP_STATE") == null ? ""
									: String.valueOf(mapTpl.get("TZ_TEMP_STATE")));
							psTzSiteiTempTWithBLOBs.setTzTempName(mapTpl.get("TZ_TEMP_NAME") == null ? ""
									: String.valueOf(mapTpl.get("TZ_TEMP_NAME")));
							psTzSiteiTempTWithBLOBs.setTzTempType(mapTpl.get("TZ_TEMP_TYPE") == null ? ""
									: String.valueOf(mapTpl.get("TZ_TEMP_TYPE")));
							psTzSiteiTempTWithBLOBs.setTzTempPccode(mapTpl.get("TZ_TEMP_PCCODE") == null ? ""
									: String.valueOf(mapTpl.get("TZ_TEMP_PCCODE")));
							psTzSiteiTempTWithBLOBs.setTzTempMscode(mapTpl.get("TZ_TEMP_MSCODE") == null ? ""
									: String.valueOf(mapTpl.get("TZ_TEMP_MSCODE")));
							psTzSiteiTempTWithBLOBs.setTzPctempScriptHtml(mapTpl.get("TZ_PCTEMP_SCRIPT_HTML") == null
									? "" : String.valueOf(mapTpl.get("TZ_PCTEMP_SCRIPT_HTML")));
							psTzSiteiTempTWithBLOBs.setTzMstempScriptHtml(mapTpl.get("TZ_MSTEMP_SCRIPT_HTML") == null
									? "" : String.valueOf(mapTpl.get("TZ_MSTEMP_SCRIPT_HTML")));

							
							
							psTzSiteiTempTMapper.insertSelective(psTzSiteiTempTWithBLOBs);

							sql = "update PS_TZ_SITEI_COLU_T set TZ_TEMP_ID=? where TZ_TEMP_ID=? and TZ_SITEI_ID=?";
							sqlQuery.update(sql,
									new Object[] { psTzSiteiTempTWithBLOBs.getTzTempId(), strTempId, strSiteIId });

							sql = "update PS_TZ_SITEI_COLU_T set TZ_CONT_TEMP=? where TZ_CONT_TEMP=? and TZ_SITEI_ID=?";
							sqlQuery.update(sql,
									new Object[] { psTzSiteiTempTWithBLOBs.getTzTempId(), strTempId, strSiteIId });

							sql = "update PS_TZ_SITEI_MTYP_T set TZ_TEMP_ID=? where TZ_TEMP_ID=? and TZ_SITEI_ID=?";
							sqlQuery.update(sql,
									new Object[] { psTzSiteiTempTWithBLOBs.getTzTempId(), strTempId, strSiteIId });

							sql = "update PS_TZ_SITEI_MTYP_T set TZ_CONT_TEMP=? where TZ_CONT_TEMP=? and TZ_SITEI_ID=?";
							sqlQuery.update(sql,
									new Object[] { psTzSiteiTempTWithBLOBs.getTzTempId(), strTempId, strSiteIId });

						}

						mapRet.put("success", true);
						mapRet.put("siteId", strSiteIId);
						strRet = jacksonUtil.Map2json(mapRet);

					} else {

						errMsg[0] = "1";
						errMsg[1] = "站点模板不存在！";
						mapRet.put("success", false);
						strRet = jacksonUtil.Map2json(mapRet);

					}

				} else {

					errMsg[0] = "1";
					errMsg[1] = "请求参数异常！";
					mapRet.put("success", false);
					strRet = jacksonUtil.Map2json(mapRet);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "请求添加区域异常！";
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;
	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> formData = jacksonUtil.getMap("data");

				switch (typeFlag) {
				case "save":
					strRet = this.savePage(formData, errMsg);
					break;
				case "release":
					strRet = this.releasePage(formData, errMsg);
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 保存首页、登录页、注册页
	 * 
	 * @param formData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	private String savePage(Map<String, Object> formData, String[] errMsg) {

		String strRet = "";

		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String strSiteIdSrc = formData.get("siteId") == null ? "" : String.valueOf(formData.get("siteId"));
			String strSiteId = "";
			if ("".equals(strSiteIdSrc)) {

				return strRet;
			}

			String strSaveContent = formData.get("savecontent") == null ? ""
					: String.valueOf(formData.get("savecontent"));
			String strPagetype = formData.get("pagetype") == null ? "" : String.valueOf(formData.get("pagetype"));
			strPagetype = strPagetype.toLowerCase();
			

			ArrayList<Map<String, Object>> listActData = (ArrayList<Map<String, Object>>) formData.get("dataArea");

			for (Map<String, Object> mapActData : listActData) {

				strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));
				if ("".equals(strSiteId)) {
					continue;
				}

				String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				// String strOrgId = sqlQuery.queryForObject(sql, new Object[] {
				// strSiteId }, "String");

				String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));
				// String strAreaZone = mapActData.get("areaZone") == null ? "":
				// String.valueOf(mapActData.get("areaZone"));
				String strAreaType = mapActData.get("areaType") == null ? ""
						: String.valueOf(mapActData.get("areaType"));

				String strAreaTypeId = "";
				if ("".equals(strAreaId)) {
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_TYPE=? and TZ_AREA_STATE='Y'";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				} else {
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				}

				if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {
					sql = "select TZ_AREA_HTML_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? AND TZ_AREA_TYPE_ID=?";
					String strAreaServiceImpl = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaTypeId },
							"String");

					if (null != strAreaServiceImpl && !"".equals(strAreaServiceImpl)) {
						TzSiteActionServiceImpl areaServiceImpl = (TzSiteActionServiceImpl) ctx
								.getBean(strAreaServiceImpl);
						areaServiceImpl.tzSaveArea(mapActData, errMsg);
					}

				}

			}

			if (!"".equals(strSaveContent)) {

				boolean boolResult = false;

				String strHomePageCode = "";
				String strLoginPageCode = "";
				String strEnrollPageCode = "";
				String strMEnrollPageCode = "";
				
				String sql = "select 'Y' from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				if ("Y".equals(recExists)) {

					switch (strPagetype) {

					case "homepage":
						boolResult = this.saveHomepage(strSaveContent, strSiteId, errMsg);
						if (boolResult) {
							strLoginPageCode = this.handleLoginPage(strSiteId);
							if (strLoginPageCode != null && !"".equals(strLoginPageCode)) {
								boolResult = this.saveLoginpage(strLoginPageCode, strSiteId, errMsg);
								if (boolResult) {

									// strEnrollPageCode =
									// this.handleEnrollPage(strSiteId);
									strEnrollPageCode = registeServiceImpl.handleEnrollPage(strSiteId);
									if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
										boolResult = registeServiceImpl.saveEnrollpage(strEnrollPageCode, strSiteId,
												errMsg);
										if (boolResult) {
											strMEnrollPageCode = registeServiceImpl.handleMEnrollPage(strSiteId);
											if (strMEnrollPageCode != null && !"".equals(strMEnrollPageCode)) {
												boolResult = registeServiceImpl.saveMEnrollpage(strMEnrollPageCode, strSiteId,errMsg);
												if(boolResult){
													errMsg[0] = "0";
													errMsg[1] = "站点发布完成！";
													mapRet.put("success", true);
													strRet = jacksonUtil.Map2json(mapRet);
												}else{
													errMsg[0] = "1";
													errMsg[1] = "站点手机注册页保存失败！";
												}
											}else{
												errMsg[0] = "1";
												errMsg[1] = "站点手机注册页保存失败！";
											}
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点注册页保存失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点注册页保存失败！";
									}

								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点登录页保存失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点登录页保存失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点首页保存失败！";
						}

						break;

					case "loginpage":
						boolResult = this.saveLoginpage(strSaveContent, strSiteId, errMsg);
						if (boolResult) {
							strHomePageCode = this.handleHomePage(strSiteId);
							if (strHomePageCode != null && !"".equals(strHomePageCode)) {
								boolResult = this.saveHomepage(strHomePageCode, strSiteId, errMsg);
								if (boolResult) {

									// strEnrollPageCode =
									// this.handleEnrollPage(strSiteId);
									strEnrollPageCode = registeServiceImpl.handleEnrollPage(strSiteId);
									if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
										boolResult = registeServiceImpl.saveEnrollpage(strEnrollPageCode, strSiteId,
												errMsg);
										if (boolResult) {
											strMEnrollPageCode = registeServiceImpl.handleMEnrollPage(strSiteId);
											if (strMEnrollPageCode != null && !"".equals(strMEnrollPageCode)) {
												boolResult = registeServiceImpl.saveMEnrollpage(strMEnrollPageCode, strSiteId,errMsg);
												if(boolResult){
													errMsg[0] = "0";
													errMsg[1] = "站点发布完成！";
													mapRet.put("success", true);
													strRet = jacksonUtil.Map2json(mapRet);
												}else{
													errMsg[0] = "1";
													errMsg[1] = "站点手机注册页保存失败！";
												}
											}else{
												errMsg[0] = "1";
												errMsg[1] = "站点手机注册页保存失败！";
											}
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点注册页保存失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点注册页保存失败！";
									}

								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点首页保存失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点首页保存失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点登录页保存失败！";
						}

						break;

					case "enrollpage":

						// 由于注册页不需要站点装修功能，因此没有单独的保存按钮，此段代码不需要执行
						strEnrollPageCode = this.handleEnrollPage(strSiteId);
						if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
							boolResult = this.saveEnrollpage(strEnrollPageCode, strSiteId, errMsg);
							if (boolResult) {
								strHomePageCode = this.handleHomePage(strSiteId);
								if (strHomePageCode != null && !"".equals(strHomePageCode)) {
									boolResult = this.saveHomepage(strHomePageCode, strSiteId, errMsg);
									if (boolResult) {
										strLoginPageCode = this.handleLoginPage(strSiteId);
										if (strLoginPageCode != null && !"".equals(strLoginPageCode)) {
											boolResult = this.saveLoginpage(strLoginPageCode, strSiteId, errMsg);
											if (boolResult) {
												errMsg[0] = "0";
												errMsg[1] = "站点保存完成！";
												mapRet.put("success", true);
												strRet = jacksonUtil.Map2json(mapRet);
											} else {
												errMsg[0] = "1";
												errMsg[1] = "站点登录页保存失败！";
											}
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点登录页保存失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点首页保存失败！";
									}
								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点首页保存失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点注册页保存失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点注册页保存失败！";
						}

						break;
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "获取参数失败！";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "保存失败！" + e.toString();
		}

		return strRet;

	}

	/**
	 * 发布首页、登录页、注册页
	 * 
	 * @param formData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	private String releasePage(Map<String, Object> formData, String[] errMsg) {

		String strRet = "";

		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String strSiteIdSrc = formData.get("siteId") == null ? "" : String.valueOf(formData.get("siteId"));
			String strSiteId = "";
			if ("".equals(strSiteIdSrc)) {

				return strRet;
			}

			String strReleaseContent = formData.get("releasecontent") == null ? ""
					: String.valueOf(formData.get("releasecontent"));
			String strPagetype = formData.get("pagetype") == null ? "" : String.valueOf(formData.get("pagetype"));

			ArrayList<Map<String, Object>> listActData = (ArrayList<Map<String, Object>>) formData.get("dataArea");

			for (Map<String, Object> mapActData : listActData) {

				strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));
				if ("".equals(strSiteId)) {
					continue;
				}

				String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));
				// String strAreaZone = mapActData.get("areaZone") == null ? "":
				// String.valueOf(mapActData.get("areaZone"));
				String strAreaType = mapActData.get("areaType") == null ? ""
						: String.valueOf(mapActData.get("areaType"));

				String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				// String strOrgId = sqlQuery.queryForObject(sql, new Object[] {
				// strSiteId }, "String");

				String strAreaTypeId = "";
				if (!"".equals(strAreaId)) {
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				} else {
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE=? and TZ_AREA_TYPE_STATE='Y'";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}

				if (strAreaTypeId != null && !"".equals(strAreaTypeId)) {
					sql = "select TZ_AREA_HTML_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
					String strAreaServiceImpl = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaTypeId },
							"String");

					if (null != strAreaServiceImpl && !"".equals(strAreaServiceImpl)) {
						TzSiteActionServiceImpl areaServiceImpl = (TzSiteActionServiceImpl) ctx
								.getBean(strAreaServiceImpl);
						areaServiceImpl.tzReleaseArea(mapActData, errMsg);
					}

				}

			}

			if (!"".equals(strReleaseContent)) {

				boolean boolResult = false;

				String strHomePageCode = "";
				String strLoginPageCode = "";
				String strEnrollPageCode = "";
				String strMEnrollPageCode = "";
				
				String sql = "select 'Y' from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				if ("Y".equals(recExists)) {

					switch (strPagetype) {

					case "homepage":
						boolResult = this.releasHomepage(strReleaseContent, strSiteId, errMsg);
						if (boolResult) {
							strLoginPageCode = this.handleLoginPage(strSiteId);
							if (strLoginPageCode != null && !"".equals(strLoginPageCode)) {
								boolResult = this.releasLoginpage(strLoginPageCode, strSiteId, errMsg);
								if (boolResult) {

									errMsg[0] = "0";
									errMsg[1] = "站点发布完成！";
									mapRet.put("success", true);
									strRet = jacksonUtil.Map2json(mapRet);

									// strEnrollPageCode =
									// this.handleEnrollPage(strSiteId);
									strEnrollPageCode = registeServiceImpl.handleEnrollPage(strSiteId);
									if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
										boolResult = registeServiceImpl.releasEnrollpage(strEnrollPageCode, strSiteId,
												errMsg);
										if (boolResult) {
											strMEnrollPageCode = registeServiceImpl.handleMEnrollPage(strSiteId);
											if (strMEnrollPageCode != null && !"".equals(strMEnrollPageCode)) {
												boolResult = registeServiceImpl.releasMEnrollpage(strMEnrollPageCode, strSiteId,errMsg);
												if(boolResult){
													errMsg[0] = "0";
													errMsg[1] = "站点发布完成！";
													mapRet.put("success", true);
													strRet = jacksonUtil.Map2json(mapRet);
												}else{
													errMsg[0] = "1";
													errMsg[1] = "站点手机注册页发布失败！";
												}
											}else{
												errMsg[0] = "1";
												errMsg[1] = "站点手机注册页发布失败！";
											}
											
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点注册页发布失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点注册页发布失败！";
									}

								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点登录页发布失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点登录页发布失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点首页发布失败！";
						}

						break;

					case "loginpage":
						boolResult = this.releasLoginpage(strReleaseContent, strSiteId, errMsg);
						if (boolResult) {
							strHomePageCode = this.handleHomePage(strSiteId);
							if (strHomePageCode != null && !"".equals(strHomePageCode)) {
								boolResult = this.releasHomepage(strHomePageCode, strSiteId, errMsg);
								if (boolResult) {

									errMsg[0] = "0";
									errMsg[1] = "站点发布完成！";
									mapRet.put("success", true);
									strRet = jacksonUtil.Map2json(mapRet);

									// strEnrollPageCode =
									// this.handleEnrollPage(strSiteId);
									strEnrollPageCode = registeServiceImpl.handleEnrollPage(strSiteId);
									if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
										boolResult = registeServiceImpl.releasEnrollpage(strEnrollPageCode, strSiteId,
												errMsg);
										if (boolResult) {
											strMEnrollPageCode = registeServiceImpl.handleMEnrollPage(strSiteId);
											if (strMEnrollPageCode != null && !"".equals(strMEnrollPageCode)) {
												boolResult = registeServiceImpl.releasMEnrollpage(strMEnrollPageCode, strSiteId,errMsg);
												if(boolResult){
													errMsg[0] = "0";
													errMsg[1] = "站点发布完成！";
													mapRet.put("success", true);
													strRet = jacksonUtil.Map2json(mapRet);
												}else{
													errMsg[0] = "1";
													errMsg[1] = "站点手机注册页发布失败！";
												}
											}else{
												errMsg[0] = "1";
												errMsg[1] = "站点手机注册页发布失败！";
											}
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点注册页发布失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点注册页发布失败！";
									}

								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点首页发布失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点首页发布失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点登录页发布失败！";
						}

						break;

					case "enrollpage":

						// 由于注册页不需要站点装修功能，因此没有单独的发布按钮，此段代码不需要执行
						strEnrollPageCode = this.handleEnrollPage(strSiteId);
						if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
							boolResult = this.releasEnrollpage(strEnrollPageCode, strSiteId, errMsg);
							if (boolResult) {
								strHomePageCode = this.handleHomePage(strSiteId);
								if (strHomePageCode != null && !"".equals(strHomePageCode)) {
									boolResult = this.releasHomepage(strHomePageCode, strSiteId, errMsg);
									if (boolResult) {
										strLoginPageCode = this.handleLoginPage(strSiteId);
										if (strLoginPageCode != null && !"".equals(strLoginPageCode)) {
											boolResult = this.releasLoginpage(strLoginPageCode, strSiteId, errMsg);
											if (boolResult) {
												errMsg[0] = "0";
												errMsg[1] = "站点发布完成！";
												mapRet.put("success", true);
												strRet = jacksonUtil.Map2json(mapRet);
											} else {
												errMsg[0] = "1";
												errMsg[1] = "站点登录页发布失败！";
											}
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点登录页发布失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点首页发布失败！";
									}
								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点首页发布失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点注册页发布失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点注册页发布失败！";
						}

						break;
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "获取参数失败！";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "发布失败！" + e.toString();
		}

		return strRet;
	}

	/**
	 * 首页保存程序
	 * 
	 * @param strSaveContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	public boolean saveHomepage(String strSaveContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				if (!strSaveContent.startsWith("<body") || !strSaveContent.endsWith("</body>")) {
					strSaveContent = "<body style=\"background:#fff\">" + strSaveContent + "</body>";
				}

				String strPreviewContent = strSaveContent;

				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				String ctxPath = request.getContextPath();
				strSaveContent = siteRepCssServiceImpl.repResetContextPath(strSaveContent);
				strSaveContent = siteRepCssServiceImpl.repResetSkinsImgPath(strSaveContent, strSkinId);
				
				//String strSavedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteIndexSaveTpl",strSaveContent);
				String strSaveContentReplace = strSaveContent;
				if(strSaveContentReplace.contains("\\")){
					strSaveContentReplace = strSaveContentReplace.replace("\\", "\\\\");
				}
				if(strSaveContentReplace.contains("$")){
					strSaveContentReplace = strSaveContentReplace.replace("$", "\\$");
				}
				String strSavedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteIndexSaveTpl",strSaveContentReplace);

				strSavedContent = siteRepCssServiceImpl.repTitle(strSavedContent, strSiteId);
				strSavedContent = siteRepCssServiceImpl.repWelcome(strSavedContent, "");
				strSavedContent = siteRepCssServiceImpl.repSiteid(strSavedContent, strSiteId);
				strSavedContent = siteRepCssServiceImpl.repJgid(strSavedContent, orgid);
				strSavedContent = siteRepCssServiceImpl.repLang(strSavedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzIndexSavecode(strSavedContent);

				// 生成预览代码
				//String strPreviewHtml = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteIndexReleaseTpl",strPreviewContent);
				String strPreviewContentReplace = strPreviewContent;
				if(strPreviewContentReplace.contains("\\")){
					strPreviewContentReplace = strPreviewContentReplace.replace("\\", "\\\\");
				}
				if(strPreviewContentReplace.contains("$")){
					strPreviewContentReplace = strPreviewContentReplace.replace("$", "\\$");
				}
				String strPreviewHtml = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteIndexReleaseTpl",strPreviewContentReplace);
				
				strPreviewHtml = siteRepCssServiceImpl.repContextPath(strPreviewHtml);
				strPreviewHtml = siteRepCssServiceImpl.repCss(strPreviewHtml, strSiteId);
				strPreviewHtml = siteRepCssServiceImpl.repSkinsImgPath(strPreviewHtml, strSkinId);

				String strSelfJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsIndexRelease",
						ctxPath);
				strPreviewHtml = siteRepCssServiceImpl.repJavascriptTags(strPreviewHtml, strSelfJavascripts, orgid,
						strSiteId, "Y");

				strPreviewHtml = siteRepCssServiceImpl.repTitle(strPreviewHtml, strSiteId);
				strPreviewHtml = siteRepCssServiceImpl.repWelcome(strPreviewHtml, "");
				strPreviewHtml = siteRepCssServiceImpl.repSdkbar(strPreviewHtml, "");
				strPreviewHtml = siteRepCssServiceImpl.repSiteid(strPreviewHtml, strSiteId);
				strPreviewHtml = siteRepCssServiceImpl.repJgid(strPreviewHtml, orgid);
				strPreviewHtml = siteRepCssServiceImpl.repLang(strPreviewHtml, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzIndexPrecode(strPreviewHtml);
				psTzSiteiDefnTWithBLOBs.setTzLastmantDttm(new Date());
				psTzSiteiDefnTWithBLOBs.setTzLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点首页保存异常！";
		}

		return boolRet;

	}

	/**
	 * 登录页保存程序
	 * 
	 * @param strSaveContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	public boolean saveLoginpage(String strSaveContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				if (!strSaveContent.startsWith("<body") || !strSaveContent.endsWith("</body>")) {
					strSaveContent = "<body style=\"background:#fff\">" + strSaveContent + "</body>";
				}

				String strPreviewContent = strSaveContent;

				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				String ctxPath = request.getContextPath();

				strSaveContent = siteRepCssServiceImpl.repResetContextPath(strSaveContent);
				strSaveContent = siteRepCssServiceImpl.repResetSkinsImgPath(strSaveContent, strSkinId);

				String strSavedHtml = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteLoginSaveTpl", strSaveContent);
				strSavedHtml = siteRepCssServiceImpl.repTitle(strSavedHtml, strSiteId);
				strSavedHtml = siteRepCssServiceImpl.repWelcome(strSavedHtml, "");
				strSavedHtml = siteRepCssServiceImpl.repSiteid(strSavedHtml, strSiteId);
				strSavedHtml = siteRepCssServiceImpl.repJgid(strSavedHtml, orgid);
				strSavedHtml = siteRepCssServiceImpl.repLang(strSavedHtml, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzLonginSavecode(strSavedHtml);

				// 生成预览代码
				String strPreviewHtml = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteLoginReleaseTpl",
						strPreviewContent);

				strPreviewHtml = siteRepCssServiceImpl.repContextPath(strPreviewHtml);
				strPreviewHtml = siteRepCssServiceImpl.repCss(strPreviewHtml, strSiteId);
				strPreviewHtml = siteRepCssServiceImpl.repSkinsImgPath(strPreviewHtml, strSkinId);

				String strSelfJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsLoginRelease",
						ctxPath);
				strPreviewHtml = siteRepCssServiceImpl.repJavascriptTags(strPreviewHtml, strSelfJavascripts, orgid,
						strSiteId, "Y");

				strPreviewHtml = siteRepCssServiceImpl.repTitle(strPreviewHtml, strSiteId);
				strPreviewHtml = siteRepCssServiceImpl.repWelcome(strPreviewHtml, "");
				strPreviewHtml = siteRepCssServiceImpl.repSdkbar(strPreviewHtml, "");
				strPreviewHtml = siteRepCssServiceImpl.repSiteid(strPreviewHtml, strSiteId);
				strPreviewHtml = siteRepCssServiceImpl.repJgid(strPreviewHtml, orgid);
				strPreviewHtml = siteRepCssServiceImpl.repLang(strPreviewHtml, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzLoginPrecode(strPreviewHtml);
				psTzSiteiDefnTWithBLOBs.setTzLastmantDttm(new Date());
				psTzSiteiDefnTWithBLOBs.setTzLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点登录页保存异常！";
		}

		return boolRet;

	}

	/**
	 * 注册页保存程序
	 * 
	 * @param strSaveContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	public boolean saveEnrollpage(String strSaveContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strSavedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteSaveTpl", strSaveContent);
				strSavedContent = siteRepCssServiceImpl.repWelcome(strSavedContent, "");
				strSavedContent = siteRepCssServiceImpl.repSiteid(strSavedContent, strSiteId);
				strSavedContent = siteRepCssServiceImpl.repJgid(strSavedContent, orgid);
				strSavedContent = siteRepCssServiceImpl.repLang(strSavedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzEnrollSavecode(strSavedContent);

				String strPreviewContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteReleaseTpl",
						strSaveContent);
				strPreviewContent = siteRepCssServiceImpl.repWelcome(strPreviewContent, "");
				strPreviewContent = siteRepCssServiceImpl.repSdkbar(strPreviewContent, "");
				strPreviewContent = siteRepCssServiceImpl.repSiteid(strPreviewContent, strSiteId);
				strPreviewContent = siteRepCssServiceImpl.repJgid(strPreviewContent, orgid);
				strPreviewContent = siteRepCssServiceImpl.repLang(strPreviewContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzEnrollPrecode(strPreviewContent);

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点注册页保存异常！";
		}

		return boolRet;

	}

	/**
	 * 发布站点首页
	 * 
	 * @param strReleaseContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	public boolean releasHomepage(String strReleaseContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strReleasedHtml = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteIndexReleaseTpl",
						strReleaseContent);

				String ctxPath = request.getContextPath();

				strReleasedHtml = siteRepCssServiceImpl.repContextPath(strReleasedHtml);
				strReleasedHtml = siteRepCssServiceImpl.repCss(strReleasedHtml, strSiteId);
				strReleasedHtml = siteRepCssServiceImpl.repSkinsImgPath(strReleasedHtml, strSkinId);
				strReleasedHtml = siteRepCssServiceImpl.repTitle(strReleasedHtml, strSiteId);
				strReleasedHtml = siteRepCssServiceImpl.repWelcome(strReleasedHtml, "");
				strReleasedHtml = siteRepCssServiceImpl.repSdkbar(strReleasedHtml, "");
				strReleasedHtml = siteRepCssServiceImpl.repSiteid(strReleasedHtml, strSiteId);
				strReleasedHtml = siteRepCssServiceImpl.repJgid(strReleasedHtml, orgid);
				strReleasedHtml = siteRepCssServiceImpl.repLang(strReleasedHtml, siteLang);

				String strPreviewHTML = strReleasedHtml;

				String strSelfJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsIndexRelease",
						ctxPath);
				strReleasedHtml = siteRepCssServiceImpl.repJavascriptTags(strReleasedHtml, strSelfJavascripts, orgid,
						strSiteId, "");
				strPreviewHTML = siteRepCssServiceImpl.repJavascriptTags(strPreviewHTML, strSelfJavascripts, orgid,
						strSiteId, "Y");

				psTzSiteiDefnTWithBLOBs.setTzIndexPubcode(strReleasedHtml);
				psTzSiteiDefnTWithBLOBs.setTzIndexPrecode(strPreviewHTML);
				psTzSiteiDefnTWithBLOBs.setTzSiteFbzt("Y");
				psTzSiteiDefnTWithBLOBs.setTzLastmantDttm(new Date());
				psTzSiteiDefnTWithBLOBs.setTzLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点首页发布异常！";
		}

		return boolRet;

	}

	/**
	 * 发布站点登录页
	 * 
	 * @param strReleaseContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	public boolean releasLoginpage(String strReleaseContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strReleaseHtml = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteLoginReleaseTpl",
						strReleaseContent);

				String ctxPath = request.getContextPath();

				strReleaseHtml = siteRepCssServiceImpl.repContextPath(strReleaseHtml);
				strReleaseHtml = siteRepCssServiceImpl.repCss(strReleaseHtml, strSiteId);
				strReleaseHtml = siteRepCssServiceImpl.repSkinsImgPath(strReleaseHtml, strSkinId);
				strReleaseHtml = siteRepCssServiceImpl.repTitle(strReleaseHtml, strSiteId);
				strReleaseHtml = siteRepCssServiceImpl.repWelcome(strReleaseHtml, "");
				strReleaseHtml = siteRepCssServiceImpl.repSdkbar(strReleaseHtml, "");
				strReleaseHtml = siteRepCssServiceImpl.repSiteid(strReleaseHtml, strSiteId);
				strReleaseHtml = siteRepCssServiceImpl.repJgid(strReleaseHtml, orgid);
				strReleaseHtml = siteRepCssServiceImpl.repLang(strReleaseHtml, siteLang);

				String strPreviewHTML = strReleaseHtml;

				String strSelfJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsLoginRelease",
						ctxPath);
				strReleaseHtml = siteRepCssServiceImpl.repJavascriptTags(strReleaseHtml, strSelfJavascripts, orgid,
						strSiteId, "");
				strPreviewHTML = siteRepCssServiceImpl.repJavascriptTags(strPreviewHTML, strSelfJavascripts, orgid,
						strSiteId, "Y");

				psTzSiteiDefnTWithBLOBs.setTzLonginPubcode(strReleaseHtml);
				psTzSiteiDefnTWithBLOBs.setTzLoginPrecode(strPreviewHTML);
				psTzSiteiDefnTWithBLOBs.setTzSiteFbzt("Y");
				psTzSiteiDefnTWithBLOBs.setTzLastmantDttm(new Date());
				psTzSiteiDefnTWithBLOBs.setTzLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点登录页发布异常！";
		}

		return boolRet;

	}

	/**
	 * 发布站点注册页
	 * 
	 * @param strReleaseContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	public boolean releasEnrollpage(String strReleaseContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strReleasedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteReleaseTpl",
						strReleaseContent);
				strReleasedContent = siteRepCssServiceImpl.repTitle(strReleasedContent, strSiteId);
				// strReleasedContent =
				// siteRepCssServiceImpl.repCss(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repWelcome(strReleasedContent, "");
				strReleasedContent = siteRepCssServiceImpl.repSdkbar(strReleasedContent, "");
				strReleasedContent = siteRepCssServiceImpl.repSiteid(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repJgid(strReleasedContent, orgid);
				strReleasedContent = siteRepCssServiceImpl.repLang(strReleasedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzSiteFbzt("Y");
				psTzSiteiDefnTWithBLOBs.setTzEnrollPubcode(strReleasedContent);
				psTzSiteiDefnTWithBLOBs.setTzEnrollPrecode(strReleasedContent);

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				/* 注册页面静态化 */
				boolRet = this.staticFile(strReleasedContent, orgid, strSiteId, "enroll.html", errMsg);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点注册页发布异常！";
		}

		return boolRet;

	}

	/**
	 * 生成站点静态页面
	 * 
	 * @param strReleaseContent
	 * @param orgid
	 * @param strSiteId
	 * @param filename
	 * @param errMsg
	 * @return
	 */
	private boolean staticFile(String strReleaseContent, String orgid, String strSiteId, String filename,
			String[] errMsg) {

		boolean boolRet = false;

		try {

			String parentPath = getSysHardCodeVal.getWebsiteEnrollPath() + "/" + orgid.toLowerCase() + "/"
					+ strSiteId.toLowerCase();

			byte[] fileBytes = strReleaseContent.getBytes();

			boolRet = fileManageServiceImpl.CreateFile(parentPath, filename, fileBytes);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "3";
			errMsg[1] = "静态化文件时异常！";
		}

		return boolRet;

	}

	/**
	 * 获取首页主体内容
	 * 
	 * @param strSiteId
	 * @return String
	 * @throws Exception
	 */
	private String handleHomePage(String strSiteId) throws Exception {

		String strRet = "";

		try {

			String sql = "select TZ_INDEX_SAVECODE from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			String strContent = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

			if (null != strContent && !"".equals(strContent)) {

				Pattern p = Pattern.compile("<body\\sstyle=\"background:#fff\">([\\s\\S]*)</body>");
				Matcher m = p.matcher(strContent);
				while (m.find()) {
					// System.out.println(m.group(1));
					strRet = m.group(1);
					break;
				}

				if (strRet != null && !"".equals(strRet)) {
					strRet = "<body style=\"background:#fff\">" + strRet + "</body>";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return strRet;

	}

	/**
	 * 处理要发布的登录页内容
	 * 
	 * @param strSiteId
	 * @return String
	 * @throws Exception
	 */
	private String handleLoginPage(String strSiteId) throws Exception {

		String strRet = "";

		try {

			String sql = "select TZ_LONGIN_SAVECODE from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			String strContent = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

			if (null != strContent && !"".equals(strContent)) {

				Pattern p = Pattern.compile("<body\\sstyle=\"background:#fff\">([\\s\\S]*)</body>");
				Matcher m = p.matcher(strContent);
				while (m.find()) {
					// System.out.println(m.group(1));
					strRet = m.group(1);
					break;
				}

				if (strRet != null && !"".equals(strRet)) {
					strRet = "<body style=\"background:#fff\">" + strRet + "</body>";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return strRet;

	}

	/**
	 * 处理要发布的注册页内容
	 * 
	 * @param strSiteId
	 * @return String
	 * @throws Exception
	 */
	private String handleEnrollPage(String strSiteId) throws Exception {

		String strRet = "";

		try {

			// String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where
			// TZ_SITEI_ID=?";
			// String strOrgId = sqlQuery.queryForObject(sql, new Object[] {
			// strSiteId }, "String");

			String strContent = registeServiceImpl.handleEnrollPage(strSiteId);

			strContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzSetRegisterPage", strContent);

			if (null != strContent && !"".equals(strContent)) {

				Pattern p = Pattern.compile("<body\\sstyle=\"background:#fff\">([\\s\\S]*)</body>");
				Matcher m = p.matcher(strContent);
				while (m.find()) {
					// System.out.println(m.group(1));
					strRet = m.group(1);
					break;
				}

				if (strRet != null && !"".equals(strRet)) {
					strRet = "<body style=\"background:#fff\">" + strRet + "</body>";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return strRet;

	}

}
