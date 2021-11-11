package com.wanmi.sbc.setting.topicconfig.repository;


import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicStoreyContentRepository extends JpaRepository<TopicStoreyContent, Integer>,
        JpaSpecificationExecutor<TopicStoreyContent> {

    @Query("from TopicStoreyContent w where w.topicId = ?1 and w.deleted = 0 and w.status =1")
    List<TopicStoreyContent> getByTopicId(Integer topicId);
}