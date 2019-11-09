package com.zhihui.dontStarve.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





















import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zhihui.dontStarve.bean.Customer;
import com.zhihui.dontStarve.bean.ImageMessage;
import com.zhihui.dontStarve.service.AccessTokenService;
import com.zhihui.dontStarve.service.CustomerService;
import com.zhihui.dontStarve.service.CustomserviceService;
import com.zhihui.dontStarve.service.QrCodeService;
import com.zhihui.dontStarve.service.WechatService;
import com.zhihui.dontStarve.util.CheckUtil;
import com.zhihui.dontStarve.util.Message.ImageMessageUtil;
import com.zhihui.dontStarve.util.Message.MessageUtil;
import com.zhihui.dontStarve.util.Message.TextMessageUtil;




/**
 * 
 * 类名称: LoginController
 * 类描述: 与微信对接登陆验证
 * @author yuanjun
 * 创建时间:2017年12月5日上午10:52:13
 */
@Controller
public class LoginController {
	@Resource
	AccessTokenService accessTokenService;
	@Resource
	WechatService wechatService;
	@Resource
	CustomerService customerService;
	@Resource
	QrCodeService qrCodeService;
	@Resource
	CustomserviceService customserviceService;
	
	@RequestMapping(value = "/wxdemo",method=RequestMethod.GET)
	public void login(HttpServletRequest request,HttpServletResponse response){
		System.out.println("success");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		PrintWriter out = null;
		try {
			  out = response.getWriter();
			if(CheckUtil.checkSignature(signature, timestamp, nonce)){
				out.write(echostr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	@RequestMapping(value = "/wxdemo",method=RequestMethod.POST)
	public void dopost(HttpServletRequest request,
			HttpServletResponse response,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("openid") String openid,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature
			){
		//检查入参
		if (!CheckUtil.checkSignature(signature, timestamp, nonce)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }


		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		//将微信请求xml转为map格式，获取所需的参数
		Map<String,String> map = MessageUtil.xmlToMap(request);
		String ToUserName = map.get("ToUserName");
		String FromUserName = map.get("FromUserName");
		String MsgType = map.get("MsgType");
		String Content = map.get("Content");
		String Event = map.get("Event");
		String EventKey = map.get("EventKey");
		List<String> messageList = new ArrayList<String>();
		String message = null;
		if("event".equals(MsgType)){
			if("subscribe".equals(Event)){
				
				Customer customer = customerService.getUserInfoByOpenid(FromUserName);
				if(customer!=null&&customer.getNickname()!=null){
					String nickname  = customer.getNickname();
					TextMessageUtil textMessage = new TextMessageUtil();
					message = textMessage.initMessage(FromUserName, ToUserName,"欢迎"+nickname+"关注公众号");
//					messageList.add(message);
					
					//二维码
					int id  = customer.getId();
					String localUrl = qrCodeService.getLocalUrlQrCodeUrlByOpenid(id,FromUserName);
					ImageMessageUtil imageMessage = new ImageMessageUtil();
					String accessToken = accessTokenService.getAccess_Token().getAccess_token();
					String mediaId = imageMessage.getmediaId(localUrl ,accessToken);
//					message = imageMessage.initMessage(FromUserName, ToUserName,mediaId);
//					messageList.add(message);
					
					customserviceService.sendMsg(FromUserName,accessToken,"",mediaId);

				}else{
					TextMessageUtil textMessage = new TextMessageUtil();
					message = textMessage.initMessage(FromUserName, ToUserName,"欢迎关注公众号");
				}
				
				
			}else if("unsubscribe".equals(Event)){
				System.out.println(FromUserName+"取消了关注");
				TextMessageUtil textMessage = new TextMessageUtil();
				message = textMessage.initMessage(FromUserName, ToUserName,"再见");
			}
		}else 
		//处理文本类型，实现输入1，回复相应的封装的内容
		if("text".equals(MsgType)){
			if("图片".equals(Content)){
				ImageMessageUtil util = new ImageMessageUtil();
				message = util.initMessage(FromUserName, ToUserName);
			}else if("1".equals(Content)){
				TextMessageUtil textMessage = new TextMessageUtil();
				message = textMessage.initMessage(FromUserName, ToUserName);
				
			}
//			else if(Content.startsWith("翻译")){
//				//设置翻译的规则，以翻译开头	
//				TextMessageUtil textMessage = new TextMessageUtil();
//				//将翻译开头置为""
//				String query = Content.replaceAll("^翻译", "");
//				if(query==""){
//				//如果查询字段为空，调出使用指南
//				message = textMessage.initMessage(FromUserName, ToUserName);
//				}else{
//						message = textMessage.initMessage(FromUserName, ToUserName,TransApi.getTransResult(query));
//					}
//				}
			else{
				TextMessageUtil textMessage = new TextMessageUtil();
				message = textMessage.initMessage(FromUserName, ToUserName,"您输入的内容是:"+Content);
			}
		}
		try {
			out = response.getWriter();
//			if(messageList!=null&&messageList.size()>0){
//				for(int i=0;i<messageList.size();i++){
//					String messageItem = messageList.get(i);
//					out.write(messageItem);
//				}
//			}else{
				out.write(message);
//			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
	}
}
