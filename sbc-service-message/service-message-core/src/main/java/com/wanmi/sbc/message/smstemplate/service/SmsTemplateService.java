package com.wanmi.sbc.message.smstemplate.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.SmsBaseResponse;
import com.wanmi.sbc.message.SmsProxy;
import com.wanmi.sbc.message.api.constant.SmsErrorCode;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingQueryRequest;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateQueryRequest;
import com.wanmi.sbc.message.api.request.smstemplate.SyncPlatformHistorySmsTemplateRequest;
import com.wanmi.sbc.message.bean.constant.SmsResponseCode;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.enums.SmsType;
import com.wanmi.sbc.message.bean.vo.SmsTemplateVO;
import com.wanmi.sbc.message.smssend.repository.SmsSendRepository;
import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import com.wanmi.sbc.message.smssetting.repository.SmsSettingRepository;
import com.wanmi.sbc.message.smssetting.service.SmsSettingWhereCriteriaBuilder;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import com.wanmi.sbc.message.smstemplate.repository.SmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>短信模板业务逻辑</p>
 *
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@Service("SmsTemplateService")
public class SmsTemplateService {

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Autowired
    private SmsSettingRepository smsSettingRepository;

    @Autowired
    private SmsSendRepository smsSendRepository;

    @Autowired
    private SmsProxy smsProxy;

