package com.dingli.springsecuritypersonal.security;

import com.dingli.springsecuritypersonal.entity.User;
import com.dingli.springsecuritypersonal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		System.out.println(" 开始认证："+login);
         // 1. 查询用户
		User userFromDatabase = userRepository.findOneByLogin(login);
		System.out.println(" 开始认证："+userFromDatabase);
		if (userFromDatabase == null) {
			//log.warn("User: {} not found", login);
		 throw new UsernameNotFoundException("User " + login + " was not found in db");
            //这里找不到必须抛异常
		}

	    // 2. 设置角色
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userFromDatabase.getRole());
		grantedAuthorities.add(grantedAuthority);

		System.out.println(" 匹配密码："+userFromDatabase.getPassword());
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(login,
				userFromDatabase.getPassword(), grantedAuthorities);
		System.out.println(" userDetails："+userDetails);
		return userDetails;
	}

}