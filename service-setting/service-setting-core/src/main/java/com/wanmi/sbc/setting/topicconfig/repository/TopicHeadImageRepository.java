package com.wanmi.sbc.setting.topicconfig.repository;

import com.wanmi.sbc.setting.topicconfig.model.root.TopicHeadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicHeadImageRepository extends JpaRepository<TopicHeadImage, Integer>,
        JpaSpecificationExecutor<TopicHeadImage> {

    @Modifying
    @Query("update TopicHeadImage w set w.deleted = 1, w.updateTime = now() where w.topicId = ?1")
    int deleteByTopicId(Integer topicId);

   
    @Query(value = "from TopicHeadImage w  where w.topicId = ?1 and w.deleted = 0")
    List<TopicHeadImage> getByTopicId(Integer topicId);
}