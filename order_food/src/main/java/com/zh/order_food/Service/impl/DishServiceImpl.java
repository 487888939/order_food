package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Dto.DishDto;
import com.zh.order_food.Entity.Category;
import com.zh.order_food.Entity.Dish;
import com.zh.order_food.Entity.DishFlavor;
import com.zh.order_food.Mapper.DishMapper;
import com.zh.order_food.Service.CategoryService;
import com.zh.order_food.Service.DishFlavorService;
import com.zh.order_food.Service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public Page<DishDto> pageInfo(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishLambdaUpdateWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name)
                .orderByDesc(Dish::getCreateTime);
        Page<Dish> pageInfo = this.page(dishPage, dishLambdaUpdateWrapper);
        //复制对象,忽略records信息
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto>  dishDtoList= records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return dishDtoPage;
    }

    @Override
    @Transactional
    public void saveDishAndFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavorList= dishDto.getFlavors();
        flavorList= flavorList.stream().map(item -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavorList);
    }

    @Override
    @Transactional
    public Boolean deleteDish(List<Long> dishId) {
        //查询当前菜品的售卖状态
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        for (Long id:dishId){
            Dish dish = this.getById(id);
            //当前菜品为禁售状态可以删除
            if (dish.getStatus()!=0){
                //首先删除口味数据
                dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
                dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
                //删除菜品数据
                this.removeById(id);
                return true;
            }
        }
        return false;
    }
    @Override
    public DishDto getDishById(Long id) {
        Dish dish= this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaUpdateWrapper<DishFlavor> dishFlavorLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishFlavorLambdaUpdateWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaUpdateWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateDish(DishDto dishDto) {
        //跟新菜品表
        this.updateById(dishDto);
        Long id = dishDto.getId();
        //跟新口味数
        LambdaUpdateWrapper<DishFlavor> dishFlavorLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishFlavorLambdaUpdateWrapper.eq(DishFlavor::getDishId,id);
        //删除当前菜品的所有口味数据
        dishFlavorService.remove(dishFlavorLambdaUpdateWrapper);
        List<DishFlavor> flavorList= dishDto.getFlavors().stream().map(item -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        //保存重新设置好的flavor
        dishFlavorService.saveBatch(flavorList);
    }

    @Override
    public List<DishDto> getDishByCategory(Dish dish) {
        List<DishDto> dishDtoList=null;
        //构造查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = this.list(dishLambdaQueryWrapper);
        dishDtoList=list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName= category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return dishDtoList;
    }
}
