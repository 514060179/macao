package com.yinghai.macao.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinghai.macao.app.vo.ResponseVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.service.SpcarPassengerService;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TlsSignUtil;

@Controller
@RequestMapping("/spcar/sign")
public class TlsSignRestController {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private SpcarDriverService driverService;
	@Autowired
	private SpcarPassengerService spcarPassengerService;
	/**
	 * 重新获取签名
	 * @param request
	 * @param response
	 */
	@RequestMapping("/rest")
	public void signAgain(HttpServletRequest request,HttpServletResponse response){
		String imName = request.getParameter("imName");
		String driverId = request.getParameter("driverId");
		JSONObject responseObject = new JSONObject();
		if (StringUtil.empty(imName)) {
			responseObject.put("msg", "imName为空！");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			log.error("==========imName not exist==========");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if (StringUtil.empty(driverId)) {
			responseObject.put("msg", "driverId为空！");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			log.error("==========driverId not exist==========");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		SpcarDriver driver = driverService.findById(Integer.parseInt(driverId));
		String oldImName = driver.getImName();
		if(!imName.equals(oldImName)){
			log.error("签名与骑手id不匹配！");
			ResponseVo.sendNotMeErrorCode(response,"签名与骑手司机id不匹配");
			return;
		}
		String sign = "";
		try {
			sign = TlsSignUtil.getTlsSignKey(imName);
		} catch (Exception e) {
			log.error("重新获取签名失败！"+e);
			e.printStackTrace();
			ResponseVo.sendNotMeErrorCode(response,"重新获取签名失败！");
			return;
		}
		if("".equals(sign)){
			ResponseVo.sendNotMeErrorCode(response,"重新获取签名失败！");
		}else{
			//更新
			int i = driverService.updateDriverSign(Integer.parseInt(driverId),sign);
			if(i<1){
				responseObject.put("msg", "获取签名成功，但更新失败");
			}else{
				responseObject.put("msg", "success");
			}
			JSONObject object = new JSONObject();
			object.put("sign", sign);
			object.put("imName", imName);
			responseObject.put("code", "1");
			responseObject.put("data", object);
		}
		ResponseUtils.renderJson(response, responseObject.toString());
	}
	/**
	 * 乘客端重新获取签名
	 */
	@RequestMapping("/passengerReset")
	public void getSignAgain(HttpServletRequest request,HttpServletResponse response){
		String imName = request.getParameter("imName");
		String passengerId = request.getParameter("passengerId");
		JSONObject  responseObject = new JSONObject();
		if(StringUtil.empty(imName)){
			responseObject.put("msg", "imName为空！");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			log.error("==========imName not exist==========");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if (StringUtil.empty(passengerId)) {
			responseObject.put("msg", "passengerId为空！");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			log.error("==========driverId not exist==========");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		SpcarPassenger passenger = null;
		try {
			passenger = spcarPassengerService.findById(Integer.parseInt(passengerId));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			ResponseVo.send101Code(response, "id格式错误");
			return;
		}
		if(passenger==null){
			ResponseVo.send102Code(response, "乘客信息不存在");
			return;
		}
		String oldImName = passenger.getImName();
		if(!imName.equals(oldImName)){
			log.error("签名与乘客id不匹配！");
			ResponseVo.sendNotMeErrorCode(response,"签名与乘客id不匹配");
			return;
		}
		String sign = "";
		try {
			sign = TlsSignUtil.getTlsSignKey(imName);
		} catch (Exception e) {
			log.error("重新获取签名失败！"+e);
			e.printStackTrace();
			ResponseVo.sendNotMeErrorCode(response,"重新获取签名失败！");
			return;
		}
		if("".equals(sign)){
			ResponseVo.sendNotMeErrorCode(response,"重新获取签名失败！");
		}else{
			//更新
			SpcarPassenger p = new SpcarPassenger();
			p.setSpcarId(passenger.getSpcarId());
			p.setSign(sign);
			int i = spcarPassengerService.updateSelect(p);
			if(i<1){
				responseObject.put("msg", "获取签名成功，但更新失败");
			}else{
				responseObject.put("msg", "success");
			}
			JSONObject object = new JSONObject();
			object.put("sign", sign);
			object.put("imName", imName);
			responseObject.put("code", "1");
			responseObject.put("data", object);
		}
		ResponseUtils.renderJson(response, responseObject.toString());
	}
	
}
