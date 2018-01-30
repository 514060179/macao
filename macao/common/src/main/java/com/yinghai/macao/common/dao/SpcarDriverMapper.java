package com.yinghai.macao.common.dao;

import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.util.Page;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SpcarDriverMapper {
    int deleteByPrimaryKey(Integer spcarDriverId);

    int insert(SpcarDriver record);

    int insertSelective(SpcarDriver record);

    SpcarDriver selectByPrimaryKey(Integer spcarDriverId);

    int updateByPrimaryKeySelective(SpcarDriver record);

    int updateByPrimaryKeyWithBLOBs(SpcarDriver record);

    int updateByPrimaryKey(SpcarDriver record);

    /**
     * 按条件查询列表
     * @param record
     * @return
     */
    List<SpcarDriver> findListByCondition(SpcarDriver record);
    
    Page<SpcarDriver> findList(SpcarDriver spcarDriver);
    
    SpcarDriver findByTel(@Param("countryCode")String countryCode,@Param("tel")String tel);

    int updateDriverRate();
    
    Page<SpcarDriver> findPageOnlineDriver(List<SpcarDriver> driverList);
    
  
}