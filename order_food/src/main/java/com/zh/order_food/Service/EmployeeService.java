package com.zh.order_food.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.order_food.Entity.Employee;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface EmployeeService extends IService<Employee> {
    Employee checkEmployee(Employee employee);
    Page pageEmployee(int page,int pageSize,String name);
    void saveEmployee(Employee employee);
}
