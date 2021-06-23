package com.wanmi.sbc.goods.util.mapper;

import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.info.model.root.Goods;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoodsMapper {

    @Mappings({})
    GoodsVO goodsToGoodsVO(Goods goods);

    @Mappings({})
    List<GoodsVO> goodsListToGoodsVOList(List<Goods> goodsList);
}
