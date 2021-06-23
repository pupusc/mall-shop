package com.wanmi.sbc.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityAddListByActivityIdRequest;
import com.wanmi.sbc.elastic.bean.dto.customer.EsCustomerDetailDTO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsEnterpriseInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@EnableBinding
public class ManagerBaseProducerService {

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
     * 根据优惠券活动ID集合，同步生成ES数据，发送MQ消息
     * @param activityIdList
     */
    public void sendMQForAddCouponActivity(List<String> activityIdList) {
        resolver.resolveDestination(MQConstant.Q_ES_SERVICE_COUPON_ADD_POINTS_COUPON).send(new GenericMessage<>(JSONObject.toJSONString(new EsCouponActivityAddListByActivityIdRequest(activityIdList))));

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
