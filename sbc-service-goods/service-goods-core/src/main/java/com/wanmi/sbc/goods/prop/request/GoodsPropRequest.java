package com.wanmi.sbc.goods.prop.request;

import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import lombok.Data;

import java.util.List;


@Data
public class GoodsPropRequest {
    private Long lastPropId;
    private GoodsProp goodsProp;
    List<GoodsProp> goodsProps;
}
