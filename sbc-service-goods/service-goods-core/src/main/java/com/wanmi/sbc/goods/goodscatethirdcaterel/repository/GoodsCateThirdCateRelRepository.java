package com.wanmi.sbc.goods.goodscatethirdcaterel.repository;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.goodscatethirdcaterel.model.root.GoodsCateThirdCateRel;
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
 * <p>平台类目和第三方平台类目映射DAO</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@Repository
public interface GoodsCateThirdCateRelRepository extends JpaRepository<GoodsCateThirdCateRel, Long>,
        JpaSpecificationExecutor<GoodsCateThirdCateRel> {

    /**
     * 单个删除平台类目和第三方平台类目映射
     * @author 
     */
    @Modifying
    @Query("update GoodsCateThirdCateRel set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除平台类目和第三方平台类目映射
     * @author 
     */
    @Modifying
    @Query("update GoodsCateThirdCateRel set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<GoodsCateThirdCateRel> findByIdAndDelFlag(Long id, DeleteFlag delFlag);

    @Modifying
    @Query("update GoodsCateThirdCateRel SET delFlag=1 WHERE delFlag=0 AND thirdCateId IN ?1 AND thirdPlatformType=?2")
    void deleteInThirdCateIds( List<Long> thirdCateIds, ThirdPlatformType thirdPlatformType);

}
