package com.wanmi.sbc.setting.topicconfig.repository;


import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicStoreyContentRepository extends JpaRepository<TopicStoreyContent, Integer>,
        JpaSpecificationExecutor<TopicStoreyContent> {

    @Query("from TopicStoreyContent w where w.topicId = ?1 and w.deleted = 0")
    List<TopicStoreyContent> getByTopicId(Integer topicId);

    @Modifying
    @Query("update TopicStoreyContent w set w.deleted = 1, w.updateTime = now() where w.storeyId = ?1")
    int deleteBySid(Integer storeyId);


}