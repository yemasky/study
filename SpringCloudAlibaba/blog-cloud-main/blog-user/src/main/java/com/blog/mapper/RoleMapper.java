package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.Role;
import com.blog.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @path：com.blog.mapper.RoleMapper.java
 * @className：RoleMapper.java
 * @description：角色
 * @author：tanyp
 * @dateTime：2020/11/19 15:09 
 * @editNote：
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<RoleVo> getRoleByUserId(String userId);

}
