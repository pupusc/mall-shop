package com.wanmi.sbc.pay.repository;

import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.model.root.PayGateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sunkun on 2017/8/3.
 */
@Repository
public interface GatewayRepository extends JpaRepository<PayGateway, Long>, JpaSpecificationExecutor<PayGateway> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update PayGateway set isOpen=:isOpen,type=:type,name=:name where id=:id")
    int update(@Param("id") Long id, @Param("isOpen") IsOpen isOpen, @Param("type") Boolean type, @Param("name") PayGatewayEnum
            name);

    @Query("select p from PayGateway p where p.name=?1  and p.storeId = ?2 ")
    PayGateway queryByNameAndStoreId(PayGatewayEnum payGatewayEnum, Long storeId);


    List<PayGateway> findByStoreId(Long storeId);
}
