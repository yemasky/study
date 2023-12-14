package demo2.service.impl;

import demo2.core.constants.Constants;
import demo2.core.constants.JWTConstants;
import demo2.core.result.Wrapper;
import demo2.domain.AuthUser;
import demo2.provider.UserProvider;
import demo2.vo.MenuVo;
import demo2.vo.RoleVo;
import demo2.vo.UserVo;
//import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    //@DubboReference
    @Autowired
    private UserProvider userProvider;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(" 开始验证用户 :"+username);
        //取得用户信息
        Wrapper<UserVo> userInfo = userProvider.findByUsername(username);
        if (userInfo.getCode() != Constants.SUCCESS) {
            throw new UsernameNotFoundException("用户:" + username + ",不存在!");
        }
        System.out.println(" userInfo :"+userInfo);
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userInfo.getResult(), userVo);
        //取得role 权限
        Wrapper<List<RoleVo>> roleInfo = userProvider.getRoleByUserId(String.valueOf(userVo.getId()));
        System.out.println(" roleInfo :"+roleInfo);
        if (roleInfo.getCode() == Constants.SUCCESS) {
            List<RoleVo> roleVoList = roleInfo.getResult();
            for (RoleVo role : roleVoList) {
                // 角色必须是ROLE_开头，可以在数据库中设置
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(JWTConstants.ROLE_PREFIX + role.getValue());
                grantedAuthorities.add(grantedAuthority);

                // 获取权限
                Wrapper<List<MenuVo>> menuInfo = userProvider.getRolePermission(String.valueOf(role.getId()));
                System.out.println(" menuInfo :"+menuInfo);
                if (menuInfo.getCode() == Constants.SUCCESS) {
                    List<MenuVo> permissionList = menuInfo.getResult();
                    for (MenuVo menu : permissionList) {
                        if (!StringUtils.hasText(menu.getUrl())) {
                            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(menu.getUrl());
                            grantedAuthorities.add(authority);
                        }
                    }
                }
            }
        }

        AuthUser user = new AuthUser(userVo.getUsername(), userVo.getPassword(), grantedAuthorities);
        user.setId(userVo.getId());
        System.out.println("开始验证用户:"+user.toString());
        return user;
    }
}
