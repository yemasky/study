package com.mcy.order.service;

import com.mcy.order.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
public interface ITOrderService extends IService<TOrder> {

    public int delByOrderId(String orderId);
}
