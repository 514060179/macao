package com.yinghai.macao.backstage.controller;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.StringUtil;
import com.yinghai.macao.common.model.SpcarBroadcast;
import com.yinghai.macao.common.service.SpcarBroadcastService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/broadcast")
public class SpcarBroadcastController {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SpcarBroadcastService spcarBroadcastService;
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public void saveBroadcast(HttpServletRequest request,HttpServletResponse response)throws Exception{
		log.info("==========新增推送信息==========");
		response.setContentType("application/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		Date now = new Date();
		JSONObject jsonObject = new JSONObject();
		String content = request.getParameter("broadcastContent");
		if (StringUtil.isEmpty(content)) {
			jsonObject.put("code", "101");
			jsonObject.put("msg", "內容不能為空");
		}
		String typeCode = request.getParameter("broadcastType");
		if (StringUtil.isEmpty(typeCode)) {
			jsonObject.put("code", "101");
			jsonObject.put("msg", "類型不能為空");
		}
		if (!typeCode.equals(0) || !typeCode.equals(1)) {
			jsonObject.put("code", "102");
			jsonObject.put("msg", "類型不是指定的類型");
		}
		int type = Integer.parseInt(typeCode);
		SpcarBroadcast spcarBroadcast = new SpcarBroadcast();
		spcarBroadcast.setId(0);
		spcarBroadcast.setTitle("公告");
		spcarBroadcast.setCreateTime(now);
		spcarBroadcast.setUpdateTime(now);
		spcarBroadcast.setContent(content);
		spcarBroadcast.setType(type);
		spcarBroadcastService.save(spcarBroadcast);
		jsonObject.put("code", "1");
		jsonObject.put("msg", "消息保存成功");
		pw.write(jsonObject.toString());
		pw.flush();
		pw.close();
	}
	@RequestMapping(value="update",method=RequestMethod.POST)
	public void updateBroadcast(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("==========更改推送信息==========");
		response.setContentType("application/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		Date now = new Date();
		JSONObject jsonObject = new JSONObject();
		String id = request.getParameter("broadcastId");
		if (StringUtil.isEmpty(id)) {
			jsonObject.put("code", "101");
			jsonObject.put("msg", "id不能為空");
		}
		SpcarBroadcast spcarBroadcast = spcarBroadcastService.selectById(Integer.parseInt(id));
		if (spcarBroadcast == null) {
			jsonObject.put("code", "101");
			jsonObject.put("msg", "該消息不存在");
		}
		String title = request.getParameter("broadcastTitle");
		if (!StringUtil.isEmpty(title)) {
			spcarBroadcast.setTitle(title);
		}
		String content = request.getParameter("broadcastContent");
		if (!StringUtil.isEmpty(content)) {
			spcarBroadcast.setContent(content);
		}
		String typeCode = request.getParameter("broadcastType");
		if (!StringUtil.isEmpty(typeCode)) {
			int type = Integer.parseInt(typeCode);
			spcarBroadcast.setType(type);
		}
		spcarBroadcast.setUpdateTime(now);
		spcarBroadcastService.updateById(spcarBroadcast);
		jsonObject.put("code", "1");
		jsonObject.put("msg", "消息更新成功");
		
		pw.write(jsonObject.toString());
		pw.flush();
		pw.close();
	}
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public void deleteBroadcast(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("==========刪除推送信息==========");
		response.setContentType("application/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		String id = request.getParameter("broadcastId");
		if (StringUtil.isEmpty(id)) {
			jsonObject.put("code", "101");
			jsonObject.put("msg", "id不能為空");
		}
		spcarBroadcastService.deleteById(Integer.parseInt(id));
		jsonObject.put("code", "1");
		jsonObject.put("msg", "消息刪除成功");
		pw.write(jsonObject.toString());
		pw.flush();
		pw.close();
	}
	

}
