package com.yinghai.macao.common.service.impl;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.dao.SpcarPassengerMapper;
import com.yinghai.macao.common.dao.TaxigoPassengerMapper;
import com.yinghai.macao.common.dao.TaxigoUserMapper;
import com.yinghai.macao.common.model.Passenger;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.model.TaxigoUser;
import com.yinghai.macao.common.service.SpcarPassengerService;
import com.yinghai.macao.common.util.IMMsgRequestUtil;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TecentImUtils;
import com.yinghai.macao.common.util.TlsSignUtil;
import com.yinghai.macao.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SpcarPassengerServiceImpl implements SpcarPassengerService {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SpcarPassengerMapper spcarPassengerMapper;
	@Autowired
	private TaxigoUserMapper taxigoUserMapper;
	@Autowired
	private TaxigoPassengerMapper taxigoPassengerMapper;
	public int addSpcarPassenger(String sign, String imClient, Integer id) {
		// TODO Auto-generated method stub
		return spcarPassengerMapper.insertByTaxigoUser(sign,imClient,id);
	}
	public int addSpcarPassenger(SpcarPassenger spcarPassenger) {
		int i = spcarPassengerMapper.insertSelective(spcarPassenger);
		if(i<1){
			log.error("新增乘客用户信息失败:==============================SpcarPassengerServiceImpl/addSpcarPassenger"+i);
		}
		try {
			createIm(spcarPassenger);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return spcarPassenger.getSpcarId();
	}

	public int updateSpcarPaaengerStatus(Integer spcarId, Integer status) {
		SpcarPassenger spcarPassenger = new SpcarPassenger();
		spcarPassenger.setSpcarId(spcarId);
		spcarPassenger.setStatus(status);
		return spcarPassengerMapper.updateByPrimaryKeySelective(spcarPassenger);
	}
	@Override
	public SpcarPassenger findById(Integer spcarPassengerId) {
		// TODO Auto-generated method stub
		return spcarPassengerMapper.selectByPrimaryKey(spcarPassengerId);
	}
	@Override
	public Page<SpcarPassenger> findList(int pageNum, int pageSize, SpcarPassenger spcarPassenger) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum,pageSize);
		return spcarPassengerMapper.findList(spcarPassenger);
	}
	@Override
	public SpcarPassenger findByTel(String countryCode, String tel) {
		// TODO Auto-generated method stub
		return spcarPassengerMapper.findByTel(countryCode, tel);
	}
	@Transactional
	@Override
	public int updateSpcarPaaenger(SpcarPassenger spcarPassenger) {
		// TODO Auto-generated method stub
		int i = 0;
//		try {
//			spcarPassengerMapper.updateTaxigoUser(spcarPassenger.getSpcarId()+"", spcarPassenger.getVip());
//			i++;
//		} catch (Exception e) {
//			// TODO: handle exception
//			log.error("����taxigoUserʧ��"+e);
//			e.printStackTrace();
//			throw  new RuntimeException("����taxigoUserʧ��");
//			
//		}
		try {
			spcarPassengerMapper.updateByPrimaryKeySelective(spcarPassenger);
			i++;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("����ר���˿���Ϣʧ��"+e);
			e.printStackTrace();
			throw  new RuntimeException("����ר���˿���Ϣʧ��");
		}
		return i;
	}
	@Override
	public int updateTaxigoUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return spcarPassengerMapper.updateUserByMap(map);
	}
	@Override
	public List<SpcarPassenger> findAllList(SpcarPassenger spcarPassenger) {
		// TODO Auto-generated method stub
		 return spcarPassengerMapper.findList(spcarPassenger);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateSpcarPassengerAndTaxiGo(SpcarPassenger spcarPassenger) {
		//更新TaxiGo中的乘客和用户
		TaxigoUser taxigo = taxigoUserMapper.findTaxigoUser(TransformUtils.toInt(spcarPassenger.getSpcarId()));
		if(taxigo==null){
			throw new RuntimeException("taxigouser is not found");
		}
		TaxigoUser updateTaxigo = new TaxigoUser();
		Passenger passenger = new Passenger();
		updateTaxigo.setId(taxigo.getId());
		passenger.setId(TransformUtils.toInt(spcarPassenger.getSpcarId()));
		updateTaxigo.setVip(spcarPassenger.getVip());
		updateTaxigo.setSex(spcarPassenger.getSex());
		updateTaxigo.setTitle(spcarPassenger.getTitle());
		passenger.setTitle(spcarPassenger.getTitle());
		updateTaxigo.setGivenName(spcarPassenger.getGivenName());
		passenger.setGivenName(spcarPassenger.getGivenName());
		updateTaxigo.setFamilyName(spcarPassenger.getFamilyName());
		passenger.setFamilyName(spcarPassenger.getFamilyName());
		updateTaxigo.setName(spcarPassenger.getFamilyName()+spcarPassenger.getGivenName());
		passenger.setName(spcarPassenger.getFamilyName()+spcarPassenger.getGivenName());
		int i = taxigoUserMapper.updateTaxigoUserSelective(updateTaxigo);
		if(i!=1){
			throw new RuntimeException("update fail,TaxigoUser update fail");
		}
		int j = taxigoPassengerMapper.updatePassengerWithBLOBsSelective(passenger);
		if(j!=1){
			throw new RuntimeException("update fail,passenger update fail");
		}
		//更新专车中的乘客
		int k = spcarPassengerMapper.updateByPrimaryKeySelective(spcarPassenger);
		if(k!=1){
			throw new RuntimeException("update fail,spcarPassenger update fail");
		}
		return 1;
	}

	@Transactional
	public SpcarPassenger createIm(SpcarPassenger spcarPassenger) throws Exception {
		// TODO Auto-generated method stub
		String sign = "";
		String login = "";
		String userSign = TlsSignUtil.getTlsSignKey(spcarPassenger.getSpcarId() + "Passenger");
		sign = TlsSignUtil.getTlsSignKey(Constant.manager);
		if (StringUtil.notEmpty(sign)) {
			// 导入账号，返回登录是否成功的信息
			login = TecentImUtils.login(sign, spcarPassenger.getSpcarId() + "Passenger",null);
			// 解析返回来的数据
			JSONObject loginJson = JSONObject.fromObject(login);
			if (!loginJson.getString("ActionStatus").equals("OK")) {
				log.error("================导入账号失败TaxigoUserServiceImpl/createIm"+loginJson);
				throw new RuntimeException("================导入账号失败TaxigoUserServiceImpl/createIm"+loginJson);
				
			}
		} else {
			log.error("================获取管理员签名失败TaxigoUserServiceImpl/createIm");
			throw new RuntimeException("================获取管理员签名失败TaxigoUserServiceImpl/createIm");
		}
		//标签名字
		JSONArray tags = new JSONArray();
		tags.add("Passenger");
		// IM账号
		String imName = spcarPassenger.getSpcarId() + "Passenger";
		String addTags = TecentImUtils.addTags(sign, imName,tags);
		JSONObject addTagsJson = JSONObject.fromObject(addTags);
		if (!addTagsJson.getString("ActionStatus").equals("OK")) {
			log.error("================添加用户签名失败TaxigoUserServiceImpl/createIm"+addTagsJson);
			throw new RuntimeException("================添加用户签名失败TaxigoUserServiceImpl/createIm"+addTagsJson);
		}
		List<String> memberList = new ArrayList<>();
		memberList.add(spcarPassenger.getSpcarId() + "Passenger");
		// 加入群组
		Boolean addGroupMember = IMMsgRequestUtil.addGroupMember(sign, Constant.passengerGroupId, memberList);
		if (!addGroupMember) {
			log.error("================加入群组失败TaxigoUserServiceImpl/createIm");
			throw new RuntimeException("================加入群组失败TaxigoUserServiceImpl/createIm");
		}
		SpcarPassenger upSpcarPassenger = new SpcarPassenger();
		upSpcarPassenger.setSpcarId(spcarPassenger.getSpcarId());
		upSpcarPassenger.setImName(imName);
		upSpcarPassenger.setSign(userSign);
		spcarPassenger.setImName(imName);
		spcarPassenger.setSign(userSign);
		int i = spcarPassengerMapper.updateByPrimaryKeySelective(upSpcarPassenger);
		return spcarPassenger;
	}


    @Transactional
    @Override
    public int addPassenger(SpcarPassenger spcarPassenger) {
        //新增用户
        int i = spcarPassengerMapper.insertSelective(spcarPassenger);
        //创建IM
        String sign = "";
        String userSig = "";
        SpcarPassenger update = new SpcarPassenger();
        update.setSpcarId(spcarPassenger.getSpcarId());
        try {
            sign = TlsSignUtil.getTlsSignKey(Constant.manager);
            // IM名字
            String imName = spcarPassenger.getSpcarId() + "Passenger";
            update.setImName(imName);
            userSig = TlsSignUtil.getTlsSignKey(imName);
            update.setSign(userSig);
            String login = TecentImUtils.login(sign,imName,spcarPassenger.getName());
            JSONObject loginJson = JSONObject.fromObject(login);
            if (!loginJson.getString("ActionStatus").equals("OK")) {
                log.error("===================導入IM賬號失敗");
                throw new RuntimeException("導入IM賬號失敗");
            }
            //标签名字
            JSONArray tags = new JSONArray();
            tags.add(Constant.passenger);
            String addTags = TecentImUtils.addTags(sign, imName,tags);
            JSONObject addTagsJson = JSONObject.fromObject(addTags);
            if (!addTagsJson.getString("ActionStatus").equals("OK")) {
                log.error("===================添加用戶標籤失敗");
                throw new RuntimeException("添加用戶標籤失敗");
            }
            //加入群聊
            List<String> memberList = new ArrayList<>();
            memberList.add(spcarPassenger.getSpcarId() + "Passenger");
            // 加入群聊
            Boolean addGroupMember = IMMsgRequestUtil.addGroupMember(sign, Constant.passengerGroupId, memberList);
            if (!addGroupMember) {
                log.error("===================加入群聊失敗");
                throw new RuntimeException("加入群聊失敗");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        i+=spcarPassengerMapper.updateByPrimaryKeySelective(update);
        return i;
    }

    @Override
    public int updateSelect(SpcarPassenger spcarPassenger) {
        return spcarPassengerMapper.updateByPrimaryKeySelective(spcarPassenger);
    }
}
