package com.yinghai.macao.common.dao;

import java.util.List;

import com.yinghai.macao.common.model.SpcarNews;
import com.yinghai.macao.common.util.Page;

public interface SpcarNewsMapper {
    int deleteByPrimaryKey(Integer newsId);

    int insert(SpcarNews record);

    int insertSelective(SpcarNews record);

    SpcarNews selectByPrimaryKey(Integer newsId);

    int updateByPrimaryKeySelective(SpcarNews record);

    int updateByPrimaryKeyWithBLOBs(SpcarNews record);

    int updateByPrimaryKey(SpcarNews record);
    
    Page<SpcarNews> findListSelective(SpcarNews record);
    
    Page<SpcarNews> findListSelectiveApp(SpcarNews record);
    
}