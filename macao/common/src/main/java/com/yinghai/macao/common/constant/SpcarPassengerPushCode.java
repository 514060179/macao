package com.yinghai.macao.common.constant;

/**
 * Created by Administrator on 2017/5/16.
 */
public class SpcarPassengerPushCode {
    /**
     * 后台下单推送给APP乘客
     */
    public final static String pushOrder = "700" ;

    public final static String pushOrderOfflineMsg = "您收到一張帶支付訂單！" ;
    /**
     * 订单匹配专车成功，推送乘客端（待确认-->已确认）
     */
    public final static String matchingSuccess = "701" ;

    public final static String matchingSuccessOfflineMsg = "您有一條訂單匹配成功！" ;

    /**
     * 司機前往接載乘客，推送乘客端
     */
    public final static String driverSetOut = "702" ;

    public final static String driverSetOutOfflineMsg = "您的司機已前往接載！" ;

    /**
     * 司機到達接載乘客目的地，推送乘客端
     */
    public final static String driverArrive = "703" ;

    public final static String driverArriveOfflineMsg = "您的司機已到達等候！" ;

    /**
     * 退款成功，推送乘客端
     */
    public final static String moneyReback = "704" ;

    public final static String moneyRebackOfflineMsg = "抱歉！訂車沒有被確認，已退款，請重訂！" ;

    /**
     * 司機接到乘客，推送乘客端
     */
    public final static String carry = "705";
    /**
     * 订单完成，推送乘客端
     */
    public final static String finished = "706";

    public final static String finishedOfflineMsg = "您有張訂單已完成！";

    /**
     * 改派司机专车成功，推送乘客端
     */
    public final static String changeSuccess = "707";
    
    public final static String changeSuccessOfflineMsg = "您的訂單已改派其他司機專車！";

    /**
     * 預約訂單提醒，推送乘客端
     */
    public final static String passengerTip = "708";

    public final static String passengerTipMsg = "您有張預約的訂單，將在一個小時后執行";
    
    /**
     * 按照标签推送给乘客端
     */
    public final static String PUSHBYTAGS = "709" ;
    
    /**
     * 乘客的订单发生改变
     */
    public final static String ORDEREDIT = "710" ;
    public final static String ORDEREDITOfflineMsg="您有一张订单发生了改变";
}
