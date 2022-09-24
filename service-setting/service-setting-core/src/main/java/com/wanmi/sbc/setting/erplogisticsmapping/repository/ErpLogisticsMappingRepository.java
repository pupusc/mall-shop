package com.wanmi.sbc.setting.erplogisticsmapping.repository;

import com.wanmi.sbc.setting.erplogisticsmapping.model.root.ErpLogisticsMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>erp系统物流编码映射DAO</p>
 * @author weiwenhao
 * @date 2021-04-03 13:38:51
 */
@Repository
public interface ErpLogisticsMappingRepository extends JpaRepository<ErpLogisticsMapping, Integer>,
        JpaSpecificationExecutor<ErpLogisticsMapping> {


    ErpLogisticsMapping findByErpLogisticsCode(String erpLogisticsCode);

    List<ErpLogisticsMapping> findByWmLogisticsCode(String wmLogisticsCode);
}
