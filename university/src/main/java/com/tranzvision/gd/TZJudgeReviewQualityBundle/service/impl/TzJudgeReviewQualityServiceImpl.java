package com.tranzvision.gd.TZJudgeReviewQualityBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Image;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 材料评委评审质量管理
 * 
 * @author LuYan 2017-5-22
 *
 */

@Service("com.tranzvision.gd.TZJudgeReviewQualityBundle.service.impl.TzJudgeReviewQualityServiceImpl")
public class TzJudgeReviewQualityServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private HttpServletRequest request;
	

	/*材料评委列表信息*/
	@Override
	@SuppressWarnings({ "unchecked", "unused" })
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		try {
			
			/*为了使用平均面试通过率进行排序*/
			/*
			//评审意见成绩项ID
			String psyjCjxId = getHardCodePoint.getHardCodePointVal("TZ_CLPS_PSYJ_CJX_ID");
			//计算为材料评议结果的评审意见值
			String psyjValue = getHardCodePoint.getHardCodePointVal("TZ_CLPS_PSYJ_VALUE");
			String[] psyjValueArr = psyjValue.trim().split(",");
			String psyjDesc = "";
			for(String psyj:psyjValueArr) {
				if(psyj!=null && !"".equals(psyj)) {
					if(!"".equals(psyjDesc)) {
						psyjDesc += "," + "'" + psyj + "'";
					} else {
						psyjDesc = "'" + psyj + "'";
					}
				}
			}
			*/
			
			
			// 排序字段
			String[][] orderByArr = new String[][] {};
			
			String sort = request.getParameter("sort");
			if(sort!=null && !"".equals(sort)) {
				sort = "{\"sort\":"+sort+"}";
				jacksonUtil.json2Map(sort);
				List<Map<String, String>> sortList = (List<Map<String,String>>) jacksonUtil.getList("sort");
				List<String[]> orderList = new ArrayList<String[]>();
				for(Map<String, String> sortMap : sortList) {
					String columnField = sortMap.get("property");
					String sortMethod = sortMap.get("direction");
					
					if("judgeDlzhId".equals(columnField)) {
						orderList.add(new String[]{"TZ_DLZH_ID",sortMethod});
					}
					
					if("judgeName".equals(columnField)) {
						orderList.add(new String[]{"TZ_REALNAME",sortMethod});
					}
					
					if("interviewPass".equals(columnField)) {
						orderList.add(new String[]{"TZ_AVG_MSPS",sortMethod});
					}
				}
				
				orderByArr = new String[orderList.size()][2];
				for(int i=0;i<orderList.size();i++){
					orderByArr[i] = orderList.get(i);
				}
			}
			

			// json数据要的结果字段
			String[] resultFldArray = { "OPRID", "TZ_DLZH_ID", "TZ_REALNAME","TZ_AVG_MSPS"};

			// 可配置搜索通用函数
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errMsg);
			
			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					
					String judgeOprid = rowList[0];
					
					//String sql = "";
					
					//平均面试通过率
					//String interviewPass = "0";  
					/*
					 * 面试通过率=推荐人员面试情况/材料评议结果
					 * 材料评议结果：评委每个评审批次强烈推荐和推荐进入面试的人数M
					 * 推荐人员面试情况：M人中 面试后预录取人数N
					 */
					/*
					Integer interviewPassAvg = 0;
					Integer tjryMsNum = 0;
					Integer clpsNum = 0;
					sql = "SELECT COUNT(1)";
					sql += " FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CJX_TBL B,PS_TZ_CLASS_INF_T C,PS_TZ_CLS_BATCH_T D";
					sql += " WHERE A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_CLASS_ID=D.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=D.TZ_BATCH_ID ";
					sql += " AND A.TZ_SCORE_INS_ID=B.TZ_SCORE_INS_ID AND B.TZ_SCORE_ITEM_ID=? AND B.TZ_CJX_XLK_XXBH IN (" + psyjDesc + ") AND A.TZ_PWEI_OPRID=?";
					
					String clpsjg = sqlQuery.queryForObject(sql,  new Object[]{psyjCjxId,judgeOprid},"String");
					if(!"".equals(clpsjg) && clpsjg!=null && !"0".equals(clpsjg)) {
						clpsNum = Integer.valueOf(clpsjg);
	
						sql = "SELECT COUNT(1)";
						sql += " FROM PS_TZ_MSJG_DR_T A,PS_TZ_FORM_WRK_T B,PS_TZ_CP_PW_KS_TBL C,PS_TZ_CLASS_INF_T D,PS_TZ_CLS_BATCH_T F";
						sql += " WHERE C.TZ_CLASS_ID=D.TZ_CLASS_ID AND C.TZ_CLASS_ID=F.TZ_CLASS_ID AND C.TZ_APPLY_PC_ID=F.TZ_BATCH_ID ";
						sql += " AND A.TZ_MSH_ID=B.TZ_MSH_ID AND B.TZ_APP_INS_ID=C.TZ_APP_INS_ID AND C.TZ_PWEI_OPRID=? AND A.TZ_MSJG IN('预录取','有条件预录取')";
						
						String tjryMs = sqlQuery.queryForObject(sql,  new Object[]{judgeOprid},"String");
						if(!"".equals(tjryMs) && tjryMs!=null && !"0".equals(tjryMs)) {
							tjryMsNum = Integer.valueOf(tjryMs);
						} 
						
						//interviewPass = String.valueOf((int)((Float.valueOf(tjryMsNum)/clpsNum)*100));
						interviewPassAvg = (int)((Float.valueOf(tjryMsNum)/clpsNum)*100);
						if(interviewPassAvg!=0) {
							interviewPass = interviewPassAvg+"%";
						}
					}
					*/
					
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("judgeOprid", rowList[0]);
					mapList.put("judgeDlzhId", rowList[1]);
					mapList.put("judgeName", rowList[2]);
					mapList.put("interviewPass", rowList[3]);
					listData.add(mapList);

				}
				
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		
		return strRet;
	}
}
