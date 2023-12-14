package demo2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import demo2.domain.Role;
import demo2.vo.RoleVo;

import java.util.List;

/**
 * @path：com.blog.service.RoleService.java
 * @className：RoleService.java
 * @description：角色service
 * @author：tanyp
 * @dateTime：2020/11/19 15:22 
 * @editNote：
 */
public interface RoleService extends IService<Role> {

    List<RoleVo> getRoleByUserId(String userId);

}
