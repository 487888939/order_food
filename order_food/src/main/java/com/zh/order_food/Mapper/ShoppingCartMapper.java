package com.zh.order_food.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zh.order_food.Common.BaseContext;
import com.zh.order_food.Entity.AddressBook;
import com.zh.order_food.Entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
