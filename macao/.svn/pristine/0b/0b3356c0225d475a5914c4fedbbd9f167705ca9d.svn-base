package com.yinghai.macao.app.interceptor;

import com.yinghai.macao.app.dao.TaxigoAccessTokensDao;
import com.yinghai.macao.common.service.SpcarPassengerService;
import com.yinghai.macao.app.model.TaxigoAccessTokens;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/6/5.
 * taxigo单点登录
 */
public class SingleLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TaxigoAccessTokensDao taxigoAccessTokensDao;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        try {
            httpServletRequest.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject responseObject = new JSONObject();
//        System.out.println(spcarPassengerService.findById(8808));
        //查找
        String authorization = httpServletRequest.getHeader("Authorization");
        String spcarPassengerId = httpServletRequest.getParameter("spcarPassengerId");
        if(StringUtil.empty(authorization)){
            responseObject.put("code","101");
            responseObject.put("msg","authorization为空！");
            responseObject.put("data",new JSONObject());
            ResponseUtils.renderJson(httpServletResponse,responseObject.toString());
            return false;
        }
        if(StringUtil.empty(spcarPassengerId)){
            responseObject.put("code","101");
            responseObject.put("msg","spcarPassengerId为空！");
            responseObject.put("data",new JSONObject());
            ResponseUtils.renderJson(httpServletResponse,responseObject.toString());
            return false;
        }
        TaxigoAccessTokens tokens = taxigoAccessTokensDao.selectByUserId(spcarPassengerId,"spcarPassenger");
        if(!authorization.equals(tokens.getId())){
            responseObject.put("code","111");
            responseObject.put("msg","其他设备登录！");
            responseObject.put("data",new JSONObject());
            ResponseUtils.renderJson(httpServletResponse,responseObject.toString());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
