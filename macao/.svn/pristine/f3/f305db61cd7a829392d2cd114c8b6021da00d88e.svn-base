package com.yinghai.macao.app.controller;

import com.yinghai.macao.app.dao.SpcarVersionControlMapper;
import com.yinghai.macao.app.model.SpcarVersionControl;
import com.yinghai.macao.app.vo.ResponseVo;
import com.yinghai.macao.common.model.Spcar;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.ResponseUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/22.
 */
@Controller
@RequestMapping("spcar/version")
public class SpcarVersionController {

    @Autowired
    private SpcarVersionControlMapper spcarVersionControlMapper;

    @RequestMapping(value = "/now", method = RequestMethod.POST)
    public void spcarVersion(HttpServletRequest request, HttpServletResponse response){
        String deviceType = request.getParameter("deviceType"); //设备类型 1 ios 2android
        SpcarVersionControl v = new SpcarVersionControl();
        if("".equals(deviceType)||deviceType==null){
            ResponseVo.send101Code(response,"deviceType is null");
            return;
        }else{
            v.setDeviceType(deviceType);
        }
        String realm = request.getParameter("realm"); //司机端、乘客端
        if("".equals(realm)||realm==null){
            realm = "driver";
        }
        v.setRealm(realm);
        SpcarVersionControl version = spcarVersionControlMapper.selectVersion(v);
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject data = JSONObject.fromObject(version, config);
        ResponseVo.send1Code(response,"success",data);
    }
}
