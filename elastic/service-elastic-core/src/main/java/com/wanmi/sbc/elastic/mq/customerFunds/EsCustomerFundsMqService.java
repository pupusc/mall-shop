package com.wanmi.sbc.elastic.mq.customerFunds;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.mq.CustomerFundsModifyCustomerAccountRequest;
import com.wanmi.sbc.customer.api.request.mq.CustomerFundsModifyCustomerNameAndAccountRequest;
import com.wanmi.sbc.customer.api.request.mq.CustomerFundsModifyCustomerNameRequest;
import com.wanmi.sbc.customer.api.request.mq.EsCustomerFundsGrantAmountRequest;
import com.wanmi.sbc.elastic.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsGrantAmountModifyRequest;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsListRequest;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsModifyRequest;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsRequest;
import com.wanmi.sbc.elastic.bean.dto.customerFunds.EsCustomerFundsDTO;
import com.wanmi.sbc.elastic.customerFunds.service.EsCustomerFundsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author yangzhen
 * @Description //MQ消费者
 * @Date 10:23 2020/12/16
 * @Param
 * @return
 **/
@Slf4j
@Component
@EnableBinding(EsCustomerFundsSink.class)
public class EsCustomerFundsMqService {

    @Autowired
    private EsCustomerFundsService esCustomerFundsService;


    /**
     * 更新ES会员资金-会员账号字段
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_ACCOUNT)
    public void modifyEsCustomerAccountWithCustomerFunds(String json) {
        try {
            CustomerFundsModifyCustomerAccountRequest request = JSONObject.parseObject(json, CustomerFundsModifyCustomerAccountRequest.class);
            boolean result = esCustomerFundsService.updateCustomerFunds(EsCustomerFundsModifyRequest.builder()
                    .customerAccount(request.getCustomerAccount())
                    .customerId(request.getCustomerId()).build());
            //发mq给es 修改es 会员资金账号
            log.info("ES更新会员资金-会员账号字段，是否成功 ? {}",result ? "成功" : "失败");
        } catch (Exception e) {
            log.error("ES更新会员资金-会员账号字段，发生异常! param={}", json, e);
        }
    }



    /**
     * 更新ES会员资金-会员名称字段
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME)
    public void modifyEsCustomerNameWithCustomerFunds(String json) {
        try {
            CustomerFundsModifyCustomerNameRequest request = JSONObject.parseObject(json, CustomerFundsModifyCustomerNameRequest.class);
            boolean result = esCustomerFundsService.updateCustomerFunds(EsCustomerFundsModifyRequest.builder()
                    .customerName(request.getCustomerName())
                    .customerId(request.getCustomerId()).build());
            log.info("ES更新会员资金-会员名称字段，是否成功 ? {}",result ? "成功" : "失败");
        } catch (Exception e) {
            log.error("ES更新会员资金-会员名称字段,发生异常! param={}", json, e);
        }
    }



    /**
     * 更新ES会员资金-会员名称、会员账号字段
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME_AND_ACCOUNT)
    public void modifyEsCustomerNameAndAccountWithCustomerFunds(String json) {
        try {
            CustomerFundsModifyCustomerNameAndAccountRequest request = JSONObject.parseObject(json, CustomerFundsModifyCustomerNameAndAccountRequest.class);
            boolean result = esCustomerFundsService.updateCustomerFunds(EsCustomerFundsModifyRequest.builder()
                    .customerName(request.getCustomerName())
                    .customerAccount(request.getCustomerAccount())
                    .customerId(request.getCustomerId()).build());
            log.info("ES更新会员资金-会员名称、会员账号字段，是否成功 ? {}",result ? "成功" : "失败");
        } catch (Exception e) {
            log.error("ES更新会员资金-会员名称、会员账号字段,发生异常! param={}", json, e);
        }
    }


    /**
     * ES新增会员，初始化会员资金信息
     *
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_ADD_INIT)
    public void initCustomerFunds(String json) {
        try {
            EsCustomerFundsRequest request = JSONObject.parseObject(json, EsCustomerFundsRequest.class);
            boolean addResult = esCustomerFundsService.initCustomerFunds(request);
            log.info("ES新增会员，初始化会员资金信息，是否成功 ? {}", addResult ? "成功" : "失败");
        } catch (Exception e) {
            log.error("ES新增会员，初始化会员资金信息, 发生异常！param={}", json, e);
        }
    }


    /**
     * 邀新注册-发放邀新奖金
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT)
    public void grantEsAmountWithCustomerFunds(String json) {
        try {
            EsCustomerFundsGrantAmountRequest request = JSONObject.parseObject(json, EsCustomerFundsGrantAmountRequest.class);
            boolean result = esCustomerFundsService.queryCustomerFundsIsExist(EsCustomerFundsModifyRequest
                    .builder()
                    .customerId(request.getCustomerId()).build());

            if (!result){
                EsCustomerFundsRequest request1 = KsBeanUtil.convert(request,EsCustomerFundsRequest.class);
                boolean addResult = esCustomerFundsService.initCustomerFunds(request1);
                log.info("邀新注册-发放奖金,是否成功 ? {1}",request.getCustomerId(), addResult ? "成功" : "失败");
            }else{
                boolean updateResult = esCustomerFundsService.updateGrantAmountCustomerFunds(EsCustomerFundsGrantAmountModifyRequest.builder()
                        .distributor(request.getDistributor())
                        .customerId(request.getCustomerId())
                        .amount(request.getAmount()).build());
                log.info("ES新增分销员，更新会员资金-是否分销员字段,是否成功 ? {}",updateResult ? "失败" : "成功");
            }
        } catch (Exception e) {
            log.error("ES邀新注册-发放奖金, 发生异常！param={}", json, e);
        }
    }


    /**
     * 更新es 会员资金
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS)
    public void sendEsCustomerFunds(String json) {
        try {
            EsCustomerFundsRequest request = JSONObject.parseObject(json, EsCustomerFundsRequest.class);
            boolean addResult = esCustomerFundsService.initCustomerFunds(request);
            if(addResult){
                log.info("ES更新会员资金,是否成功 ? {1}",request.getCustomerId(), addResult ? "成功" : "失败");
            }
        } catch (Exception e) {
            log.error("ES更新会员资金,发生异常！param={}", json, e);
        }
    }


    /**
     * 批量更新es会员资金
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS_LIST)
    public void sendEsCustomerFundsList(String json) {
        try {
            List<EsCustomerFundsDTO> lists = JSONObject.parseArray(json, EsCustomerFundsDTO.class);
            boolean addResult = esCustomerFundsService.initCustomerFundsList(EsCustomerFundsListRequest.builder().esCustomerFundsDTOS(lists).build());
            if(addResult){
                log.info("ES批量更新会员资金,是否成功 ", addResult ? "成功" : "失败");
            }
        } catch (Exception e) {
            log.error("ES批量更新会员资金, 发生异常！param={}", json, e);
        }
    }


}
