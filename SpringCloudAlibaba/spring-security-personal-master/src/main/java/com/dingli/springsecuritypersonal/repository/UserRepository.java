package com.dingli.springsecuritypersonal.repository;

import com.dingli.springsecuritypersonal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByLogin(String login);
}
