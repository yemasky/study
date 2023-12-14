package com.mcy.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcy.product.entity.TProduct;
import com.mcy.product.mapper.TProductMapper;
import com.mcy.product.service.ITProductService;
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
public class TProductServiceImpl extends ServiceImpl<TProductMapper, TProduct> implements ITProductService {

    @Override
    public boolean deduct(String productId, int count)  {
        QueryWrapper<TProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TProduct::getProductId, productId);
        TProduct product = baseMapper.selectOne(queryWrapper);
        if(product == null){
            throw new RuntimeException("无此产品");
        }
        if(product.getCount()< count){
            throw new RuntimeException("库存不够扣减");
        }
        return baseMapper.deduct(productId,product.getCount()-count) > 0;
    }
}
