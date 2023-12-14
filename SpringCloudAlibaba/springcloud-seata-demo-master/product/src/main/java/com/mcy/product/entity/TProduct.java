package com.mcy.product.entity;

import java.math.BigDecimal;
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
public class TProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productId;

    private String name;

    private String des;

    private BigDecimal price;

    private Integer count;


}
