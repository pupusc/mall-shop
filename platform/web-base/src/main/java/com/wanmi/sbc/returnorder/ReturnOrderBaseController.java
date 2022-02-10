package com.wanmi.sbc.returnorder;

import com.aliyuncs.linkedmall.model.v20180116.InitApplyRefundResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcInitApplyRefundRequest;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.request.refund.RefundOrderResponseByReturnOrderCodeRequest;
import com.wanmi.sbc.order.api.request.returnorder.CanReturnItemNumByTidRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByIdRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderCancelRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderDeliverRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderListByTidRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderNotVoidByTidRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderPageRequest;
import com.wanmi.sbc.order.api.response.refund.RefundOrderListReponse;
import com.wanmi.sbc.order.bean.dto.ReturnLogisticsDTO;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.bean.vo.ReturnItemVO;
import com.wanmi.sbc.order.bean.vo.ReturnLogisticsVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @menu 退单相关
 * @tag feature_d_cps
 * @status undone
 */
@Api(tags = "ReturnOrderBaseController", description = "退单基本服务API")
@RestController
@RequestMapping("/return")
public class ReturnOrderBaseController {

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private RefundOrderQueryProvider refundOrderQueryProvider;

    @Autowired
    private LinkedMallReturnOrderQueryProvider linkedMallReturnOrderQueryProvider;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * 分页查询 from ES
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询 from ES")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<ReturnOrderVO>> page(@RequestBody ReturnOrderPageRequest request) {

