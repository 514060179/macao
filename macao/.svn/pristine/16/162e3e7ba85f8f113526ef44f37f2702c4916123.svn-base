package com.yinghai.macao.common.dao;

import com.yinghai.macao.common.model.Parameter;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.util.Page;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ParameterMapper {
    int deleteByPrimaryKey(Integer spcarDriverId);

    int insert(Parameter record);

    int insertSelective(Parameter record);

    Parameter selectByPrimaryKey(Integer id);
    
    Parameter findByHour(@Param("hour")Integer hour);
    
    int updateByPrimaryKeySelective(Parameter record);

    int updateByPrimaryKeyWithBLOBs(Parameter record);

    int updateByPrimaryKey(Parameter record);

    /**
     * 按条件查询列表
     * @param record
     * @return
     */
    List<Parameter> findListByCondition(Parameter record);
    
    Page<Parameter> findList(Parameter parameter);
    /**
     * 查询相同的价格信息
     * @param parameter
     * @return
     */
	List<Parameter> findSame(Parameter parameter);

	Page<Parameter> findListApp(Parameter parameter);
    
}