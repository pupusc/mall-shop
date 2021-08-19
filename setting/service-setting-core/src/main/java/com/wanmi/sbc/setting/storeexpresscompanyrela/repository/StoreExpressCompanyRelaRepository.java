package com.wanmi.sbc.setting.storeexpresscompanyrela.repository;

import com.wanmi.sbc.setting.storeexpresscompanyrela.model.root.StoreExpressCompanyRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>店铺快递公司关联表DAO</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@Repository
public interface StoreExpressCompanyRelaRepository extends JpaRepository<StoreExpressCompanyRela, Long>,
        JpaSpecificationExecutor<StoreExpressCompanyRela> {

    List<StoreExpressCompanyRela> findStoreExpressCompanyRelasByStoreId(Long storeId);

}
