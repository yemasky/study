package demo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo2.domain.Role;
import demo2.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    @Select("select r.id,r.name,r.value,r.tips,r.create_time,r.update_time,r.status " +
            "from sys_role r inner join sys_user_role ur on r.id=ur.role_id  where ur.user_id = #{userId}")
    List<RoleVo> getRoleByUserId(String userId);

}
