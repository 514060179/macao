package com.yinghai.macao.backstage.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yinghai.macao.backstage.dao.SpcarMenuMapper;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.backstage.model.SpcarMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class LoginRequestInterceptor implements HandlerInterceptor {
    @Autowired
    private SpcarMenuMapper spcarMenuMapper;
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
        HttpSession session = req.getSession(true);
        // 从session 里面获取用户名的信息
        Object obj = session.getAttribute("spcarManager");
        Object menu = session.getAttribute("menu");
        String requestPath = req.getRequestURI();
        String[] lable = requestPath.split("\\/");

        int lableIndex = lable.length - 2;
        String lables = lable[lableIndex];
        SpcarManager spcarManager = (SpcarManager)obj;
        boolean jurisdiction = false;
        if(spcarManager!=null){
            if (spcarManager.getRoleId()!=0){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("lable",lables);
                SpcarMenu spcarMenu = spcarMenuMapper.findMenus(map).get(0);
                jurisdiction = !spcarMenu.getJurisdiction();
            }else{
                jurisdiction = true;
            }
        }
        // 判断如果没有取到用户信息，就跳转到登陆页面，提示用户进行登陆
        if (obj == null || "".equals(obj.toString()) || menu == null || "".equals(menu.toString())||!jurisdiction) {
            res.sendRedirect(req.getContextPath() + "/managerLogin/tologin");
            return false;
        }
        return true;
    }


}
