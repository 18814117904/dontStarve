package com.zhihui.dontStarve.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import com.zhihui.dontStarve.bean.JsonResult;
import com.zhihui.dontStarve.constant.UrlType;
import com.zhihui.dontStarve.util.qrcode.QRCodeUtils;


@Service
public class QrCodeService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${dontStarve.path.PICTURE}")
	String PICTURE;
	@Value("${dontStarve.url}")
	String DONTSTARVEURL;
	@Autowired
	RestTemplate restTemplate;
	@Resource
	AccessTokenService accessTokenService;
	
	public String getLocalUrlQrCodeUrlByOpenid(int id,String openid) {
		String accesstoken = accessTokenService.getAccess_Token().getAccess_token();
		String url = UrlType.OPENID_QRCODE_URL.replaceAll("TOKEN", accesstoken);
		
//		{"": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
		Map<String, Object> scene = new HashMap<>();
		scene.put("scene_id", String.valueOf(id));
		
		Map<String, Object> action_info = new HashMap<>();
		action_info.put("scene", scene);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("expire_seconds", String.valueOf(604800));
		map.put("action_name", "QR_SCENE");
		map.put("action_info",action_info );
		
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, map,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
		if(response==null){
			logger.info("微信服务器返回空");
			return null;
		}
		if(response.getStatusCodeValue()!=200){
			logger.info("微信服务器访问异常");
			return null;
		}
		JSONObject jsonobject = JSONObject.fromObject(response.getBody());
//		String ticket = (String) jsonobject.get("ticket");
		String imgUrlWx = (String) jsonobject.get("url");
		//把二维码文件下载到本地
//		
//		String showqrcode = UrlType.SHOWQRCODE_URL.replaceAll("TICKET", ticket);
//		ResponseEntity<File> qrcodeResponse = restTemplate.postForEntity(showqrcode, null,File.class );
//		if(qrcodeResponse==null){
//			logger.info("微信服务器返回空");
//			return null;
//		}
//		if(qrcodeResponse.getStatusCodeValue()!=200){
//			logger.info("微信服务器访问异常");
//			return null;		
//		}
//		File file = qrcodeResponse.getBody();
//		
		
		String localUrl = createQrCode(imgUrlWx,openid+".png");
		return localUrl;
	}
	public String createQrCode(String url,String QRCodeName) {
		//		String member_id = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxabd5e4915643494a&redirect_uri=http%3A%2F%2F192.168.43.244%2Fauthorized_user_info&response_type=code&scope=snsapi_userinfo&state=fromtest#wechat_redirect";
//        String QRCodeName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";//定义二维码文件名，采用uuid命名，存储格式为png
//      String imgPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"img/";
//        String imgPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String imgPath = PICTURE;
        if(imgPath.startsWith("file://")){//linux取文件路径
        	imgPath = imgPath.substring(7, imgPath.length());
        }else{//windows开发环境保存在classpath
        	imgPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"img/";
        }
        logger.info(imgPath);
        File file = new File(imgPath);
        if (!file.exists()) {
        	file.mkdirs();
        }
        
        QRCodeUtils handler = new QRCodeUtils();
        handler.encoderQRCode(url, imgPath+QRCodeName, "png",10);//最后一个参数为二维码大小
        return imgPath+QRCodeName;
	}
}
