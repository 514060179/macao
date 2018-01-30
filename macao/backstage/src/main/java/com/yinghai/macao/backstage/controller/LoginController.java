package com.yinghai.macao.backstage.controller;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.util.*;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.backstage.model.SpcarMenu;
import com.yinghai.macao.backstage.service.impl.MenuService;
import com.yinghai.macao.backstage.service.impl.SpcarManagerService;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/managerLogin")
public class LoginController {
	
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SpcarManagerService spcarManagerService;
	
	@RequestMapping("/tologin")
	public String tologin(HttpServletRequest request,ModelMap model){
		return "login";
	}
	@Autowired
	private MenuService menuService;
	@RequestMapping("/login")
	public void login(HttpServletRequest request,ModelMap model,HttpServletResponse response){
		log.debug("======进入后台登录管理=====");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject  responseObject = new JSONObject();
		response.setContentType("application/json;charset=utf-8");
		String username = TransformUtils.toString(request.getParameter("username"));
		if( StringUtils.isEmpty(username) || "".equals(username.trim()) ){
			responseObject.put("msg", "username not exist");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			log.debug("==========username not exist=======");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String password = TransformUtils.toString(request.getParameter("password"));
		if( StringUtils.isEmpty(password) || "".equals(password.trim()) ){
			responseObject.put("msg", "password not exist");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			log.debug("==========password not exist=======");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		SpcarManager user = null;
		if(StringUtil.checkEmail(username)){
			user = spcarManagerService.findOneByEmail(username);
		}else{
			user = spcarManagerService.findOneByUserName(username);
		}
		if (user != null) {
				if(user.getPassword().equalsIgnoreCase(EncryptUtil.MD5(EncryptUtil.MD5(password)))){
					// 保存登录状态信息
					HttpSession session = request.getSession();
					session.setAttribute("spcarManager", user);
					List<SpcarMenu> menu = menuService.findMenuList(new HashMap<String,Object>());
					session.setAttribute("menu", menu);
					responseObject.put("msg", "success");
					responseObject.put("code", "1");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
				}else{// 密码错误
					responseObject.put("msg", "Password error");
					responseObject.put("code", "105");
					responseObject.put("data",  new JSONObject());
					log.debug("=======Password error========");
					ResponseUtils.renderJson(response, responseObject.toString());
				}
			}else{// 无效
				responseObject.put("code", "102");
				responseObject.put("msg", "user is not exist");
				responseObject.put("data", new JSONObject());
				log.debug("=======user invalid========");
				ResponseUtils.renderJson(response, responseObject.toString());
			}
	}
	
	
	@RequestMapping("/logout")
	public void loginOut(HttpServletRequest request,ModelMap model,HttpServletResponse response){
		JSONObject responseObject = new JSONObject();
		log.info("======进入后台管理登出功能======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
		SpcarManager user = (SpcarManager)session.getAttribute("spcarManager");
		if(user!=null){
			session.removeAttribute("spcarManager");
			session.removeAttribute("menu");
		}
		responseObject.put("msg", "success");
		responseObject.put("code", "1");
		responseObject.put("data", new JSONObject());
		ResponseUtils.renderJson(response, responseObject.toString());
	}

	 /**
     * 
     * @param request
     * @param response
     * @throws Exception 
     * 根据状态获取管理员列表
     */
    @RequestMapping(value = "managerList", method = RequestMethod.POST)
    public void managerList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SpcarManager spcarManager = new SpcarManager();
    	JSONObject responseObject = new JSONObject();
    	String type = request.getParameter("type");//为空时传全部列表，0时传在线，1时传不在线
    	//取出所有管理员账号信息
    	List<SpcarManager> list = spcarManagerService.findAllList(spcarManager);
    	if(StringUtil.notEmpty(type)){//不为空时根据条件查询，为空时直接返回所有列表
        	String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
        	List<String> onlineList = new ArrayList<String>();
        	List<String> offlineList = new ArrayList<String>();
        	  String[] toCountList = new String[list.size()];
    		// 取出IM用户账号存放到一个数组中
        	if(list!=null&&list.size()>0){
        		int j = list.size();
        		for (int i = 0; i < j ; i++) {
        			toCountList[i]=list.get(i).getImName();
        			
        		}
        		// 腾讯云接口，通过IM用户账号数组获取IM在线的账号
        		com.alibaba.fastjson.JSONObject resultObj = IMMsgRequestUtil.queryState(sign, toCountList);
        		if (resultObj == null) {
        			responseObject.put("msg", "resultObj为空，获取到的IM在线账号为空");
        	  		responseObject.put("code", "102");
        	  		responseObject.put("data", new JSONObject());
        	  		 ResponseUtils.renderJson(response, responseObject.toString());
        	  		return;
        		}
        		// 取出所有人員的狀態
        		com.alibaba.fastjson.JSONArray array = (com.alibaba.fastjson.JSONArray) JSON
        				.parse(resultObj.get("QueryResult").toString());
        		
        		// 循環，判斷是否在線，在線就將賬號放到list裡面
        		for (int i = 0; i < array.size(); i++) {
        			com.alibaba.fastjson.JSONObject json = array.getJSONObject(i);
        			if ("Online".equals(json.getString("State"))) {
        				onlineList.add(json.getString("To_Account"));
        			}else{
        				offlineList.add(json.getString("To_Account"));
        			}
        		}
        		if("0".equals(type)){//查询所有在线的管理员列表
        			if(onlineList!=null&&onlineList.size()!=0){//判空，如果没有在线管理员，则不需要查询，直接返回null
            			spcarManager.setImNameList(onlineList);
               		 list = spcarManagerService.findAllList(spcarManager);//通过条件去获取相应的管理员列表
            		}else{
            			list = new ArrayList<>();
            		}
        		}else{//查询不在线的管理员列表
        			if(offlineList!=null&&offlineList.size()!=0){//判空，如果没有在线管理员，则不需要查询，直接返回null
            			spcarManager.setImNameList(offlineList);
               		 list = spcarManagerService.findAllList(spcarManager);//通过条件去获取相应的管理员列表
            		}else{
            			list = new ArrayList<>();
            		}
        		}
        	}
    	}
    		   JsonConfig config = new JsonConfig();
    	        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
    	        JSONArray data = JSONArray.fromObject(list, config);
	    		 responseObject.put("msg", "操作成功");
	 	  		responseObject.put("code", "1");
	 	  		responseObject.put("data", data);
	 	  	 ResponseUtils.renderJson(response, responseObject.toString());
	 	  	 return;
    	
    }
    
    /**
     * 
     * @param request
     * @param response
     * @throws Exception 
     * 判断该IMname是否在线
     */
    @RequestMapping(value = "booleanOnline", method = RequestMethod.POST)
    public void booleanOnline(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//取出所有管理员账号信息
    	String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
    	  JSONObject responseObject = new JSONObject();
    	  String imName = request.getParameter("imName");//imName
    	  if(StringUtil.empty(imName)){//空判断
    		  responseObject.put("msg", "imName is null");
  	  		responseObject.put("code", "101");
  	  		responseObject.put("data", new JSONObject());
  	  	 ResponseUtils.renderJson(response, responseObject.toString());
  	  		return;
    	  }
    	  String[] toCountList = new String[]{imName};
    		// 腾讯云接口，通过IM用户账号数组获取IM在线的账号
    		com.alibaba.fastjson.JSONObject resultObj = IMMsgRequestUtil.queryState(sign, toCountList);
    		if (resultObj == null) {
    			responseObject.put("msg", "resultObj为空，获取到的IM在线账号为空");
    	  		responseObject.put("code", "102");
    	  		responseObject.put("data", new JSONObject());
    	  		 ResponseUtils.renderJson(response, responseObject.toString());
    	  		return;
    		}
    		// 取出所有人員的狀態
    		com.alibaba.fastjson.JSONArray array = (com.alibaba.fastjson.JSONArray) JSON
    				.parse(resultObj.get("QueryResult").toString());
    		if(array!=null&&array.size()!=0&&"Online".equals(array.getJSONObject(0).getString("State"))){//空判断，如果不为空就判断该管理员是否是在线状态
    			responseObject.put("msg", "success");
    	  		responseObject.put("code", "1");
    	  		responseObject.put("data", imName);
    	  		 ResponseUtils.renderJson(response, responseObject.toString());
    	  		return;
    		}else{
    			responseObject.put("msg", "this manager is not online");
    	  		responseObject.put("code", "102");
    	  		responseObject.put("data", new JSONObject());
    	  		 ResponseUtils.renderJson(response, responseObject.toString());
    	  		return;
    		}
    }
}
