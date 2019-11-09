package com.zhihui.dontStarve.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.zhihui.dontStarve.config.WechetConfig;

import redis.clients.jedis.Jedis;


/**
 * 
 * 类名称: WeiXinUtil
 * 类描述:
 * @author yuanjun
 * 创建时间:2017年12月8日下午4:38:42
 */
@Component
public class WeiXinUtil {
	@Resource
	WechetConfig config;
//	/**
//	 * 开发者id
//	 */
//	private static final String APPID = "wxabd5e4915643494a";
//	/**
//	 * 开发者秘钥
//	 */
//	private static final String APPSECRET="4b83a8ba046e09281a670b85fa6de88a";
//	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?"
//			+ "grant_type=client_credential&appid=APPID&secret=APPSECRET";
	/**
	 * 处理doget请求
	 * @param url
	 * @return
	 */
	public static JSONObject doGetstr(String url){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity!=null){
				String result = EntityUtils.toString(entity);
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
		
	}
	/**
	 * 处理post请求
	 * @param url
	 * @return
	 */
	public static JSONObject doPoststr(String url,String outStr){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr, "utf-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(),"utf-8");
		    jsonObject =JSONObject.fromObject(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}


	public static String create_nonce_str() {
        return UUID.randomUUID().toString().replaceAll("-",	"");
    }
 
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    /**
     * 将emoji表情替换成空串
     *  
     * @param source
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
     if (source != null && source.length() > 0) {
      return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
     } else {
      return source;
     }
    }
}
