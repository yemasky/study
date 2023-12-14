package demo2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import demo2.domain.Menu;
import demo2.vo.MenuVo;

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
