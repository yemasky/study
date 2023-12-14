package com.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @path：com.blog.controller.AuthController.java
 * @className：AuthController.java
 * @description:认证中心
 * @author：tanyp
 * @dateTime：2020/10/27 14:55 
 * @editNote：
 */
@Slf4j
@RestController
public class AuthController {

    @RequestMapping("/checkUser")
    public Principal checkUser(Principal user) {
        return user;
    }

}
