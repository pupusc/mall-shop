package com.wanmi.sbc.setting.thirdaddress.repository;

import com.wanmi.sbc.setting.thirdaddress.model.root.ThirdAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>第三方地址映射表DAO</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@Repository
public interface ThirdAddressRepository extends JpaRepository<ThirdAddress, String>,
        JpaSpecificationExecutor<ThirdAddress> {

    Optional<ThirdAddress> findByIdAndDelFlag(String id, DeleteFlag delFlag);

}
