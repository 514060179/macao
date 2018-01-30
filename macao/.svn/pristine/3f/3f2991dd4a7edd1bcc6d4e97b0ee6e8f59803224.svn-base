/*
package com.yinghai.macao.backstage.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.constant.SpcarPassengerPushCode;
import com.yinghai.macao.common.model.Meter;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.service.SpcarPassengerService;
import com.yinghai.macao.common.service.WechatService;
import com.yinghai.macao.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/admin/spcar")
public class SpcarOrderManageAction {
    @Autowired
    private SpcarOrderService spCarOrderService;

    @Autowired
    private WechatService wechat;
    
    @Autowired
    SpcarPassengerService spcarPassengerService;

    private Logger log = Logger.getLogger(this.getClass());

   

  
    */
/**
 	 * 退款操作
 	 * 
 	 * @param request
 	 * @param model
 	 * @throws Exception
 	 *//*

 	@RequestMapping("refundFee")
 	public void refundFee(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
 		String orderNo = request.getParameter("orderNo");
 		String totalFee = request.getParameter("totalFee");
 		String refundFee = request.getParameter("refundFee");
 		JSONObject responseObject = new JSONObject();
		   String payType = request.getParameter("payType");
	        if (StringUtil.empty(payType)) {
	        	payType = "APP";
	        }
 		JsonConfig config = new JsonConfig();
 		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
 		Map map = WeChatPayUtils.orderrefund("20170503094953", "1", "1",payType);
 		if (map != null && "SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
 			// 更新数据库并创建流水账单
 			SpcarOrder spCarOrder = spCarOrderService.getSpcarOrderByOrderNo(orderNo);
 			SpcarOrder updateCarOrder = new SpcarOrder();
 			updateCarOrder.setSpcarOrderId(spCarOrder.getSpcarOrderId());
 			updateCarOrder.setPayStatus(SpcarOrder.PAY_REFUNDED_STATUS);
 			spCarOrder.setPayStatus(SpcarOrder.PAY_REFUNDED_STATUS);
 			Meter meter = new Meter();
 			meter.setCreateTime(new Date());
 			meter.setOrderNo(orderNo);
 			meter.setPayMoney(TransformUtils.toInt(refundFee) * (-1));
 			meter.setPayNo((String) map.get("refund_id"));
 			int i = spCarOrderService.updateOrderAndCreateMeter(updateCarOrder, meter,true);
 			if(i<3){
 				responseObject.put("msg", "false ");
 	 			responseObject.put("code", "1");
 	 			responseObject.put("data", new JSONObject());
 	 			ResponseUtils.renderJson(response, responseObject.toString());
 	 			return;
 			}
 			responseObject.put("msg", "success ");
 			responseObject.put("code", "1");
 			responseObject.put("data", new JSONObject());
 			ResponseUtils.renderJson(response, responseObject.toString());
 			return;
 		}
 		log.debug("=============================掉微信退款接口失败======="+new Date());
 		responseObject.put("msg", "fail ");
 		responseObject.put("code", "101");
 		responseObject.put("data", new JSONObject());
 		ResponseUtils.renderJson(response, responseObject.toString());
 		return;

 	}


	
	*/
/**
	 * 派单并将订单给司机并且送信息给专车乘客
	 * 
	 * @param request
	 * @param model
	 * @throws Exception
	 *//*

	@RequestMapping("confirmOrder")
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		String orderId = request.getParameter("orderId");
		String driverId = request.getParameter("driverId");
		String carId = request.getParameter("carId");
		JSONObject responseObject = new JSONObject();

		SpcarOrder spCarOrder = spCarOrderService.getSpcarOrderbyId(TransformUtils.toInt(orderId));
		spCarOrder.setDriverId(TransformUtils.toInt(driverId));
		spCarOrder.setCarId(TransformUtils.toInt(carId));
		int i = spCarOrderService.updateOrderAndPush(spCarOrder);
		if(i<1){
			responseObject.put("msg", "update fail ");
			responseObject.put("code", "107");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		log.debug("===================================分配成功");
		responseObject.put("msg", "success ");
		responseObject.put("code", "1");
		responseObject.put("data", new JSONObject());
		ResponseUtils.renderJson(response, responseObject.toString());
		return;
	}
	*/
/**
	 * 接载�?
	 *//*

	*/
