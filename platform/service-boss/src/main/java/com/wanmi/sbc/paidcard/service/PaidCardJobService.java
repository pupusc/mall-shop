package com.wanmi.sbc.paidcard.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcard.PaidCardSaveProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelSaveProvider;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByCustomerIdsRequest;
import com.wanmi.sbc.customer.api.request.paidcard.PaidCardExpireRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelQueryRequest;
import com.wanmi.sbc.customer.bean.enums.PaidCardSmsTemplate;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailBaseVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@Slf4j
public class PaidCardJobService {

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private PaidCardCustomerRelSaveProvider paidCardCustomerRelSaveProvider;

    @Autowired
    private PaidCardSaveProvider paidCardSaveProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;


    /**
     * 发送即将过期短信提醒
     */
    public void sendWillExpireMsg() {
        log.info("sendWillExpireMsg 执行");
        ConfigQueryRequest configQueryRequest = new ConfigQueryRequest();
        configQueryRequest.setConfigKey(ConfigKey.PAID_CARD.toValue());
        configQueryRequest.setConfigType(ConfigType.PAID_CARD_CONFIG.toValue());
        configQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        BaseResponse<SystemConfigTypeResponse> response = systemConfigQueryProvider.findByConfigTypeAndDelFlag(configQueryRequest);
        String context = response.getContext().getConfig().getContext();
        HashMap contextMap = JSON.parseObject(context, HashMap.class);
        Long remainDayVal = Long.valueOf(contextMap.get("remainDay").toString());
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime remainDay = now.plusDays(remainDayVal);
//        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList
//                = paidCardCustomerRelQueryProvider
//                .listNew(PaidCardCustomerRelListRequest.builder()
//                        .delFlag(DeleteFlag.NO)
//                        .sendMsgFlag(Boolean.FALSE)
//                        .endTimeEnd(remainDay)
//                        .build()).getContext().getPaidCardCustomerRelVOList();

//        log.info("paidCardCustomerRelVOList: {}",paidCardCustomerRelVOList);
        Integer maxTmpId = 0;
        int pageSize = 500;
        int i = 0;
        while (true) {
            PaidCardCustomerRelListRequest paidCardCustomerRelListRequest = new PaidCardCustomerRelListRequest();
            paidCardCustomerRelListRequest.setEndTimeBegin(remainDay);
            paidCardCustomerRelListRequest.setPageSize(pageSize);
            paidCardCustomerRelListRequest.setMaxTmpId(maxTmpId);
            List<PaidCardCustomerRelVO> paidCardCustomerRelVOList = paidCardCustomerRelQueryProvider.listNew(paidCardCustomerRelListRequest).getContext().getPaidCardCustomerRelVOList();
            if (!CollectionUtils.isEmpty(paidCardCustomerRelVOList)) {
                List<String> customerIdList = paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getCustomerId).distinct().collect(Collectors.toList());
                CustomerDetailListByConditionRequest req = new CustomerDetailListByConditionRequest();
                req.setCustomerIds(customerIdList);
                List<CustomerDetailVO> customerDetailVOList = customerDetailQueryProvider.listCustomerDetailByCondition(req).getContext().getCustomerDetailVOList();

                for (PaidCardCustomerRelVO paidCardCustomerRelVO : paidCardCustomerRelVOList) {
                    CustomerDetailVO customerDetailVO = customerDetailVOList.stream().filter(x -> x.getCustomerId().equals(paidCardCustomerRelVO.getCustomerId())).findFirst().get();
                    paidCardCustomerRelVO.setPhone(customerDetailVO.getContactPhone());
                    if (paidCardCustomerRelVO.getMaxTmpId() > maxTmpId) {
                        maxTmpId = paidCardCustomerRelVO.getMaxTmpId();
                    }
                }
                log.info("PaidCardJobService sendWillExpireMsg 轮次:{} maxTmpId:{} size:{}", i, maxTmpId, paidCardCustomerRelVOList.size());

                // TODO 发送短信
                List<PaidCardExpireRequest> requestList = paidCardCustomerRelVOList.stream()
                        .map(rel -> PaidCardExpireRequest.builder()
                                .phone(rel.getPhone())
                                .paidCardName(rel.getPaidCardName())
                                .year(rel.getEndTime().getYear()+"")
                                .month(rel.getEndTime().getMonth().getValue()+"")
                                .day(rel.getEndTime().getDayOfMonth()+"")
                                .customerId(rel.getCustomerId())
                                .build())
                        .collect(Collectors.toList());
                paidCardSaveProvider.sendWillExpireSms(requestList);

                // 变更付费卡实例发送短信状态
                List<String> relIdList = paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getId).collect(Collectors.toList());
                paidCardCustomerRelSaveProvider.changeSendMsgFlag(relIdList);
            }

           if (paidCardCustomerRelVOList.size() < pageSize) {
                break;
           }
        }
    }
    /**
     * 发送已经过期短信提醒
     */
    public void sendExpireMsg() {
        log.info("sendExpireMsg执行了");
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList
                = paidCardCustomerRelQueryProvider
                .list(PaidCardCustomerRelListRequest.builder()
                        .delFlag(DeleteFlag.NO)
                        .sendExpireMsgFlag(Boolean.FALSE)
                        .endTimeEnd(LocalDateTime.now())
                        .build()).getContext().getPaidCardCustomerRelVOList();
        log.info("paidCardCustomerRelVOList: {}",paidCardCustomerRelVOList);
        if(CollectionUtils.isNotEmpty(paidCardCustomerRelVOList)){
            List<String> customerIdList = paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getCustomerId).distinct().collect(Collectors.toList());
            CustomerDetailListByConditionRequest req = new CustomerDetailListByConditionRequest();
            req.setCustomerIds(customerIdList);
            List<CustomerDetailVO> customerDetailVOList = customerDetailQueryProvider.listCustomerDetailByCondition(req).getContext().getCustomerDetailVOList();
            paidCardCustomerRelVOList.forEach(paidCardCustomerRelVO->{
                CustomerDetailVO customerDetailVO = customerDetailVOList.stream().filter(x -> x.getCustomerId().equals(paidCardCustomerRelVO.getCustomerId())).findFirst().get();
                paidCardCustomerRelVO.setPhone(customerDetailVO.getContactPhone());
            });
            // 需要发送已经过期提醒短信的用户信息
            List<PaidCardExpireRequest> requestList = paidCardCustomerRelVOList.stream()
                    .map(rel -> PaidCardExpireRequest.builder()
                            .phone(rel.getPhone())
                            .paidCardName(rel.getPaidCardName())
                            .year(rel.getEndTime().getYear()+"")
                            .month(rel.getEndTime().getMonth().getValue()+"")
                            .day(rel.getEndTime().getDayOfMonth()+"")
                            .customerId(rel.getCustomerId())
                            .build())
                    .collect(Collectors.toList());
            paidCardSaveProvider.sendExpireSms(requestList);

            // 变更付费卡实例发送短信状态
            List<String> relIdList = paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getId).collect(Collectors.toList());
            paidCardCustomerRelSaveProvider.changeExpireSendMsgFlag(relIdList);
        }
    }
}
