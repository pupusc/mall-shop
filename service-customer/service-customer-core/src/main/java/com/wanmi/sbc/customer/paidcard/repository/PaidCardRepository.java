package com.wanmi.sbc.customer.paidcard.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.paidcard.model.root.PaidCard;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>付费会员DAO</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@Repository
public interface PaidCardRepository extends JpaRepository<PaidCard, String>,
        JpaSpecificationExecutor<PaidCard> {

    /**
     * 单个删除付费会员
     * @author xuhai
     */
    @Modifying
    @Query("update PaidCard set delFlag = 1 where id = ?1")
    void deleteById(String id);

    /**
     * 批量删除付费会员
     * @author xuhai
     */
    @Modifying
    @Query("update PaidCard set delFlag = 1 where id in ?1")
    int deleteByIdList(List<String> idList);

    Integer countByNameAndDelFlag(String name, DeleteFlag flag);

    Integer countByErpSpuCodeAndDelFlag(String erpSpuCode, DeleteFlag flag);

    @Query(value = "select count(1) from paid_card where del_flag = 0 and id <> ?1 and erp_spu_code = ?2", nativeQuery = true)
    Integer countByErpSpuCode4Modify(String id, String erpSpuCode);
}
