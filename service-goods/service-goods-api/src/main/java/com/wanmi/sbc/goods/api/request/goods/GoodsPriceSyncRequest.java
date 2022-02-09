package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsPriceSyncRequest  extends BaseQueryRequest implements Serializable{
    private static final long serialVersionUID = -7384238941913223358L;
    /**
     * 商品编码
     */
    private List<String> goodsInfoIds;

    private Long providerId;

}
