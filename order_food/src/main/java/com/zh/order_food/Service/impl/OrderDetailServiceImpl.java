package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Entity.OrderDetail;
import com.zh.order_food.Mapper.OrderDetailMapper;
import com.zh.order_food.Mapper.OrdersMapper;
import com.zh.order_food.Service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService{
}
