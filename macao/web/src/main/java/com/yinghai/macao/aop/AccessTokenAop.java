package com.yinghai.macao.aop;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.common.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;


public class AccessTokenAop {
    private Logger log = Logger.getLogger(this.getClass());


    public Object execute(ProceedingJoinPoint pjp){
        String methodName = pjp.getSignature().getName();
        String className=pjp.getTarget().getClass().getSimpleName();
        Object result = null;
        Object[] args = pjp.getArgs();  
        HttpServletResponse response = null;  
        for (int i = 0; i < args.length; i++) {  
            if (args[i] instanceof HttpServletResponse) {  
            	response = (HttpServletResponse) args[i];  
            } 
        } 
        log.info("======>The request method " + methodName + " begins with " + Arrays.asList(pjp.getArgs()));
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sdf.format(new Date());
            log.error("time:"+date+" Exception:"+className+"/"+methodName+" msg:"+e.getLocalizedMessage());
            e.printStackTrace();
            JSONObject object = new JSONObject();
            object.put("code","-111");
            object.put("msg",e.getLocalizedMessage());
            object.put("data",new JSONObject());
            if(response!=null){
            	ResponseUtils.renderJson(response, object.toJSONString());
            }
            return "500";
        }
        log.info("======>The request method " + methodName + " end with massage:"+result );
        return result;
    }
}
