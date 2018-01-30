package com.yinghai.macao.common.service.impl;

import com.yinghai.macao.common.dao.OrderDirectionMapper;
import com.yinghai.macao.common.model.OrderDirection;
import com.yinghai.macao.common.service.OrderDirectionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
public class OrderDirectionServiceImpl implements OrderDirectionService {

    @Autowired
    private OrderDirectionMapper orderDirectionMapper;
    @Override
    public int saveOrderDirection(OrderDirection direction) {
        return orderDirectionMapper.insertSelective(direction);
    }

    @Override
    public List<OrderDirection> findList(OrderDirection orderDirection) {
        return orderDirectionMapper.findListByCondition(orderDirection);
    }
}
