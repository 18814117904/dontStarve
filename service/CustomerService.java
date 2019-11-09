package com.zhihui.dontStarve.service;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zhihui.dontStarve.bean.AccessToken;
import com.zhihui.dontStarve.bean.Customer;
import com.zhihui.dontStarve.constant.UrlType;
import com.zhihui.dontStarve.dao.CustomerMapper;
import com.zhihui.dontStarve.util.WeiXinUtil;

@Service
public class CustomerService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	CustomerMapper customerMapper;
	@Autowired
	RestTemplate restTemplate;
	@Resource
	AccessTokenService accessTokenService;
	public Customer getUserInfoByOpenid(String openid) {
		AccessToken accessToken = accessTokenService.getAccess_Token();
		String url = UrlType.USER_INFO_URL.replaceAll("ACCESS_TOKEN", accessToken.getAccess_token()).replaceAll("OPENID", openid);
		ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
		if(response==null){
			logger.info("微信服务器返回空");
			return null;
		}
		if(!response.getStatusCode().equals(200)){
			Customer customer = new Customer();
			JSONObject map = JSONObject.fromObject(response.getBody());
			customer.setId((Integer) map.get("id"));
			customer.setOpenid(String.valueOf( map.get("openid")));
			customer.setPhone(String.valueOf( map.get("phone")));
			String nickname =  WeiXinUtil.filterEmoji((String)map.get("nickname"));
			customer.setNickname(nickname);
			customer.setPassword(String.valueOf( map.get("password")));
			customer.setSex(String.valueOf(map.get("sex")));
			customer.setCountry(String.valueOf( map.get("country")));
			customer.setProvince(String.valueOf( map.get("province")));
			customer.setCity(String.valueOf( map.get("city")));
			customer.setHeadimgurl(String.valueOf( map.get("headimgurl")));
			customer.setPrivilege( String.valueOf(map.get("privilege")));
			customer.setState(String.valueOf( map.get("state")));

			customer.setRegisterTime(new Date());//更新不需要这个字段
			customer.setUpdateTime(new Date());
			customerMapper.insertSelective(customer);
			return customer;
		}else{
			logger.info("微信服务器访问异常");
			return null;
		}
	}
	
}
