package com.wanmi.sbc.setting.tradeOrder;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.tradeOrder.service.TradeOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TradeOrderController {

    @RequestMapping(value = "/wx/sync")
    public BaseResponse<Map> sync() {
        BaseResponse<Map> base = new BaseResponse("K-000000");
        return base;
    }

}
