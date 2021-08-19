package com.wanmi.sbc.customer.api.request.paidcard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PaidCardErpQueryRequest {
    /**
     * spu编码
     */
    @ApiModelProperty(value = "spu编码")
    @NotBlank
    private String spuErpCode;

}
