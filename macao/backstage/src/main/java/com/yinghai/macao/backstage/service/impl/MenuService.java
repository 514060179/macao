package com.yinghai.macao.backstage.service.impl;

import java.util.List;
import java.util.Map;

import com.yinghai.macao.backstage.model.SpcarMenu;



public interface MenuService {
	
	List<SpcarMenu> findMenuList(Map<String, Object> map);
}
