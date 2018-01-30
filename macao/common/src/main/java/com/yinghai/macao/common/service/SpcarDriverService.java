package com.yinghai.macao.common.service;

import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.util.Page;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */
public interface SpcarDriverService {
    /**
     * 查询所有在线司机
     * @return
     */
    List<SpcarDriver> findOnlineList();
    
	/**
	 * 查找列表
	 * @param pageNum 第几页
	 * @param pageSize 每页条数
	 * @param spcarDriver
	 * @return
	 */
	Page<SpcarDriver> findList(int pageNum,int pageSize,SpcarDriver spcarDriver);
	int delete(Integer id);
	
	SpcarDriver findById(Integer id);
	int createSpcarDriver(SpcarDriver spcarDriver)throws Exception ;
	int updateSpcarDriver(SpcarDriver spcarDriver);
	SpcarDriver findByTel(String countryCode,String tel);

	/**
	 * 查询所有司机
	 * @return
	 */
	List<SpcarDriver> findAllList();
	/**
	 * 司机登出时移出群组
	 * @param spcarDriver
	 * @return
	 * @throws Exception
	 */
	int loginOut(SpcarDriver spcarDriver)throws Exception ;

	int updateDriverRate();
	
	Page<SpcarDriver> getPageOnlineDriver(Integer pageNum,Integer pageSize,List<SpcarDriver> driverList);
	
	/**
	 * 更新司机IM登录签名
	 * @param driverId
	 * @param sign
	 * @return
	 */
	int updateDriverSign(Integer driverId,String sign);
    

}
