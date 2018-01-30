package com.yinghai.macao.backstage.service.impl;


import java.util.List;

import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.common.util.Page;

public interface SpcarManagerService {
	/**
	 * 根据用户名查找管理员
	 * @return
	 */
	SpcarManager findOneByUserName(String username);
	
	/**
	 * 新增管理员
	 * @param loopManager
	 * @return
	 */
	int createOne(SpcarManager spcarManager);

	/**
	 * 查找列表
	 * @param pageNum 第几页
	 * @param pageSize 每页条数
	 * @param loopManager
	 * @return
	 */
	Page<SpcarManager> findList(int pageNum,int pageSize,SpcarManager spcarManager);

	/**
	 * 根据id查找管理员
	 * @param id
	 * @return
	 */
	SpcarManager findOneByUserId(Integer id);

	/**
	 * 更新记录
	 * @param loopManager
	 * @return
	 */
	int updateOne(SpcarManager spcarManager);

	int deleteOne(Integer id);
	/**
	 * 根据用户名查找管理员
	 * @return
	 */
	SpcarManager findOneByEmail(String email);
	
	List<SpcarManager> findAllList(SpcarManager spcarManager);
}
