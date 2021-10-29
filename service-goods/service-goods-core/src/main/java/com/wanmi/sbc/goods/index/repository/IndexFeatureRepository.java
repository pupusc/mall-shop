package com.wanmi.sbc.goods.index.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicSearchRequest;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.index.model.IndexFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface IndexFeatureRepository extends JpaRepository<IndexFeature, Integer>, JpaSpecificationExecutor<IndexFeature> {

    default Specification<IndexFeature> buildSearchCondition(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest){
        return (Specification<IndexFeature>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            if (cmsSpecialTopicSearchRequest.id != null) {
                conditionList.add(criteriaBuilder.equal(root.get("id"), cmsSpecialTopicSearchRequest.id));
            }
            if (StringUtils.isNotEmpty(cmsSpecialTopicSearchRequest.name)) {
                conditionList.add(criteriaBuilder.like(root.get("name"), cmsSpecialTopicSearchRequest.name + "%"));
            }
            if (!CollectionUtils.isEmpty(cmsSpecialTopicSearchRequest.idColl)) {
                conditionList.add(root.get("id").in(cmsSpecialTopicSearchRequest.idColl));
            }
            if (cmsSpecialTopicSearchRequest.publishState != null) {
                conditionList.add(criteriaBuilder.equal(root.get("publishState"), PublishState.fromValue(cmsSpecialTopicSearchRequest.publishState)));
            }
            if(cmsSpecialTopicSearchRequest.state != null){
                LocalDateTime now = LocalDateTime.now();
                if(cmsSpecialTopicSearchRequest.state == 0){
                    //未开始
                    conditionList.add(criteriaBuilder.greaterThan(root.get("beginTime"), now));
                }else if(cmsSpecialTopicSearchRequest.state == 1){
                    //进行中
                    conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), now));
                    conditionList.add(criteriaBuilder.lessThanOrEqualTo(root.get("beginTime"), now));
                }else if(cmsSpecialTopicSearchRequest.state == 2){
                    //已结束
                    conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), now));
                }
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }
}
