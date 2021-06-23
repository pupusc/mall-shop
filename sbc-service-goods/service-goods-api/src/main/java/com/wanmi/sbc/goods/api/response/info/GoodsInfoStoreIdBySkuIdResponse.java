package com.wanmi.sbc.goods.api.response.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * StoreId
 * Created by yangzhen on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoStoreIdBySkuIdResponse implements Serializable {

    private static final long serialVersionUID = -4108347931869624603L;

    /**
     * StoreId
     */
    @ApiModelProperty(value = "StoreId")
    private Long StoreId;
}
