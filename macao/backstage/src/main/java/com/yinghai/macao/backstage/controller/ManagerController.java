package com.yinghai.macao.backstage.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.backstage.service.impl.SpcarManagerService;
import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.util.EncryptUtil;
import com.yinghai.macao.common.util.IMMsgRequestUtil;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TlsSignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * Created by Administrator on 2017/3/23.
 */
@RequestMapping("/admin/manager")
@Controller
public class ManagerController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SpcarManagerService spcarManagerService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, ModelMap model) {
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        log.debug("======获取管理员列表======");
        SpcarManager spcarManager = new SpcarManager();
        String id = request.getParameter("id");
        if(!StringUtil.empty(id)){
        	spcarManager.setId(Integer.valueOf(id));
        }
        String username = request.getParameter("query.username");
        if(!StringUtil.empty(username)){
        	spcarManager.setUsername("%"+username.trim()+"%");;
        }
        String email = request.getParameter("query.email");
        if(!StringUtil.empty(email)){
        	spcarManager.setEmail("%"+email.trim()+"%");;
        }
        String num = request.getParameter("page");
        Integer pageNum;
        if(StringUtil.empty(num)){
            pageNum = 1;
        }else{
            pageNum = Integer.valueOf(num);
        }
        String size = request.getParameter("pageSize");
        Integer pageSize;
        if(StringUtil.empty(size)){
            pageSize = 10;
        }else{
            pageSize = Integer.valueOf(size);
        }
       Page<SpcarManager> page = spcarManagerService.findList(pageNum,pageSize,spcarManager);
        model.addAttribute("managerList",page);
        model.addAttribute("page",page);
        if(spcarManager.getEmail()!=null&&spcarManager.getEmail().startsWith("%")){
        	spcarManager.setEmail(email.trim());
        }
        if(spcarManager.getUsername()!=null&&spcarManager.getUsername().startsWith("%")){
        	spcarManager.setUsername(username.trim());
        }
        model.addAttribute("spcarManager",spcarManager);
        model.addAttribute("pageNo",page.getPageNum());
        model.addAttribute("pageSize",page.getPageSize());
        model.addAttribute("recordCount",page.getTotal());
        model.addAttribute("pageCount",page.getPages());
        return "manager/list";
    }
    @RequestMapping("/save")
    public void save(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======新增或修改管理员数据======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JSONObject responseObject = new JSONObject();
        SpcarManager loopManager = new SpcarManager();
        String username = request.getParameter("username");
        String id = request.getParameter("id");
        String act = request.getParameter("act");
        if("add".equalsIgnoreCase(act)){
            if(!StringUtil.empty(username)){
                loopManager.setUsername(username);
                loopManager.setCreateTime(new Date());
            }else{
                responseObject.put("msg", "username not exist");
                responseObject.put("code", "101");
                responseObject.put("data", new JSONObject());
                log.info("==========username not exist=======");
                ResponseUtils.renderJson(response, responseObject.toString());
                return;
            }
        }else{
            if(!StringUtil.empty(id)){
                loopManager.setId(Integer.valueOf(id));
            }else{
                responseObject.put("msg", "id not exist");
                responseObject.put("code", "101");
                responseObject.put("data", new JSONObject());
                log.info("==========id not exist=======");
                ResponseUtils.renderJson(response, responseObject.toString());
                return;
            }
        }
        String password = request.getParameter("password");
        if(!StringUtil.empty(password)){
            loopManager.setPassword(EncryptUtil.MD5(EncryptUtil.MD5(password)));
        }else{
            responseObject.put("msg", "password not exist");
            responseObject.put("code", "101");
            responseObject.put("data", new JSONObject());
            log.info("==========password not exist=======");
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
        }

        SpcarManager  loopManage = spcarManagerService.findOneByUserName(username);
        if("add".equalsIgnoreCase(act)&&loopManage!=null){
            responseObject.put("msg", "IS EXIST THIS USER");
            responseObject.put("code", "999");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
        }
        String email = request.getParameter("email");
        if(!StringUtil.empty(email)){
            loopManager.setEmail(email);
        }
        
        
          loopManage = spcarManagerService.findOneByEmail(email);
        if("add".equalsIgnoreCase(act)&&loopManage!=null){
            responseObject.put("msg", "email is EXIST");
            responseObject.put("code", "998");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
        }
        String remark = request.getParameter("remark");
        if(!StringUtil.empty(remark)){
            loopManager.setRemark(remark);
        }
        loopManager.setLastUpdated(new Date());
        String roleId = request.getParameter("roleId");
        if(!StringUtil.empty(roleId)){
            loopManager.setRoleId(Integer.valueOf(roleId));
        }
        int i = 0;
        String msgCode = "";
        if ("add".equalsIgnoreCase(act)){
            i = spcarManagerService.createOne(loopManager);
            if(i>0){
                msgCode = "1";
            }
        }else{
            i = spcarManagerService.updateOne(loopManager);
            if(i>0){
                msgCode = "2";
            }
        }
        if(i>0){
            responseObject.put("msg", "success");
            responseObject.put("code", msgCode);
            responseObject.put("data", new JSONObject());
            log.info("==========manager save success=======");
            ResponseUtils.renderJson(response, responseObject.toString());
        }else{
            responseObject.put("msg", "fail");
            responseObject.put("code", "110");
            responseObject.put("data", new JSONObject());
            log.info("==========manager save success fail=======");
            ResponseUtils.renderJson(response, responseObject.toString());
        }
    }
    @RequestMapping("/edit")
    public String editOrSave(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======edit or new one manager======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String act = request.getParameter("act");
        String result = "manager/edit";
        if("upd".equalsIgnoreCase(act)){
            String id = request.getParameter("id");
            if(!StringUtil.empty(id)){
                model.addAttribute("admin",spcarManagerService.findOneByUserId(Integer.valueOf(id)));
            }else{
                log.error("======edit one manager.id can not null======");
                model.addAttribute("msg","id can not be null");
                return "500";
            }
        }
        return result;
    }

    @RequestMapping("/checkUser")
    public void check(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======检验新增是否存在用户======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JSONObject responseObject = new JSONObject();
        String username = request.getParameter("username");
        SpcarManager  loopManage = spcarManagerService.findOneByUserName(username);
        if(loopManage!=null){
            responseObject.put("msg", "IS EXIST THIS USER");
            responseObject.put("code", "999");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
        }else{
            responseObject.put("msg", "success");
            responseObject.put("code", "1");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
        }
    }
    @RequestMapping("/del")
    public void delManager(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======删除管理账号!======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JSONObject responseObject = new JSONObject();
        String id = request.getParameter("id");
        int  i = spcarManagerService.deleteOne(Integer.valueOf(id));
        if(i<=0){
            responseObject.put("msg", "delete fail");
            responseObject.put("code", "110");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
        }else{
            responseObject.put("msg", "success");
            responseObject.put("code", "1");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
        }
    }
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseObject = new JSONObject();
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
   
    
}
