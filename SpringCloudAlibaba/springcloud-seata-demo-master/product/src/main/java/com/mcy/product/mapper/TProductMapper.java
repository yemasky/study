package com.mcy.product.mapper;

import com.mcy.product.entity.TProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
public interface TProductMapper extends BaseMapper<TProduct> {

    int deduct(String productId,int count);
}
