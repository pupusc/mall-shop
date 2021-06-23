package com.wanmi.sbc.customer.storereturnaddress.repository;

import com.wanmi.sbc.customer.storereturnaddress.model.root.StoreReturnAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>店铺退货地址表DAO</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@Repository
public interface StoreReturnAddressRepository extends JpaRepository<StoreReturnAddress, String>,
        JpaSpecificationExecutor<StoreReturnAddress> {

    /**
     * 单个删除店铺退货地址表
     * @author dyt
     */
    @Modifying
    @Query("update StoreReturnAddress set delFlag = 1 where addressId = ?1")
    void deleteById(String addressId);

    /**
     * 批量删除店铺退货地址表
     * @author dyt
     */
    @Modifying
    @Query("update StoreReturnAddress set delFlag = 1 where addressId in ?1")
    void deleteByIdList(List<String> addressIdList);

    Optional<StoreReturnAddress> findByAddressIdAndStoreIdAndDelFlag(String id, Long storeId, DeleteFlag delFlag);

}
