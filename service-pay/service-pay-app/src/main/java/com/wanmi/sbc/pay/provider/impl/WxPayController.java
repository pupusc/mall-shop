package com.wanmi.sbc.pay.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.*;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.service.PayDataService;
import com.wanmi.sbc.pay.service.WxPayService;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayConstants;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付接口
 */
@RestController
@Slf4j
public class WxPayController implements WxPayProvider {

    /**
     * 微信支付成功回调地址
     */
    private static final String WXPAYSUCCCALLBACK = "/tradeCallback/WXPaySuccessCallBack";

    /**
     * 微信退款成功回调地址
     */
    private static final String WXREFUNDSUCCCALLBACK = "/tradeCallback/WXPayRefundSuccessCallBack/";

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private PayDataService payDataService;

    /**
     * 统一下单接口--native扫码支付
     * @param request
     */
    @Override
    public BaseResponse<WxPayForNativeResponse> wxPayForNative(@RequestBody WxPayForNativeRequest request){
        PayGatewayConfig payGatewayConfig = payDataService.queryConfigByNameAndStoreId(PayGatewayEnum.WECHAT,request.getStoreId());
        request.setAppid(payGatewayConfig.getAppId());
        request.setMch_id(payGatewayConfig.getAccount());
        request.setNonce_str(WXPayUtil.generateNonceStr());
        request.setNotify_url(getNotifyUrl(payGatewayConfig));
        try {
            Map<String,String> nativeMap = WXPayUtil.objectToMap(request);
            nativeMap.remove("storeId");
            //获取签名
            String sign = WXPayUtil.generateSignature(nativeMap,payGatewayConfig.getApiKey());
            request.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用统一下单接口
        WxPayForNativeResponse response = wxPayService.wxPayForNative(request);
        if ("SUCCESS".equals(response.getReturn_code()) && "SUCCESS".equals(response.getResult_code())) {
            return BaseResponse.success(response);
        }
        this.throwErrMsg(response.getErr_code(), response.getErr_code_des());
        return BaseResponse.error(response.getErr_code_des());
    }

    private  String getNotifyUrl(PayGatewayConfig payGatewayConfig){
        StringBuilder notify_url = new StringBuilder();
        notify_url.append(payGatewayConfig.getBossBackUrl()+WXPAYSUCCCALLBACK);
        notify_url.append("/");
        notify_url.append(payGatewayConfig.getStoreId());
       return notify_url.toString();

    }
    /**
     * 统一下单--非微信浏览器h5支付
     * @param mWebRequest
     * @return
     */
    @Override
    public BaseResponse<WxPayForMWebResponse> wxPayForMWeb(@RequestBody WxPayForMWebRequest mWebRequest){
        PayGatewayConfig payGatewayConfig = payDataService.queryConfigByNameAndStoreId(PayGatewayEnum.WECHAT,mWebRequest.getStoreId());
        mWebRequest.setAppid(payGatewayConfig.getAppId());
        mWebRequest.setMch_id(payGatewayConfig.getAccount());
        mWebRequest.setNonce_str(WXPayUtil.generateNonceStr());
        mWebRequest.setNotify_url(getNotifyUrl(payGatewayConfig));
        try {
            Map<String,String> mwebMap = WXPayUtil.objectToMap(mWebRequest);
            mwebMap.remove("storeId");
            //获取签名
            String sign = WXPayUtil.generateSignature(mwebMap,payGatewayConfig.getApiKey());
            mWebRequest.setSign(sign);
        } catch (Exception e) {
            log.info("wxPayForMWeb exception", e);
        }
        //调用统一下单接口
        WxPayForMWebResponse response = wxPayService.wxPayForMWeb(mWebRequest);
        String out_trade_no = mWebRequest.getOut_trade_no();
        response.setPayCode(out_trade_no);
        log.info("微信下单接口返回值：{}",response);
        if ("SUCCESS".equals(response.getReturn_code()) && "SUCCESS".equals(response.getResult_code())) {
            return BaseResponse.success(response);
        }
        this.throwErrMsg(response.getErr_code(), response.getErr_code_des());
        return BaseResponse.error(response.getErr_code_des());
    }

    /**
     * 统一下单--微信浏览器内JSApi支付
     * @param jsApiRequest
     * @return
     */
    @Override
    public BaseResponse<Map<String,String>> wxPayForJSApi(@RequestBody WxPayForJSApiRequest jsApiRequest){
        PayGatewayConfig payGatewayConfig = payDataService.queryConfigByNameAndStoreId(PayGatewayEnum.WECHAT,jsApiRequest.getStoreId());
        jsApiRequest.setAppid(payGatewayConfig.getAppId());
        wxSignCommon(jsApiRequest, payGatewayConfig);
        log.info("微信支付[JSApi]统一下单接口入参:{}", jsApiRequest);
        //调用统一下单接口
        WxPayForJSApiResponse response = wxPayService.wxPayForJSApi(jsApiRequest);
        if ("SUCCESS".equals(response.getReturn_code()) && "SUCCESS".equals(response.getResult_code())) {
            return getSignResultCommon(jsApiRequest.getAppid(), payGatewayConfig.getApiKey(), response.getPrepay_id());
        }
        log.error("微信支付[JSApi]统一下单接口调用失败,入参:{},返回结果为:{}", jsApiRequest, response);
        this.throwErrMsg(response.getErr_code(), response.getErr_code_des());
        return BaseResponse.error(response.getErr_code_des());
    }

    /**
     * 统一下单--小程序内JSApi支付
     * @param jsApiRequest
     * @return
     */
    @Override
    public BaseResponse<Map<String,String>> wxPayForLittleProgram(@RequestBody WxPayForJSApiRequest jsApiRequest){
        PayGatewayConfig payGatewayConfig = payDataService.queryConfigByNameAndStoreId(PayGatewayEnum.WECHAT,jsApiRequest.getStoreId());
        wxSignCommon(jsApiRequest, payGatewayConfig);
        log.info("小程序支付[JSApi]统一下单接口入参:{}", jsApiRequest);
        //调用统一下单接口
        WxPayForJSApiResponse response = wxPayService.wxPayForJSApi(jsApiRequest);
        if("SUCCESS".equals(response.getReturn_code()) && "SUCCESS".equals(response.getResult_code())){
            return getSignResultCommon(jsApiRequest.getAppid(), payGatewayConfig.getApiKey(), response.getPrepay_id());
        }
        log.error("小程序支付[小程序]统一下单接口调用失败,入参:{},返回结果为:{}", jsApiRequest, response);
        this.throwErrMsg(response.getErr_code(), response.getErr_code_des());
        return BaseResponse.error(response.getErr_code_des());
    }

    /**
     * 微信浏览器内,小程序内签名参数公共方法
     * @param jsApiRequest
     * @param payGatewayConfig
     */
    private void wxSignCommon(@RequestBody WxPayForJSApiRequest jsApiRequest, PayGatewayConfig payGatewayConfig) {
        jsApiRequest.setMch_id(payGatewayConfig.getAccount());
        jsApiRequest.setNonce_str(WXPayUtil.generateNonceStr());
        jsApiRequest.setNotify_url(getNotifyUrl(payGatewayConfig));
        try {
            Map<String, String> mwebMap = WXPayUtil.objectToMap(jsApiRequest);
            mwebMap.remove("storeId");
            //获取签名
            String sign = WXPayUtil.generateSignature(mwebMap, payGatewayConfig.getApiKey());
            jsApiRequest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 微信浏览器内,小程序内支付,获取返回对象公共方法
     * @param appId
     * @param apiKey
     * @param prepayId
     * @return
     */
    private BaseResponse<Map<String, String>> getSignResultCommon(String appId, String apiKey, String prepayId) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("appId", appId);
        resultMap.put("timeStamp", String.valueOf(WXPayUtil.getCurrentTimestamp()));
        resultMap.put("nonceStr", WXPayUtil.generateNonceStr());
        resultMap.put("package", "prepay_id=" + prepayId);
        resultMap.put("signType", "MD5");
        try {
            resultMap.put("paySign", WXPayUtil.generateSignature(resultMap, apiKey));
        } catch (Exception e) {
            e.printStackTrace();
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        return BaseResponse.success(resultMap);
    }

    /**
     * 统一下单--微信app支付
     * @param appRequest
     * @return
     */
    @Override
    public BaseResponse<Map<String, String>> wxPayForApp(@RequestBody WxPayForAppRequest appRequest) {
        PayGatewayConfig payGatewayConfig = payDataService.queryConfigByNameAndStoreId(PayGatewayEnum.WECHAT,appRequest.getStoreId());
        appRequest.setAppid(payGatewayConfig.getOpenPlatformAppId());
        appRequest.setMch_id(payGatewayConfig.getOpenPlatformAccount());
        appRequest.setNonce_str(WXPayUtil.generateNonceStr());
        appRequest.setNotify_url(getNotifyUrl(payGatewayConfig));
        try {
            Map<String,String> appMap = WXPayUtil.objectToMap(appRequest);
            appMap.remove("storeId");
            //获取签名
            String sign = WXPayUtil.generateSignature(appMap,payGatewayConfig.getOpenPlatformApiKey());
            appRequest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        log.info("微信支付[App]统一下单接口入参:{}", appRequest);
        //调用统一下单接口
        WxPayForAppResponse response = wxPayService.wxPayForApp(appRequest);
        Map<String,String> resultMap = new HashMap<>();
        if("SUCCESS".equals(response.getReturn_code()) && "SUCCESS".equals(response.getResult_code())){
            resultMap.put("appid", payGatewayConfig.getOpenPlatformAppId());
            resultMap.put("partnerid",payGatewayConfig.getOpenPlatformAccount());
            resultMap.put("prepayid",response.getPrepay_id());
            resultMap.put("package", "Sign=WXPay");
            resultMap.put("noncestr", appRequest.getNonce_str());
            resultMap.put("timestamp", String.valueOf(WXPayUtil.getCurrentTimestamp()));
            try {
                resultMap.put("sign", WXPayUtil.generateSignature(resultMap,payGatewayConfig.getOpenPlatformApiKey()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
            }
            return BaseResponse.success(resultMap);
        }
        log.error("微信支付[app]统一下单接口调用失败,入参:{},返回结果为:{}", appRequest, response);
        this.throwErrMsg(response.getErr_code(), response.getErr_code_des());
        return BaseResponse.success(resultMap);
    }

    /**
     * @Author lvzhenwei
     * @Description 查询微信支付单详情
     * @Date 14:30 2020/9/17
     * @Param [request]
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.pay.api.response.WxPayOrderDetailReponse>
     **/
    @Override
    public BaseResponse<WxPayOrderDetailReponse> getWxPayOrderDetail(@RequestBody WxPayOrderDetailRequest request){
        WxPayOrderDetailReponse wxPayOrderDetailReponse = wxPayService.getWxPayOrderDetail(request);
        return BaseResponse.success(wxPayOrderDetailReponse);

    }

    /**
     * 微信退款
     * @param refundInfoRequest
     * @return
     */
    @Override
    public BaseResponse<WxPayRefundResponse> wxPayRefund(@RequestBody WxPayRefundInfoRequest refundInfoRequest){
        PayGatewayConfig payGatewayConfig = payDataService.queryConfigByNameAndStoreId(PayGatewayEnum.WECHAT,refundInfoRequest.getStoreId());
        String appId = payGatewayConfig.getAppId();
        String account = payGatewayConfig.getAccount();
        String apiKey = payGatewayConfig.getApiKey();
        if(refundInfoRequest.getPay_type().equals("APP")){
            appId = payGatewayConfig.getOpenPlatformAppId();
            account = payGatewayConfig.getOpenPlatformAccount();
            apiKey = payGatewayConfig.getOpenPlatformApiKey();
        }
        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
        refundRequest.setAppid(appId);
        refundRequest.setMch_id(account);
        refundRequest.setNonce_str(WXPayUtil.generateNonceStr());
        refundRequest.setOut_refund_no(refundInfoRequest.getOut_refund_no());
        refundRequest.setOut_trade_no(refundInfoRequest.getOut_trade_no());
        refundRequest.setTotal_fee(refundInfoRequest.getTotal_fee());
        refundRequest.setRefund_fee(refundInfoRequest.getRefund_fee());
        //重复支付退款不需要异步回调地址
        if(StringUtils.isNotBlank(refundInfoRequest.getRefund_type()) && !refundInfoRequest.getRefund_type().equals("REPEATPAY")){
            refundRequest.setNotify_url(payGatewayConfig.getBossBackUrl()+WXREFUNDSUCCCALLBACK+refundInfoRequest.getStoreId());
        }
        try {
            Map<String,String> refundMap = WXPayUtil.objectToMap(refundRequest);
            //获取签名
            String sign = WXPayUtil.generateSignature(refundMap,apiKey);
            refundRequest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WxPayRefundResponse wxPayRefundResponse = wxPayService.wxPayRefund(refundRequest,
                refundInfoRequest.getPay_type(), refundInfoRequest.getStoreId());
        return BaseResponse.success(wxPayRefundResponse);
    }

    /**
     * 微信支付--微信企业付款到零钱
     * @param request
     * @return
     */
    @Override
    public BaseResponse<WxPayCompanyPaymentRsponse> wxPayCompanyPayment(@RequestBody WxPayCompanyPaymentInfoRequest request){
        WxPayCompanyPaymentRsponse wxPayCompanyPaymentRsponse = wxPayService.wxPayCompanyPayment(request);
        return BaseResponse.success(wxPayCompanyPaymentRsponse);
    }

    /**
     * 微信支付公共异常方法
     * @param code
     * @param msg
     */
    private void throwErrMsg(String code, String msg) throws SbcRuntimeException{
        if ("ORDERPAID".equals(code)) {
            //订单已支付
            throw new SbcRuntimeException("K-100210");
        } else if ("NOTENOUGH".equalsIgnoreCase(code)
                || "ORDERCLOSED".equalsIgnoreCase(code)
                || "OUT_TRADE_NO_USED".equalsIgnoreCase(code)) {
            //订单超时、关闭、单号重复
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{msg});
        } else {
            throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
        }
    }
}
