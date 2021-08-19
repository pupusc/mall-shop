package com.wanmi.sbc.goods.thirdgoodscate.repository;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateRelDTO;
import com.wanmi.sbc.goods.thirdgoodscate.model.root.ThirdGoodsCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>DAO</p>
 * @author 
 * @date 2020-08-17 14:46:43
 */
@Repository
public interface ThirdGoodsCateRepository extends JpaRepository<ThirdGoodsCate, Long>,
        JpaSpecificationExecutor<ThirdGoodsCate> {

    /**
     * 单个删除
     * @author 
     */
    @Modifying
    @Query("update ThirdGoodsCate set delFlag = 1 where cateId = ?1")
    void deleteById(Long cateId);

    /**
     * 批量删除
     * @author 
     */
    @Modifying
    @Query("update ThirdGoodsCate set delFlag = 1 where cateId in ?1")
    void deleteByIdList(List<Long> cateIdList);

    Optional<ThirdGoodsCate> findByCateIdAndDelFlag(Long id, DeleteFlag delFlag);

    @Modifying
    @Query("delete from ThirdGoodsCate where thirdPlatformType=?1")
    void delAllByThirdPlatformType(ThirdPlatformType thirdPlatformType);

    /**
     * 根据三方类目父id关联查询平台类目
     * @param cateParentId
     * @return
     */
    @Query(value = "SELECT new com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateRelDTO(t.id,t.cateId,t.cateName,t.cateParentId,t.thirdPlatformType,g.cateId,g.cateName) FROM ThirdGoodsCate t \n" +
            "LEFT JOIN GoodsCateThirdCateRel r ON t.cateId=r.cateId\n" +
            "LEFT JOIN GoodsCate g ON g.cateId =r.thirdCateId\n" +
            "WHERE t.thirdPlatformType=:thirdPlatformType AND t.cateParentId=:cateParentId order by t.cateId")
    List<ThirdGoodsCateRelDTO> getRelByParentId(@Param("thirdPlatformType") ThirdPlatformType thirdPlatformType, @Param("cateParentId") Long cateParentId);
    /**
     * 关联查询平台类目
     * @return
     */
    @Query(value = "SELECT new com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateRelDTO(t.id,t.cateId,t.cateName,t.cateParentId,t.thirdPlatformType,g.cateId,g.cateName) FROM ThirdGoodsCate t \n" +
            "LEFT JOIN GoodsCateThirdCateRel r ON r.delFlag=0 and t.cateId=r.thirdCateId\n" +
            "LEFT JOIN GoodsCate g ON g.delFlag=0 and g.cateId =r.cateId\n" +
            "WHERE t.thirdPlatformType=?1 and t.delFlag=0 order by t.cateId")
    List<ThirdGoodsCateRelDTO> getRel(ThirdPlatformType thirdPlatformType);

    @Query(value = "from ThirdGoodsCate where thirdPlatformType=:thirdPlatformType and cateId in :cateIds")
    List<ThirdGoodsCate> getByCateIds(ThirdPlatformType thirdPlatformType, List<Long> cateIds);
}
