package com.yinghai.macao.backstage.controller;

import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.common.util.ResponseUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/5/16.
 */
@Controller
@RequestMapping("vip")
public class BackSessionVerifi {

    @RequestMapping("/session")
    public void session(HttpServletRequest request, HttpServletResponse response) {
        Object object = request.getSession().getAttribute("spcarManager");
        JSONObject responseObject = new JSONObject();
        if(object==null){
            responseObject.put("msg", "fail");
            responseObject.put("code", "1");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
        }else{
            responseObject.put("msg", "success");
            responseObject.put("code", "1");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
        }
    }
}
