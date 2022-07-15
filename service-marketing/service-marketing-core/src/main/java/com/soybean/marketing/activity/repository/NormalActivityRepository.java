package com.soybean.marketing.activity.repository;

import com.soybean.marketing.activity.model.NormalActivity;
import com.soybean.marketing.api.req.NormalActivitySearchReq;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface NormalActivityRepository extends JpaRepository<NormalActivity, Integer>, JpaSpecificationExecutor<NormalActivity> {


    default Specification<NormalActivity> buildSearchCondition(NormalActivitySearchReq searchReq){
        return (Specification<NormalActivity>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            if (searchReq.getId() != null) {
                conditionList.add(criteriaBuilder.equal(root.get("id"), searchReq.getId()));
            }
            if (StringUtils.isNotBlank(searchReq.getName())) {
                conditionList.add(criteriaBuilder.like(root.get("name"), "%" + searchReq.getName() + "%"));
            }
            if (searchReq.getPublishState() != null) {
                conditionList.add(criteriaBuilder.equal(root.get("publishState"), searchReq.getPublishState()));
            }
            if (CollectionUtils.isNotEmpty(searchReq.getChannelTypes())) {
                conditionList.add(root.get("channelType").in(searchReq.getChannelTypes()));
            }
            if (searchReq.getBeginTime() != null) {
                conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("beginTime"), searchReq.getBeginTime()));
            }
            if (searchReq.getEndTime() != null) {
                conditionList.add(criteriaBuilder.lessThan(root.get("beginTime"), searchReq.getEndTime()));
            }
            if (searchReq.getBeginTimeR() != null && searchReq.getEndTime() != null) {
                Predicate condition1 = criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("beginTime"), searchReq.getBeginTimeR()),
                        criteriaBuilder.lessThan(root.get("beginTime"), searchReq.getEndTimeR()));
                Predicate condition2 = criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), searchReq.getBeginTimeR()),
                        criteriaBuilder.lessThan(root.get("endTime"), searchReq.getEndTimeR()));
                conditionList.add(criteriaBuilder.or(condition1, condition2));
            }
            //状态
            if (searchReq.getStatus() != null) {
                LocalDateTime now = LocalDateTime.now();
                if(searchReq.getStatus().equals(StateEnum.BEFORE.getCode())){
                    //未开始
                    conditionList.add(criteriaBuilder.greaterThan(root.get("beginTime"), now));
                }else if(searchReq.getStatus().equals(StateEnum.RUNNING.getCode())){
                    //进行中
                    conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), now));
                    conditionList.add(criteriaBuilder.lessThanOrEqualTo(root.get("beginTime"), now));
                }else if(searchReq.getStatus().equals(StateEnum.AFTER.getCode())){
                    //已结束
                    conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), now));
                }
            }

            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }
}
