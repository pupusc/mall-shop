package com.wanmi.sbc.customer.quicklogin.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.quicklogin.model.root.WeChatQuickLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-22 15:53
 **/
@Repository
public interface WeChatQuickLoginRepository extends JpaRepository<WeChatQuickLogin, String> {

    Optional<WeChatQuickLogin> findByOpenIdAndDelFlag(String openId, DeleteFlag deleteFlag);

}
