package demo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo2.domain.Menu;
import demo2.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper  extends BaseMapper<Menu> {
    @Select("select m.id,m.code,m.p_code,m.p_id,m.name,m.url,m.is_menu,m.level,m.sort,m.status,m.icon,m.create_time,m.update_time " +
            "from sys_menu m inner join sys_privilege p on m.id = p.menu_id where p.role_id = #{roleId}")
    List<MenuVo> getMenuByRoleId(String roleId);

}