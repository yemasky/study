package com.mcy.product.service;

import com.mcy.product.entity.TProduct;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
public interface ITProductService extends IService<TProduct> {

    boolean deduct(String productId,int count) ;

}
