package com.wanmi.sbc.todo;

import com.wanmi.sbc.account.api.provider.finance.record.SettlementQueryProvider;
import com.wanmi.sbc.account.api.request.finance.record.SettlementCountRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerCountByStateRequest;
import com.wanmi.sbc.customer.api.request.customer.InvoiceCountByStateRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsUnAuditCountRequest;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.returnorder.ReturnCountByConditionRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderCountByFlowStateRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCountCriteriaRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.todo.response.BossTodoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunkun on 2017/9/16.
 */
@Api(tags="TodoController", description = "S2B平台管理员待处理事项todo")
@RestController
public class TodoController {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private SettlementQueryProvider settlementQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;


    /**
     * S2B平台管理员todo
     *
     * @return
     */
    @ApiOperation(value = "S2B平台管理员todo")
    @RequestMapping(value = "todo", method = RequestMethod.GET)
    public BaseResponse<BossTodoResponse> bossTodo() {
        BossTodoResponse bossTodoResponse = new BossTodoResponse();
        bossTodoResponse.setWaitSupplier(companyInfoQueryProvider.countCompanyByWaitCheck().getContext().getCount());
        //待审核商品
        if(auditQueryProvider.isBossGoodsAudit().getContext().isAudit() || auditQueryProvider.isSupplierGoodsAudit().getContext().isAudit()) {
            bossTodoResponse.setCheckGoodsFlag(true);
            bossTodoResponse.setWaitGoods(goodsQueryProvider.countUnAudit(GoodsUnAuditCountRequest.builder().build()).getContext().getUnAuditCount());
        }

        //待付款订单
        TradeCountCriteriaRequest tradeCountCriteriaRequest = TradeCountCriteriaRequest.builder()
                .tradePageDTO(
                        TradeQueryDTO.builder().tradeState(
                                TradeStateDTO.builder().payState(PayState.NOT_PAID).build()
                        ).build()
                ).build();
        bossTodoResponse.setWaitPay(tradeQueryProvider.countCriteria(tradeCountCriteriaRequest).getContext().getCount());

        //待退款

        ReturnOrderCountByFlowStateRequest returnQueryRequest = new ReturnOrderCountByFlowStateRequest();
        returnQueryRequest.setReturnFlowState(ReturnFlowState.RECEIVED);

        ReturnCountByConditionRequest returnCountByConditionRequest = ReturnCountByConditionRequest.builder()
                .returnFlowState(ReturnFlowState.RECEIVED).build();
        bossTodoResponse.setWaitRefund(returnOrderQueryProvider.countByCondition(returnCountByConditionRequest).getContext().getCount());

        //待审核客户
        if(auditQueryProvider.isCustomerAudit().getContext().isAudit()) {
            bossTodoResponse.setCheckCustomerFlag(true);
            bossTodoResponse.setWaitAuditCustomer(customerQueryProvider.countCustomerByState(new
                    CustomerCountByStateRequest(CheckState.WAIT_CHECK, DeleteFlag.NO)).getContext().getCount());
        }
        //待审核增票资质
        if(auditQueryProvider.isTicketAudit().getContext().isAduit()) {
            bossTodoResponse.setCheckCustomerInvoiceFlag(true);
            bossTodoResponse.setWaitAuditCustomerInvoice(customerQueryProvider.countInvoiceByState(new
                    InvoiceCountByStateRequest(CheckState.WAIT_CHECK)).getContext().getCount());
        }
        //待结算
        bossTodoResponse.setWaitSettle(settlementQueryProvider.count(new SettlementCountRequest()).getContext().getCount());
        return BaseResponse.success(bossTodoResponse);
    }

}
