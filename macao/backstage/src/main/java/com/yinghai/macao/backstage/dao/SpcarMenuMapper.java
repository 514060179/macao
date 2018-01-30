package com.yinghai.macao.backstage.dao;

import java.util.List;
import java.util.Map;

import com.yinghai.macao.backstage.model.SpcarMenu;


public interface SpcarMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SpcarMenu record);

    int insertSelective(SpcarMenu record);

    SpcarMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpcarMenu record);

    int updateByPrimaryKey(SpcarMenu record);
    List<SpcarMenu> findMenus(Map<String, Object> map);
}