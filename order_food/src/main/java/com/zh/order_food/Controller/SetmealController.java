package com.zh.order_food.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zh.order_food.Common.R;
import com.zh.order_food.Dto.SetmealDto;
import com.zh.order_food.Entity.Setmeal;
import com.zh.order_food.Service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        return R.success(setmealService.page(page,pageSize,name));
    }

    /**
     * 套餐删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam List<Long> ids){
        for (Long id:ids){
            Integer status = setmealService.getById(id).getStatus();
            if (status==1){
                return R.error("启售状态套餐无法删除");
            }
            setmealService.removeById(id);
        }
        return R.success("删除成功");
    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        for (Long id:ids){
            setmealLambdaUpdateWrapper.eq(Setmeal::getId,id)
                    .set(Setmeal::getStatus,status);
            setmealService.update(setmealLambdaUpdateWrapper);
        }
       return R.success("修改状态成功");
    }
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.SaveSetmeal(setmealDto);
        return R.success("套餐保存成功");
    }
    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmeal(setmealDto);
        return R.success("套餐修改成功");
    }
    @GetMapping("{id}")
    public R<Setmeal> getSetmeal(@PathVariable("id") Long id){
        SetmealDto setmealDto = setmealService.getSetmealDto(id);
        return R.success(setmealDto);
    }
}
