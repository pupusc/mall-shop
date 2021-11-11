package com.wanmi.sbc.setting.topicconfig.repository;


import com.wanmi.sbc.setting.topicconfig.model.root.TopicStorey;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicStoreyGoodsRepository extends JpaRepository<TopicStoreyGoods, Integer>,
        JpaSpecificationExecutor<TopicStoreyGoods> {

    @Query("from TopicStoreyGoods w where w.topicId = ?1 and w.deleted = 0 and w.status =1")
    List<TopicStoreyGoods> getByTopicId(Integer topicId);
}