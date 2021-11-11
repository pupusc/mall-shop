package com.wanmi.sbc.setting.topicconfig.repository;

import com.wanmi.sbc.setting.topicconfig.model.root.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer>,
        JpaSpecificationExecutor<Topic> {

    @Query("from Topic w where w.topicKey = ?1 and w.deleted = 0")
    List<Topic> getByKey(String topicKey);
}