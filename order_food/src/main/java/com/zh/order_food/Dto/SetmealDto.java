package com.zh.order_food.Dto;

import com.zh.order_food.Entity.Setmeal;
import com.zh.order_food.Entity.SetmealDish;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.*;

@Data
public class SetmealDto extends Setmeal{
    private String categoryName;
    private List<SetmealDish> setmealDishes;
}
