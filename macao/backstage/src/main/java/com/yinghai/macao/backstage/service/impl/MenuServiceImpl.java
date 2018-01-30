package com.yinghai.macao.backstage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.macao.backstage.dao.SpcarMenuMapper;
import com.yinghai.macao.backstage.model.SpcarMenu;


public class MenuServiceImpl implements MenuService {
	@Autowired
	private SpcarMenuMapper menuMapper;
	@Override
	public List<SpcarMenu> findMenuList(Map<String, Object> map) {
		return menuMapper.findMenus(map);
	}

}
