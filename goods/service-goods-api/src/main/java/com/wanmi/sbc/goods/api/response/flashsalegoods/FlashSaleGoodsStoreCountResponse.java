package com.wanmi.sbc.goods.api.response.flashsalegoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>秒杀设置参与商家数量</p>
 * @author yxz
 * @date 2019-06-11 14:54:31
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsStoreCountResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀设置参与商家数量
     */
    @ApiModelProperty(value = "秒杀设置参与商家数量")
    private Long storeCount;
}
