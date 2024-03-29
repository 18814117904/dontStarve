package com.zhihui.dontStarve.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zhihui.dontStarve.bean.AccessToken;
import com.zhihui.dontStarve.bean.JsApiTicket;
import com.zhihui.dontStarve.constant.UrlType;

@Configuration          //证明这个类是一个配置文件
@EnableScheduling       //打开quartz定时器总开关

@Service
public class JsApiTicketService {
	@Resource
	JsApiTicket jsApiTicket;
	@Resource
	AccessTokenService accessTokenService;
	public JsApiTicket getJsApiTicket() {
		if(jsApiTicket!=null&&jsApiTicket.getJs_api_ticket()!=null){
			return jsApiTicket;
		}
		JsApiTicket jsApiTicket = getJsApiTicket(accessTokenService.getAccess_Token());
		this.jsApiTicket = jsApiTicket;
		return jsApiTicket;
	}
	public JsApiTicket getJsApiTicket(AccessToken accessToken) {
		JsApiTicket jsApiTicket = new JsApiTicket();
		// 访问微信服务器
		String url = UrlType.JSAPI_TICKET_URL.replaceAll("ACCESS_TOKEN", accessToken.getAccess_token());
		try {
			URL getUrl = new URL(url);
			HttpURLConnection http = (HttpURLConnection) getUrl
					.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);

			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] b = new byte[size];
			is.read(b);

			String message = new String(b, "UTF-8");

			JSONObject json = JSONObject.fromObject(message);

			jsApiTicket.setJs_api_ticket(json.getString("ticket"));
			jsApiTicket.setExpires_in(new Integer(json.getString("expires_in")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsApiTicket;
	}

//    @Scheduled(cron = "0/2 * * * * *")
    @Scheduled(cron = "0 0 0/2 * * *")
	public void refreshJsApiTicket() {
        //获取当前时间
        LocalDateTime localDateTime =LocalDateTime.now();
        System.out.println("当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		//刷新jsApiTicket
		JsApiTicket jsApiTicket = getJsApiTicket(accessTokenService.getAccess_Token());
		this.jsApiTicket = jsApiTicket;
        System.out.println("刷新jsApiTicket为:"+jsApiTicket.getJs_api_ticket());
	}
	
	
}
