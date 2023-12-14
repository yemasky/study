package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.User;
import com.blog.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @path：com.blog.mapper.UserMapper.java
 * @className：UserMapper.java
 * @description：用户信息
 * @author：tanyp
 * @dateTime：2020/10/28 16:46 
 * @editNote：
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    UserVo findByUsername(String username);

}
