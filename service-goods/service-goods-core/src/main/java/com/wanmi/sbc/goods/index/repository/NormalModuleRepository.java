package com.wanmi.sbc.goods.index.repository;


import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.index.model.NormalModule;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public interface NormalModuleRepository extends JpaRepository<NormalModule, Integer>, JpaSpecificationExecutor<NormalModule> {



    default Specification<NormalModule> packageWhere(){
        return new Specification<NormalModule>() {
            @Override
            public Predicate toPredicate(Root<NormalModule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();
                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
