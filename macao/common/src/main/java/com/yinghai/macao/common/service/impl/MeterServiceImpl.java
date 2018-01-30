package com.yinghai.macao.common.service.impl;

import java.util.List;

import com.yinghai.macao.common.dao.MeterMapper;
import com.yinghai.macao.common.model.Meter;
import com.yinghai.macao.common.service.MeterService;
import org.springframework.beans.factory.annotation.Autowired;



public class MeterServiceImpl implements MeterService {
	@Autowired
	private MeterMapper meterMapper;

	public void insertSelective(Meter order) {
		// TODO Auto-generated method stub
		meterMapper.insertSelective(order);
	}

	public Meter selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return meterMapper.selectByPrimaryKey(id);
	}


}
