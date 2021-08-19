package com.wanmi.sbc.mq;

import com.google.common.collect.Lists;
import com.wanmi.ares.provider.CustomerBaseQueryProvider;
import com.wanmi.ares.request.CustomerQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.crmgroup.CrmGroupProvider;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.message.api.provider.smssend.SmsSendSaveProvider;
import com.wanmi.sbc.message.api.provider.smssenddetail.SmsSendDetailSaveProvider;
import com.wanmi.sbc.message.api.provider.smssign.SmsSignQueryProvider;
import com.wanmi.sbc.message.api.request.smssend.SmsSendDetailSendRequest;
import com.wanmi.sbc.message.api.request.smssend.SmsSendSaveRequest;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailAddRequest;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailBatchAddRequest;
import com.wanmi.sbc.message.api.request.smssign.SmsSignListRequest;
import com.wanmi.sbc.message.bean.constant.ReceiveGroupType;
import com.wanmi.sbc.message.bean.dto.SmsSendDTO;
import com.wanmi.sbc.message.bean.dto.SmsSendDetailDTO;
import com.wanmi.sbc.message.bean.enums.ReceiveType;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import com.wanmi.sbc.message.bean.vo.SmsSignVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-5
 * \* Time: 17:36
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
@Slf4j
public class SmsSendTaskService {
    @Autowired
    CustomerBaseQueryProvider customerBaseQueryProvider;

    @Autowired
    private SmsSendDetailSaveProvider smsSendDetailSaveProvider;

    @Autowired
    private SmsSendSaveProvider smsSendSaveProvider;

    @Autowired
    private SmsSignQueryProvider smsSignQueryProvider;

    @Autowired
    private CrmGroupProvider crmGroupProvider;

    private final long PAGE_SIZE=1000L;

   // @Async("myAsyncPool")
    public void send(SmsSendDTO smsSend){
        List<SmsSignVO> signVOList = smsSignQueryProvider.list(SmsSignListRequest.builder().id(smsSend.getSignId())
                .delFlag(DeleteFlag.NO).reviewStatus(ReviewStatus.REVIEWPASS).build()).getContext().getSmsSignVOList();
        SmsSignVO smsSign = null;
        if(CollectionUtils.isNotEmpty(signVOList)){
            smsSign = signVOList.get(0);
        }

        if(smsSend.getSendDetailCount()==null||smsSend.getSendDetailCount()<=0) {
            if (smsSign == null) {
                smsSend.setStatus(SendStatus.FAILED);
                smsSend.setMessage("签名信息不存在或者已删除！");
                this.smsSendSaveProvider.save(KsBeanUtil.convert(smsSend, SmsSendSaveRequest.class));
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
            //this.smsSendRepository.save(smsSend);
        }
        smsSendSaveProvider.sendDetail(SmsSendDetailSendRequest.builder().sendDTO(smsSend).build());
        return;
    }

    /**
     * 全部会员
     * @param smsSend
     */
    private void allProcess(SmsSendDTO smsSend){
        int sendDetailCount=0;
        List<String> manualAddList = null;
        if(StringUtils.isNotEmpty(smsSend.getManualAdd())){
            manualAddList = Arrays.asList(smsSend.getManualAdd().split(","));
        }
        CustomerQueryRequest request = new CustomerQueryRequest();
        SmsSendDetailDTO smsSendDetail = new SmsSendDetailDTO();
        smsSendDetail.setSendId(smsSend.getId());
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
     * 按会员等级
     * @param smsSend
     */
    private void levelProcess(SmsSendDTO smsSend){
        allProcess(smsSend);
    }

    /**
     * 会员分组
     * @param smsSend
     */
    private void groupProcess(SmsSendDTO smsSend){
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
        CrmGroupRequest request = new CrmGroupRequest();
        request.setSysGroupList(sysGroupList);
        request.setCustomGroupList(customGroupList);
        SmsSendDetailDTO smsSendDetail = new SmsSendDetailDTO();
        smsSendDetail.setSendId(smsSend.getId());

        long totalNum = this.crmGroupProvider.queryCustomerPhoneCount(request).getContext();
        if(totalNum>0) {
            request.setPageSize((int)PAGE_SIZE);
            long pageCount = totalNum % PAGE_SIZE == 0 ? totalNum / PAGE_SIZE : totalNum / PAGE_SIZE + 1;
            for (int pageNum = 0; pageNum < pageCount; pageNum++) {
                request.setPageNum(pageNum);

                List<String> requestList = this.crmGroupProvider.queryCustomerPhone(request).getContext();
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
    private void customProcess(SmsSendDTO smsSend){
        int sendDetailCount = 0;
        long totalCount = 0L;
        SmsSendDetailDTO smsSendDetail = new SmsSendDetailDTO();
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
    private void saveSendDetail(SmsSendDetailDTO smsSendDetail, List<String> list){
        List<List<String>> lists = Lists.partition(list, (int) PAGE_SIZE);
        List<SmsSendDetailDTO> dtos = new ArrayList<>();
        for (List<String> tempList : lists) {
            smsSendDetail.setPhoneNumbers(StringUtils.join(tempList, ","));
            smsSendDetail.setCreateTime(LocalDateTime.now());
            dtos.add(KsBeanUtil.convert(smsSendDetail, SmsSendDetailDTO.class));
        }
        smsSendDetailSaveProvider.batchAdd(SmsSendDetailBatchAddRequest.builder().detailList(dtos).build());
    }

    /**
     * 去重
     * @param fixedList
     * @param varList
     */
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
    private void dataProcess(List<String> phoneList,List<String> manualAddList,SmsSendDetailDTO smsSendDetail){
        if (CollectionUtils.isNotEmpty(phoneList)) {
            if (phoneList.size() == PAGE_SIZE) {
                if (CollectionUtils.isNotEmpty(manualAddList)) {

                    duplicateList(phoneList,manualAddList);
                }
                smsSendDetail.setPhoneNumbers(StringUtils.join(phoneList, ","));
                smsSendDetail.setCreateTime(LocalDateTime.now());
                smsSendDetailSaveProvider.add(KsBeanUtil.convert(smsSendDetail, SmsSendDetailAddRequest.class));
            } else {
                if (CollectionUtils.isNotEmpty(manualAddList)) {
                    phoneList.addAll(manualAddList);
                    saveSendDetail(smsSendDetail,phoneList);
                    manualAddList = null;
                } else {
                    smsSendDetail.setPhoneNumbers(StringUtils.join(phoneList, ","));
                    smsSendDetail.setCreateTime(LocalDateTime.now());
                    smsSendDetailSaveProvider.add(KsBeanUtil.convert(smsSendDetail, SmsSendDetailAddRequest.class));
                }
            }
        }
    }
}
