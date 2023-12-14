package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.Menu;
import com.blog.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @path：com.blog.mapper.MenuMapper.java
 * @className：MenuMapper.java
 * @description：菜单
 * @author：tanyp
 * @dateTime：2020/11/19 15:09 
 * @editNote：
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuVo> getMenuByRoleId(String roleId);

}
