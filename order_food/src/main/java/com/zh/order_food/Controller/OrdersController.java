package com.zh.order_food.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zh.order_food.Common.R;
import com.zh.order_food.Dto.OrdersDto;
import com.zh.order_food.Entity.Orders;
import com.zh.order_food.Service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @GetMapping("/page")
    public R<Page<OrdersDto>> page(Integer page,Integer pageSize,String number,String beginTime,String endTime){
        Page<OrdersDto> ordersDtoPage = ordersService.pageInfo(page, pageSize, number, beginTime, endTime);
        return R.success(ordersDtoPage);
    }
}
