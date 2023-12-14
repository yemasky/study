package com.seata.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seata.storage.po.Storage;

public interface StorageService extends IService<Storage> {

    void decrease(Long productId, Integer count);
}
