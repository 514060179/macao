package com.yinghai.macao.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.yinghai.macao.app.vo.ResponseVo;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.service.SpcarLocationService;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.service.SpcarPoiService;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.StringUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/spcar/spcarDriver")
public class DriverController {

	@Autowired
	private SpcarLocationService spcarLocationService;
	
    @Autowired
    SpcarDriverService spcarDriverService;
    @Autowired
    SpcarOrderService spcarOrderService;
    /**
     * 自动登录获取司机信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "driverMe", method = RequestMethod.POST)
    public void driverMe(HttpServletRequest request, HttpServletResponse response) {
        //获取司机id
        String spcarDriverId = request.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        SpcarDriver spcarDriver = spcarDriverService.findById(Integer.parseInt(spcarDriverId));
        if(spcarDriver==null){
            ResponseVo.send102Code(response,"司机不存在！");
            return;
        }
        JSONObject spcarDriverObject = new JSONObject();
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject data = JSONObject.fromObject(spcarDriver, config);
        spcarDriverObject.put("spcarDriver",data);
        //获取订单信息
        String s = spcarDriver.getStatus();
        String status = "";
        switch (s){
            case "0":status="0";break;//载客中 进行中
            case "1":status="3";break;//上线 已配对
            case "3":status="4";break;//前往接载  接载中
            case "4":status="4";break;//到达目的地
        }

        List<SpcarOrder> spcarOrder = spcarOrderService.findByDriverId(spcarDriver.getSpcarDriverId(),status);
        JSONArray d = JSONArray.fromObject(spcarOrder, config);
        spcarDriverObject.put("spcarOrderList",d);
        ResponseVo.send1Code(response,"success",spcarDriverObject);
    }

    /**
     * 更新司机信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "driverUpdate", method = RequestMethod.POST)
    public void driverUpdate(HttpServletRequest request, HttpServletResponse response) {
        String spcarDriverId = request.getParameter("spcarDriverId");
        String givenName = request.getParameter("givenName"); //名字/教名
        String familyName = request.getParameter("familyName"); //姓氏
//        String sex = request.getParameter("sex");
        if(StringUtil.empty(givenName)) {
            ResponseVo.send101Code(response,"givenName为空！");
            return;
        }
        if(StringUtil.empty(familyName)) {
            ResponseVo.send101Code(response,"familyName为空！");
            return;
        }
//        if(StringUtil.empty(sex)) {
//            ResponseVo.send101Code(response,"sex为空！");
//            return;
//        }
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        SpcarDriver spcarDriver = spcarDriverService.findById(Integer.parseInt(spcarDriverId));
        if(spcarDriver==null){
            ResponseVo.send102Code(response,"司机不存在！");
            return;
        }
        SpcarDriver updateDriver = new SpcarDriver();
        updateDriver.setSpcarDriverId(spcarDriver.getSpcarDriverId());
        updateDriver.setFamilyName(familyName);
        updateDriver.setGivenName(givenName);
        updateDriver.setName(familyName+givenName);
        int i = spcarDriverService.updateSpcarDriver(updateDriver);
        if(i<0){
            ResponseVo.sendNoDataUpdateCode(response,"更新不成功！");
        }else{
            spcarDriver.setFamilyName(familyName);
            spcarDriver.setGivenName(givenName);
            JSONObject spcarDriverObject = new JSONObject();
            JsonConfig config = new JsonConfig();
            config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
            JSONObject data = JSONObject.fromObject(spcarDriver, config);
            spcarDriverObject.put("spcarDriver",data);
            ResponseVo.send1Code(response,"success",spcarDriverObject);
        }
    }

    /**
     * 司机登出
     * @param request
     * @param response
     * @throws Exception 
     */
    @RequestMapping(value = "driverLogout", method = RequestMethod.POST)
    public void driverLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String spcarDriverId = request.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        SpcarDriver spcarDriver = spcarDriverService.findById(Integer.parseInt(spcarDriverId));
        if(spcarDriver==null){
            ResponseVo.send102Code(response,"司机不存在！");
            return;
        }
        if(SpcarDriver.DRIVER_ONLINE_STATUS.equals(spcarDriver.getStatus())){
            //修改状态
            SpcarDriver updateDriver = new SpcarDriver();
            updateDriver.setSpcarDriverId(spcarDriver.getSpcarDriverId());
            updateDriver.setStatus(SpcarDriver.DRIVER_OFFLINE_STATUS);
            updateDriver.setLastUpdate(new Date());
            int i = spcarDriverService.loginOut(updateDriver);
            if(i>0){
                ResponseVo.send1Code(response,"success",new JSONObject());
            }else{
                ResponseVo.sendNoDataUpdateCode(response,"更新司机状态失败！");
            }
        }else{
            ResponseVo.send103Code(response,"当前状态为："+spcarDriver.getStatus()+" 在线状态下（状态码：1）才可登出！");
        }
    }

    /**
     * 更新位置信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "driverLocUpdate", method = RequestMethod.POST)
    public void driverLocUpdate(HttpServletRequest request, HttpServletResponse response) {
        String spcarDriverId = request.getParameter("spcarDriverId");
        String locX = request.getParameter("locX");
        String locY = request.getParameter("locY");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(response,"spcarDriverId为空！");
            return;
        }
        if(StringUtil.empty(locX)) {
            ResponseVo.send101Code(response,"locX为空！");
            return;
        }
        if(StringUtil.empty(locY)) {
            ResponseVo.send101Code(response,"locY为空！");
            return;
        }
        SpcarDriver updateDriver = new SpcarDriver();
        updateDriver.setSpcarDriverId(Integer.parseInt(spcarDriverId));
        try {
            Double locXd = Double.parseDouble(locX);
            updateDriver.setLocX(locXd);
        }catch (Exception e){
            ResponseVo.send119Code(response,"locX格式异常！");
            e.printStackTrace();
        }
        try {
            Double locYd = Double.parseDouble(locY);
            updateDriver.setLocY(locYd);
        }catch (Exception e){
            ResponseVo.send119Code(response,"locY格式异常");
            e.printStackTrace();
        }
        updateDriver.setLastUpdate(new Date());
        int i=0;
		try {
			i = spcarDriverService.updateSpcarDriver(updateDriver);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseVo.sendNoDataUpdateCode(response,"更新司机位置失败！");
			return;
		}
        //更新司机的实时位置
        if(i>0){
            ResponseVo.send1Code(response,"success",new JSONObject());
        }else{
            ResponseVo.sendNoDataUpdateCode(response,"更新司机位置失败！");
        }
    }
}