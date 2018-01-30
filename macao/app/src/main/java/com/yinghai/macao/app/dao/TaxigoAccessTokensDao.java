package com.yinghai.macao.app.dao;

import com.yinghai.macao.app.model.TaxigoAccessTokens;
import org.apache.ibatis.annotations.Param;

public interface TaxigoAccessTokensDao {
	  int deleteByPrimaryKey(String id);

	    int insert(TaxigoAccessTokens record);

	    int insertSelective(TaxigoAccessTokens record);

	    TaxigoAccessTokens selectByPrimaryKey(String id);
	    
	    TaxigoAccessTokens selectByUserId(@Param("userId") String userId, @Param("realm")String realm);

	    int updateByPrimaryKeySelective(TaxigoAccessTokens record);

	    int updateByPrimaryKey(TaxigoAccessTokens record);
	    
	    TaxigoAccessTokens selectByRealmId(Integer realmId);
}