package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 根据商品SKU编号查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoByIdRequest implements Serializable {

    private static final long serialVersionUID = 8415135684020619843L;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    @NotBlank
    private String goodsInfoId;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;
}
