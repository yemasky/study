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
 * @path：com.blog.domain.Role.java
 * @className：Role.java
 * @description：角色实体
 * @author：tanyp
 * @dateTime：2020/11/19 15:16 
 * @editNote：
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role")
public class Role extends BaseEntiy implements Serializable {

    private Integer id;

    private String name;

    private String value;

    private String tips;

    private Date createTime;

    private Date updateTime;

    private Integer status;
}
