package com.wanmi.sbc.trade;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.setting.api.response.wechat.WechatSetResponse;
import com.wanmi.sbc.third.wechat.WechatSetService;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
import com.wanmi.sbc.order.bean.dto.TradeDTO;
import com.wanmi.sbc.order.bean.dto.TradePayCallBackOnlineDTO;
import com.wanmi.sbc.order.bean.dto.TradeUpdateDTO;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.api.provider.AliPayProvider;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.PayGatewayConfigResponse;
import com.wanmi.sbc.pay.api.response.WxPayForNativeResponse;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import com.wanmi.sbc.pay.bean.enums.WxPayTradeType;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.bean.vo.PayGatewayConfigVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.trade.request.PayMobileRequest;
import com.wanmi.sbc.trade.request.WeiXinPayRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by sunkun on 2017/8/23.
 */
@Api(tags = "PayController", description = "支付Api")
@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WxPayProvider wxPayProvider;

    @Autowired
    private PayServiceHelper payServiceHelper;

    @Autowired
    private AliPayProvider aliPayProvider;

    @Autowired
    private WechatSetService wechatSetService;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 获取可用支付项
     *
     * @return
     */
    @ApiOperation(value = "获取可用支付项")
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public BaseResponse<List<PayChannelItemVO>> items() {
        GatewayOpenedByStoreIdRequest request = new GatewayOpenedByStoreIdRequest();
        request.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        List<PayGatewayConfigVO> payGatewayConfigList = payQueryProvider.listOpenedGatewayConfig(request).getContext()
                .getGatewayConfigVOList();

        DefaultFlag wxOpenFlag = wechatSetService.getStatus(com.wanmi.sbc.common.enums.TerminalType.H5);

        List<PayChannelItemVO> itemList = new ArrayList<>();
        payGatewayConfigList.forEach(config -> {
            List<PayChannelItemVO> payChannelItemList = payQueryProvider.listOpenedChannelItemByGatewayName(new
                    OpenedChannelItemRequest(config.getPayGateway().getName(), TerminalType.PC)).getContext()
                    .getPayChannelItemVOList();
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
     * 创建Charges
     *
     * @param payMobileRequest
     * @return
     */
    @ApiOperation(value = "创建Charges")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @MultiSubmit
    @GlobalTransactional
    public BaseResponse<Object> create(@RequestBody @Valid PayMobileRequest payMobileRequest) {
        List<TradeVO> trades = payServiceHelper.findTrades(payMobileRequest.getTid());
        payServiceHelper.checkPayBefore(trades);
        TradeVO trade = trades.get(0);
        PayOrderVO payOrder = tradeQueryProvider.getPayOrderById(TradeGetPayOrderByIdRequest.builder()
                .payOrderId(trade.getPayOrderId()).build()).getContext().getPayOrder();
        PayExtraRequest payExtraRequest = new PayExtraRequest();
        payExtraRequest.setSuccessUrl(payMobileRequest.getSuccessUrl());
        payExtraRequest.setBusinessId(payMobileRequest.getTid());
        payExtraRequest.setChannelItemId(payMobileRequest.getChannelItemId());
        payExtraRequest.setTerminal(TerminalType.PC);
        payExtraRequest.setAmount(trade.getTradePrice().getTotalPrice());
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
                Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("1")
                        .name("system").account("system").platform
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

    @ApiOperation(value = "支付前校验是否已支付成功")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单号", required = true)
    @RequestMapping(value = "/aliPay/check/{tid}", method = RequestMethod.GET)
    public BaseResponse checkPayState(@PathVariable String tid) {
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                .tid(tid).build()).getContext().getTradeVO();

        String flag = "0";
        if (Objects.nonNull(trade)) {
            if (Objects.nonNull(trade.getTradeState())) {
                if (PayState.PAID.equals(trade.getTradeState().getPayState())) {
                    flag = "1";
                }
            }
        }
        return BaseResponse.success(flag);
    }

    /*
     * @Description: 支付表单
     * @Param:
     * @Author: Bob->
     * @Date: 2019-02-01 11:12
     */
    @ApiOperation(value = "支付宝支付表单", notes = "该请求需新打开一个空白页，返回的支付宝脚本会自动提交重定向到支付宝收银台", httpMethod = "GET")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "base64编码后的支付请求",
            required = true)
    @RequestMapping(value = "/aliPay/{encrypted}", method = RequestMethod.GET)
    @GlobalTransactional
    public void aliPay(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        PayMobileRequest payMobileRequest = JSON.parseObject(decrypted, PayMobileRequest.class);
        log.info("====================支付宝支付表单================payMobileRequest :{}",payMobileRequest);

        payMobileRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        String id = payServiceHelper.getPayBusinessId(payMobileRequest.getTid(), payMobileRequest.getParentTid());
        List<TradeVO> trades = payServiceHelper.checkTrades(id);
        //订单总金额
        BigDecimal totalPrice = payServiceHelper.calcTotalPriceByYuan(trades);

        //组装请求数据
        PayExtraRequest payExtraRequest = new PayExtraRequest();
        payExtraRequest.setStoreId(payMobileRequest.getStoreId());
        payExtraRequest.setSuccessUrl(payMobileRequest.getSuccessUrl());
        payExtraRequest.setBusinessId(id);
        payExtraRequest.setChannelItemId(payMobileRequest.getChannelItemId());
        payExtraRequest.setTerminal(TerminalType.PC);
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
        payExtraRequest.setSubject(title);
        payExtraRequest.setBody(body);
        payExtraRequest.setClientIp(HttpUtil.getIpAddr());
        payExtraRequest.setTerminal(TerminalType.PC);
        try {
            //form是一段js脚本，必须返回到一个没有任何代码的空白页，脚本会自动提交重定向到支付宝的收银台
            String form = aliPayProvider.getPayForm(payExtraRequest).getContext().getForm();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
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
        } catch (IOException e) {
            // TODO: 2019-01-28 gb支付异常未处理
            log.error("execute alipay has IO exception:{} ", e);
        }
    }

    /**
     * 银联企业支付
     *
     * @param tradeId  单笔支付场景为订单编号，多笔合并支付场景为父订单编号
     * @param response 银联交互的方式比较特殊，是返回html给前台，并没有加上重复提交注解 - @MultiSubmit
     *                 因为注解throw返回的是code和message，显示在前台不友好，所以单独判断重复提交，重复则返回提示字符串到前台
     */
    @ApiOperation(value = "银联企业支付")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tradeId", value = "订单编号", required = true)
    @RequestMapping(value = "/unionB2BPay/{tradeId}", method = RequestMethod.GET)
    @GlobalTransactional
    public void unionB2BPay(@PathVariable("tradeId") String tradeId, HttpServletResponse response) {
        log.info("====================银联支付逻辑开始================");
        String unionPayKey = "unionB2BPay".concat(tradeId);
        String html = "";
        try {
            try {
                if (redisService.setNx(unionPayKey)) {
                    redisService.expireBySeconds(unionPayKey, 10L);
                    List<TradeVO> trades = payServiceHelper.checkTrades(tradeId);
                    TradeVO trade = trades.get(0);

                    String title = trade.getTradeItems().get(0).getSkuName();
                    String body =
                            trade.getTradeItems().get(0).getSkuName() + " " + (trade.getTradeItems().get(0).getSpecDetails
                                    () == null ? "" : trade.getTradeItems().get(0).getSpecDetails());
                    if (trades.size() > 1 || trade.getTradeItems().size() > 1) {
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
                    BigDecimal totalPrice = payServiceHelper.calcTotalPriceByYuan(trades);
                    UnionPayRequest unionPayRequest = new UnionPayRequest();
                    unionPayRequest.setAmount(totalPrice);
                    unionPayRequest.setBusinessId(tradeId);

                    PayGatewayConfigResponse payGatewayConfigResponse = payQueryProvider.getGatewayConfigByGateway(new
                            GatewayConfigByGatewayRequest(PayGatewayEnum.UNIONB2B, Constants.BOSS_DEFAULT_STORE_ID)).getContext();
                    unionPayRequest.setApiKey(payGatewayConfigResponse.getApiKey());
                    unionPayRequest.setFrontUrl(payGatewayConfigResponse.getPcBackUrl() + "/pay/pay-success");
                    unionPayRequest.setNotifyUrl(payGatewayConfigResponse.getBossBackUrl() + "/tradeCallback" +
                            "/unionB2BCallBack");
                    unionPayRequest.setTerminal(TerminalType.PC);
                    unionPayRequest.setSubject(title);
                    unionPayRequest.setBody(body);
                    unionPayRequest.setChannelItemId(11L);
                    unionPayRequest.setClientIp(HttpUtil.getIpAddr());
                    html = payProvider.unionB2BPay(unionPayRequest).getContext();

                    trade.getTradeState().setStartPayTime(LocalDateTime.now());
                    tradeProvider.update(TradeUpdateRequest.builder().trade(KsBeanUtil.convert(trade,
                            TradeUpdateDTO.class)).build());
                } else {
                    throw new SbcRuntimeException(CommonErrorCode.REPEAT_REQUEST);
                }
            } catch (SbcRuntimeException e) {
                html = new BaseResponse<>(e.getErrorCode()).getMessage();
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.setContentType("application/json;charset=utf-8");
            } catch (Exception e) {
                log.error("银联支付异常，{}", e);
            } finally {
                if (StringUtils.isBlank(html)) {
                    html = "发生未知错误，请重试";
                }
                response.getWriter().write(html);
            }
        } catch (IOException e) {
            log.error("将生成的html写到浏览器失败", e);
        }
    }

    /**
     * 银联企业支付同步回调
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @ApiOperation(value = "银联企业支付同步回调")
    @RequestMapping("/pay-success")
    public ModelAndView unionPaySuccess(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        log.info("银联企业支付同步回调前台接收报文返回开始");
        String encoding = request.getParameter("encoding");
        log.info("返回报文中encoding=[" + encoding + "]");
        Map<String, String> respParam = getAllRequestParam(request);
        PayGatewayConfigResponse payGatewayConfigResponse = payQueryProvider.getGatewayConfigByGateway(new
                GatewayConfigByGatewayRequest(PayGatewayEnum.UNIONB2B,Constants.BOSS_DEFAULT_STORE_ID)).getContext();
        if (null != respParam && !respParam.isEmpty() && "00".equals(respParam.get("respCode"))) {

            if (!payProvider.unionCheckSign(respParam).getContext()) {

                log.info("验证签名结果[失败].");

            } else {
                log.info("验证签名结果[成功].");
//                    UnionPayRequest unionPayRequest = new UnionPayRequest();
//                    unionPayRequest.setApiKey(request.getParameter("merId"));
//                    unionPayRequest.setBusinessId(request.getParameter("orderId"));
//                    unionPayRequest.setTxnTime(request.getParameter("txnTime"));
//                    Map<String,String> resultMap = payQueryProvider.getUnionPayResult(unionPayRequest).getContext();
//                    if("00".equals(resultMap.get("respCode")) && "00".equals(resultMap.get("origRespCode"))){
//                        payProvider.unionCallBack(respParam);
//                        Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name("UNIONB2B")
//                                .account("UNIONB2B").platform
//                                (Platform.THIRD)
//                                .build();
//                        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(respParam.get
//                        ("orderId"))
//                                .build()).getContext().getTradeVO();
//                        PayOrderVO payOrder = tradeQueryProvider.getPayOrderById(TradeGetPayOrderByIdRequest
//                                .builder().payOrderId(trade.getPayOrderId()).build()).getContext().getPayOrder();
                //修改订单状态
//                        tradeProvider.payCallBackOnline(TradePayCallBackOnlineRequest.builder()
//                                .trade(KsBeanUtil.convert(trade, TradeDTO.class))
//                                .payOrderOld(KsBeanUtil.convert(payOrder, PayOrderDTO.class))
//                                .operator(operator)
//                                .build());
//                    }

            }

        }
        log.info("银联企业支付同步回调结束");
        return new ModelAndView(new RedirectView(payGatewayConfigResponse.getPcWebUrl() + "/pay-success/" + respParam.get
                ("orderId")));
    }

    /**
     * 微信扫码支付统一下单接口获取二维码接口
     *
     * @param weiXinPayRequest
     * @return
     */
    @ApiOperation(value = "微信扫码支付统一下单接口获取二维码接口", notes = "返回pc端页面生成二维码所需参数")
    @RequestMapping(value = "/unifiedorderForNative", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<WxPayForNativeResponse> unifiedorderForNative(@RequestBody @Valid WeiXinPayRequest weiXinPayRequest) {
        //PC沿用H5公众号验证支付开关
        WechatSetResponse setResponse = wechatSetService.get(com.wanmi.sbc.common.enums.TerminalType.H5);
        if (DefaultFlag.NO.equals(setResponse.getMobileServerStatus())) {
            throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
        }
        WxPayForNativeRequest nativeRequest = new WxPayForNativeRequest();
        String id = payServiceHelper.getPayBusinessId(weiXinPayRequest.getTid(), weiXinPayRequest.getParentTid());
        List<TradeVO> trades = payServiceHelper.checkTrades(id);
        //订单金额
        String totalPrice = payServiceHelper.calcTotalPriceByPenny(trades).toString();
        nativeRequest.setOut_trade_no(id);
        nativeRequest.setTotal_fee(totalPrice);
        nativeRequest.setSpbill_create_ip(HttpUtil.getIpAddr());
        String body = payServiceHelper.buildBody(trades);
        TradeVO trade = trades.get(0);
        String productId = trade.getTradeItems().get(0).getSkuId();
        if (trades.size() > 1 || trade.getTradeItems().size() > 1) {
            body = body + " 等多件商品";
        }
        nativeRequest.setBody(body + "订单");
        nativeRequest.setProduct_id(productId);
        nativeRequest.setTrade_type(WxPayTradeType.NATIVE.toString());
        nativeRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        nativeRequest.setAppid(setResponse.getPcAppId());
        return wxPayProvider.wxPayForNative(nativeRequest);
    }

    /**
     * 获取请求参数中所有的信息
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }
        return res;
    }

}