package com.zhihui.dontStarve.util.Message;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import com.thoughtworks.xstream.XStream;
import com.zhihui.dontStarve.bean.Image;
import com.zhihui.dontStarve.bean.ImageMessage;
import com.zhihui.dontStarve.util.BaseMessageUtil;
import com.zhihui.dontStarve.util.UploadUtil;

public class ImageMessageUtil implements BaseMessageUtil<ImageMessage> {
	/**
	 * 将信息转为xml格式
	 */
	public String messageToxml(ImageMessage imageMessage) {
		XStream xtream = new XStream();
		xtream.alias("xml", imageMessage.getClass());
		xtream.alias("Image", new Image().getClass());
		return xtream.toXML(imageMessage);
	}
	/**
	 * 封装信息
	 */
	public String initMessage(String FromUserName, String ToUserName,String mediaId) {
		Image image = new Image();
		image.setMediaId(mediaId);
		ImageMessage message = new ImageMessage();
		message.setFromUserName(ToUserName);
		message.setToUserName(FromUserName);
		message.setCreateTime(new Date().getTime());
		message.setImage(image);
		message.setMsgType("image");
		return messageToxml(message);
	}
	
	public String initMessage(String FromUserName, String ToUserName ) {
		Image image = new Image();
//		image.setMediaId(getmediaId(path ,accessToken));
		ImageMessage message = new ImageMessage();
		message.setFromUserName(ToUserName);
		message.setToUserName(FromUserName);
		message.setCreateTime(new Date().getTime());
		message.setImage(image);
		message.setMsgType("image");
		return messageToxml(message);
	}
	/**
	 * 获取Media
	 * @return
	 */
	public String getmediaId(String path ,String accessToken){
		String mediaId = null;
		try {
			mediaId = UploadUtil.upload(path, accessToken, "image");
			
		} catch (KeyManagementException | NoSuchAlgorithmException
				| NoSuchProviderException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mediaId;
	}
}
