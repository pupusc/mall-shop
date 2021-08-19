package com.wanmi.sbc.order.api.request.purchase;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 购物车缓存信息request
 */
@Data
@ApiModel
public class PurchaseCacheInfoRequest implements Serializable {

    private static final long serialVersionUID = 7659676429335769047L;

    /**
     * 单品ids
     */
    @NotEmpty
    private List<String> goodsInfoIds;

}
