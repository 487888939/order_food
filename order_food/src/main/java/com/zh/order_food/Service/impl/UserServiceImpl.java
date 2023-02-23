package com.zh.order_food.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.order_food.Common.CustomException;
import com.zh.order_food.Common.MailUtils;
import com.zh.order_food.Entity.User;
import com.zh.order_food.Mapper.UserMapper;
import com.zh.order_food.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Boolean sendMsg(User user) {
        //获取前端用户传来的邮箱
        String email = user.getEmail();
        if (email!=null){
            //随机生成6位数验证码
            String code = MailUtils.getCode();
            //发送验证码到邮箱
            try {
                MailUtils.sendMail(email,code);
                //将验证码缓存到redis中,并设置有效时间为5分钟
                redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);
                log.info("登录验证码为:{}",code);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    @Override
    public User login(Map<String, String> map, HttpSession session) {
        //首先判断集合中的code是否为空
        String code = map.get("code");
        String email = map.get("email");
        if (code==null||email==null){
            throw new CustomException("验证码不能为空");
        }
        //从redis中获取验证码
        Object trueCode= redisTemplate.opsForValue().get(email);
        //比对验证码
        if (!(trueCode.equals(code))){
            throw new CustomException("验证码不正确");
        }
        //验证码正确,查询用户表是否有该用户
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,email);
        User user = this.getOne(userLambdaQueryWrapper);
        if (user==null){
            //表示当前用户是新用户
            User newUser = new User();
            newUser.setEmail(email);
            //保存该用户
            this.save(newUser);
            user=this.getOne(userLambdaQueryWrapper);
        }
        //将该用户保存到session作用域
        session.setAttribute("user",user.getId());
        return user;
    }
}
