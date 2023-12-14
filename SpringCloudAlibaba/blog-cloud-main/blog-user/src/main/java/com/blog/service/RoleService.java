package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.Role;
import com.blog.vo.RoleVo;

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
