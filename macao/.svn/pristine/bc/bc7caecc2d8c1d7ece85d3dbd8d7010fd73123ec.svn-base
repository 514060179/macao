package com.yinghai.macao.common.job;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.constant.SpcarDriverPushCode;
import com.yinghai.macao.common.constant.SpcarPassengerPushCode;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.util.IMMsgRequestUtil;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.RequestJson;
import com.yinghai.macao.common.util.TlsSignUtil;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.yinghai.macao.common.util.TlsSignUtil.getTlsSignKey;

/**
 * Created by Administrator on 2017/8/2.
 */
@Component
public class SpcarOrderTask {

    @Autowired
    private SpcarOrderService spcarOrderService;

    private Logger log = Logger.getLogger(this.getClass());

    public void sendMsgToSpcarPassenger(){
        log.info("提醒乘客，预约订单即将进行！");
        SpcarOrder spcarOrder = new SpcarOrder();
        spcarOrder.setStatusArray(new String[]{SpcarOrder.ORDER_PAIRED_STATUS+""});
        List<SpcarOrder> list = spcarOrderService.findList(spcarOrder);

        for (SpcarOrder order:list
             ) {
            SpcarPassenger spcarPassenger = order.getSpcarPassenger();
            Date now = new Date();
            if((order.getStartTime().getTime()-now.getTime())>=1000*60*60&&(order.getStartTime().getTime()-now.getTime())<=(1000*60*60+10*1000)){//一个小时執行推送一次
                JsonConfig config = new JsonConfig();
                config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
                JSONObject jsonOrder = JSONObject.fromObject(order, config);
                JSONObject responseObject = new JSONObject();
                responseObject.put("msg", SpcarPassengerPushCode.passengerTipMsg);
                responseObject.put("code", SpcarPassengerPushCode.passengerTip);
                responseObject.put("data", jsonOrder);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String sign = null;
                            try {
                                sign = TlsSignUtil.getTlsSignKey(Constant.manager);
                            } catch (Exception e) {
                                log.error("获取签名失败！");
                                e.printStackTrace();
                            }
                            IMMsgRequestUtil.sendMsg(sign,1,Constant.manager,spcarPassenger.getImName(),RequestJson.getTextJsonMsg(responseObject.toString()),true,SpcarPassengerPushCode.passengerTipMsg);
                        } catch (Exception e) {
                            log.equals(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
