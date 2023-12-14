package com.blog.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.core.result.WrapMapper;
import com.blog.common.core.result.Wrapper;
import com.blog.domain.User;
import com.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @path：com.blog.controller.UserController.java
 * @className：UserController.java
 * @description: 用户信息
 * @author：tanyp
 * @dateTime：2020/10/27 14:55 
 * @editNote：
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("userList")
    public Wrapper<IPage<User>> userList(@RequestBody User user) {
        log.info("find start user:{}", JSON.toJSON(user));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(user.getKeyword()), "username", user.getKeyword());
        IPage<User> page = userService.page(new Page<>(user.getPageNum(), user.getPageSize()), queryWrapper);
        log.info("find end result:{}", JSON.toJSON(page));
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, page);
    }
}
