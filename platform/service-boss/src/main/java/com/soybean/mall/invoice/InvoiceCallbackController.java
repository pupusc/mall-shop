package com.soybean.mall.invoice;


import com.soybean.mall.invoice.request.InvoiceCallbackRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.AutoUpdateInvoiceRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdsRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdsResponse;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "InvoiceCallbackController", description = "发票回调处理")
@RequestMapping("/invoice")
@RestController
@Slf4j
public class InvoiceCallbackController {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @PostMapping("/callback")
    public void callback(@Validated @RequestBody InvoiceCallbackRequest request){
        TradeGetByIdsRequest tradeGetByIdsRequest = TradeGetByIdsRequest.builder().tid(request.getOrderCodes()).build();
        TradeGetByIdsResponse byIds = tradeQueryProvider.getByIds(tradeGetByIdsRequest).getContext();
        if(CollectionUtils.isNotEmpty(byIds.getTradeVO())){
            for (TradeVO tradeVO : byIds.getTradeVO()) {
//                tradeProvider.update()
                try {
                    log.info("invoice callback: trade id:{}", tradeVO.getTradeId());
                    AutoUpdateInvoiceRequest autoUpdateInvoiceRequest = AutoUpdateInvoiceRequest.builder().tradeId(tradeVO.getTradeId()).build();
                    tradeProvider.updateInvoice(autoUpdateInvoiceRequest);
                }catch(Exception ex){
                    log.info("update fail:{}", tradeVO.getTradeId());
                }
            }
        }
    }
}
