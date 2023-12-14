package com.mcy.user.service;

import com.mcy.user.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
public interface ITUserService extends IService<TUser> {

    boolean debit(String userId, BigDecimal amount);

}
