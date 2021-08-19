package com.wanmi.sbc.marketing.grouponactivity.service;

import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>拼团活动信息表业务逻辑</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@Service
public class GrouponActivityConsumerService {

    @Autowired
    private GrouponActivityService grouponActivityService;

    /**
     * 根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数）
     *
     * @param grouponActivityId
     * @param grouponNum
     * @param grouponOrderStatus
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateStatisticsNumByGrouponActivityId(String grouponActivityId, Integer grouponNum, GrouponOrderStatus grouponOrderStatus) {
        return grouponActivityService.updateStatisticsNumByGrouponActivityId(grouponActivityId,grouponNum,grouponOrderStatus);
    }
}
