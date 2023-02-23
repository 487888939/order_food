package com.zh.order_food.Dto;

import com.zh.order_food.Entity.OrderDetail;
import com.zh.order_food.Entity.Orders;
import lombok.Data;

import java.util.*;

@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetaill;
}
