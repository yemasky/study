package demo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import demo2.domain.Menu;
import demo2.mapper.MenuMapper;
import demo2.service.MenuService;
import demo2.vo.MenuVo;
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

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuVo> getMenuByRoleId(String roleId) {
        return menuMapper.getMenuByRoleId(roleId);
    }

}
