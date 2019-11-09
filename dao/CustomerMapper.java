package com.zhihui.dontStarve.dao;

import org.apache.ibatis.annotations.Mapper;

import com.zhihui.dontStarve.bean.Customer;
@Mapper
public interface CustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Integer id);
    Customer selectByOpenId(String id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);
}