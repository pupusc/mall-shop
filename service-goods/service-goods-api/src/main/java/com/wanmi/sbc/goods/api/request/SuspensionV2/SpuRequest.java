package com.wanmi.sbc.goods.api.request.SuspensionV2;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>redis</p>
 * @author chenzhen
 * @date 2023-02-4
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpuRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "type")
    private String spu_id;

    @ApiModelProperty(value = "type")
    private String sku_id;

    @ApiModelProperty(value = "type")
    private String isbn;

}
