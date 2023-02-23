package com.zh.order_food.Common;

public class CustomException extends RuntimeException{
    public CustomException(String ex){
        super(ex);//调用父类的构造方法
    }
}
