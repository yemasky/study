package com.mcy.user.mapper;

import com.mcy.user.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
public interface TUserMapper extends BaseMapper<TUser> {

    int debit(String userId, BigDecimal balance);

}
