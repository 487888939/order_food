package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Entity.DishFlavor;
import com.zh.order_food.Mapper.DishFlavorMapper;
import com.zh.order_food.Service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
