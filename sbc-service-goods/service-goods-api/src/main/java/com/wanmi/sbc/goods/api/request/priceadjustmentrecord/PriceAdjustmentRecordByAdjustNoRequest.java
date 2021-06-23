package com.wanmi.sbc.goods.api.request.priceadjustmentrecord;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个查询调价记录表请求参数</p>
 * @author
 * @date 2020-12-09 19:57:21
 */

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordByAdjustNoRequest extends BaseRequest {

    private static final long serialVersionUID = 5521321277291020101L;

    /**
     * 调价单号
     */
    @ApiModelProperty(value = "调价单号")
    @NotNull
    private String adjustNo;
}
