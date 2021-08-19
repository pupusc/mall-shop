package com.wanmi.sbc.goods.util.mapper;

import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.info.model.root.Goods;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoodsBrandMapper {


    List<GoodsBrandVO> brandToVoList(List<GoodsBrand> goodsBrands);
}
