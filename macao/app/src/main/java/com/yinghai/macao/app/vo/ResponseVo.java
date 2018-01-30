package com.yinghai.macao.app.vo;

import com.yinghai.macao.common.util.ResponseUtils;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/6/7.
 */
public class ResponseVo {

    /**
     * 缺少参数
     * @param response
     * @param msg
     */
    public static void send101Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "101");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }

    /**
     * 数据不存在，例如司机用户，订单信息
     * @param response
     * @param msg
     */
    public static void send102Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "102");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 注册手机号码亦存在
     * @param response
     * @param msg
     */
    public static void send1021Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "1021");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }

    /**
     * 被拉黑单没有删除，deleted=1  如司机、乘客被拉黑返回
     * @param response
     * @param msg
     */
    public static void send110Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "110");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 獲取管理員簽名失敗
     * @param response
     * @param msg
     */
    public static void send112Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "112");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 加入群聊失败
     * @param response
     * @param msg
     */
    public static void send122Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "122");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 第三方登录失败
     * @param response
     * @param msg
     */
    public static void send121Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "121");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 登录类型错误
     * @param response
     * @param msg
     */
    public static void send123Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "123");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    
    
    
    /**
     * 成功返回
     * @param response
     * @param msg
     * @param data
     */
    public static void send1Code(HttpServletResponse response,String msg,JSONObject data){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "1");
        responseObject.put("msg", msg);
        responseObject.put("data", data);
        ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 未绑定手机号
     * @param response
     * @param msg
     * @param data
     */
    public static void send2Code(HttpServletResponse response,String msg,JSONObject data){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "2");
    	responseObject.put("msg", msg);
    	responseObject.put("data", data);
    	ResponseUtils.renderJson(response, responseObject.toString());
    }

    /**
     * 调用第三方接口失败！
     * @param response
     * @param msg
     */
    public static void sendNotMeErrorCode(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "-111");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }

    /**
     * 没有更新到数据错误（数据原因导致）
     * @param response
     * @param msg
     */
    public static void sendNoDataUpdateCode(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "000");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }

    /**
     * 单点登录验证失败！
     * @param response
     * @param msg
     */
    public static void send111Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "111");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }

    /**
     * 操作有异常（当前状态与操作状态不一致，如 司机前往接载（3）状态下，登出（登出，在线状态（1）下才可以登出）。）
     * @param response
     * @param msg
     */
    public static void send103Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "103");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }

    /**
     * 数据格式异常
     * @param response
     * @param msg
     */
    public static void send119Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "119");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }

    /**
     * 非本人操作异常 如 不是你的订单，你却做相应操作时
     * @param response
     * @param msg
     */
    public static void send120Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "120");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 重复操作
     * @param response
     * @param msg
     */
    public static void send106Code(HttpServletResponse response,String msg){
        JSONObject responseObject = new JSONObject();
        responseObject.put("code", "106");
        responseObject.put("msg", msg);
        responseObject.put("data", new JSONObject());
        ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 验证码错误
     * @param response
     * @param msg
     */
    public static void send105Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "105");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 该手机号已经被绑定
     * @param response
     * @param msg
     */
    public static void send124Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "124");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 該手機已綁定該微信，無需重新綁定
     * @param response
     * @param msg
     */
    public static void send115Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "115");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 該微信已綁定其他司机信息，禁止綁定
     * @param response
     * @param msg
     */
    public static void send116Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "116");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 该手机已有绑定的微信
     * @param response
     * @param msg
     */
    public static void send117Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "117");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 校验第三方登录失败
     * @param response
     * @param msg
     */
    public static void send130Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "130");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 系统异常
     * @param response
     * @param msg
     */
    public static void send999Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "999");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }
    /**
     * 密碼錯誤
     * @param response
     * @param msg
     */
    public static void send140Code(HttpServletResponse response,String msg){
    	JSONObject responseObject = new JSONObject();
    	responseObject.put("code", "140");
    	responseObject.put("msg", msg);
    	responseObject.put("data", new JSONObject());
    	ResponseUtils.renderJson(response, responseObject.toString());
    }


    
	/**
	 * 封装的响应方法
	 * @param code	响应码
	 * @param msg	响应信息
	 * @param data	响应数据
	 * @param response	响应对象
	 */
	public static void common(String code,String msg,JSON data,HttpServletResponse response){
		JSONObject responseObject = new JSONObject();
		responseObject.put("code", code);
		responseObject.put("msg", msg);
		responseObject.put("data", data);
		ResponseUtils.renderJson(response, responseObject.toString());
	}

}
