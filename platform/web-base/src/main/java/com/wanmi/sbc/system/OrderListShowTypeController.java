package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.systemconfig.OrderListShowTypeProvider;
import com.wanmi.sbc.setting.api.response.systemconfig.OrderListShowTypeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * S2B 平台端-订单列表展示设置
 * Created by dyt on 2017/12/06.
 */
@Api(tags = "OrderListShowTypeController", description = "S2B 平台端-订单列表展示设置配置API")
@RestController
@RequestMapping("/config/orderListShowType")
public class OrderListShowTypeController {

    @Resource
    private OrderListShowTypeProvider orderListShowTypeProvider;


    @ApiOperation(value = "查询订单列表展示设置")
    @GetMapping
    public BaseResponse<OrderListShowTypeResponse> query() {
        return orderListShowTypeProvider.query();
    }
}
