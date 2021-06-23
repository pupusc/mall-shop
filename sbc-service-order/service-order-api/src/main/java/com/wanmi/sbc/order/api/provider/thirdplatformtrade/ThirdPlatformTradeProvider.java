package com.wanmi.sbc.order.api.provider.thirdplatformtrade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.thirdplatformtrade.ThirdPlatformTradeAddRequest;
import com.wanmi.sbc.order.api.request.thirdplatformtrade.ThirdPlatformTradeCompensateRequest;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradeUpdateRequest;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradeUpdateStateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Description: 第三方渠道订单处理
 * @Autho qiaokang
 * @Date：2020-03-27 09:08
 */
@FeignClient(value = "${application.order.name}", contextId = "ThirdPlatformTradeProvider")
public interface ThirdPlatformTradeProvider {

    /**
     * 根据业务id新增第三方渠道订单
     *
     * @param request 请求信息 {@link ThirdPlatformTradeAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/third-platform-trade/add")
    BaseResponse add(@RequestBody @Valid ThirdPlatformTradeAddRequest request);

    /**
     * 补偿第三方渠道订单
     *
     * @param request 请求信息 {@link ThirdPlatformTradeCompensateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/third-platform-trade/compensate")
    BaseResponse compensate(@RequestBody @Valid ThirdPlatformTradeCompensateRequest request);

    /**
     * 更新第三方平台订单
     *
     * @param tradeUpdateRequest 订单信息 {@link ThirdPlatformTradeUpdateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/third-platform-trade/update")
    BaseResponse update(@RequestBody @Valid ThirdPlatformTradeUpdateRequest tradeUpdateRequest);

    /**
     * 更新第三方平台订单，同时修改ProviderTrade及Trade状态
     *
     * @param tradeUpdateStateRequest 订单信息 {@link ThirdPlatformTradeUpdateStateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/third-platform-trade/update-trade-state")
    BaseResponse updateTradeState(@RequestBody @Valid ThirdPlatformTradeUpdateStateRequest tradeUpdateStateRequest);

}
