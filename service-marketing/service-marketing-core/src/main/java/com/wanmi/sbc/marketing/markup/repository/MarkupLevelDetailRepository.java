package com.wanmi.sbc.marketing.markup.repository;

import com.wanmi.sbc.marketing.markup.model.root.MarkupLevelDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>加价购活动DAO</p>
 * @author he
 * @date 2021-02-04 16:11:24
 */
@Repository
public interface MarkupLevelDetailRepository extends JpaRepository<MarkupLevelDetail, Long>,
        JpaSpecificationExecutor<MarkupLevelDetail> {
    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     *  根据加价购活动id 删除活动阶梯的详细信息
     * @param id
     * @return
     */
    int deleteByMarkupId(Long id);

    /**
     *  获取换购的商品
     * @param marketingId
     * @return
     */
    @Query("from MarkupLevelDetail where markupId in  (?1) and  markupId  <> (?2)")
    List<MarkupLevelDetail> getMarkupSku(List<Long> marketingIds,Long notContainMarkupId);
}
