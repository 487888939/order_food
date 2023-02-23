package com.zh.order_food.Controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zh.order_food.Common.R;
import com.zh.order_food.Entity.Category;
import com.zh.order_food.Service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page pageInfo = categoryService.pageInfo(page, pageSize);
        return  R.success(pageInfo);

    }
    //新增菜品分类
    @PostMapping
    public R<String> saveCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功");
    }
    //修改分类
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    //删除分类
    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.removeById(ids);
        return R.success("删除成功");
    }
    //获取菜品分类信息
    @GetMapping("/list")
    public R<List<Category>> getCategory(int type){
        LambdaUpdateWrapper<Category> categoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        categoryLambdaUpdateWrapper.eq(Category::getType,type);
        List<Category> categoryList = categoryService.list(categoryLambdaUpdateWrapper);
        return R.success(categoryList);
    }
}
