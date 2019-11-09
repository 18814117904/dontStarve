package com.zhihui.dontStarve.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zhihui.dontStarve.bean.JsonResult;
import com.zhihui.dontStarve.constant.UrlType;

@Service
public class CustomserviceService {
	@Resource
	RestTemplate restTemplate;
	
	public void sendMsg(String openid,String access_token,String type,String mediaid) {
		String url = UrlType.CUSTOM_SERVICE_SEND_URL.replaceAll("ACCESS_TOKEN", access_token);
		
		Map<String, Object> image = new HashMap<String, Object>();
		image.put("media_id", mediaid);
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("touser", openid);
		map.put("msgtype", "image");
		map.put("image", image);
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, map,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
//		if(response==null){
//			return new JsonResult(false,"微信服务器返回空",null);
//		}
//		if(!response.getStatusCode().equals(200)){
//			return new JsonResult(true,"正常",JSONObject.fromObject(response.getBody()));
//		}else{
//			return new JsonResult(false,"微信服务器访问异常",JSONObject.fromObject(response.getBody()));
//		}
		
	}
}
