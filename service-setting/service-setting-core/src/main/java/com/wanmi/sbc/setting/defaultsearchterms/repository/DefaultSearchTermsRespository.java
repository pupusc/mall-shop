package com.wanmi.sbc.setting.defaultsearchterms.repository;


import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.request.topicconfig.ColumnContentQueryRequest;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.SearchTermBo;
import com.wanmi.sbc.setting.defaultsearchterms.model.DefaultSearchTerms;
import com.wanmi.sbc.setting.popularsearchterms.model.PopularSearchTerms;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyColumnContent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 默认搜索词
 * @Author zh
 * @Date 2023/2/4 11:14
 */
public interface DefaultSearchTermsRespository extends JpaRepository<DefaultSearchTerms, Long>,
        JpaSpecificationExecutor<DefaultSearchTerms> {

    /**
     * @Description 查询父默认搜索词
     * @Author zh
     * @Date 2023/2/4 14:13
     */
    List<DefaultSearchTerms> findByDelFlagAndDefaultChannelAndIsParentOrderBySortNumberAscCreateTimeDesc(DeleteFlag delFlag, Integer defaultChannel, Boolean isParent);

    /**
     * @Description 查询子默认搜索词
     * @Author zh
     * @Date 2023/2/4 14:13
     */
    List<DefaultSearchTerms> findByDelFlagAndDefaultChannelAndParentIdAndIsParentOrderBySortNumberAscCreateTimeDesc(DeleteFlag delFlag, Integer defaultChannel, Long parentId, Boolean isParent);

    default Specification<DefaultSearchTerms> deFaultSearchTermsSearchContent(SearchTermBo request) {
        return (Specification<DefaultSearchTerms>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            if (request.getId() != null && !"".equals(request.getId())) {
                conditionList.add(criteriaBuilder.equal(root.get("parentId"), request.getId()));
            } else {
                conditionList.add(criteriaBuilder.equal(root.get("parentId"), 0));
            }
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), 0));
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }

    @Query("select count(1) from DefaultSearchTerms where defaultSearchKeyword=?1 and delFlag=0")
    int countByDefaultSearchKeywordAndDelFlag(String defaultSearchKeyword);

    @Query("select count(1) from DefaultSearchTerms where id=?1 and delFlag=0")
    int countById(Long id);
}
