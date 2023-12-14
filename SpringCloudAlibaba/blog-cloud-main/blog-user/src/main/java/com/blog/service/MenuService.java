package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.Menu;
import com.blog.vo.MenuVo;

import java.util.List;

/**
 * @path：com.blog.service.MenuService.java
 * @className：MenuService.java
 * @description：菜单service
 * @author：tanyp
 * @dateTime：2020/11/19 15:22 
 * @editNote：
 */
public interface MenuService extends IService<Menu> {

    List<MenuVo> getMenuByRoleId(String roleId);

}
