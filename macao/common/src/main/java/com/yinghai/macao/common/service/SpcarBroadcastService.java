package com.yinghai.macao.common.service;

import java.util.List;


import com.yinghai.macao.common.model.SpcarBroadcast;
import com.yinghai.macao.common.util.Page;

public interface SpcarBroadcastService {

	int save(SpcarBroadcast broadcast);
	
	int updateById(SpcarBroadcast broadcast);
	
	int deleteById(Integer id);
	
	SpcarBroadcast selectById(Integer id);
	
	List<SpcarBroadcast> getAll(SpcarBroadcast broadcast);
	
	Page<SpcarBroadcast> findAll(int pageNum, int pageSize,SpcarBroadcast spcarBroadcast);
}
