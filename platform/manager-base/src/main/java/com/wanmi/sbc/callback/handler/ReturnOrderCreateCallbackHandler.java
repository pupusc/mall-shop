package com.wanmi.sbc.callback.handler;
import java.time.Instant;
import com.soybean.mall.wx.mini.enums.AfterSalesReasonEnum;
import com.soybean.mall.wx.mini.enums.AfterSalesStateEnum;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse.ReturnInfo;
import com.soybean.mall.wx.mini.order.bean.request.WxDealAftersaleRequest;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.TerminalSource;


import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAddRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.bean.dto.CompanyDTO;
import com.wanmi.sbc.order.bean.dto.ReturnItemDTO;
import com.wanmi.sbc.order.bean.dto.ReturnLogisticsDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.dto.ReturnPriceDTO;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/31 5:21 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Component
public class ReturnOrderCreateCallbackHandler implements CallbackHandler {

    @Resource
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private PayOrderQueryProvider payOrderQueryProvider;

    @Autowired
    private WxOrderApiController wxOrderApiController;

    @Override
    public boolean support(String eventType) {
        return "aftersale_new_order".equals(eventType);
    }

    @Override
    public String handle(Map<String, Object> paramMap) {
        log.info("ReturnOrderCreateCallbackHandler handle --> begin");
        long beginTime = System.currentTimeMillis();
        Object returnOrderObj = paramMap.get("aftersale_info");
        if (returnOrderObj == null) {
            log.error("回调参数异常 param:{}", paramMap);
            return "fail";
        }

        Map<String, Object> returnOrderMap = (Map<String, Object>) returnOrderObj;
        String orderId = returnOrderMap.get("out_order_id").toString(); //订单号
        String aftersaleId = returnOrderMap.get("aftersale_id").toString(); //视频号 退单号

        //保证订单已经支付
        BaseResponse<FindPayOrderResponse> response =
                payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(orderId).build());
        FindPayOrderResponse payOrderResponse = response.getContext();
        if (Objects.isNull(payOrderResponse) || Objects.isNull(payOrderResponse.getPayOrderStatus()) || payOrderResponse.getPayOrderStatus() != PayOrderStatus.PAYED) {
            log.error("ReturnOrderCreateCallbackHandler handler orderId:{} aftersaleId: {} 未支付，无法申请售后", orderId, aftersaleId);
            return "fail";
        }

        //根据视频号的售后id获取 售后详细信息
        WxDealAftersaleRequest wxDealAftersaleRequest = new WxDealAftersaleRequest();
        wxDealAftersaleRequest.setAftersaleId(Long.valueOf(aftersaleId));
        BaseResponse<WxDetailAfterSaleResponse> wxDetailAfterSaleResponseBaseResponse = wxOrderApiController.detailAfterSale(wxDealAftersaleRequest);
        WxDetailAfterSaleResponse context = wxDetailAfterSaleResponseBaseResponse.getContext();
        if (context.getAfterSalesOrder() == null) {
            log.error("ReturnOrderCreateCallbackHandler handler orderId:{} aftersaleId:{} 内容为空,不能生成售后订单", orderId, aftersaleId);
            return "fail";
        }
        WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder = context.getAfterSalesOrder();
        if (AfterSalesStateEnum.getByCode(afterSalesOrder.getStatus()) != AfterSalesStateEnum.AFTER_SALES_STATE_TWO) {
            log.error("ReturnOrderCreateCallbackHandler handler orderId:{} aftersaleId:{} 非创建售后状态，return", orderId, aftersaleId);
            return "fail";
        }

        afterSalesOrder.setReturnInfo(new ReturnInfo());


