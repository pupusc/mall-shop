package com.wanmi.sbc.setting.popularsearchterms.repository;


import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.popularsearchterms.model.PopularSearchTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>热门搜索词</p>
 * @author weiwenhao
 * @date 2019-04-17
 */
public interface PopularSearchTermsRespository extends JpaRepository<PopularSearchTerms,Long>,
        JpaSpecificationExecutor<PopularSearchTerms> {



    /**
     * 查询热门搜索词的个数
     * @return
     */
    @Query(value = "select count(1) from PopularSearchTerms a where a.delFlag=0")
    Long countPopularSearchTerms();


    /**
     * 删除热门搜索词
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update PopularSearchTerms p set  p.delFlag = 1 where id=?1")
    int  deletePopularSearchTerms(Long id);

    /**
     * 查询热门搜索词
     * @param delFlag
     * @return
     */
    List<PopularSearchTerms> findByDelFlagOrderBySortNumberAscCreateTimeDesc(DeleteFlag delFlag);

    /**
     * 热门搜索词排序
     *
     * @param
     * @return
     */
    @Modifying
    @Query(value = "update PopularSearchTerms at set at.sortNumber =:sortNumber,at.delFlag = 0 where at" +
            ".id=:popularSearchTermsId")
    Integer sortPopularSearchTerms(@Param("sortNumber") Long sortNumber,
                                  @Param("popularSearchTermsId") Long popularSearchTermsId);

    /**
     * 除自己外，根据热搜词名称查询其他热搜词是否存在
     *
     * @param id   热搜词ID
     * @param popularSearchKeyword 热搜词名称
     * @return
     */
    @Query("select count(1) from PopularSearchTerms c where c.delFlag = '0' and c.id <> ?1 and c.popularSearchKeyword = ?2")
    Integer findByKeyWordExceptSelf(Long id, String popularSearchKeyword);


}
