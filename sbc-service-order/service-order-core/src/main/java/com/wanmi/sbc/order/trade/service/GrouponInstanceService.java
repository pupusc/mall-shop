package com.wanmi.sbc.order.trade.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceVO;
import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import com.wanmi.sbc.order.trade.repository.GrouponInstanceRepository;
import com.wanmi.sbc.order.trade.request.GrouponInstanceQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;

import java.util.ArrayList;

/**
 * 拼团订单实例
 */
@Service
@Slf4j
public class GrouponInstanceService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GrouponInstanceRepository grouponInstanceRepository;
    /**
     * 团实例分页
     *
     * @param whereCriteria 条件
     * @param request       参数
     * @return
     */
    public Page<GrouponInstance> page(Criteria whereCriteria, GrouponInstanceQueryRequest request) {
        long totalSize = this.countNum(whereCriteria, request);
        if (totalSize < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        return new PageImpl<>(mongoTemplate.find(query.with(request.getPageRequest()), GrouponInstance.class), request
                .getPageable(), totalSize);
    }




    /**
     * 统计数量
     *
     * @param whereCriteria
     * @param request
     * @return
     */
    public long countNum(Criteria whereCriteria, GrouponInstanceQueryRequest request) {
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        long totalSize = mongoTemplate.count(query, GrouponInstance.class);
        return totalSize;
    }

    /**
     * 查询团实例
     *
     * @param grouponNo
     */
    public GrouponInstance detailByGrouponNo(String grouponNo) {
        return grouponInstanceRepository.findTopByGrouponNo(grouponNo);
    }

    /**
     * 将实体包装成VO
     *
     * @author groupon
     */
    public GrouponInstanceVO wrapperVo(GrouponInstance grouponInstance) {
        if (grouponInstance != null) {
            GrouponInstanceVO grouponInstanceVO = new GrouponInstanceVO();
            KsBeanUtil.copyPropertiesThird(grouponInstance, grouponInstanceVO);
            return grouponInstanceVO;
        }
        return null;
    }


    public GrouponInstance detailByActivityId(String grouponActivityId){
        return grouponInstanceRepository.findTopByGrouponActivityIdAndGrouponStatusOrderByCreateTimeDesc(grouponActivityId,GrouponOrderStatus.WAIT);
    }
}
