package com.zh.order_food.Controller;

import com.zh.order_food.Common.R;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件下载,文件上传
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${order_food.path}")
    private String basePath;
    @GetMapping("/download")
public void download(String name, HttpServletResponse response) {
    //输入流,通过输入流读取文件内容
    try {
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        //通过输出流将文件写回浏览器,在浏览器展示图片
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/jpeg");
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len=fileInputStream.read(bytes))!= -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }
        //关闭资源
        outputStream.close();
        fileInputStream.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@PostMapping("/upload")
public R<String> upload(MultipartFile file){
    //file是临时文件,需要转存到指定位置,否则本次请求完成后临时文件会被删除
    String originalFilename = file.getOriginalFilename();//获取原始文件名
    //使用UUID重新生成文件名,防止文件名重复造成文件覆盖
    String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//拿到后缀名
    String fileName = UUID.randomUUID().toString()+suffix;
    File dir= new File(basePath);
    //判断当前目录是否存在
    if (!dir.exists()){
        //目录不存在,需要创建
        dir.mkdir();
    }
    try {
        file.transferTo(new File(basePath+fileName));
    } catch (IOException e) {
        e.printStackTrace();
    }
    return R.success(fileName);
}
}
