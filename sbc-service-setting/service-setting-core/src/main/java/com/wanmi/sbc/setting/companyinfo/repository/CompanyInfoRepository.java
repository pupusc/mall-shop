package com.wanmi.sbc.setting.companyinfo.repository;

import com.wanmi.sbc.setting.companyinfo.model.root.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>公司信息DAO</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long>,
        JpaSpecificationExecutor<CompanyInfo> {

    /**
     * 单个删除公司信息
     * @author lq
     */
    @Modifying
    @Query("update CompanyInfo set delFlag = 1 where companyInfoId = ?1")
    void deleteById(Long companyInfoId);

    /**
     * 批量删除公司信息
     * @author lq
     */
    @Modifying
    @Query("update CompanyInfo set delFlag = 1 where companyInfoId in ?1")
    int deleteByIdList(List<Long> companyInfoIdList);


}
