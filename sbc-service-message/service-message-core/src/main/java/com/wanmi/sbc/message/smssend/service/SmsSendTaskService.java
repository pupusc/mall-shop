package com.wanmi.sbc.message.smssend.service;

import com.google.common.collect.Lists;
import com.wanmi.ares.provider.CustomerBaseQueryProvider;
import com.wanmi.ares.request.CustomerQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.SmsBaseResponse;
import com.wanmi.sbc.message.SmsProxy;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailQueryRequest;
import com.wanmi.sbc.message.api.request.smssign.SmsSignQueryRequest;
import com.wanmi.sbc.message.bean.constant.ReceiveGroupType;
import com.wanmi.sbc.message.bean.constant.SmsResponseCode;
import com.wanmi.sbc.message.bean.enums.ReceiveType;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.enums.SendDetailStatus;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import com.wanmi.sbc.message.smssend.model.root.SmsSend;
import com.wanmi.sbc.message.smssend.repository.SmsSendRepository;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import com.wanmi.sbc.message.smssenddetail.service.SmsSendDetailService;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smssign.repository.SmsSignRepository;
import com.wanmi.sbc.message.smssign.service.SmsSignWhereCriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发送任务服务
 * 除了sendDetail方法，其他均无用，迁移至boss的bff下
 * @see com.wanmi.sbc.mq.SmsSendTaskService
 */
@Service
@Slf4j
public class SmsSendTaskService {
    @Autowired
    private SmsProxy smsProxy;
    @Autowired
    CustomerBaseQueryProvider customerBaseQueryProvider;
    @Autowired
    private SmsSendDetailService smsSendDetailService;
    @Autowired
    private SmsSendRepository smsSendRepository;
    @Autowired
    private SmsSignRepository smsSignRepository;
    /*@Autowired
    private CrmGroupProvider crmGroupProvider;*/

    private final long PAGE_SIZE=1000L;


   // @Async("myAsyncPool")
    @Deprecated
    public void send(SmsSend smsSend){
        SmsSign smsSign = this.smsSignRepository.findOne(SmsSignWhereCriteriaBuilder
                .build(
                        SmsSignQueryRequest
                                .builder()
                                .id(smsSend.getSignId())
                                .delFlag(DeleteFlag.NO)
                                .reviewStatus(ReviewStatus.REVIEWPASS)
                                .build()
                )
        ).orElse(null);
        if(smsSend.getSendDetailCount()==null||smsSend.getSendDetailCount()<=0) {
            if (smsSign == null) {
                smsSend.setStatus(SendStatus.FAILED);
                smsSend.setMessage("签名信息不存在或者已删除！");
                this.smsSendRepository.save(smsSend);
                return;
            } else {
                smsSend.setSignName(smsSign.getSmsSignName());
            }
            switch (smsSend.getReceiveType()) {
                case ALL:
                    allProcess(smsSend);
                    break;
                case LEVEL:
                    levelProcess(smsSend);
                    break;
                case GROUP:
                    groupProcess(smsSend);
                    break;
                case CUSTOM:
                    customProcess(smsSend);
                    break;
            }
            this.smsSendRepository.save(smsSend);
        }

        if(sendDetail(smsSend)){
            smsSend.setStatus(SendStatus.END);
            smsSend.setMessage(SmsResponseCode.SUCCESS);

        }
        smsSend.setSendTime(LocalDateTime.now());
        this.smsSendRepository.save(smsSend);
        log.info("发送任务结束：sendId={}",smsSend.getId());
        return;
    }

    /**
     * 全部会员
     * @param smsSend
     */
    @Deprecated
    private void allProcess(SmsSend smsSend){
        int sendDetailCount=0;
        List<String> manualAddList = null;
        if(StringUtils.isNotEmpty(smsSend.getManualAdd())){
            manualAddList = Arrays.asList(smsSend.getManualAdd().split(","));
        }
        CustomerQueryRequest request = new CustomerQueryRequest();
        if(smsSend.getReceiveType().equals(ReceiveType.LEVEL)){
            request.setLevelIds(
                    Arrays.asList(
                            smsSend.getReceiveValue().split(",")
                    ).stream()
                            .map(id->Long.parseLong(id))
                            .collect(Collectors.toList()));
        }
        long totalNum = this.customerBaseQueryProvider.queryCustomerPhoneCount(request);
        if(totalNum>0) {
            request.setPageSize(PAGE_SIZE);

            long pageCount = totalNum % PAGE_SIZE == 0 ? totalNum / PAGE_SIZE : totalNum / PAGE_SIZE + 1;
            for (long pageNum = 0; pageNum < pageCount; pageNum++) {
                request.setPageNum(pageNum);
                List<String> requestList =  this.customerBaseQueryProvider.queryCustomerPhone(request).getContext();
                SmsSendDetail smsSendDetail = new SmsSendDetail();
                smsSendDetail.setSendId(smsSend.getId());
                dataProcess(requestList,manualAddList,smsSendDetail);
            }

        }else {
            SmsSendDetail smsSendDetail = new SmsSendDetail();
            smsSendDetail.setSendId(smsSend.getId());
            if(CollectionUtils.isNotEmpty(manualAddList)){
                saveSendDetail(smsSendDetail,manualAddList);
            }
        }
        long manualAddNum = CollectionUtils.isNotEmpty(manualAddList)?manualAddList.size():0;
        long totalCount = totalNum+manualAddNum;
        sendDetailCount = (int) (totalCount%PAGE_SIZE>0?totalCount/PAGE_SIZE+1:totalCount/PAGE_SIZE);
        smsSend.setSendDetailCount(sendDetailCount);

    }

