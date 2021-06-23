package com.wanmi.sbc.message.handle;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.node.*;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.message.SmsProxy;
import com.wanmi.sbc.message.bean.dto.SmsTemplateParamDTO;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsNoticeSendHandler implements MessageDelivery {

    @Autowired
    private SmsProxy smsProxy;


    @Override
    public void handle(MessageMQRequest request) {
        log.info("通知类短信节点{}接收消息", request.getNodeCode());
        SmsSendDetail smsSend = new SmsSendDetail();
        smsSend.setBusinessType(request.getNodeCode());
        smsSend.setPhoneNumbers(request.getMobile());
        if(CollectionUtils.isNotEmpty(request.getParams())) {
            SmsTemplateParamDTO dto = new SmsTemplateParamDTO();
            //第一参数是number
            if(AccoutAssetsType.COUPON_RECEIPT.getType().equals(request.getNodeCode())
                    ||AccoutAssetsType.COUPON_EXPIRED.getType().equals(request.getNodeCode())
                    ||AccoutAssetsType.INTEGRAL_RECEIPT.getType().equals(request.getNodeCode())
                    ||AccoutAssetsType.INTEGRAL_EXPIRED.getType().equals(request.getNodeCode())
                    ||AccoutAssetsType.INTEGRAL_EXPIRED_AGAIN.getType().equals(request.getNodeCode())
                    ||AccoutAssetsType.GROWTH_VALUE_RECEIPT.getType().equals(request.getNodeCode())){
                dto.setNumber(request.getParams().get(0));
            }else if(AccoutAssetsType.BALANCE_CHANGE.getType().equals(request.getNodeCode())){
                //第一参数是money
                dto.setMoney(request.getParams().get(0));
            }else if(AccoutAssetsType.BALANCE_WITHDRAW_REJECT.getType().equals(request.getNodeCode())){
                //第一参数是remark
                dto.setRemark(StringUtil.trunc(request.getParams().get(0), 20, "..."));
            }else {
                dto.setName(StringUtil.trunc(request.getParams().get(0), 10, "..."));
            }

            if(request.getParams().size() > 1) {
                //第二参数是原因
                if (OrderProcessType.ORDER_CHECK_NOT_PASS.getType().equals(request.getNodeCode())
                        || ReturnOrderProcessType.AFTER_SALE_ORDER_CHECK_NOT_PASS.getType().equals(request.getNodeCode())
                        || ReturnOrderProcessType.RETURN_ORDER_GOODS_REJECT.getType().equals(request.getNodeCode())
                        || ReturnOrderProcessType.REFUND_CHECK_NOT_PASS.getType().equals(request.getNodeCode())) {
                    dto.setRemark(StringUtil.trunc(request.getParams().get(1), 20, "..."));
                } else if (OrderProcessType.GROUP_NUM_LIMIT.getType().equals(request.getNodeCode())
                        || DistributionType.FRIEND_REGISTER_SUCCESS_NO_REWARD.getType().equals(request.getNodeCode())
                        || DistributionType.FRIEND_REGISTER_SUCCESS_HAS_REWARD.getType().equals(request.getNodeCode())
                        || DistributionType.INVITE_CUSTOMER_REWARD_RECEIPT.getType().equals(request.getNodeCode())
                ) {
                    //第二参数是Number
                    dto.setNumber(request.getParams().get(1));
                } else if (AccoutAssetsType.COUPON_RECEIPT.getType().equals(request.getNodeCode())
                    || AccoutAssetsType.COUPON_EXPIRED.getType().equals(request.getNodeCode())) {
                    //第二参数是Money
                    dto.setMoney(request.getParams().get(1));
                } else if (AccoutAssetsType.BALANCE_CHANGE.getType().equals(request.getNodeCode())) {
                    //第二参数是Price
                    dto.setPrice(request.getParams().get(1));
                } else if (DistributionType.PROMOTE_ORDER_PAY_SUCCESS.getType().equals(request.getNodeCode())) {
                    //第二参数是Product
                    dto.setProduct(StringUtil.trunc(request.getParams().get(1), 10, "..."));
                }

                if(request.getParams().size() > 2){
                    //第三参数是money
                    if(DistributionType.PROMOTE_ORDER_PAY_SUCCESS.getType().equals(request.getNodeCode())){
                        dto.setMoney(request.getParams().get(2));
                    }
                    if(DistributionType.FRIEND_REGISTER_SUCCESS_NO_REWARD.getType().equals(request.getNodeCode())
                            || DistributionType.FRIEND_REGISTER_SUCCESS_HAS_REWARD.getType().equals(request.getNodeCode())
                            || DistributionType.INVITE_CUSTOMER_REWARD_RECEIPT.getType().equals(request.getNodeCode())){
                        dto.setMoney(request.getParams().get(2));
                        if(request.getParams().size() > 3){
                            dto.setPrice(request.getParams().get(3));
                        }
                    }
                }
            }
            smsSend.setTemplateParam(JSON.toJSONString(dto));
        }
        smsProxy.sendSms(smsSend);
    }

}
