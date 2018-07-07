package com.tranzvision.gd.TZStudentBbTjBundle.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.SqlQuery;

import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;

/**
 * 
 * 考生报表年龄统计;
 *
 */
@Service("com.tranzvision.gd.TZStudentBbTjBundle.service.impl.TzGdBbTjServiceImpl")
public class TzGdBbTjServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		
		// 保存的路径
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String downloadPath = getSysHardCodeVal.getDownloadPath();
		String expDirPath = downloadPath + "/" + orgid + "/" + "EXPORTBBTJEXCEL";
		String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
		
		//excel的文件名;
		Date dt = new Date();
		SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		String sDttm = datetimeFormate.format(dt);
		String strUseFileName = sDttm + "_" + (new Random().nextInt(99999 - 10 + 1) + 10000) + "." + "xlsx";
		
		this.createTemporaryTable(strParams);
		
		//导出年龄分布
		//文件头
		List<String[]> ageDataCellKeys = new ArrayList<String[]>();
		ageDataCellKeys.add(new String[] { "name", "年龄段" });
		ageDataCellKeys.add(new String[] { "num", "数量" });
		ageDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> ageMap = this.getAgeTjList();
		List<Map<String, Object>> ageDataList =  (List<Map<String, Object>>) ageMap.get("root");
		
		//导出毕业院校
		List<String[]> schoolDataCellKeys = new ArrayList<String[]>();
		schoolDataCellKeys.add(new String[] { "name", "学校分类" });
		schoolDataCellKeys.add(new String[] { "num", "数量" });
		schoolDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> schoolMap = this.getSchoolTjList();
		List<Map<String, Object>> schoolDataList =  (List<Map<String, Object>>) schoolMap.get("root");
		
		//导出工龄
		List<String[]> workAgeDataCellKeys = new ArrayList<String[]>();
		workAgeDataCellKeys.add(new String[] { "name", "工龄范围" });
		workAgeDataCellKeys.add(new String[] { "num", "数量" });
		workAgeDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> workAgeMap = this.getWorkAgeTjList();
		List<Map<String, Object>> workAgeDataList =  (List<Map<String, Object>>) workAgeMap.get("root");
		
		//导出年薪
		List<String[]> workSalaryDataCellKeys = new ArrayList<String[]>();
		workSalaryDataCellKeys.add(new String[] { "name", "年薪范围" });
		workSalaryDataCellKeys.add(new String[] { "num", "数量" });
		workSalaryDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> workSalaryMap = this.getWorkSalaryList();
		List<Map<String, Object>> workSalaryDataList =  (List<Map<String, Object>>) workSalaryMap.get("root");
		
		//导出专业类别;
		List<String[]> majorTypeDataCellKeys = new ArrayList<String[]>();
		majorTypeDataCellKeys.add(new String[] { "name", "专业类别" });
		majorTypeDataCellKeys.add(new String[] { "num", "数量" });
		majorTypeDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> majorTypeMap = this.getMajorTypeList();
		List<Map<String, Object>> majorTypeDataList =  (List<Map<String, Object>>) majorTypeMap.get("root");
				
		//导出教育背景
		List<String[]> educationDataCellKeys = new ArrayList<String[]>();
		educationDataCellKeys.add(new String[] { "name", "教育背景" });
		educationDataCellKeys.add(new String[] { "num", "数量" });
		educationDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> educationMap = this.getEducationList();
		List<Map<String, Object>> educationDataList =  (List<Map<String, Object>>) educationMap.get("root");
		
		
		//导出入学前单位性质
		List<String[]> insDataCellKeys = new ArrayList<String[]>();
		insDataCellKeys.add(new String[] { "name", "入学前单位性质" });
		insDataCellKeys.add(new String[] { "num", "数量" });
		insDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> insMap = this.getInsList();
		List<Map<String, Object>> insDataList =  (List<Map<String, Object>>) insMap.get("root");
		
		//导出入学前行业背景
		List<String[]> indDataCellKeys = new ArrayList<String[]>();
		indDataCellKeys.add(new String[] { "name", "入学前行业背景" });
		indDataCellKeys.add(new String[] { "num", "数量" });
		indDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> indMap = this.getIndList();
		List<Map<String, Object>> indDataList =  (List<Map<String, Object>>) indMap.get("root");
		
		//导出管理经验
		List<String[]> experienceDataCellKeys = new ArrayList<String[]>();
		experienceDataCellKeys.add(new String[] { "name", "管理经验" });
		experienceDataCellKeys.add(new String[] { "num", "平均值" });
		//数据
		HashMap<String, Object> experienceMap = this.getExperienceList();
		List<Map<String, Object>> experienceDataList =  (List<Map<String, Object>>) experienceMap.get("root");
		
		//导出性别;
		List<String[]> sexDataCellKeys = new ArrayList<String[]>();
		sexDataCellKeys.add(new String[] { "name", "入学前行业背景" });
		sexDataCellKeys.add(new String[] { "num", "数量" });
		sexDataCellKeys.add(new String[] { "scale", "百分比" });
		//数据
		HashMap<String, Object> sexMap = this.getSexList();
		List<Map<String, Object>> sexDataList =  (List<Map<String, Object>>) sexMap.get("root");
		

		jdbcTemplate.execute("drop table TMP_KS_BBTJ_TMP");

		ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
		
		excelHandle.createSheetByName("年龄分布", ageDataCellKeys, ageDataList);
		excelHandle.createSheetByName("毕业院校", schoolDataCellKeys, schoolDataList);
		excelHandle.createSheetByName("工龄范围", workAgeDataCellKeys, workAgeDataList);
		excelHandle.createSheetByName("年薪范围", workSalaryDataCellKeys, workSalaryDataList);
		excelHandle.createSheetByName("专业类别", majorTypeDataCellKeys, majorTypeDataList);
		excelHandle.createSheetByName("教育背景", educationDataCellKeys, educationDataList);
		excelHandle.createSheetByName("入学前单位性质", insDataCellKeys, insDataList);
		excelHandle.createSheetByName("入学前行业背景", indDataCellKeys, indDataList);
		excelHandle.createSheetByName("管理经验", experienceDataCellKeys, experienceDataList);
		excelHandle.createSheetByName("性别", sexDataCellKeys, sexDataList);
		
		boolean rst =  excelHandle.writeToExcel(strUseFileName);
		
		String urlExcel = "";
		if (rst) {
			urlExcel = excelHandle.getExportExcelPath();
		}else{
			urlExcel = "";
		}
		
		mapRet.put("url", urlExcel);
		return jacksonUtil.Map2json(mapRet);
	}

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		this.createTemporaryTable(strParams);
		// 年龄   
		HashMap<String, Object> ageMap = this.getAgeTjList();
	
		// 毕业院校
		HashMap<String, Object> schoolMap = this.getSchoolTjList();

		// 工龄
		HashMap<String, Object> workAgeMap = this.getWorkAgeTjList();

		// 年薪
		HashMap<String, Object> workSalaryMap = this.getWorkSalaryList();

		// 专业类别;
		HashMap<String, Object> majorTypeMap = this.getMajorTypeList();

		// 教育背景
		HashMap<String, Object> educationMap = this.getEducationList();

		// 入学前单位性质
		HashMap<String, Object> insMap = this.getInsList();

		// 入学前行业背景
		HashMap<String, Object> indMap = this.getIndList();

		// 管理经验
		HashMap<String, Object> experienceMap = this.getExperienceList();

		// 性别;
		HashMap<String, Object> sexMap = this.getSexList();

		
		jdbcTemplate.execute("drop table TMP_KS_BBTJ_TMP");

		mapRet.put("age", ageMap);
		mapRet.put("school", schoolMap);
		mapRet.put("workAge", workAgeMap);
		mapRet.put("workSalary", workSalaryMap);
		mapRet.put("majorType", majorTypeMap);
		mapRet.put("education", educationMap);
		mapRet.put("ins", insMap);
		mapRet.put("ind", indMap);
		mapRet.put("experience", experienceMap);
		mapRet.put("sex", sexMap);

		return jacksonUtil.Map2json(mapRet);

	}

	@SuppressWarnings("unchecked")
	private void createTemporaryTable(String strParams) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		String jgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		StringBuilder sql = new StringBuilder();
		sql.append(" CREATE TEMPORARY TABLE IF NOT EXISTS TMP_KS_BBTJ_TMP ");
		sql.append(" SELECT A.TZ_APP_INS_ID ");
		sql.append(" FROM PS_TZ_APP_INS_T A, PS_TZ_FORM_WRK_T B,PS_TZ_CLASS_INF_T C ");
		sql.append(" WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_CLASS_ID = C.TZ_CLASS_ID ");
		sql.append(" AND C.TZ_JG_ID='" + jgId + "' ");

		String yearsWhere = "", bmbStatusWhere = "", msZgWhere = "", msJgWhere = "", lqZtWhere = "",colorTypeWhere = "";
		if (jacksonUtil.containsKey("formDate")) {
			Map<String, Object> formDate = jacksonUtil.getMap("formDate");
			// 年度;
			List<String> years = (List<String>) formDate.get("years");
			if (years != null && years.size() > 0) {
				for (int i = 0; i < years.size(); i++) {
					yearsWhere = yearsWhere + ",'" + years.get(i) + "'";
				}
				yearsWhere = yearsWhere.substring(1);

				sql.append(" AND YEAR(C.TZ_RX_DT) IN (" + yearsWhere + ")");
			}

			// 报名表状态;
			List<String> bmbStatus = (List<String>) formDate.get("bmbStatus");
			if (bmbStatus != null && bmbStatus.size() > 0) {
				for (int i = 0; i < bmbStatus.size(); i++) {
					bmbStatusWhere = bmbStatusWhere + ",'" + bmbStatus.get(i) + "'";
				}
				bmbStatusWhere = bmbStatusWhere.substring(1);
				sql.append(" AND A.TZ_APP_FORM_STA IN (" + bmbStatusWhere + ") ");
			}

			// 面试资格;
			List<String> msZg = (List<String>) formDate.get("msZg");
			if (msZg != null && msZg.size() > 0) {
				for (int i = 0; i < msZg.size(); i++) {
					msZgWhere = msZgWhere + ",'" + msZg.get(i) + "'";
				}
				msZgWhere = msZgWhere.substring(1);
				sql.append(
						" AND exists (SELECT 'Y' FROM PS_TZ_MSZG_DR_T E WHERE E.TZ_MSH_ID=B.TZ_MSH_ID AND E.TZ_MSZG IN ("
								+ msZgWhere + ")) ");
			}

			// 面试结果;
			List<String> msJg = (List<String>) formDate.get("msJg");
			if (msJg != null && msJg.size() > 0) {
				for (int i = 0; i < msJg.size(); i++) {
					msJgWhere = msJgWhere + ",'" + msJg.get(i) + "'";
				}
				msJgWhere = msJgWhere.substring(1);
				sql.append(" AND exists (SELECT 'Y' FROM PS_TZ_MSJG_DR_T F WHERE F.TZ_MSH_ID=B.TZ_MSH_ID AND TZ_FLAG='Y' AND F.TZ_MSJG IN ("
								+ msJgWhere + ")) ");
			}

			// 录取状态;
			List<String> lqZt = (List<String>) formDate.get("lqZt");
			if (lqZt != null && lqZt.size() > 0) {
				for (int i = 0; i < lqZt.size(); i++) {
					lqZtWhere = lqZtWhere + ",'" + lqZt.get(i) + "'";
				}
				lqZtWhere = lqZtWhere.substring(1);
				sql.append(
						" AND exists (SELECT 'Y' FROM PS_TZ_LQJG_DR_T G WHERE G.TZ_MSH_ID=B.TZ_MSH_ID AND TZ_FLAG='Y' AND G.TZ_LQZT IN ("
								+ lqZtWhere + ")) ");
			}
			
			//不统计放弃;
			String fq =  String.valueOf(formDate.get("fq"));
			if("true".equals(fq)){
				//查询颜色类别放弃编号;
				String fqId = jdbcTemplate.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_COLOR_FQ'", "String");
				sql.append(" AND (B.TZ_COLOR_SORT_ID<>'"+fqId+"' OR B.TZ_COLOR_SORT_ID IS NULL) AND NOT exists (SELECT 'Y' FROM PS_TZ_STU_EXT_T WHERE TZ_MSH_ID = B.TZ_MSH_ID AND TZ_IS_ABANDON='Y') ");
			}
			/*
			if (colorType != null &&colorType.size() > 0) {
				for (int i = 0; i < colorType.size(); i++) {
					colorTypeWhere = colorTypeWhere + ",'" + colorType.get(i) + "'";
				}
				colorTypeWhere = colorTypeWhere.substring(1);
				sql.append(
						" AND B.TZ_COLOR_SORT_ID IN (" + colorTypeWhere + ") ");
			}
			*/
		}

		String typWhere = "", prjWhere = "", clsWhere = "", batWhere = "";
		// 分类
		if (jacksonUtil.containsKey("typ")) {
			List<String> typ = (List<String>) jacksonUtil.getList("typ");
			if (typ != null && typ.size() > 0) {
				for (int i = 0; i < typ.size(); i++) {
					typWhere = typWhere + ",'" + typ.get(i) + "'";
				}
				typWhere = typWhere.substring(1);
				typWhere = " EXISTS(SELECT 'Y' FROM PS_TZ_PRJ_INF_T D WHERE D.TZ_PRJ_ID=C.TZ_PRJ_ID AND TZ_PRJ_TYPE_ID IN ("
						+ typWhere + ")) ";
			}
		}

		// 项目
		if (jacksonUtil.containsKey("prj")) {
			List<String> prj = (List<String>) jacksonUtil.getList("prj");
			if (prj != null && prj.size() > 0) {
				for (int i = 0; i < prj.size(); i++) {
					prjWhere = prjWhere + ",'" + prj.get(i) + "'";
				}
				prjWhere = prjWhere.substring(1);
				if (typWhere == null || "".equals(typWhere)) {
					typWhere = " C.TZ_PRJ_ID IN (" + prjWhere + ") ";
				} else {
					typWhere = typWhere + " or C.TZ_PRJ_ID IN (" + prjWhere + ") ";
				}

			}
		}

		// 班级
		if (jacksonUtil.containsKey("cls")) {
			List<String> cls = (List<String>) jacksonUtil.getList("cls");
			if (cls != null && cls.size() > 0) {
				for (int i = 0; i < cls.size(); i++) {
					clsWhere = clsWhere + ",'" + cls.get(i) + "'";
				}
				clsWhere = clsWhere.substring(1);
				if (typWhere == null || "".equals(typWhere)) {
					typWhere = " B.TZ_CLASS_ID IN (" + clsWhere + ") ";
				} else {
					typWhere = typWhere + " or B.TZ_CLASS_ID IN (" + clsWhere + ") ";
				}

			}
		}

		// 批次
		if (jacksonUtil.containsKey("bat")) {
			List<String> bat = (List<String>) jacksonUtil.getList("bat");
			if (bat != null && bat.size() > 0) {
				for (int i = 0; i < bat.size(); i++) {
					batWhere = batWhere + ",'" + bat.get(i) + "'";
				}
				batWhere = batWhere.substring(1);
				if (typWhere == null || "".equals(typWhere)) {
					typWhere = " B.TZ_BATCH_ID IN (" + batWhere + ") ";
				} else {
					typWhere = typWhere + " or C.TZ_PRJ_ID IN (" + batWhere + ") ";
				}

			}
		}

		if (typWhere != null && !"".equals(typWhere)) {
			sql.append(" AND ( " + typWhere + ") ");
		}

		System.out.println("sql======>:" + sql);
		jdbcTemplate.execute(String.valueOf(sql));
	}

	// 统计年龄;
	private HashMap<String, Object> getAgeTjList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");
		
		String bmbBirthdayName = jdbcTemplate.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_BMTJ_BIRTHDAY'", "String");
		if(bmbBirthdayName == null){
			bmbBirthdayName = "";
		}
		
		StringBuilder nlSql = new StringBuilder();
		nlSql.append(" SELECT D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ,COUNT(E.age_type) NUM FROM ");
		nlSql.append(" (select TZ_ZHZ_ID,TZ_ZHZ_DMS from  PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BB_NLTJ' AND TZ_EFF_STATUS='A') D  ");
		nlSql.append(" LEFT JOIN (SELECT C.age,case when C.age <24 then 'A' when C.age <=26 then 'B' ");
		nlSql.append(" when C.age <=30 then 'C' when C.age <=34 then 'D' when C.age >=35 then 'E' else 'F' end age_type");
		nlSql.append(" FROM (select TZ_APP_S_TEXT,(year(now())-year(TZ_APP_S_TEXT)-1) + ( DATE_FORMAT(TZ_APP_S_TEXT, '%m%d') <= DATE_FORMAT(NOW(), '%m%d') ) as age  from TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID");
		nlSql.append(" AND B.TZ_XXX_BH = ?) C) E ON D.TZ_ZHZ_ID=E.age_type GROUP BY D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS");

		List<Map<String, Object>> nlList = jdbcTemplate.queryForList(String.valueOf(nlSql),new Object[]{bmbBirthdayName});
		if (nlList != null && nlList.size() > 0) {
			for (int i = 0; i < nlList.size(); i++) {
				Map<String, Object> mapList = new HashMap<>();

				mapList.put("name", nlList.get(i).get("TZ_ZHZ_DMS"));

				long num = (long) nlList.get(i).get("NUM");
				mapList.put("num", String.valueOf(num));

				if (total == 0) {
					mapList.put("scale", "0.0000%");
				} else {
					double scale = (num * 100.0D) / total;
					DecimalFormat df = new DecimalFormat("0.0000");
					mapList.put("scale", df.format(scale) + "%");
				}

				listData.add(mapList);
			}
			
		}
		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		
		return mapRet;
	}

	// 统计毕业院校;
	private HashMap<String, Object> getSchoolTjList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		
		//总人数;
		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");
		
		//有分类的人数;
		long hastypenum = 0;
		//分类
		StringBuilder nlSql = new StringBuilder("SELECT TB.TZ_SCHOOL_TYPEID,TB.TZ_SCHOOL_TYPENAME,COUNT(*) NUM FROM (SELECT A.TZ_APP_INS_ID,");
		nlSql.append(" (SELECT TZ_SCHOOL_TYPE FROM PS_TZ_SCH_LIB_TBL WHERE TZ_SCHOOL_NAMEENG =B.TZ_APP_S_TEXT OR TZ_SCHOOL_NAME = B.TZ_APP_S_TEXT OR concat(TZ_SCHOOL_NAME,\"(\",TZ_SCHOOL_NAMEENG,\")\")=B.TZ_APP_S_TEXT LIMIT 1) TZ_SCHOOL_TYPE");
		nlSql.append(" FROM TMP_KS_BBTJ_TMP A INNER JOIN PS_TZ_APP_CC_T B ON(A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_XXX_BH = 'heduheduuniversch' AND B.TZ_APP_S_TEXT IS NOT NULL AND B.TZ_APP_S_TEXT<>'')) TA,");
		nlSql.append(" PS_TZ_SCHOOL_TYPE_TBL TB WHERE TA.TZ_SCHOOL_TYPE = TB.TZ_SCHOOL_TYPEID GROUP BY TB.TZ_SCHOOL_TYPEID,TB.TZ_SCHOOL_TYPENAME");

		List<Map<String, Object>> nlList = jdbcTemplate.queryForList(String.valueOf(nlSql));
		if (nlList != null && nlList.size() > 0) {
			for (int i = 0; i < nlList.size(); i++) {
				Map<String, Object> mapList = new HashMap<>();

				mapList.put("name", nlList.get(i).get("TZ_SCHOOL_TYPENAME"));

				long num = (long) nlList.get(i).get("NUM");
				hastypenum = hastypenum + num;
				mapList.put("num", String.valueOf(num));

				if (total == 0) {
					mapList.put("scale", "0.0000%");
				} else {
					double scale = (num * 100.0D) / total;
					DecimalFormat df = new DecimalFormat("0.0000");
					mapList.put("scale", df.format(scale) + "%");
				}

				listData.add(mapList);
			}
			
		}
		
		/*
		//985;
		StringBuilder school_985_Sql = new StringBuilder();
		school_985_Sql.append(" select count(1) from TMP_KS_BBTJ_TMP A , PS_TZ_APP_CC_T B  ");
		school_985_Sql.append(" WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_XXX_BH = 'heduheduuniversch' ");
		school_985_Sql.append(" AND B.TZ_APP_S_TEXT IS NOT NULL  AND B.TZ_APP_S_TEXT <> '' ");
		school_985_Sql.append(" and  B.TZ_APP_S_TEXT in ( ");
		school_985_Sql.append("   select TZ_SCHOOL_NAME from PS_TZ_SCH_LIB_TBL where COUNTRY IN ('中国','中国大陆','CHN')  ");
		school_985_Sql.append("   and TZ_SCHOOL_TYPE in ('1','2','3','6')) ");
		school_985_Sql.append(" and exists( ");
		school_985_Sql.append("   select 'Y' FROM PS_TZ_APP_CC_T C WHERE C.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND C.TZ_XXX_BH='heduheduunivercountry'  ");
		school_985_Sql.append("   AND C.TZ_APP_S_TEXT IN ('中国','中国大陆','CHN')) ");
		int school_985_total = jdbcTemplate.queryForObject(String.valueOf(school_985_Sql),"Integer");
		
		Map<String, Object> map985List = new HashMap<>();
		map985List.put("name", "985院校");
		map985List.put("num", school_985_total);
		if (total == 0) {
			map985List.put("scale", "0.0000%");
		} else {
			double scale = (school_985_total * 100.0D) / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			map985List.put("scale", df.format(scale) + "%");
		}
		listData.add(map985List);
		
		//211;
		StringBuilder school_211_Sql = new StringBuilder();
		school_211_Sql.append(" select count(1) from TMP_KS_BBTJ_TMP A , PS_TZ_APP_CC_T B  ");
		school_211_Sql.append(" WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_XXX_BH = 'heduheduuniversch' ");
		school_211_Sql.append(" AND B.TZ_APP_S_TEXT IS NOT NULL  AND B.TZ_APP_S_TEXT <> '' ");
		school_211_Sql.append(" and  B.TZ_APP_S_TEXT in ( ");
		school_211_Sql.append("  select TZ_SCHOOL_NAME from PS_TZ_SCH_LIB_TBL where COUNTRY IN ('中国','中国大陆','CHN')  ");
		school_211_Sql.append("  and TZ_SCHOOL_TYPE = '4') ");
		school_211_Sql.append(" and exists( ");
		school_211_Sql.append("  select 'Y' FROM PS_TZ_APP_CC_T C WHERE C.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND C.TZ_XXX_BH='heduheduunivercountry'  ");
		school_211_Sql.append("  AND C.TZ_APP_S_TEXT IN ('中国','中国大陆','CHN')) ");
		int school_211_total = jdbcTemplate.queryForObject(String.valueOf(school_211_Sql),"Integer");
		
		Map<String, Object> map211List = new HashMap<>();
		map211List.put("name", "211院校");
		map211List.put("num", school_211_total);
		if (total == 0) {
			map211List.put("scale", "0.0000%");
		} else {
			double scale = (school_211_total * 100.0D) / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			map211List.put("scale", df.format(scale) + "%");
		}
		listData.add(map211List);
				
		//海外;
		StringBuilder school_frn_Sql = new StringBuilder();
		school_frn_Sql.append(" select count(1) from TMP_KS_BBTJ_TMP A , PS_TZ_APP_CC_T B ");
		school_frn_Sql.append(" WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_XXX_BH = 'heduheduuniversch' ");
		school_frn_Sql.append(" AND B.TZ_APP_S_TEXT IS NOT NULL  AND B.TZ_APP_S_TEXT <> '' ");
		school_frn_Sql.append(" and exists( ");
		school_frn_Sql.append("  select 'Y' FROM PS_TZ_APP_CC_T C WHERE C.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND C.TZ_XXX_BH='heduheduunivercountry'  ");
		school_frn_Sql.append("  AND C.TZ_APP_S_TEXT NOT IN ('中国','中国大陆','CHN') ");
		school_frn_Sql.append("  AND C.TZ_APP_S_TEXT IS NOT NULL  AND C.TZ_APP_S_TEXT <> '') ");
		int school_frn_total = jdbcTemplate.queryForObject(String.valueOf(school_frn_Sql),"Integer");
		
		Map<String, Object> mapFrnList = new HashMap<>();
		mapFrnList.put("name", "海外院校");
		mapFrnList.put("num", school_frn_total);
		if (total == 0) {
			mapFrnList.put("scale", "0.0000%");
		} else {
			double scale = (school_frn_total * 100.0D) / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			mapFrnList.put("scale", df.format(scale) + "%");
		}
		listData.add(mapFrnList);
		*/
		//填写院校的人数
		StringBuilder school_has_Sql = new StringBuilder();
		school_has_Sql.append(" select count(1) from TMP_KS_BBTJ_TMP A , PS_TZ_APP_CC_T B  ");
		school_has_Sql.append(" WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_XXX_BH = 'heduheduuniversch' ");
		school_has_Sql.append(" AND B.TZ_APP_S_TEXT IS NOT NULL  AND B.TZ_APP_S_TEXT <> '' ");
		int school_has_total = jdbcTemplate.queryForObject(String.valueOf(school_has_Sql),"Integer");
		
		int school_qt_total = (int) (school_has_total - hastypenum);
		Map<String, Object> mapQtList = new HashMap<>();
		mapQtList.put("name", "其他院校");
		mapQtList.put("num", String.valueOf(school_qt_total));
		if (total == 0) {
			mapQtList.put("scale", "0.0000%");
		} else {
			double scale = (school_qt_total * 100.0D) / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			mapQtList.put("scale", df.format(scale) + "%");
		}
		listData.add(mapQtList);
		
		//未知
		int school_wz_total = total - school_has_total;
		Map<String, Object> mapWzList = new HashMap<>();
		mapWzList.put("name", "未知");
		mapWzList.put("num", String.valueOf(school_wz_total));
		if (total == 0) {
			mapWzList.put("scale", "0.0000%");
		} else {
			double scale = (school_wz_total * 100.0D) / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			mapWzList.put("scale", df.format(scale) + "%");
		}
		listData.add(mapWzList);
		
		mapRet.replace("total", total);
		mapRet.replace("root", listData);

		return mapRet;
	}

	// 统计工龄;
	private HashMap<String, Object> getWorkAgeTjList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");

		StringBuilder glSql = new StringBuilder("SELECT D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ,COUNT(WORK_AGE) NUM FROM");
		glSql.append(" PS_TZ_PT_ZHZXX_TBL D");
		glSql.append(" JOIN (SELECT A.TZ_APP_INS_ID,case when CAST(TZ_APP_S_TEXT AS SIGNED) >= 15 then 'F'");
		glSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) >= 11 then 'E'");
		glSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) >= 9 then 'D'");
		glSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) >= 6 then 'C'");
		glSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) >= 3 then 'B'");
		glSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) > 0 then 'A'");
		glSql.append(" else 'G' END WORK_AGE");
		glSql.append(" FROM TMP_KS_BBTJ_TMP A join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH like '%fulltime%') C");
		glSql.append(" ON D.TZ_ZHZ_ID=C.WORK_AGE WHERE D.TZ_ZHZJH_ID='TZ_BB_GLTJ' AND D.TZ_EFF_STATUS='A' GROUP BY D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS");

		List<Map<String, Object>> nlList = jdbcTemplate.queryForList(String.valueOf(glSql));
		if (nlList != null && nlList.size() > 0) {
			for (int i = 0; i < nlList.size(); i++) {
				Map<String, Object> mapList = new HashMap<>();
				mapList.put("name", nlList.get(i).get("TZ_ZHZ_DMS"));

				long num = (long) nlList.get(i).get("NUM");
				mapList.put("num", String.valueOf(num));

				if (total == 0) {
					mapList.put("scale", "0.0000%");
				} else {
					double scale = (num * 100.0D) / total;
					DecimalFormat df = new DecimalFormat("0.0000");
					mapList.put("scale", df.format(scale) + "%");
				}

				listData.add(mapList);
			}
			
		}
		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		return mapRet;
	}

	// 统计年薪;
	private HashMap<String, Object> getWorkSalaryList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");

		StringBuilder nxSql = new StringBuilder();
		nxSql.append(" SELECT D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ,COUNT(WORK_SALARY) NUM FROM ");
		nxSql.append(" (select TZ_ZHZ_ID,TZ_ZHZ_DMS from  PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BB_SALARY' AND TZ_EFF_STATUS='A') D  ");
		nxSql.append(" LEFT JOIN ( ");
		nxSql.append(" SELECT A.TZ_APP_INS_ID,case when CAST(TZ_APP_S_TEXT AS SIGNED) >= 60 then 'E' ");
		nxSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) >= 40 then 'D' ");
		nxSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) >= 20 then 'C' ");
		nxSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) >= 10 then 'B' ");
		nxSql.append(" when CAST(TZ_APP_S_TEXT AS SIGNED) > 0 then 'A' ");
		nxSql.append(" else 'F' END WORK_SALARY ");
		nxSql.append(" from TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH like '%ansalary%') C ");
		nxSql.append(" ON D.TZ_ZHZ_ID=C.WORK_SALARY GROUP BY D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ");

		List<Map<String, Object>> nlList = jdbcTemplate.queryForList(String.valueOf(nxSql));
		if (nlList != null && nlList.size() > 0) {
			for (int i = 0; i < nlList.size(); i++) {
				Map<String, Object> mapList = new HashMap<>();
				mapList.put("name", nlList.get(i).get("TZ_ZHZ_DMS"));

				long num = (long) nlList.get(i).get("NUM");
				mapList.put("num", String.valueOf(num));

				if (total == 0) {
					mapList.put("scale", "0.0000%");
				} else {
					double scale = (num * 100.0D) / total;
					DecimalFormat df = new DecimalFormat("0.0000");
					mapList.put("scale", df.format(scale) + "%");
				}

				listData.add(mapList);
			}
			
		}
		
		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		return mapRet;
	}

	// 专业类别;
	private HashMap<String, Object> getMajorTypeList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");

		StringBuilder zylbSql = new StringBuilder();
		zylbSql.append(" SELECT D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ,COUNT(C.TZ_APP_S_TEXT) NUM FROM ");
		zylbSql.append(
				"  (select TZ_ZHZ_ID,TZ_ZHZ_DMS from  PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BB_MAJOR_TYPE' AND TZ_EFF_STATUS='A') D  ");
		zylbSql.append("   LEFT JOIN ( ");
		zylbSql.append(
				"     SELECT CASE WHEN TZ_APP_S_TEXT IS NULL THEN '99' WHEN TZ_APP_S_TEXT = '' THEN '99' ELSE TZ_APP_S_TEXT END TZ_APP_S_TEXT ");
		zylbSql.append(
				"     FROM TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH = 'hedumajortype') C");
		zylbSql.append("  ON D.TZ_ZHZ_ID=C.TZ_APP_S_TEXT GROUP BY D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ");

		List<Map<String, Object>> nlList = jdbcTemplate.queryForList(String.valueOf(zylbSql));
		if (nlList != null && nlList.size() > 0) {
			for (int i = 0; i < nlList.size(); i++) {
				Map<String, Object> mapList = new HashMap<>();
				mapList.put("name", nlList.get(i).get("TZ_ZHZ_DMS"));

				long num = (long) nlList.get(i).get("NUM");
				mapList.put("num", String.valueOf(num));

				if (total == 0) {
					mapList.put("scale", "0.0000%");
				} else {
					double scale = (num * 100.0D) / total;
					DecimalFormat df = new DecimalFormat("0.0000");
					mapList.put("scale", df.format(scale) + "%");
				}

				listData.add(mapList);
			}
			
		}
		
		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		return mapRet;
	}

	// 教育背景;
	private HashMap<String, Object> getEducationList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");

		StringBuilder jybjSql = new StringBuilder();
		jybjSql.append("  SELECT D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ,COUNT(C.TZ_APP_S_TEXT) NUM FROM  ");
		jybjSql.append(
				"    (select TZ_ZHZ_ID,TZ_ZHZ_DMS from  PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BB_EDU_TYPE' AND TZ_EFF_STATUS='A') D  ");
		jybjSql.append("     LEFT JOIN ( ");
		jybjSql.append(
				"      SELECT CASE WHEN TZ_APP_S_TEXT IS NULL THEN '99' WHEN TZ_APP_S_TEXT = '' THEN '99' ELSE TZ_APP_S_TEXT END TZ_APP_S_TEXT  ");
		jybjSql.append(
				"      FROM TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH = 'heduqua') C ");
		jybjSql.append("    ON D.TZ_ZHZ_ID=C.TZ_APP_S_TEXT GROUP BY D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ");

		List<Map<String, Object>> nlList = jdbcTemplate.queryForList(String.valueOf(jybjSql));
		if (nlList != null && nlList.size() > 0) {
			for (int i = 0; i < nlList.size(); i++) {
				Map<String, Object> mapList = new HashMap<>();
				mapList.put("name", nlList.get(i).get("TZ_ZHZ_DMS"));

				long num = (long) nlList.get(i).get("NUM");
				mapList.put("num", String.valueOf(num));

				if (total == 0) {
					mapList.put("scale", "0.0000%");
				} else {
					double scale = (num * 100.0D) / total;
					DecimalFormat df = new DecimalFormat("0.0000");
					mapList.put("scale", df.format(scale) + "%");
				}

				listData.add(mapList);
			}
			
		}

		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		return mapRet;
	}

	// 入学前单位性质;
	private HashMap<String, Object> getInsList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");

		StringBuilder insSql = new StringBuilder();
		insSql.append(" SELECT D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ,COUNT(C.TZ_APP_S_TEXT) NUM FROM ");
		insSql.append(
				"  (select TZ_ZHZ_ID,TZ_ZHZ_DMS from  PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BB_INS_TYPE' AND TZ_EFF_STATUS='A') D ");
		insSql.append("  LEFT JOIN (    ");
		insSql.append(
				"   SELECT CASE WHEN TZ_APP_S_TEXT IS NULL THEN '99' WHEN TZ_APP_S_TEXT = '' THEN '99' ELSE TZ_APP_S_TEXT END TZ_APP_S_TEXT  ");
		insSql.append(
				"   FROM TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH = 'currentworkins') C ");
		insSql.append(" ON D.TZ_ZHZ_ID=C.TZ_APP_S_TEXT GROUP BY D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ");

		List<Map<String, Object>> nlList = jdbcTemplate.queryForList(String.valueOf(insSql));
		if (nlList != null && nlList.size() > 0) {
			for (int i = 0; i < nlList.size(); i++) {
				Map<String, Object> mapList = new HashMap<>();
				mapList.put("name", nlList.get(i).get("TZ_ZHZ_DMS"));

				long num = (long) nlList.get(i).get("NUM");
				mapList.put("num", String.valueOf(num));

				if (total == 0) {
					mapList.put("scale", "0.0000%");
				} else {
					double scale = (num * 100.0D) / total;
					DecimalFormat df = new DecimalFormat("0.0000");
					mapList.put("scale", df.format(scale) + "%");
				}

				listData.add(mapList);
			}
			
		}
		
		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		return mapRet;
	}

	// 入学前行业背景;
	private HashMap<String, Object> getIndList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");

		StringBuilder indSql = new StringBuilder();
		indSql.append(" SELECT D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ,COUNT(C.TZ_APP_S_TEXT) NUM FROM ");
		indSql.append(
				"  (select TZ_ZHZ_ID,TZ_ZHZ_DMS from  PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BB_IND_TYPE' AND TZ_EFF_STATUS='A') D  ");
		indSql.append("  LEFT JOIN (   ");
		indSql.append(
				"    SELECT CASE WHEN TZ_APP_S_TEXT IS NULL THEN '99' WHEN TZ_APP_S_TEXT = '' THEN '99' ELSE TZ_APP_S_TEXT END TZ_APP_S_TEXT  ");
		indSql.append(
				"   FROM TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH = 'currentworkgsindustry') C ");
		indSql.append(" ON D.TZ_ZHZ_ID=C.TZ_APP_S_TEXT GROUP BY D.TZ_ZHZ_ID,D.TZ_ZHZ_DMS ");

		List<Map<String, Object>> nlList = jdbcTemplate.queryForList(String.valueOf(indSql));
		if (nlList != null && nlList.size() > 0) {
			for (int i = 0; i < nlList.size(); i++) {
				Map<String, Object> mapList = new HashMap<>();
				mapList.put("name", nlList.get(i).get("TZ_ZHZ_DMS"));

				long num = (long) nlList.get(i).get("NUM");
				mapList.put("num", String.valueOf(num));

				if (total == 0) {
					mapList.put("scale", "0.0000%");
				} else {
					double scale = (num * 100.0D) / total;
					DecimalFormat df = new DecimalFormat("0.0000");
					mapList.put("scale", df.format(scale) + "%");
				}

				listData.add(mapList);
			}
			
		}

		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		return mapRet;
	}

	// 管理经验;
	private HashMap<String, Object> getExperienceList() {

		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		// 平均管理岗工龄;
		int total = jdbcTemplate.queryForObject(
				"SELECT count(1) FROM TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH = 'workmanagetime' where CAST(TZ_APP_S_TEXT AS SIGNED) > 0",
				"Integer");
		Map<String, Object> mapList = new HashMap<>();
		if (total > 0) {
			Map<String, Object> gljyMap = jdbcTemplate.queryForMap(
					"SELECT sum(CAST(TZ_APP_S_TEXT AS SIGNED)) SUM,sum(CAST(TZ_APP_S_TEXT AS SIGNED))/count(1) PJ FROM TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH = 'workmanagetime' where CAST(TZ_APP_S_TEXT AS SIGNED) > 0");
			mapList.put("name", "平均管理岗工龄");
			mapList.put("num", String.valueOf(gljyMap.get("PJ")));
		} else {
			mapList.put("name", "平均管理岗工龄");
			mapList.put("num", "0");
		}

		// 平均管理人数
		total = jdbcTemplate.queryForObject(
				"SELECT count(1) FROM TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH = 'currentworknnzzxss' where CAST(TZ_APP_S_TEXT AS SIGNED) > 0",
				"Integer");
		Map<String, Object> mapList2 = new HashMap<>();
		if (total > 0) {
			Map<String, Object> gljyMap = jdbcTemplate.queryForMap(
					"SELECT sum(CAST(TZ_APP_S_TEXT AS SIGNED)) SUM,sum(CAST(TZ_APP_S_TEXT AS SIGNED))/count(1) PJ FROM TMP_KS_BBTJ_TMP A left join PS_TZ_APP_CC_T B on A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH = 'currentworknnzzxss' where CAST(TZ_APP_S_TEXT AS SIGNED) > 0");
			mapList2.put("name", "平均管理人数");
			mapList2.put("num", String.valueOf(gljyMap.get("PJ")));
		} else {
			mapList2.put("name", "平均管理人数");
			mapList2.put("num", "0");
		}

		listData.add(mapList);
		listData.add(mapList2);
		mapRet.replace("root", listData);

		return mapRet;
	}

	// 性别;
	private HashMap<String, Object> getSexList() {
		// 返回值;
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		int total = jdbcTemplate.queryForObject("select count(1) from TMP_KS_BBTJ_TMP", "Integer");

		int mTotal = jdbcTemplate.queryForObject(
				"select COUNT(1) FROM TMP_KS_BBTJ_TMP A , PS_TZ_APP_CC_T B WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH  = 'pinfodgender' and TZ_APP_S_TEXT='M'",
				"Integer");

		int fTotal = jdbcTemplate.queryForObject(
				"select COUNT(1) FROM TMP_KS_BBTJ_TMP A , PS_TZ_APP_CC_T B WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND TZ_XXX_BH  = 'pinfodgender' and TZ_APP_S_TEXT='F'",
				"Integer");

		// 男
		Map<String, Object> mMap = new HashMap<>();
		if (total > 0) {
			mMap.put("name", "男");
			mMap.put("num", mTotal);

			double scale = (mTotal * 100.0D) / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			mMap.put("scale", df.format(scale) + "%");
		} else {
			mMap.put("name", "男");
			mMap.put("num", 0);
			mMap.put("scale", "0.0000%");
		}
		listData.add(mMap);

		// 女
		Map<String, Object> fMap = new HashMap<>();
		if (total > 0) {
			fMap.put("name", "女");
			fMap.put("num", fTotal);

			double scale = (fTotal * 100.0D) / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			fMap.put("scale", df.format(scale) + "%");
		} else {
			fMap.put("name", "女");
			fMap.put("num", 0);
			fMap.put("scale", "0.0000%");
		}
		listData.add(fMap);

		// 未知
		Map<String, Object> noMap = new HashMap<>();
		int noTotal = total - mTotal - fTotal;
		if (total > 0) {
			noMap.put("name", "未知");
			noMap.put("num", noTotal);

			double scale = (noTotal * 100.0D) / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			noMap.put("scale", df.format(scale) + "%");
		} else {
			noMap.put("name", "未知");
			noMap.put("num", 0);
			noMap.put("scale", "0.0000%");
		}
		listData.add(noMap);

		mapRet.replace("total", total);
		mapRet.replace("root", listData);

		return mapRet;
	}
}
