package com.wanmi.sbc.customer.enterpriseinfo.repository;

import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>企业信息表DAO</p>
 * @author TangLian
 * @date 2020-03-02 19:05:06
 */
@Repository
public interface EnterpriseInfoRepository extends JpaRepository<EnterpriseInfo, String>,
        JpaSpecificationExecutor<EnterpriseInfo> {

    /**
     * 单个删除企业信息表
     * @author TangLian
     */
    @Modifying
    @Query("update EnterpriseInfo set delFlag = 1 where enterpriseId = ?1")
    void deleteById(String enterpriseId);

    /**
     * 批量删除企业信息表
     * @author TangLian
     */
    @Modifying
    @Query("update EnterpriseInfo set delFlag = 1 where enterpriseId in ?1")
    void deleteByIdList(List<String> enterpriseIdList);

    Optional<EnterpriseInfo> findByEnterpriseIdAndDelFlag(String id, DeleteFlag delFlag);

    /**
     * 查询企业信息
     * @param customerId
     * @param delFlag
     * @return
     */
    Optional<EnterpriseInfo> findByCustomerIdAndDelFlag(String customerId, DeleteFlag delFlag);



    /**
     * 根据会员IdList查询企业信息
     * @param customerIdList
     * @return
     */
    List<EnterpriseInfo> findAllByCustomerIdInAndDelFlag(List<String> customerIdList, DeleteFlag delFlag);
}
