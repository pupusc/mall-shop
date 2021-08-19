package com.wanmi.sbc.message.umengtoken.repository;

import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.umengtoken.model.root.UmengToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * <p>友盟推送设备与会员关系DAO</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@Repository
public interface UmengTokenRepository extends JpaRepository<UmengToken, Long>,
        JpaSpecificationExecutor<UmengToken> {

    Optional<List<UmengToken>> queryByPlatform(PushPlatform pushPlatform);

    Optional<UmengToken> queryByCustomerIdAndDevlceToken(String customerId, String token);

    Optional<UmengToken> queryByCustomerId(String customerId);

    Optional<List<UmengToken>> queryByCustomerIdIn(List<String> customerIds);

}
