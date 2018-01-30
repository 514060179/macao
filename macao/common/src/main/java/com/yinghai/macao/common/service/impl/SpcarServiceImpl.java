package com.yinghai.macao.common.service.impl;

import com.yinghai.macao.common.dao.SpcarMapper;
import com.yinghai.macao.common.model.Spcar;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.service.SpcarService;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */
public class SpcarServiceImpl implements SpcarService {
    @Autowired
    private SpcarMapper spcarMapper;
    @Override
    public List<Spcar> findNoUsedList() {
        Spcar spcar = new Spcar();
        spcar.setSpcarUsed(false);
        return spcarMapper.findListByCondition(spcar);
    }
    @Override
	public Page<Spcar> findList(int pageNum, int pageSize, Spcar spcar) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum,pageSize);
		return spcarMapper.findList(spcar);
	}

	@Override
	public int delete(Integer spcarId) {
		// TODO Auto-generated method stub
		return spcarMapper.deleteByPrimaryKey(spcarId);
	}

	@Override
	public Spcar findById(Integer spcarId) {
		// TODO Auto-generated method stub
		return spcarMapper.selectByPrimaryKey(spcarId);
	}
	@Override
	public int createSpcar(Spcar spcar) {
		// TODO Auto-generated method stub
		return spcarMapper.insertSelective(spcar);
	}

	@Override
	public int updateSpcar(Spcar spcar) {
		// TODO Auto-generated method stub
		return spcarMapper.updateByPrimaryKeySelective(spcar);
	}
	@Override
	public Spcar findBySpcarNo(String spcarNo) {
		// TODO Auto-generated method stub
		return spcarMapper.findBySpcarNo(spcarNo);
	}
	@Override
	public List<Spcar> findAllList() {
		Spcar spcar = new Spcar();
        return spcarMapper.findListByCondition(spcar);
	}
}