/**
	 * 派�?�订单给司机并且送信息给专车乘客
	 * 
	 * @param request
	 * @param model
	 * @throws Exception
	 *//*

	@RequestMapping("takingOn")
	public void takingOn(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
//		String orderId = request.getParameter("orderId");
//		//String driverId = request.getParameter("driverId");
//		JSONObject responseObject = new JSONObject();
//		JsonConfig config = new JsonConfig();
//		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
//		SpcarOrder spCarOrder = spCarOrderService.getSpcarOrderbyId(TransformUtils.toInt(orderId));
//		spCarOrder.setStatus(SpcarOrder.ORDER_CARRY_STATUS);
//		SpcarOrder updateSpcarOrder = new SpcarOrder();
//		updateSpcarOrder.setSpcarOrderId(spCarOrder.getSpcarOrderId());
//		updateSpcarOrder.setStatus(SpcarOrder.ORDER_CARRY_STATUS);
//		int i = spCarOrderService.updateByPrimaryKeySelective(updateSpcarOrder);
//		if(i<1){
//			log.debug("===================================更新专车订单失败");
//			responseObject.put("msg", "update spcarorder fail ");
//			responseObject.put("code", "107");
//			responseObject.put("data", new JSONObject());
//			ResponseUtils.renderJson(response, responseObject.toString());
//			return;
//		}
//		String sign = "";
//		sign = TlsSignUtil.getTlsSignKey(Constant.manager);
//		JSONObject jsonOrder = JSONObject.fromObject(spCarOrder, config);
//		JSONObject data = new JSONObject();
//		data.put("spCarOrder", jsonOrder);
//
//		responseObject.put("msg", "this driver is raining ");
//		responseObject.put("code", SpcarPassengerPushCode.driverSetOut);
//		responseObject.put("data", data);
//		com.alibaba.fastjson.JSONObject json = RequestJson.getTextJsonMsg(responseObject.toString());
//		Boolean singleChar = IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, spCarOrder.getPassengerId()+"Passenger", json,false,);
//		if (!singleChar) {
//			log.debug("=============================推送司机已出发出錯"+new Date());
//			throw new RuntimeException();
//		}
//		responseObject.put("msg", "success ");
//		responseObject.put("code", "1");
//		responseObject.put("data", new JSONObject());
//		ResponseUtils.renderJson(response, responseObject.toString());
	}
	*/
/**
	 * 接到乘客，乘客状态为乘车中�?�订单状态为进行�?
	 *//*

	@RequestMapping("ongoing")
	public void ongoing(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
//		String orderId = request.getParameter("orderId");
//		//String driverId = request.getParameter("driverId");
//		JSONObject responseObject = new JSONObject();
//		JsonConfig config = new JsonConfig();
//		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
//		SpcarOrder spCarOrder = spCarOrderService.getSpcarOrderbyId(TransformUtils.toInt(orderId));
//		spCarOrder.setStatus(SpcarOrder.ORDER_CARRY_STATUS);
//		SpcarOrder updateSpcarOrder = new SpcarOrder();
//		updateSpcarOrder.setSpcarOrderId(spCarOrder.getSpcarOrderId());
//		updateSpcarOrder.setStatus(SpcarOrder.ORDER_CARRY_STATUS);
//		SpcarPassenger updateSpcarPassenger = new SpcarPassenger();
//		updateSpcarPassenger.setSpcarId(spCarOrder.getPassengerId());
//		updateSpcarPassenger.setStatus(SpcarPassenger.PASSENGER_GET_ON_STATUS);
//		int i = spCarOrderService.updateOrderandSpcarPassenger(updateSpcarOrder,updateSpcarPassenger);
//		if(i<2){
//			log.debug("===================================更新专车订单或乘客信息失�?");
//			responseObject.put("msg", "update  fail ");
//			responseObject.put("code", "107");
//			responseObject.put("data", new JSONObject());
//			ResponseUtils.renderJson(response, responseObject.toString());
//			return;
//		}
//		String sign = "";
//		sign = TlsSignUtil.getTlsSignKey(Constant.manager);
//		JSONObject jsonOrder = JSONObject.fromObject(spCarOrder, config);
//		JSONObject data = new JSONObject();
//		data.put("spCarOrder", jsonOrder);
//
//		responseObject.put("msg", "this driver is raining ");
//		responseObject.put("code", SpcarPassengerPushCode.carry);
//		responseObject.put("data", data);
//		com.alibaba.fastjson.JSONObject json = RequestJson.getTextJsonMsg(responseObject.toString());
//		Boolean singleChar = IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, spCarOrder.getPassengerId()+"Passenger", json,false);
//		if (!singleChar) {
//			log.debug("=============================�?款信息推送失�?"+new Date());
//			throw new RuntimeException();
//		}
//		responseObject.put("msg", "success ");
//		responseObject.put("code", "1");
//		responseObject.put("data", new JSONObject());
//		ResponseUtils.renderJson(response, responseObject.toString());
	}
	*/
