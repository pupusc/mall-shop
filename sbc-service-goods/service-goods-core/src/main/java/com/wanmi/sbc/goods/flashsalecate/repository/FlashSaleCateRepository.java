package com.wanmi.sbc.goods.flashsalecate.repository;

import com.wanmi.sbc.goods.flashsalecate.model.root.FlashSaleCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * <p>秒杀分类DAO</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@Repository
public interface FlashSaleCateRepository extends JpaRepository<FlashSaleCate, Long>,
        JpaSpecificationExecutor<FlashSaleCate> {

    /**
     * 单个删除秒杀分类
     * @author yxz
     */
    @Modifying
    @Query("update FlashSaleCate set delFlag = 1 where cateId = ?1")
    int modifyDelFlagById(Long cateId);

}
