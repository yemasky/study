package com.mcy.user.controller;


import com.mcy.user.entity.TUser;
import com.mcy.user.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

//import javax.xml.ws.Action;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
@RestController
@RequestMapping("user")
public class TUserController {

    @Autowired
    private ITUserService userService;

    @PostMapping("create")
    public String create(String name,String password,int age,String sex,String address){
        TUser user = new TUser();
        user.setUserId(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
        user.setUsername(name);
        user.setPassword(password);
        user.setAge(age);
        user.setSex(sex);
        user.setAddress(address);
        userService.save(user);
        return "success";
    }

    /**
     *  扣款
     * @param userId
     * @param money
     */
    @PostMapping("debit")
    public String debit(String userId, BigDecimal money)  {
        userService.debit(userId,money);
        return "success";
    }


}
