package com.tranzvision.gd.util.httpclient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.EncodingUtil;

public class HttpUtils {
	public static void httpSend(String url, List<ParamsBean> params,
			RequestCallback rcb) {
		try {
			URL mUrl = new URL(url);

			HttpURLConnection connection = (HttpURLConnection)mUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setDefaultUseCaches(false);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			connection.setDoOutput(true);
			connection.setDoInput(true);

			PrintWriter out = new PrintWriter(connection.getOutputStream());

			out.print(getValue(params));
			String str = getValue(params).replaceAll("=", "/");
			String str1 = str.replaceAll("&", "/");

			System.out.println(mUrl+str1);

			out.flush();
			InputStream is = connection.getInputStream();
			String result = StreamUtils.getStringFromStream(is);
			rcb.callBack(result);
			is.close();
			out.close();


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常");
		}

	}
	public static void httpSends(String url,RequestCallback rcb) {
		try {
			URL mUrl = new URL(url);

			HttpURLConnection connection = (HttpURLConnection)mUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setDefaultUseCaches(false);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			connection.setDoOutput(true);
			connection.setDoInput(true);

			InputStream is = connection.getInputStream();
			String result = StreamUtils.getStringFromStream(is);
			rcb.callBack(result);
			is.close();


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常");
		}

	}
	
	
	
	
	
	/** 
	 *  
	 *  
	 * @param urlStr 
	 * @param textMap 
	 * @param fileMap 
	 * @return 
	 */  
	public static String formUpload(String urlStr, Map<String, String> textMap,  
			Map<String, String> fileMap) {  
		String res = "";  
		HttpURLConnection conn = null;  
		String BOUNDARY = "---------------------------123821742118716"; 
		try {  
			URL url = new URL(urlStr);  
			conn = (HttpURLConnection) url.openConnection();  //建立连接
			conn.setConnectTimeout(5000);  
			conn.setReadTimeout(60000);  
			conn.setDoOutput(true);  //设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在  http正文内，因此需要设为true, 默认情况下是false;  
			conn.setDoInput(true);  
			conn.setUseCaches(false);  
			conn.setRequestMethod("POST");//制定相应方式  
			conn.setRequestProperty("Connection", "Keep-Alive");  
			conn.setRequestProperty("User-Agent",  
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
			conn.setRequestProperty("Content-Type",  
					"multipart/form-data; boundary=" + BOUNDARY);  

			OutputStream out = new DataOutputStream(conn.getOutputStream());  
			// text  
			if (textMap != null) {  
				StringBuffer strBuf = new StringBuffer();  
				Iterator iter = textMap.entrySet().iterator();  
				while (iter.hasNext()) {  
					Map.Entry entry = (Map.Entry) iter.next();  
					String inputName = (String) entry.getKey();  
					String inputValue = (String) entry.getValue();  
					if (inputValue == null) {  
						continue;  
					}  
					strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
							"\r\n");  
					strBuf.append("Content-Disposition: form-data; name=\""  
							+ inputName + "\"\r\n\r\n");  
					strBuf.append(inputValue);  
				}  
				out.write(strBuf.toString().getBytes());  
			}  

			// file  
			if (fileMap != null) {  
				Iterator iter = fileMap.entrySet().iterator();  
				while (iter.hasNext()) {  
					Map.Entry entry = (Map.Entry) iter.next();  
					String inputName = (String) entry.getKey();  
					String inputValue = (String) entry.getValue();  
					if (inputValue == null) {  
						continue;  
					}  
					File file = new File(inputValue);  
					String filename = file.getName();  
					String contentType= null; 
					if (contentType == null || contentType.equals("")) {  
						contentType = "application/octet-stream";  
					}  
					StringBuffer strBuf = new StringBuffer();  
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");  
					strBuf.append("Content-Disposition: form-data; name=\""  
							+ inputName + "\"; filename=\"" + filename  + "\"\r\n");  
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");  
					out.write(strBuf.toString().getBytes()); 
					//使用UTF-8编码  
					out.write(EncodingUtil.getBytes(strBuf.toString(), "UTF-8")); //为了解决带中文的文件上传之后不显示名字
					DataInputStream in = new DataInputStream(new FileInputStream(file));  
					int bytes = 0;  
					byte[] bufferOut = new byte[1024];  
					while ((bytes = in.read(bufferOut)) != -1) { 
						out.write(bufferOut, 0, bytes);  
					}  
					in.close();  
				}  
			}  

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
			out.write(endData);  
			out.flush();  
			out.close();  


			StringBuffer strBuf = new StringBuffer();  
			BufferedReader reader = new BufferedReader(new InputStreamReader(  
					conn.getInputStream()));  
			String line = null;  
			while ((line = reader.readLine()) != null) {  
				strBuf.append(line).append("\n");  
			}  
			res = strBuf.toString();  
			reader.close();  
			reader = null;  
		} catch (Exception e) {  
			System.out.println("异常" + urlStr);  
			e.printStackTrace();  
		} finally {  
			if (conn != null) {  
				conn.disconnect();  
				conn = null;  
			}  
		}  
		return res;    
	}  

	private static String getValue(List<ParamsBean> params) {
		String value = "";
		for (int i = 0; i < params.size(); i++) {
			ParamsBean bean = params.get(i);
			value += bean.getKey() + "=" + bean.getValues();
			if (i + 1 != params.size()) {
				value += "&";
			}
		}

		return value;
	}

}
