package demo2.provider;

import demo2.core.result.Wrapper;
import demo2.vo.MenuVo;
import demo2.vo.RoleVo;
import demo2.vo.UserVo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserProvider {
    Wrapper<UserVo> findByUsername(@PathVariable("username") String username);

    Wrapper<List<RoleVo>> getRoleByUserId(@PathVariable("userId") String userId);

    Wrapper<List<MenuVo>> getRolePermission(@PathVariable("roleId") String roleId);
}
