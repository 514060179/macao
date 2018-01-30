package com.yinghai.macao.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.macao.common.dao.SpcarLocationMapper;
import com.yinghai.macao.common.model.Location;
import com.yinghai.macao.common.service.SpcarLocationService;

public class SpcarLocationServiceImpl implements SpcarLocationService {
	@Autowired
	private SpcarLocationMapper spcarLocationMapper;
	@Override
	public List<Location> findLocation(Integer userId, Date start, Date end) {
		return spcarLocationMapper.findLocationByTimeAndUserId(userId, start, end);
	}
	@Override
	public int createLoc(Double locXd, Double locYd, Integer spcarDriverId) {
		Location l = new Location();
		l.setUserId(spcarDriverId);
		l.setLocX(locXd);
		l.setLocY(locYd);
		l.setCreateTime(new Date());
		return spcarLocationMapper.insertSelective(l);
	}

}
