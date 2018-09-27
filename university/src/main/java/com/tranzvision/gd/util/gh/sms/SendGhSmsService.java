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

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.dysmsapi.transform.v20170525.SendSmsResponseUnmarshaller;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
	
	
	/**
	 * 短信单发接口 阿里云
	 * 
	 * @param mobile
	 *            手机号
	 * @param content
	 *            短信内容
	 * @return
	 */
	public Map<String, String> doSendSmsForAli(String mobile, String content) {
		Map<String, String> mapRet = new HashMap<String, String>();

		//设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//初始化ascClient需要的几个参数
		final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
		//替换成你的AK
		final String accessKeyId = "yourAccessKeyId";//你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = "yourAccessKeySecret";//你的accessKeySecret，参考本文档步骤2
		//初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
		accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		 //组装请求对象
		 SendSmsRequest request = new SendSmsRequest();
		 //使用post提交
		 request.setMethod(MethodType.POST);
		 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
		 request.setPhoneNumbers("1500000000");
		 //必填:短信签名-可在短信控制台中找到
		 request.setSignName("云通信");
		 //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
		 request.setTemplateCode("SMS_1000000");
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		 request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
		 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		 //request.setSmsUpExtendCode("90997");
		 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		 request.setOutId("yourOutId");
		//请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = null;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
		//请求成功
		}

		return mapRet;
	}

}