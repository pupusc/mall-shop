package com.wanmi.sbc.goods.api.request.storegoodstab;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:01 2018/12/13
 * @Description:
 */
@ApiModel
@Data
public class StoreGoodsTabDeleteRequest implements Serializable {

    private static final long serialVersionUID = -3983814990059217629L;

    /**
     * 模板标识
     */
    @ApiModelProperty(value = "模板标识")
    private Long tabId;

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    private Long storeId;

}
