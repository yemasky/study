package com.mcy.order.service.impl;

import com.mcy.order.entity.TOrder;
import com.mcy.order.mapper.TOrderMapper;
import com.mcy.order.service.ITOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

    @Override
    public int delByOrderId(String orderId) {
        return baseMapper.delByOrderId(orderId);
    }
}
