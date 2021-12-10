package com.wanmi.sbc.trade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsDetailProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailAddRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsModifyRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdResponse;
import com.wanmi.sbc.account.bean.enums.FundsStatus;
import com.wanmi.sbc.account.bean.enums.FundsSubType;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerCheckPayPasswordRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.elastic.api.provider.customerFunds.EsCustomerFundsProvider;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsModifyRequest;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsProvider;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.TradeListByParentIdResponse;
import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
import com.wanmi.sbc.order.bean.dto.TradeDTO;
import com.wanmi.sbc.order.bean.dto.TradePayCallBackOnlineDTO;
import com.wanmi.sbc.order.bean.dto.TradeUpdateDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.api.provider.AliPayProvider;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.PayGatewayConfigResponse;
import com.wanmi.sbc.pay.api.response.WxPayForMWebResponse;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import com.wanmi.sbc.pay.bean.enums.WxPayTradeType;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.bean.vo.PayGatewayConfigVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.provider.storewechatminiprogramconfig.StoreWechatMiniProgramConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.MiniProgramSetGetResponse;
import com.wanmi.sbc.third.login.api.WechatApi;
import com.wanmi.sbc.third.login.response.GetAccessTokeResponse;
import com.wanmi.sbc.third.login.response.LittleProgramAuthResponse;
import com.wanmi.sbc.third.wechat.WechatSetService;
import com.wanmi.sbc.trade.request.PayMobileRequest;
import com.wanmi.sbc.trade.request.WeiXinPayRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 支付
 * Created by sunkun on 2017/8/10.
 */
@RestController
@RequestMapping("/pay")
@Validated
@Api(tags = "PayController", description = "mobile 支付")
@Slf4j
public class PayController {

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private PayServiceHelper payServiceHelper;

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private WxPayProvider wxPayProvider;

    @Autowired
    private AliPayProvider aliPayProvider;

    @Autowired
    private WechatApi wechatApi;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private CustomerFundsProvider customerFundsProvider;

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    @Autowired
    private CustomerFundsDetailProvider customerFundsDetailProvider;

    @Autowired
    private CustomerSiteProvider customerSiteProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StoreWechatMiniProgramConfigQueryProvider storeWechatMiniProgramConfigQueryProvider;

    @Autowired
    private ProviderTradeQueryProvider providerTradeQueryProvider;

    @Autowired
    private ProviderTradeProvider providerTradeProvider;

    @Autowired
    private AppointmentSaleGoodsProvider appointmentSaleGoodsProvider;

    @Autowired
    private WechatSetService wechatSetService;

