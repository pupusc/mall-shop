package com.wanmi.sbc.setting.email;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.EmailConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.EmailConfigQueryResponse;
import com.wanmi.sbc.setting.bean.enums.EmailStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 客户邮箱配置服务类
 */
@Service
public class EmailConfigService {

    @Autowired
    private EmailConfigRepository emailConfigRepository;

    private static Integer MAX_CUSTOMER_EMAIL_COUNT = 5;

    /**
     * 查询BOSS管理后台邮箱接口配置
     *
     * @return
     */
    public EmailConfigQueryResponse getSystemEmailConfig() {
        List<EmailConfig> configs = emailConfigRepository.findEmailConfigByDelFlag(DeleteFlag.NO);
        EmailConfig emailConfig = new EmailConfig();
        if (CollectionUtils.isEmpty(configs)) {
            // 如果数据库里无数据，初始化
            emailConfig.setStatus(EmailStatus.DISABLE);
            emailConfig.setDelFlag(DeleteFlag.NO);
            emailConfig.setCreateTime(LocalDateTime.now());
            emailConfig = emailConfigRepository.saveAndFlush(emailConfig);
        } else {
            emailConfig = configs.get(0);
        }
        EmailConfigQueryResponse response = new EmailConfigQueryResponse();
        KsBeanUtil.copyProperties(emailConfig, response);
        return response;
    }

    /**
     * 修改BOSS管理后台邮箱接口配置
     *
     * @param request
     * @return
     */
    @Transactional
    public EmailConfigQueryResponse modifyCustomerEmail(EmailConfigModifyRequest request) {

        // 根据邮箱配置id查询详情信息
        EmailConfig emailConfig = emailConfigRepository.findEmailConfigByEmailConfigIdAndDelFlag(
                request.getEmailConfigId(), DeleteFlag.NO);
        // 邮箱配置不存在
        if (Objects.isNull(emailConfig)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        KsBeanUtil.copyProperties(request, emailConfig);
        emailConfig = emailConfigRepository.save(emailConfig);

        EmailConfigQueryResponse response = new EmailConfigQueryResponse();
        KsBeanUtil.copyProperties(emailConfig, response);
        return response;
    }

}
