package com.tranzvision.gd.TZBaseBundle.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年6月22日 上午11:20:08 类说明
 */
@Controller
@RequestMapping(value = "/")
public class FileDownloadController {


	
	/**
	 * 下载上传过的PDF模板文件
	 * 
	 * @param request
	 * @param response
	 * @param allRequestParams
	 * @return
	 */
	@RequestMapping(value = "DownExcelTServlet", produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgDownBmbExcelHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams) {
		// System.out.println("PDF实例下载");
		String insid = String.valueOf(allRequestParams.get("insid"));

		int processinstance = Integer.parseInt(insid);
		
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		SqlQuery jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		Map<String,Object> map = jdbcTemplate.queryForMap("SELECT TZ_DR_TASK_DESC,TZ_FWQ_FWLJ FROM PS_TZ_GD_DCEXCEL_V where PROCESSINSTANCE = ? limit 0,1",
				new Object[] { processinstance });
		
		String fileName = "";
		String filePath = "";
		if (map!= null) {
			try {
				fileName = map.get("TZ_DR_TASK_DESC")==null ? "":String.valueOf(map.get("TZ_DR_TASK_DESC"));
				filePath = map.get("TZ_FWQ_FWLJ")==null ? "":String.valueOf(map.get("TZ_FWQ_FWLJ"));
				String userAgent = request.getHeader("User-Agent").toUpperCase();
				if (userAgent != null && (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("LIKE GECKO")>0)) {
					fileName = URLEncoder.encode(fileName, "UTF-8");
					if (fileName.length() > 150) {
						// 根据request的locale 得出可能的编码， 中文操作系统通常是gb2312
						String guessCharset = "gb2312";
						fileName = new String(fileName.getBytes(guessCharset), "ISO8859-1");
					}
				} else {
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				}

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ServletOutputStream out;
			try {

				byte[] buffer = null;

				try {
					filePath = request.getServletContext().getRealPath(filePath);
					System.out.println(filePath);
					InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
					buffer = new byte[fis.available()];

					fis.read(buffer);
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (buffer == null || buffer.length <=0) {
					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("success", false);
					mapRet.put("msg", "文件不存在");
					JacksonUtil jacksonUtil = new JacksonUtil();
					return jacksonUtil.Map2json(mapRet);
				} else {

					response.setContentType("multipart/form-data");
					// 2.设置文件头：最后一个参数是设置下载文件名
					response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".xlsx");
					// response.reset();
					// 3.通过response获取ServletOutputStream对象(out)
					out = response.getOutputStream();
					out.write(buffer);// 输出文件
					out.close();
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", true);
		mapRet.put("msg", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);
	}
	/**
	 * 下载上传过的PDF模板文件
	 * 
	 * @param request
	 * @param response
	 * @param allRequestParams
	 * @return
	 */
	@RequestMapping(value = "DownTplServlet", produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgDownTplHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams) {
		// System.out.println("PDF实例下载");
		String tplId = String.valueOf(allRequestParams.get("tplId"));
		
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		SqlQuery jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		Map<String,Object> map = jdbcTemplate.queryForMap("SELECT TZ_FILENAME ,TZ_EXCEL_TPL FROM TZ_IMP_TPL_DFN_T where TZ_TPL_ID=?",
				new Object[] { tplId });
		
		String fileName = "";
		String filePath = "";
		if (map!= null) {
			try {
				fileName = map.get("TZ_FILENAME")==null ? "":String.valueOf(map.get("TZ_FILENAME"));
				filePath = map.get("TZ_EXCEL_TPL")==null ? "":String.valueOf(map.get("TZ_EXCEL_TPL"));
				String userAgent = request.getHeader("User-Agent").toUpperCase();
				if (userAgent != null && (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("LIKE GECKO")>0)) {
					fileName = URLEncoder.encode(fileName, "UTF-8");
					if (fileName.length() > 150) {
						// 根据request的locale 得出可能的编码， 中文操作系统通常是gb2312
						String guessCharset = "gb2312";
						fileName = new String(fileName.getBytes(guessCharset), "ISO8859-1");
					}
				} else {
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				}

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ServletOutputStream out;
			try {

				byte[] buffer = null;

				try {
					filePath = request.getServletContext().getRealPath(filePath);
					InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
					buffer = new byte[fis.available()];

					fis.read(buffer);
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (buffer == null || buffer.length <=0) {
					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("success", false);
					mapRet.put("msg", "文件不存在");
					JacksonUtil jacksonUtil = new JacksonUtil();
					return jacksonUtil.Map2json(mapRet);
				} else {

					response.setContentType("multipart/form-data");
					// 2.设置文件头：最后一个参数是设置下载文件名
					response.setHeader("Content-Disposition", "attachment;fileName=" + fileName );
					// response.reset();
					// 3.通过response获取ServletOutputStream对象(out)
					out = response.getOutputStream();
					out.write(buffer);// 输出文件
					out.close();
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", true);
		mapRet.put("msg", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);
	}
}
