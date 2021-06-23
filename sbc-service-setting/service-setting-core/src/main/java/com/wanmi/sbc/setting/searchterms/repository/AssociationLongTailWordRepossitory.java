package com.wanmi.sbc.setting.searchterms.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.searchterms.model.AssociationLongTailWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AssociationLongTailWordRepossitory extends JpaRepository<AssociationLongTailWord, Long>,
        JpaSpecificationExecutor<AssociationLongTailWord> {

    /**
     * 删除联想词
     *
     * @param associationLongTailWordId
     * @return
     */
    @Modifying
    @Query("update AssociationLongTailWord set  del_flag = 1 where " +
            "association_long_tail_word_id=:associationLongTailWordId")
    int deleteAssociationLongTailWord(@Param("associationLongTailWordId") Long associationLongTailWordId);

    /**
     * 查询searchAssociationalWordId下的联想词个数
     *
     * @param searchAssociationalWordId
     * @return
     */
    @Query(value = "select count(1) from AssociationLongTailWord a where a" +
            ".searchAssociationalWordId=:searchAssociationalWordId and a.delFlag=0")
    Long countAssociationLongTailWord(@Param("searchAssociationalWordId") Long searchAssociationalWordId);

    /**
     * 联想词排序
     *
     * @param
     * @return
     */
    @Modifying
    @Query(value = "update AssociationLongTailWord at set at.sortNumber =:sortNumber,at.delFlag = 0 where at" +
            ".associationLongTailWordId=:associationLongTailWordId")
    Integer sortAssociationalWord(@Param("sortNumber") Long sortNumber,
                                  @Param("associationLongTailWordId") Long associationLongTailWordId);

    /**
     * 根据搜索词id查询关联的联想词
     * @param searchAssociationalWordId
     * @return
     */
    List<AssociationLongTailWord> findBySearchAssociationalWordIdAndDelFlagOrderBySortNumberAscCreateTimeDesc(Long searchAssociationalWordId, DeleteFlag delFlag);

    /**
     * 根据搜索词id查询关联的联想词
     */


    List<AssociationLongTailWord> findBySearchAssociationalWordIdInAndDelFlagOrderBySortNumberAscCreateTimeDesc(List<Long> ids,DeleteFlag delFlag);
    /**
     * 根据搜索词id查询关联的联想词
     * @param searchAssociationalWordId
     * @param deleteFlag
     * @return
     */
    List<AssociationLongTailWord> findBySearchAssociationalWordIdAndDelFlag(Long searchAssociationalWordId,DeleteFlag deleteFlag);

    /**
     * 除自己外，联想词是否重复
     *
     * @param searchAssociationalWordId   搜索词ID
     * @param associationalWord 联想词名称
     * @param associationLongTailWordId 联想词id
     * @return
     */
    @Query("select count(1) from AssociationLongTailWord c where c.delFlag = 0 and c.associationLongTailWordId <> ?1 and c" +
            ".associationalWord = ?3 and c.searchAssociationalWordId = ?2 ")
    Integer findByNameExceptSelf(Long associationLongTailWordId, Long searchAssociationalWordId,
                                 String associationalWord);

}
