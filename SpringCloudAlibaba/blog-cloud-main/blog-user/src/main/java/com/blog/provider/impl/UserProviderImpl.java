package com.blog.provider.impl;

import com.blog.common.core.exception.BusinessException;
import com.blog.common.core.result.WrapMapper;
import com.blog.common.core.result.Wrapper;
import com.blog.provider.UserProvider;
import com.blog.service.MenuService;
import com.blog.service.RoleService;
import com.blog.service.UserService;
import com.blog.vo.MenuVo;
import com.blog.vo.RoleVo;
import com.blog.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @path：com.blog.provider.impl.UserProviderImpl.java
 * @className：UserProviderImpl.java
 * @description：用户接口
 * @author：tanyp
 * @dateTime：2020/11/9 16:56 
 * @editNote：
 */
@Slf4j
@DubboService
public class UserProviderImpl implements UserProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Override
    public Wrapper<UserVo> findByUsername(String username) {
        try {
            UserVo userVo = userService.findByUsername(username);
            return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, userVo);
        } catch (BusinessException e) {
            return WrapMapper.wrap(Wrapper.ERROR_CODE, e.getMessage());
        } catch (Exception e) {
            return WrapMapper.error();
        }
    }

    @Override
    public Wrapper<List<RoleVo>> getRoleByUserId(String userId) {
        try {
            List<RoleVo> roleVoList = roleService.getRoleByUserId(userId);
            return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, roleVoList);
        } catch (BusinessException e) {
            return WrapMapper.wrap(Wrapper.ERROR_CODE, e.getMessage());
        } catch (Exception e) {
            return WrapMapper.error();
        }
    }

    @Override
    public Wrapper<List<MenuVo>> getRolePermission(String roleId) {
        try {
            List<MenuVo> menuVoList = menuService.getMenuByRoleId(roleId);
            return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, menuVoList);
        } catch (BusinessException e) {
            return WrapMapper.wrap(Wrapper.ERROR_CODE, e.getMessage());
        } catch (Exception e) {
            return WrapMapper.error();
        }
    }
}
