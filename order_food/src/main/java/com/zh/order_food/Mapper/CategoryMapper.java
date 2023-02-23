package com.zh.order_food.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zh.order_food.Entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
