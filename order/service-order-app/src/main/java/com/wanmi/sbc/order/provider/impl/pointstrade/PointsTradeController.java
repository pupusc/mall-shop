package com.wanmi.sbc.order.provider.impl.pointstrade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.pointstrade.PointsTradeProvider;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradeConfirmReceiveRequest;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradeDeliverRequest;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradeDeliveryCheckRequest;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradeRemedySellerRemarkRequest;
import com.wanmi.sbc.order.api.response.trade.TradeDeliverResponse;
import com.wanmi.sbc.order.pointstrade.service.PointsTradeService;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.request.TradeDeliverRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @ClassName PointsTradeController
 * @Description 积分订单相关接口实现类
 * @Author lvzhenwei
 * @Date 2019/5/10 13:46
 **/
@Validated
@RestController
public class PointsTradeController implements PointsTradeProvider {

    @Autowired
    private PointsTradeService pointsTradeService;

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 发货前校验
     * @Date 16:32 2019/5/21
     * @Param [pointsTradeDeliveryCheckRequest]
     **/
    @Override
    public BaseResponse deliveryCheck(@RequestBody @Valid PointsTradeDeliveryCheckRequest pointsTradeDeliveryCheckRequest) {
        pointsTradeService.deliveryCheck(pointsTradeDeliveryCheckRequest.getTid(),
                KsBeanUtil.convert(pointsTradeDeliveryCheckRequest.getTradeDeliver(), TradeDeliverRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.order.api.response.trade.TradeDeliverResponse>
     * @Author lvzhenwei
     * @Description 积分订单发货
     * @Date 15:59 2019/5/21
     * @Param [pointsTradeDeliverRequest]
     **/
    @Override
    public BaseResponse<TradeDeliverResponse> deliver(@RequestBody @Valid PointsTradeDeliverRequest pointsTradeDeliverRequest) {
        String deliverId = pointsTradeService.deliver(pointsTradeDeliverRequest.getTid(),
                KsBeanUtil.convert(pointsTradeDeliverRequest.getTradeDeliver(), TradeDeliver.class),
                pointsTradeDeliverRequest.getOperator());
        return BaseResponse.success(TradeDeliverResponse.builder().deliverId(deliverId).build());
    }

    /**
     * 积分订单确认收货
     *
     * @param pointsTradeConfirmReceiveRequest 订单编号 操作信息 {@link PointsTradeConfirmReceiveRequest}
     * @return
     */
    @Override
    public BaseResponse confirmReceive(@RequestBody @Valid PointsTradeConfirmReceiveRequest pointsTradeConfirmReceiveRequest) {
        pointsTradeService.confirmReceive(pointsTradeConfirmReceiveRequest.getTid(), pointsTradeConfirmReceiveRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 修改卖家备注
     * @Date 14:31 2019/5/22
     * @Param [pointsTradeRemedySellerRemarkRequest]
     **/
    @Override
    public BaseResponse remedySellerRemark(@RequestBody @Valid PointsTradeRemedySellerRemarkRequest pointsTradeRemedySellerRemarkRequest) {
        pointsTradeService.remedySellerRemark(pointsTradeRemedySellerRemarkRequest.getTid(),
                pointsTradeRemedySellerRemarkRequest.getSellerRemark(),
                pointsTradeRemedySellerRemarkRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }
}
