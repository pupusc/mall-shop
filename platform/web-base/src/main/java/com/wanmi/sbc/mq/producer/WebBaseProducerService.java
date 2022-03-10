package com.wanmi.sbc.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.soybean.common.util.IntegerEncryptTool;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailModifyRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsCustomerDetailDTO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsEnterpriseInfoDTO;
import com.wanmi.sbc.order.bean.dto.SensorsMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@EnableBinding
public class WebBaseProducerService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 会员注册后，发送MQ消息
     * @param customerVO
     */
    public void sendMQForCustomerRegister(CustomerVO customerVO) {
        resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_REGISTER).send(new GenericMessage<>(JSONObject.toJSONString(wrapperEsCustomerDetailDTO(customerVO))));
    }

    /**
     * 神策埋点事件
     */
    private void sendSensorsEvent(List<SensorsMessageDto> sensorsMessageDto) {
        resolver.resolveDestination("msg-sensors-producer-event").send(new GenericMessage<>(sensorsMessageDto));
    }

    public void sendUserRegisterEvent(String fandengUserNo, String source) {
        if (StringUtils.isNotBlank(fandengUserNo)) {
            log.info("发送用户注册埋点事件: {}, {}", fandengUserNo, source);
            String fanDengUserNoEncrypt = "";
            try {
                fanDengUserNoEncrypt = IntegerEncryptTool.encrypt(Integer.parseInt(fandengUserNo));
            } catch (Exception ex) {
                log.error("SensorsDataService sendPaySuccessEvent Encrypt error", ex);
            }

            SensorsMessageDto sensorsMessageDto = new SensorsMessageDto();
            sensorsMessageDto.setEventName("shop_regist_0_success");
            sensorsMessageDto.setDistinctId(fanDengUserNoEncrypt);
            sensorsMessageDto.setLoginId(false);
            sensorsMessageDto.addProperty("source", "mini_app");
            sensorsMessageDto.addProperty("var_id", fandengUserNo);
            sendSensorsEvent(Collections.singletonList(sensorsMessageDto));
        }
    }

    /**
     * 修改会员基本信息后，发送MQ消息
     * @param modifyRequest
     */
    public void sendMQForModifyBaseInfo(CustomerDetailModifyRequest modifyRequest) {
        resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO).send(new GenericMessage<>(JSONObject.toJSONString(wrapperEsCustomerDetailDTO(modifyRequest))));

    }

    /**
     * 修改会员基本信息后，发送MQ消息
     * @param customerId
     * @param customerAccount
     */
    public void sendMQForModifyCustomerAccount(String customerId,String customerAccount) {
        resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_ACCOUNT).send(new GenericMessage<>(JSONObject.toJSONString(wrapperEsCustomerDetailDTO(customerId,customerAccount))));

    }

    /**
     * 删除会员信息后，发送MQ消息
     */
    public void sendMQForDelCustomerInfo(String customerId) {
        resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_ACCOUNT).send(new GenericMessage<>(JSONObject.toJSONString(wrapperEsCustomerDetailDTO(customerId))));

    }


    /**
     * 包装会员ID和会员账号
     * @param customerId
     * @param customerAccount
     * @return
     */
    public EsCustomerDetailDTO wrapperEsCustomerDetailDTO(String customerId,String customerAccount){
        EsCustomerDetailDTO esCustomerDetail = new EsCustomerDetailDTO();
        esCustomerDetail.setCustomerId(customerId);
        esCustomerDetail.setCustomerAccount(customerAccount);
        return esCustomerDetail;
    }

    /**
     * 包装会员ID
     * @param customerId
     * @return
     */
    public EsCustomerDetailDTO wrapperEsCustomerDetailDTO(String customerId){
        EsCustomerDetailDTO esCustomerDetail = new EsCustomerDetailDTO();
        esCustomerDetail.setCustomerId(customerId);
        return esCustomerDetail;
    }


    /**
     * 包装会员基本信息
     * @param modifyRequest
     * @return
     */
    public EsCustomerDetailDTO wrapperEsCustomerDetailDTO(CustomerDetailModifyRequest modifyRequest){
        EsCustomerDetailDTO esCustomerDetail = new EsCustomerDetailDTO();
        esCustomerDetail.setAreaId(modifyRequest.getAreaId());
        esCustomerDetail.setCityId(modifyRequest.getCityId());
        esCustomerDetail.setContactName(modifyRequest.getContactName());
        esCustomerDetail.setContactPhone(modifyRequest.getContactPhone());
        esCustomerDetail.setCustomerAddress(modifyRequest.getCustomerAddress());
        esCustomerDetail.setCustomerId(modifyRequest.getCustomerId());
        esCustomerDetail.setCustomerName(modifyRequest.getCustomerName());
        esCustomerDetail.setProvinceId(modifyRequest.getProvinceId());
        esCustomerDetail.setStreetId(modifyRequest.getStreetId());
        return esCustomerDetail;
    }


    /**
     * 包装同步ES会员对象
     * @param customerVO
     * @return
     */
    public EsCustomerDetailDTO wrapperEsCustomerDetailDTO(CustomerVO customerVO){
        CustomerDetailVO customerDetail = customerVO.getCustomerDetail();
        EsCustomerDetailDTO esCustomerDetail = new EsCustomerDetailDTO();
        esCustomerDetail.setCustomerId( customerDetail.getCustomerId() );
        esCustomerDetail.setCustomerName( customerDetail.getCustomerName() );
        esCustomerDetail.setCustomerAccount( customerVO.getCustomerAccount() );
        esCustomerDetail.setProvinceId( customerDetail.getProvinceId() );
        esCustomerDetail.setCityId( customerDetail.getCityId() );
        esCustomerDetail.setAreaId( customerDetail.getAreaId() );
        esCustomerDetail.setStreetId( customerDetail.getStreetId() );
        esCustomerDetail.setCustomerAddress( customerDetail.getCustomerAddress() );
        esCustomerDetail.setContactName( customerDetail.getContactName() );
        esCustomerDetail.setCustomerLevelId( customerVO.getCustomerLevelId() );
        esCustomerDetail.setContactPhone( customerDetail.getContactPhone() );
        esCustomerDetail.setCheckState( customerVO.getCheckState() );
        esCustomerDetail.setCustomerStatus( customerDetail.getCustomerStatus() );
        esCustomerDetail.setEmployeeId( customerDetail.getEmployeeId() );
        esCustomerDetail.setIsDistributor( customerDetail.getIsDistributor() );
        esCustomerDetail.setRejectReason( customerDetail.getRejectReason() );
        esCustomerDetail.setForbidReason( customerDetail.getForbidReason() );
        esCustomerDetail.setEsStoreCustomerRelaList(Lists.newArrayList() );
        esCustomerDetail.setEnterpriseInfo(Objects.isNull(customerVO.getEnterpriseInfoVO()) ? null : KsBeanUtil.convert(customerVO.getEnterpriseInfoVO(), EsEnterpriseInfoDTO.class));
        esCustomerDetail.setEnterpriseCheckState( customerVO.getEnterpriseCheckState() );
        esCustomerDetail.setEnterpriseCheckReason( customerVO.getEnterpriseCheckReason() );
        esCustomerDetail.setCreateTime( customerDetail.getCreateTime());
        return esCustomerDetail;
    }
}
