package com.mcy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcy.user.entity.TUser;
import com.mcy.user.mapper.TUserMapper;
import com.mcy.user.service.ITUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Wrapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

    @Override
    public boolean debit(String userId, BigDecimal amount) {
        QueryWrapper<TUser> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.lambda().eq(TUser::getUserId, userId);

            TUser user = baseMapper.selectOne(queryWrapper);
            if(user != null) {
                if (user.getBalance().compareTo(amount) == -1) {
                    throw new RuntimeException("余额不够付款");
                }

                return baseMapper.debit(userId, user.getBalance().subtract(amount)) > 0;
            }
            return false;
        }
        return false;
    }
}
