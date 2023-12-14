package zoua.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import zoua.entity.User;
import zoua.mapper.IUserMapper;

public class UserDao {
	@Autowired
	IUserMapper iUserMapper;

	public User findUserById(Long id) {
		return iUserMapper.findUserById(id);
	}

	public List<User> findAllUsers() {
		return iUserMapper.findAllUsers();
	}

	public int insertUser(User user) {
		return iUserMapper.insertUser(user);
	}

}
