package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>批量查询SKU市场价参数</p>
 * Created by of628-wenzhi on 2020-12-14-9:28 下午.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class GoodsInfoMarketingPriceByNosRequest extends GoodsBaseRequest {
    private static final long serialVersionUID = 9187246070612239373L;

    /**
     * SKU NO集合
     */
    @ApiModelProperty(value = "SKU NO集合")
    @NotEmpty
    private List<String> goodsInfoNos;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
