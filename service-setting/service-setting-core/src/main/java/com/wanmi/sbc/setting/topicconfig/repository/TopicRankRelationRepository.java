package com.wanmi.sbc.setting.topicconfig.repository;

import com.wanmi.sbc.setting.topicconfig.model.root.TopicHeadImage;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicRankRelation;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRankRelationRepository extends JpaRepository<TopicRankRelation, Integer>,
        JpaSpecificationExecutor<TopicRankRelation> {

    @Query(value = "select * from topic_rank_relation where p_rank_colum_id in ?1 order by c_rank_sorting asc,p_rank_colum_id asc", nativeQuery = true)
    List<TopicRankRelation> collectByPRankColumIdOrderByCRankSortingAsc(List<Integer> ids);

    @Query(value="select * from topic_rank_relation order by topic_rank_sorting asc limit 10",nativeQuery = true)
    List<TopicRankRelation> collectOrderByTopicRankSortingAsc();
}
