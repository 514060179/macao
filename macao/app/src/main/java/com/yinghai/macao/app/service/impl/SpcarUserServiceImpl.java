package com.yinghai.macao.app.service.impl;

import com.yinghai.macao.app.dao.TaxigoAccessTokensDao;
import com.yinghai.macao.app.model.TaxigoAccessTokens;
import com.yinghai.macao.app.service.SpcarUserService;
import com.yinghai.macao.common.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/6.
 */
public class SpcarUserServiceImpl implements SpcarUserService {
    @Autowired
    private TaxigoAccessTokensDao taxigoAccessTokensDao;
    @Override
    public int updateAccessToken(Integer spcarPassengerId, String accessToken,String type) {
        TaxigoAccessTokens tokens = new TaxigoAccessTokens();
        tokens.setCreated(new Date());
        tokens.setUserid(spcarPassengerId);
        tokens.setId(accessToken);
        if(StringUtil.notEmpty(type)){
        	tokens.setRealm("spcarPassenger");
        }else{
        	tokens.setRealm("spcarDriver");
        }
        return taxigoAccessTokensDao.insertSelective(tokens);
    }
}
