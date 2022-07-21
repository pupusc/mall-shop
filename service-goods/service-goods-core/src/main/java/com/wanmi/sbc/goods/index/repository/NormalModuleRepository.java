package com.wanmi.sbc.goods.index.repository;


import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSearchReq;
import com.wanmi.sbc.goods.index.model.NormalModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface NormalModuleRepository extends JpaRepository<NormalModule, Integer>, JpaSpecificationExecutor<NormalModule> {



    default Specification<NormalModule> packageWhere(NormalModuleSearchReq normalModuleSearchReq){
        return new Specification<NormalModule>() {
            @Override
            public Predicate toPredicate(Root<NormalModule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();
                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (normalModuleSearchReq.getId() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("id"), normalModuleSearchReq.getId()));
                }
                if (StringUtils.isNotBlank(normalModuleSearchReq.getName())) {
                    conditionList.add(criteriaBuilder.like(root.get("name"), "%" + normalModuleSearchReq.getName() + "%"));
                }
                if (normalModuleSearchReq.getPublishState() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("publishState"), normalModuleSearchReq.getPublishState()));
                }
                if (normalModuleSearchReq.getBeginTime() != null) {
                    conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("beginTime"), normalModuleSearchReq.getBeginTime()));
                }
                if (normalModuleSearchReq.getEndTime() != null) {
                    conditionList.add(criteriaBuilder.lessThan(root.get("beginTime"), normalModuleSearchReq.getEndTime()));
                }
                if (normalModuleSearchReq.getBeginTimeR() != null && normalModuleSearchReq.getEndTimeR() != null) {
                    Predicate condition1 = criteriaBuilder.and(
                            criteriaBuilder.greaterThanOrEqualTo(root.get("beginTime"), normalModuleSearchReq.getBeginTimeR()),
                            criteriaBuilder.lessThan(root.get("beginTime"), normalModuleSearchReq.getEndTimeR()));
                    Predicate condition2 = criteriaBuilder.and(
                            criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), normalModuleSearchReq.getBeginTimeR()),
                            criteriaBuilder.lessThan(root.get("endTime"), normalModuleSearchReq.getEndTimeR()));
                    conditionList.add(criteriaBuilder.or(condition1, condition2));
                }
                //状态
                if (normalModuleSearchReq.getStatus() != null) {
                    LocalDateTime now = LocalDateTime.now();
                    if(normalModuleSearchReq.getStatus().equals(StateEnum.BEFORE.getCode())){
                        //未开始
                        conditionList.add(criteriaBuilder.greaterThan(root.get("beginTime"), now));
                    }else if(normalModuleSearchReq.getStatus().equals(StateEnum.RUNNING.getCode())){
                        //进行中
                        conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), now));
                        conditionList.add(criteriaBuilder.lessThanOrEqualTo(root.get("beginTime"), now));
                    }else if(normalModuleSearchReq.getStatus().equals(StateEnum.AFTER.getCode())){
                        //已结束
                        conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), now));
                    }
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
