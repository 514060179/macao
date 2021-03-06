package com.yinghai.macao.backstage.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.model.Spcar;
import com.yinghai.macao.common.service.SpcarService;
import com.yinghai.macao.common.util.EncryptUtil;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TransformUtils;

/**
 * Created by Administrator on 2017/3/23.
 */
@RequestMapping("/admin/spcar")
@Controller
public class SpcarController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SpcarService spcarService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, ModelMap model) {
        log.debug("======获取管理员列表======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Spcar spcar = new Spcar();
        String id = request.getParameter("id");
        if(!StringUtil.empty(id)){
        	spcar.setSpcarId(Integer.valueOf(id));
        }
        String spcarNo = request.getParameter("query.spcarNo");
        if(!StringUtil.empty(spcarNo)){
        	spcar.setSpcarNo("%"+spcarNo.trim()+"%");
        }
        String spcarSit = request.getParameter("query.spcarSit");
        if(!StringUtil.empty(spcarSit)){
        	spcar.setSpcarSit(TransformUtils.toInt(spcarSit.trim()));
        }
        
        String spcarUsed = request.getParameter("query.spcarUsed");
        if(!StringUtil.empty(spcarUsed)){
        	spcar.setSpcarUsed(TransformUtils.toBoolean(spcarUsed));
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
       Page<Spcar> page = spcarService.findList(pageNum,pageSize,spcar);
        model.addAttribute("managerList",page);
        model.addAttribute("page",page);
        //查詢條件數據，模糊查詢的需要去掉第一位與最後一位字節
        if(spcar.getSpcarNo()!=null&&spcar.getSpcarNo().startsWith("%")){
        	spcar.setSpcarNo(spcarNo.trim());
        }
        model.addAttribute("spcar",spcar);
        model.addAttribute("pageNo",page.getPageNum());
        model.addAttribute("pageSize",page.getPageSize());
        model.addAttribute("recordCount",page.getTotal());
        model.addAttribute("pageCount",page.getPages());
        return "car/list";
    }
//    @RequestMapping("/save")
//    public void save(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
//        log.debug("======获取專車司機列表======");
//        JSONObject responseObject = new JSONObject();
//        SpcarDriver spcarDriver = new SpcarDriver();
//        String id = request.getParameter("id");
//        String act = request.getParameter("act");
//        if("add".equalsIgnoreCase(act)){
//            if(!StringUtil.empty("")){
//            	//spcarDriver.setUsername(username);
//            	spcarDriver.setCreateTime(new Date());
//            }else{
//                responseObject.put("msg", "username not exist");
//                responseObject.put("code", "101");
//                responseObject.put("data", new JSONObject());
//                log.info("==========username not exist=======");
//                ResponseUtils.renderJson(response, responseObject.toString());
//                return;
//            }
//        }else{
//            if(!StringUtil.empty(id)){
//            	spcarDriver.setSpcarDriverId(Integer.valueOf(id));
//            }else{
//                responseObject.put("msg", "id not exist");
//                responseObject.put("code", "101");
//                responseObject.put("data", new JSONObject());
//                log.info("==========id not exist=======");
//                ResponseUtils.renderJson(response, responseObject.toString());
//                return;
//            }
//        }
//        String password = request.getParameter("password");
//        if(!StringUtil.empty(password)){
//            loopManager.setPassword(EncryptUtil.MD5(EncryptUtil.MD5(password)));
//        }else{
//            responseObject.put("msg", "password not exist");
//            responseObject.put("code", "101");
//            responseObject.put("data", new JSONObject());
//            log.info("==========password not exist=======");
//            ResponseUtils.renderJson(response, responseObject.toString());
//            return;
//        }
//
//        SpcarManager  loopManage = spcarDriverService.findOneByUserName(username);
//        if("add".equalsIgnoreCase(act)&&loopManage!=null){
//            responseObject.put("msg", "IS EXIST THIS USER");
//            responseObject.put("code", "999");
//            responseObject.put("data", new JSONObject());
//            ResponseUtils.renderJson(response, responseObject.toString());
//            return;
//        }
//        String email = request.getParameter("email");
//        if(!StringUtil.empty(email)){
//            loopManager.setEmail(email);
//        }
//        String remark = request.getParameter("remark");
//        if(!StringUtil.empty(remark)){
//            loopManager.setRemark(remark);
//        }
//        loopManager.setLastUpdated(new Date());
//        String roleId = request.getParameter("roleId");
//        if(!StringUtil.empty(roleId)){
//            loopManager.setRoleId(Integer.valueOf(roleId));
//        }
//        int i = 0;
//        String msgCode = "";
//        if ("add".equalsIgnoreCase(act)){
//            i = spcarManagerService.createOne(loopManager);
//            if(i>0){
//                msgCode = "1";
//            }
//        }else{
//            i = spcarManagerService.updateOne(loopManager);
//            if(i>0){
//                msgCode = "2";
//            }
//        }
//        if(i>0){
//            responseObject.put("msg", "success");
//            responseObject.put("code", msgCode);
//            responseObject.put("data", new JSONObject());
//            log.info("==========manager save success=======");
//            ResponseUtils.renderJson(response, responseObject.toString());
//        }else{
//            responseObject.put("msg", "fail");
//            responseObject.put("code", "110");
//            responseObject.put("data", new JSONObject());
//            log.info("==========manager save success fail=======");
//            ResponseUtils.renderJson(response, responseObject.toString());
//        }
//    }
    
    @RequestMapping("/save")
    public void save( HttpServletRequest request, HttpServletResponse response,ModelMap model) throws IllegalStateException, IOException, ParseException {
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 JSONObject responseObject = new JSONObject();
    	Spcar spcar = new Spcar();
    	String id = request.getParameter("id");
    	 String act = request.getParameter("act");
    	if(StringUtil.notEmpty(id)){
    		spcar.setSpcarId(TransformUtils.toInt(id));
    		act="UPDATE";
    	}else{
    		act="ADD";
    	}
    	String spcarType = request.getParameter("spcarType");
    	if(StringUtil.notEmpty(spcarType)){
    		spcar.setSpcarType(spcarType);
    	}
    	String spcarColor = request.getParameter("spcarColor");
    	if(StringUtil.notEmpty(spcarColor)){
    		spcar.setSpcarColor(spcarColor);
    	}
    	String spcarNo = request.getParameter("spcarNo");
    	if(StringUtil.notEmpty(spcarNo)){
    		spcar.setSpcarNo(spcarNo);
    	}
    	String spcarSit = request.getParameter("spcarSit");
    	if(StringUtil.notEmpty(spcarSit)){
    		spcar.setSpcarSit(TransformUtils.toInt(spcarSit));
    	}
    	Boolean spcarUsed = TransformUtils.toBoolean(request.getParameter("spcarUsed"));
    		spcar.setSpcarUsed(spcarUsed);
    		if("ADD".equals(act)){
    			spcar.setCreateTime(new Date());
        		int i = spcarService.createSpcar(spcar);
        		if(i>0){
            			id = spcar.getSpcarId()+"";
        		}else{
                		  responseObject.put("msg", "新增專車出錯");
                          responseObject.put("code", "-1");
                          responseObject.put("data", new JSONObject());
                          ResponseUtils.renderJson(response, responseObject.toString());
                          return;
        		}
        	}
    		String boochange = request.getParameter("boochange");
        	if(StringUtil.notEmpty(boochange)&&"true".equals(boochange)){
        	  CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        	  if(multipartResolver.isMultipart(request)){  
                  //转换成多部分request    
                  MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
                  //取得request中的所有文件名  
                  Iterator<String> iter = multiRequest.getFileNames();  
                  while(iter.hasNext()){  
                      //记录上传过程起始时的时间，用来计算上传时间  
                      int pre = (int) System.currentTimeMillis();  
                      //取得上传文件  
                      MultipartFile file = multiRequest.getFile(iter.next());  
                      if(file != null){  
                          //取得当前上传文件的文件名称  
                          String myFileName = file.getOriginalFilename();  
                          //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                          if(myFileName.trim() !=""){  
                              System.out.println(myFileName);  
                              //重命名上传后的文件名  
                              String fileName = id +"."+ file.getOriginalFilename().split("\\.")[1];  
                              //定义上传路径  
                              String realPath = Constant.filepath+Constant.spcarimage;
    	                      	File fileIo = new File(realPath);
    	        				//创建文件夹
    	        				if (!fileIo.exists()) {
    	        					fileIo.mkdirs();
    	        				}
                              String path = realPath + fileName;  
                              File localFile = new File(path);  
                              file.transferTo(localFile);
                              
                              if("ADD".equals(act)){
                            	  spcar = new Spcar();
                            	  spcar.setSpcarId(TransformUtils.toInt(id));
                            	  spcar.setImage(Constant.spcarimage + fileName);
                            	  int i =  spcarService.updateSpcar(spcar);
                            	  if(i>0){
                            		  responseObject.put("msg", "create success");
                                      responseObject.put("code", "1");
                                      responseObject.put("data", new JSONObject());
                                      ResponseUtils.renderJson(response, responseObject.toString());
                                      return;
                            	  }else{
                            		  responseObject.put("msg", "保存圖片信息出錯");
                                      responseObject.put("code", "-1");
                                      responseObject.put("data", new JSONObject());
                                      ResponseUtils.renderJson(response, responseObject.toString());
                                      return;
                            	  }
                            	 
                              }else{
                            	  spcar.setUpdateTime(new Date());
                            	  spcar.setImage(Constant.spcarimage + fileName);
                            	  int i =  spcarService.updateSpcar(spcar);
                            	  if(i>0){
                            		  responseObject.put("msg", "update success");
                                      responseObject.put("code", "2");
                                      responseObject.put("data", new JSONObject());
                                      ResponseUtils.renderJson(response, responseObject.toString());
                                      return;
                            	  }else{
                            		  responseObject.put("msg", "在有圖片時修改專車信息出錯");
                                      responseObject.put("code", "-1");
                                      responseObject.put("data", new JSONObject());
                                      ResponseUtils.renderJson(response, responseObject.toString());
                                      return;
                            	  }
                            }
                          }  
                      }  
                      //记录上传该文件后的时间  
                      int finaltime = (int) System.currentTimeMillis();  
                      System.out.println(finaltime - pre);  
                  }  
                    
              }  
        	}else{
        		if("ADD".equals(act)){
        			 responseObject.put("msg", "create success");
                     responseObject.put("code", "1");
                     responseObject.put("data", new JSONObject());
                     ResponseUtils.renderJson(response, responseObject.toString());
                     return;
        		}
        		spcar.setUpdateTime(new Date());
        		 int i =  spcarService.updateSpcar(spcar);
        		  if(i>0){
            		  responseObject.put("msg", "update success");
                      responseObject.put("code", "2");
                      responseObject.put("data", new JSONObject());
                      ResponseUtils.renderJson(response, responseObject.toString());
                      return;
            	  }else{
            		  responseObject.put("msg", "無修改圖片時，修改專車信息出錯");
                      responseObject.put("code", "-1");
                      responseObject.put("data", new JSONObject());
                      ResponseUtils.renderJson(response, responseObject.toString());
                      return;
                 }
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
        String result = "car/edit";
        if("upd".equalsIgnoreCase(act)){
            String id = request.getParameter("id");
            if(!StringUtil.empty(id)){
                model.addAttribute("spcar",spcarService.findById(Integer.valueOf(id)));
            }else{
                log.error("======edit one manager.id can not null======");
                model.addAttribute("msg","id can not be null");
                return "500";
            }
        }
        return result;
    }

  
    @RequestMapping("/del")
    public void delManager(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======删除專車账号!======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JSONObject responseObject = new JSONObject();
        String id = request.getParameter("id");
        int  i = spcarService.delete(Integer.valueOf(id));
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
    /**
     * 检查车牌号码是否存在
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("/checkSpcarNo")
    public void checkSpcarNo(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======检查车牌号码是否存在!======");
        String spcarNo = request.getParameter("spcarNo");
        String id = request.getParameter("id");
        if(StringUtil.empty(id)){
        	  Spcar spcar = spcarService.findBySpcarNo(spcarNo);
              if(spcar!=null){
            	   JSONObject responseObject = new JSONObject();
            	  responseObject.put("msg", "該車牌號碼已存在");
                  responseObject.put("code", "-1");
                  responseObject.put("data", new JSONObject());
                  ResponseUtils.renderJson(response, responseObject.toString());
                  return;
              }
        }
      
    }
}
