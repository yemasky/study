package com.blog.common.core.base;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @path：com.blog.common.core.base.BaseEntiy.java
 * @className：BaseEntiy.java
 * @description：公共实体
 * @author：tanyp
 * @dateTime：2020/10/29 11:22 
 * @editNote：
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntiy implements Serializable {

    /**
     * 修改时间
     */
    @TableField(exist = false)
    private Date updateTime;

    /**
     * 页数
     */
    @TableField(exist = false)
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    @TableField(exist = false)
    private Integer pageSize = 10;

    /**
     * 关键字
     */
    @TableField(exist = false)
    private String keyword;
}
