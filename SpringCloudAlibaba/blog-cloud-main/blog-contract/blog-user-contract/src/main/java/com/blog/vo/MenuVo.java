package com.blog.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @path：com.blog.vo.MenuVo.java
 * @className：MenuVo.java
 * @description：菜单实体
 * @author：tanyp
 * @dateTime：2020/11/19 13:53 
 * @editNote：
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class MenuVo implements Serializable {

    private Integer id;
    private String code;
    private String pCode;
    private String pId;
    private String name;
    private String url;
    private Integer isMenu;
    private Integer level;
    private Integer sort;
    private Integer status;
    private String icon;
    private Date createTime;
    private Date updateTime;

}
