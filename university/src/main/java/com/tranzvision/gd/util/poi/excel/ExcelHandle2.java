/**
 * 
 */
package com.tranzvision.gd.util.poi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 处理导出excel工具类
 * 
 * @author SHIHUA
 * @since 2016-02-25
 */
public class ExcelHandle2 {
	
	/**
	 * 导出数据，生成的Excel文件路径
	 */
	private String exportExcelPath;
	
	private String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";
	
	//下载路径;
	private String expDirPath;
	//实际路径;
	private String absexpDirPath;
	
	/**
	 * 读取到的excel列表数据
	 */
	private ArrayList<Map<String, Object>> excelListData = new ArrayList<Map<String, Object>>();
	
	/**
	 * 导出文件用构造函数
	 * 
	 * @param request
	 * @param downloadPath
	 *            系统规划的下载文件存储路径
	 * @param loginOrgid
	 *            当前登录的机构
	 * @param mdlName
	 *            当前操作的模块名称，用于创建导出excel的路径，默认为：default
	 */
	public ExcelHandle2() {}
	
	public ExcelHandle2(String expDirPath,String absexpDirPath) {
		this.expDirPath = expDirPath;
		this.absexpDirPath = absexpDirPath;
	}
	
	
	/**
	 * 导出数据集到Excel文件
	 * 
	 * @param expFileName
	 *            生成的excel文件名
	 * @param dataCellKeys
	 *            导出的excel的表头，String[]: 0 => 字段名，1 => 表头显示内容；
	 * @param dataList
	 *            要导出的数据集，Map eg: Map<字段名,字段值>
	 * @return boolean
	 */
	public boolean export2Excel(String expFileName, List<String[]> dataCellKeys, List<Map<String, Object>> dataList) {

		boolean ret = false;


		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("DataExport");

		int rownum = 0;
		for (Map<String, Object> mapData : dataList) {

			int cellnum = 0;
			if (rownum == 0) {
				// 如果是第一行，则写入表头
				Row row = sheet.createRow(rownum);
				for (String[] keysAry : dataCellKeys) {
					String key = keysAry[0];
					String keyName = key;
					if (keysAry.length > 1) {
						keyName = keysAry[1];
					}
					org.apache.poi.ss.usermodel.Cell cell = row.createCell(cellnum);
					cell.setCellValue(keyName);
					cellnum++;
				}
				rownum++;
			}

			// 生成数据行
			Row row = sheet.createRow(rownum);
			cellnum = 0;
			for (String[] keysAry : dataCellKeys) {
				String key = keysAry[0];
				org.apache.poi.ss.usermodel.Cell cell = row.createCell(cellnum);
				Object obj = mapData.get(key);

				String dateFormat = "";
				if (obj instanceof Object[]) {
					Object[] objAry = (Object[]) obj;
					if (objAry[0] instanceof Date) {
						// 获取日期格式
						obj = objAry[0];
						dateFormat = String.valueOf(objAry[1]);
					}
				}

				if (obj instanceof Date) {

					if ("".equals(dateFormat)) {
						dateFormat = defaultDateFormat;
					}

					CellStyle cellStyle = workbook.createCellStyle();
					CreationHelper createHelper = workbook.getCreationHelper();
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
					cell.setCellValue((Date) obj);
					cell.setCellStyle(cellStyle);

				} else if (obj instanceof Boolean)
					cell.setCellValue((Boolean) obj);
				else if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Double)
					cell.setCellValue((Double) obj);
				else if (obj instanceof Integer)
					cell.setCellValue(Double.parseDouble(obj.toString()));

				cellnum++;
			}

			rownum++;
		}

		try {
			File expFile = newFileObject(expFileName);
			FileOutputStream os = new FileOutputStream(expFile);
			workbook.write(os);
			os.close();
			workbook.close();
			ret = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;

	}
	
	
	/**
	 * 创建要生成的File实例
	 * 
	 * @param expFileName
	 *            文件名
	 * @return
	 */
	private File newFileObject(String expFileName) {

		File dir = new File(absexpDirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		if (null == expFileName || "".equals(expFileName)) {
			// 如果不传文件名，则表示让系统自动命名
			expFileName = genFileName() + ".xlsx";
		}

		String dirAbsolutePath = dir.getAbsolutePath() + File.separator;
		File expFile = new File(dirAbsolutePath + expFileName);

		while (expFile.exists()) {
			expFileName = genFileName() + ".xlsx";
			expFile = new File(dirAbsolutePath + expFileName);
		}

		exportExcelPath = expDirPath + "/" + expFileName;

		return expFile;
	}
	
	
	/**
	 * 生成文件名
	 * 
	 * @return
	 */
	private String genFileName() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		int hour = cal.get(10);
		int minute = cal.get(12);
		int second = cal.get(13);
		int mi = cal.get(14);
		long num = cal.getTimeInMillis();
		int rand = (int) (Math.random() * 899999 + 100000);
		return (new StringBuilder()).append(year).append(month).append(day).append(hour).append(minute).append(second)
				.append(mi).append(num).append("_").append(rand).toString();
	}
	
