package com.wanmi.sbc.goods.api.request.storegoodstab;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:32 2018/12/13
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreGoodsTabListByStoreIdRequest implements Serializable {

    private static final long serialVersionUID = 4415781929640887338L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

}
