package com.zh.order_food.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zh.order_food.Entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
