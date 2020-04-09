package com.bread.springmvc.example.controller;


import annotation.Autowrited;
import annotation.Controller;
import annotation.RequestMapping;
import com.bread.springmvc.example.service.UserService;

/**
 *
 * @Description:
 * @author zhiwj
 * @date 2019-10-18 15:08
 */
@Controller("userController")
@RequestMapping("/user")
public class UserController {

    @Autowrited("userService")
    private UserService userService;

    @RequestMapping("/getUser")
    public String getUser(String userId) {
        return userService.getUser(userId);
    }
}
