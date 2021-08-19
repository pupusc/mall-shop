package com.wanmi.sbc.setting.expresscompany.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.expresscompany.model.root.ExpressCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>物流公司DAO</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@Repository
public interface ExpressCompanyRepository extends JpaRepository<ExpressCompany, Long>,
        JpaSpecificationExecutor<ExpressCompany> {

    List<ExpressCompany>findExpressCompanyByDelFlag(DeleteFlag deleteFlag);

    /**
     * 单个删除物流公司
     * @author lq
     */
    @Modifying
    @Query("update ExpressCompany set delFlag = 1 where expressCompanyId = ?1")
    void deleteById(Long expressCompanyId);

    /**
     * 批量删除物流公司
     * @author lq
     */
    @Modifying
    @Query("update ExpressCompany set delFlag = 1 where expressCompanyId in ?1")
    int deleteByIdList(List<Long> expressCompanyIdList);


    /**
     * 查询所有有效的物流公司个数
     * @author bail
     */
    @Query("select count(1) from ExpressCompany l where l.delFlag = 0 ")
    int countExpressCompany();

}
