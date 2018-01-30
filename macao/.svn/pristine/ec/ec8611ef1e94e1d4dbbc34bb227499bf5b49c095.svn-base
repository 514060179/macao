package com.yinghai.macao.app.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.macao.common.model.SpcarNews;
import com.yinghai.macao.common.service.SpcarNewsService;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * APP端獲取最新消息
 * @author Administrator
 * Created by Administrator on 2017/8/30.
 */
@Controller
@RequestMapping("spcarNews")
public class SpcarNewsController {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource
	private SpcarNewsService spcarNewsService;
	/**
	 * APP端獲取最新消息列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getNewsList", method = RequestMethod.POST)//
	public void getNewsList(HttpServletRequest request,HttpServletResponse response){
		log.info(this.getClass().toString() + "==========獲取最新消息列表===========");
		response.setContentType("application/json;charset=utf-8");
		//獲取類型：乘客1，司機2
		String t = request.getParameter("type");
		Integer type = StringUtil.empty(t)||!"1".equals(t.trim())?2:1;
		Integer pageNo = TransformUtils.toInt(request.getParameter("pageNo"));
        pageNo = pageNo==0?1:pageNo;
        Integer pageSize = TransformUtils.toInt(request.getParameter("pageSize"));
        pageSize = pageSize==0?10:pageSize;
		SpcarNews news = new SpcarNews();
		//發佈截止日期要晚於當前時間，發佈日期要早於當前時間
		Date now = new Date();
		news.setPublishSince(now);
		news.setPublishTill(now);
		news.setPushLatestTime(now);
		if(type==1){//乘客
			news.setRealm("passenger");
		}else{//司機
			news.setRealm("driver");
		}
		Page<SpcarNews> list = spcarNewsService.findListSelective(pageNo,pageSize,news);
		//System.out.println("內容"+list);
		JSONObject responseObject = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray data = JSONArray.fromObject(list, config);
		responseObject.put("msg", "success");
		responseObject.put("code", "1");
		responseObject.put("data", data);
		ResponseUtils.renderJson(response, responseObject.toString());
	}
	
}
