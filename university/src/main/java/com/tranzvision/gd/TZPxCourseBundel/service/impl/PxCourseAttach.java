package com.tranzvision.gd.TZPxCourseBundel.service.impl;

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
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqComzcTbl;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqPagzcTbl;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseAnnexMapper;
import com.tranzvision.gd.TZPXBundle.model.PxCourseAnnex;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang
 * 2015-10-9
 * 功能组件注册管理相关类
 * PS:TZ_GD_COMREGMG_PKG:TZ_GD_COMREG_CLS
 *
 */
@Service("com.tranzvision.gd.TZPxCourseBundel.service.impl.PxCourseAttach")
public class PxCourseAttach extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PxCourseAnnexMapper pxCourseAnnexMapper;

	/* 新增组件注册信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				System.out.println(strForm);
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String tzCourseId = jacksonUtil.getString("tzCourseId");
				String filename = jacksonUtil.getString("filename");
				String sysFileName = jacksonUtil.getString("sysFileName");
				String accessPath = jacksonUtil.getString("accessPath");
				// 信息内容;
				
				PxCourseAnnex pxCourseAnnex=new PxCourseAnnex();
				pxCourseAnnex.setTzCourseId(tzCourseId);
				
				//需要修改
				pxCourseAnnex.setTzPkskXh((int)new Date().getTime());
				pxCourseAnnex.setTzAttachfileName(filename);
					int i = pxCourseAnnexMapper.insert(pxCourseAnnex);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
					}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	// 获取页面注册信息列表 
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String strComID = jacksonUtil.getString("tzCourseId");
			if (strComID != null && !"".equals(strComID)) {

				// 页面ID，页面名称，是否默认首页;
				String tzCourseId = "", tzAttachfileName = "";
				int tzPkskXh = 0;
				// 序号;
				int numOrder = 0;
				// 页面注册信息列表sql;
				String sqlPageList = "";
				String totalSQL = "";
				//查询组件下的页面列表
				Object[] obj = null;
				if (numLimit == 0) {
					sqlPageList = "SELECT TZ_COURSE_ID,TZ_PKSK_XH,TZ_ATTACHFILE_NAME FROM PX_COURSE_ANNEX_T WHERE TZ_COURSE_ID=? ORDER BY TZ_PKSK_XH";
					obj = new Object[] { strComID };
				} else {
					sqlPageList = "SELECT TZ_COURSE_ID,TZ_PKSK_XH,TZ_ATTACHFILE_NAME FROM PX_COURSE_ANNEX_T WHERE TZ_COURSE_ID=? ORDER BY TZ_PKSK_XH limit ?,?";
					obj = new Object[] { strComID, numStart, numLimit };
				}
				
				int total = 0;
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlPageList, obj);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						tzCourseId = (String) list.get(i).get("TZ_COURSE_ID").toString();
						tzPkskXh = (Integer)list.get(i).get("TZ_PKSK_XH");
						tzAttachfileName = (String) list.get(i).get("TZ_ATTACHFILE_NAME");
						
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("tzCourseId", tzCourseId);
						mapList.put("tzPkskXh", tzPkskXh);
						mapList.put("tzAttachfileName", tzAttachfileName);
						listData.add(mapList);
					}
					
					totalSQL = "SELECT COUNT(1) FROM PX_COURSE_ANNEX_T WHERE TZ_COURSE_ID=? ";
					total = jdbcTemplate.queryForObject(totalSQL,new Object[] { strComID },"Integer");
					mapRet.replace("total", total);
					mapRet.replace("root", listData);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件页面信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}

}
