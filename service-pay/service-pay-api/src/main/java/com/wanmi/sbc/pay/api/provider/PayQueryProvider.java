package com.wanmi.sbc.pay.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.*;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>支付查询Provider</p>
 * Created by of628-wenzhi on 2018-08-13-下午3:12.
 */
@FeignClient(value = "${application.pay.name}", contextId = "PayQueryProvider")
public interface PayQueryProvider {

    /**
     * 根据id查询交易记录
     *
     * @param tradeRecordByIdRequest 包含记录id的查询参数 {@link TradeRecordByIdRequest}
     * @return 交易记录 {@link PayTradeRecordResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-trade-record-by-id")
    BaseResponse<PayTradeRecordResponse> getTradeRecordById(@RequestBody @Valid TradeRecordByIdRequest
                                                                    tradeRecordByIdRequest);

    /**
     * 根据支付/退款对象id查询交易记录
     *
     * @param recordByChangeRequest 包含支付/退款对象id的查询参数 {@link TradeRecordByChargeRequest}
     * @return 交易记录 {@link PayTradeRecordResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-trade-record-by-charge-id")
    BaseResponse<PayTradeRecordResponse> getTradeRecordByChargeId(@RequestBody @Valid TradeRecordByChargeRequest
                                                                          recordByChangeRequest);

    /**
     * 根据业务订单/退单号查询交易记录
     *
     * @param recordByOrderCodeRequest 包含业务订单/退单号的查询参数 {@link TradeRecordByOrderCodeRequest}
     * @return 交易记录 {@link PayTradeRecordResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-trade-record-by-order-code")
    BaseResponse<PayTradeRecordResponse> getTradeRecordByOrderCode(@RequestBody @Valid TradeRecordByOrderCodeRequest
                                                                           recordByOrderCodeRequest);

    /**
     * 根据业务订单/退单号统计交易记录数
     *
     * @param recordCountByOrderCodeRequest 包含业务订单/退单号的查询参数 {@link TradeRecordCountByOrderCodeRequest}
     * @return 交易记录 {@link PayTradeRecordCountResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-trade-record-count-by-order-code")
    BaseResponse<PayTradeRecordCountResponse> getTradeRecordCountByOrderCode(
            @RequestBody @Valid TradeRecordCountByOrderCodeRequest recordCountByOrderCodeRequest);


    /**
     * 根据业务父子订单号查询交易记录，若子订单查不出，则匹配父订单号
     *
     * @param tradeRecordByOrderOrParentCodeRequest 包含业务订单/退单号的查询参数
     *                                              {@link TradeRecordByOrderOrParentCodeRequest}
     * @return 交易记录 {@link PayTradeRecordResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-trade-record-by-order-or-parent-code")
    BaseResponse<PayTradeRecordResponse> getTradeRecordByOrderOrParentCode(@RequestBody @Valid TradeRecordByOrderOrParentCodeRequest
                                                                                   tradeRecordByOrderOrParentCodeRequest);

    /**
     * 根据业务父子订单号统计交易记录数，若子订单查不出，则匹配父订单号
     *
     * @param tradeRecordCountByOrderOrParentCodeRequest 包含业务订单/退单号的查询参数
     *                                                   {@link TradeRecordCountByOrderOrParentCodeRequest}
     * @return 交易记录 {@link PayTradeRecordCountResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-trade-record-count-by-order-or-parent-code")
    BaseResponse<PayTradeRecordCountResponse> getTradeRecordCountByOrderOrParentCode(
            @RequestBody @Valid TradeRecordCountByOrderOrParentCodeRequest tradeRecordCountByOrderOrParentCodeRequest);

    /**
     * 获取网关列表
     *
     * @return 网关列表，只包含网关基础信息，不带出该网关支付渠道项与配置信息 {@link PayGatewayListResponse}
     */
    @PostMapping("/pay/${application.pay.version}/list-gateway")
    BaseResponse<PayGatewayListResponse> listGatewayByStoreId(@RequestBody @Valid GatewayByStoreIdRequest request);

