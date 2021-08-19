package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsByGrouponActivityIdAndGoodsInfoIdRequest implements Serializable {
    private static final long serialVersionUID = 5794652344512329593L;

    /**
     * 拼团活动ID
     */
    @ApiModelProperty(value = "拼团活动ID")
    @NotBlank
    private String grouponActivityId;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    @NotBlank
    private String goodsInfoId;

}