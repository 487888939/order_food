package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Dto.SetmealDto;
import com.zh.order_food.Entity.Category;
import com.zh.order_food.Entity.Setmeal;
import com.zh.order_food.Entity.SetmealDish;
import com.zh.order_food.Mapper.SetmealMapper;
import com.zh.order_food.Service.CategoryService;
import com.zh.order_food.Service.SetmealDishService;
import com.zh.order_food.Service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;
     @Override
    public Page<SetmealDto> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name)
                .orderByDesc(Setmeal::getCreateTime);
         Page<Setmeal> pageInfo = this.page(setmealPage, setmealLambdaQueryWrapper);
         //对象拷贝
         BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
         List<SetmealDto> recordDto= setmealPage.getRecords().stream().map(item -> {
             SetmealDto setmealDto = new SetmealDto();
             //对象拷贝
             BeanUtils.copyProperties(item, setmealDto);
             Category category = categoryService.getById(item.getCategoryId());
             if (category != null) {
                 setmealDto.setCategoryName(category.getName());
             }
             return setmealDto;
         }).collect(Collectors.toList());
         setmealDtoPage.setRecords(recordDto);
         return setmealDtoPage;
    }
    @Override
    @Transactional
    public void SaveSetmeal(SetmealDto setmealDto) {
        this.save(setmealDto);
        for (SetmealDish setmealDish:setmealDto.getSetmealDishes()){
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }
    @Override
    public SetmealDto getSetmealDto(Long id) {
        Setmeal setmeal= this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
         return setmealDto;
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDto setmealDto) {
        //首先删除套餐相关的所有菜品数据
        LambdaUpdateWrapper<SetmealDish> setmealDishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setmealDishLambdaUpdateWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaUpdateWrapper);
        //保存当前传过来的套餐菜品数据
        List<SetmealDish> collect = setmealDto.getSetmealDishes().stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(collect);
        //保存套餐数据
        this.updateById(setmealDto);
    }
}
