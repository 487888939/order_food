package com.zh.order_food.Common;

public class BaseContext {
    private final static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static Long getCurrentID(){
return threadLocal.get();
}
    public static void setCurrentID(Long id){
    threadLocal.set(id);
}
}
