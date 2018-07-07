package com.tranzvision.gd.ztest.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.util.gh.sms.SendGhSmsService;

@Controller
@RequestMapping(value = "/")
public class TestSendSmsController {
	@Autowired
	private SendGhSmsService sendSmsService;
	
	@RequestMapping(value="sendGhSms", produces="text/html;charset=UTF-8")
	@ResponseBody
	public String sendGhSms(HttpServletRequest request, HttpServletResponse response){

		Map<String, String> map = sendSmsService.doSendSms("18013565866", "测试短信发送内容【北大光华】");
		
		System.out.println(map.get("code")+"===============>"+map.get("msg"));
		return map.get("code")+"===============>"+map.get("msg");
	}
}
