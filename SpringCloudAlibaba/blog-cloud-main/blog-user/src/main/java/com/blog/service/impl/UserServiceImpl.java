package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.User;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.blog.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @path：com.blog.service.impl.UserServiceImpl.java
 * @className：UserServiceImpl.java
 * @description：用户信息
 * @author：tanyp
 * @dateTime：2020/10/29 10:41 
 * @editNote：
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVo findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

}
