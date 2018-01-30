package com.yinghai.macao.backstage.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yinghai.macao.backstage.dao.OperationLogMapper;
import com.yinghai.macao.backstage.model.OperationLog;


public class OperationLogServiceImpl implements OperationLogService {
	@Resource
	private OperationLogMapper operationLogMapper;
	@Override
	public int insert(OperationLog log) {
		int i = operationLogMapper.insertSelective(log);
		return i;
	}

}