	/**
	 * 获取生成的Excel文件路径
	 * 
	 * @return
	 */
	public String getExportExcelPath() {
		return exportExcelPath;
	}
	
	/**
	 * 读取Excel列表
	 * 
	 * @param dataFilePath
	 *            Excel文件路径
	 * @param dataListCellKeys
	 *            要读取的表头字段名，传 null 则表示读取所有列
	 * @param firstRowKeys
	 *            第一行是否为表头：true 是；false 否；
	 */
	public void readExcel(String dataFilePath, List<String> dataListCellKeys, boolean firstRowKeys) {

		try {

			//dataFilePath = request.getServletContext().getRealPath(dataFilePath);

			File file = new File(dataFilePath);

			Workbook workbook;

			if (dataFilePath.toLowerCase().endsWith(".xlsx")) {

				workbook = new XSSFWorkbook(new FileInputStream(file));

			} else if (dataFilePath.toLowerCase().endsWith(".xls")) {

				workbook = new HSSFWorkbook(new FileInputStream(file));

			} else {
				return;
			}

			Sheet sheet = workbook.getSheetAt(0);

			// 存储excel第一行表头字段名
			List<String> excelFirstRowKeys = new ArrayList<String>();

			// 遍历行记录
			int rownum = 0;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();

				Map<String, Object> mapCell = new HashMap<String, Object>();

				// 遍历所有列
				for(int cellnum = 0; cellnum<row.getLastCellNum(); cellnum++) {
					
				    // if the cell is missing from the file, generate a blank one
					org.apache.poi.ss.usermodel.Cell cell = row.getCell(cellnum,Row.CREATE_NULL_AS_BLANK);

					if (rownum == 0 && firstRowKeys) {
						// 如果是第一行，并且第一行包含表头，则读取表头
						String keyName = "";
						switch (cell.getCellType()) {
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
							keyName = String.valueOf(cell.getNumericCellValue());
							break;
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
							keyName = cell.getStringCellValue();
							break;
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK:
							keyName = cell.getStringCellValue();
							break;
						}

						excelFirstRowKeys.add(keyName.trim());
						cellnum++;
						continue;
					}

					//String cellKey = excelFirstRowKeys.get(cellnum);
					String cellKey = String.valueOf(cellnum);
					if (null != dataListCellKeys && dataListCellKeys.size() > 0 && firstRowKeys) {
						cellKey = excelFirstRowKeys.get(cellnum);
						if (!dataListCellKeys.contains(cellKey)) {
							// 若不需要获取当前列数据，则跳过
							cellnum++;
							continue;
						}
					}
					
					switch (cell.getCellType()) {
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
						mapCell.put(cellKey, cell.getBooleanCellValue());
						break;
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:

						if (DateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							mapCell.put(cellKey, cell.getDateCellValue());
							SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);
							mapCell.put(cellKey + "_format", sdf.format(date));
						} else {
							NumberFormat nf = NumberFormat.getInstance();
							nf.setGroupingUsed(false);// true时的格式：1,234,567,890
							String cellValue = nf.format(cell.getNumericCellValue());// 数值类型的数据为double，所以需要转换一下
							mapCell.put(cellKey, cellValue);
						}

						break;
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
						mapCell.put(cellKey, cell.getStringCellValue());
						break;
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
						mapCell.put(cellKey, cell.getCellFormula());
						break;
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK:
						mapCell.put(cellKey, "");
						break;
					default:
						mapCell.put(cellKey, "");
						break;
					}
				}

				if (!firstRowKeys || rownum > 0) {
					excelListData.add(mapCell);
				}

				rownum++;

			}

			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 获取读取到的列表数据
	 * 
	 * @return
	 */
	public ArrayList<Map<String, Object>> getExcelListData() {
		return excelListData;
	}

}
