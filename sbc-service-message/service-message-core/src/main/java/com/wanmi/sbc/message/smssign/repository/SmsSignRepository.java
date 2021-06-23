package com.wanmi.sbc.message.smssign.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>短信签名DAO</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@Repository
public interface SmsSignRepository extends JpaRepository<SmsSign, Long>,
        JpaSpecificationExecutor<SmsSign> {

    List<SmsSign> findAllBySmsSignNameAndAndDelFlag(String signName, DeleteFlag deleteFlag);

    /**
     * 根据模板名称更新模板审核状态
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsSign set reviewStatus = ?1,reviewReason = ?2 where smsSignName = ?3")
    int modifyReviewStatusByname(ReviewStatus reviewStatus,String reviewReason,String smsSignName);

    /**
     * 单个删除短信签名
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsSign set delFlag = 1 where id = ?1")
    int deleteByBeanId(Long id);

    /**
     * 批量删除短信签名
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsSign set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

}
