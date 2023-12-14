package demo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo2.domain.Role;
import demo2.mapper.RoleMapper;
import demo2.service.RoleService;
import demo2.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @path：com.blog.service.impl.RoleServiceImpl.java
 * @className：RoleServiceImpl.java
 * @description：角色
 * @author：tanyp
 * @dateTime：2020/11/19 15:28 
 * @editNote：
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleVo> getRoleByUserId(String userId) {
        return roleMapper.getRoleByUserId(userId);
    }

}
