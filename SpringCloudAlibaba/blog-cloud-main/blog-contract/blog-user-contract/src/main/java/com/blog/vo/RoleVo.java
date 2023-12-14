package com.blog.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @path：com.blog.vo.RoleVo.java
 * @className：RoleVo.java
 * @description：角色实体
 * @author：tanyp
 * @dateTime：2020/11/19 13:52 
 * @editNote：
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class RoleVo implements Serializable {

    private Integer id;
    private String name;
    private String value;
    private String tips;
    private Date createTime;
    private Date updateTime;
    private Integer status;

}
