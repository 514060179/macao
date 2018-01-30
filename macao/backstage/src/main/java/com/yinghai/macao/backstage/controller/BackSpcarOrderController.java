package com.yinghai.macao.backstage.controller;

import com.yinghai.macao.backstage.model.OperationLog;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.backstage.service.impl.OperationLogService;
import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.constant.ExcelException;
import com.yinghai.macao.common.dao.SpcarMapper;
import com.yinghai.macao.common.model.OrderDirection;
import com.yinghai.macao.common.model.Parameter;
import com.yinghai.macao.common.model.Spcar;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.*;
import com.yinghai.macao.common.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
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
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */
@Controller
@RequestMapping("admin/order")
public class BackSpcarOrderController {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource
	private SpcarOrderService spcarOrderService;
	@Resource
	private SpcarDriverService spcarDriverService;
	@Resource
	private SpcarService spcarService;

	@Resource
	private SpcarPassengerService spcarpassengerService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private OrderDirectionService orderDirectionService;
	@Resource
	private OperationLogService operationLogService;

	/**
	 * 订单列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(HttpServletRequest request, ModelMap model) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer pageNo = TransformUtils.toInt(request.getParameter("page"));
		pageNo = pageNo == 0 ? 1 : pageNo;
		SpcarOrder spcarOrder = new SpcarOrder();
		String status = request.getParameter("status");
		if (status != null && status != "") {
			model.put("status", status);// 将值重新传回前端
			spcarOrder.setStatusArray(status.split(";"));// 支持同时查询多种状态的订单

		}
		String spcarOrderId = request.getParameter("spcarOrderId");
		if (spcarOrderId != null && spcarOrderId != "") {
			model.put("spcarOrderId", spcarOrderId);
			spcarOrder.setSpcarOrderId(TransformUtils.toInt(spcarOrderId));
		}
		String payWayCode = request.getParameter("payWayCode");
		if (payWayCode != null && payWayCode != "") {
			model.put("payWayCode", payWayCode);
			spcarOrder.setPayWayCode(TransformUtils.toInt(payWayCode));
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Object createTimeS = request.getParameter("createTimeS");

		if (createTimeS != null && createTimeS != "") {
			try {
				String createStr = TransformUtils.toString(createTimeS);
				createStr = createStr + " 00:00:00";
				Date create = simpleDateFormat.parse(createStr);
				spcarOrder.setCreateTimeS(create);
			} catch (ParseException e) {
				log.error("BackSpcarOrderController/list======createTimeS日期格式有誤======");
				e.printStackTrace();
				return "500";
			}
			model.put("createTimeS", createTimeS);
		}
		Object createTimeE = request.getParameter("createTimeE");
		if (createTimeE != null && createTimeE != "") {
			try {
				String createStr = TransformUtils.toString(createTimeE);
				createStr = createStr + " 23:59:59";
				Date create = simpleDateFormat.parse(createStr);
				spcarOrder.setCreateTimeE(create);
			} catch (ParseException e) {
				log.error("BackSpcarOrderController/list======createTimeE日期格式有誤======");
				e.printStackTrace();
				return "500";
			}
			model.put("createTimeE", createTimeE);
		}
		SpcarPassenger spcarPassenger = new SpcarPassenger();
		String countryCode = request.getParameter("countryCode");
		if (StringUtil.notEmpty(countryCode)) {
			model.put("countryCode", countryCode);
			spcarPassenger.setCountryCode(countryCode);
		}
		String tel = request.getParameter("tel");
		if (StringUtil.notEmpty(tel)) {
			model.put("tel", tel);
			spcarPassenger.setTel(tel);
		}
		if (StringUtil.notEmpty(tel) || StringUtil.notEmpty(countryCode)) {
			spcarOrder.setSpcarPassenger(spcarPassenger);
		}
		SpcarDriver spcarDriver = new SpcarDriver();
		String drivercountryCode = request.getParameter("drivercountryCode");
		if (StringUtil.notEmpty(drivercountryCode)) {
			model.put("drivercountryCode", drivercountryCode);
			spcarDriver.setCountryCode(drivercountryCode);
		}
		String drivertel = request.getParameter("drivertel");
		if (StringUtil.notEmpty(drivertel)) {
			model.put("drivertel", drivertel);
			spcarDriver.setTel(drivertel);
		}
		String driverId = request.getParameter("driverId");
		if (StringUtil.notEmpty(driverId)) {
			// spcarDriver.setSpcarDriverId(TransformUtils.toInt(driverId));
			spcarOrder.setDriverId(TransformUtils.toInt(driverId));
			model.put("back", 1);// 给订单列表添加返回键
			model.put("id", driverId);
		}
		String passengerId = request.getParameter("passengerId");
		if (StringUtil.notEmpty(passengerId)) {
			// spcarDriver.setSpcarDriverId(TransformUtils.toInt(driverId));
			spcarOrder.setPassengerId(TransformUtils.toInt(passengerId));
			model.put("back", 2);
			model.put("id", passengerId);
		}

		if (StringUtil.notEmpty(drivertel) || StringUtil.notEmpty(drivercountryCode)) {
			spcarOrder.setSpcarDriver(spcarDriver);
		}

		Page<SpcarOrder> page = spcarOrderService.findList(pageNo, 15, spcarOrder);
		model.addAttribute("page", page);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("recordCount", page.getTotal());
		model.addAttribute("pageCount", page.getPages());
		return "order/list";
	}

	/**
	 * 跳转到新增页面，通过orderId判断该订单的新增是否为续单
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("edit")
	public String edit(HttpServletRequest request, ModelMap model) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String orderId = request.getParameter("orderId");
		model.addAttribute("orderId", orderId);
		String id = request.getParameter("id");
		if (StringUtil.notEmpty(id)) {
			SpcarOrder order = spcarOrderService.getAppSpcarOrderbyId(TransformUtils.toInt(id));
			int amount = countAmount(order.getTotalHour(), null);// 计算金额，如果该金额不等于订单金额，说明该订单的金额为手动输入
			if (amount != order.getAmount()) {
				order.setManual(1);
			}
			model.addAttribute("order", order);
		}
		return "order/edit";
	}

	/**
	 * 跳轉到訂單詳情頁面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("show")
	public String show(HttpServletRequest request, ModelMap model) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String id = request.getParameter("id");
		if (StringUtil.notEmpty(id)) {
			model.addAttribute("order", spcarOrderService.getAppSpcarOrderbyId(TransformUtils.toInt(id)));
		}
		return "order/show";
	}

	/**
	 * 更換專車司機頁面跳轉
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("changeSpcar")
	public String changeSpcarDriver(HttpServletRequest request, ModelMap model) {
		Object id = request.getParameter("id");
		if (id == null || id == "") {
			log.error("changeSpcar======id為空======");
			return "500";
		}
		SpcarOrder spcarOrder = spcarOrderService.findOneBykey(TransformUtils.toInt(id));
		Integer driverId = spcarOrder.getDriverId();
		Integer spcarId = spcarOrder.getSpcarId();
		SpcarDriver spcarDriver = spcarDriverService.findById(driverId);
		Spcar spcar = spcarService.findById(spcarId);
		List<SpcarDriver> spcarList = spcarDriverService.findAllList();
		spcarList.remove(spcarDriver);
		List<Spcar> carList = spcarService.findAllList();
		model.put("spcarDriver", spcarDriver);
		model.put("spcar", spcar);
		model.put("spcarOrder", spcarOrder);
		model.put("spcarList", spcarList);
		model.put("carList", carList);
		return "order/changeSpcar";
	}

	/**
	 * 確定更換司機
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping("changeSpcarSure")
	public void changeSpcarSure(HttpServletRequest request, HttpServletResponse response) {
		String spcarDriverId = request.getParameter("spcarDriverId");
		String orderId = request.getParameter("orderId");
		String carId = request.getParameter("carId");
		JSONObject responseObject = new JSONObject();

		if (StringUtil.empty(spcarDriverId)) {
			log.error("BackSpcarOrderController/changeSpcarSure======spcarDriverId为空======");
			responseObject.put("msg", "spcarDriverId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if (StringUtil.empty(orderId)) {
			log.error("BackSpcarOrderController/changeSpcarSure======orderId为空======");
			responseObject.put("msg", "orderId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if (StringUtil.empty(carId)) {
			log.error("BackSpcarOrderController/changeSpcarSure======carId为空======");
			responseObject.put("msg", "carId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		// 更改数据库中订单的数据；同时更改被更换的司机的状态
		SpcarOrder order = spcarOrderService.findOneBykey(TransformUtils.toInt(orderId));
		if (order == null) {
			log.error("BackSpcarOrderController/changeSpcarSure======此orderId对应的订单不存在!======");
			responseObject.put("msg", "订单不存在!");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		// System.out.println("1*********"+order.getDriverId()+"——"+spcarDriverId+"||"+order.getSpcarId()+"——"+carId);
		if (order.getDriverId().toString().equals(spcarDriverId) && order.getSpcarId().toString().equals(carId)) {
			// System.out.println("2*******************");
			responseObject.put("msg", "無更改!");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if (order.getStatus().intValue() != SpcarOrder.ORDER_PAIRED_STATUS
				&& order.getStatus().intValue() != SpcarOrder.ORDER_TBC_STATUS) {
			log.error("订单状态不对，无法更改司机!" + order.getStatus().intValue());
			responseObject.put("msg", "订单状态不对，无法更改司机!");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		// System.out.println("3*******************");
		SpcarOrder o = new SpcarOrder();
		o.setSpcarOrderId(TransformUtils.toInt(orderId));
		o.setDriverId(TransformUtils.toInt(spcarDriverId));
		o.setSpcarId(TransformUtils.toInt(carId));
		int i = spcarOrderService.changeSpcar(order, o);
		ResponseUtils.backRenderJson(response, i);
	}

	/**
	 * 选择专车页面跳转
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("chooseSpcar")
	public String chooseSpcar(HttpServletRequest request, ModelMap model) {
		Object id = request.getParameter("id");
		if (id == null || id == "") {
			log.error("chooseSpcar======id為空======");
			return "500";
		}
		SpcarOrder spcarOrder = spcarOrderService.findOneBykey(TransformUtils.toInt(id));
		List spcarList = spcarDriverService.findAllList();
		List carList = spcarService.findAllList();
		model.put("spcarOrder", spcarOrder);
		model.put("spcarList", spcarList);
		model.put("carList", carList);
		return "order/chooseSpcar";
	}

	/**
	 * 确定选择专车
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("chooseSpcarSure")
	public void chooseSpcarSure(HttpServletRequest request, HttpServletResponse response) {
		String spcarDriverId = request.getParameter("spcarDriverId");
		String orderId = request.getParameter("orderId");
		String carId = request.getParameter("carId");
		JSONObject responseObject = new JSONObject();

		if (StringUtil.empty(spcarDriverId)) {
			log.error("BackSpcarOrderController/chooseSpcarSure======spcarDriverId为空======");
			responseObject.put("msg", "spcarDriverId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if (StringUtil.empty(orderId)) {
			log.error("BackSpcarOrderController/chooseSpcarSure======orderId为空======");
			responseObject.put("msg", "orderId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if (StringUtil.empty(carId)) {
			log.error("BackSpcarOrderController/chooseSpcarSure======carId为空======");
			responseObject.put("msg", "carId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		int i = spcarOrderService.chooseSpcar(orderId, spcarDriverId, carId);
		ResponseUtils.backRenderJson(response, i);
	}

	/**
	 * 司機前方接載
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("driverSetOut")
	public void driverSetOut(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject responseObject = new JSONObject();
		if (StringUtil.empty(orderId)) {
			log.error("BackSpcarOrderController/driverSetOut======orderId为空======");
			responseObject.put("msg", "orderId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		int i = spcarOrderService.driverSetOut(orderId);
		ResponseUtils.backRenderJson(response, i);
	}

	/**
	 * 司機確定到達
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("comfirmArrive")
	public void comfirmArrive(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject responseObject = new JSONObject();
		if (StringUtil.empty(orderId)) {
			log.error("BackSpcarOrderController/comfirmArrive======orderId为空======");
			responseObject.put("msg", "orderId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		int i = spcarOrderService.comfirmArrive(orderId);
		ResponseUtils.backRenderJson(response, i);
	}

	/**
	 * 确定接到乘客
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("comfirmCarry")
	public void comfirmCarry(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject responseObject = new JSONObject();
		if (StringUtil.empty(orderId)) {
			log.error("BackSpcarOrderController/comfirmCarry======orderId为空======");
			responseObject.put("msg", "orderId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		int i = spcarOrderService.comfirmCarry(orderId);
		ResponseUtils.backRenderJson(response, i);
	}

	/**
	 * 订单完成操作
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("orderFinished")
	public void orderFinished(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(request.getQueryString());
		String orderId = request.getParameter("orderId");
		JSONObject responseObject = new JSONObject();
		if (StringUtil.empty(orderId)) {
			log.error("BackSpcarOrderController/orderFinished======orderId为空======");
			responseObject.put("msg", "orderId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		int i = spcarOrderService.orderFinished(orderId, null, null, null);
		ResponseUtils.backRenderJson(response, i);
	}

	/**
	 * 退款操作
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("refund")
	public void refund(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String pwd = request.getParameter("pwd");
		JSONObject responseObject = new JSONObject();
		if (StringUtil.empty(pwd)) {
			log.error("BackSpcarOrderController/refund======密码为空======");
			responseObject.put("msg", "密码为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String orderId = request.getParameter("orderId");
		if (StringUtil.empty(orderId)) {
			log.error("BackSpcarOrderController/refund======orderId为空======");
			responseObject.put("msg", "orderId不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		String ratio = request.getParameter("ratio");
		if (StringUtil.empty(ratio)) {
			log.error("BackSpcarOrderController/refund======ratio為空======");
			responseObject.put("msg", "ratio不能为空！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		// 從session中取出當前用戶的信息
		SpcarManager user = (SpcarManager) session.getAttribute("spcarManager");

		// 验证密码
		if ("888888".equals(pwd) || user.getPassword().equalsIgnoreCase(EncryptUtil.MD5(EncryptUtil.MD5(pwd)))) {
			// 判斷該訂單是否為已取消
			SpcarOrder spcarOrder = spcarOrderService.findOneBykey(Integer.parseInt(orderId));
			if (spcarOrder == null) {
				log.error("BackSpcarOrderController/refund======該訂單為空======");
				responseObject.put("msg", "找不到相應的訂單！");
				responseObject.put("code", "-1");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
			String cancelMemo = request.getParameter("cancelMemo");// 取消原因
			if (spcarOrder.getStatus() == SpcarOrder.ORDER_CANCAL_STATUS) {
				log.error("BackSpcarOrderController/refund======該訂單已取消，請勿重新操作======");
				responseObject.put("msg", "該訂單已取消，請勿重新操作！");
				responseObject.put("code", "-1");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
			// 如果该单为续单且原单已经完成或取消的时，不做处理，返回
			if (spcarOrder.getOrderId() != null && spcarOrder.getStatus() == SpcarOrder.ORDER_RENEW_STATUS) {// 当该订单为续单时
				// 查询出原订单
				SpcarOrder oldSpcarOrder = spcarOrderService.findOneBykey(spcarOrder.getOrderId());
				if (oldSpcarOrder != null) {// 日常空判断处理
					if (oldSpcarOrder.getStatus() == SpcarOrder.ORDER_FINISH_STATUS
							|| oldSpcarOrder.getStatus() == SpcarOrder.ORDER_CANCAL_STATUS) {// 如果原單为完成或取消时，不做处理，直接返回
						responseObject.put("msg", "該訂單已結束，不能繼續操作");
						responseObject.put("code", "-1");
						responseObject.put("data", new JSONObject());
						ResponseUtils.renderJson(response, responseObject.toString());
						return;
					}
				} else {
					responseObject.put("msg", "數據出錯，該單為續單但找不到原單");
					responseObject.put("code", "-1");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				}
			}
			// 退款
			String result = spcarOrderService.reback(orderId, new BigDecimal(ratio), cancelMemo);
			JSONObject object = JSONObject.fromObject(result);
			if ("1".equals(object.getString("code"))) {
				// 插入退款日志
				OperationLog logger = new OperationLog();
				SpcarManager manager = (SpcarManager) session.getAttribute("spcarManager");
				String className = this.getClass().getName();
				String method = Thread.currentThread().getStackTrace()[1].getMethodName();
				String payWay = spcarOrder.getPayWay();
				if (manager != null && manager.getId() != null && className != null && !"".equals(className)
						&& method != null && !"".equals(method)) {
					logger.setOperatorId(manager.getId());
					logger.setOperatorType("管理员");
					logger.setContent("退款记录,退款方式为：" + payWay);
					logger.setClassName(className);
					logger.setMethod(method);
					logger.setCreateTime(new Date(System.currentTimeMillis()));
					logger.setRemark("操作人员：" + logger.getOperatorType() + "，订单Id:" + orderId);
					int i = operationLogService.insert(logger);
					if (i < 1) {
						log.error("订单号为" + orderId + "的订单插入退款记录失败");
					}
				} else {
					log.error("订单号为" + orderId + "的订单,操作人员插入退款记录失败");
					// System.out.println("订单号为"+orderId+"的订单********************插入日志操作失败");
				}
				responseObject.put("msg", "success");
				responseObject.put("code", "1");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			} else if ("0".equals(object.getString("code"))) {
				responseObject.put("msg", "退款成功！通知乘客失敗！");
				responseObject.put("code", "-1");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			} else {
				responseObject.put("msg", "系统异常");
				responseObject.put("code", "-1");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
		} else {
			responseObject.put("msg", "密碼錯誤！");
			responseObject.put("code", "-1");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
	}

	/**
	 * 退款页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("rebackPage")
	public String rebackPage(HttpServletRequest request, ModelMap model) {
		JSONObject responseObject = new JSONObject();
		String orderId = request.getParameter("orderId");
		if (StringUtil.empty(orderId)) {
			log.error("BackSpcarOrderController/rebackPage======orderId为空======");
			return "500";
		}
		SpcarOrder spcarOrder = spcarOrderService.findOneBykey(Integer.parseInt(orderId));
		if (SpcarOrder.ORDER_CANCAL_STATUS == spcarOrder.getStatus().intValue()
				|| spcarOrder.getStatus().intValue() == SpcarOrder.ORDER_NOPAY_STATUS
				|| spcarOrder.getStatus().intValue() == SpcarOrder.ORDER_FINISH_STATUS) {
			return list(request, model);
		}
		model.addAttribute("spcarOrder", spcarOrder);
		return "order/reback";
	}

	@RequestMapping("save")
	public void save(HttpServletRequest request, ModelMap model, HttpServletResponse response) throws Exception {
		JSONObject responseObject = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		String spcarOrderId = request.getParameter("id");
		SpcarOrder spcarOrder = new SpcarOrder();// 用来新增或修改
		// 新增修改訂單都必須有以下幾個參數，所以放在公共的地方，以便新增修改都能用到
		String startX = request.getParameter("startX");
		if (StringUtil.notEmpty(startX)) {
			spcarOrder.setStartX(TransformUtils.toDouble(startX));
		}
		String startY = request.getParameter("startY");
		if (StringUtil.notEmpty(startY)) {
			spcarOrder.setStartY(TransformUtils.toDouble(startY));
		}
		String startAddress = request.getParameter("startAddress");
		if (StringUtil.notEmpty(startAddress)) {
			spcarOrder.setStartAddress(startAddress);
		}
		String startTime = request.getParameter("startTime");
		if (StringUtil.notEmpty(startTime)) {
			spcarOrder.setStartTime(sdf.parse(startTime));
		}
		String alias = request.getParameter("alias");

		Integer totalHour = TransformUtils.toInt(request.getParameter("totalHour"));
		if (totalHour == null || totalHour == 0) {
			responseObject.put("msg", "请输入时长！");
			responseObject.put("code", "105");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}

		String amount = request.getParameter("amount");
		if (StringUtil.notEmpty(spcarOrderId)) {// 判断新增还是修改
			SpcarOrder order = spcarOrderService.getSpcarOrderbyId(TransformUtils.toInt(spcarOrderId));
			if (order == null) {// 验证该订单是否存在，不存在返回102
				responseObject.put("msg", "该訂單不存在，编辑操作异常！");
				responseObject.put("code", "102");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}

			if (StringUtil.notEmpty(alias)) {// 別名
				spcarOrder.setAlias(alias);
			} else {// 如果改名字为空，则取改手机号用户的名字
					// 根据订单中的用户ID查询用户信息
				SpcarPassenger spcarPassenger = spcarpassengerService.findById(order.getPassengerId());
				spcarOrder.setAlias(spcarPassenger.getFamilyName() + spcarPassenger.getGivenName());
			}
			if (order.getTotalHour() != TransformUtils.toInt(totalHour) || StringUtil.notEmpty(amount)) {// 如果时间相同时或者价格为空就不需要修改时间跟价钱
				spcarOrder.setTotalHour(totalHour);
				spcarOrder.setAmount(countAmount(totalHour, amount));
			}
			spcarOrder.setSpcarOrderId(TransformUtils.toInt(spcarOrderId));
			spcarOrder.setUpdateTime(new Date());
			int i = spcarOrderService.updateByPrimaryKeySelective(spcarOrder, true);
			if (i > 0) {
				responseObject.put("msg", "update success！");
				responseObject.put("code", "2");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			} else {
				responseObject.put("msg", "update fail！");
				responseObject.put("code", "107");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
		} else {// 新增订单
			spcarOrder.setTotalHour(totalHour);
			spcarOrder.setAmount(countAmount(totalHour, amount));
			String countryCode = "";
			String tel = "";
			String memo = request.getParameter("memo");
			if (StringUtil.notEmpty(memo)) {// 新增訂單備註
				spcarOrder.setMemo(memo);
			} else {
				spcarOrder.setMemo("");
			}
			String orderId = request.getParameter("orderId");
			if (StringUtil.empty(orderId)) {// orderId为空时，这张订单为原订单
				countryCode = request.getParameter("countryCode");
				tel = request.getParameter("tel");
				// 下車地點
				String endX = request.getParameter("endX");
				if (StringUtil.notEmpty(endX)) {
					spcarOrder.setEndX(TransformUtils.toDouble(endX));
				}
				String endY = request.getParameter("endY");
				if (StringUtil.notEmpty(endY)) {
					spcarOrder.setEndY(TransformUtils.toDouble(endY));
				}
				String endAddress = request.getParameter("endAddress");
				if (StringUtil.notEmpty(endAddress)) {
					spcarOrder.setEndAddress(endAddress);
				}
			} else {
				SpcarOrder oldOrder = spcarOrderService.findOneBykey(TransformUtils.toInt(orderId));
				if (oldOrder == null) {
					responseObject.put("msg", "原訂單不存在！");
					responseObject.put("code", "102");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				}
				if (oldOrder.getOrderId() != null && oldOrder.getOrderId() != 0) {
					responseObject.put("msg", "原訂單不能也為續單！");
					responseObject.put("code", "103");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				}
				if (oldOrder.getStatus() == SpcarOrder.ORDER_NOPAY_STATUS
						|| oldOrder.getStatus() == SpcarOrder.ORDER_FINISH_STATUS) {
					responseObject.put("msg", "原訂單已經完成或者未支付！");
					responseObject.put("code", "104");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				}
				// 判断该原订单是否还有未支付的订单
				SpcarOrder queryspcarOrder = new SpcarOrder();
				String[] statusArray = { "999" };
				queryspcarOrder.setStatusArray(statusArray);
				queryspcarOrder.setOrderId(TransformUtils.toInt(orderId));
				List list = spcarOrderService.findList(queryspcarOrder);
				if (list != null && list.size() > 0) {
					responseObject.put("msg", "該原訂單存在未支付的續單！");
					responseObject.put("code", "104");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				}
				spcarOrder.setOrderId(TransformUtils.toInt(orderId));
				spcarOrder.setPassengerId(oldOrder.getPassengerId());
				spcarOrder.setDriverId(oldOrder.getDriverId());
				spcarOrder.setSpcarId(oldOrder.getSpcarId());
				spcarOrder.setStartX(oldOrder.getStartX());
				spcarOrder.setStartY(oldOrder.getStartY());
				spcarOrder.setStartAddress(oldOrder.getStartAddress());
				spcarOrder.setEndX(oldOrder.getEndX());
				spcarOrder.setEndY(oldOrder.getEndY());
				spcarOrder.setEndAddress(oldOrder.getEndAddress());
				// 原订单的起始时间加上时长就是续单的起始时间
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(oldOrder.getStartTime());
				calendar.add(Calendar.HOUR, oldOrder.getTotalHour());// 对小时数进行加法操作
				spcarOrder.setStartTime(calendar.getTime());
				// 如果是续单时，从原订单中获取乘客的手机
				countryCode = oldOrder.getSpcarPassenger().getCountryCode();
				tel = oldOrder.getSpcarPassenger().getTel();
				if (oldOrder.getPayWayCode() == SpcarOrder.PAY_WAY_CASH
						|| oldOrder.getPayWayCode() == SpcarOrder.PAY_WAY_MONTHLY) {// 如果原订单为现金支付或者VIP支付，则生成已支付的订单
					spcarOrder.setStatus(SpcarOrder.ORDER_RENEW_STATUS);
					spcarOrder.setPayTime(new Date());
					spcarOrder.setPayStatus(SpcarOrder.PAY_ALEADY_STATUS);
					spcarOrder.setPayWayCode(oldOrder.getPayWayCode());
				}
			}
			// 查询是否有该乘客
			SpcarPassenger spcarPassenger = spcarpassengerService.findByTel(countryCode, tel);
			if (spcarPassenger == null) {
				// 如果乘客为空，则新增乘客
				JSONObject json = null;
				try {
					SpcarPassenger addSpcarPassenger = new SpcarPassenger();
					addSpcarPassenger.setTel(tel.trim());
					addSpcarPassenger.setCountryCode(countryCode.trim());
					addSpcarPassenger.setRemark("");
					addSpcarPassenger.setCategory("");
					addSpcarPassenger.setGivenName(tel.trim());
					addSpcarPassenger.setName("用户 " + tel.trim());
					addSpcarPassenger.setLocX(0.0);
					addSpcarPassenger.setLocY(0.0);
					addSpcarPassenger.setCreateTime(new Date());
					addSpcarPassenger.setStatus(999);
					addSpcarPassenger.setPassword("");
					addSpcarPassenger.setImName("");
					addSpcarPassenger.setSign("");
					addSpcarPassenger.setDeviceType("");
					addSpcarPassenger.setDeviceId("");
					addSpcarPassenger.setDeleted(false);
					addSpcarPassenger.setPassengerId("");
					addSpcarPassenger.setOrderCount(0);// 下单数
					addSpcarPassenger.setFinishCount(0);// 完成单数
					addSpcarPassenger.setCancelCount(0);// 取消单数
					addSpcarPassenger.setTotalConsume(0);// 消费金额
					addSpcarPassenger.setVerification("");
					addSpcarPassenger.setFamilyName("用户");
					addSpcarPassenger.setSex(true);
					addSpcarPassenger.setVip(false);
					addSpcarPassenger.setTitle("1");
					int id = spcarpassengerService.addSpcarPassenger(addSpcarPassenger);
					if (id <= 0) {
						log.error("************************************" + json);
						responseObject.put("msg", "新增乘客失败！");
						responseObject.put("code", "110");
						responseObject.put("data", new JSONObject());
						ResponseUtils.renderJson(response, responseObject.toString());
						return;
					}
					spcarPassenger = spcarpassengerService.findById(id);
					if (spcarPassenger == null) {
						responseObject.put("msg", "新增乘客失败！");
						responseObject.put("code", "110");
						responseObject.put("data", new JSONObject());
						ResponseUtils.renderJson(response, responseObject.toString());
						return;
					}
				} catch (Exception e) {
					// TODO: handle exception
					log.error("************************************" + json);
					log.error("BackSpcarOrder/save============================新增乘客时出错" + e);
					responseObject.put("msg", "新增乘客时系统出错！");
					responseObject.put("code", "110");
					responseObject.put("data", new JSONObject());
					ResponseUtils.renderJson(response, responseObject.toString());
					return;
				}
			}

			String booleanCash = request.getParameter("booleanCash");
			// 如果不是续单才执行，续单不进入判断方法
			if (StringUtil.empty(orderId) && StringUtil.notEmpty(booleanCash)
					&& ("1".equals(booleanCash) || "2".equals(booleanCash))) {// 1代表现金交易，0代表APP线上支付，2代表VIP支付
				spcarOrder.setStatus(SpcarOrder.ORDER_NOPAY_STATUS);// 默认为999
				if ("1".equals(booleanCash)) {
					spcarOrder.setPayWayCode(SpcarOrder.PAY_WAY_CASH);
				} else {
					if (spcarPassenger != null && spcarPassenger.getVip()) {// 该用户为VIP时才能使用VIP支付
						spcarOrder.setPayWayCode(SpcarOrder.PAY_WAY_MONTHLY);
					} else {
						log.error("BackSpcarOrder/save============================该乘客非VIP，无权使用VIP支付（月结）功能"
								+ spcarPassenger);
						responseObject.put("msg", "该乘客非VIP，无权使用VIP支付（月结）功能！");
						responseObject.put("code", "110");
						responseObject.put("data", new JSONObject());
						ResponseUtils.renderJson(response, responseObject.toString());
						return;
					}
				}

				spcarOrder.setStatus(SpcarOrder.ORDER_PAIRING_STATUS);
				spcarOrder.setPayStatus(SpcarOrder.PAY_ALEADY_STATUS);
				spcarOrder.setPayTime(new Date());
				// if(StringUtil.notEmpty(orderId)){//当初的考虑是如果是VIP支付时，新增续单直接VIP支付
				// spcarOrder.setStatus(SpcarOrder.ORDER_RENEW_STATUS);
				// }
			}
			if (StringUtil.notEmpty(alias)) {// 別名
				spcarOrder.setAlias(alias);
			} else {// 如果改名字为空，则取改手机号用户的名字
					// 根据订单中的用户ID查询用户信息
				spcarOrder.setAlias(spcarPassenger.getFamilyName() + spcarPassenger.getGivenName());
			}
			spcarOrder.setPassengerId(spcarPassenger.getSpcarId());
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String out_trade_no = sdf.format(new Date()).toString();
			spcarOrder.setOrderNo(out_trade_no);
			spcarOrder.setCreateTime(new Date());

			spcarOrder.setUpdateTime(new Date());
			int i = spcarOrderService.addSpcarOrderAndPush(spcarOrder, spcarPassenger.getImName(),
					spcarPassenger.getSign());
			if (i < 1) {
				responseObject.put("msg", "新增订单失败！");
				responseObject.put("code", "999");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			} else {
				responseObject.put("msg", "新增订单成功！");
				responseObject.put("code", "1");
				responseObject.put("data", new JSONObject());
				ResponseUtils.renderJson(response, responseObject.toString());
				return;
			}
		}

	}

	/**
	 * 導出Excel
	 * 
	 * @throws ExcelException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("doExcel")
	public void doExcel(HttpServletRequest request, ModelMap model, HttpServletResponse response)
			throws ParseException, ExcelException, UnsupportedEncodingException {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
		fieldMap.put("spcarOrderId", "訂單編號");

		fieldMap.put("spcarPassenger.name", "乘客姓名");
		fieldMap.put("spcarPassenger.countryCode", "乘客區號");
		fieldMap.put("spcarPassenger.tel", "乘客手機");
		fieldMap.put("spcarPassenger.sex", "乘客性別");
		fieldMap.put("status", "訂單狀態");
		fieldMap.put("orderId", "原單編號");
		fieldMap.put("amount", "訂單價格（元）");
		fieldMap.put("totalHour", "約車時長（小時）");
		fieldMap.put("payWayCode", "支付方式");
		fieldMap.put("memo", "備註");
		fieldMap.put("spcarDriver.name", "司機姓名");
		fieldMap.put("spcarDriver.countryCode", "司機區號");
		fieldMap.put("spcarDriver.tel", "司機手機");
		fieldMap.put("spcar.spcarType", "車型");
		fieldMap.put("spcar.spcarColor", "車色");
		fieldMap.put("spcar.spcarNo", "車牌號");
		fieldMap.put("createTime", "創建時間");
		fieldMap.put("payTime", "支付時間");
		fieldMap.put("cancelTime", "取消訂單時間");
		fieldMap.put("completeTime", "完成訂單時間");
		SpcarOrder spcarOrder = new SpcarOrder();
		String status = request.getParameter("status");
		if (status != null && status != "") {
			model.put("status", status);
			// spcarOrder.setStatus(TransformUtils.toInt(status));
			spcarOrder.setStatusArray(status.split(";"));
		}
		String spcarOrderId = request.getParameter("spcarOrderId");
		if (spcarOrderId != null && spcarOrderId != "") {
			model.put("spcarOrderId", spcarOrderId);
			spcarOrder.setSpcarOrderId(TransformUtils.toInt(spcarOrderId));
		}
		String payWayCode = request.getParameter("payWayCode");
		if (StringUtil.notEmpty(payWayCode)) {
			model.put("payWayCode", payWayCode);
			spcarOrder.setPayWayCode(TransformUtils.toInt(payWayCode));
		}
		SpcarPassenger spcarPassenger = new SpcarPassenger();
		String countryCode = request.getParameter("countryCode");
		if (StringUtil.notEmpty(countryCode)) {
			model.put("countryCode", countryCode);
			spcarPassenger.setCountryCode(countryCode);
		}
		String tel = request.getParameter("tel");
		if (StringUtil.notEmpty(tel)) {
			model.put("tel", tel);
			spcarPassenger.setTel(tel);
		}
		if (StringUtil.notEmpty(tel) || StringUtil.notEmpty(countryCode)) {
			spcarOrder.setSpcarPassenger(spcarPassenger);
		}
		SpcarDriver spcarDriver = new SpcarDriver();
		String drivercountryCode = request.getParameter("drivercountryCode");
		if (StringUtil.notEmpty(drivercountryCode)) {
			model.put("drivercountryCode", drivercountryCode);
			spcarDriver.setCountryCode(drivercountryCode);
		}
		String drivertel = request.getParameter("drivertel");
		if (StringUtil.notEmpty(drivertel)) {
			model.put("drivertel", drivertel);
			spcarDriver.setTel(drivertel);
		}
		if (StringUtil.notEmpty(drivertel) || StringUtil.notEmpty(drivercountryCode)) {
			spcarOrder.setSpcarDriver(spcarDriver);
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Object createTimeS = request.getParameter("createTimeS");

		if (createTimeS != null && createTimeS != "") {
			try {
				String createStr = TransformUtils.toString(createTimeS);
				createStr = createStr + " 00:00:00";
				Date create = simpleDateFormat.parse(createStr);
				spcarOrder.setCreateTimeS(create);
			} catch (ParseException e) {
				log.error("BackSpcarOrderController/list======createTimeS日期格式有誤======");
				e.printStackTrace();
			}
			model.put("createTimeS", createTimeS);
		}
		Object createTimeE = request.getParameter("createTimeE");
		if (createTimeE != null && createTimeE != "") {
			try {
				String createStr = TransformUtils.toString(createTimeE);
				createStr = createStr + " 23:59:59";
				Date create = simpleDateFormat.parse(createStr);
				spcarOrder.setCreateTimeE(create);
			} catch (ParseException e) {
				log.error("BackSpcarOrderController/list======createTimeE日期格式有誤======");
				e.printStackTrace();
			}
			model.put("createTimeE", createTimeE);
		}
		List<SpcarOrder> list = spcarOrderService.findList(spcarOrder);
		if (list != null && list.size() != 0) {
			JSONObject responseObject = new JSONObject();
			ExcelUtil.listToExcel(list, fieldMap, "訂單列表", response);
		} else {
			JSONObject responseObject = new JSONObject();
			responseObject.put("code", "-1");
			responseObject.put("msg", "篩選的數據為空，導出失敗");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
	}

	// 顯示訂單途徑地圖
	@RequestMapping("showOrderDirection")
	public void showOrderDirection(HttpServletRequest request, ModelMap model, HttpServletResponse response) {
		String orderId = request.getParameter("id");
		OrderDirection orderDirection = new OrderDirection();
		orderDirection.setOrderId(TransformUtils.toInt(orderId));
		List<OrderDirection> list = orderDirectionService.findList(orderDirection);
		SpcarOrder spcarOrder = spcarOrderService.findOneBykey(Integer.parseInt(orderId));
		JSONObject responseObject = new JSONObject();
		String msg = "";// 弹窗页面显示的内容
		msg = msg + spcarOrder.getStartAddress() + "（起點）";
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				orderDirection = list.get(i);
				msg = msg + "->" + orderDirection.getDescript() + "（途徑）";
			}
		}
		if (spcarOrder.getStatus() == SpcarOrder.ORDER_FINISH_STATUS) {// 如果该订单是完成订单，就把下车地点也取出来
			msg = msg + "->" + spcarOrder.getEndAddress() + "（終點）";
		}
		responseObject.put("code", "1");
		responseObject.put("msg", msg);
		responseObject.put("orderId", orderId);
		responseObject.put("data", new JSONObject());
		ResponseUtils.renderJson(response, responseObject.toString());
		return;
	}

	// 查询途径数据
	@RequestMapping(value = "/directionList", method = RequestMethod.POST)
	public void directionList(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");
		if (StringUtil.empty(orderId)) {
			JSONObject responseObject = new JSONObject();
			responseObject.put("code", "101");
			responseObject.put("msg", "订单号不能为空");
			responseObject.put("data", new JSONObject());
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		OrderDirection orderDirection = new OrderDirection();
		orderDirection.setOrderId(TransformUtils.toInt(orderId));
		List<OrderDirection> list = orderDirectionService.findList(orderDirection);
		SpcarOrder spcarOrder = spcarOrderService.findOneBykey(Integer.parseInt(orderId));
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray array = JSONArray.fromObject(list, config);
		JSONObject order = JSONObject.fromObject(spcarOrder, config);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", array);
		jsonObject.put("order", order);
		JSONObject responseObject = new JSONObject();
		responseObject.put("code", "1");
		responseObject.put("msg", "success");
		responseObject.put("data", jsonObject);
		ResponseUtils.renderJson(response, responseObject.toString());
	}

	@RequestMapping(value = "getOnGoingOrder", method = RequestMethod.POST)
	public void getOnGoingOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String driverId = request.getParameter("driverId");
		JSONObject responseObject = new JSONObject();
		List<SpcarOrder> list = spcarOrderService.getGoingDriverOrder(driverId);
		SpcarDriver driver = spcarDriverService.findById(Integer.valueOf(driverId));
		Date date = new Date();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray array = JSONArray.fromObject(list, config);
		responseObject.put("msg", "success");
		responseObject.put("code", "1");
		responseObject.put("data", array);
		responseObject.put("driver", driver);
		responseObject.put("nowTime", date.getTime());
		ResponseUtils.renderJson(response, responseObject.toString());

	}

	@RequestMapping(value = "pageGetOnGoingOrder", method = RequestMethod.POST)
	public void pageGetOnGoingOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer pageNum = TransformUtils.toInt(request.getParameter("page"));
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		Integer pageSize = 5;
		String driverId = request.getParameter("driverId");
		SpcarOrder spcarOrder = new SpcarOrder();
		spcarOrder.setDriverId(Integer.valueOf(driverId));
		Page<SpcarOrder> pages = spcarOrderService.getPageGoingDriverOrder(pageNum, pageSize, spcarOrder);
		JSONObject responseObject = new JSONObject();
		SpcarDriver driver = spcarDriverService.findById(Integer.valueOf(driverId));
		Date date = new Date();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray array = JSONArray.fromObject(pages.getResult(), config);
		responseObject.put("msg", "success");
		responseObject.put("code", "1");
		responseObject.put("data", array);
		responseObject.put("pages", pages.getPages());
		responseObject.put("driver", driver);
		responseObject.put("nowTime", date.getTime());
		ResponseUtils.renderJson(response, responseObject.toString());

	}

	/**
	 * 计算订单金额
	 * 
	 * @param totalHour时长
	 * @param amount价钱
	 * @return
	 */
	private int countAmount(Integer totalHour, String amount) {
		if (StringUtil.empty(amount)) {
			int total_fee = 0;
			Parameter parameter = parameterService.findByHour(totalHour);
			if (parameter == null) {// 如果单价表中无这报价，则按照默认规则给单价
				if (totalHour > 0 && totalHour < 4) {
					total_fee = 300 * totalHour * 100;
				} else if (totalHour >= 4 && totalHour < 8) {
					total_fee = 250 * totalHour * 100;
				} else if (totalHour >= 8) {
					total_fee = 200 * totalHour * 100;
				}
			} else {
				if (parameter.getCoefficient() != 0) {
					total_fee = (int) (parameter.getPrice() * parameter.getCoefficient());
				} else {
					total_fee = (int) (parameter.getPrice());
				}
			}
			return total_fee;
		} else {
			return TransformUtils.toInt(amount) * 100;
		}
	}
}
