package com.wanmi.sbc.goods.api.request.adjustprice;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * <p>删除调价详情请求参数</p>
 * Created by of628-wenzhi on 2020-12-15-7:33 下午.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class AdjustPriceDetailDeleteRequest extends GoodsBaseRequest {
    private static final long serialVersionUID = -1678822142894757657L;

    /**
     * 调价单号
     */
    @ApiModelProperty(value = "调价单号")
    @NotBlank
    private String adjustNo;

    /**
     * 调价明细ID
     */
    @ApiModelProperty(value = "调价明细id")
    @NonNull
    private Long adjustDetailId;
}
