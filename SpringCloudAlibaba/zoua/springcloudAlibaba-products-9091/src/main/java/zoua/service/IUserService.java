package zoua.service;

import java.util.List;

import zoua.entity.User;

public interface IUserService {
	 
    User findUserById(Long id);
 
    List<User> findAllUsers();
 
    int insertUser(User user);
}
