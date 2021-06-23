package com.wanmi.sbc.goods.util.mapper;

import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.request.GoodsInfoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoodsInfoMapper {

    @Mappings({})
    GoodsInfoRequest goodsInfoViewByIdsRequestToGoodsInfoRequest(GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest);


    @Mappings({})
    GoodsInfoVO goodsInfoToGoodsInfoVO(GoodsInfo goodsInfo);

    List<GoodsInfoVO> goodsInfosToGoodsInfoVOs(List<GoodsInfo> goodsInfo);
}
