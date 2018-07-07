package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Console;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * 
 * @author tmt
 * 用户申请管理 注册用户管理
 * 保存颜色类别
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.ApplicationUserSaveColorServiceImpl")
public class ApplicationUserSaveColorServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strComContent;
		}

		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				Map<String, Object> dataMap = jacksonUtil.getMap("data");
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				// 保存数据;
				String sortID = (String) dataMap.get("sortID");
				String OPRID = (String) dataMap.get("OPRID");
				String classID = (String) dataMap.get("classID");
				System.out.println(sortID+" "+OPRID+" "+classID);
				String updateSQL = "UPDATE PS_TZ_FORM_WRK_T SET TZ_COLOR_SORT_ID=?,ROW_LASTMANT_DTTM=now(),ROW_LASTMANT_OPRID=? WHERE TZ_CLASS_ID=? AND OPRID = ?";
				jdbcTemplate.update(updateSQL, new Object[] { sortID, oprid, classID, OPRID });
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strComContent;
	}
}