    /**
     * 按会员等级
     * @param smsSend
     */
    @Deprecated
    private void levelProcess(SmsSend smsSend){
        allProcess(smsSend);
    }

    /**
     * 会员分组
     * @param smsSend
     */
    @Deprecated
    private void groupProcess(SmsSend smsSend){
        int sendDetailCount=0;
        List<String> manualAddList = null;
        if(StringUtils.isNotEmpty(smsSend.getManualAdd())){
            manualAddList = Arrays.asList(smsSend.getManualAdd().split(","));
        }
        List<Long> sysGroupList = new ArrayList<>();
        List<Long> customGroupList = new ArrayList<>();
        for(String str : smsSend.getReceiveValue().split(",")){
            String[] arr = str.split("_");
            if(arr[0].equals(ReceiveGroupType.CUSTOM)){
              //  str = str.replaceAll(ReceiveGroupType.CUSTOM+"_","");
                customGroupList.add(Long.valueOf(arr[1]));
            }
            if(arr[0].equals(ReceiveGroupType.SYS)){
            //    str = str.replaceAll(ReceiveGroupType.SYS+"_","");
                sysGroupList.add(Long.valueOf(arr[1]));
            }
        }
//        CrmGroupRequest request = new CrmGroupRequest();
//        request.setSysGroupList(sysGroupList);
//        request.setCustomGroupList(customGroupList);
        SmsSendDetail smsSendDetail = new SmsSendDetail();
        smsSendDetail.setSendId(smsSend.getId());

//        long totalNum = this.crmGroupProvider.queryCustomerPhoneCount(request).getContext();
        long totalNum = 0;
        if(totalNum>0) {
//            request.setPageSize((int)PAGE_SIZE);
            long pageCount = totalNum % PAGE_SIZE == 0 ? totalNum / PAGE_SIZE : totalNum / PAGE_SIZE + 1;
            for (int pageNum = 0; pageNum < pageCount; pageNum++) {
//                request.setPageNum(pageNum);

//                List<String> requestList = this.crmGroupProvider.queryCustomerPhone(request).getContext();
                List<String> requestList = null;
                dataProcess(requestList,manualAddList,smsSendDetail);
            }

        }else {
            if(CollectionUtils.isNotEmpty(manualAddList)){
                saveSendDetail(smsSendDetail,manualAddList);
            }
        }
        long manualAddNum = CollectionUtils.isNotEmpty(manualAddList)?manualAddList.size():0;
        long totalCount = totalNum+manualAddNum;
        sendDetailCount = (int) (totalCount%PAGE_SIZE>0?totalCount/PAGE_SIZE+1:totalCount/PAGE_SIZE);
        smsSend.setSendDetailCount(sendDetailCount);
    }

    /**
     * 自定义人群
     * @param smsSend
     */
    @Deprecated
    private void customProcess(SmsSend smsSend){
        int sendDetailCount = 0;
        long totalCount = 0L;
        SmsSendDetail smsSendDetail = new SmsSendDetail();
        smsSendDetail.setSendId(smsSend.getId());
        List<String> manualAddList = null;
        if(StringUtils.isNotEmpty(smsSend.getManualAdd())){
            manualAddList = Arrays.asList(smsSend.getManualAdd().split(","));
        }
        if(StringUtils.isNotEmpty(smsSend.getReceiveValue())){
            List<String> customList = new ArrayList<>();
            for(String info : smsSend.getReceiveValue().split(",")){
                if(StringUtils.isNotBlank(info)) {
                    customList.add(info.split(":")[0]);
                }
            }
            if(CollectionUtils.isNotEmpty(manualAddList)){
                duplicateList(customList,manualAddList);
                customList.addAll(manualAddList);
            }
            saveSendDetail(smsSendDetail,customList);
            totalCount=customList.size();
        }else{
            if(CollectionUtils.isNotEmpty(manualAddList)){
                saveSendDetail(smsSendDetail,manualAddList);
                totalCount=manualAddList.size();
            }

        }
        sendDetailCount = (int) (totalCount%PAGE_SIZE>0?totalCount/PAGE_SIZE+1:totalCount/PAGE_SIZE);
        smsSend.setSendDetailCount(sendDetailCount);
        smsSend.setSendDetailCount(sendDetailCount);
    }

