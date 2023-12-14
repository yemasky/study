package com.blog.service;

import com.blog.domain.AuthUser;
import com.blog.common.core.constants.Constants;
import com.blog.common.core.constants.JWTConstants;
import com.blog.common.core.result.Wrapper;
import com.blog.provider.UserProvider;
import com.blog.vo.MenuVo;
import com.blog.vo.RoleVo;
import com.blog.vo.UserVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @path：com.blog.service.impl.UserDetailsServiceImpl.java
 * @className：UserDetailsServiceImpl.java
 * @description：自定义用户认证和授权
 * @author：tanyp
 * @dateTime：2020/11/9 15:44 
 * @editNote：
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @DubboReference
    private UserProvider userProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Wrapper<UserVo> userInfo = userProvider.findByUsername(username);
        if (userInfo.getCode() != Constants.SUCCESS) {
            throw new UsernameNotFoundException("用户:" + username + ",不存在!");
        }

        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userInfo.getResult(), userVo);

        Wrapper<List<RoleVo>> roleInfo = userProvider.getRoleByUserId(String.valueOf(userVo.getId()));
        if (roleInfo.getCode() == Constants.SUCCESS) {
            List<RoleVo> roleVoList = roleInfo.getResult();
            for (RoleVo role : roleVoList) {
                // 角色必须是ROLE_开头，可以在数据库中设置
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(JWTConstants.ROLE_PREFIX + role.getValue());
                grantedAuthorities.add(grantedAuthority);

                // 获取权限
                Wrapper<List<MenuVo>> menuInfo = userProvider.getRolePermission(String.valueOf(role.getId()));
                if (menuInfo.getCode() == Constants.SUCCESS) {
                    List<MenuVo> permissionList = menuInfo.getResult();
                    for (MenuVo menu : permissionList) {
                        if (!StringUtils.isEmpty(menu.getUrl())) {
                            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(menu.getUrl());
                            grantedAuthorities.add(authority);
                        }
                    }
                }
            }
        }

        AuthUser user = new AuthUser(userVo.getUsername(), userVo.getPassword(), grantedAuthorities);
        user.setId(userVo.getId());
        return user;
    }

}
