/**
 * 
 */
package com.tranzvision.gd.TZPxTeacherBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxScoreLogMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeaToCrashMapper;
import com.tranzvision.gd.TZPXBundle.dao.PxTeacherMapper;
import com.tranzvision.gd.TZPXBundle.model.PxScoreLog;
import com.tranzvision.gd.TZPXBundle.model.PxTeaToCrash;
import com.tranzvision.gd.TZPXBundle.model.PxTeacher;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZPxTeacherBundle.service.impl.PxCrashMgServiceImpl")
public class PxCrashMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PxTeacherMapper pxTeacherMapper;
	
	
	@Autowired
	private PxTeaToCrashMapper pxTeaToCrashMapper;
	
	@Autowired
	private PxScoreLogMapper pxScoreLogMapper;

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
			String[] resultFldArray = { "TEA_OPRID", "SCORE", "OPERATE_TIME","OPERATE_OPRID","TZ_REALNAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("teaOprid", rowList[0]);
					mapList.put("score", rowList[1]);
					mapList.put("operateTime", rowList[2]);
					mapList.put("operateOprid", rowList[3]);
					mapList.put("tzRealName", rowList[4]);
		
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

	/*
	 修改组件注册信息 
	public String tzUpdate(String[] actData, String[] errMsg) {
		System.out.println("update");
		String strRet = "{}";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				//Map<String, Object> infoData  = jacksonUtil.getMap("update");
				String oprid=jacksonUtil.getString("oprid");
				if(oprid==null){
					errMsg[0] = "1";
					errMsg[1] = "用户不存在！";
				}else{
					PxTeacher pxTeacher=pxTeacherMapper.selectByPrimaryKey(oprid);
					if(pxTeacher==null){
						errMsg[0] = "1";
						errMsg[1] = "用户不存在！";
					}else{
						PxTeaToCrash pxTeaToCrash=new PxTeaToCrash();
						pxTeaToCrash.setTeaOprid(pxTeacher.getOprid());
						pxTeaToCrash.setOperateTime(new Date());
						pxTeaToCrash.setScore(pxTeacher.getScore());
						pxTeaToCrash.setOperateOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
						System.out.println("oprid"+tzLoginServiceImpl.getLoginedManagerOprid(request));
						pxTeaToCrashMapper.insert(pxTeaToCrash);
						
						PxScoreLog pxScoreLog=new PxScoreLog();
						pxScoreLog.setTeaOprid(pxTeacher.getOprid());
						pxScoreLog.setChangeScore(0-pxTeacher.getScore());
						pxScoreLog.setChangeTime(new Date());
						pxScoreLog.setChangeType("提现");
						
						
						pxScoreLogMapper.insert(pxScoreLog);
						
						pxTeacher.setScore(0);
						pxTeacherMapper.updateByPrimaryKey(pxTeacher);
					}					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}*/

}