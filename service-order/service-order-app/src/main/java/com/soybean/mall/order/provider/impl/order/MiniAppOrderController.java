package com.soybean.mall.order.provider.impl.order;

import com.soybean.mall.order.api.provider.order.MiniAppOrderProvider;
import com.soybean.mall.order.api.request.order.CreateWxOrderAndPayRequest;
import com.soybean.mall.order.api.request.order.GetPaymentParamsRequest;
import com.soybean.mall.order.api.request.order.TradeOrderReportRequest;
import com.soybean.mall.order.api.request.order.WxMiniProgramCallbackRequest;
import com.soybean.mall.order.bean.vo.MiniProgramOrderReportVO;
import com.soybean.mall.order.bean.vo.WxOrderPaymentParamsVO;
import com.soybean.mall.order.miniapp.model.root.WxMiniProgramCallbackModel;
import com.soybean.mall.order.miniapp.repository.WxMiniProgramCallbackRepository;
import com.soybean.mall.order.miniapp.service.TradeOrderService;
import com.soybean.mall.order.miniapp.service.WxOrderService;
import com.soybean.mall.wx.mini.order.bean.dto.PaymentParamsDTO;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayBatchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
public class MiniAppOrderController implements MiniAppOrderProvider {

    @Autowired
    private TradeOrderService tradeOrderService;

    @Autowired
    private WxOrderService wxOrderService;

    @Autowired
    private WxMiniProgramCallbackRepository wxMiniProgramCallbackRepository;

    /**
     * 同步订单到微信
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchSyncDeliveryStatusToWechat(@RequestBody @Valid ProviderTradeErpRequest request) {
        tradeOrderService.batchSyncDeliveryStatusToWechat(request.getPageSize(),request.getPtid());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 小程序0元订单推送到小程序并支付同步
     * @param request
     * @return
     */
    @Override
    public BaseResponse createWxOrderAndPay(@RequestBody TradeDefaultPayBatchRequest request) {
        tradeOrderService.createWxOrderAndPay(request.getTradeIds().get(0));
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addOrderReportCache(@RequestBody TradeOrderReportRequest request) {
        wxOrderService.orderReportCache(request.getTid());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<Long> addCallback(@RequestBody WxMiniProgramCallbackRequest wxMiniProgramCallbackRequest){
        WxMiniProgramCallbackModel wxMiniProgramCallbackModel = WxMiniProgramCallbackModel.fromCreateRequest(wxMiniProgramCallbackRequest);
        WxMiniProgramCallbackModel save = wxMiniProgramCallbackRepository.save(wxMiniProgramCallbackModel);
        return BaseResponse.success(save.getId());
    }

    @Override
    public BaseResponse updateCallback(@RequestBody WxMiniProgramCallbackRequest wxMiniProgramCallbackRequest){
        wxMiniProgramCallbackRepository.updateStatus(wxMiniProgramCallbackRequest.getStatus(), wxMiniProgramCallbackRequest.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<MiniProgramOrderReportVO> getOrderReportCache() {
        return BaseResponse.success(wxOrderService.getMiniProgramOrderReportCache());
    }


    @Override
    public BaseResponse<WxOrderPaymentParamsVO> getWxOrderPaymentParams(GetPaymentParamsRequest request) {
        PaymentParamsDTO paymentParamsDTO = tradeOrderService.createWxOrderAndGetPaymentsParams(request.getTid());
        return BaseResponse.success(KsBeanUtil.convert(paymentParamsDTO,WxOrderPaymentParamsVO.class));
    }
}
