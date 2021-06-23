package com.wanmi.sbc.account;

import com.alibaba.fastjson.JSON;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectByIdRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectByIdResponse;
import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
import com.wanmi.sbc.account.request.OrderInvoiceSaveRequest;
import com.wanmi.sbc.account.response.OrderInvoiceDetailResponse;
import com.wanmi.sbc.account.response.OrderInvoiceResponse;
import com.wanmi.sbc.account.response.OrderInvoiceViewResponse;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceQueryProvider;
import com.wanmi.sbc.customer.api.request.invoice.CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.order.api.provider.orderinvoice.OrderInvoiceProvider;
import com.wanmi.sbc.order.api.provider.orderinvoice.OrderInvoiceQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.orderinvoice.*;
import com.wanmi.sbc.order.api.request.trade.TradeAddInvoiceRequest;
import com.wanmi.sbc.order.api.response.orderinvoice.OrderInvoiceFindByOrderInvoiceIdAndDelFlagResponse;
import com.wanmi.sbc.order.bean.dto.GeneralInvoiceDTO;
import com.wanmi.sbc.order.bean.dto.InvoiceDTO;
import com.wanmi.sbc.order.bean.dto.OrderInvoiceDTO;
import com.wanmi.sbc.order.bean.dto.SpecialInvoiceDTO;
import com.wanmi.sbc.order.bean.vo.OrderInvoiceVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 订单开票服务
 * Created by CHENLI on 2017/5/5.
 */
@Api(tags = "OrderInvoiceController", description = "订单开票服务 Api")
@RestController
@RequestMapping("/account")
public class OrderInvoiceController {

    @Autowired
    private OrderInvoiceQueryProvider orderInvoiceQueryProvider;

    @Autowired
    private OrderInvoiceProvider orderInvoiceProvider;

    @Autowired
    private OrderInvoiceDetailService orderInvoiceDetailService;

