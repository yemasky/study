package com.mcy.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;

    private String productId;

    private String userId;

    private Integer count;

    private String remark;

    private BigDecimal amount;

    private LocalDateTime createTime;


}
