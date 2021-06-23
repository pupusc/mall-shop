package com.wanmi.sbc.goods.api.request.priceadjustmentrecord;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除调价记录表请求参数</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordDelByIdRequest extends GoodsBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * 调价单号
     */
    @ApiModelProperty(value = "调价单号")
    @NotNull
    private String id;
}
