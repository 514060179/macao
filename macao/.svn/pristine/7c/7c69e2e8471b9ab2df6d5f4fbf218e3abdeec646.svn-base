package com.yinghai.macao.common.dao;

import com.yinghai.macao.common.model.Spcar;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.util.Page;

import java.util.List;

public interface SpcarMapper {
    int deleteByPrimaryKey(Integer spcarId);

    int insert(Spcar record);

    int insertSelective(Spcar record);

    Spcar selectByPrimaryKey(Integer spcarId);

    int updateByPrimaryKeySelective(Spcar record);

    int updateByPrimaryKey(Spcar record);
    Spcar findBySpcarNo(String spcarNo);
    List<Spcar> findListByCondition(Spcar record);
    Page<Spcar> findList(Spcar spcarDriver);
}