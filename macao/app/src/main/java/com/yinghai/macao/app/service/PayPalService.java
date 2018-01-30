package com.yinghai.macao.app.service;

import com.yinghai.macao.common.model.Meter;
import com.yinghai.macao.common.model.SpcarOrder;

/**
 * Created by Administrator on 2017/5/26.
 */
public interface PayPalService {

    int paypalOrder(SpcarOrder spcarOrder, Meter meter);
}
