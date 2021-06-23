package com.wanmi.sbc.message.smssenddetail.repository;

import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>短信发送DAO</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@Repository
public interface SmsSendDetailRepository extends JpaRepository<SmsSendDetail, Long>,
        JpaSpecificationExecutor<SmsSendDetail> {

}
