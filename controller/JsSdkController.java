package com.zhihui.dontStarve.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhihui.dontStarve.bean.AccessToken;
import com.zhihui.dontStarve.bean.JsApiTicket;
import com.zhihui.dontStarve.bean.Result;
import com.zhihui.dontStarve.config.WechetConfig;
import com.zhihui.dontStarve.service.AccessTokenService;
import com.zhihui.dontStarve.service.JsApiTicketService;
import com.zhihui.dontStarve.util.SignatureUtil;
import com.zhihui.dontStarve.util.WeiXinUtil;

@Controller
public class JsSdkController {
	@Resource
	WechetConfig config;
	@Resource
	JsApiTicketService jsApiTicketService;
	
	
	@ResponseBody
	@RequestMapping(value = "js_sdk_sign")
	public Result js_sdk_sign (
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(name="url",required=false) String url
			){
	        String origin = request.getHeader("Origin");
//	        if(request.getHeader("Origin").contains("gogirl.cn")) {//部分网站允许跨域
//		        response.setHeader("Access-Control-Allow-Origin", origin);
//		        response.setHeader("Access-Control-Allow-Credentials", "true");	       
//	        }
	        response.setHeader("Access-Control-Allow-Origin", origin);
	        response.setHeader("Access-Control-Allow-Credentials", "true");	       
		try {
			//获取jsapi_ticket
			JsApiTicket jsApiTicket = jsApiTicketService.getJsApiTicket();
			String timestamp = WeiXinUtil.create_timestamp();//时间搓
			String nonceStr = WeiXinUtil.create_nonce_str();//随机字符串
			//获取签名
			String signature = SignatureUtil.getSignature(jsApiTicket.getJs_api_ticket(), nonceStr, timestamp, url);
			System.out.println("签名为："+signature);
			if(signature==null){
				return new Result(false,"签名失败，请检查入参。",null);
			}
			String appId = config.getAppId();
			Map<String, Object> map = new HashMap<>();
			map.put("timestamp",timestamp );
			map.put("nonceStr",nonceStr );
			map.put("appId", appId);
			map.put("signature", signature);
			map.put("jsapi_ticket", jsApiTicket.getJs_api_ticket());
			return new Result(true,"",map);
		} catch (Exception e) {
			return new Result(false,e.getMessage(),null);
		}
	}
}
