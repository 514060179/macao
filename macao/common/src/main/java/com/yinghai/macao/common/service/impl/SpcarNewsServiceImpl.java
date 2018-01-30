package com.yinghai.macao.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.yinghai.macao.common.dao.SpcarNewsMapper;
import com.yinghai.macao.common.model.SpcarNews;
import com.yinghai.macao.common.service.SpcarNewsService;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;

public class SpcarNewsServiceImpl implements SpcarNewsService {
	@Resource
	private SpcarNewsMapper spcarNewsMapper;
	
	@Override
	public Page<SpcarNews> findListSelectiveAndPage(int pageNum, int pageSize, SpcarNews spcarNews) {
		PageHelper.startPage(pageNum, pageSize);
		return spcarNewsMapper.findListSelective(spcarNews);
	}
	
	@Override
	public Page<SpcarNews> findListSelective(int pageNum, int pageSize,SpcarNews spcarNews) {
		PageHelper.startPage(pageNum, pageSize);
		return spcarNewsMapper.findListSelectiveApp(spcarNews);
	}
	
	@Override
	public SpcarNews findNewsById(Integer newsId) {
		return spcarNewsMapper.selectByPrimaryKey(newsId);
	}

	@Override
	public int updateNews(SpcarNews news) {
		return spcarNewsMapper.updateByPrimaryKeySelective(news);
	}

	@Override
	public int addNews(SpcarNews news) {
		return spcarNewsMapper.insertSelective(news);
	}

	@Override
	public int deleteNews(Integer newsId) {
		return spcarNewsMapper.deleteByPrimaryKey(newsId);
	}



}
