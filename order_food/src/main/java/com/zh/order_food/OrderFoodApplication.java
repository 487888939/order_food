package com.zh.order_food;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
@ServletComponentScan//扫描Servlet组件
@EnableTransactionManagement//开启事务
public class OrderFoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderFoodApplication.class, args);
        System.out.println("项目启动成功...");
    }

}
