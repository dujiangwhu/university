package com.tranzvision.gd.TZPxCourseBundel.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPXBundle.dao.PxCourseTMapper;
import com.tranzvision.gd.TZPXBundle.model.PxCourse;
import com.tranzvision.gd.TZPXBundle.model.PxCourseT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 功能说明：功能组件注册管理相关类
 * @author tang
 * 2015-10-9
 * ps类：TZ_GD_COMREGMG_PKG:TZ_GD_COMREGMG_CLS
 */
@Service("com.tranzvision.gd.TZPxCourseBundel.service.impl.PxCourseInfo")
public class PxCourseInfo extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PxCourseTMapper pxCourseMapper;
	@Autowired
	private FliterForm fliterForm;	

	/* 删除组件注册信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;
				String tzCourseId = jacksonUtil.getString("tzCourseId");
				String tzPkskXh = jacksonUtil.getString("tzPkskXh");
				String comPageSql = "DELETE FROM PX_COURSE_ANNEX_T WHERE TZ_COURSE_ID=? AND TZ_PKSK_XH=?";
				jdbcTemplate.update(comPageSql,new Object[]{tzCourseId,tzPkskXh});		
				
				//删除附件？地址？
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	
	/* 修改组件注册信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
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
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 组件编号;
					String tzCourseId = (String) infoData.get("tzCourseId");
					String tzCourseTypeId = (String) infoData.get("tzCourseTypeId");
					//String tzCourseNumber = (String) infoData.get("tzCourseNumber");
					String tzCourseName = (String) infoData.get("tzCourseName");
					String tzCourseDesc = (String) infoData.get("tzCourseDesc");
					
					// 组件名称;
					
					PxCourseT pxCourse=pxCourseMapper.selectByPrimaryKey(tzCourseId);
					// 是否已经存在;
					if (pxCourse==null) {
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + tzCourseId + "的信息不存在。";
						return strRet;
					}
					pxCourse.setTzCourseName(tzCourseName);
					pxCourse.setTzCourseTypeId(tzCourseTypeId);
					pxCourse.setTzCourseDesc(tzCourseDesc);
					pxCourse.setRowLastmantDttm(new Date());
					int i = pxCourseMapper.updateByPrimaryKey(pxCourse);
					
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}
				}

				/*if ("PAGE".equals(strFlag)) {
					strRet = this.tzUpdatePageInfo(infoData, errMsg);
				}*/

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

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
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("COM".equals(strFlag)) {
					// 组件编号;
					String tzCourseId = (String) infoData.get("tzCourseId");
					String tzCourseTypeId = (String) infoData.get("tzCourseTypeId");
					//String tzCourseNumber = (String) infoData.get("tzCourseNumber");
					String tzCourseName = (String) infoData.get("tzCourseName");
					String tzCourseDesc = (String) infoData.get("tzCourseDesc");
					
					// 组件名称;
					
					PxCourseT pxCourse=pxCourseMapper.selectByPrimaryKey(tzCourseId);
					// 是否已经存在;
					if (pxCourse!=null) {
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + tzCourseId + "的信息已经注册，请修改组件ID。";
						return strRet;
					}
					pxCourse=new PxCourseT();
					pxCourse.setTzCourseId(tzCourseId);
					pxCourse.setTzCourseName(tzCourseName);
					pxCourse.setTzCourseTypeId(tzCourseTypeId);
					pxCourse.setTzCourseDesc(tzCourseDesc);
					pxCourse.setRowLastmantDttm(new Date());
					int i = pxCourseMapper.insert(pxCourse);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
					}
				}

				if ("PAGE".equals(strFlag)) {
					//strRet = this.tzUpdatePageInfo(infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}