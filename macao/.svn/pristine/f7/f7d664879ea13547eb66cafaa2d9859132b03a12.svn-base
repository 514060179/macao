package com.yinghai.macao.common.service.impl;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.dao.ParameterMapper;
import com.yinghai.macao.common.dao.SpcarCommentMapper;
import com.yinghai.macao.common.dao.SpcarDriverMapper;
import com.yinghai.macao.common.dao.SpcarOrderMapper;
import com.yinghai.macao.common.model.Parameter;
import com.yinghai.macao.common.model.SpcarComment;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.ParameterService;
import com.yinghai.macao.common.service.SpcarCommentService;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.util.IMMsgRequestUtil;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;
import com.yinghai.macao.common.util.StringUtil;
import com.yinghai.macao.common.util.TecentImUtils;
import com.yinghai.macao.common.util.TlsSignUtil;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2017/5/16.
 */
public class SpcarCommentServiceImpl implements SpcarCommentService {
    @Autowired
    private SpcarCommentMapper spcarCommentMapper;
    @Autowired
    private SpcarOrderMapper spcarOrderMapper;
	private Logger log = Logger.getLogger(this.getClass());

    
	@Override
	public Page<SpcarComment> findList(int pageNum, int pageSize, SpcarComment spcarComment) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum,pageSize);
		return spcarCommentMapper.findList(spcarComment);
	}

	@Override
	public int delete(Integer id) {
		// TODO Auto-generated method stub
		return spcarCommentMapper.deleteByPrimaryKey(id);
	}

	@Override
	public SpcarComment findById(Integer id) {
		// TODO Auto-generated method stub
		return spcarCommentMapper.selectByPrimaryKey(id);
	}
	/**
	 * 提交评论并且更新订单信息
	 */
	@Transactional
	@Override
	public int createSpcarComment(SpcarComment spcarComment)  {
		Integer orderId = spcarComment.getOrderId();
		SpcarOrder spcarOrder = new SpcarOrder();
		spcarOrder.setSpcarOrderId(orderId);
		spcarOrder.setComment(true);
		int i = 0;
		try {
			 i = spcarOrderMapper.updateByPrimaryKeySelective(spcarOrder);//将订单修改成
			if(i<1){
				log.error("SpcarComment/createSpcarComment is error,spcarOrder update error");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("SpcarComment/createSpcarComment is error,spcarOrder update error"+e);
			throw new RuntimeException();
		}
		try {
			i=spcarCommentMapper.insertSelective(spcarComment);
			if(i<1){
				log.error("SpcarComment/createSpcarComment is error,spcarComment create error");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("SpcarComment/createSpcarComment is error,spcarComment create error"+e);
			throw new RuntimeException();
		}
			 return  i;
			
	}

	@Override
	public int findCount(SpcarComment spcarComment) {
		// TODO Auto-generated method stub
		return spcarCommentMapper.findCount(spcarComment);
	}
	
}
