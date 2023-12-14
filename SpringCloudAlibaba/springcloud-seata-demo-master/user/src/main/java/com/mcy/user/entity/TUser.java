package com.mcy.user.entity;

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
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String username;

    private String password;

    private BigDecimal balance;

    private Integer age;

    private String sex;

    private String address;


}
