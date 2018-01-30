package com.yinghai.macao.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.macao.app.vo.ResponseVo;
import com.yinghai.macao.common.model.PoiAddress;
import com.yinghai.macao.common.service.SpcarPoiService;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.TransformUtils;

import jxl.common.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/spcar/poi")
public class SpcarPoiController {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SpcarPoiService spcarPoiService;
	//根据条件查询poi列表
	@RequestMapping(value="/list",method = RequestMethod.POST)
	public void getPoiList(HttpServletRequest request,HttpServletResponse response){
		log.info("======获取poi列表======");
		//根据名称和类型查询poi列表
		String name = request.getParameter("name");//输入
		String type = request.getParameter("type");//类型
		if(name==null&&type==null){
			ResponseVo.send101Code(response, "参数为空");
			return;
		}
		Integer page = TransformUtils.toInt(request.getParameter("page"));
		Integer pageSize = TransformUtils.toInt(request.getParameter("pageSize"));
		if(page<1){
			page = 1;
		}
		if(pageSize<1){
			pageSize = 10;
		}
		PoiAddress p = new PoiAddress();
		p.setName(name);
		p.setType(type);
		//根据输入或类型查询poi列表
		Page<PoiAddress> list = spcarPoiService.findListByPage(page,pageSize,p);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject obj = new JSONObject();
		JSONArray arr = JSONArray.fromObject(list, config);
		obj.put("poi", arr);
		obj.put("totalPage", list.getPages());
		obj.put("totalRecord", list.getTotal());
		obj.put("pageNum", list.getPageNum());
		obj.put("startRow", list.getStartRow());
		obj.put("endRow", list.getEndRow());
		ResponseVo.send1Code(response, "success", obj);
	}
}
