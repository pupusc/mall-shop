package com.wanmi.sbc.message.smssend.repository;

import com.wanmi.sbc.message.smssend.model.root.SmsSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * <p>短信发送DAO</p>
 *
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@Repository
public interface SmsSendRepository extends JpaRepository<SmsSend, Long>,
        JpaSpecificationExecutor<SmsSend> {

    /**
     * 删除模板时判断模板是否被使用
     *
     * @param templateCode
     * @return
     */
    @Query(value = "select count(id) from sms_send where template_code = ?1 " +
            "and (status = 0 or status = 1  or (status = 3 and resend_type = 1))", nativeQuery = true)
    int countNumByTemplateCodeUsed(String templateCode);

}
