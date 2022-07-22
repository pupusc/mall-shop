package com.wanmi.sbc.pay.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.PayResponse;
import com.wanmi.sbc.pay.api.response.RefundResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>支付Provider</p>
 * Created by of628 on 2018-08-13-下午8:15.
 */
@FeignClient(value = "${application.pay.name}", contextId = "PayProvider")
public interface PayProvider {

    /**
     * 新增支付网关
     *
     * @param gatewayAddRequest 新增支付网关request{@link GatewayAddRequest}
     * @return BaseResponse
     */
    @PostMapping("/pay/${application.pay.version}/add-gateway")
    BaseResponse addGateway(@RequestBody @Valid GatewayAddRequest gatewayAddRequest);

    /**
     * 修改支付网关基础信息
     *
     * @param gatewayModifyRequest
     * @return BaseResponse
     */
    @PostMapping("/pay/${application.pay.version}/modify-gateway")
    BaseResponse modifyGateway(@RequestBody @Valid GatewayModifyRequest gatewayModifyRequest);

    /**
     * 保存支付渠道项，若id为空，默认新增
     *
     * @param channelItemSaveRequest 支付渠道项新增结构 {@link ChannelItemSaveRequest}
     * @return BaseResponse
     */
    @PostMapping("/pay/${application.pay.version}/add-channel-item")
    BaseResponse saveChannelItem(@RequestBody @Valid ChannelItemSaveRequest channelItemSaveRequest);

    /**
     * 保存网关配置信息，如果id为空，则默认新增
     *
     * @param gatewayConfigSaveRequest 网关配置新增数据结构 {@link GatewayAddRequest}
     * @return BaseResponse
     */
    @PostMapping("/pay/${application.pay.version}/add-gateway-config")
    BaseResponse saveGatewayConfig(@RequestBody @Valid GatewayConfigSaveRequest gatewayConfigSaveRequest);


    /**
     * 获取支付对象，这里返回到的支付对象实际为支付凭证，由前端获取后通过JS请求第三方支付
     *
     * @param payRequest 支付请求对象 {@link PayRequest}
     * @return 支付凭证
     */
    @PostMapping("/pay/${application.pay.version}/get-pay-charge")
    BaseResponse<PayResponse> getPayCharge(@RequestBody @Valid PayExtraRequest payRequest);

    /**
     * 退款请求，当退款操作请求成功后，返回的退款对象
     *
     * @param refundRequest 支付请求对象 {@link PayRequest}
     * @return 退款对象
     */
    @PostMapping("/pay/${application.pay.version}/refund")
    BaseResponse<RefundResponse> refund(@RequestBody @Valid RefundRequest refundRequest);

    /**
     * 支付/退款回调，接口不执行业务触发，只做更新交易记录操作
     *
     * @param request 回调请求参数 {@link TradeCallbackRequest}
     * @return BaseResponse
     */
    @PostMapping("/pay/${application.pay.version}/callback")
    BaseResponse callback(@RequestBody @Valid TradeCallbackRequest request);

    /**
     * 银联企业支付
     *
     * @param unionPay
     * @return 请求HTML
     */
    @PostMapping("/pay/${application.pay.version}/unionB2BPay")
    BaseResponse<String> unionB2BPay(@RequestBody @Valid UnionPayRequest unionPay);

    /**
     * 银联企业支付 签名校验
     *
     * @param validateData 验签参数
     * @return 验签是否成功
     */
    @PostMapping("/pay/${application.pay.version}/unionCheckSign")
    BaseResponse<Boolean> unionCheckSign(@RequestBody Map<String, String> validateData);

    /**
     * 银联企业支付 同步回调添加交易数据
     *
     * @param resMap
     * @return
     */
    @PostMapping("/pay/${application.pay.version}/unionCallBack")
    BaseResponse unionCallBack(@RequestBody Map<String, String> resMap);

    /**
     * 保存支付配置
     *
     * @param payGatewaySaveRequest 保存支付配置request{@link PayGatewaySaveRequest}
     * @return BaseResponse
     */
    @PostMapping("/pay/${application.pay.version}/save-pay-gateway")
    BaseResponse savePayGateway(@RequestBody @Valid PayGatewaySaveRequest payGatewaySaveRequest);

    /**
     * 微信支付 同步回调添加交易数据
     *
     * @param request
     * @return
     */
    @PostMapping("/pay/${application.pay.version}/wx-pay-call-back")
    BaseResponse wxPayCallBack(@RequestBody PayTradeRecordRequest request);

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 上传微信支付证书
     * @Date 14:22 2019/5/7
     * @Param [request]
     **/
    @PostMapping("/pay/${application.pay.version}/upload-pay-certificate")
    BaseResponse uploadPayCertificate(@RequestBody PayGatewayUploadPayCertificateRequest request);

    /**
     * 新增交易记录
     *
     * @param request
     * @return
     */
    @PostMapping("/pay/${application.pay.version}/save-pay-trade-record")
    BaseResponse savePayTradeRecord(@RequestBody PayTradeRecordRequest request);

    /**
     * 批量添加交易记录
     *
     * @param payTradeRecordRequestList
     * @return
     */
    @PostMapping("/pay/${application.pay.version}/batch-save-pay-trade-record")
    BaseResponse batchSavePayTradeRecord(@RequestBody List<PayTradeRecordRequest> payTradeRecordRequestList);

    /**
     * 根据不同终端保存支付配置
     *
     * @param request
     * @return
     */
    @PostMapping("/pay/${application.pay.version}/add-gateway-config-terminal-type")
    BaseResponse savePayGatewayByTerminalType(@RequestBody @Valid PayGatewaySaveByTerminalTypeRequest request);

    @PostMapping("/pay/${application.pay.version}/save-appid/{appId}/{tradeNO}")
    BaseResponse saveAppId(@PathVariable("appId") String appId, @PathVariable("tradeNo") String tradeNo);
}