        request.setBuyerId(commonUtil.getOperatorId());
        request.setInviteeId(request.getInviteeId());
        request.setChannelType(request.getChannelType());
        MicroServicePage<ReturnOrderVO> returnPage =
                returnOrderQueryProvider.page(request).getContext().getReturnOrderPage();
        return BaseResponse.success(returnPage);
    }


    /**
     * @description H5查看退单详情
     * @menu 退单相关
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "查看退单详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/{rid}")
    public BaseResponse<ReturnOrderVO> findById(@PathVariable String rid) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build())
                .getContext();
        checkUnauthorized(rid, returnOrder);
        return BaseResponse.success(returnOrder);
    }

    /**
     * 查看退单附件
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "查看退单附件")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/images/{rid}", method = RequestMethod.GET)
    public BaseResponse<List<String>> images(@PathVariable String rid) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build())
                .getContext();
        checkUnauthorized(rid, returnOrder);
        return BaseResponse.success(returnOrder.getImages());
    }

    /**
     * 查看退单商品清单
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "查看退单商品清单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/returnItems/{rid}", method = RequestMethod.GET)
    public BaseResponse<List<ReturnItemVO>> returnItems(@PathVariable String rid) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build())
                .getContext();
        checkUnauthorized(rid, returnOrder);
        return BaseResponse.success(returnOrder.getReturnItems());
    }

    /**
     * 查询退款物流
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "查询退款物流")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/returnLogistics/{rid}", method = RequestMethod.GET)
    public BaseResponse<ReturnLogisticsVO> returnLogistics(@PathVariable String rid) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build())
                .getContext();
        checkUnauthorized(rid, returnOrder);
        return BaseResponse.success(returnOrder.getReturnLogistics());
    }

    /**
     * 查询退单退款记录
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "查询退单退款记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/refundOrder/{rid}", method = RequestMethod.GET)
    public BaseResponse<RefundOrderListReponse> refundOrder(@PathVariable String rid) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build())
                .getContext();
        checkUnauthorized(rid, returnOrder);
        return refundOrderQueryProvider.getRefundOrderRespByReturnOrderCode(new RefundOrderResponseByReturnOrderCodeRequest(rid));

    }


    /**
     * 查找所有退货方式
     *
     * @return
     */
    @ApiOperation(value = "查找所有退货方式")
    @RequestMapping(value = "/ways", method = RequestMethod.GET)
    public BaseResponse<List<ReturnWay>> findReturnWay() {
        return BaseResponse.success(returnOrderQueryProvider.listReturnWay().getContext().getReturnWayList());
    }

    /**
     * 所有退货原因
     *
     * @return
     */
    @ApiOperation(value = "所有退货原因")
    @RequestMapping(value = "/reasons", method = RequestMethod.GET)
    public BaseResponse<List<ReturnReason>> findReturnReason() {
        return BaseResponse.success(returnOrderQueryProvider.listReturnReason().getContext().getReturnReasonList());
    }


    @ApiOperation(value = "linkedmall订单逆向渲染接口")
    @GetMapping("/findLinkedMallInitApplyRefundData")
    public BaseResponse<InitApplyRefundResponse.InitApplyRefundData> findLinkedMallInitApplyRefundData(SbcInitApplyRefundRequest sbcInitApplyRefundRequest) {
        return linkedMallReturnOrderQueryProvider.initApplyRefund(sbcInitApplyRefundRequest);
    }



    /**
     * @description 查看退货订单详情和可退商品数
     * @menu 退单相关
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "查看退货订单详情和可退商品数")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单Id", required = true)
    @RequestMapping(value = "/trade/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeVO> tradeDetails(@PathVariable String tid, @RequestParam("replace") Integer replace) {
        TradeVO trade = returnOrderQueryProvider.queryCanReturnItemNumByTid(CanReturnItemNumByTidRequest.builder()
                .tid(tid).replace(replace).build()).getContext();
        if (!trade.getBuyer().getId().equals(commonUtil.getOperatorId())) {
            throw new SbcRuntimeException("K-050100", new Object[]{tid});
        }
        // 过滤可退的中的虚拟商品
        List<TradeItemVO> virtualItem = trade.getTradeItems().stream()
                .filter(i -> GoodsType.VIRTUAL_COUPON.equals(i.getGoodsType())
                        || GoodsType.VIRTUAL_GOODS.equals(i.getGoodsType() ))
                .collect(Collectors.toList());
        trade.getTradeItems().removeAll(virtualItem);
        if(CollectionUtils.isNotEmpty(trade.getGifts()) && CollectionUtils.isNotEmpty(virtualItem)) {
            // 过滤虚拟商品的赠品
            if( !trade.getCycleBuyFlag() &&  CollectionUtils.isNotEmpty(trade.getTradeMarketings())) {
                List<Long> marketingIds = virtualItem.stream()
                        .flatMap(i -> i.getMarketingIds().stream()).distinct().collect(Collectors.toList());
                List<Long> giftMarketingIds = marketingIds.stream().filter(i -> {
                    for (TradeMarketingVO tradeMarketing : trade.getTradeMarketings()) {
                        if (tradeMarketing.getMarketingId().equals(i)
                                && MarketingType.GIFT.equals(tradeMarketing.getMarketingType())) {
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toList());
                List<TradeItemVO> giftsItem = trade.getGifts().stream()
                        .filter(i -> giftMarketingIds.contains(i.getMarketingIds().get(NumberUtils.INTEGER_ZERO)))
                        .collect(Collectors.toList());
                trade.getGifts().removeAll(giftsItem);
            }
        }
        return BaseResponse.success(trade);
    }

    /**
     * 根据订单id查询已完成的退单
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "根据订单id查询已完成的退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单Id", required = true)
    @RequestMapping(value = "/findCompletedByTid/{tid}", method = RequestMethod.GET)
    public BaseResponse<List<ReturnOrderVO>> findCompletedByTid(@PathVariable String tid) {
        String customerId = commonUtil.getOperatorId();
        List<ReturnOrderVO> returnOrders = returnOrderQueryProvider.listNotVoidByTid(ReturnOrderNotVoidByTidRequest
                .builder().tid(tid).build()).getContext().getReturnOrderList();
        if (returnOrders.stream().anyMatch(r -> !r.getBuyer().getId().equals(customerId))) {
            throw new SbcRuntimeException("K-050003");
        }
        return BaseResponse.success(returnOrders);
    }

    /**
     * 填写物流信息
     *
     * @param rid
     * @param logistics
     * @return
     */
    @ApiOperation(value = "填写物流信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/deliver/{rid}", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse deliver(@PathVariable String rid, @RequestBody ReturnLogisticsDTO logistics) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build())
                .getContext();
        checkUnauthorized(rid, returnOrder);
        return returnOrderProvider.deliver(ReturnOrderDeliverRequest.builder().rid(rid).logistics(logistics)
                .operator(commonUtil.getOperator()).build());
    }


    /**
     * 取消退单
     *
     * @param rid
     * @param reason
     * @return
     */
    @ApiOperation(value = "取消退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/cancel/{rid}", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse cancel(@PathVariable String rid, @RequestParam("reason") String reason) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build())
                .getContext();
        checkUnauthorized(rid, returnOrder);
        if (returnOrder.getReturnFlowState() != ReturnFlowState.INIT) {
            throw new SbcRuntimeException("K-050102");
        }
        return returnOrderProvider.cancel(ReturnOrderCancelRequest.builder().rid(rid).remark(reason)
                .operator(commonUtil.getOperator()).build());
    }

    /**
     * 根据订单id查询退单(过滤拒绝退款、拒绝收货、已作废)
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "根据订单id查询退单(过滤拒绝退款、拒绝收货、已作废)")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单Id", required = true)
    @RequestMapping(value = "/findByTid/{tid}", method = RequestMethod.GET)
    public BaseResponse<List<ReturnOrderVO>> findByTid(@PathVariable String tid) {
        String customerId = commonUtil.getOperatorId();
        List<ReturnOrderVO> returnOrders = returnOrderQueryProvider.listByTid(ReturnOrderListByTidRequest.builder()
                .tid(tid).build()).getContext().getReturnOrderList();
        if (returnOrders.stream().anyMatch(r -> !r.getBuyer().getId().equals(customerId))) {
            throw new SbcRuntimeException("K-050003");
        }
        List<ReturnOrderVO> returnOrderVOList = returnOrders.stream().filter(o -> o.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                && o.getReturnFlowState() != ReturnFlowState.REJECT_RECEIVE && o.getReturnFlowState() != ReturnFlowState.VOID)
                .collect(Collectors.toList());
        //如果是拆分的退单，有部分完成退款，则退款完成的退单也过滤掉（避免拆分的退单中有的被驳回，有的是退款完成，导致前端不能再次申请退款）
        if (CollectionUtils.isNotEmpty(returnOrders) && StringUtils.isNotBlank(returnOrders.get(0).getPtid())){
            List<ReturnOrderVO> completedReturnOrderList
                    = returnOrders.stream().filter(o -> o.getReturnFlowState() == ReturnFlowState.COMPLETED).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(completedReturnOrderList) && completedReturnOrderList.size() < returnOrders.size()){
                returnOrderVOList = returnOrderVOList.stream().filter(o -> o.getReturnFlowState() != ReturnFlowState.COMPLETED).collect(Collectors.toList());
            }
        }
        return BaseResponse.success(returnOrderVOList);
    }

    private void checkUnauthorized(@PathVariable String rid, ReturnOrderVO returnOrder) {
        if (!returnOrder.getBuyer().getId().equals(commonUtil.getOperatorId())) {
            throw new SbcRuntimeException("K-050003");
        }
    }

}
