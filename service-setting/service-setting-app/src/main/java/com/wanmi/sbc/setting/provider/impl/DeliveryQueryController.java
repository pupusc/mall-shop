package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.DeliveryQueryProvider;
import com.wanmi.sbc.setting.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.setting.api.response.DeliveryQueryResponse;
import com.wanmi.sbc.setting.kuaidi100.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DeliveryQueryController implements DeliveryQueryProvider {
    @Autowired
    private DeliveryService deliveryService;

    @Override
    public BaseResponse<DeliveryQueryResponse> queryExpressInfoUrl(@RequestBody DeliveryQueryRequest queryRequest) {
        DeliveryQueryResponse response = new DeliveryQueryResponse();
        try {
            response.setOrderList(deliveryService.queryExpressInfoUrl(queryRequest));
        }catch (Exception e){
            log.error("DeliveryQueryController queryExpressInfoUrl exceptin", e);
            return BaseResponse.FAILED();
        }
        return BaseResponse.success(response);
    }
}