/**
	 * 后台下单并推送给专车乘客
	 *//*

	  @RequestMapping("sendOrder")
		public void sendOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		  JSONObject responseObject = new JSONObject();
		  JsonConfig config = new JsonConfig();
		  config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
	        Date date = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	        String out_trade_no = sdf.format(date).toString();
	        String startAddress = request.getParameter("startAddress");
	        Integer passengerId = TransformUtils.toInt(request.getParameter("passengerId"));// 乘客ID
	        Integer totalHour = TransformUtils.toInt(request.getParameter("totalHour"));// 约车时长
	        Double startX = TransformUtils.toDouble(request.getParameter("startX"));
	        Double startY = TransformUtils.toDouble(request.getParameter("startY"));
	        Double endX = TransformUtils.toDouble(request.getParameter("endX"));
	        Double endY = TransformUtils.toDouble(request.getParameter("endY"));
	        String endAddress = request.getParameter("endAddress");
	        String startTime = request.getParameter("startTime");
	        int total_fee = 0;
	        if (totalHour > 0 && totalHour < 4) {
	            total_fee = 300 * totalHour;
	        } else if (totalHour >= 4 && totalHour < 8) {
	            total_fee = 250 * totalHour;
	        } else if (totalHour >= 8) {
	            total_fee = 200 * totalHour;
	        }
	        // 成功处理完微信返回来的信息，新增订单记录
	        SpcarOrder order = new SpcarOrder();
	        order.setStatus(999);
	        order.setPayStatus(0);
	        order.setPassengerId(passengerId);
	        order.setOrderNo(out_trade_no);
	        order.setMemo("");
	        order.setStartX(startX);
	        order.setStartY(startY);
	        order.setStartAddress(startAddress);
	        try {
	            order.setStartTime(sdf.parse(startTime));
	        } catch (ParseException e) {
	            e.printStackTrace();
	            log.error("======提交下单操作失败！startTime时间格式转换异常======"+startTime+e.getMessage());
	        }
	        order.setTotalHour(totalHour);
	        order.setCreateTime(new Date());
	        order.setUpdateTime(new Date());
	        order.setPayWayCode(0);
	        order.setAmount(total_fee * 100);
	        if (StringUtil.notEmpty(endAddress)) {
	            order.setEndX(endX);
	            order.setEndY(endY);
	            order.setEndAddress(endAddress);
	        }
	        order = spCarOrderService.submitOrder(order);
	        if(order.getSpcarOrderId()!=null&&order.getSpcarOrderId()!=0){
	        	//推�?�给专车乘客APP
	        	// 推�??
				String sign = "";
				sign = TlsSignUtil.getTlsSignKey(Constant.manager);
				JSONObject jsonOrder = JSONObject.fromObject(order, config);
				JSONObject data = new JSONObject();
				data.put("spCarOrder", jsonOrder);
				responseObject.put("msg", "this order was refund ");
				responseObject.put("code", SpcarPassengerPushCode.matchingSuccess);
				responseObject.put("data", data);
				com.alibaba.fastjson.JSONObject json = RequestJson.getTextJsonMsg(responseObject.toString());
				Boolean singleChar = IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, passengerId+"Passenger", json,false);
				if (!singleChar) {
					log.debug("=============================�?款信息推送失�?"+new Date());
					throw new RuntimeException();
				}
	        }else{
	        	 responseObject.put("msg", "新增订单失败");
	 	        responseObject.put("code", "108");
	 	        responseObject.put("data", new JSONObject() );
	 	        ResponseUtils.renderJson(response, responseObject.toString());
	 	        return;
	        }
	        
	        JSONObject data = JSONObject.fromObject(order, config);
	        JSONObject json = new JSONObject();
	        json.put("spCarOrder", data);
	        responseObject.put("msg", "訂單保存成功");
	        responseObject.put("code", "1");
	        responseObject.put("data", json);
	        ResponseUtils.renderJson(response, responseObject.toString());
		  
	  }
    */
/**
     * 测试
     *
     * @param request
     * @param response
     *//*

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");orderId
		//String driverId = request.getParameter("driverId");
		JSONObject responseObject = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		SpcarOrder spCarOrder = spCarOrderService.getSpcarOrderbyId(TransformUtils.toInt(orderId));
        //taxigoUser = taxigoUserService.createIm(taxigoUser);
		
    }

    public static void main(String[] args) throws Exception {
//        Map map = WeChatPayUtils.orderrefund("20170503094953", "1", "1");
//        //WeChatPayUtils.refundquery("20170503101452");
//        //WeChatPayUtils.queryWechatOrder("20170503101452");
//        System.out.println(map);
    		System.out.println( PropertyUtil.getAppProperty("identifier"));
    }

}
*/
