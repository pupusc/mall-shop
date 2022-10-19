package com.wanmi.sbc.pay.repository;

import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sunkun on 2017/8/3.
 */
@Repository
public interface GatewayConfigRepository extends JpaRepository<PayGatewayConfig, Long> {

    @Query("select p from PayGatewayConfig p where p.payGateway.id = ?1 and p.storeId = ?2 ")
    PayGatewayConfig queryConfigByGatwayIdAndStoreId(Long gatwayId,Long storeId);

    @Query("select p from PayGatewayConfig p where p.payGateway.isOpen = 1 and p.storeId = ?1")
    List<PayGatewayConfig> queryConfigByOpenAndStoreId(Long storeId);

    @Query("select p from PayGatewayConfig p where p.payGateway.isOpen = 1 and p.payGateway.name=?1  and p.storeId = ?2 ")
    PayGatewayConfig queryConfigByNameAndStoreId(PayGatewayEnum payGatewayEnum,Long storeId);

    @Query("select p from PayGatewayConfig p where p.payGateway.isOpen = 1 and p.payGateway.name=?1  and p.storeId = ?2 ")
    List<PayGatewayConfig> queryConfigOpenByNameAndStoreId(PayGatewayEnum payGatewayEnum,Long storeId);

    // todo 退款改造完 queryConfigByName 用 queryConfigByNameAndStoreId 代替
    @Query("select p from PayGatewayConfig p where p.payGateway.name=?1")
    PayGatewayConfig queryConfigByName(PayGatewayEnum payGatewayEnum);

    @Modifying
    @Query("update PayGatewayConfig p set p.appId = ?3, p.secret =?4 where p.payGateway.name=?1  and p.storeId = ?2 ")
    PayGatewayConfig updateMobileKey(PayGatewayEnum payGatewayEnum,Long storeId, String apiKey, String secret);

    @Modifying
    @Query("update PayGatewayConfig p set p.openPlatformAppId = ?3, p.openPlatformSecret =?4 where p.payGateway.name=?1  and p.storeId = ?2 ")
    PayGatewayConfig updateAppKey(PayGatewayEnum payGatewayEnum,Long storeId, String apiKey, String secret);


    @Query("select p from PayGatewayConfig p where p.payGateway.isOpen = 1 and p.appId = ?1 and p.storeId = ?2")
    List<PayGatewayConfig> queryConfigByAppIdAndStoreId(String appId, Long storeId);

    @Query("select p from PayGatewayConfig p where p.payGateway.isOpen = 1 and p.appId = ?1 and p.account = ?2 and p.storeId = ?3")
    List<PayGatewayConfig> queryConfigByAppIdAndMchIdAndStoreId(String appId, String mchId, Long storeId);

}
