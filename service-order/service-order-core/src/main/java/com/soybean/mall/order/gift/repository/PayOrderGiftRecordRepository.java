package com.soybean.mall.order.gift.repository;

import com.soybean.mall.order.api.request.record.OrderGiftRecordSearchReq;
import com.soybean.mall.order.gift.model.OrderGiftRecord;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 10:19 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public interface PayOrderGiftRecordRepository extends JpaRepository<OrderGiftRecord, Integer>, JpaSpecificationExecutor<OrderGiftRecord> {



    /**
     * 记录信息
     * @return
     */
    default Specification<OrderGiftRecord> packageWhere(OrderGiftRecordSearchReq req) {
        return new Specification<OrderGiftRecord>() {
            @Override
            public Predicate toPredicate(Root<OrderGiftRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();
                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (StringUtils.isNotBlank(req.getOrderId())) {
                    conditionList.add(criteriaBuilder.equal(root.get("orderId"), req.getOrderId()));
                }
                if (StringUtils.isNotBlank(req.getCustomerId())) {
                    conditionList.add(criteriaBuilder.equal(root.get("customerId"), req.getCustomerId()));
                }
                if (StringUtils.isNotBlank(req.getActivityId())) {
                    conditionList.add(criteriaBuilder.equal(root.get("activityId"), req.getActivityId()));
                }
                if (CollectionUtils.isNotEmpty(req.getActivityIds())) {
                    conditionList.add(root.get("activityId").in(req.getActivityIds()));
                }
                if (req.getRecordCategory() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("recordCategory"), req.getRecordCategory()));
                }
                if (StringUtils.isNotBlank(req.getQuoteId())) {
                    conditionList.add(criteriaBuilder.equal(root.get("quoteId"), req.getQuoteId()));
                }
                if (CollectionUtils.isNotEmpty(req.getQuoteIds())) {
                    conditionList.add(root.get("quoteId").in(req.getQuoteIds()));
                }
                if (!CollectionUtils.isEmpty(req.getRecordStatus())) {
                    conditionList.add(root.get("recordStatus").in(req.getRecordStatus()));

                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
