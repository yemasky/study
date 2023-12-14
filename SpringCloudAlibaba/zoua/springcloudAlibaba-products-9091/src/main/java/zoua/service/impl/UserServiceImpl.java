package zoua.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zoua.entity.User;
import zoua.mapper.IUserMapper;
import zoua.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
 
    @Autowired
    IUserMapper iUserMapper;
 
    @Override
    public User findUserById(Long id) {
        return iUserMapper.findUserById(id);
    }
 
    @Override
    public List<User> findAllUsers() {
        return iUserMapper.findAllUsers();
    }
 
    @Override
    public int insertUser(User user) {
        return iUserMapper.insertUser(user);
    }
}
