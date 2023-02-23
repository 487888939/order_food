package com.zh.order_food.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.order_food.Dto.OrdersDto;
import com.zh.order_food.Entity.Orders;

public interface OrdersService extends IService<Orders> {
    Page<OrdersDto> pageInfo(Integer page,Integer pageSize,String number,String beginTime,String endTime);
}
