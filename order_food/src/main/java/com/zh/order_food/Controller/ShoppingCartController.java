package com.zh.order_food.Controller;


import com.zh.order_food.Common.R;
import com.zh.order_food.Entity.ShoppingCart;
import com.zh.order_food.Service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> addCart(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据{}",shoppingCart);
        ShoppingCart add = shoppingCartService.add(shoppingCart);
        return R.success(add);
    }
    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        List shoppingCart = shoppingCartService.getShoppingCart();
        return R.success(shoppingCart);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        shoppingCartService.cleanShoppingCart();
        return R.success("清空购物车成功");
    }
    @PostMapping("/sub")
    public R<String> subCart(@RequestBody ShoppingCart shoppingCart){
       if (shoppingCartService.sub(shoppingCart)){
           return R.success("减1操作成功");
       }
        return R.error("减1操作失败");
    }
}
