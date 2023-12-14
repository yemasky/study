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
 * @path：com.blog.domain.Menu.java
 * @className：Menu.java
 * @description：菜单实体
 * @author：tanyp
 * @dateTime：2020/11/19 15:19 
 * @editNote：
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_menu")
public class Menu extends BaseEntiy implements Serializable {

    private Integer id;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 菜单父编码
     */
    private String pCode;

    /**
     * 父菜单ID
     */
    private String pId;

    /**
     * 名称
     */
    private String name;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 是否是菜单
     */
    private Integer isMenu;

    /**
     * 菜单层级
     */
    private Integer level;

    /**
     * 菜单排序
     */
    private Integer sort;

    private Integer status;

    private String icon;

    private Date createTime;

    private Date updateTime;

}
