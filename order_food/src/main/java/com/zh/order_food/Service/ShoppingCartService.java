package com.zh.order_food.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.order_food.Entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart>{
    ShoppingCart add(ShoppingCart shoppingCart);
    List getShoppingCart();
    void cleanShoppingCart();
    Boolean sub(ShoppingCart shoppingCart);
}
