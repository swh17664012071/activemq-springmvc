package com.etoak;

import com.etoak.bean.User;
import com.etoak.config.RootConfig;
import com.etoak.service.UserService;
import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.jms.JMSException;

public class Test {

    public static void main(String[] args) throws JMSException {
        ApplicationContext ioc = new
                AnnotationConfigApplicationContext(RootConfig.class);

        UserService userService = ioc.getBean(UserService.class);
        User user = new User();
        user.setName("wangwu3xxxx");
        user.setEmail("wangwu@et.com");
        user.setAge(20);

        int result = userService.addUser(user);
        System.out.println(result == 1 ? "添加成功": "添加失败");
    }
}
