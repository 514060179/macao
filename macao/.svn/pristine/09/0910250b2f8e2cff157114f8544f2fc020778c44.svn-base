package com.yinghai.macao.app.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.service.MeterService;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.service.WechatService;
import com.yinghai.macao.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

@Controller
@RequestMapping("/wechat")
public class WeChatPayAction {
	@Autowired
	WechatService wechatService;
	@Autowired
	private MeterService meterService;
	@Autowired
	private SpcarOrderService spCarOrderService;
	
	private Logger log = Logger.getLogger(this.getClass());
	

	/**
	 * 回调
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "weixinOrder", method = RequestMethod.POST)
	public String weixinOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String callback = "";
		try {
			String responseStr = parseWeixinCallback(request);
			Map<String, Object> map = XMLUtil.doXMLParse(responseStr);
			System.out.println("回调中微信返回的数据：=================================================以下");
			for (String s : map.keySet()) {
				System.out.println("key:" + s);
				System.out.println("values:" + map.get(s));
			}
			System.out.println("回调中微信返回的数据：=================================================以上");
			// 校验签名 防止数据泄漏导致出现“假通知”，造成资金损失
			if (!WeChatPayUtils.checkIsSignValidFromResponseString(responseStr,(String) map.get("trade_type"))) {
				log.error("微信回调失败,签名可能被篡改");
				callback = XMLUtil.setXML("FAIL", "invalid sign");
				ResponseUtils.renderJson(response, callback);
				return callback;
			}
			if ("FAIL".equalsIgnoreCase(map.get("result_code").toString())) {
				log.error("微信回调失败");
				callback = XMLUtil.setXML("FAIL", "weixin pay fail");
				ResponseUtils.renderJson(response, callback);
				return callback;
			}
			if ("SUCCESS".equalsIgnoreCase(map.get("result_code").toString())) {
				SpcarOrder spCarOrder = spCarOrderService.getSpcarOrderByOrderNo((String)map.get("out_trade_no"));
				if(spCarOrder.getPayStatus()== SpcarOrder.PAY_ALEADY_STATUS){
					callback = XMLUtil.setXML("SUCCESS", "OK");
					ResponseUtils.renderJson(response, callback);
					return callback;
				}
				// 对数据库的操作，新增流水號與修改訂單狀態等信息
				int i = wechatService.createMeterAndUpdateOrder(map,spCarOrder);
				if(i<2){
					log.error("================更新數據庫出錯-------------------");
					callback = XMLUtil.setXML("FAIL", "pay fail");
					ResponseUtils.renderJson(response, callback);
					return callback;
				}
				boolean isOk = true;
				// 告诉微信服务器，我收到信息了，不要在调用回调action了
				if (isOk) {
					callback = XMLUtil.setXML("SUCCESS", "OK");
					ResponseUtils.renderJson(response, callback);
					return callback;
				} else {
					callback = XMLUtil.setXML("FAIL", "pay fail");
					ResponseUtils.renderJson(response, callback);
					return callback;
				}
			}
		} catch (Exception e) {
			log.debug("支付失败" + e.getMessage());
			XMLUtil.setXML("FAIL", "weixin pay server exception");
		}
		XMLUtil.setXML("FAIL", "weixin pay fail");
		return callback;
	}

	/**
	 * 解析微信回调参数
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws JDOMException 
	 * @throws ParseException 
	 */
	private String parseWeixinCallback(HttpServletRequest request) throws IOException, JDOMException, ParseException {
		
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息

		return result;
	}

	

	public static void main(String[] args) throws Exception, Exception {
		String a="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wxd74af2b277375f05]]></appid><mch_id><![CDATA[1380512402]]></mch_id><nonce_str><![CDATA[0nnRMHPBuAODVmZD]]></nonce_str><sign><![CDATA[DF06DFB8D50F6B712818EE986EADEBC5]]></sign><result_code><![CDATA[SUCCESS]]></result_code><out_trade_no><![CDATA[20170425174513]]></out_trade_no><trade_state><![CDATA[NOTPAY]]></trade_state><trade_state_desc><![CDATA[订单未支付]]></trade_state_desc></xml>";
		Map<String, Object> resutlMap = XMLUtil.doXMLParse(a);
		System.out.println(resutlMap);
	}

}
