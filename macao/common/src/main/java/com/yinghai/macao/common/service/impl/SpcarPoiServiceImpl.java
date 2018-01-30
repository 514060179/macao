package com.yinghai.macao.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.macao.common.dao.SpcarPoiMapper;
import com.yinghai.macao.common.model.PoiAddress;
import com.yinghai.macao.common.service.SpcarPoiService;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;

public class SpcarPoiServiceImpl implements SpcarPoiService {
	@Autowired
	private SpcarPoiMapper spcarPoiMapper;
	@Override
	public Page<PoiAddress> findListByPage(Integer page, Integer pageSize, PoiAddress p) {
		PageHelper.startPage(page, pageSize);
		return spcarPoiMapper.findList(p);
	}
	@Override
	public Page<PoiAddress> findListBackByPage(Integer page, Integer pageSize, PoiAddress p) {
		PageHelper.startPage(page, pageSize);
		return spcarPoiMapper.findListBack(p);
	}
	@Override
	public PoiAddress findById(Integer id) {
		return spcarPoiMapper.findById(id);
	}
	@Override
	public int createPoi(PoiAddress p) {
		return spcarPoiMapper.insertSelective(p);
	}
	@Override
	public int updatePoi(PoiAddress p) {
		return spcarPoiMapper.updateSelective(p);
	}
	@Override
	public int findMaxId() {
		return spcarPoiMapper.findMaxId();
	}
	@Override
	public int deletePoi(PoiAddress poi) {
		return spcarPoiMapper.deleteSelective(poi);
	}
	@Override
	public PoiAddress findByLoc(Double locX, Double locY) {
		return spcarPoiMapper.findByLoc(locX,locY);
	}

}