    @Autowired
    private EsCustomerFundsProvider esCustomerFundsProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 创建Charges
     *
     * @param payMobileRequest
     * @return
     */
    @ApiOperation(value = "创建Charges", notes = "返回的支付对象实际为支付凭证，由前端获取后通过JS请求第三方支付")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @MultiSubmit
    @GlobalTransactional
    public BaseResponse<Object> create(@RequestBody @Valid PayMobileRequest payMobileRequest) {
        List<TradeVO> trades = payServiceHelper.findTrades(payMobileRequest.getTid());
        payServiceHelper.checkPayBefore(trades);
        TradeVO trade = trades.get(0);
        PayOrderVO payOrder =
                tradeQueryProvider.getPayOrderById(TradeGetPayOrderByIdRequest.builder().payOrderId(trade.getPayOrderId()).build()).getContext().getPayOrder();
        PayExtraRequest payExtraRequest = new PayExtraRequest();
        if (payMobileRequest.getSuccessUrl() != null) {
            payExtraRequest.setSuccessUrl(payMobileRequest.getSuccessUrl());
        }
        payExtraRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        payExtraRequest.setBusinessId(payMobileRequest.getTid());
        payExtraRequest.setChannelItemId(payMobileRequest.getChannelItemId());
        payExtraRequest.setTerminal(payMobileRequest.getTerminal());
        payExtraRequest.setAmount(trade.getTradePrice().getTotalPrice());
        //TODO 订单标题及订单描述待添加
        payExtraRequest.setOpenId(payMobileRequest.getOpenId());
        String title = trade.getTradeItems().get(0).getSkuName();
        String body = trade.getTradeItems().get(0).getSkuName() + " " + (trade.getTradeItems().get(0).getSpecDetails
                () == null ? "" : trade.getTradeItems().get(0).getSpecDetails());
        if (trade.getTradeItems().size() > 1) {
            if (title.length() > 23) {
//                title = String.format("s%s% %s", title.substring(0, 22), "...", "  等多件商品");
                title = title.substring(0, 22) + "...  等多件商品";
            } else {
                title = title + " 等多件商品";
            }
            body = body + " 等多件商品";
        } else {
            if (title.length() > 29) {
                title = title.substring(0, 28) + "...";
            }
        }

        payExtraRequest.setSubject(title);
        payExtraRequest.setBody(body);

        payExtraRequest.setClientIp(HttpUtil.getIpAddr());
        Object object;
        try {
            object = payProvider.getPayCharge(payExtraRequest).getContext().getObject();
        } catch (SbcRuntimeException e) {
            if (e.getErrorCode() != null && e.getErrorCode().equals("K-100203")) {
                Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("1").name("system")
                        .account("system").platform
                                (Platform.BOSS).build();
                tradeProvider.payCallBackOnline(TradePayCallBackOnlineRequest.builder()
                        .trade(KsBeanUtil.convert(trade, TradeDTO.class))
                        .payOrderOld(KsBeanUtil.convert(payOrder, PayOrderDTO.class))
                        .operator(operator)
                        .build());
            }
            throw new SbcRuntimeException(e.getErrorCode(), e.getParams());
        }
        return BaseResponse.success(object);
    }


    /**
     * 获取支付网关配置
     *
     * @return
     */
    @ApiOperation(value = "获取支付网关配置")
    @RequestMapping(value = "/getWxConfig", method = RequestMethod.GET)
    public BaseResponse<String> getWxConfig() {
        GatewayOpenedByStoreIdRequest request = new GatewayOpenedByStoreIdRequest();
        request.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        List<PayGatewayConfigVO> payGatewayConfigs = payQueryProvider.listOpenedGatewayConfig(request).getContext()
                .getGatewayConfigVOList();
        Optional<PayGatewayConfigVO> optional = payGatewayConfigs.stream().filter(
                c ->
                        c.getPayGateway().getName() == PayGatewayEnum.WECHAT
        ).findFirst();
        return BaseResponse.success(optional.get().getAppId2());
    }

    /**
     * 获取可用支付项
     *
     * @return
     */
    @ApiOperation(value = "获取可用支付项")
    @RequestMapping(value = "/items/{type}", method = RequestMethod.GET)
    public BaseResponse<List<PayChannelItemVO>> items(@PathVariable String type) {
        GatewayOpenedByStoreIdRequest request = new GatewayOpenedByStoreIdRequest();
        request.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        List<PayGatewayConfigVO> payGatewayConfigList = payQueryProvider.listOpenedGatewayConfig(request).getContext()
                .getGatewayConfigVOList();

        DefaultFlag wxOpenFlag = wechatSetService.getStatus(com.wanmi.sbc.common.enums.TerminalType.valueOf(type));
        //如果小程序，暂时传H5支付类型
        if(com.wanmi.sbc.common.enums.TerminalType.MINI.name().equals(type)){
            type = TerminalType.H5.name();
        }
        final String terminalType = type;

        List<PayChannelItemVO> itemList = new ArrayList<>();
        payGatewayConfigList.forEach(config -> {
            List<PayChannelItemVO> payChannelItemList = payQueryProvider.listOpenedChannelItemByGatewayName(new
                    OpenedChannelItemRequest(
                    config.getPayGateway().getName(), TerminalType.valueOf(terminalType))).getContext().getPayChannelItemVOList();
            if (CollectionUtils.isNotEmpty(payChannelItemList)) {
                itemList.addAll(payChannelItemList);
            }
        });

        //如果是关的，则关闭渠道
        if(DefaultFlag.NO.equals(wxOpenFlag)) {
            itemList.stream().filter(i -> PayWay.WECHAT.name().equalsIgnoreCase(i.getChannel())).forEach(i -> {
                i.setIsOpen(IsOpen.NO);
            });
        }
        return BaseResponse.success(itemList);
    }

