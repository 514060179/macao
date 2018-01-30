package com.yinghai.macao.backstage.controller;

import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.backstage.service.impl.SpcarManagerService;
import com.yinghai.macao.common.model.Parameter;
import com.yinghai.macao.common.model.SpcarComment;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.service.ParameterService;
import com.yinghai.macao.common.service.SpcarCommentService;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.service.SpcarOrderService;
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
@RequestMapping("/admin/spcarcomment")
@Controller
public class SpcarCommentController {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SpcarCommentService spcarCommentService;
	@Autowired
	private SpcarOrderService spcarOrderService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, ModelMap model) {
		log.debug("======获取评论列表======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SpcarComment spcarComment = new SpcarComment();
		String id = request.getParameter("id");
		if (!StringUtil.empty(id)) {
			spcarComment.setSpcarCommentId(TransformUtils.toInt(id));
		}
		String score = request.getParameter("score");
		if (!StringUtil.empty(score)) {
			spcarComment.setScore(TransformUtils.toDouble(score));
		}
		String tel = request.getParameter("spcarPassengerTel");
	
		  if(!StringUtil.empty(tel)){
			  spcarComment.setSpcarPassengerTel("%"+tel.trim()+"%");;
	        }
		String orderId = request.getParameter("orderId");
		if (!StringUtil.empty(orderId)) {
			spcarComment.setOrderId(TransformUtils.toInt(orderId));
		}
		//根据司机查询对应评论
		String driverId = request.getParameter("driverId");
		if(!StringUtil.empty(driverId)){
			spcarComment.setSpcarDriverId(TransformUtils.toInt(driverId));
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
		Page<SpcarComment> page = spcarCommentService.findList(pageNum, pageSize, spcarComment);
		model.addAttribute("spcarCommentList", page);
		model.addAttribute("page", page);
	    if(spcarComment.getSpcarPassengerTel()!=null&&spcarComment.getSpcarPassengerTel().startsWith("%")){
	    	spcarComment.setSpcarPassengerTel(tel.trim());
        }
		model.addAttribute("spcarComment", spcarComment);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("recordCount", page.getTotal());
		model.addAttribute("pageCount", page.getPages());
		return "spcarcomment/list";
	}
	@RequestMapping("/del")
	public void delManager(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======删除!======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject();
		String id = request.getParameter("id");
		int i = spcarCommentService.delete(Integer.valueOf(id));
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
