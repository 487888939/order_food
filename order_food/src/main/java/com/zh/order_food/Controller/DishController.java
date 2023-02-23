package com.zh.order_food.Controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zh.order_food.Common.R;
import com.zh.order_food.Dto.DishDto;
import com.zh.order_food.Dto.SetmealDto;
import com.zh.order_food.Entity.Dish;
import com.zh.order_food.Service.DishService;
import com.zh.order_food.Service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<DishDto> dishDtoPage = dishService.pageInfo(page, pageSize, name);
        return R.success(dishDtoPage);
    }

    /**
     * 新建菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto){
        dishService.saveDishAndFlavor(dishDto);
        return R.success("新建菜品成功");
    }

    /**
     * 删除菜品
     * @param dishId
     * @return
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> dishId){
        Boolean status= dishService.deleteDish(dishId);
        if (status){
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    /**
     * 菜品停售启售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> stopSell(@PathVariable("status") Integer status, @RequestParam List<Long> ids){
        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        for (Long id:ids){
            dishLambdaUpdateWrapper.eq(Dish::getId,id);
            dishLambdaUpdateWrapper.set(Dish::getStatus,status);
            dishService.update(dishLambdaUpdateWrapper);
        }
        return R.success("菜品状态更改成功");
    }

    /**
     * 获取菜品详细数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDishDto(@PathVariable("id") Long id){
        return R.success(dishService.getDishById(id));
    }
    @PutMapping()
    public R<String> updateDish(@RequestBody DishDto dishDto){
        dishService.updateDish(dishDto);
        return R.success("修改成功");
    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishByCategory = dishService.getDishByCategory(dish);
        return R.success(dishByCategory);
    }
}
