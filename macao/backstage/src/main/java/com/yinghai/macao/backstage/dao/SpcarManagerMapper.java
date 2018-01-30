package com.yinghai.macao.backstage.dao;


import java.util.List;

import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.common.util.Page;

public interface SpcarManagerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SpcarManager record);

    int insertSelective(SpcarManager record);

    SpcarManager selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpcarManager record);

    int updateByPrimaryKey(SpcarManager record);
    
    SpcarManager selectByName(String username);

    Page<SpcarManager> findList(SpcarManager loopManager);
    
    SpcarManager selectByEmail(String email);
    
}