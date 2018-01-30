package com.yinghai.macao.common.service.impl;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import com.yinghai.macao.common.constant.SpcarDriverPushCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.constant.PayWay;
import com.yinghai.macao.common.constant.SpcarPassengerPushCode;
import com.yinghai.macao.common.dao.MeterMapper;
import com.yinghai.macao.common.dao.SpcarDriverMapper;
import com.yinghai.macao.common.dao.SpcarMapper;
import com.yinghai.macao.common.dao.SpcarOrderMapper;
import com.yinghai.macao.common.dao.SpcarPassengerMapper;
import com.yinghai.macao.common.model.Meter;
import com.yinghai.macao.common.model.Spcar;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.MeterService;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.service.SpcarPassengerService;
import com.yinghai.macao.common.util.HttpRequester;
import com.yinghai.macao.common.util.IMMsgRequestUtil;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;
import com.yinghai.macao.common.util.PaypalUtil;
import com.yinghai.macao.common.util.RequestJson;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TecentImUtils;
import com.yinghai.macao.common.util.TlsSignUtil;
import com.yinghai.macao.common.util.TransformUtils;
import com.yinghai.macao.common.util.WeChatPayUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class SpcarOrderServiceImpl implements SpcarOrderService {
	@Resource
	private SpcarOrderMapper spcarOrderMapper;
	@Autowired
	private SpcarPassengerMapper spcarPassengerMapper;
	@Autowired
	private SpcarDriverMapper spcarDriverMapper;
	@Autowired
	private SpcarMapper spcarMapper;
	@Resource
	private MeterService meterService;
	@Resource
	private MeterMapper meterMapper;

	 @Autowired
	 SpcarPassengerService spcarPassengerService;

    private Logger log = Logger.getLogger(this.getClass());
	public void insertSelective(SpcarOrder order) {
		// TODO Auto-generated method stub
		spcarOrderMapper.insertSelective(order);
	}

	public SpcarOrder getSpcarOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		SpcarOrder order = new SpcarOrder();
		order.setOrderNo(orderNo);
		return spcarOrderMapper.selectByCondition(order);

	}

	public SpcarOrder getAppSpcarOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		SpcarOrder order = new SpcarOrder();
		order.setOrderNo(orderNo);
		return spcarOrderMapper.selectByCondition(order);

	}
	public Integer updateByPrimaryKeySelective(SpcarOrder spcarorder,Boolean isPush) throws Exception {
		// TODO Auto-generated method stub
		int i = spcarOrderMapper.updateByPrimaryKeySelective(spcarorder);
		if(i<1){//更新條數為0時直接返回
			log.error("SpcarOrderServiceImpl/updateByPrimaryKeySelective==========更新失敗，更新的條數為0");
			return 0;
		}else{
			spcarorder = spcarOrderMapper.selectByPrimaryKeyToApp(spcarorder.getSpcarOrderId());
			//判斷是否需要推送並且司機不為空的情況下才去推送
			if(isPush&&spcarorder.getSpcarDriver()!=null){
				String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
			
				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
				JSONObject jsonOrder = JSONObject.fromObject(spcarorder, config);
				JSONObject responseObject = new JSONObject();
					 //通知司機
					 try {
						  responseObject = new JSONObject();
							responseObject.put("msg", "乘客的訂單已改變");
							responseObject.put("code", SpcarDriverPushCode.ORDEREDIT);//702
							responseObject.put("data", jsonOrder);
							String  resultMsg = responseObject.toString();
						 if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, spcarorder.getSpcarDriver().getImName(), RequestJson.getTextJsonMsg(resultMsg),true,SpcarDriverPushCode.ORDEREDITOfflineMsg)) {
								i++;
							} else {
								log.error("SpcarOrderServiceImpl/pushMsg======单聊通知司機失败:imName=" + spcarorder.getSpcarPassenger().getImName() + "======");
								return -10;
							}
					} catch (Exception e) {
						// TODO: handle exception
						log.error("SpcarOrderServiceImpl/pushMsg======通知司機异常:" + e.getMessage() + "======");
						e.printStackTrace();
						return -1;
					}
					 //乘客的推送
					 try {
						  responseObject = new JSONObject();
							responseObject.put("msg", "您的訂單已改變");
							responseObject.put("code", SpcarPassengerPushCode.ORDEREDIT);//702
							responseObject.put("data", jsonOrder);
							String  resultMsg = responseObject.toString();
						 if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, spcarorder.getSpcarPassenger().getImName(), RequestJson.getTextJsonMsg(resultMsg),true,SpcarPassengerPushCode.ORDEREDITOfflineMsg)) {
								i++;
							} else {
								log.error("SpcarOrderServiceImpl/pushMsg======单聊通知乘客失败:imName=" + spcarorder.getSpcarPassenger().getImName() + "======");
								return -10;
							}
					} catch (Exception e) {
						// TODO: handle exception
						log.error("SpcarOrderServiceImpl/pushMsg======通知乘客异常:" + e.getMessage() + "======");
						e.printStackTrace();
						return -1;
					}
			}
		}
		return i;
	}

	public SpcarOrder getSpcarOrderbyId(Integer id) {
		// TODO Auto-generated method stub
		return spcarOrderMapper.selectByPrimaryKey(id);
	}


	public SpcarOrder getAppSpcarOrderbyId(Integer id) {
		// TODO Auto-generated method stub
		return spcarOrderMapper.selectByPrimaryKeyToApp(id);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Integer updateOrderAndCreateMeter(SpcarOrder spCarOrder, Meter meter,boolean pushOrNo) throws Exception {
		// TODO Auto-generated method stub
		int i = 0;
		try {
			spcarOrderMapper.updateByPrimaryKeySelective(spCarOrder);
			i++;
		} catch (Exception e) {
			// TODO: handle exception
			i=0;
			e.printStackTrace();
			log.debug("==================更新专车订单表失败================");
			throw new RuntimeException();
		}
		try {
			meterService.insertSelective(meter);
			i++;
		} catch (Exception e) {
			// TODO: handle exception
			i=1;
			e.printStackTrace();
			log.debug("==================新增流水记录表================");
			throw new RuntimeException();
		}
		try {
			spcarPassengerService.updateSpcarPaaengerStatus(spCarOrder.getPassengerId(), SpcarPassenger.PASSENGER_CLOSE_STATUS);
			i++;
		} catch (Exception e) {
			// TODO: handle exception
			i=2;
			e.printStackTrace();
			log.debug("==================修改订单状态失败================");
			throw new RuntimeException();
		}

		if(pushOrNo){
	 		JSONObject responseObject = new JSONObject();
			JsonConfig config = new JsonConfig();
	 		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			// 推送
 			String sign = "";
 			sign = TlsSignUtil.getTlsSignKey(Constant.manager);
 			JSONObject jsonOrder = JSONObject.fromObject(spCarOrder, config);
 			JSONObject data = new JSONObject();
 			data.put("spCarOrder", jsonOrder);

 			responseObject.put("msg", "this order was refund ");
 			responseObject.put("code", SpcarPassengerPushCode.moneyReback);
 			responseObject.put("data", data);
 			com.alibaba.fastjson.JSONObject json = RequestJson.getTextJsonMsg(responseObject.toString());
 			Boolean singleChar = IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, spCarOrder.getPassengerId()+"Passenger", json,false,SpcarPassengerPushCode.moneyRebackOfflineMsg);
 			if (!singleChar) {
 				log.debug("=============================退款信息推送失败"+new Date());
 				throw new RuntimeException();
 			}
		}
		return i;
	}
	@Transactional(propagation= Propagation.REQUIRED)
	public SpcarOrder submitOrder(SpcarOrder spCarOrder) {
		int i = spcarOrderMapper.insertSelective(spCarOrder);
//		SpcarPassenger spcarPassenger = new SpcarPassenger();
//		spcarPassenger.setSpcarId(spCarOrder.getPassengerId());
//		spcarPassenger.setStatus(SpcarPassenger.PASSENGER_CLOSE_STATUS);
//		i = i+spcarPassengerMapper.updateByPrimaryKeySelective(spcarPassenger);
		spCarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(spCarOrder.getSpcarOrderId());
		return spCarOrder;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Integer updateOrderAndPush(SpcarOrder spcarOrder) throws Exception {
		// TODO Auto-generated method stub
		JSONObject responseObject = new JSONObject();
		int i=0;
		JsonConfig config = new JsonConfig();
		if(spcarOrder!=null){
			SpcarOrder updateSpCarOrder = new SpcarOrder();
			updateSpCarOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
			//updateSpCarOrder.setCarId(spcarOrder.getCarId());
			updateSpCarOrder.setDriverId(spcarOrder.getDriverId());
			updateSpCarOrder.setStatus(SpcarOrder.ORDER_PAIRED_STATUS);
			 i = spcarOrderMapper.updateByPrimaryKeySelective(updateSpCarOrder);
			if(i<1){
				return 0;
			}

			// 推送
						String sign = "";
						sign = TlsSignUtil.getTlsSignKey(Constant.manager);
						JSONObject jsonOrder = JSONObject.fromObject(spcarOrder, config);
						JSONObject data = new JSONObject();
						data.put("spCarOrder", jsonOrder);

						responseObject.put("msg", "this order was matching ");
						responseObject.put("code", SpcarPassengerPushCode.matchingSuccess);
						responseObject.put("data", data);
						com.alibaba.fastjson.JSONObject json = RequestJson.getTextJsonMsg(responseObject.toString());
						Boolean singleChar = IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, spcarOrder.getPassengerId()+"Passenger", json,true,SpcarPassengerPushCode.matchingSuccessOfflineMsg);
						if (!singleChar) {
							log.debug("=============================派单信息推送失败"+new Date());
							throw new RuntimeException();
						}

		}

		return i;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Integer updateOrderandSpcarPassenger(SpcarOrder spcarOrder, SpcarPassenger spcarPassenger) {
		// TODO Auto-generated method stub
		int i = 0;
		if(spcarOrder!=null){
			try {
				i = spcarOrderMapper.updateByPrimaryKeySelective(spcarOrder);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("=============================更新专车订单失败"+new Date());
 				throw new RuntimeException();
			}

		}

		if(spcarPassenger!=null){
			try {
				spcarPassengerService.updateSpcarPaaengerStatus(spcarPassenger.getSpcarId(), spcarPassenger.getStatus());
				i++;
			} catch (Exception e) {
				// TODO: handle exception
				log.error("=============================更新专车乘客失败"+new Date());
 				throw new RuntimeException();
			}

		}
		return i;
	}

	@Override
	public Page<SpcarOrder> findList(Integer pageNumber, Integer pageStartSize, SpcarOrder spcarOrder) {
		PageHelper.startPage(pageNumber,pageStartSize);
		return spcarOrderMapper.findListPage(spcarOrder);
	}

	@Override
	public SpcarOrder findOneBykey(Integer id) {
		return spcarOrderMapper.selectByPrimaryKeyToApp(id);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int changeSpcar(SpcarOrder order,SpcarOrder o) {
		//判斷當前狀態是否一致/訂單狀態 配對中 2
		if(order.getStatus().intValue()!=SpcarOrder.ORDER_PAIRED_STATUS&&order.getPayStatus().intValue()!=SpcarOrder.PAY_ALEADY_STATUS){
			log.error("SpcarOrderServiceImpl/changeSpcar======訂單:"+order.getSpcarOrderId()+"非法操作,當前狀態為:"+order.getStatus().intValue()+"當前操作匹配訂單狀態："+SpcarOrder.ORDER_PAIRED_STATUS);
			return -3;
		}
		if(o.getSpcarOrderId()==null||o.getSpcarOrderId()==0){
			log.error("SpcarOrderServiceImpl/changeSpcar======该订单spcarOrderId为空======");
			return 0;
		}
		if(o.getDriverId()==null||o.getDriverId()==0){
			log.error("SpcarOrderServiceImpl/changeSpcar======该订单spcarDriverId为空======");
			return 0;
		}
		if(o.getSpcarId()==null||o.getSpcarId()==0){
			log.error("SpcarOrderServiceImpl/changeSpcar======该订单spcarId为空======");
			return 0;
		}
		//更新订单的司机和专车信息
		o.setUpdateTime(new Date());
		int i=0;
		try {
			i = spcarOrderMapper.updateByPrimaryKeySelective(o);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("=============================更新专车订单失败"+new Date());
			throw new RuntimeException("更新专车订单失败",e);
		}
		//订单更新完之后查询出最新的数据，然后推送
		o = spcarOrderMapper.selectByPrimaryKeyToApp(o.getSpcarOrderId());
		if(i>0){
			//通知乘客端
			SpcarPassenger spcarPassenger = spcarPassengerMapper.selectByPrimaryKey(o.getPassengerId());
			if(spcarPassenger==null){
				log.error("SpcarOrderServiceImpl/changeSpcar======查找spcarPassenger为空，spcarPassenger:"+o.getPassengerId()+"======");
				return 0;
			}
			int j = 0;
			j = pushMsg(o,SpcarPassengerPushCode.changeSuccess,"訂單改派司机专车成功！");
			if (j>0){
				i = i+j;
			}else {
				log.error("SpcarOrderServiceImpl/changeSpcar======通知乘客失敗！======");
			}
			if(!sendToWebchatClient(o,SpcarPassengerPushCode.changeSuccess)){
				log.error("SpcarOrderServiceImpl/changeSpcar======通知微信用户失败！======");
			}
			//通知司机
			String sign = null;
			try {
				sign = TlsSignUtil.getTlsSignKey(Constant.manager);
			} catch (Exception e) {
				log.error("获取签名失败！");
				e.printStackTrace();
			}
			try {
				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
				JSONObject jsonOrder = JSONObject.fromObject(o, config);
				JSONObject responseObject = new JSONObject();
				responseObject.put("msg", "訂單改派成功！");
				responseObject.put("code", SpcarDriverPushCode.matchingSuccess);
				responseObject.put("data", jsonOrder);
				if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, o.getSpcarDriver().getImName(), RequestJson.getTextJsonMsg(responseObject.toString()),true,SpcarDriverPushCode.matchingSuccessOfflineMsg)) {
                    i++;
                    responseObject.put("code", SpcarDriverPushCode.ORDERCHANGE);
                    responseObject.put("msg", "订单已经改派给其他司机!");
                    if(IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, order.getSpcarDriver().getImName(), RequestJson.getTextJsonMsg(responseObject.toString()),true,SpcarDriverPushCode.ORDERCHANGEOfflineMsg)){
                    }else{
                    	log.error("SpcarOrderServiceImpl/pushMsg======单聊通知失败:imName=" + order.getSpcarDriver().getImName() + "======");
                        return -10;
                    }
                } else {
                    log.error("SpcarOrderServiceImpl/pushMsg======单聊通知失败:imName=" + o.getSpcarDriver().getImName() + "======");
                    return -10;
                }
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			log.error("SpcarOrderServiceImpl/changeSpcar======更新失败！");
			return 0;
		}
		return i;
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int chooseSpcar(String spcarOrderId, String spcarDriverId,String carId) {
		SpcarOrder spcarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(Integer.parseInt(spcarOrderId));
		if(spcarOrder==null){
			log.error("SpcarOrderServiceImpl/chooseSpcar======查找spcarOrder为空，spcarOrderId:"+spcarOrderId+"======");
			return 0;
		}
		//判斷當前狀態是否一致/訂單狀態 配對中 2
		if(spcarOrder.getStatus().intValue()!=SpcarOrder.ORDER_PAIRING_STATUS&&spcarOrder.getPayStatus().intValue()!=SpcarOrder.PAY_ALEADY_STATUS){
			log.error("SpcarOrderServiceImpl/chooseSpcar======訂單:"+spcarOrderId+"非法操作,當前狀態為:"+spcarOrder.getStatus().intValue()+"當前操作匹配訂單狀態："+SpcarOrder.ORDER_PAIRING_STATUS);
			return -3;
		}
		
		if(spcarOrder.getDriverId()!=null&&spcarOrder.getDriverId()!=0){
			log.error("SpcarOrderServiceImpl/chooseSpcar======该订单spcarDriverId不为空，spcarDriverId:"+spcarOrder.getDriverId()+"======");
			return 0;
		}
		if(spcarOrder.getSpcarId()!=null&&spcarOrder.getSpcarId()!=0){
			log.error("SpcarOrderServiceImpl/chooseSpcar======该订单spcarId不为空，spcarId:"+spcarOrder.getSpcarId()+"======");
			return 0;
		}
		SpcarDriver spcarDriver = spcarDriverMapper.selectByPrimaryKey(Integer.parseInt(spcarDriverId));
		if(spcarDriver==null){
			log.error("SpcarOrderServiceImpl/chooseSpcar======查找spcarDriver为空，spcarDriverId:"+spcarDriverId+"======");
			return 0;
		}
		//更新订单的司机和专车信息
		SpcarOrder order = new SpcarOrder();
		order.setSpcarOrderId(Integer.parseInt(spcarOrderId));
		order.setDriverId(Integer.parseInt(spcarDriverId));
		order.setSpcarId(Integer.parseInt(carId));
		order.setStatus(SpcarOrder.ORDER_PAIRED_STATUS);
		order.setUpdateTime(new Date());
		int i = 0;
		try {
			i = spcarOrderMapper.updateByPrimaryKeySelective(order);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.debug("=============================更新专车乘客失败"+new Date());
			throw new RuntimeException("更新专车乘客失败",e);
		}
		//订单更新完之后查询出最新的数据，然后推送
		spcarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(Integer.parseInt(spcarOrderId));
		//更新乘客状态
//		SpcarPassenger updatePassenger = new SpcarPassenger();
//		updatePassenger.setSpcarId(order.getPassengerId());
//		updatePassenger.setStatus(SpcarPassenger.PASSENGER_READY_STATUS);
//		i=i+spcarPassengerMapper.updateByPrimaryKeySelective(updatePassenger);
		if(i>0){
			//通知乘客端
			SpcarPassenger spcarPassenger = spcarPassengerMapper.selectByPrimaryKey(spcarOrder.getPassengerId());
			if(spcarPassenger==null){
				log.error("SpcarOrderServiceImpl/chooseSpcar======查找spcarPassenger为空，spcarPassenger:"+spcarOrder.getPassengerId()+"======");
				return 0;
			}
			int j = 0;
			j = pushMsg(spcarOrder,SpcarPassengerPushCode.matchingSuccess,"訂單確定匹配！");
			if (j>0){
				i = i+j;
			}else {
				log.error("SpcarOrderServiceImpl/chooseSpcar======通知乘客失敗！======");
			}
			if(!sendToWebchatClient(spcarOrder,SpcarPassengerPushCode.matchingSuccess)){
				log.error("SpcarOrderServiceImpl/chooseSpcar======通知微信用户失败！======");
			}
			//通知司机
			String sign = null;
			try {
				sign = TlsSignUtil.getTlsSignKey(Constant.manager);
			} catch (Exception e) {
				log.error("获取签名失败！");
				e.printStackTrace();
			}
			try {
				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
				JSONObject jsonOrder = JSONObject.fromObject(spcarOrder, config);
				JSONObject responseObject = new JSONObject();
				responseObject.put("msg", "訂單確定匹配！");
				responseObject.put("code", SpcarDriverPushCode.matchingSuccess);
				responseObject.put("data", jsonOrder);
				if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, spcarDriver.getImName(), RequestJson.getTextJsonMsg(responseObject.toString()),true,SpcarDriverPushCode.matchingSuccessOfflineMsg)) {
                    i++;
                } else {
                    log.error("SpcarOrderServiceImpl/pushMsg======单聊通知失败:imName=" + spcarDriver.getImName() + "======");
                    return -10;
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			log.error("SpcarOrderServiceImpl/chooseSpcar======更新失败！");
			return 0;
		}
		return i;
	}

	@Override
	public int driverSetOut(String spcarOrderid) {
		SpcarOrder spcarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(Integer.parseInt(spcarOrderid));
		Spcar spcar = spcarOrder.getSpcar();//专车
		SpcarDriver spcarDriver = spcarOrder.getSpcarDriver();//司机
		SpcarPassenger spcarPassenger = spcarOrder.getSpcarPassenger();//乘客
		if(spcar.getSpcarId()==null||spcar.getSpcarId()==0||spcarDriver.getSpcarDriverId()==null||spcarDriver.getSpcarDriverId()==0){
			log.error("SpcarOrderServiceImpl/driverSetOut======數據異常，專車/司機/乘客信息為空！");
			return 0;
		}
		//判斷當前狀態是否一致/訂單狀態 已确认6
		if(spcarOrder.getStatus().intValue()!=SpcarOrder.ORDER_TBC_STATUS||!SpcarDriver.DRIVER_ONLINE_STATUS.equals(spcarDriver.getStatus())){
			log.error("SpcarOrderServiceImpl/driverSetOut======訂單:"+spcarOrderid+"非法操作,當前狀態為:"+spcarOrder.getStatus().intValue()+"當前操作匹配訂單狀態："+SpcarOrder.ORDER_PAIRED_STATUS);
			return -3;
		}
		//更新司機信息
		SpcarDriver updateDriver = new SpcarDriver();
		updateDriver.setSpcarDriverId(spcarDriver.getSpcarDriverId());
		updateDriver.setStatus(SpcarDriver.DRIVER_READY_STATUS);//3 前往載客/出發
		updateDriver.setDriverSetoutTime(new Date());
//		i = spcarDriverMapper.updateByPrimaryKeySelective(updateDriver);
		//更新乘客信息
		SpcarPassenger updatePassenger = new SpcarPassenger();
		updatePassenger.setSpcarId(spcarOrder.getPassengerId());
		updatePassenger.setStatus(SpcarPassenger.PASSENGER_READY_STATUS);//2 等待中
//		i = i+spcarPassengerMapper.updateByPrimaryKeySelective(spcarPassenger);
		//更新訂單信息
		SpcarOrder updateOrder = new SpcarOrder();
		updateOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
		updateOrder.setStatus(SpcarOrder.ORDER_CARRY_STATUS);//4  接載中
		updateOrder.setUpdateTime(new Date());
		updateOrder.setDriverSetoutTime(new Date());
//		i = i+spcarOrderMapper.updateByPrimaryKeySelective(spcarOrder);
		//更新專車信息
		Spcar updateSpcar = new Spcar();
		updateSpcar.setSpcarId(spcar.getSpcarId());
		updateSpcar.setSpcarUsed(true);
//		i = i+spcarMapper.updateByPrimaryKeySelective(updateSpcar);
		return update(updateSpcar,updateDriver,updatePassenger,updateOrder,true,spcarOrder,spcarPassenger.getImName(),SpcarPassengerPushCode.driverSetOut,"司機已出發，前往目的地接載！",4);
	}

	@Override
	public int comfirmArrive(String spcarOrderid) {
		SpcarOrder spcarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(Integer.parseInt(spcarOrderid));
		if(spcarOrder.getStatus().intValue()!=SpcarOrder.ORDER_CARRY_STATUS){
			log.error("SpcarOrderServiceImpl/comfirmArrive======訂單:"+spcarOrderid+"非法操作,當前狀態為:"+spcarOrder.getStatus().intValue()+"當前操作匹配訂單狀態："+SpcarOrder.ORDER_CARRY_STATUS);
			return -3;
		}
		Spcar spcar = spcarOrder.getSpcar();//专车
		SpcarDriver spcarDriver = spcarOrder.getSpcarDriver();//司机
		SpcarPassenger spcarPassenger = spcarOrder.getSpcarPassenger();//乘客
		if(spcar.getSpcarId()==null||spcar.getSpcarId()==0||spcarDriver.getSpcarDriverId()==null||spcarDriver.getSpcarDriverId()==0){
			log.error("SpcarOrderServiceImpl/comfirmArrive======數據異常，專車/司機/乘客信息為空！");
			return 0;
		}
//		//更新訂單信息
		SpcarOrder updateOrder = new SpcarOrder();
		if(!SpcarDriver.DRIVER_ARRIVE.equals(spcarDriver.getStatus())){
			updateOrder.setDriverArriveTime(new Date());
		}
		updateOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
		updateOrder.setUpdateTime(new Date());
//		updateOrder.setStatus(SpcarOrder.ORDER_WORK_STATUS);//達到目的地并接載到乘客 0 進行中
//		//更新司機信息
		SpcarDriver updateDriver = new SpcarDriver();
		updateDriver.setSpcarDriverId(spcarOrder.getDriverId());
		updateDriver.setStatus(SpcarDriver.DRIVER_ARRIVE);//到达目的地
		updateDriver.setDriverArriveTime(new Date());
//		//更新乘客信息
//		SpcarPassenger updatePassenger = new SpcarPassenger();
//		updatePassenger.setSpcarId(spcarOrder.getPassengerId());
//		updatePassenger.setStatus(SpcarPassenger.PASSENGER_GET_ON_STATUS);//乘客已上車 0
//		int i = update(null,updateDriver,updatePassenger,updateOrder);
		//通知乘客
//		if(!sendToWebchatClient(spcarOrder,SpcarPassengerPushCode.driverArrive)){
//			log.error("SpcarOrderServiceImpl/chooseSpcar======通知微信用户失败！======");
//		}
//		return pushMsg(spcarOrder,spcarPassenger.getImName(),SpcarPassengerPushCode.driverArrive,"司機已到達目的地！");
		return update(null,updateDriver,null,updateOrder,true,spcarOrder,spcarOrder.getSpcarPassenger().getImName(),SpcarPassengerPushCode.driverArrive,"司機已到達目的地！",2);

	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int comfirmCarry(String spcarOrderid) {
		SpcarOrder spcarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(Integer.parseInt(spcarOrderid));
		if(spcarOrder.getStatus().intValue()!=SpcarOrder.ORDER_CARRY_STATUS){
			log.error("SpcarOrderServiceImpl/comfirmCarry======訂單:"+spcarOrderid+"非法操作,當前狀態為:"+spcarOrder.getStatus().intValue()+"當前操作匹配訂單狀態："+SpcarOrder.ORDER_CARRY_STATUS);
			return -3;
		}
		Spcar spcar = spcarOrder.getSpcar();//专车
		SpcarDriver spcarDriver = spcarOrder.getSpcarDriver();//司机
		SpcarPassenger spcarPassenger = spcarOrder.getSpcarPassenger();//乘客
		if(spcar.getSpcarId()==null||spcar.getSpcarId()==0||spcarDriver.getSpcarDriverId()==null||spcarDriver.getSpcarDriverId()==0){
			log.error("SpcarOrderServiceImpl/comfirmArrive======數據異常，專車/司機/乘客信息為空！");
			return 0;
		}
		//更新訂單信息
		SpcarOrder updateOrder = new SpcarOrder();
		updateOrder.setUpdateTime(new Date());
		updateOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
		updateOrder.setPassengerArriveTime(new Date());
		updateOrder.setStatus(SpcarOrder.ORDER_WORK_STATUS);//達到目的地并接載到乘客 0 進行中
		//更新司機信息
		SpcarDriver updateDriver = new SpcarDriver();
		updateDriver.setSpcarDriverId(spcarOrder.getDriverId());
		updateDriver.setStatus(SpcarDriver.DRIVER_WORK_STATUS);//載客中 0
		updateDriver.setPassengerGetinTime(new Date());
		//更新乘客信息
		SpcarPassenger updatePassenger = new SpcarPassenger();
		updatePassenger.setSpcarId(spcarOrder.getPassengerId());
		updatePassenger.setStatus(SpcarPassenger.PASSENGER_GET_ON_STATUS);//乘客已上車 0
		return update(null,updateDriver,updatePassenger,updateOrder,true,spcarOrder,spcarPassenger.getImName(),SpcarPassengerPushCode.carry,"司機接到乘客！",3);
	}

	@Override
	public int orderFinished(String spcarOrderid,String locX,String locY,String address) {
		SpcarOrder spcarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(Integer.parseInt(spcarOrderid));
		if(spcarOrder.getStatus().intValue()!=SpcarOrder.ORDER_WORK_STATUS){//0 进行中
			log.error("SpcarOrderServiceImpl/orderFinished======訂單:"+spcarOrderid+"非法操作,當前狀態為:"+spcarOrder.getStatus().intValue()+"當前操作匹配訂單狀態："+SpcarOrder.ORDER_WORK_STATUS);
			return -3;
		}
		Spcar spcar = spcarOrder.getSpcar();//专车
		SpcarDriver spcarDriver = spcarOrder.getSpcarDriver();//司机
		SpcarPassenger spcarPassenger = spcarOrder.getSpcarPassenger();//乘客
		if(spcar.getSpcarId()==null||spcar.getSpcarId()==0||spcarDriver.getSpcarDriverId()==null||spcarDriver.getSpcarDriverId()==0){
			log.error("SpcarOrderServiceImpl/comfirmArrive======數據異常，專車/司機/乘客信息為空！");
			return 0;
		}
		//更新訂單信息
		SpcarOrder updateOrder = new SpcarOrder();
		updateOrder.setUpdateTime(new Date());
		updateOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
		updateOrder.setStatus(SpcarOrder.ORDER_FINISH_STATUS);// 5 完成
		if(StringUtil.notEmpty(locX)){
			updateOrder.setEndX(TransformUtils.toDouble(locX));
		}
		if(StringUtil.notEmpty(locY)){
			updateOrder.setEndY(TransformUtils.toDouble(locY));
		}
		if(StringUtil.notEmpty(address)){
			updateOrder.setEndAddress(address);
		}
		updateOrder.setCompleteTime(new Date());
		//更新司機信息
		SpcarDriver updateDriver = new SpcarDriver();
		updateDriver.setSpcarDriverId(spcarOrder.getDriverId());
		updateDriver.setStatus(SpcarDriver.DRIVER_ONLINE_STATUS);//在线 1
		updateDriver.setFinishCount(spcarDriver.getFinishCount()+1);//完成数
		if(spcarDriver.getFinishAmount()==0){//针对之前未统计的接单数
			SpcarOrder order = new SpcarOrder();
			order.setDriverId(spcarOrder.getDriverId());
			order.setStatus(SpcarOrder.ORDER_FINISH_STATUS);
			Page<SpcarOrder> list = spcarOrderMapper.findFinishList(spcarOrder);
			if(list!=null){
				int sum = 0;
				for(SpcarOrder o:list){
					sum += o.getAmount();
				}
				spcarDriver.setFinishAmount(sum);
			}
		}
		updateDriver.setFinishAmount(spcarDriver.getFinishAmount()+spcarOrder.getAmount());//接单总额
		updateDriver.setOrderCount(spcarDriver.getOrderCount());//总单数
		updateDriver.setDriverSetoutTime(null);
		updateDriver.setDriverArriveTime(null);
		updateDriver.setPassengerGetinTime(null);
		//更新乘客信息
		SpcarPassenger updatePassenger = new SpcarPassenger();
		updatePassenger.setSpcarId(spcarOrder.getPassengerId());
		updatePassenger.setStatus(SpcarPassenger.PASSENGER_CLOSE_STATUS);//待机 1
		updatePassenger.setFinishCount(spcarPassenger.getFinishCount()+1);//完成数
		if(spcarPassenger.getTotalConsume()==0){
			SpcarOrder order = new SpcarOrder();
			order.setPassengerId(spcarOrder.getPassengerId());
			order.setStatus(SpcarOrder.ORDER_FINISH_STATUS);
			Page<SpcarOrder> list = spcarOrderMapper.findFinishList(spcarOrder);
			if(list!=null){
				int sum = 0;
				for(SpcarOrder o:list){
					sum += o.getAmount();
				}
				spcarPassenger.setTotalConsume(sum);
			}
			
		}
		updatePassenger.setTotalConsume(spcarPassenger.getTotalConsume()+spcarOrder.getAmount());//消费总额
		updatePassenger.setOrderCount(spcarPassenger.getOrderCount()+1);//总单数
		//更新专车
		Spcar updateSpcar = new Spcar();
		updateSpcar.setSpcarId(spcar.getSpcarId());
		updateSpcar.setSpcarUsed(false);
		return update(updateSpcar,updateDriver,updatePassenger,updateOrder,true,spcarOrder,spcarPassenger.getImName(),SpcarPassengerPushCode.finished,"司機完成訂單！",4);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String reback(String spcarOrderid, BigDecimal bigDecimal,String cancelMemo) {
		JSONObject result = new JSONObject();
		if (bigDecimal.compareTo(new BigDecimal(1)) > 1) {
			log.error("SpcarOrderServiceImpl/reback======退款比率大于1");
			result.put("code", "-1");
			result.put("msg", "退款比率大于1");
			return result.toString();
		}
		SpcarOrder spcarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(Integer.parseInt(spcarOrderid));

		// 续单or非续单 非續單時取消訂單還需要將其關聯的續單也全部取消
		if (StringUtil.notEmpty(spcarOrder.getOrderId() + "")) {
			renewReback(spcarOrder, bigDecimal,cancelMemo);
		} else {// 非續單的處理
			initialReback(spcarOrder, bigDecimal,cancelMemo);
		}

//		try {
//						//通知乘客
//						JSONObject responseObject = new JSONObject();
//						JsonConfig config = new JsonConfig();
//						config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
//						JSONObject jsonOrder = JSONObject.fromObject(spcarOrder, config);
//						responseObject.put("msg", "退款成功");
//						responseObject.put("code", SpcarPassengerPushCode.moneyReback);//704
//						responseObject.put("data", jsonOrder);
//						String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
//						String imName = spcarOrder.getSpcarPassenger().getImName();
						//单聊形式
//			if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, imName,
//					RequestJson.getTextJsonMsg(responseObject.toString()),false,SpcarPassengerPushCode.moneyRebackOfflineMsg)) {
//						} else {
//							log.error("SpcarOrderServiceImpl/reback======单聊通知失败:imName=" + imName + "======");
//						}
//						if(!sendToWebchatClient(spcarOrder,SpcarPassengerPushCode.moneyReback)){
//							log.error("SpcarOrderServiceImpl/chooseSpcar======通知微信用户失败！======");
//						}
//						//推送给司机，如果有司機的情況下
//						if(spcarOrder.getSpcarDriver()!=null&&StringUtil.notEmpty( spcarOrder.getSpcarDriver().getImName())){
//							responseObject = new JSONObject();
//							responseObject.put("msg", "訂單已取消");
//							responseObject.put("code", SpcarDriverPushCode.ORDERCANCEL);//704
//							responseObject.put("data", jsonOrder);
//							if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, spcarOrder.getSpcarDriver().getImName(),
//									RequestJson.getTextJsonMsg(responseObject.toString()),false,SpcarDriverPushCode.ORDERCANCELOfflineMsg)) {
//										} else {
//											log.error("SpcarOrderServiceImpl/reback======单聊通知司機失败:imName=" + spcarOrder.getSpcarDriver().getImName() + "======");
//										}
//						}
//						//离线推送
//			String imResult = TecentImUtils.pushOne(sign, imName, responseObject.toString());
//			JSONObject resultJson = JSONObject.fromObject(imResult);
//			if ("OK".equalsIgnoreCase(resultJson.getString("ActionStatus"))) {
//			} else {
//				log.error("SpcarOrderServiceImpl/reback======离线推送失败:" + result + "======");
//			}
			result.put("code", "1");
			result.put("msg", "success");
			return result.toString();

//		} catch (Exception e) {
//			log.error("SpcarOrderServiceImpl/reback======退款异常" + e);
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int paypalCommit(String spcarOrderid,Meter meter) {
		SpcarOrder order = new SpcarOrder();
		SpcarOrder findOrder = spcarOrderMapper.selectByPrimaryKey(Integer.parseInt(spcarOrderid));
		order.setSpcarOrderId(Integer.parseInt(spcarOrderid));
		order.setStatus(SpcarOrder.ORDER_PAIRING_STATUS);
		order.setPayStatus(SpcarOrder.PAY_ALEADY_STATUS);
		order.setPayWayCode(PayWay.paypalCode);
		order.setPayWay(PayWay.paypal);
		int i = spcarOrderMapper.updateByPrimaryKeySelective(order);
		meter.setOrderNo(findOrder.getOrderNo());
		i += meterMapper.insertSelective(meter);
		 //当该订单为主单并且订单状态为匹配中时，进入计时器，30分钟后如果订单状态还是匹配中时，自动取消该订单以及其续单
		//30分钟自动取消功能暂时不需要，注释
//			if(findOrder.getOrderId()==null&&findOrder.getStatus().equals(SpcarOrder.ORDER_PAIRING_STATUS)){
//				timer(findOrder);
//			}
		return i;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int createMeter(String spcarOrderid,Payment payment) {
		SpcarOrder order = new SpcarOrder();
		SpcarOrder findOrder = spcarOrderMapper.selectByPrimaryKey(Integer.parseInt(spcarOrderid));
		order.setSpcarOrderId(Integer.parseInt(spcarOrderid));
		if(findOrder.getOrderId()!=null&&findOrder.getOrderId()!=0){
			order.setStatus(SpcarOrder.ORDER_RENEW_STATUS);
		}else{
			order.setStatus(SpcarOrder.ORDER_PAIRING_STATUS);
		}
		order.setPayStatus(SpcarOrder.PAY_ALEADY_STATUS);
		order.setPayWayCode(SpcarOrder.PAY_WAY_PAYPAL);
		order.setPayWay(payment.getId());
		int i = spcarOrderMapper.updateByPrimaryKeySelective(order);
		Meter meter = new Meter();
		meter.setOrderNo(findOrder.getOrderNo());
//		meter.setBankType();
		meter.setCreateTime(new Date());
		List<Transaction>  transactions = payment.getTransactions();
		String totalMoney = transactions.get(0).getAmount().getTotal();
		int money = new BigDecimal(totalMoney).multiply(new BigDecimal(100)).intValue();
		meter.setPayCode(SpcarOrder.PAY_WAY_PAYPAL+"");
		meter.setPayMoney(money);
		meter.setFeeType("HKD");
		meter.setPayNo(payment.getId());
		meter.setPayIntro(PayWay.paypal);
		i += meterMapper.insertSelective(meter);
		//paypal支付的定时退款，定时退款功能暂时不需要，注释
//		order = spcarOrderMapper.selectByPrimaryKey(Integer.parseInt(spcarOrderid));
//		if(order.getOrderId()==null&&order.getStatus().equals(SpcarOrder.ORDER_PAIRING_STATUS)){
//			timer(order);
//		}
		return i;
	}

	@Override
	public List<SpcarOrder> findByDriverId(Integer driverId,String status) {
		return spcarOrderMapper.selectByDriverId(driverId,status);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public int update(Spcar spcar,SpcarDriver spcarDriver,SpcarPassenger spcarPassenger,SpcarOrder spcarOrder,boolean notice,SpcarOrder sendOrder,String imName,String code,String msg,Integer updateCount){
		int i = 0;
		if(spcar!=null){
			i = i+spcarMapper.updateByPrimaryKeySelective(spcar);
		}
		if(spcarDriver!=null){
			i = i+spcarDriverMapper.updateByPrimaryKeySelective(spcarDriver);
		}
		if(spcarOrder!=null){
			i = i+spcarOrderMapper.updateByPrimaryKeySelective(spcarOrder);
		}
		if(spcarPassenger!=null){
			i = i+spcarPassengerMapper.updateByPrimaryKeySelective(spcarPassenger);
		}

		if(notice){
			if(i==updateCount){
				int j = pushMsg(sendOrder,code,msg);
				if (j>0){
					i = i+j;
				}else {
					log.error("SpcarOrderServiceImpl/update======通知乘客失敗！======");
//					throw new RuntimeException("通知乘客失敗！");
					return j;
				}

			}else{
				log.error("SpcarOrderServiceImpl/update======更新異常，更新條數不足======");
				throw new RuntimeException("======更新異常，更新條數不足======");
			}
			if(!sendToWebchatClient(sendOrder,code)){
				log.error("SpcarOrderServiceImpl/chooseSpcar======通知微信用户失败！======");
			}
		}
		return i;
	}

	/**
	 * 推送乘客信息
	 * @param spcarOrder
	 * @param code
	 * @param msg
	 * @return
	 */
	public int pushMsg(SpcarOrder spcarOrder,String code,String msg){
		int i = 0;
		try {
			String driverCode="";
			String driverMsg = "";
			SpcarOrder s = spcarOrderMapper.selectByPrimaryKeyToApp(spcarOrder.getSpcarOrderId());
			//給司機code賦值，由於訂單的取消與訂單的派送是已有司機推送的，所以不在此處理
			 if(code==SpcarPassengerPushCode.driverSetOut){
				driverCode = SpcarDriverPushCode.driverSetOut;
				driverMsg = SpcarDriverPushCode.driverSetOutOfflineMsg;
			}else if(code==SpcarPassengerPushCode.driverArrive){
				driverCode = SpcarDriverPushCode.driverArrive;
				driverMsg = SpcarDriverPushCode.driverArriveOfflineMsg;
			}else if(code==SpcarPassengerPushCode.carry){
				driverCode = SpcarDriverPushCode.carry;
				driverMsg = SpcarDriverPushCode.driverCarryPassengerMsg;
			}else if(code==SpcarPassengerPushCode.finished){
				driverCode = SpcarDriverPushCode.finished;
				driverMsg = SpcarDriverPushCode.orderFinishMsg;
			}
			//通知乘客
			//获取app数据结构
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject jsonOrder = JSONObject.fromObject(s, config);
			JSONObject responseObject = new JSONObject();
			responseObject.put("msg", msg);
			responseObject.put("code", code);//702
			responseObject.put("data", jsonOrder);
			String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
			String resultMsg = responseObject.toString();
			String offlinemsg = "";//連線消息提示
			boolean offline = false;//是否離線推送
			switch (code){
				case SpcarPassengerPushCode.matchingSuccess:offlinemsg = SpcarPassengerPushCode.matchingSuccessOfflineMsg;offline = true;break;
				case SpcarPassengerPushCode.driverSetOut:offlinemsg = SpcarPassengerPushCode.driverSetOutOfflineMsg;offline = true;break;
				case SpcarPassengerPushCode.driverArrive:offlinemsg = SpcarPassengerPushCode.driverArriveOfflineMsg;offline = true;break;
				case SpcarPassengerPushCode.moneyReback:offlinemsg = SpcarPassengerPushCode.moneyRebackOfflineMsg;offline = true;break;
				case SpcarPassengerPushCode.pushOrder:offlinemsg = SpcarPassengerPushCode.pushOrderOfflineMsg;offline = true;break;
				case SpcarPassengerPushCode.finished:offlinemsg = SpcarPassengerPushCode.finishedOfflineMsg;offline = true;break;
			}
			if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, s.getSpcarPassenger().getImName(), RequestJson.getTextJsonMsg(resultMsg),offline,offlinemsg)) {
				i++;
			} else {
				log.error("SpcarOrderServiceImpl/pushMsg======单聊通知乘客失败:imName=" + s.getSpcarPassenger().getImName() + "======");
				return -10;
			}
			//在driverCode和driverMsg有值的前提下才去推送給司機
			if(StringUtil.notEmpty(driverMsg)&&StringUtil.notEmpty(driverCode)){
				 //通知司機
				 try {
					  jsonOrder = JSONObject.fromObject(s, config);
					 responseObject = new JSONObject();
						responseObject.put("msg", driverMsg);
						responseObject.put("code", driverCode);//702
						responseObject.put("data", jsonOrder);
						 resultMsg = responseObject.toString();
					 if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager, s.getSpcarDriver().getImName(), RequestJson.getTextJsonMsg(resultMsg),offline,offlinemsg)) {
							i++;
						} else {
							log.error("SpcarOrderServiceImpl/pushMsg======单聊通知司機失败:imName=" + s.getSpcarPassenger().getImName() + "======");
							return -10;
						}
				} catch (Exception e) {
					// TODO: handle exception
					log.error("SpcarOrderServiceImpl/pushMsg======通知司機异常:" + e.getMessage() + "======");
					e.printStackTrace();
					return -1;
				}
			}
	
			//离线推送
//			String result = TecentImUtils.pushOne(sign, imName, responseObject.toString());
//			JSONObject resultJson = JSONObject.fromObject(result);
//			if ("OK".equalsIgnoreCase(resultJson.getString("ActionStatus"))) {
//				i++;
//			} else {
//				log.error("SpcarOrderServiceImpl/pushMsg======离线推送失败:" + result + "======");
//				return -11;
//			}
		} catch (Exception e) {
			log.error("SpcarOrderServiceImpl/pushMsg======通知乘客异常:" + e.getMessage() + "======");
			e.printStackTrace();
			return -1;
		}
		return i;
	}

	/**
	 * 提醒微信公众号用户
	 * @param order
	 * @param code
	 * @return
	 */
	private boolean sendToWebchatClient(SpcarOrder order,String code){
		String openId = order.getRejectId();
		if(StringUtil.empty(openId)){
			return true;
		}
		Map<String, String> p = new HashMap<String, String>();
		p.put("openId", openId);
		p.put("totalFee",order.getAmount()+"");
		p.put("sendType", code);
		p.put("orderId",order.getSpcarOrderId()+"");
		try {
			String url = Constant.webchatRemind;
			if(Constant.weixinJSAPPayType2.equals(order.getPayWay())){
				url = Constant.webchatRemind2;
			}
			String param = new HttpRequester().send(url,"POST",p).getContent().replaceAll("\r\n","");
			JSONObject object = JSONObject.fromObject(param);
			if(object.getInt("code")==1){
				return true;
			}else{
				log.error("SpcarOrderServiceImpl/chooseSpcar======通知乘客失敗！微信端======");
				return false;
			}
		} catch (IOException e) {
			log.error("SpcarOrderServiceImpl/chooseSpcar======通知乘客失敗！微信端======");
			e.printStackTrace();
			return false;
		}
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int addSpcarOrderAndPush(SpcarOrder spcarOrder, String imName, String sign) {
		// TODO Auto-generated method stub
		int i = spcarOrderMapper.insertSelective(spcarOrder);
		if (i > 0) {
			int j = pushMsg(spcarOrder, SpcarPassengerPushCode.pushOrder, "已帮您下单成功，等待您的支付");
			if (j < 0) {
				log.error("SpcarOrderServiceImpl/addSpcarOrderAndPush======通知乘客失敗！======");
//				throw new NullPointerException("通知乘客失敗");
			}
		} else {
			log.error("SpcarOrderServiceImpl/addSpcarOrderAndPush======新增订单失敗！======");
//			throw new NullPointerException("新增订单失败");
		}
		//如果该订单为原单并且订单状态为配对中时，使用计时器，定时取消、退款功能暂时不需要，注释
//		if(spcarOrder.getOrderId()==null&&spcarOrder.getStatus()==SpcarOrder.ORDER_PAIRING_STATUS){
//			timer(spcarOrder);
//		}
		return i;
	}
	public static void main(String[] args) {
		Map<String, String> p = new HashMap<String, String>();
		p.put("openId", "oX09iwJFx9oGuRVJE9IuU6HTgzyU");
		p.put("totalFee","123");
		p.put("sendType", "702");
		try {
			String code = new HttpRequester().send(Constant.webchatRemind,"POST",p).getContent();
			String param = code.replaceAll("\r\n","");
			JSONObject object = JSONObject.fromObject(param);
			if(object.getInt("code")==1){
                System.out.println("code");
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<SpcarOrder> findList(SpcarOrder spcarOrder) {
		// TODO Auto-generated method stub
		return spcarOrderMapper.findListPage(spcarOrder);
	}
	@Override
	public Page<SpcarOrder> findFinishList(Integer pageNumber, Integer pageStartSize, SpcarOrder spcarOrder) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNumber,pageStartSize);
		return spcarOrderMapper.findFinishList(spcarOrder);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Boolean orderrefund(SpcarOrder spcarOrder,String payType,Double ratio,String cancelMemo) throws Exception {
		// TODO Auto-generated method stub
		//退款金额
		int payMoney = 0;
		if(ratio==null){//默認係數為1
			ratio = 1.00;	
		}
		String payNo = "";//流水號
		String payCode = "";//支付類型碼
		String payCodeStr = "";//支付類型
		if(payType!=null&&payType.equals(Constant.PaypalPayType)){
			BigDecimal   b   =   new   BigDecimal(spcarOrder.getAmount()/100.00*ratio);  
			String jsonStr = PaypalUtil.reback(spcarOrder.getPayWay(), b.setScale(2,   BigDecimal.ROUND_HALF_UP)+"");
			com.alibaba.fastjson.JSONObject jsStr = com.alibaba.fastjson.JSONObject.parseObject(jsonStr);
			if(jsStr.get("code").equals("1")){
				com.alibaba.fastjson.JSONObject data = jsStr.getJSONObject("data");
				payCode = SpcarOrder.PAY_WAY_PAYPAL+"";
				payCodeStr = "PayPal";
				payNo = spcarOrder.getPayWay();
				payMoney = data.getBigDecimal("totalMoney").multiply(new BigDecimal(100)).intValue();
			}else{
				log.error("SpcarOrderServiceImpl/orderrefund======paypal退款失敗！======");
				throw new RuntimeException();
			}
		}else{//微信的退款
			Map map = WeChatPayUtils.orderrefund(spcarOrder.getOrderNo(), spcarOrder.getAmount()+"", (int)(spcarOrder.getAmount()*ratio)+"", spcarOrder.getPayWay());
			if("FAIL".equals(map.get("return_code"))){
				log.error("SpcarOrderServiceImpl/orderrefund======退款操作失败！======"+map.get("return_msg"));
				throw new RuntimeException("退款操作失败");
			}
			if("FAIL".equals(map.get("result_code"))){
				log.error("SpcarOrderServiceImpl/orderrefund======退款操作失败！======"+map.get("err_code_des"));
				throw new RuntimeException("退款操作失败");
			}
			if(Constant.weixinAPPPayType.equals(payType)){
				payCode = SpcarOrder.PAY_WAY_WEIXINAPP+"";
				payCodeStr = PayWay.WEIXINAPP;
			}else{
				payCodeStr = PayWay.WEIXINJSAPI;
				payCode = SpcarOrder.PAY_WAY_WEIXINJSAPI+"";
			}
			payNo = (String) map.get("refund_id");
			payMoney = Integer.valueOf((String) map.get("refund_fee"));
		}
		SpcarOrder updateSpcarOrder = new SpcarOrder();
		updateSpcarOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
		updateSpcarOrder.setPayStatus(SpcarOrder.PAY_REFUNDED_STATUS);
		updateSpcarOrder.setStatus(SpcarOrder.ORDER_CANCAL_STATUS);
		updateSpcarOrder.setRefundMoney(payMoney);
		updateSpcarOrder.setCancelMemo(cancelMemo);
		updateSpcarOrder.setCancelTime(new Date());
		updateSpcarOrder.setUpdateTime(new Date());
		int i = spcarOrderMapper.updateByPrimaryKeySelective(updateSpcarOrder);
		if(i<1){
			log.error("SpcarOrderServiceImpl/orderrefund======更新订单失败！======");
			throw new RuntimeException("更新訂單失敗");
		}
		//創建退款訂單流水號
		Meter meter = new Meter();
		meter.setCreateTime(new Date());
		meter.setOrderNo(spcarOrder.getOrderNo());
		meter.setPayMoney(-payMoney);
		meter.setPayNo(payNo);
		meter.setPayCode(payCode);
		meter.setPayIntro(payCodeStr);
		 i = meterMapper.insertSelective(meter);
			if(i<1){
				log.error("SpcarOrderServiceImpl/orderrefund======創建流水訂單失敗！======");
				throw new RuntimeException("創建流水訂單失敗");
			}
		return true;
	}
	@Override
	public void timer(SpcarOrder spCarOrder){
		//定时器，30分钟后执行，只执行一次
		 Timer timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	          public void run() {
	        	  //主订单
	        	  SpcarOrder findOrder = spcarOrderMapper.selectByPrimaryKey(spCarOrder.getSpcarOrderId());
	        	  if(findOrder.getStatus()==SpcarOrder.ORDER_PAIRING_STATUS){
	        		  initialReback(findOrder, null,"定時器取消");
	        		  }
	            timer.cancel();// 停止定时器
	          }
	        }, 1000*60*30, 1000*60*30*2);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int uodateMonthlyOrder(SpcarOrder spcarOrder) {
		int i = spcarOrderMapper.updateByPrimaryKeySelective(spcarOrder);
		if(i<1){
			log.error("SpcarOrderService/uodateMonthlyOrder error" + spcarOrder.getSpcarOrderId());
			throw  new RuntimeException();
		}
		 spcarOrder = spcarOrderMapper.selectByPrimaryKeyToApp(spcarOrder.getSpcarOrderId());
		 //当该订单为主单并且订单状态为匹配中时，进入计时器，30分钟后如果订单状态还是匹配中时，自动取消该订单以及其续单
		 //使用计时器，定时取消、退款功能暂时不需要，注释
//		if(spcarOrder.getOrderId()==null&&spcarOrder.getStatus().equals(SpcarOrder.ORDER_PAIRING_STATUS)){
//			timer(spcarOrder);
//		}
		// TODO Auto-generated method stub
		return i;
	}
	/**
	 * 續單的後台退款
	 * @param spcarOrder
	 * @param bigDecimal 係數
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int renewReback(SpcarOrder spcarOrder,BigDecimal bigDecimal,String cancelMemo) {
					Double ratio = null;
					if(bigDecimal!=null){
						ratio = bigDecimal.doubleValue();
					}
		int j=0;
					if (spcarOrder.getPayWayCode() == SpcarOrder.PAY_WAY_WEIXINAPP && !spcarOrder.getMonthly()) {// 支付方式为微信APP
						try {
							orderrefund(spcarOrder, Constant.weixinAPPPayType,ratio,cancelMemo);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.error("SpcarOrderServiceImpl/renewReback"+e);
							throw  new RuntimeException("SpcarOrderServiceImpl/renewReback"+e);
						}
					} else if (spcarOrder.getPayWayCode() == SpcarOrder.PAY_WAY_WEIXINJSAPI) {// 支付方式为公众号支付
						try {
							orderrefund(spcarOrder, Constant.weixinJSAPPayType,ratio,cancelMemo);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.error("SpcarOrderServiceImpl/renewReback"+e);
							throw  new RuntimeException("SpcarOrderServiceImpl/renewReback"+e);
						}
					} else if (spcarOrder.getPayWayCode() == SpcarOrder.PAY_WAY_PAYPAL) {// 如果是paypal支付時，調用paypal退款
						try {
							orderrefund(spcarOrder, "paypal",ratio, cancelMemo);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.error("SpcarOrderServiceImpl/renewReback"+e);
							throw  new RuntimeException("SpcarOrderServiceImpl/renewReback"+e);
						}
					} else {// 其他支付，现金、月结等直接退款
						SpcarOrder updateSpcarOrder = new SpcarOrder();
						updateSpcarOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
						updateSpcarOrder.setCancelTime(new Date());
						updateSpcarOrder.setStatus(SpcarOrder.ORDER_CANCAL_STATUS);
						if (!spcarOrder.getMonthly()) {// 如果是月結的話還是顯示未支付
							updateSpcarOrder.setPayStatus(SpcarOrder.PAY_REFUNDED_STATUS);
						}
							updateSpcarOrder.setRefundMoney((int)(spcarOrder.getAmount()*ratio));
							updateSpcarOrder.setCancelMemo(cancelMemo);
						 j = spcarOrderMapper.updateByPrimaryKeySelective(updateSpcarOrder);
						if (j < 1) {
							log.error("SpcarOrderServiceImpl/renewReback");
							throw new RuntimeException();
						}
					}
				//續單的取消與推送
			    pushMsg(spcarOrder,  SpcarPassengerPushCode.moneyReback, "推送信息給乘客");
		return j;
	}
	/**
	 * 原訂單的取消與退款
	 * @param bigDecimal
	 * @return payNo
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void initialReback(SpcarOrder findOrder,BigDecimal bigDecimal,String cancelMemo) {
		//
		Double ratio = null;
		if(bigDecimal!=null){
			ratio = bigDecimal.doubleValue();
		}
		  if(findOrder.getPayWayCode()==SpcarOrder.PAY_WAY_WEIXINAPP&&!findOrder.getMonthly()){//支付方式为微信APP
			  try {
				orderrefund(findOrder, Constant.weixinAPPPayType,ratio,cancelMemo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("SpcarOrderServiceImpl/initialReback"+e);
				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+e);
			}
		  }else if(findOrder.getPayWayCode()==SpcarOrder.PAY_WAY_WEIXINJSAPI){//支付方式为公众号支付
			  try {
				orderrefund(findOrder, Constant.weixinJSAPPayType,ratio,cancelMemo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("SpcarOrderServiceImpl/initialReback"+e);
				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+e);
			}
		  }else if(findOrder.getPayWayCode()==SpcarOrder.PAY_WAY_PAYPAL){//如果是paypal支付時，調用paypal退款
			 try {
				orderrefund(findOrder, "paypal",ratio,cancelMemo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("SpcarOrderServiceImpl/initialReback"+e);
				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+e);
			}
		  }else{//其他支付，现金、月结等直接退款
			  SpcarOrder updateSpcarOrder = new SpcarOrder();
      		 updateSpcarOrder.setSpcarOrderId(findOrder.getSpcarOrderId());
      		 updateSpcarOrder.setCancelTime(new Date());
			 updateSpcarOrder.setStatus(SpcarOrder.ORDER_CANCAL_STATUS);
			 if(!findOrder.getMonthly()){//判断是否为月结，如果不是月结，则直接设置为已退款
				 updateSpcarOrder.setPayStatus(SpcarOrder.PAY_REFUNDED_STATUS);
			 }
			 	updateSpcarOrder.setCancelMemo(cancelMemo);
				 updateSpcarOrder.setRefundMoney((int)(ratio*findOrder.getAmount()));
			 int j =  spcarOrderMapper.updateByPrimaryKeySelective(updateSpcarOrder);
			if(j<1){
				log.error("update spCarOrder fail" + updateSpcarOrder.getSpcarOrderId());
				log.error("SpcarOrderServiceImpl/initialReback");
				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback");
			}
		 }
		  //处理完主订单，处理续单
		  //增加过滤条件
		  Map map = new HashMap<>();
		  map.put("orderId", findOrder.getSpcarOrderId());
		  String[] str = new String[]{SpcarOrder.ORDER_RENEW_STATUS+"",SpcarOrder.ORDER_NOPAY_STATUS+""};
		  map.put("status", str);//筛选出续单订单状态为续单和未支付的订单
		  //查询主单关联的所有订单状态为未支付与续单的续单
		  List<SpcarOrder> list = spcarOrderMapper.getNewAppointmentOrder(map);
		  //在不为空的情况下循环取出续单，如果续单是未支付，直接取消，如果是续单状态，则改为退款中
        if(list!=null&&list.size()!=0){
      	 for(int i=0;i<list.size();i++){
      		 //取出续单
      		 SpcarOrder spcarOrder = list.get(i);
      		 SpcarOrder updateSpcarOrder = new SpcarOrder();
      		 updateSpcarOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
      		 updateSpcarOrder.setCancelTime(new Date());
      		 //如果是未支付的续单,直接取消
      		 if(spcarOrder.getStatus()==SpcarOrder.ORDER_NOPAY_STATUS){
      			 updateSpcarOrder.setStatus(SpcarOrder.ORDER_CANCAL_STATUS);
      			//如果是已支付的续单，如果是月结，直接取消，如果是微信，直接退款，如果是其他，给后台客服退款
      		 }else if(spcarOrder.getStatus()==SpcarOrder.ORDER_RENEW_STATUS){
      			 if(spcarOrder.getPayWayCode()==SpcarOrder.PAY_WAY_WEIXINAPP){//如果是微信APP支付的话
      				 try {
							orderrefund(spcarOrder, Constant.weixinAPPPayType,ratio,cancelMemo);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.error("SpcarOrderServiceImpl/initialReback"+e);
							throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+e);
						}
      			 }else if(spcarOrder.getPayWayCode()==SpcarOrder.PAY_WAY_WEIXINJSAPI){//微信公众号支付
      				 try {
								orderrefund(spcarOrder, Constant.weixinJSAPPayType,ratio,cancelMemo);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.error("SpcarOrderServiceImpl/initialReback"+e);
								throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+e);
							}
      			 }else if(findOrder.getPayWayCode()==SpcarOrder.PAY_WAY_PAYPAL){//如果是paypal支付時，調用paypal退款
	        			 try {
								orderrefund(spcarOrder, "paypal",ratio,cancelMemo);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.error("SpcarOrderServiceImpl/initialReback"+e);
								throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+e);
							}
		        		  }else{//其他支付，现金、月结等由后台去操作退款
      				 updateSpcarOrder.setStatus(SpcarOrder.ORDER_CANCAL_STATUS);
          			 updateSpcarOrder.setPayStatus(SpcarOrder.PAY_REFUNDED_STATUS);
          			updateSpcarOrder.setRefundMoney((int)(ratio*spcarOrder.getAmount()));//记录退款金额
      			 }
      		 }
      		 //如果需要更新的订单状态为空时，说明该订单在退款中已经做了处理，所以不再做重复操作
      		 if(updateSpcarOrder.getStatus()!=null){
      			 int j =  spcarOrderMapper.updateByPrimaryKeySelective(updateSpcarOrder);
       			if(j<1){
       				log.error("SpcarOrderServiceImpl/initialReback");
       				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback");
       			}
      			}
      	 }
        }
        //取消订单完成后，司机乘客的状态都改成在线(在匹配了司机之后)
        if(StringUtil.notEmpty(findOrder.getDriverId()+"")){
        	  SpcarDriver updateSpcarDriver = new SpcarDriver();
              updateSpcarDriver.setSpcarDriverId(findOrder.getDriverId());
              updateSpcarDriver.setStatus(SpcarDriver.DRIVER_ONLINE_STATUS);
              try {
              	 int i = spcarDriverMapper.updateByPrimaryKeySelective(updateSpcarDriver);
              	 if(i<1){
              		 log.error("SpcarOrderServiceImpl/initialReback"+"更新司机状态失败");
         				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+"更新司机状态失败");
                   }
      		} catch (Exception e) {
      			// TODO: handle exception
      			 log.error("SpcarOrderServiceImpl/initialReback"+"更新司机状态出错");
      				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+"更新司机状态出错");
      		}
		}
       //更新乘客状态
 
        try {
        	//当这改单是进行中的订单时才去修改乘客状态（避免出现乘客两张单，一张等待一张进行，取消等待单时出现的问题）
        	if(findOrder.getStatus()==SpcarOrder.ORDER_WORK_STATUS||findOrder.getStatus()==SpcarOrder.ORDER_CARRY_STATUS){
        	       SpcarPassenger updateSpcarPassenger = new SpcarPassenger();
        	        updateSpcarPassenger.setSpcarId(findOrder.getPassengerId());
        	        updateSpcarPassenger.setStatus(SpcarPassenger.PASSENGER_CLOSE_STATUS);
        	 	 int i = spcarPassengerMapper.updateByPrimaryKeySelective(updateSpcarPassenger);
               	 if(i<1){
               		 log.error("SpcarOrderServiceImpl/initialReback"+"更新乘客状态失败");
          				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+"更新乘客状态失败");
                    }
        	}
      
		} catch (Exception e) {
			// TODO: handle exception
			 log.error("SpcarOrderServiceImpl/initialReback"+"更新乘客状态出错");
				throw  new RuntimeException("SpcarOrderServiceImpl/initialReback"+"更新乘客状态出错");
		}
        //推送信息给微信
        	sendToWebchatClient(findOrder, SpcarPassengerPushCode.moneyReback);
        //推送信息
     	 pushMsg(findOrder,  SpcarPassengerPushCode.moneyReback, "推送信息給乘客");
		//如果该订单有司机时，取消订单的信息推送过去
     	 if(StringUtil.notEmpty(findOrder.getDriverId()+"")){
     		String  imName = "";//司机IMName
     		 if(findOrder.getSpcarDriver()!=null){//如果订单中不包含司机信息时
     			imName = findOrder.getSpcarDriver().getImName();
     		 }else{
     			 SpcarDriver spcarDriver = spcarDriverMapper.selectByPrimaryKey(findOrder.getDriverId());
     			if(spcarDriver!=null){//司机信息不为空
     				imName = spcarDriver.getImName();
     			}
     		 }
     		//通知司机
 			String sign = null;
 			try {
 				sign = TlsSignUtil.getTlsSignKey(Constant.manager);
 			} catch (Exception e) {
 				log.error("获取签名失败！");
 				e.printStackTrace();
 			}
 			try {
 				JsonConfig config = new JsonConfig();
 				config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
 				JSONObject jsonOrder = JSONObject.fromObject(findOrder, config);
 				JSONObject responseObject = new JSONObject();
 				responseObject.put("msg", "该订单已取消了！");
 				responseObject.put("code", SpcarDriverPushCode.ORDERCANCEL);
 				responseObject.put("data", jsonOrder);
 				if (IMMsgRequestUtil.sendMsg(sign, 1, Constant.manager,imName, RequestJson.getTextJsonMsg(responseObject.toString()),false,SpcarDriverPushCode.ORDERCANCELOfflineMsg)) {
                 } else {
                     log.error("SpcarOrderServiceImpl/initialReback======单聊通知失败:imName=" + imName + "======");
                 }
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
     	 }
	}

	@Override
	public List<SpcarOrder> getGoingDriverOrder(String driverId) {
		// TODO Auto-generated method stub
		return spcarOrderMapper.findGoingDriverOrder(driverId);
	}

	@Override
	public Page<SpcarOrder> getPageGoingDriverOrder(Integer pageNumber, Integer pageStartSize, SpcarOrder spcarOrder) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNumber, pageStartSize);
		return spcarOrderMapper.findPageGoingDriverOrder(spcarOrder);
	}
}
