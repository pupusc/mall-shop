package com.wanmi.sbc.order.trade.model.mapper;

import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TradeItemMapper {


    @Mappings({})
    TradeItem  tradeItemDTOToTradeItem(TradeItemDTO tradeItemDTO);

    @Mappings({})
    List<TradeItem> tradeItemDTOsToTradeItems(List<TradeItemDTO> tradeItemDTO);

    @Mappings({})
    TradeItemVO tradeItemToTradeItemVO(TradeItem TradeItem);

    List<TradeItemVO> tradeItemsToTradeItemVOs(List<TradeItem> TradeItems);
}
