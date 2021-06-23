package com.wanmi.sbc.order.growthvalue.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.request.growthvalue.OrderGrowthValueTempQueryRequest;
import com.wanmi.sbc.order.bean.vo.OrderGrowthValueTempVO;
import com.wanmi.sbc.order.growthvalue.model.root.OrderGrowthValueTemp;
import com.wanmi.sbc.order.growthvalue.repository.OrderGrowthValueTempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员权益处理订单成长值 临时表业务逻辑</p>
 */
@Service("OrderGrowthValueTempService")
public class OrderGrowthValueTempService {
    @Autowired
    private OrderGrowthValueTempRepository orderGrowthValueTempRepository;

    /**
     * 新增会员权益处理订单成长值 临时表
     */
    @Transactional
    public void add(OrderGrowthValueTemp entity) {
        orderGrowthValueTempRepository.save(entity);
    }

    /**
     * 单个删除会员权益处理订单成长值 临时表
     */
    @Transactional
    public void deleteById(Long id) {
        orderGrowthValueTempRepository.deleteById(id);
    }

    /**
     * 批量删除会员权益处理订单成长值 临时表
     */
    @Transactional
    public void deleteByIdList(List<Long> ids) {
        orderGrowthValueTempRepository.deleteAll(ids.stream().map(id -> {
            OrderGrowthValueTemp entity = new OrderGrowthValueTemp();
            entity.setId(id);
            return entity;
        }).collect(Collectors.toList()));
    }

    /**
     * 分页查询会员权益处理订单成长值 临时表
     */
    public Page<OrderGrowthValueTemp> page(OrderGrowthValueTempQueryRequest queryReq) {
        return orderGrowthValueTempRepository.findAll(
                OrderGrowthValueTempWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询会员权益处理订单成长值 临时表
     */
    public List<OrderGrowthValueTemp> list(OrderGrowthValueTempQueryRequest queryReq) {
        return orderGrowthValueTempRepository.findAll(OrderGrowthValueTempWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     */
    public OrderGrowthValueTempVO wrapperVo(OrderGrowthValueTemp orderGrowthValueTemp) {
        if (orderGrowthValueTemp != null) {
            OrderGrowthValueTempVO orderGrowthValueTempVO = new OrderGrowthValueTempVO();
            KsBeanUtil.copyPropertiesThird(orderGrowthValueTemp, orderGrowthValueTempVO);
            return orderGrowthValueTempVO;
        }
        return null;
    }
}
