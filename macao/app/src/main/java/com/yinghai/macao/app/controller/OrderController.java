package com.yinghai.macao.app.controller;

import com.yinghai.macao.app.vo.ResponseVo;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TransformUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */
@Controller
@RequestMapping("/spcar/order")
public class OrderController {

    @Autowired
    private SpcarOrderService spcarOrderService;
    @Autowired
    private SpcarDriverService spcarDriverService;
    
    /**
     * 司机点击确认按钮
     * @param request
     * @param response
     * @throws Exception 
     */
    @RequestMapping(value = "driverTBC", method = RequestMethod.POST)
    public void driverTBC(HttpServletRequest request, HttpServletResponse response) throws Exception{

        String spcarDriverId = request.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        String spcarOrderId = request.getParameter("spcarOrderId");
        if(StringUtil.empty(spcarOrderId)) {
            ResponseVo.send101Code(response,"spcarOrderId为空！");
            return;
        }
        SpcarOrder spcarOrder = spcarOrderService.getAppSpcarOrderbyId(Integer.parseInt(spcarOrderId));
        if(SpcarOrder.ORDER_CARRY_STATUS==spcarOrder.getStatus().intValue()){
            ResponseVo.send106Code(response,"订单已经被操作过！当前订单状态为："+spcarOrder.getStatus().intValue());
            return;
        }
        if(!spcarDriverId.equals(spcarOrder.getDriverId()+"")){
            ResponseVo.send120Code(response,"该订单非本人订单！");
            return;
        }
        //司机点击确认
        SpcarOrder updateSpcarOrder = new SpcarOrder();
        updateSpcarOrder.setSpcarOrderId(Integer.parseInt(spcarOrderId));
        updateSpcarOrder.setStatus(SpcarOrder.ORDER_TBC_STATUS);
        updateSpcarOrder.setUpdateTime(new Date());
       // updateSpcarOrder.setTbc(true);
        int i = spcarOrderService.updateByPrimaryKeySelective(updateSpcarOrder,false);
        if(i<1){
        	ResponseVo.sendNoDataUpdateCode(response, "更新失败");
        }
        //spcarOrder.setDriverSetoutTime(new Date());
        //resultSend(response,i,spcarOrder);
        ResponseVo.send1Code(response, "success", new JSONObject());
    }
    
    /**
     * 司机前往接载
     * @param request
     * @param response
     */
    @RequestMapping(value = "driverGotoCarry", method = RequestMethod.POST)
    public void gotoCarry(HttpServletRequest request, HttpServletResponse response){

        String spcarDriverId = request.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        String spcarOrderId = request.getParameter("spcarOrderId");
        if(StringUtil.empty(spcarOrderId)) {
            ResponseVo.send101Code(response,"spcarOrderId为空！");
            return;
        }
        SpcarOrder spcarOrder = spcarOrderService.getAppSpcarOrderbyId(Integer.parseInt(spcarOrderId));
        SpcarDriver spcarDriver = spcarOrder.getSpcarDriver();
        SpcarPassenger spcarPassenger = spcarOrder.getSpcarPassenger();
        if(SpcarOrder.ORDER_CARRY_STATUS==spcarOrder.getStatus().intValue()){
            ResponseVo.send106Code(response,"订单已经被操作过！当前订单状态为："+spcarOrder.getStatus().intValue());
            return;
        }
//        if(SpcarPassenger.PASSENGER_CLOSE_STATUS!=spcarPassenger.getStatus().intValue()){
//            ResponseVo.send103Code(response,"当前乘客状态为："+spcarPassenger.getStatus()+"，状态为："+SpcarPassenger.PASSENGER_CLOSE_STATUS+"才可以前往接载！");
//            return;
//        }
        if(!SpcarDriver.DRIVER_ONLINE_STATUS.equals(spcarDriver.getStatus())){
            ResponseVo.send103Code(response,"当前司机状态为："+spcarDriver.getStatus()+"，状态为："+SpcarDriver.DRIVER_ONLINE_STATUS+"才可以前往接载！");
            return;
        }
        if(!spcarDriverId.equals(spcarOrder.getDriverId()+"")){
            ResponseVo.send120Code(response,"该订单非本人订单！");
            return;
        }
        int i = spcarOrderService.driverSetOut(spcarOrderId);
        spcarOrder.setDriverSetoutTime(new Date());
        resultSend(response,i,spcarOrder);
    }

