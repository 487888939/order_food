package com.zh.order_food.Dto;

import com.zh.order_food.Entity.Category;
import com.zh.order_food.Entity.Dish;
import com.zh.order_food.Entity.DishFlavor;
import lombok.Data;

import java.util.*;
@Data
public class DishDto extends Dish {
    //菜品对应口味信息
    private List<DishFlavor> flavors;
    private String categoryName;
    private Integer copies;
}
