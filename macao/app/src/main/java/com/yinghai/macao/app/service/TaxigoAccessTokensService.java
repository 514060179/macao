package com.yinghai.macao.app.service;

public interface TaxigoAccessTokensService {
	/**
	 * 获取最近登录的AccessToken
	 * @param userId
	 * @return
	 */
	String validateUserToken(String userId);
	String createUserToken(String userId);
	String getUserTokenByUseIdI(String userId);
}
