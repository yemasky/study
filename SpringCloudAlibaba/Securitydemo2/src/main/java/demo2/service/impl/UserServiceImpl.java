package demo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo2.domain.User;
import demo2.mapper.UserMapper;
import demo2.service.UserService;
import demo2.vo.UserVo;
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
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVo findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

}
