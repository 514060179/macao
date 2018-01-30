package com.yinghai.macao.app.model;

/**
 * Created by Administrator on 2017/6/6.
 */
public class AjaxResult {

    public static final String STATUS_SUCCESS ="1";
    public static final String STATUS_FAILED ="-1";

    /**
     * 提示信息
     */
    private String msg;
    /**
     * 请求结果状态
     */
    private String code;
    /**

     * 请求数据
     */
    private String data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
