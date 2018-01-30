package com.yinghai.macao.common.util;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.yinghai.macao.common.constant.Constant;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/19.
 */
public class PaypalUtil {
    /**
     * paypal退款
     * @param paymentId
     * @param totalMoney 退款金
     * @return
     */
    public static String reback(String paymentId,String totalMoney){
        //获取订单信息
        JSONObject response = new JSONObject();
        APIContext apiContext = new APIContext(Constant.clientId, Constant.clientSecret, Constant.mode);
        Payment payment = null;
        try {
            payment = Payment.get(apiContext,paymentId);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            response.put("code","-1");
            response.put("msg","退款操作，查询订单详情失败！paymentId："+paymentId);
            return response.toString();
        }
        String saleId = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();//获取sale_id
        //进行退款操作
        RefundRequest refund = new RefundRequest();
        Amount amount = new Amount();
        amount.setTotal(totalMoney);
        amount.setCurrency("HKD");
        refund.setAmount(amount);
        Sale sale = new Sale();
        sale.setId(saleId);
        DetailedRefund detailedRefund = null;
        try {
            // Refund sale
            detailedRefund  = sale.refund(apiContext, refund);
            System.out.println(detailedRefund);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            response.put("code","-1");
            response.put("msg","退款操作，退款失败！paymentId："+paymentId);
            return response.toString();
        }
        System.out.println("Payment retrieved ID = " + payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId()
                + ", status = " + payment.getState());
//        JSONObject detailedRefundObject = JSONObject.
//        if("".detailedRefund.getState())
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject data = new JSONObject();
        data.put("totalMoney",new BigDecimal(detailedRefund.getAmount().getTotal()));
        data.put("paymentId",paymentId);
        response.put("code","1");
        response.put("msg","success");
        response.put("data",data);
        return response.toString();
    }

    public static void main(String[] args) {
        System.out.println(reback("PAY-5D94056484849673ELFEJYXI","0.01"));
    }
}
