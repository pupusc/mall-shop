package com.wanmi.sbc.account;

import com.google.common.collect.Lists;
import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectByIdRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectByIdResponse;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
import com.wanmi.sbc.account.response.OrderInvoiceDetailResponse;
import com.wanmi.sbc.account.response.OrderInvoiceResponse;
import com.wanmi.sbc.account.response.OrderInvoiceViewResponse;
import com.wanmi.sbc.common.base.BaseQueryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyListRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.request.invoice.CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest;
import com.wanmi.sbc.customer.api.request.invoice.CustomerInvoiceByCustomerIdAndDelFlagRequest;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByCustomerIdAndDelFlagResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.order.api.provider.orderinvoice.OrderInvoiceQueryProvider;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderProvider;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.orderinvoice.OrderInvoiceFindAllRequest;
import com.wanmi.sbc.order.api.request.orderinvoice.OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderByOrderCodeRequest;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderByOrderCodesRequest;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdsRequest;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdsResponse;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.*;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;


/**
 * Created by CHENLI on 2017/5/8.
 */
@Service
public class OrderInvoiceDetailService {

    @Autowired
    private PayOrderProvider payOrderProvider;

    @Autowired
    private PayOrderQueryProvider payOrderQueryProvider;

    @Autowired
    private OrderInvoiceQueryProvider orderInvoiceQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private CustomerInvoiceQueryProvider customerInvoiceQueryProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private InvoiceProjectQueryProvider invoiceProjectQueryProvider;

    /**
     * 分页查询订单开票信息
     * @param queryRequest
     * @return BaseQueryResponse<OrderInvoiceResponse>
     */
    public BaseQueryResponse<OrderInvoiceResponse> page(OrderInvoiceFindAllRequest queryRequest){
        BaseQueryResponse<OrderInvoiceResponse> baseQueryResponse = new BaseQueryResponse<OrderInvoiceResponse>();
        //模糊匹配会员/商户名称，不符合条件直接返回
        if(!this.likeCustomerAndSupplierName(queryRequest)){
            baseQueryResponse.setData(EMPTY_LIST);
            baseQueryResponse.setTotal(0L);
            return baseQueryResponse;
        }



        MicroServicePage<OrderInvoiceVO> orderInvoices = orderInvoiceQueryProvider.findAll(queryRequest).getContext().getValue();

//        Page<OrderInvoice> orderInvoices = repository.findAll(queryRequest.getWhereCriteria(),queryRequest.getPageRequest());
        if (Objects.isNull(orderInvoices) || CollectionUtils.isEmpty(orderInvoices.getContent())) {
            baseQueryResponse.setData(EMPTY_LIST);
            baseQueryResponse.setTotal(0L);
            return baseQueryResponse;
        }
        //拼接返回的数据
        List<OrderInvoiceResponse> invoiceResponses = getOrderInvoiceResponse(orderInvoices.getContent());

        baseQueryResponse.setData(invoiceResponses);
        baseQueryResponse.setTotal(orderInvoices.getTotalElements());

        return baseQueryResponse;
    }

    /**
     * 导出订单发票
     * @param queryRequest
     * @return
     */
    public List<OrderInvoiceResponse> queryOrderInvoice(OrderInvoiceFindAllRequest queryRequest){
        List<OrderInvoiceResponse> responseList = Lists.newArrayList();
        //模糊匹配会员/商户名称，不符合条件直接返回
        if(!this.likeCustomerAndSupplierName(queryRequest)){
            return responseList;
        }

        queryRequest.setPageNum(0);
        queryRequest.setPageSize(1000);

        MicroServicePage<OrderInvoiceVO> orderInvoicesPage = orderInvoiceQueryProvider.findAll(queryRequest).getContext().getValue();

        //最多返回1000条
       // Page<OrderInvoice> orderInvoicesPage = repository.findAll(queryRequest.getWhereCriteria(), new PageRequest(0, 1000));
        if (Objects.nonNull(orderInvoicesPage) && !Collections.isEmpty(orderInvoicesPage.getContent())) {
            responseList =  getOrderInvoiceResponse(orderInvoicesPage.getContent());
        }
        //拼接返回的数据
        return responseList;
    }

