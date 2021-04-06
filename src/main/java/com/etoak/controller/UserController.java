package com.etoak.controller;

import com.etoak.bean.User;
import com.etoak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.JMSException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /** 跳转到注册页面 */
    @RequestMapping("/toReg")
    public String toReg() {
        return "reg";
    }

    /**
     * 注册请求
     */
    @PostMapping("/reg")
    public String reg(User user) throws JMSException {
        userService.addUser(user);
        return "redirect:/user/toReg";
    }

}
