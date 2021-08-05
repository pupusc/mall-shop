package com.wanmi.sbc.order.orderinvoice.service;


import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.orderinvoice.request.OrderInvoiceModifyOrderStatusRequest;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.TradeService;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
import com.wanmi.sbc.account.bean.enums.IsCompany;
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
import com.wanmi.sbc.order.orderinvoice.model.root.OrderInvoice;
import com.wanmi.sbc.order.orderinvoice.repository.OrderInvoiceRepository;
import com.wanmi.sbc.order.orderinvoice.request.OrderInvoiceQueryRequest;
import com.wanmi.sbc.order.orderinvoice.request.OrderInvoiceSaveRequest;
import com.wanmi.sbc.order.orderinvoice.response.OrderInvoiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 订单开票服务层
 * Created by CHENLI on 2017/5/5.
 */
@Service
public class OrderInvoiceService {
    @Autowired
    private OrderInvoiceRepository repository;

    @Autowired
    private CustomerInvoiceQueryProvider customerInvoiceQueryProvider;



    @Autowired
    private TradeService tradeService;


    /**
     * 生成开票
     * @param orderInvoiceSaveRequest orderInvoiceSaveRequest
     * @return Optional<OrderInvoice>
     */
    @Transactional
    @GlobalTransactional
    public Optional<OrderInvoice> generateOrderInvoice(OrderInvoiceSaveRequest orderInvoiceSaveRequest, String employeeId, InvoiceState invoiceState){
        OrderInvoice orderInvoice = new OrderInvoice();
        if (!CollectionUtils.isEmpty(repository.findByDelFlagAndOrderNo(DeleteFlag.NO, orderInvoiceSaveRequest.getOrderNo()))) {
            throw new SbcRuntimeException("K-070003");
        }
        BeanUtils.copyProperties(orderInvoiceSaveRequest, orderInvoice);
        if(Objects.nonNull(orderInvoiceSaveRequest.getInvoiceTime())){
            orderInvoice.setInvoiceTime(DateUtil.parse(orderInvoiceSaveRequest.getInvoiceTime(),DateUtil.FMT_TIME_1));
        }
        if (InvoiceType.SPECIAL.equals(orderInvoiceSaveRequest.getInvoiceType())) {
            CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest = new CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest();
            customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest.setCustomerId(orderInvoiceSaveRequest.getCustomerId());
            BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse> customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerIdAndDelFlagAndCheckState(customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest);
            CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse = customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse.getContext();
            if (Objects.nonNull(customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse)){
                orderInvoice.setInvoiceTitle(customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse.getCompanyName());
            }
            orderInvoice.setIsCompany(IsCompany.YES);
        }else {
            if (StringUtils.isBlank(orderInvoiceSaveRequest.getInvoiceTitle())) {
                orderInvoice.setIsCompany(IsCompany.NO);
            }else {
                orderInvoice.setIsCompany(IsCompany.YES);
            }
        }
        orderInvoice.setDelFlag(DeleteFlag.NO);
        orderInvoice.setCreateTime(LocalDateTime.now());
        orderInvoice.setOperateId(employeeId);
        orderInvoice.setInvoiceState(invoiceState);

        //更新订单状态
        Trade trade= tradeService.detail(orderInvoiceSaveRequest.getOrderNo());

        if (PayState.NOT_PAID==trade.getTradeState().getPayState()) {
            orderInvoice.setPayStatus(PayOrderStatus.NOTPAY);
        } else  if (PayState.PAID==trade.getTradeState().getPayState()) {
            orderInvoice.setPayStatus(PayOrderStatus.PAYED);
        }else  if (PayState.UNCONFIRMED==trade.getTradeState().getPayState()) {
            orderInvoice.setPayStatus(PayOrderStatus.TOCONFIRM);
        }
        orderInvoice.setOrderStatus(trade.getTradeState().getFlowState());

        return Optional.ofNullable(repository.save(orderInvoice));
    }

    /**
     * 修改订单开票
     * @param orderInvoiceSaveRequest orderInvoiceSaveRequest
     * @return Optional<OrderInvoice>
     */
    @Transactional
    public Optional<OrderInvoice> updateOrderInvoice(OrderInvoiceSaveRequest orderInvoiceSaveRequest){
        if(StringUtils.isEmpty(orderInvoiceSaveRequest.getOrderInvoiceId())){
            throw new SbcRuntimeException("K-000009");

        }
        OrderInvoice orderInvoice = repository.findById(orderInvoiceSaveRequest.getOrderInvoiceId()).orElse(null);
        KsBeanUtil.copyProperties(orderInvoiceSaveRequest, orderInvoice);
        if (InvoiceType.SPECIAL.equals(orderInvoiceSaveRequest.getInvoiceType())) {
            CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest = new CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest();
            customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest.setCustomerId(orderInvoiceSaveRequest.getCustomerId());
            BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse> customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerIdAndDelFlagAndCheckState(customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest);
            CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse = customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse.getContext();
            if (Objects.nonNull(customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse)) {
                orderInvoice.setInvoiceTitle(customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse.getCompanyName());
            }
            orderInvoice.setIsCompany(IsCompany.YES);
        }else {
            if (StringUtils.isBlank(orderInvoiceSaveRequest.getInvoiceTitle())) {
                orderInvoice.setIsCompany(IsCompany.NO);
            }else {
                orderInvoice.setIsCompany(IsCompany.YES);
            }
        }
        orderInvoice.setUpdateTime(LocalDateTime.now());
        return Optional.ofNullable(repository.save(orderInvoice));
    }