    /**
     * 转换对象 新 ，循环查询修改为内存缓存
     * @param orderInvoices
     * @return
     */
    private List<OrderInvoiceResponse> getOrderInvoiceResponseNew(List<OrderInvoiceVO> orderInvoices) {


        List<String> orderNos = orderInvoices.stream().map(OrderInvoiceVO::getOrderNo).collect(Collectors.toList());
        BaseResponse<TradeGetByIdsResponse> tradeResponse = tradeQueryProvider.getByIds(TradeGetByIdsRequest.builder()
                .tid(orderNos).build());


        List<TradeVO> tradeVos = tradeResponse.getContext().getTradeVO();
        //转换为map
        Map<String, TradeVO> tradeMap
                = tradeVos.stream().collect(Collectors.toMap(TradeVO::getId, Function.identity()));

        FindPayOrderByOrderCodesRequest requeststr = FindPayOrderByOrderCodesRequest.builder().value(orderNos).build();
        List<PayOrderVO> values = payOrderQueryProvider.findPayOrderByOrderCodes(requeststr).getContext().getValues();

        //转换为map
        Map<String, PayOrderVO> PayOrderMap
                = values.stream().collect(Collectors.toMap(PayOrderVO::getOrderCode, Function.identity()));

        List<OrderInvoiceResponse> items = new ArrayList<>();
        orderInvoices.forEach(orderInvoice -> {
            OrderInvoiceResponse invoiceResponse = new OrderInvoiceResponse();
            BeanUtils.copyProperties(orderInvoice, invoiceResponse);
            if (CollectionUtils.isNotEmpty(tradeVos)) {
                TradeVO tradeVO = tradeMap.get(orderInvoice.getOrderNo());
                if(Objects.nonNull(tradeVO)){
                    if (Objects.nonNull(tradeVO.getTradePrice())) {
                        invoiceResponse.setOrderNo(tradeVO.getId());
                        invoiceResponse.setOrderPrice(tradeVO.getTradePrice().getTotalPrice());
                    }
                    if (Objects.nonNull(tradeVO.getBuyer())) {
                        //客户相关信息
                        invoiceResponse.setCustomerId(tradeVO.getBuyer().getId());
                        invoiceResponse.setCustomerName(tradeVO.getBuyer().getName());
                    }
                    //商家名称 兼容老数据
                    SupplierVO supplier;
                    if ((supplier = tradeVO.getSupplier()) != null) {
                        invoiceResponse.setSupplierName(supplier.getSupplierName());
                    }
                }

            }
            PayOrderVO payOrderVO = PayOrderMap.get(orderInvoice.getOrderNo());
            if (Objects.nonNull(payOrderVO)) {
                invoiceResponse.setPayOrderStatus(payOrderVO.getPayOrderStatus());
            }
            items.add(invoiceResponse);
        });
        return items;
    }


