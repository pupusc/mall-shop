package com.wanmi.sbc.goods.api.request.distributor.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商家-社交分销开关，更新对应的分销员商品状态对象
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:22
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorGoodsInfoModifyByStoreIdAndStatusRequest implements Serializable {

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    @NotNull
    private Long storeId;

    /**
     * 是否删除,0：否，1：是
     */
    @ApiModelProperty(value = "是否删除,0：否，1：是")
    @NotNull
    private Integer status;
}
