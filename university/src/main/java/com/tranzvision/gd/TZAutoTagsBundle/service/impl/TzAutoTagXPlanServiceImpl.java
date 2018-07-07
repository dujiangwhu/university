package com.tranzvision.gd.TZAutoTagsBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutoTagsBundle.service.TzAutoTagsInterface;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


/**
 * 【X计划】
X计划（项目类别为X计划）的考生面试结果为“通过”，可以在之后3-5【本科3-5年  博士和硕士研究生2-4】年内进行MBA项目的报名，若是考生在相应年度时间内登录系统进行了报名表，
需要系统自动根据考生身份证号校验，并给考生形成一个“X计划”的标签。
 * @author zhanglang
 *
 */
@Service("com.tranzvision.gd.TZAutoTagsBundle.service.impl.TzAutoTagXPlanServiceImpl")
public class TzAutoTagXPlanServiceImpl implements TzAutoTagsInterface {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private TZGDObject tzGDObject;
	
	
	@Override
	public boolean execute(String classId, Long appInsId, String oprId) {
		boolean bool = false;
		try{
			//“X计划”项目类型
			String xplanPrjType = getHardCodePoint.getHardCodePointVal("TZ_XPLAN_PRJ_TYPE");
			
			//当前报考班级入学年份
			Integer rxYear = sqlQuery.queryForObject("select year(TZ_RX_DT) from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?", 
					new Object[]{ classId }, "Integer");
			//当前考生省份证号码
			String nationalId = sqlQuery.queryForObject("select NATIONAL_ID from PS_TZ_REG_USER_T where OPRID=?", 
					new Object[]{ oprId }, "String");
			if(rxYear != null 
					&& nationalId != null && !"".equals(nationalId)){
				
				String sql = tzGDObject.getSQLText("SQL.TZAutoTagsBundle.TzAutoTagsXPlanCondition");
				List<Map<String,Object>> xPlanList = sqlQuery.queryForList(sql, new Object[]{ xplanPrjType, nationalId });
				if(xPlanList != null && xPlanList.size() > 0){
					
					//“X计划”在读阶段报名表信息编号
					String xPlanZdjd = getHardCodePoint.getHardCodePointVal("TZ_XPLAN_ZDJD_ITEMID");
					
					for(Map<String,Object> xPlanMap: xPlanList){
						String xpAppInsId = xPlanMap.get("TZ_APP_INS_ID").toString();
						String strXpRxYear = xPlanMap.get("TZ_RX_YEAR") == null ? "" : xPlanMap.get("TZ_RX_YEAR").toString();
						
						if(!"".equals(strXpRxYear)){
							int xpRxYear = Integer.valueOf(strXpRxYear);
							
							String xpZdjd = "select TZ_XXXKXZ_MC from PS_TZ_APP_DHCC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=? and TZ_IS_CHECKED='Y'";
							String zdjdMc = sqlQuery.queryForObject(xpZdjd, new Object[]{ xpAppInsId, xPlanZdjd }, "String");
							//在读阶段：B-本科， M-硕士研究生、D-博士研究生
							if(zdjdMc != null && !"".equals(zdjdMc)){
								if("B".equals(zdjdMc)){
									if((rxYear-xpRxYear)>=3 && (rxYear-xpRxYear) <= 5){
										bool = true;
									}
								}else if("M".equals(zdjdMc) || "D".equals(zdjdMc)){
									if((rxYear-xpRxYear)>=2 && (rxYear-xpRxYear) <= 4){
										bool = true;
									}
								}
							}
						}
						
						if(bool) break;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bool;
	}
}
