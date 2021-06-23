package com.wanmi.sbc.order.receivables.service;

import com.wanmi.sbc.order.receivables.repository.ReceivableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 收款单服务
 * Created by zhangjin on 2017/3/20.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class ReceivableService {

    /**
     * 收款单数据源
     */
    @Autowired
    private ReceivableRepository receivableRepository;


    @Transactional
    public void deleteReceivables(List<String> payOrderIds) {
        if (CollectionUtils.isEmpty(payOrderIds)) {
            return;
        }
        receivableRepository.deleteReceivables(payOrderIds);
    }
}
