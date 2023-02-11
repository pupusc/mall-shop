package com.wanmi.sbc.setting.topicconfig.repository;

import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyContent;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreySearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface TopicStoreySearchRepository extends JpaRepository<TopicStoreySearch, Integer>, JpaSpecificationExecutor<TopicStoreyContent> {




}
