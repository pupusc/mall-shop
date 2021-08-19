package com.wanmi.sbc.customer.storestatistics.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.vo.StoreConsumerStatisticsVO;
import com.wanmi.sbc.customer.storestatistics.model.root.StoreConsumerStatistics;
import com.wanmi.sbc.customer.storestatistics.repository.StoreConsumerStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>店铺客户消费统计表业务逻辑</p>
 *
 * @author yang
 * @date 2019-03-13 17:55:08
 */
@Service("StoreConsumerStatisticsService")
public class StoreConsumerStatisticsService {
    @Autowired
    private StoreConsumerStatisticsRepository storeConsumerStatisticsRepository;

    /**
     * 新增店铺客户消费统计表
     *
     * @author yang
     */
    @Transactional
    public StoreConsumerStatistics add(StoreConsumerStatistics entity) {
        storeConsumerStatisticsRepository.save(entity);
        return entity;
    }

    /**
     * 修改店铺客户消费统计表
     */
    @Transactional
    public StoreConsumerStatistics modify(StoreConsumerStatistics entity) {
        storeConsumerStatisticsRepository.save(entity);
        return entity;
    }


    /**
     * 单个查询店铺客户消费统计表
     *
     * @author yang
     */
    public StoreConsumerStatistics getByCustomerIdAndStoreId(String customerId, Long storeId) {
        return storeConsumerStatisticsRepository.findByCustomerIdAndStoreId(customerId, storeId);
    }

    /**
     * 将实体包装成VO
     */
    public StoreConsumerStatisticsVO wrapperVo(StoreConsumerStatistics storeConsumerStatistics) {
        if (storeConsumerStatistics != null){
            StoreConsumerStatisticsVO storeConsumerStatisticsVO=new StoreConsumerStatisticsVO();
            KsBeanUtil.copyPropertiesThird(storeConsumerStatistics,storeConsumerStatisticsVO);
            return storeConsumerStatisticsVO;
        }
        return null;
    }
}
