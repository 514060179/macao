package com.yinghai.macao.backstage.service.impl;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.util.TecentImUtils;
import com.yinghai.macao.common.util.TlsSignUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.macao.backstage.dao.SpcarManagerMapper;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class SpcarManagerServiceImpl implements SpcarManagerService {

	@Autowired
	private SpcarManagerMapper spcarManagerMapper;
	@Override
	public SpcarManager findOneByUserName(String username) {
		return spcarManagerMapper.selectByName(username);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public int createOne(SpcarManager spcarManager) {
		int i = spcarManagerMapper.insertSelective(spcarManager);
		//创建IM账号
		try {
			String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
			String imName = "service"+spcarManager.getId();
			JSONObject result = JSONObject.fromObject(TecentImUtils.login(sign,"service"+spcarManager.getId(),spcarManager.getUsername()+"(service"+spcarManager.getId()+")"));
			if(!result.getString("ActionStatus").equals("OK")){
				throw new RuntimeException("新增IM账号失败！ "+result);
			}else{
				//获取IM sign
				String imSign = TlsSignUtil.getTlsSignKey(imName);
				SpcarManager updateManager = new SpcarManager();
				updateManager.setId(spcarManager.getId());
				updateManager.setImName(imName);
				updateManager.setSign(imSign);
				i = spcarManagerMapper.updateByPrimaryKeySelective(updateManager);
				if(i<0){
					throw new RuntimeException("创建IM更新失败！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取IM签名失败!");
		}
		return i;
	}

	@Override
	public Page<SpcarManager> findList(int pageNum,int pageSize,SpcarManager spcarManager) {
		PageHelper.startPage(pageNum,pageSize);
		return spcarManagerMapper.findList(spcarManager);
	}
	@Override
	public SpcarManager findOneByUserId(Integer id){
		return spcarManagerMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateOne(SpcarManager spcarManager) {
		return spcarManagerMapper.updateByPrimaryKeySelective(spcarManager);
	}

	@Override
	public int deleteOne(Integer id) {
		return spcarManagerMapper.deleteByPrimaryKey(id);
	}
	@Override
	public SpcarManager findOneByEmail(String email) {
		// TODO Auto-generated method stub
		return spcarManagerMapper.selectByEmail(email);
	}
	@Override
	public List<SpcarManager> findAllList(SpcarManager spcarManager) {
		// TODO Auto-generated method stub
		return spcarManagerMapper.findList(spcarManager);
	}
}
