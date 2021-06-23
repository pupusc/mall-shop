package com.wanmi.sbc.message.smssign.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.SmsBaseResponse;
import com.wanmi.sbc.message.SmsProxy;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingQueryRequest;
import com.wanmi.sbc.message.api.request.smssign.SmsSignQueryRequest;
import com.wanmi.sbc.message.api.request.smssign.SyncSmsSignByNamesRequest;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoQueryRequest;
import com.wanmi.sbc.message.bean.constant.SmsResponseCode;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.enums.SignSource;
import com.wanmi.sbc.message.bean.vo.SmsSignVO;
import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import com.wanmi.sbc.message.smssetting.repository.SmsSettingRepository;
import com.wanmi.sbc.message.smssetting.service.SmsSettingWhereCriteriaBuilder;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smssign.repository.SmsSignRepository;
import com.wanmi.sbc.message.smssignfileinfo.model.root.SmsSignFileInfo;
import com.wanmi.sbc.message.smssignfileinfo.repository.SmsSignFileInfoRepository;
import com.wanmi.sbc.message.smssignfileinfo.service.SmsSignFileInfoWhereCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>短信签名业务逻辑</p>
 *
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@Service("SmsSignService")
public class SmsSignService {
    @Autowired
    private SmsSignRepository smsSignRepository;

    @Autowired
    private SmsSignFileInfoRepository smsSignFileInfoRepository;

    @Autowired
    private SmsSettingRepository smsSettingRepository;

    @Autowired
    private SmsProxy smsProxy;

    /**
     * 新增短信签名
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public SmsSign add(SmsSign entity) {
        SmsBaseResponse smsBaseResponse = smsProxy.addSmsSign(entity);
        if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
            SmsSetting smsSetting = smsSettingRepository.findOne(SmsSettingWhereCriteriaBuilder.build(SmsSettingQueryRequest.builder()
                    .delFlag(DeleteFlag.NO)
                    .status(EnableStatus.ENABLE)
                    .build())).orElse(null);
            entity.setSmsSettingId(smsSetting.getId());
            smsSignRepository.save(entity);
            addSmsSignFileInfo(entity);
        } else {
            throw new SbcRuntimeException("K-300203", new Object[]{"新增失败，调用短信平台报错：" + smsBaseResponse.getMessage()});
        }
        return entity;
    }

    /**
     * 修改短信签名
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public SmsSign modify(SmsSign entity) {
        SmsBaseResponse smsBaseResponse = smsProxy.modifySmsSign(entity);
        if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
            entity.setReviewStatus(ReviewStatus.PENDINGREVIEW);
            smsSignRepository.save(entity);
            addSmsSignFileInfo(entity);
        } else {
            throw new SbcRuntimeException("K-300203", new Object[]{"编辑失败，调用短信平台报错：" + smsBaseResponse.getMessage()});
        }
        return entity;
    }

    /**
     * @return com.wanmi.sbc.message.smssign.model.root.SmsSign
     * @Author lvzhenwei
     * @Description 根据模板名称更新模板审核状态
     * @Date 16:27 2019/12/6
     * @Param []
     **/
    @Transactional(rollbackFor = Exception.class)
    public void modifyReviewStatusByName(SmsSign entity) {
        SmsBaseResponse smsBaseResponse = smsProxy.querySmsSign(entity);
        if(smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)){
            if(ReviewStatus.fromValue(Integer.valueOf(smsBaseResponse.getSignStatus()))==ReviewStatus.REVIEWFAILED){
                entity.setReviewReason(smsBaseResponse.getReason());
            }
        }
        smsSignRepository.modifyReviewStatusByname(entity.getReviewStatus(), entity.getReviewReason(), entity.getSmsSignName());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 保存短信签名文件信息
     * @Date 10:37 2019/12/5
     * @Param [entity]
     **/
    private void addSmsSignFileInfo(SmsSign entity) {
        List<SmsSignFileInfo> smsSignFileInfoList = entity.getSmsSignFileInfoList().stream().map(smsSignFileInfo -> {
            return SmsSignFileInfo.builder()
                    .createTime(LocalDateTime.now())
                    .delFlag(DeleteFlag.NO)
                    .fileName(smsSignFileInfo.getFileName())
                    .fileUrl(smsSignFileInfo.getFileUrl())
                    .smsSignId(entity.getId())
                    .build();
        }).collect(Collectors.toList());
        smsSignFileInfoRepository.saveAll(smsSignFileInfoList);
    }

    /**
     * 单个删除短信签名
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        SmsBaseResponse smsBaseResponse = smsProxy.deleteSmsSign(smsSignRepository.findById(id).orElse(null));
        if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
            smsSignRepository.deleteByBeanId(id);
        } else {
            throw new SbcRuntimeException("K-300203", new Object[]{"删除失败，调用短信平台报错：" + smsBaseResponse.getMessage()});
        }
    }

    /**
     * 批量删除短信签名
     *
     * @author lvzhenwei
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdList(List<Long> ids) {
        smsSignRepository.deleteByIdList(ids);
    }

    /**
     * 单个查询短信签名
     *
     * @author lvzhenwei
     */
    public SmsSign getById(Long id) {
        SmsSign smsSign = smsSignRepository.findById(id).orElse(null);
        List<SmsSignFileInfo> smsSignFileInfoList = smsSignFileInfoRepository.findAll(SmsSignFileInfoWhereCriteriaBuilder.build(
                SmsSignFileInfoQueryRequest.builder()
                        .smsSignId(id)
                        .build()));
        smsSign.setSmsSignFileInfoList(smsSignFileInfoList);
        return smsSign;
    }

