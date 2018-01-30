package com.yinghai.macao.backstage.controller;

import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.backstage.service.impl.SpcarManagerService;
import com.yinghai.macao.common.model.Parameter;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.service.ParameterService;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.util.EncryptUtil;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TransformUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */
@RequestMapping("/admin/parameter")
@Controller
public class ParameterController {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ParameterService parameterService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, ModelMap model) {
		log.debug("======获取价格列表======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Parameter parameter = new Parameter();
		String id = request.getParameter("id");
		if (!StringUtil.empty(id)) {
			parameter.setId(Integer.valueOf(id));
		}

		String num = request.getParameter("page");
		Integer pageNum;
		if (StringUtil.empty(num)) {
			pageNum = 1;
		} else {
			pageNum = Integer.valueOf(num);
		}
		String size = request.getParameter("pageSize");
		Integer pageSize;
		if (StringUtil.empty(size)) {
			pageSize = 10;
		} else {
			pageSize = Integer.valueOf(size);
		}
		Page<Parameter> page = parameterService.findList(pageNum, pageSize, parameter);
		model.addAttribute("parameterList", page);
		model.addAttribute("page", page);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("recordCount", page.getTotal());
		model.addAttribute("pageCount", page.getPages());
		return "parameter/list";
	}

	@RequestMapping("/save")
	public void save(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject();
		Parameter parameter = new Parameter();

		String act = "";
		String id = request.getParameter("id");
		if (StringUtil.notEmpty(id)) {
			parameter.setId(TransformUtils.toInt(id));
			act = "UPDATE";
		} else {
			act = "ADD";
		}
		Integer hour = TransformUtils.toInt(request.getParameter("hour"));
		parameter.setHour(hour);
		Double price = TransformUtils.toDouble(request.getParameter("price"));
		parameter.setPrice((int) (price*100));
		Double coefficient = TransformUtils.toDouble(request.getParameter("coefficient"));
		parameter.setCoefficient(coefficient);
		String remark = request.getParameter("remark");
		parameter.setRemark(remark);
		String item = request.getParameter("item");
		if(StringUtil.empty(item)){
			parameter.setItem(null);
		}else{
			parameter.setItem(item);
		}
		//判断是否有相同的价格信息
		List<Parameter> list = parameterService.findSame(parameter);
		if ("ADD".equals(act)) {
			if(list!=null&&list.size()>0){
				responseObject.put("msg", "新增失败，需要新增的价格信息已经存在！");
	            responseObject.put("code", "0");
	            responseObject.put("data", new JSONObject());
	            log.error("==========parameter create fail=======");
	            ResponseUtils.renderJson(response, responseObject.toString());
	            return;
			}
			int i = parameterService.createParameter(parameter);
			if (i > 0) {
				responseObject.put("msg", "success");
	            responseObject.put("code", "1");
	            responseObject.put("data", new JSONObject());
	            log.info("==========parameter create success=======");
	            ResponseUtils.renderJson(response, responseObject.toString());
			}
		} else {
			if((list!=null&&list.size()>1)||(list!=null&&list.size()==1&&!list.get(0).getId().equals(parameter.getId()))){
				responseObject.put("msg", "更新失败，价格信息已经存在不能更新成该类型价格！");
	            responseObject.put("code", "0");
	            responseObject.put("data", new JSONObject());
	            log.error("==========parameter update fail=======");
	            ResponseUtils.renderJson(response, responseObject.toString());
	            return;
			}
			int i = parameterService.updateParameter(parameter);
			if(i>0){
				 responseObject.put("msg", "success");
 	            responseObject.put("code", "2");
 	            responseObject.put("data", new JSONObject());
 	            log.info("==========parameter update success=======");
 	            ResponseUtils.renderJson(response, responseObject.toString());
			}
		}
	}

	@RequestMapping("/edit")
	public String editOrSave(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======edit or new one manager======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String act = request.getParameter("act");
		String result = "parameter/edit";
		if ("upd".equalsIgnoreCase(act)) {
			String id = request.getParameter("id");
			if (!StringUtil.empty(id)) {
				model.addAttribute("parameter", parameterService.findById(Integer.valueOf(id)));
			} else {
				log.error("======edit one manager.id can not null======");
				model.addAttribute("msg", "id can not be null");
				return "500";
			}
		}
		return result;
	}

	@RequestMapping("/del")
	public void delManager(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======删除專車账号!======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject();
		String id = request.getParameter("id");
		int i = parameterService.delete(Integer.valueOf(id));
		if (i <= 0) {
			responseObject.put("msg", "delete fail");
			responseObject.put("code", "110");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
		} else {
			responseObject.put("msg", "success");
			responseObject.put("code", "1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
		}
	}
}
