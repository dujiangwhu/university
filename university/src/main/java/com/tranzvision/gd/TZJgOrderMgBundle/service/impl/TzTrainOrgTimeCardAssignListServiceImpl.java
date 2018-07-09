/**
 * 
 */
package com.tranzvision.gd.TZJgOrderMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseTWithBLOBs;
import com.tranzvision.gd.TZPXBundle.dao.PxJgKsLogTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxJgKsOrderTMapper;
import com.tranzvision.gd.TZPXBundle.model.PxJgKsLogT;
import com.tranzvision.gd.TZPXBundle.model.PxJgKsOrderT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZJgOrderMgBundle.service.impl.TzTrainOrgTimeCardAssignListServiceImpl")
public class TzTrainOrgTimeCardAssignListServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;
	
	@Autowired
	private PxJgKsLogTMapper pxJgKsLogTMapper;
	
	@Autowired
	private PxJgKsOrderTMapper PxJgKsOrderTMapper;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID", "TZ_JG_NAME", "TZ_REALNAME","TZ_MOBILE","TZ_CHANGE","TZ_AFTER_CHANGE","TZ_CHANGE_TIME","OPRNAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("orgId", rowList[0]);
					mapList.put("orgName", rowList[1]);
					mapList.put("stuName", rowList[2]);
					mapList.put("stuPhone", rowList[3]);
					mapList.put("stuTimeCardAssign", rowList[4]);
					mapList.put("stuTimeCardAssignAfter", rowList[5]);
					mapList.put("stuTimeCardAssignTime", rowList[6]);
					mapList.put("stuTimeCardAssignOprName", rowList[7]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}
}