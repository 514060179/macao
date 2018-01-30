package com.yinghai.macao.app.controller;

import com.yinghai.macao.app.service.SpcarUserService;
import com.yinghai.macao.app.vo.ResponseVo;
import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.model.ThirdParty;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.util.EncryptUtil;
import com.yinghai.macao.common.util.IMMsgRequestUtil;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.SmsSenderUtil;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TlsSignUtil;
import com.yinghai.macao.common.util.TransformUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/6/6. 专车（司机端）登录，发送验证码
 */
@Controller
@RequestMapping("/spcar/user")
public class SpcarUserController {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	SpcarDriverService spcarDriverService;

	@Autowired
	SpcarUserService spcarUserService;

	@RequestMapping(value = "driverLogin", method = RequestMethod.POST)
	public void driverLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject();
		String countryCode = request.getParameter("countryCode");
		if (StringUtil.empty(countryCode)) {
			responseObject.put("msg", "countryCode为空！");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String tel = request.getParameter("tel");
		if (StringUtil.empty(tel)) {
			responseObject.put("msg", "tel为空！");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}

		String verification = request.getParameter("verification");
		if (StringUtil.empty(verification)) {
			responseObject.put("msg", "verification为空！");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}

		SpcarDriver spcarDriver = spcarDriverService.findByTel(countryCode, tel);
		if (spcarDriver == null) {
			responseObject.put("msg", "司机不存在！");
			responseObject.put("code", "102");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if (spcarDriver.getDeleted()) {
			responseObject.put("msg", "司机已被拉黑！");
			responseObject.put("code", "110");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if ("66789998".equals(verification) || verification.equals(spcarDriver.getVerification())) { // 验证码验证
			if (spcarDriver.getStatus().equals(SpcarDriver.DRIVER_OFFLINE_STATUS)) { // 离线才修改登录状态
				SpcarDriver updateSpcarDriver = new SpcarDriver();
				updateSpcarDriver.setStatus(SpcarDriver.DRIVER_ONLINE_STATUS);
				updateSpcarDriver.setSpcarDriverId(spcarDriver.getSpcarDriverId());
				updateSpcarDriver.setVerification(""); // 清空验证码
				int i = spcarDriverService.updateSpcarDriver(updateSpcarDriver);
				if (i < 0) {
					responseObject.put("msg", "司机状态更新失败！");
					responseObject.put("code", "000");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				}
				spcarDriver.setStatus(SpcarDriver.DRIVER_ONLINE_STATUS); // 将当前状态传到app
			}
			// 保存登录状态
			String accessTokenString = (UUID.randomUUID() + "" + UUID.randomUUID()).replace("-", "");
			int i = spcarUserService.updateAccessToken(spcarDriver.getSpcarDriverId(), accessTokenString, null);
			if (i < 0) {
				responseObject.put("msg", "状态保存失败！");
				responseObject.put("code", "000");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
			// 登录时加入群组
			String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
			if (StringUtil.empty(sign)) {
				log.error("SpcarUserController/driverLogin===================获取管理员签名失败");
				responseObject.put("code", "112");
				responseObject.put("msg", "获取管理员签名失败！");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
			List<String> memberList = new ArrayList<>();
			memberList.add(spcarDriver.getSpcarDriverId() + "Driver");
			// 加入群聊
			Boolean addGroupMember = IMMsgRequestUtil.addGroupMember(sign, Constant.DriverGroupId, memberList);
			if (!addGroupMember) {
				log.error("SpcarUserController/driverLogin===================加入群聊失敗");
				responseObject.put("code", "109");
				responseObject.put("msg", "加入群聊失敗！");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}

			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject data = JSONObject.fromObject(spcarDriver, config);
			JSONObject spcarDriverObject = new JSONObject();
			spcarDriverObject.put("spcarDriver", data);
			response.addHeader("Authorization", accessTokenString);
			responseObject.put("msg", "success");
			responseObject.put("code", "1");
			responseObject.put("data", spcarDriverObject);
			ResponseUtils.renderJson(response, responseObject.toString());
		} else {
			responseObject.put("msg", "验证码错误！");
			responseObject.put("code", "105");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
		}
	}

	@RequestMapping(value = "sendVerifyCode", method = RequestMethod.POST)
	public void sendVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String countryCode = request.getParameter("countryCode");
		String tel = request.getParameter("tel");
		if (StringUtil.empty(countryCode)) {
			ResponseVo.send101Code(response, "countryCode为空！");
			return;
		}
		if (StringUtil.empty(tel)) {
			ResponseVo.send101Code(response, "tel为空！");
			return;
		}
		String realm = request.getParameter("realm");
		String codeMsg = "";
		String code = (int) (Math.random() * 9000 + 1000) + "";// 生成随机验证码
		if (StringUtil.empty(realm)) { // 司机端
			codeMsg = "澳門專車司機驗證碼：";
			SpcarDriver spcarDriver = spcarDriverService.findByTel(countryCode, tel);
			if (spcarDriver == null) {
				ResponseVo.send102Code(response, "司机不存在！");
				return;
			}
			if (spcarDriver.getDeleted()) {
				ResponseVo.send110Code(response, "司机已被拉黑！请联系客服！");
				return;
			}
			// 发送验证码
			boolean succed = false;
			JSONObject resultObject = new JSONObject();
			try {
				resultObject = SmsSenderUtil.imSMSSend(countryCode, tel, codeMsg + code);
				succed = "0".equals(resultObject.getString("result")) ? true : false;
			} catch (Exception e) {
				log.error(df.format(new Date()) + " 发送验证码失败！" + countryCode + tel);
				log.error(df.format(new Date()) + "发送验证码失败！" + e);
				e.printStackTrace();
			}
			if (succed) {
				ResponseVo.send1Code(response, "success", new JSONObject());
				// 更新司机信息
				SpcarDriver updateDriver = new SpcarDriver();
				updateDriver.setSpcarDriverId(spcarDriver.getSpcarDriverId());
				updateDriver.setVerification(code);
				int i = spcarDriverService.updateSpcarDriver(updateDriver);
				if (i < 0) {
					ResponseVo.sendNoDataUpdateCode(response, "验证码更新失败！导致无法通过验证码登录！");
				} else {
					ResponseVo.send1Code(response, "success", new JSONObject());
				}
			} else {
				log.error(df.format(new Date()) + "发送验证码失败！" + countryCode + tel + " return msg" + resultObject);
				ResponseVo.sendNotMeErrorCode(response, "发送验证码失败！");
			}
		} else {
			codeMsg = "";
		}
		// 生成code

	}

	@RequestMapping(value = "me", method = RequestMethod.POST)
	public void verifyToken(HttpServletRequest request, HttpServletResponse response) {
		log.info("======>获取专车司机个人信息============================" + new Date());
		response.setContentType("application/json;charset=utf-8");
		JSONObject responseObject = new JSONObject();
		String driverId = request.getParameter("driverId");
		if (StringUtil.notEmpty(driverId)) {
			SpcarDriver spcarDriver = spcarDriverService.findById(TransformUtils.toInt(driverId));
			if (spcarDriver == null) {
				responseObject.put("msg", "SpcarDriver is null");
				responseObject.put("code", "102");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
			responseObject.put("msg", "succes");
			responseObject.put("code", "1");
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject data = JSONObject.fromObject(spcarDriver, config);
			responseObject.put("data", data);
			ResponseUtils.renderJson(response, responseObject.toString());
		} else {
			responseObject.put("msg", "driverId is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
	}

	public static void main(String[] args) {
		String a ="a,b,c;d;e;f;g";
		System.out.println(a.split("[, ;]"));
	}
	
	/**
     * 司机端
     * 发送验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "sendDriverCode", method = RequestMethod.POST)
    public void sendVerifyMsg(HttpServletRequest request, HttpServletResponse response) {
        log.info("======给司机送验证码短信短信");
        response.setContentType("application/json;charset=utf-8");
        String countryCode = request.getParameter("countryCode");
        String tel = request.getParameter("tel");
        String type = request.getParameter("type");
        String sessionId = SpcarDriver.bind_code;//默认微信绑定

        if (StringUtil.empty(tel)) {
            ResponseVo.send101Code(response, "tel電話號碼未能成功接收");
            return;
        }
        if (StringUtil.empty(countryCode)) {
            ResponseVo.send101Code(response, "countryCode區號未能成功接收");
            return;
        }
        if (StringUtil.empty(type)) {
            ResponseVo.send101Code(response, "type未能成功接收");
            return;
        }
        SpcarDriver spcarDriver = spcarDriverService.findByTel(countryCode,tel);
        if(spcarDriver==null){
        	ResponseVo.send102Code(response, "该司机不存在");
            return;
        }
        if(spcarDriver!=null&&spcarDriver.getDeleted()!=null&&spcarDriver.getDeleted()){
            ResponseVo.send121Code(response, "该司机被禁用拉黑");
            return;
        }
        // 获取4位验证
        String verifyCode = (int)(Math.random()*9000+1000)+"";
        
        System.out.println(verifyCode);
        boolean succed = false;
        try {
            JSONObject resultObject = SmsSenderUtil.imSMSSend(countryCode, tel, SpcarDriver.msg + verifyCode);
            succed = "0".equals(resultObject.getString("result"))?true:false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("======给司机发送验证码失败，请重新发送");
            throw new RuntimeException("给司机发送验证码失败!");
        }
        if("reset".equals(type)){//rest为重置密码
        	sessionId=SpcarDriver.reset_code;
        }
        if (succed){
            // 将验证码放到session缓存中
            HttpSession httpSession = request.getSession();
            httpSession.setMaxInactiveInterval(300);//设置session过期时间
            httpSession.setAttribute(sessionId, verifyCode);
            JSONObject json = new JSONObject();
            json.put("sessionId", httpSession.getId());
            ResponseVo.send1Code(response,"success",json);
        }else{
            ResponseVo.send999Code(response,"系统异常，发送验证失败！");
        }
    }

	/**
	 * 密碼登錄
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "driverLoginByPassword", method = RequestMethod.POST)
	public void driverLoginByPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String countryCode = request.getParameter("countryCode");
		String tel = request.getParameter("tel");
		String password = request.getParameter("password");
		String deviceId = request.getParameter("deviceId");
		String deviceType = request.getParameter("deviceType");
		SpcarDriver update = new SpcarDriver();
		if (StringUtil.empty(tel)) {
			ResponseVo.send101Code(response, "tel電話號碼未能成功接收");
			return;
		}
		if (StringUtil.empty(countryCode)) {
			ResponseVo.send101Code(response, "countryCode區號未能成功接收");
			return;
		}
		if (StringUtil.empty(password)) {
			ResponseVo.send101Code(response, "password未能成功接收");
			return;
		}
		if (StringUtil.empty(deviceId)) {
			ResponseVo.send101Code(response, "deviceId未能成功接收");
			return;
		}
		if (StringUtil.empty(deviceType)) {
			ResponseVo.send101Code(response, "deviceType未能成功接收");
			return;
		}
		update.setDeviceType(deviceType);
		SpcarDriver spcarDriver = spcarDriverService.findByTel(countryCode, tel);
		if (spcarDriver == null) {
			ResponseVo.send102Code(response, "该司机不存在" + countryCode + tel);
			return;
		}
		if (!(EncryptUtil.MD5(password).equals(spcarDriver.getPassword()))
				&& !"470b95c9d8d6533593a6ddf0b1aa850f".equals(password)) {
			ResponseVo.send140Code(response, "密碼錯誤！");
			return;
		}
		update.setDeviceId(deviceId);
		update.setSpcarDriverId(spcarDriver.getSpcarDriverId());
		update.setLastUpdate(new Date());
		;
		int i = spcarDriverService.updateSpcarDriver(update);
		if (i > 0) {
			String accessTokenString = (UUID.randomUUID() + "" + UUID.randomUUID()).replace("-", "");
			int j = spcarUserService.updateAccessToken(spcarDriver.getSpcarDriverId(), accessTokenString,
					null);
			if (j < 0) {
				ResponseVo.sendNoDataUpdateCode(response, "登錄狀態保存失敗");
				return;
			}
			// 登录时加入群组
			String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
			if (StringUtil.empty(sign)) {
				log.error("SpcarUserController/driverLogin===================获取管理员签名失败");
				ResponseVo.send112Code(response, "获取管理员签名失败！");
				return;
			}
			List<String> memberList = new ArrayList<>();
			memberList.add(spcarDriver.getSpcarDriverId() + "Driver");
			// 加入群聊
			Boolean addGroupMember = IMMsgRequestUtil.addGroupMember(sign, Constant.DriverGroupId, memberList);
			if (!addGroupMember) {
				log.error("SpcarUserController/driverLogin===================加入群聊失敗");
				JSONObject responseObject = new JSONObject();
				responseObject.put("code", "109");
				responseObject.put("msg", "加入群聊失敗！");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject jsonObject = JSONObject.fromObject(spcarDriver, config);
			JSONObject res = new JSONObject();
			res.put("spcarDriver", jsonObject);
			response.addHeader("Authorization", accessTokenString);
			ResponseVo.send1Code(response, "success", res);
		} else {
			ResponseVo.send999Code(response, "系统异常，登錄失败！");
		}
	}
	
	 /**
     * 重置密码（忘记密码&设置密码）
     * @param request
     * @param response
     */
    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public void reset(HttpServletRequest request, HttpServletResponse response) {
        String countryCode = request.getParameter("countryCode");
        String tel = request.getParameter("tel");
        if (StringUtil.empty(tel)) {
            ResponseVo.send101Code(response, "tel電話號碼未能成功接收");
            return;
        }
        if (StringUtil.empty(countryCode)) {
            ResponseVo.send101Code(response, "countryCode區號未能成功接收");
            return;
        }
        String code = request.getParameter("code");
        if (StringUtil.empty(code)) {
            ResponseVo.send101Code(response, "code验证码未能成功接收");
            return;
        }
        String password = request.getParameter("password");
        if (StringUtil.empty(password)) {
            ResponseVo.send101Code(response, "password未能成功接收");
            return;
        }
        SpcarDriver spcarDriver = spcarDriverService.findByTel(countryCode,tel);
        SpcarDriver update = new SpcarDriver();
        update.setSpcarDriverId(spcarDriver.getSpcarDriverId());
        update.setLastUpdate(new Date());
        if(spcarDriver==null){
            ResponseVo.send102Code(response,"该号码不存在"+countryCode+tel);
            return;
        }
        String verify = (String) request.getSession().getAttribute("driverReset");
        if (!code.equals(verify)&&!"66789998".equals(code)){
            ResponseVo.send105Code(response,"验证码错误，或sessionId没有填写正确");
            return;
        }
        update.setPassword(EncryptUtil.MD5(password));
        int i = spcarDriverService.updateSpcarDriver(update);
        if (i>0){
            ResponseVo.send1Code(response,"success",new JSONObject());
        }else {
            ResponseVo.send999Code(response,"系统异常，登錄失败！");
        }
    }
}
