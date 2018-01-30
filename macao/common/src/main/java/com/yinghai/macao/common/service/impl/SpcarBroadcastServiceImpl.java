package com.yinghai.macao.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import com.yinghai.macao.common.dao.SpcarBroadcastMapper;
import com.yinghai.macao.common.model.SpcarBroadcast;
import com.yinghai.macao.common.service.SpcarBroadcastService;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;

public class SpcarBroadcastServiceImpl implements SpcarBroadcastService{
	@Autowired
	private SpcarBroadcastMapper spcarBroadcast; 
	
	@Override
	public int save(SpcarBroadcast broadcast) {
		// TODO Auto-generated method stub
		return spcarBroadcast.insert(broadcast);
	}

	@Override
	public int updateById(SpcarBroadcast broadcast) {
		// TODO Auto-generated method stub
		return spcarBroadcast.updateByPrimaryKey(broadcast);
	}

	@Override
	public int deleteById(Integer id) {
		// TODO Auto-generated method stub
		return spcarBroadcast.deleteByPrimaryKey(id);
	}

	@Override
	public SpcarBroadcast selectById(Integer id) {
		// TODO Auto-generated method stub
		return spcarBroadcast.selectByPrimaryKey(id);
	}

	@Override
	public Page<SpcarBroadcast> findAll(int pageNum, int pageSize,SpcarBroadcast broadcast) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum,pageSize);
		return spcarBroadcast.findList(broadcast);
	}

	@Override
	public List<SpcarBroadcast> getAll(SpcarBroadcast broadcast) {
		// TODO Auto-generated method stub
		return spcarBroadcast.getList(broadcast);
	}


		
	
}