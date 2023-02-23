package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Common.BaseContext;
import com.zh.order_food.Entity.ShoppingCart;
import com.zh.order_food.Mapper.ShoppingCartMapper;
import com.zh.order_food.Service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public  ShoppingCart add(ShoppingCart shoppingCart) {
        //设置用户id,指定当前是那个用户的购物车数据
        Long currentId = BaseContext.getCurrentID();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId!=null){
            //添加到购物车的是菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            //添加到购物车中的是套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        //查询当前菜品或者套餐是否在购物车中
        ShoppingCart cartServiceOne = this.getOne(shoppingCartLambdaQueryWrapper);
        if (cartServiceOne!=null){
            //如果已经存在,就在原来的基础上加1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            this.updateById(cartServiceOne);
        }else{
            //如果不存在,则添加到购物车,数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            cartServiceOne=shoppingCart;
        }
        return cartServiceOne;
    }

    @Override
    public List getShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentID());
        shoppingCartLambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = this.list(shoppingCartLambdaQueryWrapper);
        return list;
    }

    @Override
    public void cleanShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentID());
        this.remove(shoppingCartLambdaQueryWrapper);
    }

    /**
     * 购物车菜品或套餐数量减
     * @param shoppingCart
     * @return
     */
    @Override
    public Boolean sub(ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentID();
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaUpdateWrapper<ShoppingCart> shoppingCartLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (dishId!=null){
            //菜品数量加1操作
            //根据id查询购物车中菜品数量
            shoppingCartLambdaUpdateWrapper.eq(ShoppingCart::getDishId,dishId);
            shoppingCartLambdaUpdateWrapper.eq(ShoppingCart::getUserId,currentId);
        }else if (setmealId!=null){
            //套餐数量加1操作
            shoppingCartLambdaUpdateWrapper.eq(ShoppingCart::getSetmealId,setmealId);
            shoppingCartLambdaUpdateWrapper.eq(ShoppingCart::getUserId,currentId);
        }
        ShoppingCart cart = this.getOne(shoppingCartLambdaUpdateWrapper);
        Integer number = cart.getNumber();
        if (number>1){
            number--;
            cart.setNumber(number);
            return this.updateById(cart);
        }
            //其他情况删除
           return this.removeById(cart);
    }
}
