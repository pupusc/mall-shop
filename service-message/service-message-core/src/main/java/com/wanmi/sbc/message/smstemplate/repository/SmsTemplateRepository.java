package com.wanmi.sbc.message.smstemplate.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>短信模板DAO</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@Repository
public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, Long>,
        JpaSpecificationExecutor<SmsTemplate> {

    SmsTemplate findByTemplateCode(String templateCode);

    /**
     * 根据业务类型获取模板
     * @param businessType 业务类型
     * @return
     */
    SmsTemplate findByBusinessTypeAndDelFlag(String businessType, DeleteFlag deleteFlag);

    /**
     * 根据模板名称更新模板审核状态
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsTemplate set reviewStatus = ?1,reviewReason = ?2 where templateCode = ?3")
    int modifySmsTemplateReviewStatusByTemplateCode(ReviewStatus reviewStatus, String reviewReason, String templateCode);

    /**
     * 单个删除短信模板
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsTemplate set delFlag = 1 where id = ?1")
    int deleteByBeanId(Long id);

    /**
     * 批量删除短信模板
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsTemplate set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

    /**
     * 单个删除短信模板
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsTemplate set openFlag = ?2 where id = ?1")
    void modifyOpenFlagById(Long id, Boolean openFlag);
}
