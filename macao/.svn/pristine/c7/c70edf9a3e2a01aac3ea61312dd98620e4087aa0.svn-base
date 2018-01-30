package com.yinghai.macao.common.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.macao.common.dao.ParameterMapper;
import com.yinghai.macao.common.model.Parameter;
import com.yinghai.macao.common.service.ParameterService;
import com.yinghai.macao.common.util.Page;
import com.yinghai.macao.common.util.PageHelper;

/**
 * Created by Administrator on 2017/5/16.
 */
public class ParameterServiceImpl implements ParameterService {
    @Autowired
    private ParameterMapper parameterMapper;
	private Logger log = Logger.getLogger(this.getClass());

    
	@Override
	public Page<Parameter> findList(int pageNum, int pageSize, Parameter parameter) {
		PageHelper.startPage(pageNum,pageSize);
		return parameterMapper.findList(parameter);
	}

	@Override
	public int delete(Integer id) {
		return parameterMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Parameter findById(Integer id) {
		return parameterMapper.selectByPrimaryKey(id);
	}
	@Override
	public int createParameter(Parameter parameter) throws Exception {
		 return  parameterMapper.insertSelective(parameter);
	}

	@Override
	public int updateParameter(Parameter parameter) {
		return parameterMapper.updateByPrimaryKeySelective(parameter);
	}

	@Override
	public Parameter findByHour(Integer hour) {
		return parameterMapper.findByHour(hour);
	}

	@Override
	public List<Parameter> findSame(Parameter parameter) {
		return parameterMapper.findSame(parameter);
	}

	@Override
	public Page<Parameter> findListApp(int pageNum, int pageSize, Parameter parameter) {
		PageHelper.startPage(pageNum,pageSize);
		return parameterMapper.findListApp(parameter);
	}
}
