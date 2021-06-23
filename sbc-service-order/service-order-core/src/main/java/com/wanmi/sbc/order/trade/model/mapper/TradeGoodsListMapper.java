package com.wanmi.sbc.order.trade.model.mapper;

import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TradeGoodsListMapper {

    @Mappings({})
    TradeGoodsListVO tradeGoodsInfoPageDTOToTradeGoodsListVO(TradeGoodsInfoPageDTO tradeGoodsInfoPageDTO);
}
