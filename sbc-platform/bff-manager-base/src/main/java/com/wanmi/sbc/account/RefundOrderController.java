package com.wanmi.sbc.account;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.order.api.provider.refund.RefundBillProvider;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderProvider;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.request.refund.*;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderRejectRefundRequest;
import com.wanmi.sbc.order.api.response.refund.RefundOrderListReponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderPageResponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderWithoutPageResponse;
import com.wanmi.sbc.order.bean.vo.RefundOrderResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.jsonwebtoken.Claims;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * 退款单
 * Created by zhangjin on 2017/4/21.
 */
@Api(tags = "RefundOrderController", description = "退款单")
@RestController
@RequestMapping("/account")
public class RefundOrderController {

    @Autowired
    private RefundOrderQueryProvider refundOrderQueryProvider;

    @Autowired
    private RefundOrderProvider refundOrderProvider;

    @Autowired
    private RefundBillProvider refundBillProvider;

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    /**
     * 导出文件名后的时间后缀
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * 查询订单退款
     *
     * @param refundOrderRequest refundOrderRequest
     * @return status
     */
    @ApiOperation(value = "查询订单退款")
    @RequestMapping(value = "/refundOrders", method = RequestMethod.POST)
    public BaseResponse<RefundOrderPageResponse> findPayOrder(@RequestBody RefundOrderPageRequest refundOrderRequest) {
        refundOrderRequest.setSupplierId(commonUtil.getCompanyInfoId());
        return refundOrderQueryProvider.page(refundOrderRequest);
    }


//    /**
//     * 销毁
//     *
//     * @param refundId refundId
//     * @return ResponseEntity<BaseResponse>
//     */
//    @ApiOperation(value = "销毁")
//    @ApiImplicitParam(paramType = "path", dataType = "String", name = "refundId", value = "退款Id", required = true)
//    @RequestMapping(value = "/refundOrders/destory/{refundId}", method = RequestMethod.GET)
//    public ResponseEntity<BaseResponse> destory(@PathVariable("refundId") String refundId, HttpServletRequest request) {
//        RefundOrderByIdResponse response =
//                refundOrderQueryProvider.getById(new RefundOrderByIdRequest(refundId)).getContext();
//
//        refundOrderProvider.destory(new RefundOrderDestoryRequest(refundId, buildOperator(request)));
//        //操作日志记录
//        operateLogMQUtil.convertAndSend("财务", "作废退款单",
//                "作废退款单：退单编号" + (Objects.nonNull(response) ? response.getReturnOrderCode() : ""));
//        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
//    }


