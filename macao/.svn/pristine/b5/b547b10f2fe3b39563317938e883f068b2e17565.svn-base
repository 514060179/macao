package com.yinghai.macao.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.macao.common.model.SpcarComment;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.service.SpcarCommentService;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.service.SpcarPassengerService;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TransformUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/spcar/comment")
public class SpcarCommentAction {
	
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	SpcarCommentService spcarCommentService;
	@Autowired
	SpcarPassengerService spcarPassengerService;

	@Autowired
	SpcarOrderService spcarOrderService;
	@RequestMapping(value = "submit", method = RequestMethod.POST)
	public void submit(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException
	{
		log.info("======新增评论============================" + new Date());
		response.setContentType("application/json;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		JSONObject  responseObject = new JSONObject();
	
	
		String score = request.getParameter("score");
		if(StringUtil.empty(score)){
			responseObject.put("msg", "score is null");
            responseObject.put("code", "101");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
		}
		String orderId = request.getParameter("orderId");
		if(StringUtil.empty(orderId)){
			responseObject.put("msg", "orderId is null");
            responseObject.put("code", "101");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
		}
		SpcarOrder spcarOrder = spcarOrderService.findOneBykey(TransformUtils.toInt(orderId));
		if(spcarOrder==null){
			responseObject.put("msg", "spcarOrder is null");
            responseObject.put("code", "102");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
		}
		SpcarComment spcarComment = new SpcarComment();
		
		String comments = request.getParameter("comments");
		if(StringUtil.notEmpty(comments)){
			log.debug("。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。"+comments);
			spcarComment.setComments(comments);
		}
		spcarComment.setSpcarPassengerId(spcarOrder.getPassengerId());
		
		spcarComment.setOrderId(TransformUtils.toInt(orderId));

		spcarComment.setScore(TransformUtils.toDouble(score));
		spcarComment.setCreateTime(new Date());
		spcarComment.setSpcarPassengerName(spcarOrder.getSpcarPassenger().getName());
		spcarComment.setSpcarPassengerTel(spcarOrder.getSpcarPassenger().getTel());
		spcarComment.setSpcarDriverId(spcarOrder.getDriverId());
	
		int i = spcarCommentService.createSpcarComment(spcarComment);
		if(i>0){
			responseObject.put("msg", "success");
            responseObject.put("code", "1");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
		}else{
			responseObject.put("msg", "create spcarComment fail");
            responseObject.put("code", "000");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
		}
	 	 	
	}
	
	@RequestMapping(value = "isexist", method = RequestMethod.POST)
	public void isExist(HttpServletRequest request,HttpServletResponse response)
	{
		log.info("======新增评论============================" + new Date());
		response.setContentType("application/json;charset=utf-8");
		JSONObject  responseObject = new JSONObject();
		String orderId = request.getParameter("spCarOrderId");
		SpcarComment spcarComment = new SpcarComment();
		if(StringUtil.empty(orderId)){
			responseObject.put("msg", "spCarOrderId is null");
            responseObject.put("code", "101");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
		}
		spcarComment.setOrderId(TransformUtils.toInt(orderId));
		Page<SpcarComment> page = spcarCommentService.findList(1, 1, spcarComment);
		if(page.getResult()!=null&&page.getResult().size()!=0){
			responseObject.put("msg", "exist");
            responseObject.put("code", "1");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
		}else{
			responseObject.put("msg", "not exist");
            responseObject.put("code", "0");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
            return;
		}
	}
}
