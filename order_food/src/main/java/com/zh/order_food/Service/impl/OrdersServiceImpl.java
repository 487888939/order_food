package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Dto.OrdersDto;
import com.zh.order_food.Entity.OrderDetail;
import com.zh.order_food.Entity.Orders;
import com.zh.order_food.Mapper.OrdersMapper;
import com.zh.order_food.Service.OrderDetailService;
import com.zh.order_food.Service.OrdersService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper,Orders> implements OrdersService{
    @Autowired
    private OrderDetailService orderDetailService;
    @Override
    public Page<OrdersDto> pageInfo(Integer page, Integer pageSize, String number, String beginTime, String endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number)
                .gt(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,beginTime)
                .lt(StringUtils.isNotEmpty(endTime),Orders::getOrderTime,endTime);
        Page<Orders> pageInfo = this.page(ordersPage, ordersLambdaQueryWrapper);
        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");
        List<OrdersDto> ordersDtosRecords= pageInfo.getRecords().stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getNumber());
            List<OrderDetail> list = orderDetailService.list(orderDetailLambdaQueryWrapper);
            ordersDto.setOrderDetaill(list);
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtosRecords);
        return ordersDtoPage;
    }
}
