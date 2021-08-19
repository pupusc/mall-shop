package com.wanmi.sbc.account.invoice;

import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by yuanlinling on 2017/4/25.
 */
@Repository
public interface InvoiceProjectRepository extends JpaRepository<InvoiceProject, Long> {

    /**
     * 根据开票项目名称查询该项目是否存在
     * @param projectName
     * @param deleteFlag
     * @return
     */
    int countByProjectNameAndDelFlagAndCompanyInfoId(String projectName, DeleteFlag deleteFlag, Long companyInfoId);

    /**
     * 根据项目名称查询开票项目
     * @param projectName
     * @param deleteFlag
     * @return
     */
    InvoiceProject findByProjectNameAndDelFlagAndCompanyInfoId(String projectName, DeleteFlag deleteFlag, Long
            companyInfoId);

    /**
     * 根据id查询开票项目
     * @param projectId
     * @param deleteFlag
     * @return
     */
    Optional<InvoiceProject> findByProjectIdAndDelFlag(String projectId, DeleteFlag deleteFlag);

    /**
     * 删除
     * @param id
     */
    @Query("update InvoiceProject i set i.delFlag = 1 where i.projectId = :id")
    @Modifying
    void deleteInvoiceProjectByIds(@Param("id") String id);

    /**
     * 查询所有开票项
     * @param deleteFlag deleteFlag
     * @return list
     */
    List<InvoiceProject> findByDelFlagAndCompanyInfoIdOrCompanyInfoIdIsNull(DeleteFlag deleteFlag, Long companyInfoId);

    /**
     * 查询商家下所有的开票项目
     * @param deleteFlag
     * @param companyInfoId
     * @param pageable
     * @return
     */
    Page<InvoiceProject> findByDelFlagAndCompanyInfoIdOrCompanyInfoIdIsNullOrderByCreateTimeDesc(DeleteFlag
                                                                                                         deleteFlag, Long companyInfoId, Pageable pageable);
}
