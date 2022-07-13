package com.soybean.marketing.activity.repository;

import com.soybean.marketing.activity.model.NormalActivityPointSku;
import com.soybean.marketing.api.req.NormalActivityPointSkuSearchReq;
import com.wanmi.sbc.common.enums.DeleteFlag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public interface NormalActivityPointSkuRepository extends JpaRepository<NormalActivityPointSku, Integer>, JpaSpecificationExecutor<NormalActivityPointSku> {


    default Specification<NormalActivityPointSku> buildSearchCondition(NormalActivityPointSkuSearchReq searchReq){
        return (Specification<NormalActivityPointSku>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            if (searchReq.getNormalActivityId() != null) {
                conditionList.add(criteriaBuilder.equal(root.get("normalActivityId"), searchReq.getNormalActivityId()));
            }
            if (CollectionUtils.isNotEmpty(searchReq.getNormalActivityIds())) {
                conditionList.add(root.get("normalActivityId").in(searchReq.getNormalActivityIds()));
            }
            if (CollectionUtils.isNotEmpty(searchReq.getSkuIds())) {
                conditionList.add(root.get("skuId").in(searchReq.getSkuIds()));
            }
            if (CollectionUtils.isNotEmpty(searchReq.getSpuIds())) {
                conditionList.add(root.get("spuId").in(searchReq.getSpuIds()));
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }
}
