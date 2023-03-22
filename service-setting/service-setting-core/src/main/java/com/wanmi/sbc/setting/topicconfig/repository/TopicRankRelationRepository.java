package com.wanmi.sbc.setting.topicconfig.repository;

import com.wanmi.sbc.setting.topicconfig.model.root.TopicHeadImage;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicRankRelation;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface TopicRankRelationRepository extends JpaRepository<TopicRankRelation, Integer>,
        JpaSpecificationExecutor<TopicRankRelation> {

    @Query(value = "select * from topic_rank_relation where del_flag=0 and p_rank_colum_id in ?1 order by c_rank_sorting asc,p_rank_colum_id asc", nativeQuery = true)
    List<TopicRankRelation> collectByPRankColumIdOrderByCRankSortingAsc(List<Integer> ids);

    @Query(value="select * from topic_rank_relation order by topic_rank_sorting asc limit 10",nativeQuery = true)
    List<TopicRankRelation> collectOrderByTopicRankSortingAsc();

    @Modifying
    @Query(value = "update TopicRankRelation t set t.delFlag=?1 where t.id=?2")
    List<TopicRankRelation> enableRank(Integer delFlag, Integer id);

    @Modifying
    @Transactional
    Integer deleteByPRankColumId(Integer PRankColumId);
}
