package com.wanmi.sbc.goods.api.request.adjustprice;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * <p>根据改价类型分页查询市场价详情请求参数</p>
 * Created by of628-wenzhi on 2020-12-16-11:48 上午.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceAdjustmentDetailPageByTypeRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 3534143514809734370L;

    /**
     * 改价单号
     */
    @ApiModelProperty(value = "改价单号")
    @NotBlank
    private String adjustNo;

    /**
     * 改价类型
     */
    @ApiModelProperty(value = "改价类型")
    private PriceAdjustmentType type;

}
