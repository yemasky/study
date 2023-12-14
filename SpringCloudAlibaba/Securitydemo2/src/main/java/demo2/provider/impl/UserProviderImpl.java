package demo2.provider.impl;

import demo2.core.result.WrapMapper;
import demo2.core.result.Wrapper;
import demo2.exception.BusinessException;
import demo2.provider.UserProvider;
import demo2.service.MenuService;
import demo2.service.RoleService;
import demo2.service.UserService;
import demo2.vo.MenuVo;
import demo2.vo.RoleVo;
import demo2.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @path：com.blog.provider.impl.UserProviderImpl.java
 * @className：UserProviderImpl.java
 * @description：用户接口
 * @author：tanyp
 * @dateTime：2020/11/9 16:56
 * @editNote：
 */
//@DubboService
@Service
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
