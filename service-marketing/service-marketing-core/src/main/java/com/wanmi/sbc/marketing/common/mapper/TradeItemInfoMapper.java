package com.wanmi.sbc.marketing.common.mapper;

import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.common.request.TradeItemInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TradeItemInfoMapper {

    @Mappings({})
    TradeItemInfo tradeItemInfoDTOToTradeItemInfo(TradeItemInfoDTO tradeItemInfoDTO);

    List<TradeItemInfo> tradeItemInfoDTOsToTradeItemInfos(List<TradeItemInfoDTO> tradeItemInfoDTO);
}
