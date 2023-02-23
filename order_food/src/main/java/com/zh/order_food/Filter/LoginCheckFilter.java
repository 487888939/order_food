package com.zh.order_food.Filter;

import com.alibaba.fastjson.JSON;
import com.zh.order_food.Common.BaseContext;
import com.zh.order_food.Common.R;
import com.zh.order_food.Entity.User;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;
import java.util.logging.LogRecord;

/**
 * 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        //获取本次请求的url
        String requestURI = request.getRequestURI();
        //配置不需要过滤的请求
        String[] urls=new String[]{
            "/employee/login",
            "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg"
        };
        //判断本次请求是否需要处理
        Boolean check = check(urls, requestURI);
        //如果不需要处理直接放行
        if (check){
            filterChain.doFilter(request,response);
            return;
        }
        HttpSession session = request.getSession();
        //判断用户登录状态,如果已登陆,则直接放行

        if (session.getAttribute("employee")!=null){
            Long empId =(Long) session.getAttribute("employee");
            BaseContext.setCurrentID(empId);
            log.info("后台用户已登录,用户id:{}",empId);
            filterChain.doFilter(request,response);
            return;
        }
        //用户未登陆
        if (session.getAttribute("user")!=null){
            Long userID =(Long) session.getAttribute("user");
            BaseContext.setCurrentID(userID);
            log.info("前台用户已登录,用户id:{}",userID);
            return;
        }
        if (session.getAttribute("employee")==null||session.getAttribute("user")==null){
            //5.未登录
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        }
    }

    @Override
    public void destroy() {

    }
    public Boolean check(String[] urls,String requestURI){
        for (String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
