package com.yinghai.macao.common.service;

import java.util.List;

import com.yinghai.macao.common.model.PoiAddress;
import com.yinghai.macao.common.util.Page;

public interface SpcarPoiService {
	/**
	 * 前端，分页查询poi列表
	 * @param page
	 * @param pageSize
	 * @param p
	 * @return
	 */
	Page<PoiAddress> findListByPage(Integer page, Integer pageSize, PoiAddress p);
	/**
	 * 後台管理，分頁查詢poi列表
	 * @param page
	 * @param pageSize
	 * @param p
	 * @return
	 */
	Page<PoiAddress> findListBackByPage(Integer page, Integer pageSize, PoiAddress p);
	/**
	 * 根据id查询poi
	 * @param id
	 * @return
	 */
	PoiAddress findById(Integer id);
	/**
	 * 新增poi
	 * @param p
	 * @return
	 */
	int createPoi(PoiAddress p);
	/**
	 * 更新poi
	 * @param p
	 * @return
	 */
	int updatePoi(PoiAddress p);
	/**
	 * 查詢當前表最大id
	 * @return
	 */
	int findMaxId();
	/**
	 * 删除poi
	 * @param poi
	 * @return
	 */
	int deletePoi(PoiAddress poi);
	/**
	 * 通过位置查询poi
	 * @param locX
	 * @param locY
	 * @return
	 */
	PoiAddress findByLoc(Double locX, Double locY);

}
