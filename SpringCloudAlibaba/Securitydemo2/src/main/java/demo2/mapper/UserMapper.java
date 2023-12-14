package demo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo2.domain.User;
import demo2.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT id,birthday,salt,sex,update_time,avatar,password,create_time,phone,name,email,status,username" +
            " FROM sys_user WHERE username = #{username}")
    UserVo findByUsername(String username);

}
