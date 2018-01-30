package com.yinghai.macao.app.service;

import java.util.List;
import java.util.Map;

import com.yinghai.macao.common.model.ThirdParty;

public interface ThirdPartyService {
	/**
	 *  獲取接口調用憑證access_token
	 * @param code	同意登錄參數
	 * @param deviceType	設備類型
	 * @param type	登錄類型
	 * @param realm 标记是司机端还是乘客端
	 * @return
	 */

	public Map<String, Object> getAccessToken(String code, String deviceType, String type,String realm);
	/**
	 * 获取第三方的信息
	 * @param accessToken
	 * @param openid
	 * @param type
	 * @return
	 */
	public Map<String, Object> getInfo(String accessToken, String openid, String type);
	/**
	 * 根据openId查询本地第三方登录数据
	 * @param openId
	 * @return
	 */
	public ThirdParty selectByOpenid(String openId,Integer userType);
	
	/**
	 * 根据ID查询第三方登录数据
	 */
	public ThirdParty selectByPrimaryKey(Integer id);
	/**
	 * 本地新增第三方登录数据
	 * @param thirdParty
	 * @return
	 */
	public int create(ThirdParty thirdParty);
	/**
	 * 查询用户所绑定的所有第三方数据
	 * @param spcarId 乘客或司机ID
	 * @param type 登录类型 1QQ，2微信，3邮箱
	 * @param userType 用户类型，乘客为0，司机为1，专车司机为2
	 * @return
	 */
	public List<ThirdParty> findListByUserId(Integer spcarId,Integer type,Integer userType);
	/**
	 * 更新第三方登錄信息
	 * @param thirdParty
	 * @return
	 */
	public int updateSelective(ThirdParty thirdParty);
	/**
	 * 根據一定條件查詢第三方登錄信息
	 * @param third
	 * @return
	 */
	public List<ThirdParty> selectByThirdParty(ThirdParty third);
	int updateByPrimaryKeySelective(ThirdParty third);
}
