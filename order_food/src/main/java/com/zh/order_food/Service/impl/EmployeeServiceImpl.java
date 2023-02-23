package com.zh.order_food.Service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Entity.Employee;
import com.zh.order_food.Mapper.EmployeeMapper;
import com.zh.order_food.Service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{
    @Override
    public Employee checkEmployee(Employee employee) {
        //查询用户是否存在
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = this.getOne(employeeLambdaQueryWrapper);
        return emp;
    }

    @Override
    public Page pageEmployee(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Employee> employeePage = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        employeeLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        employeeLambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        Page<Employee> pageInfo = this.page(employeePage, employeeLambdaQueryWrapper);
        return pageInfo;
    }

    @Override
    public void saveEmployee(Employee employee) {
        //设置初始密码为123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        this.save(employee);
    }
}
