package com.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @path：com.blog.dto.UserDto.java
 * @className：UserDto.java
 * @description：用户实体
 * @author：tanyp
 * @dateTime：2020/4/18 13:21
 * @editNote：
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class UserDto implements Serializable {

    private Long id;

    private String username;

    private String password;

    private Integer status;

    private String clientId;

    private List<Integer> roles;

}