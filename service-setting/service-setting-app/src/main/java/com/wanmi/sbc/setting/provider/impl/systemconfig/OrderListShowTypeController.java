package com.wanmi.sbc.setting.provider.impl.systemconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.systemconfig.OrderListShowTypeProvider;
import com.wanmi.sbc.setting.api.request.systemconfig.OrderListShowTypeModifyRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.OrderListShowTypeResponse;
import com.wanmi.sbc.setting.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by feitingting on 2019/11/6.
 */
@RestController
public class OrderListShowTypeController implements OrderListShowTypeProvider {

    @Autowired
    ConfigService configService;

    @Override
    public BaseResponse modify(@RequestBody OrderListShowTypeModifyRequest request){
        configService.modifyOrderListShowType(request.getStatus());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询订单列表展示状态
     *
     * @return 订单列表展示状态
     */
    @Override
    public BaseResponse<OrderListShowTypeResponse> query(){
        return BaseResponse.success(OrderListShowTypeResponse.builder().status(configService.queryOrderListShowType()).build());
    }
}

