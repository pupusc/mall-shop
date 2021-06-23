package com.wanmi.sbc.paidcard;


import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.constant.PaidCardConstant;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.provider.paidcardbuyrecord.PaidCardBuyRecordQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardrule.PaidCardRuleQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleByIdRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordByIdResponse;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.PaidCardCustomerRelListResponse;
import com.wanmi.sbc.customer.bean.dto.PaidCardRedisDTO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardRuleVO;
import com.wanmi.sbc.order.api.request.trade.TradeGetPayOrderByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradePayCallBackOnlineBatchRequest;
import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
import com.wanmi.sbc.order.bean.dto.TradeDTO;
import com.wanmi.sbc.order.bean.dto.TradePayCallBackOnlineDTO;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.api.provider.AliPayProvider;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.PayExtraRequest;
import com.wanmi.sbc.pay.api.request.WxPayForJSApiRequest;
import com.wanmi.sbc.pay.api.request.WxPayForMWebRequest;
import com.wanmi.sbc.pay.api.response.WxPayForMWebResponse;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import com.wanmi.sbc.pay.bean.enums.WxPayTradeType;
import com.wanmi.sbc.third.wechat.WechatSetService;
import com.wanmi.sbc.trade.request.PayMobileRequest;
import com.wanmi.sbc.trade.request.WeiXinPayRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "PaidCardPayController", description = "付费会员支付API")
@RestController
@RequestMapping("/paidCardPay")
@Slf4j
public class PaidCardPayController {

    @Autowired
    private PaidCardRuleQueryProvider paidCardRuleQueryProvider;

    @Autowired
    private AliPayProvider aliPayProvider;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private WxPayProvider wxPayProvider;

    @Autowired
    private WechatSetService wechatSetService;

    @Autowired
    private PaidCardBuyRecordQueryProvider paidCardBuyRecordQueryProvider;

