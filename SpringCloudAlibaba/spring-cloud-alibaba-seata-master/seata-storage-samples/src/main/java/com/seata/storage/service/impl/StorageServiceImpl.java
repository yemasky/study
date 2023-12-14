package com.seata.storage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seata.storage.dao.StorageMapper;
import com.seata.storage.po.Storage;
import com.seata.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author zxx
 * @version 1.0
 * @date 2021/5/28 11:53
 */
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Autowired
    private StorageMapper storageMapper;

    @Override
    public void decrease(Long productId, Integer count) {
        LOGGER.info("------->seata-storage-samples中扣减库存开始-------<");
        storageMapper.decrease(productId, count);
        LOGGER.info("------->seata-storage-samples中扣减库存结束-------<");
    }
}
