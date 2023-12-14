package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.Role;
import com.blog.mapper.RoleMapper;
import com.blog.service.RoleService;
import com.blog.vo.RoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @path：com.blog.service.impl.RoleServiceImpl.java
 * @className：RoleServiceImpl.java
 * @description：角色
 * @author：tanyp
 * @dateTime：2020/11/19 15:28 
 * @editNote：
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleVo> getRoleByUserId(String userId) {
        return roleMapper.getRoleByUserId(userId);
    }

}