    /**
     * 转换对象
     * @param orderInvoices
     * @return
     */
    private List<OrderInvoiceResponse> getOrderInvoiceResponse(List<OrderInvoiceVO> orderInvoices){
        //拼接返回的数据
        return orderInvoices.parallelStream().map((OrderInvoiceVO orderInvoice) -> {
            OrderInvoiceResponse invoiceResponse = new OrderInvoiceResponse();
            BeanUtils.copyProperties(orderInvoice,invoiceResponse);
            //订单相关信息
            TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                    .tid(orderInvoice.getOrderNo()).build()).getContext().getTradeVO();
            if(Objects.nonNull(trade)){
                invoiceResponse.setOrderNo(trade.getId());
                if(Objects.nonNull(trade.getTradePrice())){
                    invoiceResponse.setOrderPrice(trade.getTradePrice().getTotalPrice());
                }
                if(Objects.nonNull(trade.getBuyer())){
                    //客户相关信息
                    invoiceResponse.setCustomerId(trade.getBuyer().getId());
                    invoiceResponse.setCustomerName(trade.getBuyer().getName());
                }
                //商家名称 兼容老数据
                SupplierVO supplier;
                if ( ( supplier = trade.getSupplier()) != null) {
                    invoiceResponse.setSupplierName( supplier.getSupplierName());
                }
            }
            FindPayOrderByOrderCodeRequest requeststr = FindPayOrderByOrderCodeRequest.builder().value(orderInvoice.getOrderNo()).build();


            PayOrderVO value =  payOrderQueryProvider.findPayOrderByOrderCode(requeststr).getContext().getValue();

            if(value != null){

                invoiceResponse.setPayOrderStatus(value.getPayOrderStatus());
            }

            invoiceResponse.setTradeState(trade.getTradeState());

            return invoiceResponse;
        }).collect(Collectors.toList());
    }

    /**
     * 查看订单开票详情
     * @param orderNo 订单号
     * @return
     */
    public OrderInvoiceDetailResponse findOrderInvoiceDetail(String orderNo){
        OrderInvoiceDetailResponse detailResponse = new OrderInvoiceDetailResponse();
        //订单开票信息

        Optional<OrderInvoiceVO>   remotecall = Optional.ofNullable(

        orderInvoiceQueryProvider.findByOrderInvoiceIdAndDelFlag(OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest.builder()
        .id(orderNo).flag(DeleteFlag.NO).build()).getContext().getOrderInvoiceVO());



        //repository.findByOrderNoAndDelFlag(orderNo, DeleteFlag.NO)
        remotecall .ifPresent(orderInvoice -> {
            BeanUtils.copyProperties(orderInvoice,detailResponse);
            //开票项目
            InvoiceProjectByIdRequest invoiceProjectByIdRequest = new InvoiceProjectByIdRequest();
            invoiceProjectByIdRequest.setProjcetId(orderInvoice.getProjectId());
            BaseResponse<InvoiceProjectByIdResponse> baseResponse = invoiceProjectQueryProvider.getById(invoiceProjectByIdRequest);
            InvoiceProjectByIdResponse invoiceProjectByIdResponse = baseResponse.getContext();
           // InvoiceProjectVO invoiceProject = orderInvoice.getInvoiceProject();
            detailResponse.setProjectName(invoiceProjectByIdResponse.getProjectName());
        });
        //订单信息
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                .tid(orderNo).build()).getContext().getTradeVO();
        if(trade != null) {
            detailResponse.setCustomerName(trade.getBuyer().getName());
            detailResponse.setCustomerId(trade.getBuyer().getId());
            this.transformTradeInfo(trade, detailResponse);
            //增专票信息
            BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse> baseResponse =
                    customerInvoiceQueryProvider.getByCustomerIdAndDelFlagAndCheckState(
                            CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest.builder()
                                    .customerId(trade.getBuyer().getId()).build()
                    );
            CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse response = baseResponse.getContext();
            if (Objects.nonNull(response)){
                detailResponse.setInvoiceTitle(response.getCompanyName());
                //审核通过的时候才设置
                if (CheckState.CHECKED.equals(response.getCheckState())) {
                    detailResponse.setCustomerInvoiceId(response.getCustomerInvoiceId());
                }
                BeanUtils.copyProperties(response,detailResponse,"customerName");
            }
//            customerInvoiceRepository.findByCustomerId(trade.getBuyer().getId()).ifPresent(customerInvoice -> {
//
//            });
        }

        return detailResponse;
    }

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    public Boolean findByOrderNo(String orderNo){
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                .tid(orderNo).build()).getContext().getTradeVO();
        return Objects.isNull(trade);
    }

    /**
     * 订单开票前判断订单的状态
     * @param orderNo
     */
    public void findOrderCheckState(String orderNo, Long storeId) {
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                .tid(orderNo).build()).getContext().getTradeVO();
        //订单不存在
        if (Objects.isNull(trade)|| null == trade.getId()) {
            throw new SbcRuntimeException("K-050100", new Object[]{orderNo});
        }
        // 商家只能给自己店铺的订单开票, boss平台都可以开票
        if (null != storeId && storeId > 1L) {
            if (!storeId.equals(trade.getSupplier().getStoreId())) {
                throw new SbcRuntimeException("K-050100", new Object[]{orderNo});
            }
        }
        //订单未付款
        if (!trade.getTradeState().getPayState().equals(PayState.PAID)) {
            throw new SbcRuntimeException("K-050134");
        }
    }


    /**
     * 根据id查询开票详情
     *
     * @param invoiceId invoiceId
     * @return OrderInvoiceViewResponse
     */
    public OrderInvoiceViewResponse findByOrderInvoiceId(String invoiceId) {

        OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest orderInvoiceFindByOrderInvoiceIdAndDelFlagRequest =
                OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest.builder()
                .id(invoiceId).flag(DeleteFlag.NO).build();


        Optional<OrderInvoiceVO>   remotecall = Optional.ofNullable(orderInvoiceQueryProvider.findByOrderInvoiceIdAndDelFlag(orderInvoiceFindByOrderInvoiceIdAndDelFlagRequest).getContext()
                .getOrderInvoiceVO());



        return remotecall
                .map(orderInvoice -> {
            TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                    .tid(orderInvoice.getOrderNo()).build()).getContext().getTradeVO();
            if (Objects.isNull(trade)) {
                throw new SbcRuntimeException("K-050100", new Object[]{orderInvoice.getOrderNo()});
            }


                    FindPayOrderRequest findPayOrderRequest = FindPayOrderRequest.builder().value(trade.getId()).build();

            BaseResponse<FindPayOrderResponse>  findPayOrderResponse =  payOrderQueryProvider.findPayOrder(findPayOrderRequest);

            // PayOrderResponse payOrderResponse = payOrderService.findPayOrder(trade.getId());

            FindPayOrderResponse payOrderResponse = findPayOrderResponse.getContext();

            if (Objects.isNull(payOrderResponse)) {
                throw new SbcRuntimeException("K-070001");
            }
            InvoiceProjectByIdRequest invoiceProjectByIdRequest = new InvoiceProjectByIdRequest();
            invoiceProjectByIdRequest.setProjcetId(orderInvoice.getProjectId());
            BaseResponse<InvoiceProjectByIdResponse> invoiceProjectByIdResponseBaseResponse = invoiceProjectQueryProvider.getById(invoiceProjectByIdRequest);
            InvoiceProjectByIdResponse invoiceProjectByIdResponse = invoiceProjectByIdResponseBaseResponse.getContext();
            // 普通发票
            if (orderInvoice.getInvoiceType() == InvoiceType.NORMAL) {
                CustomerDetailVO customerDetail = customerDetailQueryProvider.getCustomerDetailByCustomerId(
                        CustomerDetailByCustomerIdRequest.builder()
                                .customerId(orderInvoice.getCustomerId()).build()).getContext();
                return OrderInvoiceViewResponse.builder()
                        .invoiceType(orderInvoice.getInvoiceType())
                        .invoiceTime(orderInvoice.getInvoiceTime())
                        .invoiceTitle(orderInvoice.getInvoiceTitle())
                        .invoiceAddress(Objects.isNull(orderInvoice.getInvoiceAddress()) ?
                                trade.getConsignee().getDetailAddress(): orderInvoice.getInvoiceAddress())
                        .orderNo(orderInvoice.getOrderNo())
                        .orderPrice(trade.getTradePrice().getTotalPrice())
                        .customerName(customerDetail.getCustomerName())
                        .payOrderStatus(payOrderResponse.getPayOrderStatus())
                        .projectName(invoiceProjectByIdResponse.getProjectName())
                        .invoiceState(orderInvoice.getInvoiceState())
                        .supplierName( trade.getSupplier() != null? trade.getSupplier().getSupplierName() : null)
                        .taxNo( trade.getInvoice() != null? trade.getInvoice().getGeneralInvoice().getIdentification() : null)
                        .tradeState(trade.getTradeState())
                        .build();
            }

            // 增值税发票
            CustomerInvoiceByCustomerIdAndDelFlagRequest request = new CustomerInvoiceByCustomerIdAndDelFlagRequest();
            request.setCustomerId(orderInvoice.getCustomerId());
            BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagResponse>  baseResponse = customerInvoiceQueryProvider.getByCustomerIdAndDelFlag(request);
            CustomerInvoiceByCustomerIdAndDelFlagResponse response = baseResponse.getContext();
//            return response.map(customerInvoice -> ).orElseThrow(() -> new SbcRuntimeException("K-070008"));
            return OrderInvoiceViewResponse.builder()
                    .registerPhone(response.getCompanyPhone())
                    .bankNo(response.getBankNo())
                    .bankName(response.getBankName())
                    .registerAddress(response.getCompanyAddress())
                    .invoiceType(orderInvoice.getInvoiceType())
                    .invoiceTime(orderInvoice.getInvoiceTime())
                    .invoiceTitle(orderInvoice.getInvoiceTitle())
                    .invoiceAddress(orderInvoice.getInvoiceAddress())
                    .orderNo(orderInvoice.getOrderNo())
                    .orderPrice(trade.getTradePrice().getTotalPrice())
                    .taxNo(response.getTaxpayerNumber())
                    .customerName(response.getCompanyName())
                    .payOrderStatus(payOrderResponse.getPayOrderStatus())
                    .projectName(invoiceProjectByIdResponse.getProjectName())
                    .invoiceState(orderInvoice.getInvoiceState())
                    .supplierName( trade.getSupplier() != null ? trade.getSupplier().getSupplierName() : null)
                    .tradeState(trade.getTradeState())
                    .build();
        }).orElseThrow(() -> new SbcRuntimeException("K-070004"));
    }

    /**
     * 查询订单数据，转换成要展示的类
     * @param trade
     * @param detailResponse
     */
    private void transformTradeInfo(TradeVO trade, OrderInvoiceDetailResponse detailResponse){
        detailResponse.setOrderNo(trade.getId());
        detailResponse.setOrderPrice(trade.getTradePrice().getTotalPrice());
        detailResponse.setPayState(trade.getTradeState().getPayState().getDescription());
    }

    /**
     * 替代关联查询-模糊商家名称、模糊会员名称，以并且关系的判断
     * @param queryRequest
     * @return true:有符合条件的数据,false:没有符合条件的数据
     */
    private boolean likeCustomerAndSupplierName(final OrderInvoiceFindAllRequest queryRequest) {
        boolean supplierLike = true;
        //商家名称
        if (!StringUtils.isEmpty(queryRequest.getSupplierName()) && !StringUtils.isEmpty(queryRequest
                .getSupplierName().trim())) {
            List<Long> companyIds = companyInfoQueryProvider.listCompanyInfo(
                    CompanyListRequest.builder().supplierName(queryRequest.getSupplierName()).build()
            ).getContext().getCompanyInfoVOList().stream()
                    .map(CompanyInfoVO::getCompanyInfoId)
                    .collect(Collectors.toList());
            queryRequest.setCompanyInfoIds(companyIds);
            if(CollectionUtils.isEmpty(queryRequest.getCompanyInfoIds())){
                supplierLike = false;
            }
        }
        boolean customerLike = true;
        //模糊会员名称
        if (StringUtils.isNotBlank(queryRequest.getCustomerName()) ||  StringUtils.isNotBlank(queryRequest.getEmployeeId())) {

            List<String> customerIds = customerDetailQueryProvider.listCustomerDetailByCondition(
                CustomerDetailListByConditionRequest.builder().customerName(queryRequest.getCustomerName()).employeeId(queryRequest.getEmployeeId()).build()
            ).getContext().getCustomerDetailVOList().stream()
                    .map(CustomerDetailVO::getCustomerId).collect(Collectors.toList());

            queryRequest.setCustomerIds(customerIds);

            if (CollectionUtils.isEmpty(queryRequest.getCustomerIds())) {
                customerLike = false;
            }
        }
        return supplierLike & customerLike;
    }
}
