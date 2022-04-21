package com.wanmi.sbc.order;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.CustomerInvoiceBaseController;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengInvoiceRequest;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.TradePageQueryRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.TradePriceVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.request.InvoiceRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZoneId;

@Api(tags = "InvoiceTradeController", description = "发票API")
@RestController
@RequestMapping("/trade/invoice")
@Slf4j
@Validated
public class InvoiceTradeController {

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private ExternalProvider externalProvider;



    /**
     * 可以开票的订单列表
     */
    @ApiOperation(value = "分页查询订单")
    @RequestMapping(value = "/pageOrder", method = RequestMethod.POST)
    public BaseResponse<Page<TradeVO>> pageOrder(@RequestBody TradePageQueryRequest paramRequest) {
        TradeQueryDTO tradeQueryRequest = TradeQueryDTO.builder()
                .tradeState(TradeStateDTO.builder().flowState(FlowState.COMPLETED).payState(PayState.PAID).build())
                .buyerId(commonUtil.getOperatorId())
                .invoiceType(-1)
                .build();
        tradeQueryRequest.setPageNum(paramRequest.getPageNum());
        tradeQueryRequest.setPageSize(paramRequest.getPageSize());

        MicroServicePage<TradeVO> tradePage = tradeQueryProvider.pageCriteriaOptimize(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();
        return BaseResponse.success(tradePage);
    }

    /**
     * 发起开票
     */
    @ApiOperation(value = "发起开票")
    @PostMapping(value="/submit")
    public BaseResponse<String> submitInvoice(@RequestBody InvoiceRequest request){
        TradeQueryDTO tradeQueryRequest = TradeQueryDTO.builder()
                .tradeState(TradeStateDTO.builder().flowState(FlowState.COMPLETED).payState(PayState.PAID).build())
                .ids(request.getOrderIds().toArray(new String[request.getOrderIds().size()]))
                .buyerId(commonUtil.getOperatorId())
                .invoiceType(-1)
                .build();

        MicroServicePage<TradeVO> tradePage = tradeQueryProvider.pageCriteriaOptimize(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();

        if(tradePage.getContent().size()!=request.getOrderIds().size()){
            return BaseResponse.error("开票的订单当中数据不一致");
        }
        FanDengInvoiceRequest fanDengInvoiceRequest = new FanDengInvoiceRequest();

        for (TradeVO tradeVO : tradePage.getContent()) {
            if (tradeVO.getTradeState().getEndTime() == null){
                continue;
            }
            FanDengInvoiceRequest.Item item = new FanDengInvoiceRequest.Item();
            //现金价格
            TradePriceVO tradePrice = tradeVO.getTradePrice();
            BigDecimal totalPrice = tradePrice.getTotalPrice().add(tradePrice.getDeliveryPrice());
            item.setFee(totalPrice);
            item.setCount(1);
            item.setCompleteTime(Date.from(tradeVO.getTradeState().getEndTime().atZone(ZoneId.systemDefault()).toInstant()));
            fanDengInvoiceRequest.getOrderExtendBOS().add(item);
        }


        BaseResponse<String> result = externalProvider.submitInvoiceOrder(fanDengInvoiceRequest);
        return result;
    }
}
