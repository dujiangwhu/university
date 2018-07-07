package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * @author ZBB
 * @since 2017-08-22
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.ApplicationPrjServiceImpl")
@SuppressWarnings("unchecked")
public class ApplicationPrjServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			String[][] orderByArr = new String[][] {{"TZ_PRJ_TYPE_ID","ASC"}};
			
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_PRJ_TYPE_ID", "TZ_PRJ_TYPE_NAME","TZ_JG_ID"};
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);
			
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("prjID", rowList[0]);
					mapList.put("prjName", rowList[1]);
					mapList.put("jgId", rowList[2]);
					listData.add(mapList);
				}
				
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}

}
