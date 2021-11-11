package com.wanmi.sbc.setting.topicconfig.repository;

import com.wanmi.sbc.setting.topicconfig.model.root.Topic;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStorey;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicStoreyRepository extends JpaRepository<TopicStorey, Integer>,
        JpaSpecificationExecutor<TopicStorey> {

    @Modifying
    @Query("update TopicStorey w set w.deleted = 1, w.updateTime = now() where w.topicId = ?1")
    int deleteByTopicId(Integer topicId);

    @Query("from TopicStorey w where w.topicId = ?1 and w.deleted = 0 and w.status = 1")
    List<TopicStorey> getByTopicId(Integer topicId);

    @Modifying
    @Query("update TopicStorey w set w.status = ?2, w.updateTime = now() where w.id = ?1")
    int enable(Integer storeyId,Integer status);
}