package com.yinghai.macao.common.service.impl;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.dao.SpcarDriverMapper;
import com.yinghai.macao.common.dao.SpcarLocationMapper;
import com.yinghai.macao.common.dao.SpcarPoiMapper;
import com.yinghai.macao.common.model.Location;
import com.yinghai.macao.common.model.PoiAddress;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.util.IMMsgRequestUtil;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TecentImUtils;
import com.yinghai.macao.common.util.TlsSignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2017/5/16.
 */
public class SpcarDriverServiceImpl implements SpcarDriverService {
    @Autowired
    private SpcarDriverMapper spcarDriverMapper;
    @Autowired
    private SpcarLocationMapper spcarLocationMapper;
	private Logger log = Logger.getLogger(this.getClass());
    @Override
    public List<SpcarDriver> findOnlineList() {
        SpcarDriver spcarDriver = new SpcarDriver();
		spcarDriver.setVerification("123");//这里用来查询状态不等于999处理
		spcarDriver.setLastUpdate(new Date());
        return spcarDriverMapper.findListByCondition(spcarDriver);
    }
    
	@Override
	public Page<SpcarDriver> findList(int pageNum, int pageSize, SpcarDriver spcarDriver) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum,pageSize);
		return spcarDriverMapper.findList(spcarDriver);
	}

	@Override
	public int delete(Integer spcarDriverId) {
		// TODO Auto-generated method stub
		return spcarDriverMapper.deleteByPrimaryKey(spcarDriverId);
	}

	@Override
	public SpcarDriver findById(Integer spcarDriverId) {
		// TODO Auto-generated method stub
		return spcarDriverMapper.selectByPrimaryKey(spcarDriverId);
	}
	@Transactional
	@Override
	public int createSpcarDriver(SpcarDriver spcarDriver) throws Exception {
		// TODO Auto-generated method stub
		int i = 0;
		try {
			 i = spcarDriverMapper.insertSelective(spcarDriver);
			 if(i<1){
					// TODO: handle exception
					log.debug("===================生成司機出錯");
					throw new RuntimeException();
			 }
		} catch (Exception e) {
			// TODO: handle exception
			log.debug("===================生成司機出錯");
			e.printStackTrace();
			throw new RuntimeException();
		}
		

		String sign = "";
		String login = "";
		String userSign = TlsSignUtil.getTlsSignKey(spcarDriver.getSpcarDriverId() + "Driver");
		sign = TlsSignUtil.getTlsSignKey(Constant.manager);
		if (StringUtil.notEmpty(sign)) {
			
			login = TecentImUtils.login(sign, spcarDriver.getSpcarDriverId() + "Driver",null);
			
			JSONObject loginJson = JSONObject.fromObject(login);
			if (!loginJson.getString("ActionStatus").equals("OK")) {
				log.debug("===================導入IM賬號失敗");
				throw new RuntimeException();
			}
		} else {
			log.debug("===================獲取管理員簽名失敗");
			throw new RuntimeException();
		}
		// IM名字
		String imName = spcarDriver.getSpcarDriverId() + "Driver";
		//标签名字
		JSONArray tags = new JSONArray();
		tags.add(Constant.driver);
		String addTags = TecentImUtils.addTags(sign, imName,tags);
		JSONObject addTagsJson = JSONObject.fromObject(addTags);
		if (!addTagsJson.getString("ActionStatus").equals("OK")) {
			log.debug("===================添加用戶標籤失敗");
			throw new RuntimeException();
		}
		List<String> memberList = new ArrayList<>();
		memberList.add(spcarDriver.getSpcarDriverId() + "Driver");
		// 加入群聊
		Boolean addGroupMember = IMMsgRequestUtil.addGroupMember(sign, Constant.DriverGroupId, memberList);
		if (!addGroupMember) {
			log.debug("===================加入群聊失敗");
			throw new RuntimeException();
		}
		
		//IM賬號的管理
		SpcarDriver updateSpcarDriver = new SpcarDriver();
		updateSpcarDriver.setSpcarDriverId(spcarDriver.getSpcarDriverId());
		updateSpcarDriver.setImName(imName);
		updateSpcarDriver.setSign(userSign);
		updateSpcarDriver(updateSpcarDriver);
		try {
			 i = updateSpcarDriver(updateSpcarDriver);
			 if(i<1){
				 log.debug("===================添加IM賬號信息失敗");
					throw new RuntimeException();
			 }
		}catch (Exception e) {
			// TODO: handle exception
			log.debug("===================添加IM賬號信息失敗");
			e.printStackTrace();
			throw new RuntimeException();
		}
		return i;
	}

	@Override
	@Transactional
	public int updateSpcarDriver(SpcarDriver spcarDriver) {
		int i = spcarDriverMapper.updateByPrimaryKeySelective(spcarDriver);
		Location l = new Location();
		l.setCreateTime(new Date());
		l.setLocX(spcarDriver.getLocX());
		l.setLocY(spcarDriver.getLocY());
		l.setUserId(spcarDriver.getSpcarDriverId());
		int j = spcarLocationMapper.insertSelective(l);
		if(j!=1||i!=1){
			throw new RuntimeException("更新司机位置信息失败");
		}
		return i;
	}

	@Override
	public SpcarDriver findByTel(String countryCode, String tel) {
		// TODO Auto-generated method stub
		return spcarDriverMapper.findByTel(countryCode, tel);
	}

	@Override
	public List<SpcarDriver> findAllList() {
		return spcarDriverMapper.findListByCondition(new SpcarDriver());
	}
	
	@Override
	public int loginOut(SpcarDriver spcarDriver) throws Exception {
		// TODO Auto-generated method stub
		int i = spcarDriverMapper.updateByPrimaryKeySelective(spcarDriver);
		if(i<1){
			log.error("SpcarDriverServiceImpl/loginOut  司机状态更新失败");
			throw new RuntimeException("SpcarDriverServiceImpl/loginOut  司机状态更新失败！");
		}
		  //登出时移出群组
        String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
        if(StringUtil.empty(sign)){
        	log.error("SpcarDriverServiceImpl/loginOut===================获取管理员签名失败");
        	throw new RuntimeException("SpcarDriverServiceImpl/loginOut  获取管理员签名失败！");
        }
        List<String> memberList = new ArrayList<>();
		memberList.add(spcarDriver.getSpcarDriverId() + "Driver");
		// 移出群聊
		Boolean removeGroupMember = IMMsgRequestUtil.removeMember(sign, Constant.DriverGroupId, memberList,1,"");
		if (!removeGroupMember) {
			log.error("SpcarDriverServiceImpl/loginOut===================移出群聊失敗");
			throw new RuntimeException("SpcarDriverServiceImpl/loginOut  移出群聊失敗！");
		}
		return i;
	}

	@Override
	public int updateDriverRate() {
		return spcarDriverMapper.updateDriverRate();
	}

	@Override
	public Page<SpcarDriver> getPageOnlineDriver(Integer pageNum, Integer pageSize, List<SpcarDriver> driverList) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum,pageSize);
		return spcarDriverMapper.findPageOnlineDriver(driverList);
	}

	@Override
	public int updateDriverSign(Integer driverId, String sign) {
		// TODO Auto-generated method stub
		SpcarDriver record = new SpcarDriver();
		record.setSpcarDriverId(driverId);
		record.setSign(sign);
		return spcarDriverMapper.updateByPrimaryKeySelective(record);
	}


}
