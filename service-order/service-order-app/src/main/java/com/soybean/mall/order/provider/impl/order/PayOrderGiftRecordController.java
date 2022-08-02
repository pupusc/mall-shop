package com.soybean.mall.order.provider.impl.order;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.api.enums.RecordMessageTypeEnum;
import com.soybean.mall.order.api.provider.order.OrderConfigProvider;
import com.soybean.mall.order.api.provider.order.PayOrderGiftRecordProvider;
import com.soybean.mall.order.api.request.mq.RecordMessageMq;
import com.soybean.mall.order.api.request.record.OrderGiftRecordMqReq;
import com.soybean.mall.order.api.request.record.OrderGiftRecordSearchReq;
import com.soybean.mall.order.api.response.record.OrderGiftRecordResp;
import com.soybean.mall.order.config.OrderConfigProperties;
import com.soybean.mall.order.gift.model.OrderGiftRecord;
import com.soybean.mall.order.gift.service.PayOrderGiftRecordPointService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.trade.model.entity.value.Pay;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/24 12:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@Slf4j
public class PayOrderGiftRecordController implements PayOrderGiftRecordProvider {


    @Autowired
    private PayOrderGiftRecordPointService payOrderGiftRecordPointService;

    @Override
    public BaseResponse afterRecordMessageOrder(OrderGiftRecordMqReq orderGiftRecordMqReq) {
        log.info("PayOrderGiftRecordService afterRecordMessageOrder message: {}", orderGiftRecordMqReq.getMessage());
        if (StringUtils.isBlank(orderGiftRecordMqReq.getMessage())) {
            return BaseResponse.FAILED();
        }
        RecordMessageMq recordMessageMq = JSON.parseObject(orderGiftRecordMqReq.getMessage(), RecordMessageMq.class);
        if (RecordMessageTypeEnum.CREATE_ORDER.equals(RecordMessageTypeEnum.getByCode(recordMessageMq.getRecordMessageType()))) {
            payOrderGiftRecordPointService.afterCreateOrder(recordMessageMq);
        } else if (RecordMessageTypeEnum.PAY_ORDER.equals(RecordMessageTypeEnum.getByCode(recordMessageMq.getRecordMessageType()))) {
            payOrderGiftRecordPointService.afterPayOrderLock(recordMessageMq);
        } else if (RecordMessageTypeEnum.CANCEL_ORDER.equals(RecordMessageTypeEnum.getByCode(recordMessageMq.getRecordMessageType()))) {
            payOrderGiftRecordPointService.cancelOrderGiftRecord(recordMessageMq);
        } else {
            return BaseResponse.FAILED();
        }

        return BaseResponse.SUCCESSFUL();
    }

//
//    @Override
//    public BaseResponse afterPayOrderLock(OrderGiftRecordMqReq orderGiftRecordMqReq) {
//        payOrderGiftRecordPointService.afterPayOrderLock(orderGiftRecordMqReq.getMessage());
//        return BaseResponse.SUCCESSFUL();
//    }


    @Override
    public BaseResponse<List<OrderGiftRecordResp>> listNoPage(OrderGiftRecordSearchReq req) {
        return BaseResponse.success(payOrderGiftRecordPointService.listNoPage(req));
    }
}
