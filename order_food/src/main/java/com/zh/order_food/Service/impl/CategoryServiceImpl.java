package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Entity.Category;
import com.zh.order_food.Mapper.CategoryMapper;
import com.zh.order_food.Service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public Page pageInfo(int page, int pageSize) {
        Page<Category> categoryPage = new Page<>(page,pageSize);
        LambdaUpdateWrapper<Category> categoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        categoryLambdaUpdateWrapper.orderByDesc(Category::getCreateTime);
        return this.page(categoryPage,categoryLambdaUpdateWrapper);
    }
}