    /**
     * 根据id获取支付网关
     *
     * @param gatewayByIdRequest 包含支付网关id {@link GatewayByIdRequest}
     * @return 支付网关信息 {@link PayGatewayResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-gateway-by-id")
    BaseResponse<PayGatewayResponse> getGatewayById(@RequestBody @Valid GatewayByIdRequest gatewayByIdRequest);


    /**
     * 根据支付网关id查询支付渠道项列表
     *
     * @param channelItemByGatewayRequest 包含网关id {@link ChannelItemByGatewayRequest}
     * @return 支付渠道项列表 {@link PayChannelItemListResponse}
     */
    @PostMapping("/pay/${application.pay.version}/list-channel-item-by-gateway-name")
    BaseResponse<PayChannelItemListResponse> listChannelItemByGatewayName(@RequestBody @Valid
                                                                                ChannelItemByGatewayRequest
                                                                                channelItemByGatewayRequest);

    /**
     * 根据网关id和终端类型获取已开启的支付渠道项列表
     *
     * @param openedChannelItemRequest 包含网关id和终端类型 {@link OpenedChannelItemRequest}
     * @return 支付渠道项列表 {@link PayChannelItemListResponse}
     */
    @PostMapping("/pay/${application.pay.version}/list-opened-channel-item-by-gateway-name")
    BaseResponse<PayChannelItemListResponse> listOpenedChannelItemByGatewayName(@RequestBody @Valid
                                                                                      OpenedChannelItemRequest
                                                                                      openedChannelItemRequest);

    /**
     * 根据id查询支付渠道
     *
     * @param channelItemByIdRequest 包含支付渠道项id
     * @return 支付渠道项列表 {@link PayChannelItemResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-channel-item-by-id")
    BaseResponse<PayChannelItemResponse> getChannelItemById(@RequestBody @Valid ChannelItemByIdRequest
                                                                    channelItemByIdRequest);


    /**
     * 根据id查询支付网关配置
     *
     * @param gatewayConfigByIdRequest 包含支付网关配置id {@link GatewayConfigByIdRequest}
     * @return 支付网关配置信息 {@link PayGatewayConfigResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-gateway-config-by-id")
    BaseResponse<PayGatewayConfigResponse> getGatewayConfigById(@RequestBody @Valid GatewayConfigByIdRequest
                                                                        gatewayConfigByIdRequest);

    /**
     * 根据网关名称查询支付网关配置
     *
     * @param gatewayConfigByGatewayRequest 包含支付网关枚举 {@link GatewayConfigByGatewayRequest}
     * @return 支付网关配置信息 {@link PayGatewayConfigResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-gateway-config-by-gateway")
    BaseResponse<PayGatewayConfigResponse> getGatewayConfigByGateway(@RequestBody @Valid GatewayConfigByGatewayRequest
                                                                             gatewayConfigByGatewayRequest);

    /**
     * 根据网关名称查询支付网关配置
     *
     * @param gatewayConfigByGatewayRequest 包含支付网关枚举 {@link GatewayConfigByGatewayRequest}
     * @return 支付网关配置信息 {@link PayGatewayConfigResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-gateway-config-by-gateway-un-exception")
    BaseResponse<PayGatewayConfigResponse> getGatewayConfigByGatewayUnException(@RequestBody @Valid GatewayConfigByGatewayRequest
                                                                             gatewayConfigByGatewayRequest);

    /**
     * 根据网关id查询支付网关配置
     *
     * @param gatewayConfigByGatewayIdRequest 包含支付网关枚举 {@link GatewayConfigByGatewayIdRequest}
     * @return 支付网关配置信息 {@link PayGatewayConfigResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-gateway-config-by-gateway-id")
    BaseResponse<PayGatewayConfigResponse> getGatewayConfigByGatewayId(@RequestBody @Valid
                                                                               GatewayConfigByGatewayIdRequest
                                                                               gatewayConfigByGatewayIdRequest);

    /**
     * 查询所有已开启网关的配置列表
     *
     * @return 支付网关配置信息列表 {@link PayGatewayConfigListResponse}
     */
    @PostMapping("/pay/${application.pay.version}/list-opened-gateway-config")
    BaseResponse<PayGatewayConfigListResponse> listOpenedGatewayConfig(@RequestBody @Valid GatewayOpenedByStoreIdRequest gatewayOpenedByStoreIdRequest);