    /**
     * 单个查询短信签名
     *
     * @author lvzhenwei
     */
    public SmsSign findOne(Long id) {
        return smsSignRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询短信签名
     *
     * @author lvzhenwei
     */
    public Page<SmsSign> page(SmsSignQueryRequest queryReq) {
        return smsSignRepository.findAll(
                SmsSignWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询短信签名
     *
     * @author lvzhenwei
     */
    public List<SmsSign> list(SmsSignQueryRequest queryReq) {
        return smsSignRepository.findAll(
                SmsSignWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 同步短信平台短信签名信息
     * @Date 19:38 2019/12/9
     * @Param [entity]
     **/
    @Transactional(rollbackFor = Exception.class)
    public void synchronizePlatformSmsSign() {
        SmsSignQueryRequest smsSignQueryRequest = SmsSignQueryRequest.builder().delFlag(DeleteFlag.NO).build();
        smsSignQueryRequest.setPageNum(0);
        smsSignQueryRequest.setPageSize(10);
        Page<SmsSign> smsSignPage = page(smsSignQueryRequest);
        int totalPages = (int) smsSignPage.getTotalElements();
        for (int i = 0; i < totalPages; i++) {
            smsSignQueryRequest.setPageNum(i);
            smsSignPage = page(smsSignQueryRequest);
            synchronizeSmsSign(smsSignPage.getContent());
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 短信签名同步
     * @Date 14:17 2019/12/11
     * @Param [smsSignList]
     **/
    private void synchronizeSmsSign(List<SmsSign> smsSignList) {
        for (SmsSign smsSign : smsSignList) {
            SmsBaseResponse smsBaseResponse = smsProxy.querySmsSign(smsSign);
            if (smsBaseResponse.getCode().equals(SmsResponseCode.SMS_SIGNATURE_ILLEGAL) && Objects.nonNull(smsSign)) {
                smsSign.setDelFlag(DeleteFlag.YES);
                smsSignRepository.save(smsSign);
            } else if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
                if (smsBaseResponse.getSignStatus().equals(String.valueOf(ReviewStatus.REVIEWPASS.toValue()))) {
                    smsSign.setReviewStatus(ReviewStatus.REVIEWPASS);
                    smsSignRepository.save(smsSign);
                } else if (smsBaseResponse.getSignStatus().equals(String.valueOf(ReviewStatus.PENDINGREVIEW.toValue()))) {
                    smsSign.setReviewStatus(ReviewStatus.PENDINGREVIEW);
                    smsSignRepository.save(smsSign);
                } else if (smsBaseResponse.getSignStatus().equals(String.valueOf(ReviewStatus.REVIEWFAILED.toValue()))) {
                    smsSign.setReviewStatus(ReviewStatus.REVIEWFAILED);
                    smsSign.setReviewReason(smsBaseResponse.getReason());
                    smsSignRepository.save(smsSign);
                }
            }
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 同步短信平台历史短信签名数据
     * @Date 17:14 2019/12/11
     * @Param [synchronizeSmsSignByNamesRequest]
     **/
    @Transactional(rollbackFor = Exception.class)
    public void syncPlatformHistorySmsSignByNames(SyncSmsSignByNamesRequest synchronizeSmsSignByNamesRequest) {
        List<String> signNameList = synchronizeSmsSignByNamesRequest.getSignNameList();
        for (String signName : signNameList) {
            SmsSign smsSign = smsSignRepository.findOne(SmsSignWhereCriteriaBuilder.build(SmsSignQueryRequest.builder().smsSignName(signName).build())).orElse(null);
            //如果签名名称在数据库表不存在进行同步
            if (Objects.isNull(smsSign)) {
                SmsSetting smsSetting = smsSettingRepository.findOne(SmsSettingWhereCriteriaBuilder.build(
                        SmsSettingQueryRequest.builder()
                                .delFlag(DeleteFlag.NO)
                                .status(EnableStatus.ENABLE)
                                .build())).orElse(null);
                SmsSign smsSignInfo = new SmsSign();
                smsSignInfo.setSmsSettingId(smsSetting.getId());
                smsSignInfo.setSmsSignName(signName);
                smsSignInfo.setSignSource(SignSource.ECOMMERCEPLATFORMSTORENAME);
                smsSignInfo.setDelFlag(DeleteFlag.NO);
                SmsBaseResponse smsBaseResponse = smsProxy.querySmsSign(smsSignInfo);
                if (smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
                    ReviewStatus reviewStatus = ReviewStatus.fromValue(Integer.valueOf(smsBaseResponse.getSignStatus()));
                    smsSignInfo.setReviewStatus(reviewStatus);
                    if (reviewStatus == ReviewStatus.REVIEWFAILED) {
                        smsSignInfo.setReviewReason(smsBaseResponse.getReason());
                    }
                    smsSignInfo.setCreateTime(DateUtil.parse(smsBaseResponse.getCreateDate(), DateUtil.FMT_TIME_1));
                    smsSignRepository.save(smsSignInfo);
                }
            }
        }
    }

    /**
     * @Author lvzhenwei
     * @Description 根据签名名称获取签名数据
     * @Date 14:30 2020/1/2
     * @Param [request]
     * @return java.util.List<com.wanmi.sbc.message.smssign.model.root.SmsSign>
     **/
    public List<SmsSign> getBySmsSignNameAndAndDelFlag(SmsSignQueryRequest request){
        return smsSignRepository.findAllBySmsSignNameAndAndDelFlag(request.getSmsSignName(),DeleteFlag.NO);
    }

    /**
     * 将实体包装成VO
     *
     * @author lvzhenwei
     */
    public SmsSignVO wrapperVo(SmsSign smsSign) {
        if (smsSign != null) {
            SmsSignVO smsSignVO = KsBeanUtil.convert(smsSign, SmsSignVO.class);
            return smsSignVO;
        }
        return null;
    }
}
