package com.wanmi.sbc.job.service;

import com.wanmi.sbc.job.model.entity.DistributionTaskTemp;
import com.wanmi.sbc.job.repository.DistributionTaskTempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DistributionTaskTempService {

    @Autowired
    DistributionTaskTempRepository distributionTaskTempRepository;

    /**
     * 定时任务启动，搜索数据
     *
     * @return
     */
    public List<DistributionTaskTemp> queryData(int size) {
        Pageable pageable =  PageRequest.of(0, size);
        Page<DistributionTaskTemp> dataPage = distributionTaskTempRepository.queryForTask(pageable);
        List<DistributionTaskTemp> list = dataPage.getContent();
        return list;
    }

    /**
     * 增加退单数量
     *
     * @return
     */
    @Transactional
    public void addReturnOrderNum(String orderId) {
        distributionTaskTempRepository.addReturnOrderNum(orderId);
    }

    /**
     * 减少退单数量
     *
     * @return
     */
    @Transactional
    public void minusReturnOrderNum(String orderId) {
        distributionTaskTempRepository.minusReturnOrderNum(orderId);
    }

    /**
     * 删除数据
     *
     * @return
     */
    @Transactional
    public void deleteByIds(List<String> ids) {
        distributionTaskTempRepository.deleteByIdIn(ids);
    }

    @Transactional
    public void deleteById(String id) {
        distributionTaskTempRepository.deleteById(id);
    }

}