    /**
     * 保存发送详情
     * @param smsSendDetail
     * @param list
     */
    @Deprecated
    private void saveSendDetail(SmsSendDetail smsSendDetail,List<String> list){
        List<List<String>> lists = Lists.partition(list, (int) PAGE_SIZE);
        for (List<String> tempList : lists) {
            smsSendDetail.setPhoneNumbers(StringUtils.join(tempList, ","));
            smsSendDetail.setCreateTime(LocalDateTime.now());
            smsSendDetailService.add(smsSendDetail);
        }
    }

    /**
     * 去重
     * @param fixedList
     * @param varList
     */
    @Deprecated
    private void duplicateList(List<String> fixedList,List<String> varList ){
        List<String> duplicateList = new ArrayList<>(fixedList);
     //   duplicateList = fixedList;
        duplicateList.retainAll(varList);
        if (CollectionUtils.isNotEmpty(duplicateList)) {
            varList.removeAll(duplicateList);
        }
    }

    /**
     * 发送详情-跟手工添加的手机号进行去重
     * @param phoneList
     * @param manualAddList
     * @param smsSendDetail
     */
    @Deprecated
    private void dataProcess(List<String> phoneList,List<String> manualAddList,SmsSendDetail smsSendDetail){
        if (CollectionUtils.isNotEmpty(phoneList)) {
            if (phoneList.size() == PAGE_SIZE) {
                if (CollectionUtils.isNotEmpty(manualAddList)) {

                    duplicateList(phoneList,manualAddList);
                }
                smsSendDetail.setPhoneNumbers(StringUtils.join(phoneList, ","));
                smsSendDetail.setCreateTime(LocalDateTime.now());
                smsSendDetailService.add(smsSendDetail);
            } else {
                if (CollectionUtils.isNotEmpty(manualAddList)) {
                    phoneList.addAll(manualAddList);
                    saveSendDetail(smsSendDetail,phoneList);
                    manualAddList = null;
                } else {
                    smsSendDetail.setPhoneNumbers(StringUtils.join(phoneList, ","));
                    smsSendDetail.setCreateTime(LocalDateTime.now());
                    smsSendDetailService.add(smsSendDetail);
                }
            }
        }
    }

    /**
     * 发送详情
     * @param smsSend
     * @return
     */
    public boolean sendDetail(SmsSend smsSend){
        SmsSendDetailQueryRequest detailQueryRequest = SmsSendDetailQueryRequest
                .builder()
                .sendId(smsSend.getId())
                .notStatus(SendDetailStatus.SUCCESS)
                .build();
        long taskCount = smsSendDetailService.count(detailQueryRequest);
        if(taskCount>0){
            long pageCount = taskCount % PAGE_SIZE == 0 ? taskCount / PAGE_SIZE : taskCount / PAGE_SIZE + 1;
            for (long pageNum = 0; pageNum < pageCount; pageNum++) {
                detailQueryRequest.setPageNum((int) pageNum);
                detailQueryRequest.setPageSize((int) PAGE_SIZE);
                Page<SmsSendDetail> page = this.smsSendDetailService.page(detailQueryRequest);
                for (SmsSendDetail detail : page) {
                    detail.setTemplateCode(smsSend.getTemplateCode());
                    detail.setSignName(smsSend.getSignName());
                    detail.setSendTime(LocalDateTime.now());
                    SmsBaseResponse smsBaseResponse = smsProxy.sendSms(detail);
                    detail.setCode(smsBaseResponse.getCode());
                    detail.setMessage(smsBaseResponse.getMessage());
                    detail.setBizId(smsBaseResponse.getBizId());
                    this.smsSendDetailService.modify(detail);
                    if (!smsBaseResponse.getCode().equals(SmsResponseCode.SUCCESS)) {
                        detail.setStatus(SendDetailStatus.FAILED);
                        this.smsSendDetailService.modify(detail);
                        smsSend.setStatus(SendStatus.FAILED);
                        smsSend.setResendType(SmsResponseCode.resendType(smsBaseResponse.getCode()));
                        smsSend.setMessage(smsBaseResponse.getMessage());
                        return false;
                    }
                    detail.setStatus(SendDetailStatus.SUCCESS);
                    this.smsSendDetailService.modify(detail);
                }
            }
        }

        smsSend.setStatus(SendStatus.END);
        smsSend.setMessage(SmsResponseCode.SUCCESS);
        smsSend.setSendTime(LocalDateTime.now());
        this.smsSendRepository.save(smsSend);
        log.info("发送任务结束：sendId={}",smsSend.getId());
        return true;
    }


}
