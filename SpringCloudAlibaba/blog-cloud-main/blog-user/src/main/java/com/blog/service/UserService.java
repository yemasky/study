package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.User;
import com.blog.vo.UserVo;

/**
 * @path：com.blog.service.UserService.java
 * @className：UserService.java
 * @description：用户信息
 * @author：tanyp
 * @dateTime：2020/10/29 10:39 
 * @editNote：
 */
public interface UserService extends IService<User> {

    UserVo findByUsername(String username);
}
