package com.zh.order_food.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zh.order_food.Common.BaseContext;
import com.zh.order_food.Common.R;
import com.zh.order_food.Entity.Employee;
import com.zh.order_food.Service.EmployeeService;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee,HttpServletRequest request){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//将明文MD5加密
        Employee emp = employeeService.checkEmployee(employee);
        if (emp==null||emp.getStatus()==0){
            return R.error("用户不存在,或账号被封禁");
        }else if (!(emp.getPassword().equals(password))){
            return R.error("密码错误");
        }
        //密码正确,登录成功,保存id到session
        BaseContext.setCurrentID(emp.getId());
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    //分页展示功能
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page pageInfo = employeeService.pageEmployee(page, pageSize, name);
        return R.success(pageInfo);
    }
    //退出功能
    @PostMapping("/logout")
    public R<String> logOut(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    //新增员工
    @PostMapping
    public R<String> save(@RequestBody Employee employee){
        employeeService.saveEmployee(employee);
        return R.success("新增员工成功");
    }
    //获取员工信息
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        log.info("根据id查询员工信息");
        Employee employee= employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("用户不存在");
    }
    //账户禁用
    @PutMapping
    public R<String> forbid(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("禁用成功");
    }
}
