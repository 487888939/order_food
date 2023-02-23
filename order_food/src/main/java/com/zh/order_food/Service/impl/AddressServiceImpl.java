package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Common.BaseContext;
import com.zh.order_food.Entity.AddressBook;
import com.zh.order_food.Mapper.AddressMapper;
import com.zh.order_food.Service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressBook> implements AddressService {
    /**
     * 新增
     * @param addressBook
     * @return
     */
    @Override
    public Boolean saveAddress(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentID());
        log.info("userID为:{}",addressBook.getUserId());
        log.info("当前登录用户为:{}",BaseContext.getCurrentID());
        return this.save(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @Override
    @Transactional
    public Boolean setDefault(AddressBook addressBook) {
        //将该用户所有默认地址置为0
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentID());
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault,0);
        this.update(addressBookLambdaUpdateWrapper);
        addressBook.setIsDefault(1);
        return this.updateById(addressBook);
    }
    /**
     * 获取用户的所有地址
     * @param
     * @return
     */
    @Override
    public List<AddressBook> getAllAddress() {
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentID());
        addressBookLambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = this.list(addressBookLambdaQueryWrapper);
        return list;
    }

    /**
     * 通过id获取地址
     * @param id
     * @return
     */
    @Override
    public AddressBook editAddressById(Long id) {
        return this.getById(id);
    }

    /**
     * 更新地址
     * @param addressBook
     * @return
     */

    @Override
    public void updateAddress(AddressBook addressBook) {
        this.updateById(addressBook);
    }

    /**
     * 获取默认地址
     * @return
     */
    @Override
    public AddressBook getDefaultAddress(){
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentID());
        addressBookLambdaUpdateWrapper.eq(AddressBook::getIsDefault,1);
        return this.getOne(addressBookLambdaUpdateWrapper);
    }
}
