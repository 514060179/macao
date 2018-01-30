package com.yinghai.macao.common.service;

import com.yinghai.macao.common.model.OrderDirection;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface OrderDirectionService {
    /**
     * 新增方向
     * @param direction
     * @return
     */
    int saveOrderDirection(OrderDirection direction);

    /**
     * 获取列表
     * @param orderDirection
     * @return
     */
    List<OrderDirection> findList(OrderDirection orderDirection);
}
