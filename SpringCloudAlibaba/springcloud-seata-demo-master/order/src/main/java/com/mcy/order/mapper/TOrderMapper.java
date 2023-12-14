package com.mcy.order.mapper;

import com.mcy.order.entity.TOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
public interface TOrderMapper extends BaseMapper<TOrder> {

    public int delByOrderId(String orderId);

}
