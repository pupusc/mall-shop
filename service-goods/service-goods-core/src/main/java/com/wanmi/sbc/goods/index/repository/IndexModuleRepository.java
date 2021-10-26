package com.wanmi.sbc.goods.index.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.request.index.CmsTitleSearchRequest;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.index.model.IndexModule;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public interface IndexModuleRepository  extends JpaRepository<IndexModule, Integer>, JpaSpecificationExecutor<IndexModule> {

    default Specification<IndexModule> buildSearchCondition(CmsTitleSearchRequest cmsTitleSearchRequest){
        return (Specification<IndexModule>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            if (cmsTitleSearchRequest.publishState != null) {
                conditionList.add(criteriaBuilder.equal(root.get("publishState"), PublishState.fromValue(cmsTitleSearchRequest.publishState)));
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }
}
