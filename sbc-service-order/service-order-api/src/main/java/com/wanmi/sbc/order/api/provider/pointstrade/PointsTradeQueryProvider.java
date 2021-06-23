package com.wanmi.sbc.order.api.provider.pointstrade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradeListExportRequest;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradePageCriteriaRequest;
import com.wanmi.sbc.order.api.response.pointstrade.PointsTradeGetByIdResponse;
import com.wanmi.sbc.order.api.response.pointstrade.PointsTradeListExportResponse;
import com.wanmi.sbc.order.api.response.pointstrade.PointsTradePageCriteriaResponse;
import com.wanmi.sbc.order.api.response.trade.TradeListExportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author lvzhenwei
 * @Description 积分订单相关查询接口
 * @Date 15:29 2019/5/14
 * @Param
 * @return
 **/
@FeignClient(value = "${application.order.name}", contextId = "PointsTradeQueryProvider")
public interface PointsTradeQueryProvider {

    /**
     * 通过id获取订单信息
     *
     * @param pointsTradeGetByIdRequest 订单id {@link PointsTradeGetByIdRequest}
     * @return 订单信息 {@link PointsTradeGetByIdResponse}
     */
    @PostMapping("/points/order/${application.order.version}/points-trade/get-by-id")
    BaseResponse<PointsTradeGetByIdResponse> getById(@RequestBody @Valid PointsTradeGetByIdRequest pointsTradeGetByIdRequest);

    /**
     * 根据查询条件分页查询订单信息
     *
     * @param pointsTradePageCriteriaRequest 带参分页参数 {@link PointsTradePageCriteriaRequest}
     * @return
     */
    @PostMapping("/points/order/${application.order.version}/points-trade/page-criteria")
    BaseResponse<PointsTradePageCriteriaResponse> pageCriteria(@RequestBody @Valid PointsTradePageCriteriaRequest pointsTradePageCriteriaRequest);

    /**
     * 查询积分订单导出数据
     *
     * @param pointsTradeListExportRequest 查询条件 {@link PointsTradeListExportRequest}
     * @return 验证结果 {@link TradeListExportResponse}
     */
    @PostMapping("/points/order/${application.order.version}/points-trade/list-points-trade-export")
    BaseResponse<PointsTradeListExportResponse> listPointsTradeExport(@RequestBody @Valid PointsTradeListExportRequest pointsTradeListExportRequest);

    /**
     * 积分订单自动收货
     * @return
     */
    @PostMapping("/points/order/${application.order.version}/trade/order-auto-receive")
    BaseResponse pointsOrderAutoReceive();

}
