package com.yinghai.macao.app.interceptor;

import com.yinghai.macao.app.dao.TaxigoAccessTokensDao;
import com.yinghai.macao.app.model.TaxigoAccessTokens;
import com.yinghai.macao.app.vo.ResponseVo;
import com.yinghai.macao.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/6/7.
 */
public class SpcarSingleLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TaxigoAccessTokensDao taxigoAccessTokensDao;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        try {
            httpServletRequest.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String authorization = httpServletRequest.getHeader("Authorization");
        String spcarDriverId = httpServletRequest.getParameter("spcarDriverId");
        if(StringUtil.empty(spcarDriverId)) {
            ResponseVo.send101Code(httpServletResponse,"spcarDriverId为空！");
            return false;
        }
        if(StringUtil.empty(authorization)) {
            ResponseVo.send101Code(httpServletResponse,"authorization为空！");
            return false;
        }
        TaxigoAccessTokens tokens = taxigoAccessTokensDao.selectByUserId(spcarDriverId,"spcarDriver");
        String id =  tokens.getId();
        if(authorization.equals(id)){
            return true;
        }else{
            ResponseVo.send111Code(httpServletResponse,"authorization验证失败！");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
