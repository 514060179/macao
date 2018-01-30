package com.yinghai.macao.common.service;

import com.yinghai.macao.common.model.Parameter;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.util.Page;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */
public interface ParameterService {
	/**
	 * 查找列表
	 * @param pageNum 第几页
	 * @param pageSize 每页条数
	 * @param spcarDriver
	 * @return
	 */
	Page<Parameter> findList(int pageNum,int pageSize,Parameter parameter);
	int delete(Integer id);
	Parameter findById(Integer id);
	int createParameter(Parameter parameter)throws Exception ;
	int updateParameter(Parameter parameter);
	Parameter findByHour(Integer hour);
	/**
	 * 查找是否有相同的价格信息(如果小时相等，且项目相等则视为相同)
	 * @param parameter
	 * @return
	 */
	List<Parameter> findSame(Parameter parameter);
	/**
	 * APP端获取价格列表
	 * @param pageNum
	 * @param pageSize
	 * @param parameter
	 * @return
	 */
	Page<Parameter> findListApp(int pageNum,int pageSize,Parameter parameter);
}
