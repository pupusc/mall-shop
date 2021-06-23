package com.wanmi.sbc.customer.api.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by feitingting on 2019/1/9.
 */
@ApiModel
@Data
public class StoreInfoSmallProgramRequest {
    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺id")
    private  Long storeId;
    /**
     * 店铺码地址
     */
    @ApiModelProperty(value = "店铺码地址")
    private  String codeUrl;
}