    /**
     * 司机到达接载地
     * @param request
     * @param response
     */
    @RequestMapping(value = "driverArrive", method = RequestMethod.POST)
    public void driverArrive(HttpServletRequest request, HttpServletResponse response){

        String spcarDriverId = request.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        Page<SpcarOrder> orderList = spcarOrderService.findList(1,2,new SpcarOrder());

        String spcarOrderId = request.getParameter("spcarOrderId");
        if(StringUtil.empty(spcarOrderId)) {
            ResponseVo.send101Code(response,"spcarOrderId为空！");
            return;
        }
        SpcarOrder spcarOrder = spcarOrderService.getAppSpcarOrderbyId(Integer.parseInt(spcarOrderId));
        SpcarDriver spcarDriver = spcarOrder.getSpcarDriver();
        SpcarPassenger spcarPassenger = spcarOrder.getSpcarPassenger();
//        if(SpcarPassenger.PASSENGER_READY_STATUS!=spcarPassenger.getStatus().intValue()){
//            ResponseVo.send103Code(response,"当前乘客状态为："+spcarPassenger.getStatus()+"，状态为："+SpcarPassenger.PASSENGER_READY_STATUS+"才可以司机到达！");
//            return;
//        }
        if(!SpcarDriver.DRIVER_READY_STATUS.equals(spcarDriver.getStatus())){
            ResponseVo.send103Code(response,"当前司机状态为："+spcarDriver.getStatus()+"，状态为："+SpcarDriver.DRIVER_READY_STATUS+"才可以司机到达！");
            return;
        }
        if(!spcarDriverId.equals(spcarOrder.getDriverId()+"")){
            ResponseVo.send120Code(response,"该订单非本人订单！");
            return;
        }
        int i = spcarOrderService.comfirmArrive(spcarOrderId);
        spcarOrder.setDriverArriveTime(new Date());
        resultSend(response,i,spcarOrder);
    }

    /**
     * 司机接待到乘客
     * @param request
     * @param response
     */
    @RequestMapping(value = "driverConfirmCarry", method = RequestMethod.POST)
    public void driverConfirmCarry(HttpServletRequest request, HttpServletResponse response){

        String spcarDriverId = request.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }

        String spcarOrderId = request.getParameter("spcarOrderId");
        if(StringUtil.empty(spcarOrderId)) {
            ResponseVo.send101Code(response,"spcarOrderId为空！");
            return;
        }

