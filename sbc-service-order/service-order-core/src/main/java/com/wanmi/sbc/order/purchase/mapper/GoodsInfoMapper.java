package com.wanmi.sbc.order.purchase.mapper;

import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoodsInfoMapper {

    @Mappings({})
    GoodsInfoDTO goodsInfoVOToGoodsInfoDTO(GoodsInfoVO goodsInfoVO);

    List<GoodsInfoDTO> goodsInfoVOsToGoodsInfoDTOs(List<GoodsInfoVO> goodsInfoVO);


}