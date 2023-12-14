package demo2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import demo2.domain.User;
import demo2.vo.UserVo;

/**
 * @path：com.blog.service.UserService.java
 * @className：UserService.java
 * @description：用户信息
 * @author：tanyp
 * @dateTime：2020/10/29 10:39 
 * @editNote：
 */
public interface UserService extends IService<User>{

    UserVo findByUsername(String username);
}
