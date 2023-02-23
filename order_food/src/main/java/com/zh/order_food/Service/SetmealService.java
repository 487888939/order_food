package com.zh.order_food.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.order_food.Dto.SetmealDto;
import com.zh.order_food.Entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    Page<SetmealDto> page(int page, int pageSize, String name);
    void SaveSetmeal(SetmealDto setmealDto);
    SetmealDto getSetmealDto(Long id);
    void updateSetmeal(SetmealDto setmealDto);

}