    /*
     * @Description: 支付表单
     * @Param:
     * @Author: Bob->
     * @Date: 2019-02-01 11:12
     */
    @ApiOperation(value = "H5支付宝支付表单", notes = "该请求需新打开一个空白页，返回的支付宝脚本会自动提交重定向到支付宝收银台", httpMethod = "GET")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "base64编码后的支付请求",
            required = true)
    @RequestMapping(value = "/aliPay/{encrypted}", method = RequestMethod.GET)
    @GlobalTransactional
    public void appAliPay( @PathVariable String encrypted, HttpServletResponse response) {
        String customerId = commonUtil.getOperatorId();
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PayMobileRequest payMobileRequest = JSON.parseObject(decrypted, PayMobileRequest.class);
        log.info("====================支付宝支付表单================payMobileRequest :{}", payMobileRequest);

        payMobileRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        payMobileRequest.setCustomerId(customerId);
        String form = this.alipayUtil(payMobileRequest);

        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            // TODO: 2019-01-28 gb支付异常未处理
            log.error("execute alipay has IO exception:{} ", e);
        }
    }

    /*
     * @Description: 支付之前的公共判断条件及数据组装
     * @Param:  payMobileRequest
     * @Author: Bob
     * @Date: 2019-02-22 11:27
     */
    private String alipayUtil(PayMobileRequest payMobileRequest) {

        String paidCardRuleId = payMobileRequest.getPaidCardRuleId();
        PaidCardRuleVO paidCardRuleVO = paidCardRuleQueryProvider
                .getByIdWithPaidCardInfo(PaidCardRuleByIdRequest.builder()
                        .id(paidCardRuleId).build())
                .getContext()
                .getPaidCardRuleVO();
        BigDecimal price = paidCardRuleVO.getPrice();
        String businessId = BusinessCodeGenUtils.genPaidCardBuyRecordCode();
        //将生成出来的流水号存入redis并设置过期时间
        StringBuilder key = new StringBuilder(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE);
        key.append("_").append(businessId);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key.toString(), JSON.toJSONString(PaidCardRedisDTO.builder()
                .customerId(payMobileRequest.getCustomerId())
                .ruleId(payMobileRequest.getPaidCardRuleId())
                .businessId(businessId)
                .build()));
        redisTemplate.expire(key.toString(),3L, TimeUnit.DAYS);
        PayExtraRequest payExtraRequest = new PayExtraRequest();
        payExtraRequest.setStoreId(payMobileRequest.getStoreId());
        payExtraRequest.setBusinessId(businessId);
        payExtraRequest.setChannelItemId(payMobileRequest.getChannelItemId());
        payExtraRequest.setTerminal(payMobileRequest.getTerminal());
        if (TerminalType.H5.equals(payMobileRequest.getTerminal())) {
            payExtraRequest.setSuccessUrl(payMobileRequest.getSuccessUrl());
        }
        payExtraRequest.setAmount(price);
        payExtraRequest.setOpenId(payMobileRequest.getOpenId());
        String title = paidCardRuleVO.getPaidCard().getName();
        String body = paidCardRuleVO.getName();

        log.info("=============body", body);
        log.info("=============title", title);
        payExtraRequest.setSubject(title);
        payExtraRequest.setBody(body);
        payExtraRequest.setClientIp(HttpUtil.getIpAddr());

        String form = "";
        try {
            form = aliPayProvider.getPayForm(payExtraRequest).getContext().getForm();
        } catch (SbcRuntimeException e) {
            throw new SbcRuntimeException(e.getErrorCode(), e.getParams());
        }

        return form;
    }

    /**
     * 非微信浏览器h5支付统一下单接口
     *
     * @param weiXinPayRequest
     * @return
     */
    @ApiOperation(value = "非微信浏览器h5支付统一下单接口", notes = "返回结果mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付")
    @RequestMapping(value = "/wxPayUnifiedorderForMweb", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<WxPayForMWebResponse> wxPayUnifiedorderForMweb(@RequestBody @Valid WeiXinPayRequest weiXinPayRequest) {
        //验证H5支付开关
        DefaultFlag wxOpenFlag = wechatSetService.getStatus(com.wanmi.sbc.common.enums.TerminalType.H5);
        if (DefaultFlag.NO.equals(wxOpenFlag)) {
            throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
        }
        WxPayForMWebRequest mWebRequest = new WxPayForMWebRequest();
        String paidCardRuleId = weiXinPayRequest.getPaidCardRuleId();
        PaidCardRuleVO paidCardRuleVO = paidCardRuleQueryProvider
                .getByIdWithPaidCardInfo(PaidCardRuleByIdRequest.builder()
                        .id(paidCardRuleId).build())
                .getContext()
                .getPaidCardRuleVO();
        BigDecimal price = paidCardRuleVO.getPrice();
        String businessId = BusinessCodeGenUtils.genPaidCardBuyRecordCode();

        //将生成出来的流水号存入redis并设置过期时间
        StringBuilder key = new StringBuilder(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE);
        key.append("_").append(businessId);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key.toString(), JSON.toJSONString(PaidCardRedisDTO.builder()
                .customerId(commonUtil.getOperatorId())
                .ruleId(paidCardRuleId)
                .businessId(businessId)
                .build()));
        redisTemplate.expire(key.toString(),30L, TimeUnit.DAYS);
        //订单总金额
        String body = paidCardRuleVO.getPaidCard().getName();
        mWebRequest.setBody(body + "订单");
        mWebRequest.setOut_trade_no(businessId);
        mWebRequest.setTotal_fee(price.multiply(BigDecimal.valueOf(100)).intValue()+"");
        mWebRequest.setSpbill_create_ip(HttpUtil.getIpAddr());
        mWebRequest.setTrade_type(WxPayTradeType.MWEB.toString());
        mWebRequest.setScene_info("{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://m.s2btest2.kstore.shop\"," +
                "\"wap_name\": \"h5下单支付\"}}");
        mWebRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        return wxPayProvider.wxPayForMWeb(mWebRequest);
    }


    /**
     * 微信浏览器内JSApi支付统一下单接口
     *
     * @param weiXinPayRequest
     * @return
     */
    @ApiOperation(value = "微信浏览器内JSApi支付统一下单接口", notes = "返回用于在微信内支付的所需参数")
    @RequestMapping(value = "/wxPayUnifiedorderForJSApi", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<Map<String, String>> wxPayUnifiedorderForJSApi(@RequestBody @Valid WeiXinPayRequest weiXinPayRequest) {
        //验证H5支付开关
        DefaultFlag wxOpenFlag = wechatSetService.getStatus(com.wanmi.sbc.common.enums.TerminalType.H5);
        if (DefaultFlag.NO.equals(wxOpenFlag)) {
            throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
        }
        return wxPayProvider.wxPayForJSApi(wxPayCommon(weiXinPayRequest));
    }

    /**
     * 微信内浏览器,小程序支付公用逻辑
     *
     * @param weiXinPayRequest
     * @return
     */
    private WxPayForJSApiRequest wxPayCommon(WeiXinPayRequest weiXinPayRequest) {
        WxPayForJSApiRequest jsApiRequest = new WxPayForJSApiRequest();
        String paidCardRuleId = weiXinPayRequest.getPaidCardRuleId();
        PaidCardRuleVO paidCardRuleVO = paidCardRuleQueryProvider
                .getByIdWithPaidCardInfo(PaidCardRuleByIdRequest.builder()
                        .id(paidCardRuleId).build())
                .getContext()
                .getPaidCardRuleVO();
        BigDecimal price = paidCardRuleVO.getPrice();
        String businessId = BusinessCodeGenUtils.genPaidCardBuyRecordCode();

        //将生成出来的流水号存入redis并设置过期时间
        StringBuilder key = new StringBuilder(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE);
        key.append("_").append(businessId);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key.toString(), JSON.toJSONString(PaidCardRedisDTO.builder()
                .customerId(commonUtil.getOperatorId())
                .ruleId(paidCardRuleId)
                .businessId(businessId)
                .build()));
        redisTemplate.expire(key.toString(),30L, TimeUnit.DAYS);

        String body = paidCardRuleVO.getPaidCard().getName();
        jsApiRequest.setBody(body + "订单");
        jsApiRequest.setOut_trade_no(businessId);
        jsApiRequest.setTotal_fee(price.multiply(BigDecimal.valueOf(100)).intValue()+"");
        jsApiRequest.setSpbill_create_ip(HttpUtil.getIpAddr());
        jsApiRequest.setTrade_type(WxPayTradeType.JSAPI.toString());
        jsApiRequest.setOpenid(weiXinPayRequest.getOpenid());
        jsApiRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        return jsApiRequest;
    }

    @ApiOperation(value = "查询付费卡支付状态")
    @GetMapping(value = "/get-status/{payCode}")
    public BaseResponse getStatus(@PathVariable String payCode){
        PaidCardBuyRecordByIdResponse resp = paidCardBuyRecordQueryProvider.getById(PaidCardBuyRecordByIdRequest.builder().payCode(payCode).build()).getContext();
        return BaseResponse.success(resp.getPaidCardBuyRecordVO());
    }


    @GetMapping("/test")
    public Object test(@PathVariable String  code){
        Object result = redisTemplate.opsForValue().get(code).toString();
        return result;
    }
}
