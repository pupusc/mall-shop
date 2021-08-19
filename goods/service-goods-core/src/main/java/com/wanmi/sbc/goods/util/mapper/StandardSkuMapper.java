package com.wanmi.sbc.goods.util.mapper;

import com.wanmi.sbc.goods.bean.vo.StandardSkuVO;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StandardSkuMapper {

    @Mappings({})
    List<StandardSkuVO> listToVO(List<StandardSku> sku);
}