        SpcarOrder spcarOrder = spcarOrderService.getAppSpcarOrderbyId(Integer.parseInt(spcarOrderId));
        SpcarDriver spcarDriver = spcarOrder.getSpcarDriver();
        SpcarPassenger spcarPassenger = spcarOrder.getSpcarPassenger();
        if(SpcarOrder.ORDER_WORK_STATUS==spcarOrder.getStatus().intValue()){
            ResponseVo.send106Code(response,"订单已经被操作过！当前订单状态为："+spcarOrder.getStatus().intValue());
            return;
        }
//        if(SpcarPassenger.PASSENGER_READY_STATUS!=spcarPassenger.getStatus().intValue()){
//            ResponseVo.send103Code(response,"当前乘客状态为："+spcarPassenger.getStatus()+"，状态为："+SpcarPassenger.PASSENGER_READY_STATUS+"才可以接到乘客！");
//            return;
//        }
        if(!SpcarDriver.DRIVER_ARRIVE.equals(spcarDriver.getStatus())){
            ResponseVo.send103Code(response,"当前司机状态为："+spcarDriver.getStatus()+"，状态为："+SpcarDriver.DRIVER_ARRIVE+"才可以接到乘客！");
            return;
        }
        if(!spcarDriverId.equals(spcarOrder.getDriverId()+"")){
            ResponseVo.send120Code(response,"该订单非本人订单！");
            return;
        }
        int i = spcarOrderService.comfirmCarry(spcarOrderId);
        spcarOrder.setPassengerArriveTime(new Date());
        resultSend(response,i,spcarOrder);
    }

    /**
     * 完成订单
     * @param request
     * @param response
     */
    @RequestMapping(value = "driverCompleteOrder", method = RequestMethod.POST)
    public void completeOrder(HttpServletRequest request, HttpServletResponse response){

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String spcarDriverId = request.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        String spcarOrderId = request.getParameter("spcarOrderId");
        if(StringUtil.empty(spcarOrderId)) {
            ResponseVo.send101Code(response,"spcarOrderId为空！");
            return;
        }
        SpcarOrder spcarOrder = spcarOrderService.getAppSpcarOrderbyId(Integer.parseInt(spcarOrderId));
        String locX = null;
        String locY = null;
        String endAddress = null;
        if(StringUtil.empty(spcarOrder.getEndAddress())){//当原订单有目的地时无需更新,
        	  locX = request.getParameter("locX");
             if(StringUtil.empty(locX)) {
                 ResponseVo.send101Code(response,"locX为空！");
                 return;
             }
              locY = request.getParameter("locY");
             if(StringUtil.empty(locY)) {
                 ResponseVo.send101Code(response,"locY为空！");
                 return;
             }
              endAddress = request.getParameter("endAddress");
             if(StringUtil.empty(endAddress)) {
                 ResponseVo.send101Code(response,"endAddress为空！");
                 return;
             }
        }
       

       
        SpcarDriver spcarDriver = spcarOrder.getSpcarDriver();
        SpcarPassenger spcarPassenger = spcarOrder.getSpcarPassenger();
        if(SpcarOrder.ORDER_FINISH_STATUS==spcarOrder.getStatus().intValue()){
            ResponseVo.send106Code(response,"订单已经被操作过！当前订单状态为："+spcarOrder.getStatus().intValue());
            return;
        }
//        if(SpcarPassenger.PASSENGER_GET_ON_STATUS!=spcarPassenger.getStatus().intValue()){
//            ResponseVo.send103Code(response,"当前乘客状态为："+spcarPassenger.getStatus()+"，状态为："+SpcarPassenger.PASSENGER_GET_ON_STATUS+"才可以完成订单！");
//            return;
//        }
        if(!SpcarDriver.DRIVER_WORK_STATUS.equals(spcarDriver.getStatus())){
            ResponseVo.send103Code(response,"当前司机状态为："+spcarDriver.getStatus()+"，状态为："+SpcarDriver.DRIVER_WORK_STATUS+"才可以完成订单！");
            return;
        }
        if(!spcarDriverId.equals(spcarOrder.getDriverId()+"")){
            ResponseVo.send120Code(response,"该订单非本人订单！");
            return;
        }
        int i = spcarOrderService.orderFinished(spcarOrderId,locX,locY,endAddress);
        spcarOrder.setCompleteTime(new Date());
        resultSend(response,i,spcarOrder);

    }
    /**
     * 根据状态查询订单
     * @param request
     * @param response
     */
    @RequestMapping(value = "driverRecords", method = RequestMethod.POST) 
    public void driverRecords(HttpServletRequest request, HttpServletResponse response){

        String spcarDriverId = request.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        String status = request.getParameter("status");
        String[] array = null;
        if(!StringUtil.empty(status)){
            array = status.split(";");
        }else{
            array=new String[]{"5"};
        }
        Integer pageNo = TransformUtils.toInt(request.getParameter("page"));
        Integer pageStartSize = TransformUtils.toInt(request.getParameter("pageSize"));
        pageNo = pageNo == 0 ? 1 : pageNo;
        pageStartSize = pageStartSize == 0 ? 10 : pageStartSize;
        SpcarOrder selectOrder = new SpcarOrder();
        selectOrder.setStatusArray(array);
        selectOrder.setDriverId(Integer.parseInt(spcarDriverId));
        Page<SpcarOrder> orderList = spcarOrderService.findFinishList(pageNo,pageStartSize,selectOrder);
        List<SpcarOrder> result = orderList.getResult();
        
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray data = JSONArray.fromObject(result, config);
        JSONObject responseObject = new JSONObject();
        responseObject.put("spcarOrderList",data);
        ResponseVo.send1Code(response,"success",responseObject);

    }
    private void resultSend(HttpServletResponse response,int i,SpcarOrder spcarOrder){
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject data = JSONObject.fromObject(spcarOrder, config);
        JSONObject spcarOrderObject = new JSONObject();
        spcarOrderObject.put("spcarOrder",data);
        if(i>0){
            ResponseVo.send1Code(response,"success",spcarOrderObject);
        }else if(i==-1){
            ResponseVo.sendNotMeErrorCode(response,"系统出错！");
        }else if(i==0){
            ResponseVo.sendNoDataUpdateCode(response,"数据异常！");
        }else if(i==-3){
            ResponseVo.send103Code(response,"操作异常！司机状态/订单状态/乘客状态不一致");
        }else if(i==-10){
            ResponseVo.send1Code(response,"success！但通知乘客失败",spcarOrderObject);
        }else if(i==-11){
            ResponseVo.send1Code(response,"success！但离线通知乘客失败",spcarOrderObject);
        }
    }
   
}
