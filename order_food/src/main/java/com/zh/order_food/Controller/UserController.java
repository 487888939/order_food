package com.zh.order_food.Controller;

import com.zh.order_food.Common.R;
import com.zh.order_food.Entity.User;
import com.zh.order_food.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user){
        userService.sendMsg(user);
        return R.success("验证码发送成功");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,String> map, HttpSession session){
        User loginUser= userService.login(map, session);
       return R.success(loginUser);
    }
}
