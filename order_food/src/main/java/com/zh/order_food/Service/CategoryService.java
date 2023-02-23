package com.zh.order_food.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.order_food.Entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface CategoryService extends IService<Category> {
    Page pageInfo(int page,int pageSize);
}