    /**
     * 获取微信openid
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "获取微信openid")
    @RequestMapping(value = "/getWxOpenId/{code}", method = RequestMethod.GET)
    public BaseResponse<String> getWxOpenId(@PathVariable String code) {
        WxCodeRequest wxCodeRequest = new WxCodeRequest();
        wxCodeRequest.setCode(code);
        wxCodeRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        return BaseResponse.success(payQueryProvider.getWxOpenIdByCodeAndStoreId(wxCodeRequest).getContext()
                .getOpenId());
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
        String id = payServiceHelper.getPayBusinessId(weiXinPayRequest.getTid(), weiXinPayRequest.getParentTid());
        List<TradeVO> trades = payServiceHelper.checkTrades(id);
        //订单总金额
        String totalPrice = payServiceHelper.calcTotalPriceByPenny(trades).toString();
        String body = payServiceHelper.buildBody(trades);
        mWebRequest.setBody(body + "订单");
        mWebRequest.setOut_trade_no(id);
        mWebRequest.setTotal_fee(totalPrice);
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
     * 小程序内JSApi支付统一下单接口
     *
     * @param weiXinPayRequest
     * @return
     */
    @ApiOperation(value = "小程序内JSApi支付统一下单接口", notes = "返回用于在小程序内支付的所需参数")
    @RequestMapping(value = "/wxPayUnifiedorderForLittleProgram", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<Map<String, String>> wxPayUnifiedorderForLittleProgram(@RequestBody @Valid WeiXinPayRequest weiXinPayRequest) {

        String appId;
        BaseResponse<MiniProgramSetGetResponse> baseResponse = wechatAuthProvider.getMiniProgramSet();
        if (baseResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
            //验证小程序支付开关
            if(Constants.no.equals(baseResponse.getContext().getStatus())){
                throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
            }
            JSONObject json = JSON.parseObject(baseResponse.getContext().getContext());
            appId = json.getString("appId");
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        WxPayForJSApiRequest req = wxPayCommon(weiXinPayRequest);
        req.setAppid(appId);
        return wxPayProvider.wxPayForLittleProgram(req);
    }

    /**
     * 微信内浏览器,小程序支付公用逻辑
     *
     * @param weiXinPayRequest
     * @return
     */
    private WxPayForJSApiRequest wxPayCommon(WeiXinPayRequest weiXinPayRequest) {
        WxPayForJSApiRequest jsApiRequest = new WxPayForJSApiRequest();
        String id = payServiceHelper.getPayBusinessId(weiXinPayRequest.getTid(), weiXinPayRequest.getParentTid());
        List<TradeVO> trades = payServiceHelper.checkTrades(id);
        //订单总金额
        String totalPrice = payServiceHelper.calcTotalPriceByPenny(trades).toString();
        String body = payServiceHelper.buildBody(trades);
        jsApiRequest.setBody(body + "订单");
        jsApiRequest.setOut_trade_no(id);
        jsApiRequest.setTotal_fee(totalPrice);
        jsApiRequest.setSpbill_create_ip(HttpUtil.getIpAddr());
        jsApiRequest.setTrade_type(WxPayTradeType.JSAPI.toString());
        jsApiRequest.setOpenid(weiXinPayRequest.getOpenid());
        jsApiRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        return jsApiRequest;
    }


    /**
     * 微信app支付统一下单接口
     *
     * @param weiXinPayRequest
     * @return
     */
    @ApiOperation(value = " 微信app支付统一下单接口", notes = "返回用于app内支付的所需参数")
    @RequestMapping(value = "/wxPayUnifiedorderForApp", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<Map<String, String>> wxPayUnifiedorderForApp(@RequestBody @Valid WeiXinPayRequest weiXinPayRequest) {
        //验证H5支付开关
        DefaultFlag wxOpenFlag = wechatSetService.getStatus(com.wanmi.sbc.common.enums.TerminalType.APP);
        if (DefaultFlag.NO.equals(wxOpenFlag)) {
            throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
        }
        WxPayForAppRequest appRequest = new WxPayForAppRequest();
        String id = payServiceHelper.getPayBusinessId(weiXinPayRequest.getTid(), weiXinPayRequest.getParentTid());
        List<TradeVO> trades = payServiceHelper.checkTrades(id);
        //订单总金额
        String totalPrice = payServiceHelper.calcTotalPriceByPenny(trades).toString();
        String body = payServiceHelper.buildBody(trades);
        appRequest.setBody(body + "订单");
        appRequest.setOut_trade_no(id);
        appRequest.setTotal_fee(totalPrice);
        appRequest.setSpbill_create_ip(HttpUtil.getIpAddr());
        appRequest.setTrade_type(WxPayTradeType.APP.toString());
        appRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        return wxPayProvider.wxPayForApp(appRequest);
    }


    @ApiOperation(value = "支付校验", notes = "支付前校验是否已支付成功", httpMethod = "GET")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单号", required = true)
    @RequestMapping(value = "/aliPay/check/{tid}", method = RequestMethod.GET)
    public BaseResponse checkPayState(@PathVariable String tid) {
        Boolean check = redisService.setNx("pay:" + tid, "1", 4L);
        if(BooleanUtils.isFalse(check)){
            throw new SbcRuntimeException("请稍后再试");
        }
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                .tid(tid).build()).getContext().getTradeVO();
        //0:未支付
        String flag = "0";
        if (Objects.nonNull(trade)) {
            if (Objects.nonNull(trade.getTradeState())) {
                LocalDateTime orderTimeOut = trade.getOrderTimeOut();
                //已支付
                if (PayState.PAID.equals(trade.getTradeState().getPayState())) {
                    flag = "1";
                }
                //已超过未支付取消订单时间或者已作废
                if (FlowState.VOID.equals(trade.getTradeState().getFlowState()) || Objects.nonNull(orderTimeOut) && orderTimeOut.isBefore(LocalDateTime.now())) {
                    flag = "2";
                }
            }
        }
        return BaseResponse.success(flag);
    }

    @ApiOperation(value = "微信支付订单支付状态校验", notes = "微信支付后校验是否已支付成功", httpMethod = "GET")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单号", required = true)
    @RequestMapping(value = "/weiXinPay/checkOrderPayState/{tid}", method = RequestMethod.GET)
    public BaseResponse checkOrderPayState(@PathVariable String tid) {
        Boolean check = redisService.setNx("pay:" + tid, "1", 4L);
        if(BooleanUtils.isFalse(check)){
            throw new SbcRuntimeException("请稍后再试");
        }
        String flag = "0";
        List<TradeVO> tradeVOList = new ArrayList<>();
        if (tid.startsWith(GeneratorService._PREFIX_TRADE_ID)) {
            tradeVOList.add(tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                    .tid(tid).build()).getContext().getTradeVO());
        } else if (tid.startsWith(GeneratorService._PREFIX_PARENT_TRADE_ID)) {
            tradeVOList.addAll(tradeQueryProvider.getListByParentId(TradeListByParentIdRequest.builder().parentTid(tid)
                    .build()).getContext().getTradeVOList());
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (tradeVOList.size() > 0
                && (tradeVOList.get(0).getTradeState().getPayState() == PayState.PAID
                || tradeVOList.get(0).getTradeState().getPayState() == PayState.PAID_EARNEST)) {
            flag = "1";
        }
        return BaseResponse.success(flag);
    }

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
    public void aliPay(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PayMobileRequest payMobileRequest = JSON.parseObject(decrypted, PayMobileRequest.class);
        log.info("====================支付宝支付表单================payMobileRequest :{}", payMobileRequest);

        payMobileRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
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
     * @Description: app支付表单
     * @Param:
     * @Author: Bob->
     * @Date: 2019-02-01 11:12
     */
    @ApiOperation(value = "APP支付宝支付", notes = "APP支付宝签名后的参数,返回的数据直接调用appSDK")
    @RequestMapping(value = "app/aliPay/", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse<String> appAliPay(@RequestBody @Valid PayMobileRequest payMobileRequest) {
        log.info("====================支付宝支付表单=APP支付宝支付================payMobileRequest :{}", payMobileRequest);
        payMobileRequest.setStoreId(Constant.BOSS_DEFAULT_STORE_ID);

        return BaseResponse.success(this.alipayUtil(payMobileRequest));
    }

    /*
     * @Description: 支付之前的公共判断条件及数据组装
     * @Param:  payMobileRequest
     * @Author: Bob
     * @Date: 2019-02-22 11:27
     */
    private String alipayUtil(PayMobileRequest payMobileRequest) {
        String id = payServiceHelper.getPayBusinessId(payMobileRequest.getTid(), payMobileRequest.getParentTid());
        List<TradeVO> trades = payServiceHelper.checkTrades(id);


        BigDecimal totalPrice = payServiceHelper.calcTotalPriceByYuan(trades);
        PayExtraRequest payExtraRequest = new PayExtraRequest();
        payExtraRequest.setStoreId(payMobileRequest.getStoreId());
        payExtraRequest.setBusinessId(id);
        payExtraRequest.setChannelItemId(payMobileRequest.getChannelItemId());
        payExtraRequest.setTerminal(payMobileRequest.getTerminal());
        if (TerminalType.H5.equals(payMobileRequest.getTerminal())) {
            payExtraRequest.setSuccessUrl(payMobileRequest.getSuccessUrl());
        }
        payExtraRequest.setAmount(totalPrice);
        payExtraRequest.setOpenId(payMobileRequest.getOpenId());
        TradeVO trade = trades.get(0);
        String title = trade.getTradeItems().get(0).getSkuName();
        String body = trade.getTradeItems().get(0).getSkuName() + " " + (trade.getTradeItems().get(0).getSpecDetails
                () == null ? "" : trade.getTradeItems().get(0).getSpecDetails());
        if (trades.size() > 1 || trade.getTradeItems().size() > 1) {
            if (title.length() > 23) {
                title = title.substring(0, 22) + "...  等多件商品";
            } else {
                title = title + " 等多件商品";
            }
            body = body + " 等多件商品";
        } else {
            if (title.length() > 29) {
                title = title.substring(0, 28) + "...";
            }
        }
        title = title.replaceAll("([&,'])", "");
        body = body.replaceAll("([&,'])", "");
        log.info("=============body", body);
        log.info("=============title", title);
        payExtraRequest.setSubject(title);
        payExtraRequest.setBody(body);
        payExtraRequest.setClientIp(HttpUtil.getIpAddr());

        String form = "";
        try {
            form = aliPayProvider.getPayForm(payExtraRequest).getContext().getForm();
        } catch (SbcRuntimeException e) {
            if (e.getErrorCode() != null && e.getErrorCode().equals("K-100203")) {
                //已支付，手动回调
                Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("1").name("SYSTEM").platform
                        (Platform.BOSS).build();

                List<TradePayCallBackOnlineDTO> list = new ArrayList<>();
                trades.forEach(i -> {
                    //获取订单信息
                    PayOrderVO payOrder = tradeQueryProvider.getPayOrderById(TradeGetPayOrderByIdRequest.builder()
                            .payOrderId(i.getPayOrderId()).build()).getContext().getPayOrder();
                    TradePayCallBackOnlineDTO dto = TradePayCallBackOnlineDTO.builder()
                            .payOrderOld(KsBeanUtil.convert(payOrder, PayOrderDTO.class))
                            .trade(KsBeanUtil.convert(i, TradeDTO.class))
                            .build();
                    list.add(dto);

                });
                tradeProvider.payCallBackOnlineBatch(new TradePayCallBackOnlineBatchRequest(list, operator));
            }
            throw new SbcRuntimeException(e.getErrorCode(), e.getParams());
        }

        return form;
    }

    /**
     * 根据不同的支付方式获取微信支付对应的appId
     *
     * @return
     */
    @ApiOperation(value = "根据不同的支付方式获取微信支付对应的appId")
    @RequestMapping(value = "/getAppId/{payGateway}", method = RequestMethod.GET)
    public BaseResponse<Map<String, Object>> getAppId(@PathVariable PayGatewayEnum payGateway) {
        GatewayConfigByGatewayRequest request = new GatewayConfigByGatewayRequest();
        request.setGatewayEnum(payGateway);
        request.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        BaseResponse<PayGatewayConfigResponse> baseResponse = payQueryProvider.getGatewayConfigByGateway(request);
        Map<String, Object> appIdMap = new HashMap<>();
        if (baseResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
            appIdMap.put("appId", baseResponse.getContext().getAppId());
        }
        return BaseResponse.success(appIdMap);
    }

    /**
     * 获取不同渠道对应的openid
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "获取微信支付对应的openId")
    @RequestMapping(value = "/getOpenId/{payGateway}/{code}", method = RequestMethod.GET)
    public BaseResponse<String> getOpenIdByChannel(@PathVariable PayGatewayEnum payGateway, @PathVariable String code) {
        log.info("============code==========>:{}",code);
        String appId = "";
        String secret = "";
        GatewayConfigByGatewayRequest request = new GatewayConfigByGatewayRequest();
        request.setGatewayEnum(payGateway);
        request.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        BaseResponse<PayGatewayConfigResponse> baseResponse = payQueryProvider.getGatewayConfigByGateway(request);
        if (baseResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
            appId = baseResponse.getContext().getAppId();
            secret = baseResponse.getContext().getSecret();
        }
        GetAccessTokeResponse getAccessTokeResponse = wechatApi.getWeChatAccessToken(appId, secret, code);
        log.info("============accessTokeResponse==========>:{}",getAccessTokeResponse);
        return BaseResponse.success(getAccessTokeResponse.getOpenid());
    }

    /**
     * 小程序通过code获取openId
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "获取小程序微信支付对应的openId")
    @RequestMapping(value = "/getOpenId/littleProgram/{code}", method = RequestMethod.GET)
    public BaseResponse<String> getLittleProgramOpenId(@PathVariable String code) {
        String appId;
        String secret;

        BaseResponse<MiniProgramSetGetResponse> baseResponse = wechatAuthProvider.getMiniProgramSet();
        if (baseResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
            JSONObject json = JSON.parseObject(baseResponse.getContext().getContext());
            appId = json.getString("appId");
            secret = json.getString("appSecret");
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }

        LittleProgramAuthResponse getAccessTokeResponse = wechatApi.getLittleProgramAccessToken(appId, secret, code);
        return BaseResponse.success(getAccessTokeResponse.getOpenid());
    }

    @ApiOperation(value = "余额支付")
    @RequestMapping(value = "/balancePay", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse balancePay(@RequestBody @Valid PayMobileRequest payMobileRequest) {
        RLock rLock = redissonClient.getFairLock(commonUtil.getOperatorId());
        rLock.lock();
        try {
            PayGatewayConfigResponse gatewayConfigResponse =
                    payQueryProvider.getGatewayConfigByGatewayId(GatewayConfigByGatewayIdRequest.builder()
                            .gatewayId((long) 5)
                            // boss端才有余额支付
                            .storeId(Constants.BOSS_DEFAULT_STORE_ID)
                            .build()).getContext();
            if (gatewayConfigResponse.getPayGateway().getIsOpen().equals(IsOpen.NO)) {
                throw new SbcRuntimeException("K-050407");
            }
            String tradeId = payServiceHelper.getPayBusinessId(payMobileRequest.getTid(), payMobileRequest.getParentTid());
            List<TradeVO> tradeVOS = payServiceHelper.checkTrades(tradeId);
            BigDecimal totalPrice = payServiceHelper.calcTotalPriceByYuan(tradeVOS);

            //校验密码是否可用
            CustomerGetByIdResponse customerGetByIdResponse = customerQueryProvider.getCustomerById(new
                    CustomerGetByIdRequest(commonUtil.getCustomer().getCustomerId())).getContext();
            if (StringUtils.isBlank(customerGetByIdResponse.getCustomerPayPassword())) {
                throw new SbcRuntimeException(CustomerErrorCode.NO_CUSTOMER_PAY_PASSWORD);
            }
            if (customerGetByIdResponse.getPayErrorTime() != null && customerGetByIdResponse.getPayErrorTime() == 3) {
                Duration duration = Duration.between(customerGetByIdResponse.getPayLockTime(), LocalDateTime.now());
                if (duration.toMinutes() < 30) {
                    //支付密码输错三次，并且锁定时间还未超过30分钟，返回账户冻结错误信息
                    throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_PAY_LOCK_TIME_ERROR, new Object[]{30 - duration.toMinutes()});
                }
            }

            //校验密码是否正确
            CustomerCheckPayPasswordRequest customerCheckPayPasswordRequest = new CustomerCheckPayPasswordRequest();
            customerCheckPayPasswordRequest.setPayPassword(payMobileRequest.getPayPassword());
            customerCheckPayPasswordRequest.setCustomerId(commonUtil.getCustomer().getCustomerId());
            customerSiteProvider.checkCustomerPayPwd(customerCheckPayPasswordRequest);

            // 处理用户余额, 校验余额是否可用
            CustomerFundsByCustomerIdResponse fundsByCustomerIdResponse =
                    customerFundsQueryProvider.getByCustomerId(new CustomerFundsByCustomerIdRequest(
                            commonUtil.getOperatorId())).getContext();
            if (fundsByCustomerIdResponse.getWithdrawAmount().compareTo(totalPrice) < 0) {
                throw new SbcRuntimeException("K-050408");
            }
            CustomerFundsModifyRequest fundsModifyRequest = new CustomerFundsModifyRequest();
            fundsModifyRequest.setCustomerFundsId(fundsByCustomerIdResponse.getCustomerFundsId());
            fundsModifyRequest.setExpenseAmount(totalPrice);
            customerFundsProvider.balancePay(fundsModifyRequest);

            //同步会员资金到es
            esCustomerFundsProvider.updateCustomerFunds(EsCustomerFundsModifyRequest.builder()
                    .customerFundsId(fundsByCustomerIdResponse.getCustomerFundsId())
                    .accountBalance(fundsByCustomerIdResponse.getAccountBalance().subtract(totalPrice))
                    .withdrawAmount(fundsByCustomerIdResponse.getWithdrawAmount().subtract(totalPrice)).build());

            BigDecimal tradeTotalPrice = BigDecimal.ZERO;
            List<CustomerFundsDetailAddRequest> customerFundsDetailAddRequestList = new ArrayList<>();
            for (TradeVO tradeVO : tradeVOS) {
                //定金预售取定金/尾款，其他取订单总金额
                BigDecimal  price = Objects.nonNull(tradeVO.getIsBookingSaleGoods()) && tradeVO.getBookingType() == BookingType.EARNEST_MONEY ? totalPrice : tradeVO.getTradePrice().getTotalPrice();
                tradeTotalPrice = tradeTotalPrice.add(price);
                CustomerFundsDetailAddRequest customerFundsDetailAddRequest =
                        new CustomerFundsDetailAddRequest();
                customerFundsDetailAddRequest.setCustomerId(fundsByCustomerIdResponse.getCustomerId());
                customerFundsDetailAddRequest.setBusinessId(tradeId.startsWith(GeneratorService._PREFIX_TRADE_TAIL_ID) ? tradeId : tradeVO.getId());
                customerFundsDetailAddRequest.setFundsType(FundsType.BALANCE_PAY);
                customerFundsDetailAddRequest.setReceiptPaymentAmount(price);
                customerFundsDetailAddRequest.setFundsStatus(FundsStatus.YES);
                customerFundsDetailAddRequest.setAccountBalance(fundsByCustomerIdResponse.getAccountBalance().subtract(tradeTotalPrice));
                customerFundsDetailAddRequest.setSubType(FundsSubType.BALANCE_PAY);
                customerFundsDetailAddRequest.setCreateTime(LocalDateTime.now());
                customerFundsDetailAddRequestList.add(customerFundsDetailAddRequest);
            }

            customerFundsDetailProvider.batchAdd(customerFundsDetailAddRequestList);
            // 新增交易记录
            List<PayTradeRecordRequest> payTradeRecordRequests = tradeVOS.stream()
                    .map(tradeVO -> {
                        PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
                        payTradeRecordRequest.setBusinessId(tradeId.startsWith(GeneratorService._PREFIX_TRADE_TAIL_ID) ? tradeId : tradeVO.getId());
                        payTradeRecordRequest.setApplyPrice(payServiceHelper.calcTotalPriceByYuan(Collections.singletonList(tradeVO)));
                        payTradeRecordRequest.setPracticalPrice(payTradeRecordRequest.getApplyPrice());
                        payTradeRecordRequest.setResult_code("SUCCESS");
                        payTradeRecordRequest.setChannelItemId(payMobileRequest.getChannelItemId());
                        return payTradeRecordRequest;
                    }).collect(Collectors.toList());
            payProvider.batchSavePayTradeRecord(payTradeRecordRequests);
            // 支付成功，处理订单
            List<TradePayCallBackOnlineDTO> tradePayCallBackOnlineDTOS = tradeVOS.stream()
                    .map(tradeVO -> {
                        TradePayCallBackOnlineDTO tradePayCallBackOnlineDTO = new TradePayCallBackOnlineDTO();
                        tradePayCallBackOnlineDTO.setTrade(KsBeanUtil.convert(tradeVO, TradeDTO.class));
                        PayOrderVO payOrder = tradeQueryProvider.getPayOrderById(TradeGetPayOrderByIdRequest.builder()
                                .payOrderId(tradeId.startsWith(GeneratorService._PREFIX_TRADE_TAIL_ID) ? tradeVO.getTailPayOrderId() : tradeVO.getPayOrderId()).build()).getContext().getPayOrder();
                        tradePayCallBackOnlineDTO.setPayOrderOld(KsBeanUtil.convert(payOrder, PayOrderDTO.class));
                        return tradePayCallBackOnlineDTO;
                    }).collect(Collectors.toList());
            Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name("UNIONB2B")
                    .platform(Platform.THIRD).build();
            tradeProvider.payCallBackOnlineBatch(TradePayCallBackOnlineBatchRequest.builder()
                    .requestList(tradePayCallBackOnlineDTOS)
                    .operator(operator)
                    .build());

            // 余额支付同步供应商订单状态
            //this.providerTradePayCallBack(tradeVOS);
        } catch (Exception e) {
            throw e;
        } finally {
            rLock.unlock();
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 余额支付同步供应商订单状态
     * @param trades
     */
    private void providerTradePayCallBack(List<TradeVO> trades) {
        if (CollectionUtils.isNotEmpty(trades)) {
            trades.forEach(parentTradeVO -> {
                String parentId = parentTradeVO.getId();
                BaseResponse<TradeListByParentIdResponse> supplierListByParentId =
                        providerTradeQueryProvider.getProviderListByParentId(TradeListByParentIdRequest.builder().parentTid(parentId).build());
                if (Objects.nonNull(supplierListByParentId.getContext()) && CollectionUtils.isNotEmpty(supplierListByParentId.getContext().getTradeVOList())) {
                    supplierListByParentId.getContext().getTradeVOList().forEach(childTradeVO->{
                        childTradeVO.getTradeState().setPayState(PayState.PAID);
                        TradeUpdateRequest tradeUpdateRequest = new TradeUpdateRequest(KsBeanUtil.convert(childTradeVO, TradeUpdateDTO.class));
                        providerTradeProvider.providerUpdate(tradeUpdateRequest);
                    });
                }
            });
        }
    }
}
