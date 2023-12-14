package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.Menu;
import com.blog.mapper.MenuMapper;
import com.blog.service.MenuService;
import com.blog.vo.MenuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @path：com.blog.service.impl.MenuServiceImpl.java
 * @className：MenuServiceImpl.java
 * @description：菜单
 * @author：tanyp
 * @dateTime：2020/11/19 15:31 
 * @editNote：
 */
@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuVo> getMenuByRoleId(String roleId) {
        return menuMapper.getMenuByRoleId(roleId);
    }

}