    /**
     * 根据订单号查询支付结果
     *
     * @param payResultByOrdercodeRequest 包含订单号的请求参数 {@link PayResultByOrdercodeRequest}
     * @return 返回信息包含支付状态 {@link PayResultResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-pay-response-by-ordercode")
    BaseResponse<PayResultResponse> getPayResponseByOrdercode(@RequestBody @Valid PayResultByOrdercodeRequest
                                                                      payResultByOrdercodeRequest);

    /**
     * 根据订单号和退单号查询退款结果
     *
     * @param refundResultByOrdercodeRequest 包含订单号和退单号的请求参数 {@link RefundResultByOrdercodeRequest}
     * @return 返回信息包含退款状态 {@link RefundResultResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-refund-response-by-ordercode")
    BaseResponse<RefundResultResponse> getRefundResponseByOrdercode(@RequestBody @Valid RefundResultByOrdercodeRequest
                                                                            refundResultByOrdercodeRequest);

    /**
     * 根据授权码向第三方获取授权用户微信openId
     *
     * @param wxCodeRequest 包含授权码的请求参数结构 {@link WxCodeRequest}
     * @return 返回包含用户微信openId {@link WxOpenIdResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-wx-open-id-by-code")
    BaseResponse<WxOpenIdResponse> getWxOpenIdByCodeAndStoreId(@RequestBody @Valid WxCodeRequest wxCodeRequest);

    /**
     * 交易状态查询交易
     *
     * @param unionPay
     * @return 结果码
     */
    @PostMapping("/pay/${application.pay.version}/getUnionPayResult")
    BaseResponse<Map<String, String>> getUnionPayResult(@RequestBody UnionPayRequest unionPay);


    /**
     * 初始化店铺获取网关列表
     *
     * @return 网关列表，只包含网关基础信息，不带出该网关支付渠道项与配置信息 {@link PayGatewayListResponse}
     */
    @PostMapping("/pay/${application.pay.version}/init-list-gateway")
    BaseResponse<PayGatewayListResponse> initGatewayByStoreId(@RequestBody @Valid GatewayInitByStoreIdRequest request);

    /**
     * 初始化店铺获取网关列表
     *
     * @return 网关列表，只包含网关基础信息，不带出该网关支付渠道项与配置信息 {@link PayGatewayListResponse}
     */
    @PostMapping("/pay/${application.pay.version}/get-all-channel-item")
    BaseResponse<List<PayChannelItemVO>> getAllChannelItem();


//    /**
//     * 获取支付配置信息
//     * @param req
//     * @return
//     */
//    BaseResponse<PayGatewayConfigResponse> queryConfigByAccountAndStoreId(@RequestBody GatewayConfigByGatewayRequest req);

    /**
     * 获取支付配置信息
     * @param req
     * @return
     */
    @PostMapping("/pay/${application.pay.version}/query-config-by-appId-storeId")
    BaseResponse<PayGatewayConfigResponse> queryConfigByAppIdAndStoreId(@RequestBody GatewayConfigByGatewayRequest req);

    /**
     * 获取支付配置信息
     * @param req
     * @return
     */
    @PostMapping("/pay/${application.pay.version}/query-config-by-appId")
    BaseResponse<PayGatewayConfigResponse> queryConfigByAppId(@RequestBody GatewayConfigByGatewayRequest req);
}
