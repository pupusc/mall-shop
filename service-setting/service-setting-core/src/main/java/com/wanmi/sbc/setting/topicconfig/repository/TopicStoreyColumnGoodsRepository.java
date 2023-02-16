package com.wanmi.sbc.setting.topicconfig.repository;


import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyColumnGoodsQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyColumnQueryRequest;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyContent;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreySearch;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreySearchContent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 楼层栏目商品
 * @Author zh
 * @Date  2023/2/7 17:30
 * @param: null
 * @return: null
 */
@Repository
public interface TopicStoreyColumnGoodsRepository extends JpaRepository<TopicStoreySearchContent, Integer>,
        JpaSpecificationExecutor<TopicStoreySearchContent> {
    //楼层栏目商品列表
    default Specification<TopicStoreySearchContent> topicStoreySearchContent(TopicStoreyColumnGoodsQueryRequest request) {
        return (Specification<TopicStoreySearchContent>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("topicStoreySearchId"), request.getTopicStoreySearchId()));
            if (request.getId() != null &&  "".equals(request.getId())) {
                conditionList.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }
            if (StringUtils.isNotEmpty(request.getGoodsName())) {
                conditionList.add(criteriaBuilder.like(root.get("name"), request.getGoodsName() + "%"));
            }
            if (request.getStartTime() != null) {
                conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), request.getStartTime()));
            }
            if (request.getEndTime() != null) {
                conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), request.getEndTime()));
            }
            if (request.getPublishState() != null) {
                conditionList.add(criteriaBuilder.equal(root.get("deleted"), request.getPublishState()));
            }
            if(request.getState() != null){
                LocalDateTime now = LocalDateTime.now();
                if(request.getState() == 0){
                    //未开始
                    conditionList.add(criteriaBuilder.greaterThan(root.get("startTime"), now));
                }else if(request.getState() == 1){
                    //进行中
                    conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), now));
                    conditionList.add(criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), now));
                }else if(request.getState() == 2){
                    //已结束
                    conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), now));
                }
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }


    default Specification<TopicStoreySearchContent> mixedComponentContent(MixedComponentQueryRequest request) {
        return (Specification<TopicStoreySearchContent>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("topicStoreySearchId"), request.getKeywordId()));
            conditionList.add(criteriaBuilder.equal(root.get("deleted"), 0));
            conditionList.add(criteriaBuilder.equal(root.get("level"), 0));
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }

    @Modifying
    @Query("update TopicStoreySearchContent T set T.deleted = ?2, T.updateTime = now() where T.id = ?1")
    int enable(Integer id, Integer deleted);


    @Query("from TopicStoreySearchContent w where w.topicStoreyId = ?1 and w.spuNo = ?2")
    List<TopicStoreySearchContent> getById(Integer topicId, String spuNo);

}