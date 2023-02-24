package com.wanmi.sbc.setting.topicconfig.repository;

import com.wanmi.sbc.setting.topicconfig.model.root.TopicHeadImage;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicRankRelation;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRankRelationRepository extends JpaRepository<TopicRankRelation, Integer>,
        JpaSpecificationExecutor<TopicRankRelation> {

    List<TopicRankRelation> findByPRankColumIdIn(List<Integer> ids);
}
