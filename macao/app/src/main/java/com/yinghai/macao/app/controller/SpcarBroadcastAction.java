package com.yinghai.macao.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.macao.common.model.SpcarBroadcast;
import com.yinghai.macao.common.service.SpcarBroadcastService;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/spcar/broadcast")
public class SpcarBroadcastAction {
	
	@Autowired
	private SpcarBroadcastService spcarBroadcastService;
	
	@RequestMapping(value="pageList",method=RequestMethod.POST)
	public void pageList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("application/json;charset=utf-8");
		JSONObject jsonObject = new JSONObject();
		Integer pageNo = TransformUtils.toInt(request.getParameter("page"));
		Integer pageStartSize = TransformUtils.toInt(request.getParameter("pageStartSize"));
		String typeCode = request.getParameter("broadcastType");
		pageNo = pageNo == 0 ? 1 : pageNo;
		pageStartSize = pageStartSize == 0 ? 15 : pageStartSize;
		SpcarBroadcast spcarBroadcast = new SpcarBroadcast();
		if (typeCode!=null) {
			spcarBroadcast.setType(TransformUtils.toInt(typeCode));
		}
		Page<SpcarBroadcast> page = spcarBroadcastService.findAll(pageNo, pageStartSize, spcarBroadcast);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray data = JSONArray.fromObject(page.getResult(), config);
		jsonObject.put("code", "1");
		jsonObject.put("msg", "success");
		jsonObject.put("data", data);
		jsonObject.put("pages", page.getPages());
		ResponseUtils.renderJson(response, jsonObject.toString());
		
	}
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void getList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("application/json;charset=utf-8");
		JSONObject jsonObject = new JSONObject();
		String typeCode = request.getParameter("broadcastType");
		SpcarBroadcast spcarBroadcast = new SpcarBroadcast();
		if (typeCode!=null) {
			spcarBroadcast.setType(TransformUtils.toInt(typeCode));
		}
		List<SpcarBroadcast> list = spcarBroadcastService.getAll(spcarBroadcast);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray data = JSONArray.fromObject(list, config);
		jsonObject.put("code", "1");
		jsonObject.put("msg", "success");
		jsonObject.put("data", data);
		ResponseUtils.renderJson(response, jsonObject.toString());
	}

}

