package com.wanmi.sbc.goods.priceadjustmentrecorddetail.repository;

import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentResult;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.model.root.PriceAdjustmentRecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>调价单详情表DAO</p>
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@Repository
public interface PriceAdjustmentRecordDetailRepository extends JpaRepository<PriceAdjustmentRecordDetail, Long>,
        JpaSpecificationExecutor<PriceAdjustmentRecordDetail> {

    void deleteByIdAndPriceAdjustmentNo(Long id,String adjustNo);

    @Modifying
    @Query(value = "delete from PriceAdjustmentRecordDetail r where r.priceAdjustmentNo in ?1 and r.confirmFlag = 0")
    void deleteByAdjustNos(List<String> adjustNos);

    @Modifying
    @Query(value = "update PriceAdjustmentRecordDetail d set d.adjustResult = ?2, d.failReason = ?3 where d.priceAdjustmentNo = ?1")
    void executeFail(String adjustNo, PriceAdjustmentResult result, String failReason);

}
