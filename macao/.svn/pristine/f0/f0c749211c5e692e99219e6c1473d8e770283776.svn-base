package com.yinghai.macao.app.interceptor;

import com.yinghai.macao.app.util.ValidateAPITokenUtil;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/6/5.
 */
public class ApiAccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        try {
            httpServletRequest.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String apiSendTime = httpServletRequest.getParameter("apiSendTime");// 获取api_send_time生成token
        String apiToken = httpServletRequest.getParameter("apiToken");// 获取apiToken
        JSONObject responseObject = new JSONObject();
        if(StringUtil.empty(apiSendTime)){
            responseObject.put("code","101");
            responseObject.put("msg","apiSendTime为空！");
            responseObject.put("data",new JSONObject());
            ResponseUtils.renderJson(httpServletResponse,responseObject.toString());
            return false;
        }
        if(StringUtil.empty(apiToken)){
            responseObject.put("code","101");
            responseObject.put("msg","apiToken为空！");
            responseObject.put("data",new JSONObject());
            ResponseUtils.renderJson(httpServletResponse,responseObject.toString());
            return false;
        }
        //验证
        if(ValidateAPITokenUtil.ValidatingApiToken(apiSendTime,apiToken)){
            return true;
        }
        responseObject.put("code","101");
        responseObject.put("msg","api验证失败！");
        responseObject.put("data",new JSONObject());
        ResponseUtils.renderJson(httpServletResponse,responseObject.toString());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
