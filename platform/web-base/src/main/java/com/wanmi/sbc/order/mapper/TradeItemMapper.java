package com.wanmi.sbc.order.mapper;

import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.request.TradeItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TradeItemMapper {

    @Mappings({})
    TradeItemInfoDTO tradeItemVOToTradeItemInfoDTO(TradeItemVO tradeItemVO);

    @Mappings({})
    TradeItemVO tradeItemDTOToTradeItemVo(TradeItemDTO tradeItemDTO);

    @Mappings({})
    GoodsRestrictedValidateVO tradeItemVOToGoodsRestrictedValidateVO(TradeItemVO tradeItemVO);

    @Mappings({})
    TradeItemDTO tradeItemRequestToTradeItemDTO(TradeItemRequest tradeItemRequest);

    @Mappings({})
    TradeItemDTO tradeItemVOToTradeItemDTO(TradeItemVO tradeItemVO);

    @Mappings({})
    GoodsRestrictedValidateVO tradeItemRequestToGoodsRestrictedValidateVO(TradeItemRequest tradeItemRequest);

    List<TradeItemInfoDTO> tradeItemVOsToTradeItemInfoDTOs(List<TradeItemVO> tradeItemVOs);


    List<TradeItemVO> tradeItemDTOsToTradeItemVos(List<TradeItemDTO> tradeItemDTOs);


    List<GoodsRestrictedValidateVO> tradeItemVOsToGoodsRestrictedValidateVOs(List<TradeItemVO> tradeItemVO);

    List<TradeItemDTO> tradeItemRequestsToTradeItemDTOs(List<TradeItemRequest> tradeItemRequest);


    List<TradeItemDTO> tradeItemVOsToTradeItemDTOs(List<TradeItemVO> tradeItemVO);

    List<GoodsRestrictedValidateVO> tradeItemRequestsToGoodsRestrictedValidateVOs(List<TradeItemRequest> tradeItemRequest);
}
