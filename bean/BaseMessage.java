package com.zhihui.dontStarve.bean;

/**
 * 
 * 类名称: BaseMessage
 * 类描述: 回复消息的基类
 * @author yuanjun
 * 创建时间:2017年12月8日上午11:38:11
 */
public class BaseMessage {

	protected String ToUserName;
	protected String FromUserName;
	protected long CreateTime;
	protected String MsgType;

	public BaseMessage() {
		super();
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

}