    @Autowired
    private CustomerInvoiceQueryProvider customerInvoiceQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private InvoiceProjectQueryProvider invoiceProjectQueryProvider;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 导出文件名后的时间后缀
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * 分页查询订单开票
     *
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "分页查询订单开票")
    @EmployeeCheck
    @RequestMapping(value = "/orderInvoices", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> page(@RequestBody OrderInvoiceFindAllRequest queryRequest) {
        queryRequest.setCompanyInfoId( commonUtil.getCompanyInfoId());
        queryRequest.setStoreId(commonUtil.getStoreId());
        return ResponseEntity.ok( BaseResponse.success(orderInvoiceDetailService.page(queryRequest)));
    }

    /**
     * 新增订单开票信息
     *
     * @param saveRequest
     * @return
     */
    @ApiOperation(value = "新增订单开票信息")
    @RequestMapping(value = "/orderInvoice", method = RequestMethod.POST)
    @Transactional
    @GlobalTransactional
    public ResponseEntity<BaseResponse> save(@RequestBody OrderInvoiceSaveRequest saveRequest, HttpServletRequest request) {
        Long storeId = commonUtil.getStoreId();
        //订单开票前判断订单的状态
        orderInvoiceDetailService.findOrderCheckState(saveRequest.getOrderNo(), storeId);

        OrderInvoiceGetByOrderNoRequest orderInvoiceGetByOrderNoRequest = OrderInvoiceGetByOrderNoRequest.builder().orderNo(saveRequest.getOrderNo()).build();

        //订单已开票
        if (orderInvoiceQueryProvider.getByOrderNo(orderInvoiceGetByOrderNoRequest).getContext().getOrderInvoiceVO()!= null) {
            return ResponseEntity.ok(BaseResponse.error("该订单已开过票"));
        }
        //如果开的是增票，判断客户是否具备增票资质
        CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest = new CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest();
        customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest.setCustomerId(saveRequest.getCustomerId());
        BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse>  customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerIdAndDelFlagAndCheckState(customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest);
        CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse response = customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse.getContext();
        if(saveRequest.getInvoiceType() == InvoiceType.SPECIAL && Objects.isNull(response)){
            return ResponseEntity.ok(BaseResponse.error("增票资质审核未通过"));
        }
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        saveRequest.setCompanyInfoId( commonUtil.getCompanyInfoId());
        saveRequest.setStoreId(storeId);

        OrderInvoiceGenerateRequest orderInvoiceGenerateRequest = OrderInvoiceGenerateRequest.builder()
                .employeeId(employeeId).invoiceState(InvoiceState.ALREADY)
                .orderInvoiceDTO(KsBeanUtil.convert(saveRequest, OrderInvoiceDTO.class)).build();

        Optional<OrderInvoiceVO> remotecall =
                Optional.ofNullable(
                orderInvoiceProvider.generateOrderInvoice(orderInvoiceGenerateRequest).getContext().getOrderInvoiceVO());

        if(remotecall.isPresent()){

            OrderInvoiceVO  orderInvoice = remotecall.get();
            InvoiceProjectByIdRequest invoiceProjectByIdRequest = new InvoiceProjectByIdRequest();
            invoiceProjectByIdRequest.setProjcetId(orderInvoice.getProjectId());
            BaseResponse<InvoiceProjectByIdResponse> baseResponse = invoiceProjectQueryProvider.getById(invoiceProjectByIdRequest);
            InvoiceProjectByIdResponse invoiceProjectByIdResponse = baseResponse.getContext();
            InvoiceDTO invoice = InvoiceDTO.builder()
                    .orderInvoiceId(orderInvoice.getOrderInvoiceId())
                    .type(orderInvoice.getInvoiceType().toValue())
                    .contacts(saveRequest.getContacts())
                    .projectId(orderInvoice.getProjectId())
                    .projectName(invoiceProjectByIdResponse.getProjectName())
                    .generalInvoice(orderInvoice.getInvoiceType() == InvoiceType.NORMAL ? StringUtils.isBlank(orderInvoice.getInvoiceTitle()) ?
                            GeneralInvoiceDTO.builder().title("").flag(0).build()
                            : GeneralInvoiceDTO.builder().title(orderInvoice.getInvoiceTitle()).flag(1).identification(orderInvoice.getTaxpayerNumber()).build() : null)
                    .phone( saveRequest.getPhone())
                    .address( saveRequest.getAddress())
                    .taxNo( orderInvoice.getTaxpayerNumber() )
                    .projectUpdateTime(DateUtil.format(invoiceProjectByIdResponse.getUpdateTime() != null ? invoiceProjectByIdResponse.getUpdateTime() : LocalDateTime.now(), DateUtil.FMT_TIME_1))
                    .addressId(saveRequest.getAddressInfoId())
                    .sperator(Boolean.TRUE)
                    .build();
            if(orderInvoice.getInvoiceType() == InvoiceType.SPECIAL){
                BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse> invoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse =
                        customerInvoiceQueryProvider.getByCustomerIdAndDelFlagAndCheckState(
                                CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest.builder()
                                        .customerId(saveRequest.getCustomerId()).build()
                        );
                CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse invoiceByCustomerIdAndDelFlagAndCheckStateResponse = invoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse.getContext();
                if (Objects.nonNull(invoiceByCustomerIdAndDelFlagAndCheckStateResponse)){
                    SpecialInvoiceDTO spInvoice = new SpecialInvoiceDTO();
                    if (invoiceByCustomerIdAndDelFlagAndCheckStateResponse.getCheckState() != CheckState.CHECKED) {
                        throw new SbcRuntimeException("K-010013");
                    }
                    spInvoice.setId(invoiceByCustomerIdAndDelFlagAndCheckStateResponse.getCustomerInvoiceId());
                    spInvoice.setAccount(invoiceByCustomerIdAndDelFlagAndCheckStateResponse.getBankNo());
                    spInvoice.setIdentification(invoiceByCustomerIdAndDelFlagAndCheckStateResponse.getTaxpayerNumber());
                    spInvoice.setAddress(invoiceByCustomerIdAndDelFlagAndCheckStateResponse.getCompanyAddress());
                    spInvoice.setBank(invoiceByCustomerIdAndDelFlagAndCheckStateResponse.getBankName());
                    spInvoice.setCompanyName(invoiceByCustomerIdAndDelFlagAndCheckStateResponse.getCompanyName());
                    spInvoice.setPhoneNo(invoiceByCustomerIdAndDelFlagAndCheckStateResponse.getCompanyPhone());
                    invoice.setSpecialInvoice(spInvoice);
                }
            }

            TradeAddInvoiceRequest tradeAddInvoiceRequest = TradeAddInvoiceRequest.builder()
                    .tid(orderInvoice.getOrderNo())
                    .invoiceDTO(invoice)
                    .build();
            tradeProvider.saveInvoice(tradeAddInvoiceRequest);
            // tradeService.saveInvoice(orderInvoice.getOrderNo(), invoice);
            operateLogMQUtil.convertAndSend("财务","新增开票",
                    "新增开票：订单号"+orderInvoice.getOrderNo());

        }
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }


    /**
     * 订单开票详情/新增订单开票时
     *
     * @param orderNo
     * @return
     */
    @ApiOperation(value = "订单开票详情/新增订单开票时")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "orderNo", value = "订单编号", required = true)
    @RequestMapping(value = "/orderInvoiceDetail/{orderNo}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse<OrderInvoiceDetailResponse>> findOrderInvoiceDetail(@PathVariable String orderNo) {
        //新增订单开票时判断订单的状态
        orderInvoiceDetailService.findOrderCheckState(orderNo, commonUtil.getStoreId());
        OrderInvoiceGetByOrderNoRequest orderInvoiceGetByOrderNoRequest = OrderInvoiceGetByOrderNoRequest.builder()
                .orderNo(orderNo).build();

        //订单已开票
        if (orderInvoiceQueryProvider.getByOrderNo(orderInvoiceGetByOrderNoRequest).getContext().getOrderInvoiceVO()!=null) {
            return ResponseEntity.ok(BaseResponse.error("该订单已开过票"));
        }
        OrderInvoiceDetailResponse response = orderInvoiceDetailService.findOrderInvoiceDetail(orderNo);
        return ResponseEntity.ok(BaseResponse.success(response));
    }


    /**
     * 根据开票单号查询
     *
     * @param orderInvoiceId orderInvoiceId
     * @return ResponseEntity<BaseResponse<OrderInvoiceViewResponse>>
     */
    @ApiOperation(value = "根据开票单号查询")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "orderInvoiceId", value = "订单发票ID",
            required = true)
    @RequestMapping(value = "/orderInvoice/{orderInvoiceId}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse<OrderInvoiceViewResponse>> findOrderInvoiceView(@PathVariable("orderInvoiceId") String orderInvoiceId) {
        return ResponseEntity.ok(BaseResponse.success(orderInvoiceDetailService.findByOrderInvoiceId(orderInvoiceId)));
    }

    /**
     * 订单批量/单个开票
     *
     * @param editRequest
     * @return
     */
    @ApiOperation(value = "订单批量/单个开票")
    @RequestMapping(value = "/orderInvoiceState", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> updateOrderInvoiceState(@RequestBody OrderInvoiceModifyStateRequest editRequest) {
        orderInvoiceProvider.modifyOrderInvoiceState(editRequest);
        //记录日志
        if(1 == editRequest.getOrderInvoiceIds().size()){
            OrderInvoiceFindByOrderInvoiceIdAndDelFlagResponse response =
                    orderInvoiceQueryProvider.findByOrderInvoiceIdAndDelFlag(OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest.builder().id(editRequest.getOrderInvoiceIds().get(0)).flag(DeleteFlag.NO).build()).getContext();
            String orderNo = Objects.nonNull(response) ?
                    response.getOrderInvoiceVO().getOrderNo() : "";
            operateLogMQUtil.convertAndSend("财务","开票","开票：订单号"+ orderNo);
        }else{
            operateLogMQUtil.convertAndSend("财务","批量开票","批量开票");
        }

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 订单发票作废
     *
     * @param orderInvoiceId
     * @return
     */
    @ApiOperation(value = "订单发票作废")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "orderInvoiceId", value = "订单发票ID",
            required = true)
    @RequestMapping(value = "/orderInvoice/{orderInvoiceId}", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> invalidInvoice(@PathVariable String orderInvoiceId) {
        OrderInvoiceFindByOrderInvoiceIdAndDelFlagResponse response =
                orderInvoiceQueryProvider.findByOrderInvoiceIdAndDelFlag(OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest.builder().id(orderInvoiceId).flag(DeleteFlag.NO).build()).getContext();
        String orderNo = Objects.nonNull(response) ?
                response.getOrderInvoiceVO().getOrderNo() : "";

        OrderInvoiceInvalidRequest request = OrderInvoiceInvalidRequest.builder().orderInvoiceId(orderInvoiceId).build();
        orderInvoiceProvider.invalid(request);

        // 记录日志
        operateLogMQUtil.convertAndSend("财务","作废","作废：订单号"+ orderNo);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 导出订单开票
     *
     * @return
     */
    @ApiOperation(value = "导出订单开票")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @RequestMapping(value = "/export/orderInvoices/{encrypted}", method = RequestMethod.GET)
    public void exportByParams(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));

        OrderInvoiceFindAllRequest orderInvoiceQueryRequest = JSON.parseObject(decrypted, OrderInvoiceFindAllRequest.class);
        logger.debug("/export/orderInvoices/*, employeeId=" + commonUtil.getOperatorId());

        orderInvoiceQueryRequest.setStoreId(commonUtil.getStoreId());
        List<OrderInvoiceResponse> orderInvoiceResponses = orderInvoiceDetailService.queryOrderInvoice(orderInvoiceQueryRequest);

        String headerKey = "Content-Disposition";
        LocalDateTime dateTime = LocalDateTime.now();
        String fileName = String.format("订单开票_%s.xls", dateTime.format(formatter));
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("account/export/refund, fileName={}, error={}", fileName, e);
        }
        String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
        response.setHeader(headerKey, headerValue);
        try {
            export(orderInvoiceResponses, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new SbcRuntimeException(e);
        }
    }


    private void export(List<OrderInvoiceResponse> orderInvoiceResponses, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        excelHelper.addSheet(
                "订单开票",
                new Column[]{
                        new Column("客户名称", new SpelColumnRender<OrderInvoiceResponse>("customerName")),
                        new Column("开票时间", new SpelColumnRender<OrderInvoiceResponse>("invoiceTime")),
                        new Column("订单号", new SpelColumnRender<OrderInvoiceResponse>("orderNo")),
                        new Column("订单金额", new SpelColumnRender<OrderInvoiceResponse>("orderPrice")),
                        new Column("付款状态", (cell, object) -> {
                            String status = "";
                            if (Objects.isNull(((OrderInvoiceResponse) object).getPayOrderStatus())) {
                                cell.setCellValue("未付款");
                                return;
                            }
                            switch (((OrderInvoiceResponse) object).getPayOrderStatus()) {
                                case PAYED:
                                    status = "已付款";
                                    break;
                                case NOTPAY:
                                    status = "未付款";
                                    break;
                                case TOCONFIRM:
                                    status = "待确认";
                                    break;
                                default:
                                    break;
                            }
                            cell.setCellValue(status);
                        }),
                        new Column("发票类型", (cell, object) -> {
                            InvoiceType invoiceType = ((OrderInvoiceResponse) object).getInvoiceType();
                            if (InvoiceType.SPECIAL.equals(invoiceType)) {
                                cell.setCellValue("增值税专用发票");
                            } else {
                                cell.setCellValue("普通发票");
                            }
                        }),
                        new Column("发票抬头", (cell, object) -> {
                            String invoiceTitle = ((OrderInvoiceResponse) object).getInvoiceTitle();
                            if (Objects.isNull(invoiceTitle) || StringUtils.isBlank(invoiceTitle)) {
                                cell.setCellValue("个人");
                            } else {
                                cell.setCellValue(invoiceTitle);
                            }
                        }),
                        new Column("开票状态", (cell, object) -> {
                            InvoiceState invoiceState = ((OrderInvoiceResponse) object).getInvoiceState();
                            if (InvoiceState.ALREADY.equals(invoiceState)) {
                                cell.setCellValue("已开票");
                            } else {
                                cell.setCellValue("待开票");
                            }
                        }),
                },
                orderInvoiceResponses
        );
        excelHelper.write(outputStream);
    }

    /**
     * 删除订单开票信息
     *
     * @param orderInvoiceId
     * @return
     */
    @ApiOperation(value = "删除订单开票信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "orderInvoiceId", value = "订单发票ID",
            required = true)
    @RequestMapping(value = "/orderInvoice/{orderInvoiceId}", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> deleteOrderInvoice(@PathVariable String orderInvoiceId) {
        OrderInvoiceFindByOrderInvoiceIdAndDelFlagResponse response =
                orderInvoiceQueryProvider.findByOrderInvoiceIdAndDelFlag(OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest.builder().id(orderInvoiceId).flag(DeleteFlag.NO).build()).getContext();

        orderInvoiceProvider.delete(OrderInvoiceDeleteRequest.builder().orderInvoiceId(orderInvoiceId).build());

        // 记录日志
        operateLogMQUtil.convertAndSend("财务","删除开票项目",
                "删除开票项目：项目名称"+(Objects.nonNull(response) ?
                        response.getOrderInvoiceVO().getInvoiceProject().getProjectName() : ""));

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }
}
