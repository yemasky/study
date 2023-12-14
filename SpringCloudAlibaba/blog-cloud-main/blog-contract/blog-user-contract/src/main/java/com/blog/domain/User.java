package com.blog.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.blog.common.core.base.BaseEntiy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @path：com.blog.domain.User.java
 * @className：User.java
 * @description：用户实体
 * @author：tanyp
 * @dateTime：2020/4/18 13:21
 * @editNote：
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class User extends BaseEntiy implements Serializable {

    private Integer id;

    private String avatar;

    private String username;

    private String password;

    private String salt;

    private String name;

    private Date birthday;

    private Integer sex;

    private String email;

    private String phone;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
