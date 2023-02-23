package com.zh.order_food.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.order_food.Entity.User;
import org.apache.tomcat.websocket.WsRemoteEndpointAsync;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User>{
    Boolean sendMsg(User user);
    User login(Map<String,String> map, HttpSession session);
}
