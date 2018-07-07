package com.tranzvision.gd.util.gh.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.httpclient.HttpClientService;

/**
 * 光华短信发送接口实现类
 * 
 */
@Service
public class SendGhSmsService {

	private String url = "";

	private String username = "";

	private String password = "";

	private String epid = "";

	private String subcode = "";
	
	private String md5pwd = "";

	public SendGhSmsService() {

		Resource resource = new ClassPathResource("conf/ghsms.properties");

		try {
			Properties smsProps = PropertiesLoaderUtils.loadProperties(resource);

			url = smsProps.getProperty("url");

			username = smsProps.getProperty("username");

			password = smsProps.getProperty("password");

			epid = smsProps.getProperty("epid");

			subcode = smsProps.getProperty("subcode");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * 方法名称：getMD5 功 能：字符串MD5加密 参 数：待转换字符串 返 回 值：加密之后字符串
	 */
	public String getMD5(String sourceStr) throws UnsupportedEncodingException {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr.toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 北大短信接口不加密发送
	 * 
	 */
	public Map<String, String> doSendSmsNoMd5(String mobile, String content) {
		Map<String, String> mapRet = new HashMap<String, String>();

		try {
			
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("username", username);
			mapParams.put("password", password);
			mapParams.put("epid", epid);
			if (subcode != null && !"".equals(subcode)) {
				mapParams.put("subcode", subcode);
			}
			
			if(content != null){
				  //短信内容转义;
				  content = content.replace("%", "%25");
			      content = content.replace("+", "%2B");
			      content = content.replace(" ", "%20");
			      content = content.replace("/", "%2F");
			      content = content.replace("?", "%3F");
			      content = content.replace("#", "%23");
			      content = content.replace("&", "%26");
			      content = content.replace("=", "%3D");
			}
			
			mapParams.put("message", content);
			mapParams.put("phone", mobile);

			HttpClientService httpClientService = new HttpClientService();
			httpClientService.setUrl(url);
			httpClientService.setRequestType("GET");
			httpClientService.setParamsMap(mapParams);
			//北大短信用gb2312;
			httpClientService.setCharset("gb2312");

			String rst = httpClientService.sendRequest();

			String msg = "";

			switch (rst) {
			case "00":
				msg = "短信发送成功";
				break;
			case "1":
				msg = "参数不完整";
				break;
			case "2":
				msg = "鉴权失败（包括：用户状态不正常、密码错误、用户不存在、地址验证失败，黑户）";
				break;
			case "3":
				msg = "号码数量超出50条";
				break;
			case "4":
				msg = "发送失败";
				break;
			case "5":
				msg = "余额不足";
				break;
			case "6":
				msg = "发送内容含屏蔽词";
				break;
			case "7":
				msg = "短信内容过长";
				break;
			default:
				msg = "不能识别的错误";
				break;
			}

			mapRet.put("code", rst);
			mapRet.put("msg", msg);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("code", "-9999999");
			mapRet.put("msg", e.getMessage());
		}
		return mapRet;
	}
	
	/**
	 * 短信单发接口
	 * 
	 * @param mobile
	 *            手机号
	 * @param content
	 *            短信内容
	 * @return
	 */
	public Map<String, String> doSendSms(String mobile, String content) {
		Map<String, String> mapRet = new HashMap<String, String>();

		try {
			if ("".equals(md5pwd)) {
				md5pwd = this.getMD5(password);
			}
			
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("username", username);
			mapParams.put("password", md5pwd);
			mapParams.put("epid", epid);
			if (subcode != null && !"".equals(subcode)) {
				mapParams.put("subcode", subcode);
			}
			if(content != null){
				  //短信内容转义;
				  content = content.replace("%", "%25");
			      content = content.replace("+", "%2B");
			      content = content.replace(" ", "%20");
			      content = content.replace("/", "%2F");
			      content = content.replace("?", "%3F");
			      content = content.replace("#", "%23");
			      content = content.replace("&", "%26");
			      content = content.replace("=", "%3D");
			}
			
			mapParams.put("message", content);
			mapParams.put("phone", mobile);

			HttpClientService httpClientService = new HttpClientService();
			httpClientService.setUrl(url);
			httpClientService.setRequestType("GET");
			httpClientService.setParamsMap(mapParams);
			//北大短信用gb2312;
			httpClientService.setCharset("gb2312");

			String rst = httpClientService.sendRequest();

			String msg = "";

			switch (rst) {
			case "00":
				msg = "短信发送成功";
				break;
			case "1":
				msg = "参数不完整";
				break;
			case "2":
				msg = "鉴权失败（包括：用户状态不正常、密码错误、用户不存在、地址验证失败，黑户）";
				break;
			case "3":
				msg = "号码数量超出50条";
				break;
			case "4":
				msg = "发送失败";
				break;
			case "5":
				msg = "余额不足";
				break;
			case "6":
				msg = "发送内容含屏蔽词";
				break;
			case "7":
				msg = "短信内容过长";
				break;
			default:
				msg = "不能识别的错误";
				break;
			}

			mapRet.put("code", rst);
			mapRet.put("msg", msg);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("code", "-9999999");
			mapRet.put("msg", e.getMessage());
		}

		return mapRet;
	}

}