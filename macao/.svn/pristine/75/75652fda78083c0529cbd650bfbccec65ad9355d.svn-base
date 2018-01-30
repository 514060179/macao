package com.yinghai.macao.common.service;

import java.util.Date;
import java.util.List;

import com.yinghai.macao.common.model.Location;

/**
 * 实时位置业务接口
 * @author Administrator
 *
 */
public interface SpcarLocationService {
	/**
	 * 根据时间和userId查询位置信息
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	List<Location> findLocation(Integer userId,Date start,Date end);

	int createLoc(Double locXd, Double locYd, Integer spcarDriverId);
}
