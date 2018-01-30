package com.yinghai.macao.common.dao;

import com.yinghai.macao.common.model.Meter;

import java.util.List;


public interface MeterMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(Meter record);

    Meter selectByPrimaryKey(Integer id);

}