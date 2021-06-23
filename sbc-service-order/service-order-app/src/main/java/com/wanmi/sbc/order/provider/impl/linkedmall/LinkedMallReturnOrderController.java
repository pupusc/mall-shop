package com.wanmi.sbc.order.provider.impl.linkedmall;

import com.aliyuncs.linkedmall.model.v20180116.QueryRefundApplicationDetailResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcQueryRefundApplicationDetailRequest;
import com.wanmi.sbc.order.api.provider.linkedmall.LinkedMallReturnOrderProvider;
import com.wanmi.sbc.order.api.request.linkedmall.LinkedMallReturnOrderApplyRequest;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.request.ReturnQueryRequest;
import com.wanmi.sbc.order.returnorder.service.LinkedMallReturnOrderService;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>linkedMall退单服务接口</p>
 */
@Slf4j
@RestController
public class LinkedMallReturnOrderController implements LinkedMallReturnOrderProvider {

    @Autowired
    private LinkedMallReturnOrderService linkedMallReturnOrderService;

    @Autowired
    private LinkedMallReturnOrderQueryProvider linkedMallReturnOrderQueryProvider;

    @Autowired
    private ReturnOrderService returnOrderService;

    @Override
    public BaseResponse apply(@RequestBody @Valid LinkedMallReturnOrderApplyRequest request){
        linkedMallReturnOrderService.apply(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse syncStatus(){
        Operator system = Operator.builder().name("system").account("system").platform(Platform.PLATFORM).build();

        //处理待审核
        ReturnQueryRequest request = new ReturnQueryRequest();
        request.setThirdPlatFormApplyFlag(true);
        request.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
        request.setReturnFlowState(ReturnFlowState.INIT);
        request.setPageNum(0);
        request.setPageSize(20000);//一批处理20000条
        request.putSort("createTime", SortType.ASC.toValue());
        List<ReturnOrder> returnOrderList = returnOrderService.findByPage(request);
        if(CollectionUtils.isNotEmpty(returnOrderList)) {
            SbcQueryRefundApplicationDetailRequest detailRequest = new SbcQueryRefundApplicationDetailRequest();
            for (ReturnOrder returnOrder : returnOrderList) {
                if (CollectionUtils.isEmpty(returnOrder.getReturnItems())
                        || StringUtils.isBlank(returnOrder.getReturnItems().get(0).getThirdPlatformSubOrderId())) {
                    continue;
                }
                detailRequest.setBizUid(returnOrder.getId());
                detailRequest.setSubLmOrderId(returnOrder.getReturnItems().get(0).getThirdPlatformSubOrderId());
                try {
                    QueryRefundApplicationDetailResponse.RefundApplicationDetail detail =
                            linkedMallReturnOrderQueryProvider.queryRefundApplicationDetail(detailRequest).getContext().getDetail();
                    if (detail == null) {
                        log.error("退单不存在, rid={}, subLmOrderId={}", returnOrder.getId(), detailRequest.getSubLmOrderId());
                        continue;
                    }
                    //由定时任务执行，考虑淘宝那边状态较快的情况下
                    //2: "卖家已经同意退款，等待买家退货"
                    //3: "买家已经退货，等待卖家确认收货"
                    //5: "退款成功"
                    if(Integer.valueOf(2).equals(detail.getDisputeStatus())
                        ||Integer.valueOf(3).equals(detail.getDisputeStatus())
                            || Integer.valueOf(5).equals(detail.getDisputeStatus())){
                        //审核通过
                        returnOrderService.audit(returnOrder.getId(), system, null);
                    }else if(Integer.valueOf(4).equals(detail.getDisputeStatus()) //4: "退款关闭"
                            ||Integer.valueOf(6).equals(detail.getDisputeStatus())) { //6: "卖家拒绝退款"
                        //驳回
                        String message = detail.getSellerRefuseReason();
                        if(StringUtils.isBlank(message)){
                            message = detail.getSellerRefuseAgreementMessage();
                        }
                        returnOrderService.cancel(returnOrder.getId(), system, message);
                    }
                } catch (Exception e) {
                    log.error("退单审核同步异常, rid=" + returnOrder.getId(), e);
                }
            }
        }

        //处理待商家收货
        request.setReturnFlowState(ReturnFlowState.DELIVERED);
        List<ReturnOrder> returnOrders = returnOrderService.findByPage(request);
        if(CollectionUtils.isNotEmpty(returnOrders)) {
            SbcQueryRefundApplicationDetailRequest detailRequest = new SbcQueryRefundApplicationDetailRequest();
            for (ReturnOrder returnOrder : returnOrders) {
                if (CollectionUtils.isEmpty(returnOrder.getReturnItems())
                        || StringUtils.isBlank(returnOrder.getReturnItems().get(0).getThirdPlatformSubOrderId())) {
                    continue;
                }
                detailRequest.setBizUid(returnOrder.getId());
                detailRequest.setSubLmOrderId(returnOrder.getReturnItems().get(0).getThirdPlatformSubOrderId());
                try {
                    QueryRefundApplicationDetailResponse.RefundApplicationDetail detail =
                            linkedMallReturnOrderQueryProvider.queryRefundApplicationDetail(detailRequest).getContext().getDetail();
                    if (detail == null) {
                        log.error("退单不存在, rid={}, subLmOrderId={}", returnOrder.getId(), detailRequest.getSubLmOrderId());
                        continue;
                    }
                    //由定时任务执行，考虑淘宝那边状态较快的情况下
                    //5: "退款成功"
                    //11: "退款结束"
                    if (Integer.valueOf(5).equals(detail.getDisputeStatus())
                            || Integer.valueOf(11).equals(detail.getDisputeStatus())) {
                        returnOrderService.receive(returnOrder.getId(), system);
                    } else if(Integer.valueOf(4).equals(detail.getDisputeStatus())){ //4: "退款关闭"
                        String message = detail.getSellerRefuseReason();
                        if(StringUtils.isBlank(message)){
                            message = detail.getSellerRefuseAgreementMessage();
                        }
                        returnOrderService.rejectReceive(returnOrder.getId(), message, system);
                    }
                } catch (Exception e) {
                    log.error("退单确认收货同步异常, rid=" + returnOrder.getId(), e);
                }
            }
        }
        return BaseResponse.SUCCESSFUL();
    }
}
