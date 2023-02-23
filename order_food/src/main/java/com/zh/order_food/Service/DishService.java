package com.zh.order_food.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.order_food.Dto.DishDto;
import com.zh.order_food.Entity.Dish;

import java.util.*;

public interface DishService extends IService<Dish> {
    Page<DishDto> pageInfo(int page, int pageSize, String name);
    void saveDishAndFlavor(DishDto dishDto);
    Boolean deleteDish(List<Long> dishId);
    DishDto getDishById(Long id);
    void updateDish(DishDto dishDto);
    List<DishDto> getDishByCategory(Dish dish);
}
