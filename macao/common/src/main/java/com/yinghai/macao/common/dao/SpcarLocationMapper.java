package com.yinghai.macao.common.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.macao.common.model.Location;

public interface SpcarLocationMapper {
	
	List<Location> findLocationByTimeAndUserId(@Param("userId")Integer userId,@Param("start")Date start,@Param("end")Date end);

	int insertSelective(Location l);
}
