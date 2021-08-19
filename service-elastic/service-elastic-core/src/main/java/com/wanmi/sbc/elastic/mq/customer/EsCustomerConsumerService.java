package com.wanmi.sbc.elastic.mq.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.elastic.bean.dto.customer.EsCustomerDetailDTO;
import com.wanmi.sbc.elastic.customer.service.EsCustomerDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@EnableBinding(EsCustomerSink.class)
public class EsCustomerConsumerService {

    @Autowired
    private EsCustomerDetailService esCustomerDetailService;


    /**
     * ES会员注册
     * @param json
     */
    @StreamListener(MQConstant.Q_ES_SERVICE_CUSTOMER_REGISTER)
    public void register(String json) {
        try {
            EsCustomerDetailDTO request = JSONObject.parseObject(JSON.parse(json).toString(), EsCustomerDetailDTO.class);
            esCustomerDetailService.save(request);
            log.info("========会员注册，保存会员ES信息成功=============");
        } catch (Exception e) {
            log.error("ES会员注册，生成ES数据发生异常! param={}", json, e);
        }
    }

    /**
     * 修改会员基本信息，同步ES
     * @param json
     */
    @StreamListener(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO)
    public void modifyBaseInfo(String json) {
        try {
            EsCustomerDetailDTO request = JSONObject.parseObject(JSON.parse(json).toString(), EsCustomerDetailDTO.class);
            esCustomerDetailService.modifyBaseInfo(request);
            log.info("========修改会员基本信息，同步ES会员信息成功=============");
        } catch (Exception e) {
            log.error("修改会员基本信息，同步ES数据发生异常! param={}", json, e);
        }
    }

    /**
     * 修改会员账号，同步ES
     * @param json
     */
    @StreamListener(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_ACCOUNT)
    public void modifyCustomerAccount(String json) {
        try {
            EsCustomerDetailDTO request = JSONObject.parseObject(JSON.parse(json).toString(), EsCustomerDetailDTO.class);
            esCustomerDetailService.modifyCustomerAccount(request);
            log.info("========修改会员账号，同步ES会员信息成功=============");
        } catch (Exception e) {
            log.error("修改会员账号，同步ES数据发生异常! param={}", json, e);
        }
    }



    /**
     * 删除会员账号，同步ES
     * @param json
     */
    @StreamListener(MQConstant.Q_ES_SERVICE_CUSTOMER_DEL_CUSTOMER_INFO)
    public void delEsCustomerById(String json) {
        try {
            EsCustomerDetailDTO request = JSONObject.parseObject(JSON.parse(json).toString(), EsCustomerDetailDTO.class);
            esCustomerDetailService.delEsCustomerById(request.getCustomerId());
            log.info("========删除会员账号，同步ES会员信息成功=============");
        } catch (Exception e) {
            log.error("删除会员账号，同步ES数据发生异常! param={}", json, e);
        }
    }

    /**
     * 修改会员是否分销员字段，同步ES
     * @param json
     */
    @StreamListener(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_DISTRIBUTOR)
    public void updateCustomerToDistributor(String json) {
        try {
            EsCustomerDetailDTO request = JSONObject.parseObject(JSON.parse(json).toString(), EsCustomerDetailDTO.class);
            esCustomerDetailService.updateCustomerToDistributor(request);
            log.info("========修改会员是否分销员字段，同步ES会员信息成功=============");
        } catch (Exception e) {
            log.error("修改会员是否分销员字段，同步ES数据发生异常! param={}", json, e);
        }
    }
}
