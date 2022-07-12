package com.soybean.marketing.activity.repository;

import com.soybean.marketing.activity.model.NormalActivityPointSku;
import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public interface NormalActivityPointSkuRepository extends JpaRepository<NormalActivityPointSku, Integer>, JpaSpecificationExecutor<NormalActivityPointSku> {


    default Specification<NormalActivityPointSku> buildSearchCondition(){
        return (Specification<NormalActivityPointSku>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }
}
