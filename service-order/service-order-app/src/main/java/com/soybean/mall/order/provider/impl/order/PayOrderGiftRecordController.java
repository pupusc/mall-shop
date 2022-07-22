package com.soybean.mall.order.provider.impl.order;

import com.soybean.mall.order.api.provider.order.OrderConfigProvider;
import com.soybean.mall.order.api.provider.order.PayOrderGiftRecordProvider;
import com.soybean.mall.order.api.request.record.OrderGiftRecordMqReq;
import com.soybean.mall.order.config.OrderConfigProperties;
import com.soybean.mall.order.gift.service.PayOrderGiftRecordPointService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.trade.model.entity.value.Pay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/24 12:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class PayOrderGiftRecordController implements PayOrderGiftRecordProvider {


    @Autowired
    private PayOrderGiftRecordPointService payOrderGiftRecordPointService;

    @Override
    public BaseResponse afterCreateOrder(OrderGiftRecordMqReq orderGiftRecordMqReq) {
        payOrderGiftRecordPointService.afterCreateOrder(orderGiftRecordMqReq.getMessage());
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse afterPayOrderLock(OrderGiftRecordMqReq orderGiftRecordMqReq) {
        payOrderGiftRecordPointService.afterPayOrderLock(orderGiftRecordMqReq.getMessage());
        return BaseResponse.SUCCESSFUL();
    }


}