    /**
     * 修改订单状态
     * @param orderInvoiceModifyOrderRequest orderInvoiceSaveRequest
     * @return Optional<OrderInvoice>
     */
    @Transactional
    public Optional<OrderInvoice> updateOrderStatus(OrderInvoiceModifyOrderStatusRequest orderInvoiceModifyOrderRequest){
        OrderInvoice orderInvoice = repository.findByOrderNoAndDelFlag(orderInvoiceModifyOrderRequest.getOrderNo(),DeleteFlag.NO).orElse(null);
        orderInvoice.setPayStatus(orderInvoiceModifyOrderRequest.getPayOrderStatus());
        orderInvoice.setOrderStatus(orderInvoiceModifyOrderRequest.getOrderStatus());
        return Optional.ofNullable(repository.save(orderInvoice));
    }


    /**
     * 根据订单号查询订单是否已开过票
     * @param orderNo
     * @return
     */
    public Optional<OrderInvoice> findByOrderNo(String orderNo){
        return repository.findByOrderNoAndDelFlag(orderNo,DeleteFlag.NO);
    }

    /**
     * 订单批量/单个开票
     * @param orderInvoiceIds
     */
    @Modifying
    @Transactional
    public void updateOrderInvoiceState(List<String> orderInvoiceIds){
        repository.checkInvoiceState(orderInvoiceIds, LocalDateTime.now());
    }

    /**
     * 订单发票作废
     * @param orderInvoiceId
     */
    @Modifying
    @Transactional
    public void invalidInvoice(String orderInvoiceId){
        repository.invalidInvoice(orderInvoiceId, LocalDateTime.now());
    }

    /**
     * 订单开票导出
     * @param orderInvoiceResponses
     * @param outputStream
     */
    public void export(List<OrderInvoiceResponse> orderInvoiceResponses, OutputStream outputStream){
        ExcelHelper excelHelper = new ExcelHelper();
        excelHelper
                .addSheet(
                        "订单开票导出",
                        new Column[]{
                                new Column("客户名称", new SpelColumnRender<OrderInvoiceResponse>("customerName")),
                                new Column("开票时间", new SpelColumnRender<OrderInvoiceResponse>("invoiceTime")),
                                new Column("订单编号", new SpelColumnRender<OrderInvoiceResponse>("orderNo")),
                                new Column("订单金额", new SpelColumnRender<OrderInvoiceResponse>("orderPrice")),
                                new Column("付款状态", new SpelColumnRender<OrderInvoiceResponse>("payState")),
                                new Column("发票类型", new SpelColumnRender<OrderInvoiceResponse>("invoiceType == 0 ? '普通发票' : '增值税发票' ")),
                                new Column("发票抬头", new SpelColumnRender<OrderInvoiceResponse>("invoiceTitle")),
                                new Column("开票状态", new SpelColumnRender<OrderInvoiceResponse>("invoiceState == 0 ? '待开票' : '已开票' "))
                        },
                        orderInvoiceResponses
                );
        excelHelper.write(outputStream);
    }

    /**
     * 根据ID删除订单开票信息
     * @param orderInvoiceId
     * @return
     */
    @Modifying
    @Transactional
    public void deleteOrderInvoice(String orderInvoiceId){
        repository.deleteOrderInvoice(orderInvoiceId, LocalDateTime.now());
    }

    /**
     * 根据订单号删除订单开票
     * @param orderNo
     */
    @Modifying
    @Transactional
    public void deleteOrderInvoiceByOrderNo(String orderNo){
        repository.deleteOrderInvoiceByOrderNo(orderNo, LocalDateTime.now());
    }

    /**
     * 根据状态统计开票信息
     * @param invoiceState
     * @return
     */
    public long countInvoiceByState(Long companyInfoId, Long storeId, InvoiceState invoiceState){
        OrderInvoiceQueryRequest orderInvoiceQueryRequest = new OrderInvoiceQueryRequest();
        orderInvoiceQueryRequest.setInvoiceState(invoiceState);
        orderInvoiceQueryRequest.setCompanyInfoId(companyInfoId);
        orderInvoiceQueryRequest.setStoreId(storeId);
        return repository.count(orderInvoiceQueryRequest.getWhereCriteria());
    }

    public Optional<OrderInvoice> findByOrderInvoiceIdAndDelFlag(String orderInvoiceId, DeleteFlag delFlag){
       return repository.findByOrderInvoiceIdAndDelFlag(orderInvoiceId,delFlag);
    }

    public Page<OrderInvoice> findAll(Specification<OrderInvoice> specification, PageRequest pageRequest){
        return repository.findAll(specification,pageRequest);

    }

    /**
     * 根据id获取订单开票详情
     * @param orderInvoiceId
     * @return
     */
    public Optional<OrderInvoice> findByOrderInvoiceId(String orderInvoiceId){
        return repository.findById(orderInvoiceId);
    }
}
