package com.blog.provider;

import com.blog.common.core.result.Wrapper;
import com.blog.vo.MenuVo;
import com.blog.vo.RoleVo;
import com.blog.vo.UserVo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @path：com.blog.provider.UserProvider.java
 * @className：UserProvider.java
 * @description：用户接口
 * @author：tanyp
 * @dateTime：2020/11/9 16:48 
 * @editNote：
 */
public interface UserProvider {

    Wrapper<UserVo> findByUsername(@PathVariable("username") String username);

    Wrapper<List<RoleVo>> getRoleByUserId(@PathVariable("userId") String userId);

    Wrapper<List<MenuVo>> getRolePermission(@PathVariable("roleId") String roleId);
}
