package com.yinghai.macao.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.macao.common.model.PoiAddress;
import com.yinghai.macao.common.util.Page;

public interface SpcarPoiMapper {

	Page<PoiAddress> findList(PoiAddress p);

	Page<PoiAddress> findListBack(PoiAddress poi);

	PoiAddress findById(Integer id);

	int insertSelective(PoiAddress p);

	int updateSelective(PoiAddress p);

	int findMaxId();

	int deleteSelective(PoiAddress poi);

	PoiAddress findByLoc(@Param("locX")Double locX, @Param("locY")Double locY);

}
