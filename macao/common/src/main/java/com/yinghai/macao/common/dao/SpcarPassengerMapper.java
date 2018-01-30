package com.yinghai.macao.common.dao;

import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.util.Page;

import org.apache.ibatis.annotations.Param;

import java.util.Map;


public interface SpcarPassengerMapper {
	
	SpcarPassenger findByTel(@Param("countryCode")String countryCode, @Param("tel")String tel);
	
    int deleteByPrimaryKey(Integer spcarId);

    int insert(SpcarPassenger record);

    int insertSelective(SpcarPassenger record);

    SpcarPassenger selectByPrimaryKey(Integer spcarId);

    int updateByPrimaryKeySelective(SpcarPassenger record);

    int updateByPrimaryKeyWithBLOBs(SpcarPassenger record);
    
    int updateTaxigoUser(@Param("realmId")String realmId,@Param("vip")Boolean vip);
    

    int updateByPrimaryKey(SpcarPassenger record);
    
    Page<SpcarPassenger> findList(SpcarPassenger spcarPassenger);
    
    int insertByTaxigoUser(@Param("isgn") String sign, @Param("imClient") String imClient, @Param("id") Integer id);

    int updateUserByMap(@Param("map")Map<String,String> map);
}