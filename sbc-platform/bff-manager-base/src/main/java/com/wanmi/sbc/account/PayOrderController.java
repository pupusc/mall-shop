package com.wanmi.sbc.account;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderProvider;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.*;
import com.wanmi.sbc.order.api.request.trade.TradeAddReceivableRequest;
import com.wanmi.sbc.order.api.request.trade.TradeConfirmPayOrderRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.payorder.*;
import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
import com.wanmi.sbc.order.bean.dto.ReceivableAddDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.PayOrderResponseVO;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.jsonwebtoken.Claims;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Objects.isNull;

/**
 * 支付单
 * Created by zhangjin on 2017/4/20.
 */
@Api(tags = "PayOrderController", description = "支付单")
@RestController
@RequestMapping("/account")
public class PayOrderController {

    @Autowired
    private PayOrderProvider payOrderProvider;

    @Autowired
    private PayOrderQueryProvider payOrderQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 导出文件名后的时间后缀
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * 查询订单收款
     *
     * @param payOrderRequest payOrderRequest
     * @return status
     */
    @ApiOperation(value = "查询订单收款")
    @RequestMapping(value = "/payOrders", method = RequestMethod.POST)
    public ResponseEntity<FindPayOrdersResponse> findPayOrder(@RequestBody FindPayOrdersRequest payOrderRequest) {
        if(Objects.nonNull(payOrderRequest.getOrderNo())){
            TradeVO tradeVO = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(payOrderRequest.getOrderNo()).build()).getContext().getTradeVO();
            if (Objects.nonNull(tradeVO.getIsBookingSaleGoods()) && tradeVO.getIsBookingSaleGoods() && tradeVO.getBookingType() == BookingType.EARNEST_MONEY
                    && StringUtils.isNotEmpty(tradeVO.getTailOrderNo())) {
                payOrderRequest.setOrderNo(null);
                List<String> orderNoList = new ArrayList<>();
                orderNoList.add(tradeVO.getId());
                orderNoList.add(tradeVO.getTailOrderNo());
                payOrderRequest.setOrderNoList(orderNoList);
            }
        }
        return ResponseEntity.ok(payOrderQueryProvider.findPayOrders(payOrderRequest).getContext());
    }

    /**
     * 查询订单收款（商家）
     *
     * @param payOrderRequest payOrderRequest
     * @return status
     */
    @ApiOperation(value = "查询订单收款（商家）")
    @RequestMapping(value = "/store/payOrders", method = RequestMethod.POST)
    public ResponseEntity<FindPayOrdersResponse> findStorePayOrder(@RequestBody FindPayOrdersRequest payOrderRequest) {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        payOrderRequest.setCompanyInfoId(isNull(companyInfoId) ? "0" : companyInfoId.toString());
        FindPayOrdersResponse response = payOrderQueryProvider.findPayOrders(payOrderRequest).getContext();
        return ResponseEntity.ok(response);
    }

    /**
     * 根据订单编号查询收款单
     *
     * @param orderNo orderNo 订单编号
     * @return ResponseEntity<PayOrderResponse>
     */
    @ApiOperation(value = "根据订单编号查询收款单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "orderNo", value = "订单编号", required = true)
    @RequestMapping(value = "/payOrder/{orderNo}", method = RequestMethod.GET)
    public ResponseEntity<FindPayOrderResponse> findPayOrderByOrderNo(@PathVariable("orderNo") String orderNo) {
        BaseResponse<FindPayOrderResponse> response =
                payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(orderNo).build());
        return ResponseEntity.ok(response.getContext());
    }



    /**
     * 确认订单收款
     *
     * @param payOrderOperateRequest payOrderIds
     * @return status
     */
    @ApiOperation(value = "确认订单收款")
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> confirm(@RequestBody TradeConfirmPayOrderRequest payOrderOperateRequest,
                                                HttpServletRequest request) {
        Operator operator = buildEmployee(request);
        payOrderOperateRequest.setOperator(operator);

        tradeProvider.confirmPayOrder(payOrderOperateRequest);

        // 操作日志记录
        List<String> payOrderIds = payOrderOperateRequest.getPayOrderIds();
        if (CollectionUtils.size(payOrderIds) == 1) {
            Optional<PayOrderVO> payOrderOptional = this.getPayOrderCode(payOrderIds.get(0));
            payOrderOptional.ifPresent(payOrder -> operateLogMQUtil.convertAndSend("财务", "确认收款",
                    "确认收款：订单编号" + payOrder.getOrderCode()));
        } else {
            operateLogMQUtil.convertAndSend("财务", "批量确认", "批量确认");
        }

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

//    /**
//     * 作废订单收款
//     *
//     * @param payOrderOperateRequest payOrderIds
//     * @return status
//     */
//    @ApiOperation(value = "作废订单收款")
//    @RequestMapping(value = "/payOrder/destory", method = RequestMethod.PUT)
//    @Transactional
//    public ResponseEntity<BaseResponse> destory(@RequestBody FindPayOrderByPayOrderIdsRequest payOrderOperateRequest,
//                                                HttpServletRequest request) {
//        Operator operator = this.buildEmployee(request);
//
//        //判断订单是否计入了账期，如果计入了账期，不允许作废
//        List<PayOrderVO> payOrderList =
//                payOrderQueryProvider.findPayOrderByPayOrderIds(payOrderOperateRequest).getContext().getOrders();
//        if (payOrderList != null && !payOrderList.isEmpty()) {
//            payOrderList.stream().forEach(payOrder -> {
//                Boolean settled =
//                        tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(payOrder.getOrderCode()).build()).getContext().getTradeVO().getHasBeanSettled();
//                if (settled != null && settled) {
//                    throw new SbcRuntimeException("K-050131");
//                }
//            });
//        }
//
//        List<PayOrderDTO> payOrderDTOS = KsBeanUtil.convert(payOrderList, PayOrderDTO.class);
//
//        payOrderProvider.destoryPayOrder(DestoryPayOrderRequest.builder().payOrders(payOrderDTOS).operator(operator).build());
//
//        //操作日志记录
//        if (CollectionUtils.size(payOrderOperateRequest.getPayOrderIds()) == 1) {
//            Optional<PayOrderVO> payOrderOptional =
//                    this.getPayOrderCode(payOrderOperateRequest.getPayOrderIds().get(0));
//            payOrderOptional.ifPresent(payOrder -> operateLogMQUtil.convertAndSend("财务", "作废收款单",
//                    "作废收款单：订单编号" + payOrder.getOrderCode()));
//        } else {
//            operateLogMQUtil.convertAndSend("财务", "批量作废", "批量作废");
//        }
//
//        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
//    }

    /**
     * 作废订单收款
     *
     * @param payOrderId payOrderIds
     * @return status
     */
    @ApiOperation(value = "作废订单收款")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "payOrderId", value = "支付单ID",
            required = true)
    @RequestMapping(value = "/payOrder/destory/{payOrderId}", method = RequestMethod.PUT)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> destoryByPayOrderId(@PathVariable("payOrderId") String payOrderId,
                                                            HttpServletRequest request) {
        Operator operator = this.buildEmployee(request);

        BaseResponse<FindPayOrderByPayOrderIdsResponse> responseBaseResponse =
                payOrderQueryProvider.findPayOrderByPayOrderIds(FindPayOrderByPayOrderIdsRequest.builder().payOrderIds(Lists.newArrayList(payOrderId)).build());

        //判断订单是否计入了账期，如果计入了账期，不允许作废
        List<PayOrderVO> payOrderList = responseBaseResponse.getContext().getOrders();
        if (payOrderList != null && !payOrderList.isEmpty()) {
            payOrderList.stream().forEach(payOrder -> {
                Boolean settled =
                        tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(payOrder.getOrderCode()).build()).getContext().getTradeVO().getHasBeanSettled();
                if (settled != null && settled) {
                    throw new SbcRuntimeException("K-050131");
                }
            });
        }

        List<PayOrderDTO> payOrderDtos = KsBeanUtil.convert(payOrderList, PayOrderDTO.class);

        payOrderProvider.destoryPayOrder(DestoryPayOrderRequest.builder().payOrders(payOrderDtos).operator(operator).build());

        //操作日志记录
        Optional<PayOrderVO> payOrderOptional = this.getPayOrderCode(payOrderId);
        payOrderOptional.ifPresent(payOrder -> operateLogMQUtil.convertAndSend("财务", "作废收款单",
                "作废收款单：订单编号" + payOrder.getOrderCode()));

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 新增收款单
     *
     * @param receivableAddRequest receivableAddRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "新增收款单")
    @RequestMapping(value = "/receivable", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> addReceivable(@RequestBody @Valid ReceivableAddDTO receivableAddRequest,
                                                      HttpServletRequest request) {
        Operator operator = buildEmployee(request);

        TradeAddReceivableRequest tradeAddReceivableRequest = TradeAddReceivableRequest.builder()
                .receivableAddDTO(receivableAddRequest)
                .platform(operator.getPlatform())
                .operator(commonUtil.getOperator())
                .build();

        tradeProvider.addReceivable(tradeAddReceivableRequest);

        //操作日志记录
        if (Objects.nonNull(receivableAddRequest.getPayOrderId())) {
            Optional<PayOrderVO> payOrderOptional = this.getPayOrderCode(receivableAddRequest.getPayOrderId());
            payOrderOptional.ifPresent(payOrder -> operateLogMQUtil.convertAndSend("财务", "确认收款",
                    "确认收款：订单编号" + payOrder.getOrderCode()));
        }
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }


    /**
     * 查询所有收款单价格
     *
     * @return 收款单价格
     */
    @ApiOperation(value = "查询所有收款单价格")
    @RequestMapping(value = "/sumPayOrderPrice", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse<BigDecimal>> sumPayOrderPrice(@RequestBody SumPayOrderPriceRequest payOrderRequest) {

        BaseResponse<SumPayOrderPriceResponse> responseBaseResponse =
                payOrderQueryProvider.sumPayOrderPrice(payOrderRequest);

        return ResponseEntity.ok(BaseResponse.success(responseBaseResponse.getContext().getValue()));
    }

    /**
     * 导出退单
     *
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void exportByParams(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));

        FindPayOrdersRequest payOrderRequest = JSON.parseObject(decrypted, FindPayOrdersRequest.class);

        Operator operator = commonUtil.getOperator();
        logger.debug("/return/export/params, employeeId=" + operator.getUserId());

        FindPayOrdersResponse payOrderPageResponse;
        if (CollectionUtils.isEmpty(payOrderRequest.getPayOrderIds())) {
            payOrderRequest.setPageSize(1000);

            payOrderPageResponse = payOrderQueryProvider.findPayOrders(payOrderRequest).getContext();

        } else {

            FindPayOrdersWithNoPageRequest request = KsBeanUtil.convert(payOrderRequest,
                    FindPayOrdersWithNoPageRequest.class);

            FindPayOrdersWithNoPageResponse findPayOrdersWithNoPageResponse =
                    payOrderQueryProvider.findPayOrdersWithNoPage(request).getContext();

            payOrderPageResponse = KsBeanUtil.convert(findPayOrdersWithNoPageResponse, FindPayOrdersResponse.class);

        }

        String headerKey = "Content-Disposition";
        LocalDateTime dateTime = LocalDateTime.now();
        String fileName = String.format("批量订单收款_%s.xls", dateTime.format(formatter));
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("/return/export/params, fileName={}, error={}", fileName, e);
        }
        String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
        response.setHeader(headerKey, headerValue);
        try {
            export(payOrderPageResponse.getPayOrderResponses(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new SbcRuntimeException(e);
        }
    }


    public void export(List<PayOrderResponseVO> payOrderResponses, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        excelHelper.addSheet(
                "订单收款",
                new Column[]{
                        new Column("收款流水号", new SpelColumnRender<PayOrderResponseVO>("receivableNo")),
                        new Column("收款时间", new SpelColumnRender<PayOrderResponseVO>("receiveTime")),
                        new Column("订单号", new SpelColumnRender<PayOrderResponseVO>("orderCode")),
                        new Column("客户名称", new SpelColumnRender<PayOrderResponseVO>("customerName")),
                        new Column("应收金额", (cell, object) -> {
                            PayOrderResponseVO payOrderResponse = (PayOrderResponseVO) object;
                            cell.setCellValue("￥" + payOrderResponse.getPayOrderPrice().toString());
                        }),
                        new Column("支付方式", (cell, object) -> {
                            PayOrderResponseVO payOrderResponse = (PayOrderResponseVO) object;
                            if (PayType.OFFLINE.equals(payOrderResponse.getPayType())) {
                                cell.setCellValue("线下支付");
                            } else {
                                cell.setCellValue("在线支付");
                            }
                        }),
                        new Column("收款账户", new SpelColumnRender<PayOrderResponseVO>("receivableAccount")),
                        new Column("付款状态", (cell, object) -> {
                            PayOrderResponseVO payOrderResponse = (PayOrderResponseVO) object;
                            if (PayOrderStatus.NOTPAY.equals(payOrderResponse.getPayOrderStatus())) {
                                cell.setCellValue("未付款");
                            } else if (PayOrderStatus.PAYED.equals(payOrderResponse.getPayOrderStatus())) {
                                cell.setCellValue("已付款");
                            } else {
                                cell.setCellValue("待确认");
                            }
                        }),
                        new Column("备注", new SpelColumnRender<PayOrderResponseVO>("comment")),
                },
                payOrderResponses
        );
        excelHelper.write(outputStream);
    }


    private Operator buildEmployee(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String employeeId = claims.get("employeeId").toString();
        String employeeName = claims.get("EmployeeName").toString();
        String ip = claims.get("ip").toString();
        return Operator.builder()
                .ip(ip)
                .platform(Platform.forValue(ObjectUtils.toString(claims.get("platform"))))
                .userId(employeeId).name(employeeName)
                .account(employeeName)
                .build();
    }

    /**
     * 根据payOrderId获取PayOrder对象
     *
     * @param payOrderId
     * @return
     */
    public Optional<PayOrderVO> getPayOrderCode(String payOrderId) {
        FindPayOrderByPayOrderIdsResponse response =
                payOrderQueryProvider.findPayOrderByPayOrderIds(new FindPayOrderByPayOrderIdsRequest(Lists.newArrayList(payOrderId))).getContext();
        if (Objects.isNull(response)) {
            return Optional.empty();
        }
        return Optional.of(response.getOrders().get(0));
    }
}