    /**
     * 拒绝退款
     *
     * @param refuseReasonRequest refuseReasonRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "拒绝退款")
    @RequestMapping(value = "/refundOrders/refuse", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> refuse(@RequestBody RefuseReasonRequest refuseReasonRequest) {
        returnOrderProvider.rejectRefundAndRefuse(ReturnOrderRejectRefundRequest.builder()
                .rid(refuseReasonRequest.getRefundId()).reason(refuseReasonRequest.getRefuseReason())
                .operator(commonUtil.getOperator()).build());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 新增退单流水
     *
     * @param refundBillRequest refundBillRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "新增退单流水")
    @RequestMapping(value = "/refundBill", method = RequestMethod.POST)
    @Transactional
    @GlobalTransactional
    public ResponseEntity<BaseResponse> addRefundBill(@RequestBody RefundBillAddRequest refundBillRequest) {
        Operator operator = commonUtil.getOperator();
        refundBillRequest.setOperator(operator);
        refundBillProvider.add(refundBillRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 根据退单号查询
     *
     * @return BaseResponse<RefundBill>
     */
    @ApiOperation(value = "根据退单号查询")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "returnOrderNo", value = "退单号", required = true)
    @RequestMapping(value = "/refundOrders/{returnOrderNo}")
    public BaseResponse<RefundOrderListReponse> queryRefundByReturnOrderNo(@PathVariable("returnOrderNo") String returnOrderNo) {
        return refundOrderQueryProvider.getRefundOrderRespByReturnOrderCode(new RefundOrderResponseByReturnOrderCodeRequest(returnOrderNo));
    }

    /**
     * 求和退款金额
     *
     * @return
     */
    @ApiOperation(value = "求和退款金额")
    @RequestMapping(value = "/sumReturnPrice", method = RequestMethod.POST)
    public BaseResponse<BigDecimal> sumReturnPrice(@RequestBody RefundOrderRequest refundOrderRequest) {
        return BaseResponse.success(refundOrderQueryProvider.getSumReturnPrice(refundOrderRequest).getContext().getResult());
    }


    /**
     * 导出退单
     *
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @RequestMapping(value = "/export/refund/{encrypted}", method = RequestMethod.GET)
    public void exportByParams(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        RefundOrderRequest refundOrderRequest = JSON.parseObject(decrypted, RefundOrderRequest.class);

        Operator operator = commonUtil.getOperator();
        logger.debug("/return/export/params, employeeId=" + operator.getUserId());

        List<RefundOrderResponse> refundOrderResponses = new ArrayList<>();
        //根据id超过分页数量
        if (CollectionUtils.isEmpty(refundOrderRequest.getRefundIds())) {
            refundOrderRequest.setPageSize(1000);
            RefundOrderPageRequest refundOrderPageRequest = KsBeanUtil.convert(refundOrderRequest,
                    RefundOrderPageRequest.class);
            RefundOrderPageResponse refundOrderPageResponse =
                    refundOrderQueryProvider.page(refundOrderPageRequest).getContext();
            refundOrderResponses = refundOrderPageResponse.getData();
        } else {
            RefundOrderWithoutPageRequest refundOrderWithoutPageRequest = KsBeanUtil.convert(refundOrderRequest,
                    RefundOrderWithoutPageRequest.class);
            RefundOrderWithoutPageResponse refundOrderWithoutPageResponse =
                    refundOrderQueryProvider.list(refundOrderWithoutPageRequest).getContext();
            refundOrderResponses = refundOrderWithoutPageResponse.getData();
        }
        String headerKey = "Content-Disposition";
        LocalDateTime dateTime = LocalDateTime.now();
        String fileName = String.format("批量退款明细_%s.xls", dateTime.format(formatter));
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("account/export/refund, fileName={}, error={}", fileName, e);
        }
        String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
        response.setHeader(headerKey, headerValue);
        try {
            export(refundOrderResponses, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new SbcRuntimeException(e);
        }
    }

    private void export(List<RefundOrderResponse> refundOrderResponses, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        excelHelper.addSheet(
                "退款明细",
                new Column[]{
                        new Column("退款流水号", new SpelColumnRender<RefundOrderResponse>("refundBillCode")),
                        new Column("退款时间", new SpelColumnRender<RefundOrderResponse>("refundBillTime")),
                        new Column("退单号", new SpelColumnRender<RefundOrderResponse>("returnOrderCode")),
                        new Column("客户名称", new SpelColumnRender<RefundOrderResponse>("customerName")),
                        new Column("应退金额", (cell, object) -> {
                            RefundOrderResponse refundOrderResponse = (RefundOrderResponse) object;
                            if (Objects.nonNull(refundOrderResponse.getReturnPrice())) {
                                cell.setCellValue("￥" + refundOrderResponse.getReturnPrice().toString());
                            }
                        }),
                        new Column("实退金额", (cell, object) -> {
                            RefundOrderResponse refundOrderResponse = (RefundOrderResponse) object;
                            if (Objects.nonNull(refundOrderResponse.getActualReturnPrice())) {
                                cell.setCellValue("￥" + refundOrderResponse.getActualReturnPrice().toString());
                            }
                        }),
                        new Column("退款方式", new SpelColumnRender<RefundOrderResponse>("payType.getDesc()")),
                        new Column("退款账户", new SpelColumnRender<RefundOrderResponse>("returnAccountName")),
                        new Column("状态", (cell, object) -> {
                            String status = "";
                            switch (((RefundOrderResponse) object).getRefundStatus()) {
                                case TODO:
                                    status = "待退款";
                                    break;
                                case REFUSE:
                                    status = "拒绝退款";
                                    break;
                                case FINISH:
                                    status = "已退款";
                                    break;
                                default:
                                    break;
                            }
                            cell.setCellValue(status);
                        }),
                        new Column("备注", new SpelColumnRender<RefundOrderResponse>("comment")),
                },
                refundOrderResponses
        );
        excelHelper.write(outputStream);
    }

    private Operator buildOperator(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        return Operator.builder()
                .platform(Platform.BOSS)
                .adminId(claims.get("adminId") == null ? "" : String.valueOf(claims.get("adminId")))
                .ip(String.valueOf(claims.get("ip")))
                .name(String.valueOf(claims.get("EmployeeName")))
                .account(String.valueOf(claims.get("EmployeeName")))
                .userId(String.valueOf(claims.get("employeeId")))
                .build();
    }
}
