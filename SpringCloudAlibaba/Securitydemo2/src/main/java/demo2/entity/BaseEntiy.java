package demo2.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

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