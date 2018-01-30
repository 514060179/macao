package com.yinghai.macao.app.service.impl;

import com.yinghai.macao.app.service.PayPalService;
import com.yinghai.macao.common.dao.MeterMapper;
import com.yinghai.macao.common.dao.SpcarOrderMapper;
import com.yinghai.macao.common.model.Meter;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.service.MeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/5/26.
 */
public class PayPalServiceImpl implements PayPalService {
    @Autowired
    private SpcarOrderMapper spcarOrderMapper;
    @Autowired
    private MeterMapper meterMapper;
    @Transactional
    @Override
    public int paypalOrder(SpcarOrder spcarOrder, Meter meter) {
        //订单
        SpcarOrder updateOrder = new SpcarOrder();
        spcarOrder.setSpcarOrderId(spcarOrder.getSpcarOrderId());
        return 0;
    }
}
