package com.yinghai.macao.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yinghai.macao.common.model.ThirdParty;

public interface ThirdPartyMapper {
	/**
	 * 根据openId查询第三方登录表
	 * @param openId
	 * @return
	 */
	ThirdParty selectByOpenid(@Param("openId")String openId,@Param("userType")Integer userType);
	/**
	 * 新增一条第三方登录数据
	 * @param thirdParty
	 * @return
	 */
	int create(ThirdParty thirdParty);
	/**
	 * 查询对应Id所绑定的所有第三方数据
	 * @param spcarDriverId
	 * @return
	 */
	List<ThirdParty> findListByUserId(@Param("userId")Integer spcarId,@Param("type")Integer type,@Param("userType")Integer userType);
	/**
	 * 更新第三方數據(更新用戶Id)
	 * @param thirdParty
	 * @return
	 */
	int updateData(ThirdParty thirdParty);
	/**
	 * 根據一定條件查詢第三方登錄數據
	 * @param third
	 * @return
	 */
	List<ThirdParty> findListSelective(ThirdParty third);
	
	ThirdParty selectByPrimaryKey(Integer id);
	int updateByPrimaryKeySelective(ThirdParty third);

}
