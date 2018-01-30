package com.yinghai.macao.common.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import com.yinghai.macao.common.model.SpcarOrder;
import org.jdom2.JDOMException;



public interface WechatService {

	/**
	 * request AccessToken 参数 code
	 */
	Map<String, Object> getOpenIdAccessToken(String code);

	/**
	 * request 微信用户个人信息 参数 acccessToken、 openid
	 */
	Map<String, Object> getWeChatInfo(String acccessToken, String openid);
	
	/**
	 * 对数据库的操作，新增流水號與修改訂單狀態等信息
	 * @param map 威信回調返回的信息
	 * @param spCarOrder 訂單信息
	 * @return
	 */
	int createMeterAndUpdateOrder(Map map,
								  SpcarOrder spCarOrder)throws ParseException ;

}
