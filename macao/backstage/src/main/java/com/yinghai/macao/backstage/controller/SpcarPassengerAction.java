package com.yinghai.macao.backstage.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.jersey.samples.freemarker.Main;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.backstage.service.impl.SpcarManagerService;
import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.service.SpcarPassengerService;
import com.yinghai.macao.common.util.EncryptUtil;
import com.yinghai.macao.common.util.HttpRequester;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TransformUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/passenger")
public class SpcarPassengerAction {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SpcarPassengerService spcarPassengerService;
	@Autowired
	private SpcarOrderService spcarOrderService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, ModelMap model) {
		log.debug("======获取管理员列表======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SpcarPassenger spcarPassenger = new SpcarPassenger();
		String id = request.getParameter("id");
		if (!StringUtil.empty(id)) {
			spcarPassenger.setSpcarId(Integer.valueOf(id));
		}
		String status = request.getParameter("query.status");
		if (!StringUtil.empty(status)) {
			spcarPassenger.setStatus(TransformUtils.toInt(status));
		}
		String countryCode = request.getParameter("query.countryCode");
		if (!StringUtil.empty(countryCode)) {
			spcarPassenger.setCountryCode(countryCode.trim());
			;
		}
		String tel = request.getParameter("query.tel");
		if (!StringUtil.empty(tel)) {
			spcarPassenger.setTel("%" + tel.trim() + "%");
			;
		}
		String name = request.getParameter("query.name");
		if (!StringUtil.empty(name)) {
			spcarPassenger.setName("%" + name.trim() + "%");
			;
		}
		String category = request.getParameter("query.category");
		if (!StringUtil.empty(category)) {
			spcarPassenger.setCategory("%" + category.trim() + "%");
		}
		String sex = request.getParameter("query.sex");
		if (!StringUtil.empty(sex)) {
			spcarPassenger.setSex(Boolean.valueOf(sex).booleanValue());
		} else {
			spcarPassenger.setSex(null);
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
		Page<SpcarPassenger> page = spcarPassengerService.findList(pageNum, pageSize, spcarPassenger);
		model.addAttribute("managerList", page);
		model.addAttribute("page", page);
		if (spcarPassenger.getTel() != null && spcarPassenger.getTel().startsWith("%")) {
			spcarPassenger.setTel(tel.trim());
		}
		if (spcarPassenger.getName() != null && spcarPassenger.getName().startsWith("%")) {
			spcarPassenger.setName(name.trim());
		}
		if (spcarPassenger.getCategory() != null && spcarPassenger.getCategory().startsWith("%")) {
			spcarPassenger.setCategory(category.trim());
		}

		model.addAttribute("spcarPassenger", spcarPassenger);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("recordCount", page.getTotal());
		model.addAttribute("pageCount", page.getPages());
		return "passenger/list";
	}

	@RequestMapping("/edit")
	public String editOrSave(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======edit or new one passenger======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String act = request.getParameter("act");
		String result = "passenger/edit";
		if ("upd".equalsIgnoreCase(act)) {
			String id = request.getParameter("id");
			if (!StringUtil.empty(id)) {
				model.addAttribute("passenger", spcarPassengerService.findById(TransformUtils.toInt(id)));
				// 获取乘客最近10个订单
				SpcarOrder order = new SpcarOrder();
				order.setPassengerId(TransformUtils.toInt(id));
				order.setStatus(SpcarOrder.ORDER_FINISH_STATUS);
				List<SpcarOrder> list = spcarOrderService.findList(order);
				List<SpcarOrder> passengerOrderList = null;
				Integer size = 0;
				if (list != null && list.size() > 10) {
					passengerOrderList = list.subList(0, 10);
				}
				if (list != null && list.size() > 0 && list.size() < 11) {
					passengerOrderList = list;
				}
				if (list != null) {
					size = list.size();
				}
				model.addAttribute("passengerOrderList", passengerOrderList);
				model.addAttribute("size", size);
			} else {
				log.error("======edit one manager.id can not null======");
				model.addAttribute("msg", "id can not be null");
				return "500";
			}
		}
		return result;
	}

	// addorDelVip
	@RequestMapping("/addorDelVip")
	public void addorDelVip(HttpServletRequest request, ModelMap model, HttpServletResponse response) {
		log.debug("======获取管理员列表======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject();
		String id = request.getParameter("id");
		if (StringUtil.empty(id)) {
			log.error("id is null");
			responseObject.put("msg", "id is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
		}
		SpcarPassenger spcarPassenger = spcarPassengerService.findById(TransformUtils.toInt(id));
		if (spcarPassenger == null) {
			log.error("spcarPassenger is null");
			responseObject.put("msg", "spcarPassenger is null");
			responseObject.put("code", "102");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
		}
		SpcarPassenger updateSpcarPassenger = new SpcarPassenger();
		updateSpcarPassenger.setSpcarId(TransformUtils.toInt(id));
		if(spcarPassenger.getVip()){
			updateSpcarPassenger.setVip(false);
		}else{
			updateSpcarPassenger.setVip(true);
		}
		int i = spcarPassengerService.updateSpcarPaaenger(updateSpcarPassenger);
		if (i == 1) {
			responseObject.put("msg", "success");
			responseObject.put("code", "1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
		}

	}

	@RequestMapping("/save")
	public void save(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======新增或更新乘客信息======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SpcarPassenger spcarPassenger = null;
		JSONObject responseObject = new JSONObject();
		String countryCode = request.getParameter("countryCode");// 區號
		if (StringUtil.empty(countryCode)) {
			responseObject.put("msg", "countryCode is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String tel = request.getParameter("tel");// 手機
		if (StringUtil.empty(tel)) {
			responseObject.put("msg", "tel is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String givenName = request.getParameter("givenName");// 名
		if (StringUtil.empty(givenName)) {
			responseObject.put("msg", "givenName is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String familyName = request.getParameter("familyName");// 姓
		if (StringUtil.empty(familyName)) {
			responseObject.put("msg", "familyName is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String sex = request.getParameter("sex");// 性別
		if (StringUtil.empty(sex)) {
			responseObject.put("msg", "sex is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String isTitle = request.getParameter("isTitle");// 是否屬於公司
		if (StringUtil.empty(isTitle)) {
			responseObject.put("msg", "isTitle is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}

		String vip = request.getParameter("vip");// VIP
		if (StringUtil.empty(vip)) {
			responseObject.put("msg", "vip is null");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String alias = request.getParameter("alias").replaceAll("；", ";");// 別名
		// givenName familyName
		String id = request.getParameter("id");
		// String act = request.getParameter("act");
		if (StringUtil.empty(id)) {// id為空時為新增數據
			// 新增前先判斷該手機號是否存在
			spcarPassenger = spcarPassengerService.findByTel(countryCode, tel);
			if (spcarPassenger != null) {
				responseObject.put("msg", "該乘客已存在，不能再次新增！");
				responseObject.put("code", "110");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
			
			String remark = request.getParameter("remark");
			String category = request.getParameter("category");
			SpcarPassenger spPassenger = new SpcarPassenger();
			spPassenger.setRemark(remark);
			spPassenger.setCategory(category);
			spPassenger.setCountryCode(countryCode.trim());
			spPassenger.setTel(tel.trim());
			spPassenger.setGivenName(givenName.trim());
			spPassenger.setName(familyName.trim() + givenName.trim());
			spPassenger.setLocX(0.0);
			spPassenger.setLocY(0.0);
			spPassenger.setAlias(alias);
			spPassenger.setCreateTime(new Date());
			spPassenger.setStatus(999);
			spPassenger.setPassword("");
			spPassenger.setImName("");
			spPassenger.setSign("");
			spPassenger.setDeviceType("");
			spPassenger.setDeviceId("");
			spPassenger.setDeleted(false);
			spPassenger.setPassengerId("");
			spPassenger.setOrderCount(0);// 下单数
			spPassenger.setFinishCount(0);// 完成单数
			spPassenger.setCancelCount(0);// 取消单数
			spPassenger.setTotalConsume(0);// 消费金额
			spPassenger.setVerification("");
			spPassenger.setFamilyName(familyName.trim());
			spPassenger.setSex("true".equals(sex) ? true : false);
			spPassenger.setVip("true".equals(vip) ? true : false);
			spPassenger.setTitle("true".equals(isTitle) ? "2" : ("true".equals(sex) ? "1" : "0"));
			int i = spcarPassengerService.addSpcarPassenger(spPassenger);
			if (i < 1) {
				responseObject.put("msg", "乘客新增失败！");
				responseObject.put("code", "106");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			} else {
				responseObject.put("msg", "新增成功！");
				responseObject.put("code", "1");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
		} else {// 修改乘客信息
				// 同时修改专车乘客信息和TaxiGo中用户和乘客信息
			spcarPassenger = spcarPassengerService.findById(TransformUtils.toInt(id));
			if (spcarPassenger == null) {
				responseObject.put("msg", "編輯乘客失敗，該乘客不存在！");
				responseObject.put("code", "102");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
			String remark = request.getParameter("remark");
			String category = request.getParameter("category");
			SpcarPassenger spPassenger = new SpcarPassenger();
			spPassenger.setSpcarId(spcarPassenger.getSpcarId());
			spPassenger.setRemark(remark);
			spPassenger.setCategory(category);
			spPassenger.setCountryCode(countryCode);
			spPassenger.setTel(tel);
			if(StringUtil.notEmpty(alias)){
				spPassenger.setAlias(alias);
			}
			spPassenger.setGivenName(givenName);
			spPassenger.setFamilyName(familyName);
			spPassenger.setName(familyName+givenName);
			spPassenger.setSex("true".equals(sex) ? true : false);
			spPassenger.setVip("true".equals(vip) ? true : false);
			spPassenger.setTitle("true".equals(isTitle) ? "2" : ("true".equals(sex) ? "1" : "0"));
			try {
				int i = spcarPassengerService.updateSpcarPaaenger(spPassenger);
				if (i == 1) {
					responseObject.put("msg", "success！");
					responseObject.put("code", "2");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				} else {
					responseObject.put("msg", "編輯乘客信息出錯！");
					responseObject.put("code", "110");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				}
			} catch (Exception e) {
				log.error("BackSpcarOrder/save============================編輯乘客信息出錯" + e);
				responseObject.put("msg", "編輯乘客信息出錯！");
				responseObject.put("code", "110");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}

		}
	}
	
	@RequestMapping("/toGetAlias")
	public void toGetAlias(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======新增或更新乘客信息======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject();
		String tel = request.getParameter("tel");
		String countryCode = request.getParameter("countryCode");
		SpcarPassenger spcarPassenger = spcarPassengerService.findByTel(countryCode, tel);
		if(spcarPassenger==null||StringUtil.empty(spcarPassenger.getAlias())){
			responseObject.put("msg", "success");
			responseObject.put("code", "2");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}else{
			responseObject.put("msg", "success");
			responseObject.put("code", "1");
			responseObject.put("data", spcarPassenger.getAlias());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		
	}

}
