package com.seata.storage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seata.storage.po.Storage;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageMapper extends BaseMapper<Storage> {
    void decrease(Long productId, Integer count);
}
