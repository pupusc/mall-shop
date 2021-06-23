package com.wanmi.sbc.setting.searchterms.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.searchterms.model.SearchAssociationalWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>搜索词Repository</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@Repository
public interface SearchAssociationalWordRepository extends JpaRepository<SearchAssociationalWord,Long> ,
        JpaSpecificationExecutor<SearchAssociationalWord> {

    /**
     * 删除搜索
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update SearchAssociationalWord set  del_flag = 1 where id=?1")
    int  deleteSearchAssociationalWord(Long id);

    /**
     * 模糊搜索词
     */
    List<SearchAssociationalWord> findAllBySearchTermsContainingAndDelFlag(String associationalWordName,
                                                                                DeleteFlag deleteFlag);

    /**
     * 根据名称查询存在数量
     * @param searchTerms
     * @param deleteFlag
     * @return
     */
    Integer countBySearchTermsAndDelFlag(String searchTerms, DeleteFlag deleteFlag);

    /**
     * 除自己外，根据搜索词查询是否存在相同名字
     *
     * @param id   搜索词ID
     * @param searchTerms 搜索词名称
     * @return
     */
    @Query("select count(1) from SearchAssociationalWord c where c.delFlag = 0 and c.id <> ?1 and c.searchTerms" +
            " = ?2")
    Integer findByNameExceptSelf(Long id, String searchTerms);

}
