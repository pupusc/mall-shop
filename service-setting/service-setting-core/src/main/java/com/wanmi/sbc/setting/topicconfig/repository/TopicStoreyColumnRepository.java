package com.wanmi.sbc.setting.topicconfig.repository;


import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyColumnQueryRequest;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyColumn;
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
 * @Description 楼层栏目
 * @Author zh
 * @Date  2023/2/7 17:30
 * @param: null
 * @return: null
 */
@Repository
public interface TopicStoreyColumnRepository extends JpaRepository<TopicStoreyColumn, Integer>,
        JpaSpecificationExecutor<TopicStoreyColumn> {

    /**
     * 拼接筛选条件
     * @return
     */
    default Specification<TopicStoreyColumn> topicStoreySearch(TopicStoreyColumnQueryRequest request) {
        return (Specification<TopicStoreyColumn>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("topicStoreyId"), request.getTopicStoreyId()));
            if (request.getId() != null &&  !"".equals(request.getId())) {
                conditionList.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }
            if (StringUtils.isNotEmpty(request.getName())) {
                conditionList.add(criteriaBuilder.like(root.get("name"), request.getName() + "%"));
            }
            if (request.getPublishState() != null) {
                conditionList.add(criteriaBuilder.equal(root.get("deleted"), request.getPublishState()));
            }
            if(request.getState() != null){
                LocalDateTime now = LocalDateTime.now();
                if(request.getState() == 0){
                    //未开始
                    conditionList.add(criteriaBuilder.greaterThan(root.get("createTime"), now));
                }else if(request.getState() == 1){
                    //进行中
                    conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), now));
                    conditionList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), now));
                }else if(request.getState() == 2){
                    //已结束
                    conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), now));
                }
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }

    default Specification<TopicStoreyColumn> topicStoreyRankLevel0Search(TopicStoreyColumnQueryRequest request) {
        return (Specification<TopicStoreyColumn>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("topicStoreyId"), request.getTopicStoreyId()));
            conditionList.add(criteriaBuilder.equal(root.get("level"),0));
            if (request.getId() != null &&  !"".equals(request.getId())) {
                conditionList.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }
            if (StringUtils.isNotEmpty(request.getName())) {
                conditionList.add(criteriaBuilder.like(root.get("name"), request.getName() + "%"));
            }
            if (request.getPublishState() != null) {
                conditionList.add(criteriaBuilder.equal(root.get("deleted"), request.getPublishState()));
            }
            if(request.getState() != null){
                LocalDateTime now = LocalDateTime.now();
                if(request.getState() == 0){
                    //未开始
                    conditionList.add(criteriaBuilder.greaterThan(root.get("createTime"), now));
                }else if(request.getState() == 1){
                    //进行中
                    conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), now));
                    conditionList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), now));
                }else if(request.getState() == 2){
                    //已结束
                    conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), now));
                }
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }

    //楼层栏目列表
    List<TopicStoreyColumn> getByTopicStoreyIdAndDeletedOrderByOrderNumAscCreateTimeDesc(Integer topicStoreyId, Integer deleted);

    List<TopicStoreyColumn> getByTopicStoreyIdAndLevelOrderByOrderNumAscCreateTimeDesc(Integer topicStoreyId, Integer level);

    @Modifying
    @Query("update TopicStoreyColumn T set T.deleted = ?2, T.updateTime = now() where T.id = ?1")
    int enable(Integer id, Integer deleted);


}