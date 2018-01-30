package com.yinghai.macao.common.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.dao.MeterMapper;
import com.yinghai.macao.common.dao.SpcarOrderMapper;
import com.yinghai.macao.common.model.Meter;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.service.MeterService;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.service.WechatService;
import com.yinghai.macao.common.util.EmojiUtil;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.ThirdPartyUtils;
import com.yinghai.macao.common.util.TransformUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import net.sf.json.JSONObject;

@Service
public class WechatServiceImpl implements WechatService {


	@Autowired
	private MeterMapper meterMapper;
	@Resource
	private SpcarOrderMapper spcarOrderMapper;
    @Autowired
    private SpcarOrderService spcarOrderService;

	private Logger log = Logger.getLogger(this.getClass());

	public Map<String, Object> getOpenIdAccessToken(String code) {
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ ThirdPartyUtils.getAppid() + "&secret="
				+ ThirdPartyUtils.getAppsecret() + "&code=" + code
				+ "&grant_type=authorization_code";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			URL url = new URL(URL);
			URLConnection urlcon = url.openConnection();
			InputStream is = urlcon.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			StringBuffer bs = new StringBuffer();
			String l = null;
			while ((l = buffer.readLine()) != null) {
				bs.append(l);
			}
			JSONObject json = JSONObject.fromObject(bs.toString());
			map.put("accessToken", (String) json.getString("access_token"));
			map.put("openid", (String) json.getString("openid"));
		} catch (IOException e) {
			log.error("get wechat openid fail" + e);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * request 微信用户个人信息
	 * 参数 acccessToken、 openid
	 */
	public Map<String, Object> getWeChatInfo(String acccessToken, String openid) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		String URL = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ acccessToken + "&openid=" + openid;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			URL url = new URL(URL);
			URLConnection urlcon = url.openConnection();
			InputStream is = urlcon.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			StringBuffer bs = new StringBuffer();
			String l = null;
			while ((l = buffer.readLine()) != null) {
				bs.append(l);
			}
			JSONObject json = JSONObject.fromObject(bs.toString());
			map.put("nickname",
					EmojiUtil.filterEmoji((String) json.getString("nickname"))); // 昵称
			map.put("headimgurl", (String) json.getString("headimgurl")); // 头像
			map.put("sex", TransformUtils.toString(json.getString("sex"))); // 性别1:男，2:女
			map.put("province", TransformUtils.toString(json.getString("province"))); 
			map.put("city", TransformUtils.toString(json.getString("city"))); 
			map.put("country", TransformUtils.toString(json.getString("country"))); 
			map.put("unionid", TransformUtils.toString(json.getString("unionid"))); 
		} catch (IOException e) {
			e.printStackTrace();
			log.error("get wechat info fail" + e);
			throw  new RuntimeException();
		}
		return map;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public int createMeterAndUpdateOrder(Map map, SpcarOrder spCarOrder
										 ) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyyMMddHHmmss" ); 
		int i=0;
		String outTradeNo = (String) map.get("out_trade_no");
		String transactionId = (String) map.get("transaction_id");
		String totlaFee = (String) map.get("total_fee");
		Integer totalPrice = Integer.valueOf(totlaFee);
		Meter meter = new Meter();
		meter.setCreateTime(new Date());
		meter.setOrderNo(outTradeNo);
		meter.setPayNo(transactionId);
		meter.setPayMoney(totalPrice);
		try {
			meterMapper.insertSelective(meter);
			i++;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("create meter fail" + e);
			throw  new RuntimeException();
			// TODO: handle exception
		}
		SpcarOrder updateSpcarOrder = new SpcarOrder();
		if(spCarOrder.getOrderId()!=null&&spCarOrder.getOrderId()!=0){
			updateSpcarOrder.setStatus(SpcarOrder.ORDER_RENEW_STATUS);
		}else{
			updateSpcarOrder.setStatus(SpcarOrder.ORDER_PAIRING_STATUS);
		}
		updateSpcarOrder.setSpcarOrderId(spCarOrder.getSpcarOrderId());
		updateSpcarOrder.setPayStatus(SpcarOrder.PAY_ALEADY_STATUS);
		//返回的参数类型为APP时,为APP支付，否则为公众号支付
		if(StringUtil.notEmpty((String)map.get("trade_type"))&&((String)map.get("trade_type")).equals(Constant.weixinAPPPayType)){
			updateSpcarOrder.setPayWayCode(SpcarOrder.PAY_WAY_WEIXINAPP);
		}else{
			updateSpcarOrder.setPayWayCode(SpcarOrder.PAY_WAY_WEIXINJSAPI);
		}
		updateSpcarOrder.setPayTime(sdf.parse((String)map.get("time_end")));
		try {
			spcarOrderMapper.updateByPrimaryKeySelective(updateSpcarOrder);
			i++;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("update spCarOrder fail" + e);
			throw  new RuntimeException();
		}
		//当该订单是原单时，查出他说对应的续单也进行退款
//		if(StringUtil.empty(spCarOrder.getOrderId()+"")){
//			spcarOrderService.timer(spCarOrder);
//			}
       
		return i;
	}
	
	

}
