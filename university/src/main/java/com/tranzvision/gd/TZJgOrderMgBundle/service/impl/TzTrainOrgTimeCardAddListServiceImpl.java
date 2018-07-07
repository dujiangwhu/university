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
@Service("com.tranzvision.gd.TZJgOrderMgBundle.service.impl.TzTrainOrgTimeCardAddListServiceImpl")
public class TzTrainOrgTimeCardAddListServiceImpl extends FrameworkImpl {

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
			String[] resultFldArray = { "TZ_JG_ID", "TZ_JG_NAME", "TZ_ORGAN_CONTACT","TZ_ORGAN_CONTACTPH","TZ_TIMECARD_ADD","TZ_TIMECARD_AFTER","ROW_LASTMANT_DTTM","REALNAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("orgId", rowList[0]);
					mapList.put("orgName", rowList[1]);
					mapList.put("orgLxrName", rowList[2]);
					mapList.put("orgLxrPhone", rowList[3]);
					mapList.put("orgTimeCardAdd", rowList[4]);
					mapList.put("orgTimeCardHaveAfter", rowList[5]);
					mapList.put("orgTimeCardAddTime", rowList[6]);
					mapList.put("orgTimeCardAddOprName", rowList[7]);

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

	/**
	 * 修改消息集合信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String errorMsg = "";
		String comma = "";
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

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				if ("ADDTIMECARD".equals(typeFlag)) {
					// 机构编号;
					String tzJgId = infoData.get("orgId").toString().toUpperCase();
					TzFilterIllegalCharacter tzFilterIllegalCharacter = new TzFilterIllegalCharacter();
					tzJgId = tzFilterIllegalCharacter.filterAllIllegalCharacter(tzJgId);
					int orgTimeCardNumber = Integer.parseInt(String.valueOf(infoData.get("addTimeCardNumber")));
					int addTimeCardMoney = Integer.parseInt(String.valueOf(infoData.get("addTimeCardMoney")));

					String sql = "select TZ_JG_TIMECARD_YY,TZ_JG_TIMECARD_SY from PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
					Map<String, Object> orgInfo = sqlQuery.queryForMap(sql, new Object[] { tzJgId });
					if (orgInfo!=null) {
						/*变更前课时卡*/
						int orgTimeCardHaveBefore = orgInfo.get("TZ_JG_TIMECARD_YY") ==null?0:Integer.parseInt(String.valueOf(orgInfo.get("TZ_JG_TIMECARD_YY")));
						
						// 机构名称;
						PsTzJgBaseTWithBLOBs psTzJgBaseT = new PsTzJgBaseTWithBLOBs();
						psTzJgBaseT.setTzJgId(tzJgId);
						psTzJgBaseT.setRowLastmantDttm(dateNow);
						psTzJgBaseT.setRowLastmantOprid(oprid);
						/*更新充值后的数据*/
						int orgTimeCardHaveAfter = orgTimeCardHaveBefore + orgTimeCardNumber;
						psTzJgBaseT.setTzJgTimecardYy(orgTimeCardHaveAfter);
						psTzJgBaseTMapper.updateByPrimaryKeySelective(psTzJgBaseT);
						
						//生成订单ID
						String strOrderID =	String.valueOf(getSeqNum.getSeqNum("PX_JG_KS_ORDER_T", "TZ_ORDER_ID"));
						
						//将充值记录写入充值记录表，并记录机构课时卡变更记录表
						PxJgKsOrderT pxJgKsOrderT = new PxJgKsOrderT();
						pxJgKsOrderT.setTzJgId(tzJgId);
						pxJgKsOrderT.setTzOrderId(strOrderID);
						pxJgKsOrderT.setTzTimecardBefore(orgTimeCardHaveBefore);
						pxJgKsOrderT.setTzTimecardAfter(orgTimeCardHaveAfter);
						pxJgKsOrderT.setTzTimecardAdd(orgTimeCardNumber);
						pxJgKsOrderT.setTzTimecardMoney(addTimeCardMoney);
						pxJgKsOrderT.setRowLastmantOprid(oprid);
						pxJgKsOrderT.setRowLastmantDttm(dateNow);
						PxJgKsOrderTMapper.insert(pxJgKsOrderT);
						
						String strLogInsID =	String.valueOf(getSeqNum.getSeqNum("PX_JG_KS_LOG_T", "TZ_LOG_INS_ID"));
						PxJgKsLogT pxJgKsLogT = new PxJgKsLogT();
						pxJgKsLogT.setTzJgId(tzJgId);
						pxJgKsLogT.setTzLogInsId(strLogInsID);
						pxJgKsLogT.setTzKsModifyType("A");
						pxJgKsLogT.setTzTimecardBefore(orgTimeCardHaveBefore);
						pxJgKsLogT.setTzTimecardAfter(orgTimeCardHaveAfter);
						pxJgKsLogT.setTzTimecardModify(addTimeCardMoney);
						pxJgKsLogT.setRowLastmantOprid(oprid);
						pxJgKsLogT.setRowLastmantDttm(dateNow);
						pxJgKsLogTMapper.insert(pxJgKsLogT);
						//将机构ID返回
						Map<String, Object> mapRet = new HashMap<String, Object>();
						mapRet.put("orgid", tzJgId);
						
						strRet = jacksonUtil.Map2json(mapRet);

					} else {
						errorMsg += comma + tzJgId;
						comma = ",";
					}
					
				}
				
			}
			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "机构：" + errorMsg + "，不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}