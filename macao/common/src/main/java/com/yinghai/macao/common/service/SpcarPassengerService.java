package com.yinghai.macao.common.service;


import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.util.Page;

import java.util.List;
import java.util.Map;

public interface SpcarPassengerService {
	/**
	 * 新增专车乘客
	 * @param sign
	 * @param imClient
	 * @param id
	 * @return
	 */
	int addSpcarPassenger(String sign, String imClient, Integer id);
	/**
	 * 新增专车乘客
	 * @param spcarPassenger
	 * @return
	 */
	int addSpcarPassenger(SpcarPassenger spcarPassenger);

	/**
	 * 更新专车状态
	 * @param spcarId
	 * @param status
	 * @return
	 */
	int updateSpcarPaaengerStatus(Integer spcarId,Integer status);
	
	SpcarPassenger findById(Integer spcarPassengerId);
	
	SpcarPassenger findByTel(String countryCode,String tel);
	
	/**
	 * 查找列表
	 * @param pageNum 第几页
	 * @param pageSize 每页条数
	 * @param spcarPassenger
	 * @return
	 */
	Page<SpcarPassenger> findList(int pageNum,int pageSize,SpcarPassenger spcarPassenger);
	List<SpcarPassenger> findAllList(SpcarPassenger spcarPassenger);
	int updateSpcarPaaenger(SpcarPassenger spcarPassenger); 
	int updateTaxigoUser(Map<String,String> map);
	/**
	 * 更新专车中乘客信息，同时更新TaxiGo中乘客和用户信息
	 * @param spcarPassenger
	 * @return
	 */
	int updateSpcarPassengerAndTaxiGo(SpcarPassenger spcarPassenger);

	/**
	 * 新增乘客
	 * @param spcarPassenger
	 * @return
	 */
	int addPassenger(SpcarPassenger spcarPassenger);


	/**
	 * 更新用戶
	 * @param spcarPassenger
	 * @return
	 */
	int updateSelect(SpcarPassenger spcarPassenger);
}
