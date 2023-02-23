package com.zh.order_food.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.order_food.Entity.AddressBook;
import org.springframework.context.annotation.ImportSelector;

import java.util.List;

public interface AddressService extends IService<AddressBook> {
    Boolean saveAddress(AddressBook addressBook);
    Boolean setDefault(AddressBook addressBook);
    List<AddressBook> getAllAddress();
    AddressBook editAddressById(Long id);
    void updateAddress(AddressBook addressBook);
    AddressBook getDefaultAddress();
}
