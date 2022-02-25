package com.wanmi.sbc.returnorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAddRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderProviderTradeRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderTransferAddRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderTransferByUserIdRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderTransferDeleteRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderTransferByUserIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.dto.CompanyDTO;
import com.wanmi.sbc.order.bean.dto.ReturnItemDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.vo.ProviderTradeVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.SupplierVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeReturnVO;
import com.wanmi.sbc.order.bean.vo.TradeStateVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @menu 退单相关
 * @tag feature_d_cps
 * @status undone
 */
@RestController
@RequestMapping("/return")
@Api(tags = "ReturnOrderController", description = "mobile退单Api")
public class ReturnOrderController {

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private PayOrderQueryProvider payOrderQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    /**
     * 创建退单
     *
     * @param returnOrder
     * @return
     */
    @ApiOperation(value = "创建退单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse<String> create(@RequestBody @Valid ReturnOrderDTO returnOrder) {
//        verifyIsReturnable(returnOrder.getTid());


        //验证用户
        String userId = commonUtil.getOperatorId();
//        customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(userId)).getContext();
        if (!verifyTradeByCustomerId(returnOrder.getTid(), userId)) {
            throw new SbcRuntimeException("K-050204");
        }
        ReturnOrderVO oldReturnOrderTemp = returnOrderQueryProvider.getTransferByUserId(
                ReturnOrderTransferByUserIdRequest.builder().userId(userId).build()).getContext();
        if (oldReturnOrderTemp == null) {
            throw new SbcRuntimeException("K-050120");
        }
        ReturnOrderDTO oldReturnOrder = KsBeanUtil.convert(oldReturnOrderTemp, ReturnOrderDTO.class);

        oldReturnOrder.setReturnReason(returnOrder.getReturnReason());
        oldReturnOrder.setDescription(returnOrder.getDescription());
        oldReturnOrder.setImages(returnOrder.getImages());
        oldReturnOrder.setReturnLogistics(returnOrder.getReturnLogistics());
        oldReturnOrder.setReturnWay(returnOrder.getReturnWay());
        oldReturnOrder.setReturnPrice(returnOrder.getReturnPrice());
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(returnOrder.getTid()).build()).getContext().getTradeVO();
        oldReturnOrder.setCompany(CompanyDTO.builder().companyInfoId(trade.getSupplier().getSupplierId())
                .companyCode(trade.getSupplier().getSupplierCode()).supplierName(trade.getSupplier().getSupplierName())
                .storeId(trade.getSupplier().getStoreId()).storeName(trade.getSupplier().getStoreName()).companyType(trade.getSupplier().getIsSelf() ? BoolFlag.NO : BoolFlag.YES).build());
        //
        oldReturnOrder.setChannelType(trade.getChannelType());
        oldReturnOrder.setDistributorId(trade.getDistributorId());
        oldReturnOrder.setInviteeId(trade.getInviteeId());
        oldReturnOrder.setShopName(trade.getShopName());
        oldReturnOrder.setDistributorName(trade.getDistributorName());
        oldReturnOrder.setDistributeItems(trade.getDistributeItems());
        oldReturnOrder.setReturnGift(returnOrder.getReturnGift());
        oldReturnOrder.setTerminalSource(commonUtil.getTerminal());
        String rid = returnOrderProvider.add(ReturnOrderAddRequest.builder().returnOrder(oldReturnOrder)
                .operator(commonUtil.getOperator()).build()).getContext().getReturnOrderId();
        returnOrderProvider.deleteTransfer(ReturnOrderTransferDeleteRequest.builder().userId(userId).build());
        return BaseResponse.success(rid);
    }


    /**
     * @description 创建退款单
     * @menu 退单相关
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "创建退款单")
    @RequestMapping(value = "/addRefund", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse<String> createRefund(@RequestBody @Valid ReturnOrderDTO returnOrder) {
        if (StringUtils.isBlank(returnOrder.getDescription())) {
            throw new SbcRuntimeException("K-050454");
        }
        if (returnOrder.getDescription().length() > 100) {
            throw new SbcRuntimeException("K-050453"); //描述必须的大雨100字
        }

        if (returnOrder.getReturnReason() != ReturnReason.PRICE_DELIVERY) {
            List<ReturnItemDTO> returnItemDTOList =
                    returnOrder.getReturnItems().stream().filter(item -> item.getNum() <= 0).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(returnItemDTOList)) {
                throw new SbcRuntimeException("K-000009"); //参数错误
            }
        }

        //验证订单必须的是支付完成
        BaseResponse<FindPayOrderResponse> response =
                payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(returnOrder.getTid()).build());
        FindPayOrderResponse payOrderResponse = response.getContext();
        if (Objects.isNull(payOrderResponse) || Objects.isNull(payOrderResponse.getPayOrderStatus()) || payOrderResponse.getPayOrderStatus() != PayOrderStatus.PAYED) {
            throw new SbcRuntimeException("K-050103");
        }

        TradeGetByIdRequest tradeGetByIdRequest = new TradeGetByIdRequest();
        tradeGetByIdRequest.setTid(returnOrder.getTid()); //orderId
        BaseResponse<TradeGetByIdResponse> tradeGetByIdResponseBaseResponse = tradeQueryProvider.getById(tradeGetByIdRequest);
        TradeGetByIdResponse context = tradeGetByIdResponseBaseResponse.getContext();
        //如果订单中的信息为空，则直接返回参数错误
        if (context == null || context.getTradeVO() == null || org.springframework.util.StringUtils.isEmpty(context.getTradeVO().getId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }


        TradeVO trade = context.getTradeVO();
        //判断申请中是否存在 已经全部退款的商品
        Map<String, ReturnItemDTO> applyReturnOrderMap =
                returnOrder.getReturnItems().stream().collect(Collectors.toMap(ReturnItemDTO::getSkuId, Function.identity()));

        for (TradeItemVO tradeItemParam : trade.getTradeItems()) {
            if (tradeItemParam.getTradeReturn() == null){
                continue;
            }
            TradeReturnVO tradeReturn = tradeItemParam.getTradeReturn();
            if (tradeReturn.getReturnCompleteNum() == tradeItemParam.getNum().intValue()) {
                ReturnItemDTO returnItemDTO = applyReturnOrderMap.get(tradeItemParam.getSkuId());
                if (returnItemDTO != null) {
                    throw new SbcRuntimeException("K-050211");
                }
            }
        }

        //查看订单信息和用户信息是否一致
        if (!trade.getBuyer().getId().equals(commonUtil.getOperatorId())) {
            throw new SbcRuntimeException("K-050204");
        }

        //如果为部分发货，则提示联系客服，当前系统不支持部分发货退款退货
        if (Objects.equals(trade.getTradeState().getDeliverStatus(), DeliverStatus.PART_SHIPPED)) {
            throw new SbcRuntimeException("K-050452");
        }

        //查看是否可以申请退款 1表示可以，0表示不可以申请
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        if (config.getStatus() == 0) {
            throw new SbcRuntimeException("K-050208");
        }

        if (!trade.getCycleBuyFlag()) {
            if (trade.getTradeState().getFlowState().equals(FlowState.COMPLETED)) {
                JSONObject content = JSON.parseObject(config.getContext());
                Integer day = content.getObject("day", Integer.class);
                //必须的有完成时间
                if (Objects.isNull(trade.getTradeState().getEndTime())) {
                    throw new SbcRuntimeException("K-050002");
                }
                if (trade.getTradeState().getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < LocalDateTime.now().minusDays(day).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) {
                    throw new SbcRuntimeException("K-050208");
                }
            }
        }

        //获取商家信息
        SupplierVO supplier = context.getTradeVO().getSupplier();
        if (supplier == null) {
            throw new SbcRuntimeException("K-110102");
        }
        CompanyDTO company = new CompanyDTO();
        company.setCompanyInfoId(supplier.getSupplierId());
        company.setSupplierName(supplier.getSupplierName());
        company.setCompanyCode(supplier.getSupplierCode());
        company.setAccountName("");
        company.setStoreId(supplier.getStoreId());
        company.setStoreName(supplier.getStoreName());
        company.setCompanyType(supplier.getIsSelf() ? BoolFlag.NO : BoolFlag.YES);
        returnOrder.setCompany(company);

        returnOrder.setChannelType(trade.getChannelType());
        returnOrder.setDistributorId(trade.getDistributorId());
        returnOrder.setInviteeId(trade.getInviteeId());
        returnOrder.setShopName(trade.getShopName());
        returnOrder.setDistributorName(trade.getDistributorName());
        returnOrder.setDistributeItems(trade.getDistributeItems());
        returnOrder.setTerminalSource(commonUtil.getTerminal());
//        returnOrder.setFanDengUserNo(fanDengUserNo);

        ReturnOrderAddRequest returnOrderAddRequest = new ReturnOrderAddRequest();
        returnOrderAddRequest.setReturnOrder(returnOrder);
        returnOrderAddRequest.setOperator(commonUtil.getOperator());
        String returnOrderId = returnOrderProvider.add(returnOrderAddRequest).getContext().getReturnOrderId();
        return BaseResponse.success(returnOrderId);
    }

    /**
     * 创建退单快照
     *
     * @param returnOrder
     * @return
     */
    @ApiOperation(value = "创建退单快照")
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse transfer(@RequestBody @Valid ReturnOrderDTO returnOrder) {
        BaseResponse<FindPayOrderResponse> response =
                payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(returnOrder.getTid()).build());

        FindPayOrderResponse payOrderResponse = response.getContext();
        if (Objects.isNull(payOrderResponse) || Objects.isNull(payOrderResponse.getPayOrderStatus()) || payOrderResponse.getPayOrderStatus() != PayOrderStatus.PAYED) {
            throw new SbcRuntimeException("K-050103");
        }
        verifyIsReturnable(returnOrder.getTid());
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (commonUtil.getOperatorId())).getContext();
        if (!verifyTradeByCustomerId(returnOrder.getTid(), commonUtil.getOperatorId())) {
            throw new SbcRuntimeException("K-050204");
        }
        returnOrderProvider.addTransfer(ReturnOrderTransferAddRequest.builder().returnOrder(returnOrder)
                .operator(commonUtil.getOperator()).build());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 校验是否可退
     *
     * @param tid
     */
    private void verifyIsReturnable(String tid) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        if (Objects.nonNull(trade.getTradeState().getDeliverStatus()) && (trade.getTradeState().getDeliverStatus() == DeliverStatus.SHIPPED || trade.getTradeState().getDeliverStatus() == DeliverStatus.PART_SHIPPED)) {
            TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
            request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
            TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
            if (config.getStatus() == 0) {
                throw new SbcRuntimeException("K-050208");
            }
            JSONObject content = JSON.parseObject(config.getContext());
            Integer day = content.getObject("day", Integer.class);

            if (!trade.getCycleBuyFlag()) {
                if (Objects.isNull(trade.getTradeState().getEndTime())) {
                    throw new SbcRuntimeException("K-050002");
                }
                if (trade.getTradeState().getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < LocalDateTime.now().minusDays(day).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) {
                    throw new SbcRuntimeException("K-050208");
                }
            }
        }
    }

    /**
     * 查询退单快照
     *
     * @return
     */
    @ApiOperation(value = "查询退单快照")
    @RequestMapping(value = "/findTransfer", method = RequestMethod.GET)
    public BaseResponse<ReturnOrderTransferByUserIdResponse> transfer() {
        ReturnOrderTransferByUserIdResponse response = returnOrderQueryProvider.getTransferByUserId(
                ReturnOrderTransferByUserIdRequest.builder()
                        .userId(commonUtil.getOperatorId()).build()).getContext();
        if (Objects.nonNull(response)
                && Objects.nonNull(response.getCompany())) {
            if(response.getTradeVO()!=null && CollectionUtils.isNotEmpty(response.getTradeVO().getTradeItems())){
                response.getTradeVO().getTradeItems().forEach(tradeItemVO -> {
                    String skuId = tradeItemVO.getSkuId();
                    if(StringUtils.isNotBlank(skuId)){
                        GoodsInfoByIdResponse providerGoodsInfoVo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(skuId).build()).getContext();
                        tradeItemVO.setProviderId(providerGoodsInfoVo.getProviderId());
                    }
                });
            }
        }
        return BaseResponse.success(response);
    }

    private boolean verifyTradeByCustomerId(String tid, String customerId) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        return trade.getBuyer().getId().equals(customerId);
    }


    /**
     * 是否可创建退单
     *
     * @return
     */
    @ApiOperation(value = "是否可创建退单")
    @RequestMapping(value = "/returnable/{tid}", method = RequestMethod.GET)
    public BaseResponse isReturnable(@PathVariable String tid) {
        verifyIsReturnable(tid);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取退单详列表信息
     * @param request
     * @return
     */
    @PostMapping("/list-return-provider-trade")
    public BaseResponse findReturnOrderInfo(@RequestBody @Validated ReturnOrderProviderTradeRequest request) {
        return returnOrderProvider.listReturnProviderTrade(request);
    }

}
