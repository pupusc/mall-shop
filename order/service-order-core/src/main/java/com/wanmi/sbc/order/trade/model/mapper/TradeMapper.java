package com.wanmi.sbc.order.trade.model.mapper;

import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TradeMapper {


    @Mappings({})
    TradeVO providerTradeToTradeVo(ProviderTrade providerTrade);

    List<TradeVO> providerTradesToTradeVos(List<ProviderTrade> providerTrades);

    @Mappings({})
    TradeQueryRequest tradeDtoToTradeQueryRequest(TradeQueryDTO tradeQueryDTO);

    @Mappings({})
    TradeVO tradeToTradeVo(Trade trade);
}