        //根据订单号获取订单详细信息
        TradeVO tradeVo = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(orderId).build()).getContext().getTradeVO();

        //获取售后商品
        TradeItemVO tradeItemVO = null;
        for (TradeItemVO tradeItem : tradeVo.getTradeItems()) {
            if (Objects.equals(tradeItem.getSkuId(), afterSalesOrder.getProductInfo().getOutSkuId())) {
                tradeItemVO = tradeItem;
                break;
            }
        }

        if (tradeItemVO == null) {
            log.error("ReturnOrderCreateCallbackHandler handler orderId:{} aftersaleId:{} 返回的商品 outSpuId:{} outSkuId:{} 在订单中没有匹配到",
                    orderId, aftersaleId, afterSalesOrder.getProductInfo().getOutProductId(), afterSalesOrder.getProductInfo().getOutSkuId());
            return "fail";
        }

        BigDecimal applyReturnPrice = new BigDecimal(afterSalesOrder.getOrderamt()+"").divide(new BigDecimal("100"));
        //生成商品信息
        ReturnItemDTO returnItemDTO = this.packageTradeItem(tradeItemVO, afterSalesOrder.getProductInfo().getProductCnt().intValue(), applyReturnPrice);


        ReturnOrderDTO returnOrderDTO = new ReturnOrderDTO();
        returnOrderDTO.setTid(orderId);
        returnOrderDTO.setAftersaleId(aftersaleId);
        returnOrderDTO.setReturnReason(this.wxReturnReasonType2ReturnReason(afterSalesOrder.getRefundReasonType()));
        returnOrderDTO.setDescription(afterSalesOrder.getRefundReason() + " --- " + this.wxReturnReasonType2ReturnReasonStr(afterSalesOrder.getRefundReasonType()));
        //附件
        returnOrderDTO.setImages(afterSalesOrder.getMediaList().stream().map(WxDetailAfterSaleResponse.MediaListInfo::getUrl).collect(Collectors.toList()));

        //物流信息
        if (afterSalesOrder.getReturnInfo() != null && StringUtils.isNotBlank(afterSalesOrder.getReturnInfo().getWaybillId())) {
            ReturnLogisticsDTO returnLogisticsDTO = new ReturnLogisticsDTO();
            returnLogisticsDTO.setCompany("天天快递");
            returnLogisticsDTO.setNo(afterSalesOrder.getReturnInfo().getWaybillId());
            returnLogisticsDTO.setCode("tiantian");
            returnLogisticsDTO.setCreateTime(Instant.ofEpochSecond(afterSalesOrder.getReturnInfo().getOrderReturnTime()).atOffset(ZoneOffset.of("+08:00")).toLocalDateTime());
            returnOrderDTO.setReturnLogistics(returnLogisticsDTO);
        }

        returnOrderDTO.setReturnWay(afterSalesOrder.getType() == 1 ? ReturnWay.OTHER : ReturnWay.EXPRESS);
        returnOrderDTO.setTerminalSource(TerminalSource.MINIPROGRAM);

        ReturnPriceDTO returnPrice = new ReturnPriceDTO();
        returnPrice.setApplyPrice(applyReturnPrice);
        returnPrice.setTotalPrice(applyReturnPrice);
        returnPrice.setApplyStatus(false);
        returnOrderDTO.setReturnPrice(returnPrice);

        CompanyDTO company = new CompanyDTO();
        company.setCompanyInfoId(tradeVo.getSupplier().getSupplierId());
        company.setSupplierName(tradeVo.getSupplier().getSupplierName());
        company.setCompanyCode(tradeVo.getSupplier().getSupplierCode());
        company.setAccountName("");
        company.setStoreId(tradeVo.getSupplier().getStoreId());
        company.setStoreName(tradeVo.getSupplier().getStoreName());
        company.setCompanyType(tradeVo.getSupplier().getIsSelf() ? BoolFlag.NO : BoolFlag.YES);

        returnOrderDTO.setCompany(company);
        returnOrderDTO.setChannelType(tradeVo.getChannelType());


        Operator operator = new Operator();
        operator.setPlatform(Platform.WX_VIDEO);
        operator.setUserId(tradeVo.getBuyer().getId());
        operator.setName(tradeVo.getBuyer().getName());
        operator.setStoreId(tradeVo.getStoreId().toString());
        operator.setIp("127.0.0.1");
        operator.setAccount(tradeVo.getBuyer().getAccount());
        operator.setCompanyInfoId(tradeVo.getSupplier().getSupplierId());

        returnOrderDTO.setReturnItems(Collections.singletonList(returnItemDTO));
        String returnOrderId = returnOrderProvider.add(ReturnOrderAddRequest.builder().returnOrder(returnOrderDTO).operator(operator).build()).getContext().getReturnOrderId();
        log.info("ReturnOrderCreateCallbackHandler orderId:{} aftersaleId:{} returnOrderId:{} handle --> end cost: {} ms",
                orderId, aftersaleId, returnOrderId, System.currentTimeMillis() - beginTime);
        return "success";
    }


    /**
     * 打包tradeItem
     * @param tradeItem
     * @return
     */
    private ReturnItemDTO packageTradeItem(TradeItemVO tradeItem, Integer returnNum, BigDecimal applyReturnPrice) {
        ReturnItemDTO returnItemDTO = new ReturnItemDTO();
        returnItemDTO.setApplyRealPrice(applyReturnPrice);
        returnItemDTO.setApplyKnowledge(tradeItem.getKnowledge());
        returnItemDTO.setApplyPoint(tradeItem.getPoints());
        returnItemDTO.setBuyPoint(tradeItem.getBuyPoint());
        returnItemDTO.setCanReturnNum(tradeItem.getCanReturnNum());
        returnItemDTO.setSkuId(tradeItem.getSkuId());
        returnItemDTO.setSkuName(tradeItem.getSkuName());
        returnItemDTO.setSkuNo(tradeItem.getSkuNo());
        returnItemDTO.setSpecDetails(tradeItem.getSpecDetails());
        returnItemDTO.setPrice(tradeItem.getPrice());
        returnItemDTO.setSplitPrice(tradeItem.getSplitPrice());
        returnItemDTO.setSupplyPrice(tradeItem.getSupplyPrice());
//            returnItemDTO.setProviderPrice(tradeItem.getPro);
//            returnItemDTO.setOrderSplitPrice(tradeItem.getord);
        returnItemDTO.setNum(returnNum);
        returnItemDTO.setPic(tradeItem.getPic());
        returnItemDTO.setUnit(tradeItem.getUnit());
        returnItemDTO.setGoodsType(tradeItem.getGoodsType());


//            returnItemDTO.setThirdPlatformSpuId(tradeItem.getThirdPlatformSpuId());
//            returnItemDTO.setThirdPlatformSkuId(tradeItem.getThirdPlatformSkuId());
        returnItemDTO.setGoodsSource(tradeItem.getGoodsSource());
//            returnItemDTO.setThirdPlatformType(tradeItem.getThirdPlatformType());
//            returnItemDTO.setThirdPlatformSubOrderId(tradeItem.getThirdPlatformSubOrderId());
        returnItemDTO.setProviderId(tradeItem.getProviderId());
//            returnItemDTO.setSplitPoint(tradeItem.get);


        return returnItemDTO;
    }

    /**
     * 视频号退款原因转化
     * @param afterSalesReasonType
     * @return
     */
    private ReturnReason wxReturnReasonType2ReturnReason(Integer afterSalesReasonType) {
        AfterSalesReasonEnum byCode = AfterSalesReasonEnum.getByCode(afterSalesReasonType);
        if (AfterSalesReasonEnum.AFTERSALES_TWO == byCode) {
            return ReturnReason.WRONGGOODS;
        } else if (AfterSalesReasonEnum.AFTERSALES_ONE == byCode) {
            return ReturnReason.ERRORGOODS;
        } else {
            return ReturnReason.OTHER;
        }
    }

    private String wxReturnReasonType2ReturnReasonStr(Integer afterSalesReasonType){
        AfterSalesReasonEnum byCode = AfterSalesReasonEnum.getByCode(afterSalesReasonType);
        if (byCode == null) {
            return "未知原因";
        }
        return byCode.getMessage();
    }
}