    /**
     * 新增短信模板
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public SmsTemplate add(SmsTemplate entity) {
        entity.setOpenFlag(Boolean.TRUE);
        SmsBaseResponse smsBaseResponse = smsProxy.addSmsTemplate(entity);
        if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
            entity.setTemplateCode(smsBaseResponse.getTemplateCode());
            SmsSetting smsSetting = smsSettingRepository.findOne(SmsSettingWhereCriteriaBuilder.build(SmsSettingQueryRequest.builder()
                    .delFlag(DeleteFlag.NO)
                    .status(EnableStatus.ENABLE)
                    .build())).orElse(null);
            entity.setSmsSettingId(smsSetting.getId());
            smsTemplateRepository.save(entity);
        } else {
            throw new SbcRuntimeException("K-300303", new Object[]{"新增失败，调用短信平台报错：" + smsBaseResponse.getMessage()});
        }
        return entity;
    }

    /**
     * 提交备案
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public void upload(SmsTemplate entity) {
        SmsBaseResponse smsBaseResponse = smsProxy.addSmsTemplate(entity);
        if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
            entity.setTemplateCode(smsBaseResponse.getTemplateCode());
            entity.setReviewStatus(ReviewStatus.PENDINGREVIEW);
            smsTemplateRepository.save(entity);
        } else {
            throw new SbcRuntimeException("K-300303", new Object[]{"提交备案失败，调用短信平台报错：" + smsBaseResponse.getMessage()});
        }
    }

    /**
     * 修改短信模板
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public SmsTemplate modify(SmsTemplate entity) {
        SmsTemplate smsTemplateInfo = smsTemplateRepository.findById(entity.getId()).orElse(null);
        if(Objects.isNull(smsTemplateInfo)){
            throw new SbcRuntimeException(SmsErrorCode.SMS_TEMPLATE_NOT_EXISTS);
        }
        //如果是待提交备案
        if(ReviewStatus.WAITSUBMIT.equals(smsTemplateInfo.getReviewStatus())){
            smsTemplateInfo.setTemplateContent(entity.getTemplateContent());
            smsTemplateInfo.setTemplateName(entity.getTemplateName());
            smsTemplateInfo.setTemplateType(entity.getTemplateType());
            smsTemplateInfo.setRemark(entity.getRemark());
            smsTemplateInfo.setReviewStatus(ReviewStatus.WAITSUBMIT);
            smsTemplateInfo.setSignId(entity.getSignId());
            smsTemplateInfo.setBusinessType(entity.getBusinessType());
            smsTemplateInfo.setPurpose(entity.getPurpose());
            smsTemplateRepository.save(smsTemplateInfo);
            return smsTemplateInfo;
        }
        SmsBaseResponse smsBaseResponse = smsProxy.modifySmsTemplate(entity);
        if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
            entity.setTemplateCode(smsBaseResponse.getTemplateCode());
            smsTemplateInfo.setTemplateContent(entity.getTemplateContent());
            smsTemplateInfo.setTemplateName(entity.getTemplateName());
            smsTemplateInfo.setTemplateType(entity.getTemplateType());
            smsTemplateInfo.setRemark(entity.getRemark());
            smsTemplateInfo.setReviewStatus(ReviewStatus.PENDINGREVIEW);
            if (entity.getTemplateType() == SmsType.VERIFICATIONCODE) {
                smsTemplateInfo.setSignId(entity.getSignId());
                smsTemplateInfo.setBusinessType(entity.getBusinessType());
                smsTemplateInfo.setPurpose(entity.getPurpose());
            }
            smsTemplateRepository.save(smsTemplateInfo);
        } else {
            throw new SbcRuntimeException("K-300303", new Object[]{"编辑失败，调用短信平台报错：" + smsBaseResponse.getMessage()});
        }
        return entity;
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 根据模板名称更新模板审核状态
     * @Date 16:48 2019/12/6
     * @Param [entity]
     **/
    @Transactional(rollbackFor = Exception.class)
    public void modifySmsTemplateReviewStatusByTemplateCode(SmsTemplate entity) {
        SmsBaseResponse smsBaseResponse = smsProxy.querySmsTemplate(entity);
        if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
            ReviewStatus reviewStatus = ReviewStatus.fromValue(Integer.valueOf(smsBaseResponse.getTemplateStatus()));
            if (reviewStatus == ReviewStatus.REVIEWFAILED) {
                entity.setReviewReason(smsBaseResponse.getReason());
            }
        }
        smsTemplateRepository.modifySmsTemplateReviewStatusByTemplateCode(entity.getReviewStatus(), entity.getReviewReason(), entity.getTemplateCode());
    }

    /**
     * 单个删除短信模板
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        SmsTemplate smsTemplate = smsTemplateRepository.findById(id).orElse(null);
        if (smsTemplate.getDelFlag() == DeleteFlag.YES) {
            throw new SbcRuntimeException("K-300303", new Object[]{"删除失败，该模板删除"});
        }
        //判断短信模板是否已经被用
        int num = smsSendRepository.countNumByTemplateCodeUsed(smsTemplate.getTemplateCode());
        if (num > 0) {
            throw new SbcRuntimeException("K-300303", new Object[]{"删除失败，该模板已被使用，无法删除"});
        }
        SmsBaseResponse smsBaseResponse = smsProxy.deleteSmsTemplate(smsTemplate);
        if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
            smsTemplateRepository.deleteByBeanId(id);
        } else {
            throw new SbcRuntimeException("K-300303", new Object[]{"删除失败，调用短信平台报错：" + smsBaseResponse.getMessage()});
        }
    }

    /**
     * 批量删除短信模板
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdList(List<Long> ids) {
        smsTemplateRepository.deleteByIdList(ids);
    }

    /**
     * 单个查询短信模板
     *
     * @author lvzhenwei
     */
    public SmsTemplate getById(Long id) {
        return smsTemplateRepository.findById(id).orElse(null);
    }

    /**
     * @return com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate
     * @Author lvzhenwei
     * @Description 根据模板code查询模板信息
     * @Date 11:48 2019/12/9
     * @Param [templateCode]
     **/
    public SmsTemplate findByTemplateCode(String templateCode) {
        return smsTemplateRepository.findByTemplateCode(templateCode);
    }

    /**
     * 分页查询短信模板
     *
     * @author lvzhenwei
     */
    public Page<SmsTemplate> page(SmsTemplateQueryRequest queryReq) {
        return smsTemplateRepository.findAll(
                SmsTemplateWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询短信模板
     *
     * @author lvzhenwei
     */
    public List<SmsTemplate> list(SmsTemplateQueryRequest queryReq) {
        return smsTemplateRepository.findAll(
                SmsTemplateWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 统计短信模板
     *
     * @author lvzhenwei
     */
    public Long count(SmsTemplateQueryRequest queryReq) {
        return smsTemplateRepository.count(SmsTemplateWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 同步短信平台短信签名信息
     * @Date 19:38 2019/12/9
     * @Param [entity]
     **/
    @Transactional(rollbackFor = Exception.class)
    public void synchronizePlatformSmsTemplate() {
        SmsTemplateQueryRequest smsTemplateQueryRequest = SmsTemplateQueryRequest.builder().delFlag(DeleteFlag.NO).build();
        smsTemplateQueryRequest.setPageNum(0);
        smsTemplateQueryRequest.setPageSize(100);
        Page<SmsTemplate> smsTemplatePage = page(smsTemplateQueryRequest);
        int totalPages = smsTemplatePage.getTotalPages();
        for (int i = 0; i < totalPages; i++) {
            smsTemplateQueryRequest.setPageNum(i);
            smsTemplatePage = page(smsTemplateQueryRequest);
            synchronizeSmsTemplate(smsTemplatePage.getContent());

        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 模板同步操作
     * @Date 15:25 2019/12/11
     * @Param [smsTemplateList]
     **/
    private void synchronizeSmsTemplate(List<SmsTemplate> smsTemplateList) {
        for (SmsTemplate smsTemplate : smsTemplateList) {
            if (Objects.nonNull(smsTemplate.getTemplateCode())) {
                SmsBaseResponse smsBaseResponse = smsProxy.querySmsTemplate(smsTemplate);
                if (smsBaseResponse.getCode().equals(SmsResponseCode.SMS_TEMPLATE_ILLEGAL)) {
                    smsTemplate.setDelFlag(DeleteFlag.YES);
                    smsTemplateRepository.save(smsTemplate);
                } else if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
                    smsTemplate.setTemplateName(smsBaseResponse.getTemplateName());
                    smsTemplate.setTemplateContent(smsBaseResponse.getTemplateContent());
                    smsTemplate.setTemplateType(SmsType.fromValue(Integer.valueOf(smsBaseResponse.getTemplateType())));
                    ReviewStatus reviewStatus = ReviewStatus.fromValue(Integer.valueOf(smsBaseResponse.getTemplateStatus()));
                    smsTemplate.setReviewStatus(reviewStatus);
                    if (reviewStatus == ReviewStatus.REVIEWFAILED) {
                        smsTemplate.setReviewReason(smsBaseResponse.getReason());
                    }
                    smsTemplateRepository.save(smsTemplate);
                }
            }
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 同步短信平台短信模板数据
     * @Date 19:11 2019/12/11
     * @Param [request]
     **/
    @Transactional(rollbackFor = Exception.class)
    public void synPlatformHistorySmsTemplate(SyncPlatformHistorySmsTemplateRequest request) {
        for (String templateCode : request.getTemplateCodeList()) {
            SmsTemplate smsTemplate = smsTemplateRepository.findByTemplateCode(templateCode);
            if (Objects.isNull(smsTemplate)) {
                SmsTemplate smsTemplateInfo = new SmsTemplate();
                smsTemplateInfo.setTemplateCode(templateCode);
                SmsBaseResponse smsBaseResponse = smsProxy.querySmsTemplate(smsTemplateInfo);
                if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
                    smsTemplateInfo.setTemplateCode(templateCode);
                    smsTemplateInfo.setDelFlag(DeleteFlag.NO);
                    smsTemplateInfo.setTemplateName(smsBaseResponse.getTemplateName());
                    smsTemplateInfo.setTemplateContent(smsBaseResponse.getTemplateContent());
                    smsTemplateInfo.setTemplateType(SmsType.fromValue(Integer.valueOf(smsBaseResponse.getTemplateType())));
                    ReviewStatus reviewStatus = ReviewStatus.fromValue(Integer.valueOf(smsBaseResponse.getTemplateStatus()));
                    smsTemplateInfo.setReviewStatus(reviewStatus);
                    if (reviewStatus == ReviewStatus.REVIEWFAILED) {
                        smsTemplateInfo.setReviewReason(smsBaseResponse.getReason());
                    }
                    smsTemplateInfo.setCreateTime(DateUtil.parse(smsBaseResponse.getCreateDate(), DateUtil.FMT_TIME_1));
                    smsTemplateRepository.save(smsTemplateInfo);
                }
            }
        }
    }

    /**
     * 将实体包装成VO
     *
     * @author lvzhenwei
     */
    public SmsTemplateVO wrapperVo(SmsTemplate smsTemplate) {
        if (smsTemplate != null) {
            SmsTemplateVO smsTemplateVO = new SmsTemplateVO();
            KsBeanUtil.copyPropertiesThird(smsTemplate, smsTemplateVO);
            return smsTemplateVO;
        }
        return null;
    }

    /**
     * 更新单个短信模板开关
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyOpenFlagById(Long id, Boolean openFlag) {
        smsTemplateRepository.modifyOpenFlagById(id, openFlag);
    }
}
