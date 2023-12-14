package zoua.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import zoua.entity.User;

@Mapper
public interface IUserMapper {
	 
    @Select("select * from user where id = #{id}")
    User findUserById(Long id);
 
    @Select("select * from user")
    List<User> findAllUsers();
 
    @Insert("INSERT INTO user(username, name, age, balance) VALUES(#{username}, #{name}, #{age}, #{balance})")
    int insertUser(User user);
}
