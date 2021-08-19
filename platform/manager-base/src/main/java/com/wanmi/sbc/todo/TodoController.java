package com.wanmi.sbc.todo;

import com.wanmi.sbc.account.api.provider.finance.record.SettlementQueryProvider;
import com.wanmi.sbc.account.api.request.finance.record.SettlementCountRequest;
import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.SettleStatus;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerCountByStateRequest;
import com.wanmi.sbc.customer.api.request.customer.InvoiceCountByStateRequest;
import com.wanmi.sbc.customer.api.response.CustomerTodoResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsUnAuditCountRequest;
import com.wanmi.sbc.order.api.provider.orderinvoice.OrderInvoiceQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.orderinvoice.OrderInvoiceCountByStateRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnCountByConditionRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCountCriteriaRequest;
import com.wanmi.sbc.order.api.response.trade.TradeCountByFlowStateResponse;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.todo.response.ReturnOrderTodoReponse;
import com.wanmi.sbc.todo.response.SupplierTodoResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunkun on 2017/9/16.
 */
@Api(tags = "TodoController", description = "待办事项服务 Api")
@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private OrderInvoiceQueryProvider orderInvoiceQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private SettlementQueryProvider settlementQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 订单todo
     *
     * @return
     */
    @ApiOperation(value = "订单todo")
    @RequestMapping(value = "trade", method = RequestMethod.GET)
    public BaseResponse<TradeCountByFlowStateResponse> TardeTodo() {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        TradeCountByFlowStateResponse tradeTodoReponse = new TradeCountByFlowStateResponse();
        TradeQueryDTO queryRequest = new TradeQueryDTO();
        if (companyInfoId != null) {
            queryRequest.setSupplierId(companyInfoId);
        }
        TradeStateDTO tradeState = new TradeStateDTO();
        //设置待审核状态
        tradeState.setFlowState(FlowState.INIT);
        queryRequest.setTradeState(tradeState);

        tradeTodoReponse.setWaitAudit(tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount());

        //设置待发货状态
        // 都未发货
        tradeState.setFlowState(FlowState.AUDIT);
        queryRequest.setTradeState(tradeState);
        Long noDeliveredCount = tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount();
        // 部分发货
        tradeState.setFlowState(FlowState.DELIVERED_PART);
        queryRequest.setTradeState(tradeState);
        Long deliveredPartCount = tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount();
        tradeTodoReponse.setWaitDeliver(noDeliveredCount + deliveredPartCount);

        //设置待付款订单
        tradeState.setFlowState(null);
        tradeState.setPayState(PayState.NOT_PAID);
        queryRequest.setTradeState(tradeState);
        tradeTodoReponse.setWaitPay(tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount());

        //设置待收货订单
        tradeState.setPayState(null);
        tradeState.setFlowState(FlowState.DELIVERED);
        queryRequest.setTradeState(tradeState);
        tradeTodoReponse.setWaitReceiving(tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount());
        return BaseResponse.success(tradeTodoReponse);
    }

    /**
     * 退单todo
     *
     * @return
     */
    @ApiOperation(value = "退单todo")
    @RequestMapping(value = "return", method = RequestMethod.GET)
    public BaseResponse<ReturnOrderTodoReponse> returnOrderTodo() {
        ReturnOrderTodoReponse returnOrderTodoReponse = new ReturnOrderTodoReponse();
        ReturnCountByConditionRequest returnQueryRequest = new ReturnCountByConditionRequest();
        returnQueryRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        returnQueryRequest.setStoreId(commonUtil.getStoreId());
        returnQueryRequest.setReturnFlowState(ReturnFlowState.INIT);
        returnOrderTodoReponse.setWaitAudit(returnOrderQueryProvider.countByCondition(returnQueryRequest).getContext().getCount());
        returnQueryRequest.setReturnFlowState(ReturnFlowState.AUDIT);
        returnOrderTodoReponse.setWaitFillLogistics(returnOrderQueryProvider.countByCondition(returnQueryRequest).getContext().getCount());
        returnQueryRequest.setReturnFlowState(ReturnFlowState.DELIVERED);
        returnOrderTodoReponse.setWaitReceiving(returnOrderQueryProvider.countByCondition(returnQueryRequest).getContext().getCount());
        returnQueryRequest.setReturnFlowState(ReturnFlowState.RECEIVED);
        returnOrderTodoReponse.setWaitRefund(returnOrderQueryProvider.countByCondition(returnQueryRequest).getContext().getCount());
        return BaseResponse.success(returnOrderTodoReponse);
    }


    /**
     * B2B用户todo
     *
     * @return
     */
    @ApiOperation(value = "B2B用户todo")
    @RequestMapping(value = "customer", method = RequestMethod.GET)
    public BaseResponse<CustomerTodoResponse> customerTodo() {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        Long storeId = commonUtil.getStoreId();
        CustomerTodoResponse customerTodoResponse = new CustomerTodoResponse();
        customerTodoResponse.setWaitAuditCustomer(customerQueryProvider.countCustomerByState(new
                CustomerCountByStateRequest(CheckState.WAIT_CHECK, DeleteFlag.NO)).getContext().getCount());
        customerTodoResponse.setWaitAuditCustomerInvoice(customerQueryProvider.countInvoiceByState(new
                InvoiceCountByStateRequest(CheckState.WAIT_CHECK)).getContext().getCount());

        OrderInvoiceCountByStateRequest countByStateRequest =
                OrderInvoiceCountByStateRequest.builder().invoiceState(InvoiceState.WAIT)
                .companyInfoId(companyInfoId).storeId(storeId).build();


        customerTodoResponse.setWaitAuditOrderInvoice(orderInvoiceQueryProvider.countByState(countByStateRequest).getContext().getCount());
        return BaseResponse.success(customerTodoResponse);
    }

    /**
     * S2B商家todo
     *
     * @return
     */
    @ApiOperation(value = "S2B商家todo")
    @RequestMapping(value = "goods", method = RequestMethod.GET)
    public BaseResponse<SupplierTodoResponse> goodsTodo() {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        Long storeId = commonUtil.getStoreId();
        SupplierTodoResponse customerTodoResponse = new SupplierTodoResponse();

        OrderInvoiceCountByStateRequest countByStateRequest =
                OrderInvoiceCountByStateRequest.builder().invoiceState(InvoiceState.WAIT)
                .companyInfoId(companyInfoId).storeId(storeId).build();


        customerTodoResponse.setWaitInvoice(orderInvoiceQueryProvider.countByState(countByStateRequest).getContext().getCount());
        //待审核商品
        if (auditQueryProvider.isBossGoodsAudit().getContext().isAudit() || auditQueryProvider.isSupplierGoodsAudit().getContext().isAudit()) {
            customerTodoResponse.setCheckGoodsFlag(true);
            customerTodoResponse.setWaitGoods(goodsQueryProvider.countUnAudit(GoodsUnAuditCountRequest.builder().companyInfoId(companyInfoId).storeId
                    (storeId).build()).getContext().getUnAuditCount());
        }
        //查询未结算
        SettlementCountRequest request = new SettlementCountRequest();
        request.setStoreId(storeId);
        request.setSettleStatus(SettleStatus.NOT_SETTLED);
        customerTodoResponse.setWaitSettle(settlementQueryProvider.count(request).getContext().getCount());
        return BaseResponse.success(customerTodoResponse);
    }
}
