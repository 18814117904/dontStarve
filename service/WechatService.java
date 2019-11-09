package com.zhihui.dontStarve.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zhihui.dontStarve.bean.Customer;
import com.zhihui.dontStarve.bean.JsonResult;
import com.zhihui.dontStarve.config.WechetConfig;
import com.zhihui.dontStarve.constant.UrlType;
import com.zhihui.dontStarve.dao.CustomerMapper;
import com.zhihui.dontStarve.util.WeiXinUtil;


@Service
public class WechatService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	RestTemplate restTemplate;
	@Resource
	WechetConfig wxConfig;
	@Resource
	CustomerMapper customerMapper;
	
	
	//根据code获得openid和accesstoken
	public JsonResult getOpenidByCode(String code) {
		String url = UrlType.OAuth2_ACCESS_TOKEN_URL.replaceAll("APPID", wxConfig.getAppId()).replaceAll("SECRET", wxConfig.getSecret()).replaceAll("CODE", code);
		ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
		if(response==null){
			return new JsonResult(false,"微信服务器返回空",null);
		}
		if(!response.getStatusCode().equals(200)){
			return new JsonResult(true,"正常",JSONObject.fromObject(response.getBody()));
		}else{
			return new JsonResult(false,"微信服务器访问异常",JSONObject.fromObject(response.getBody()));
		}
	}

	public JsonResult getUserInfoByAccessToken(String access_token) {
		String url = UrlType.OAuth2_USER_INFO_URL.replaceAll("APPID", wxConfig.getAppId()).replaceAll("ACCESS_TOKEN", access_token);
		ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
		if(response==null){
			return new JsonResult(false,"微信服务器返回空",null);
		}
		if(!response.getStatusCode().equals(200)){
			return new JsonResult(true,"正常",JSONObject.fromObject(response.getBody()));
		}else{
			return new JsonResult(false,"微信服务器访问异常",JSONObject.fromObject(response.getBody()));
		}
	}
	
	
	//从微信服务器获得用户信息
	public Customer getCustomerBycode(String code) {
		//获得openid和accesstoken
		JsonResult responseResult = getOpenidByCode(code);
		if(!responseResult.getSuccess()){
			return null;
		}
		String access_token = (String) ((Map<String, Object>)responseResult.getData()).get("access_token");
		String openid = (String) ((Map<String, Object>)responseResult.getData()).get("openid");
		
		//openid查询数据库信息
		Customer customer = customerMapper.selectByOpenId(openid);
		//判断数据库是否查询的到该用户信
		if(customer==null){
			customer = insertInfoFromWechat(access_token);//添加用户
		}
		return customer;
	}
//	
//	private Customer updateInfoFromWechat(Integer integer, String access_token){
//		//通过accesstoken，从微信服务器获得用户信息
//		JsonResult response2=getUserInfoByAccessToken(access_token);
//		//用户信息存入数据库
//		Customer customer;
//		if(response2.getSuccess()==true){
//			Map<String, Object> map = ((Map<String, Object>)response2.getData());
//			map.put("id",integer);//从数据库中获取
//			//Todo 设置id
//			map.put("sex",map.get("sex").equals(1)?"男":"女");
////			map.put("state","1");//状态固定设为1正常
//			
//			customer = new Customer();
//			customer.setId((Integer) map.get("id"));
//			customer.setOpenid((String) map.get("openid"));
//			customer.setPhone((String) map.get("phone"));
//			String nickname =  filterEmoji((String)map.get("nickname"));
//			customer.setNickname(nickname);
//			customer.setPassword((String) map.get("password"));
//			customer.setSex((String) map.get("sex"));
//			customer.setCountry((String) map.get("country"));
//			customer.setProvince((String) map.get("province"));
//			customer.setCity((String) map.get("city"));
//			customer.setHeadimgurl((String) map.get("headimgurl"));
//			customer.setPrivilege( map.get("privilege").toString());
//			customer.setState((String) map.get("state"));
////			customer.setRegisterTime(new Date());//更新不需要这个字段
//			customer.setUpdateTime(new Date());
//			/*更新数据库客户客户信息*/
////			restTemplate.postForObject(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective", customer,JsonResult.class );
//			myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",ParseUtil.paramsToMap(customer));
//			/*更新数据库客户客户信息*/
//		}else{
//			logger.info("JsonResult response2=getUserInfoByAccessToken(access_token);==false");
//			return null;
//		}
//		return customer;
//	}
	private Customer insertInfoFromWechat(String access_token){
		//通过accesstoken，从微信服务器获得用户信息
		JsonResult response2=getUserInfoByAccessToken(access_token);
		//用户信息存入数据库
		Customer customer;
		if(response2.getSuccess()==true){
			Map<String, Object> map = ((Map<String, Object>)response2.getData());
//			map.put("id",1);//自增
			//Todo 设置id
			map.put("sex",map.get("sex").equals(1)?"男":"女");
			map.put("state","1");//状态固定设为1正常
			
			customer = new Customer();
//			customer.setId((Integer) map.get("id"));//自增
			customer.setOpenid((String) map.get("openid"));
			customer.setPhone((String) map.get("phone"));
			String nickname =  WeiXinUtil.filterEmoji((String)map.get("nickname"));
			customer.setNickname(nickname);
			customer.setPassword((String) map.get("password"));
			customer.setSex((String) map.get("sex"));
			customer.setCountry((String) map.get("country"));
			customer.setProvince((String) map.get("province"));
			customer.setCity((String) map.get("city"));
			customer.setHeadimgurl((String) map.get("headimgurl"));
			customer.setPrivilege( map.get("privilege").toString());
			customer.setState((String) map.get("state"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			customer.setRegisterTime(new Date());//更新不需要这个字段
			customer.setUpdateTime(new Date());
			/*更新数据库客户客户信息*/
			int id = 0;
			id = customerMapper.insertSelective(customer);
			/*更新数据库客户客户信息*/
			if(id!=0){//插入返回了用户id，把用户id设置到customer中
				customer.setId(id);
			}
		}else{
			return null;
		}
		return customer;
	}

//	public JsonResult mergeRemoveCustomer(Integer id, Customer customer) {
////		ResponseEntity<String> result1 = restTemplate.postForEntity(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",customer , String.class);
//		JsonResult result1 = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",ParseUtil.paramsToMap(customer));
//		System.out.println(result1);
////		JsonResult jsonResult = restTemplate.postForObject(RouteConfig.GOGIRLUSER+"gogirl_user/mergeRemoveCustomer/"+id,null , JsonResult.class);
//		JsonResult jsonResult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/mergeRemoveCustomer/"+id,null);
//		return jsonResult;
//	}
//	public boolean  greaterThan30Day(String updateTime) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			Date d = sdf.parse(updateTime);
//			Long test = new Date().getTime()-d.getTime();
//			Long day30 = new Long("2592000000");//30天的毫秒数
//			if(test.compareTo(day30)>0){
//				return true;
//			}else{
//				return false;
//			}
//		} catch (ParseException e) {
//			System.out.println("*******************************************************");
//			e.printStackTrace();
//			return false;
//		}
//	}
	
//	public String getAUrl(String redirect_uri,String state) {
//		try {
//			redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
//			state = URLEncoder.encode(state, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		String url = WxUrlType.OAUTH2_AUTHORIZE_URL.replaceAll("APPID", wxConfig.getAppId())
//			.replaceAll("REDIRECT_URI", redirect_uri).replaceAll("SCOPE", "snsapi_userinfo").replaceAll("STATE", state);
//		return url;
//	}


}
