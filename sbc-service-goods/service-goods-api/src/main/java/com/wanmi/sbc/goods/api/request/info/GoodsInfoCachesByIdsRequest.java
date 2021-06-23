package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.goods.bean.enums.PriceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoCachesByIdsRequest implements Serializable {

    private static final long serialVersionUID = -4870215527587149437L;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    @NotEmpty
    private List<String> goodsInfoIds;

    @ApiModelProperty(value = "商品类型")
    private PriceType type;
}
