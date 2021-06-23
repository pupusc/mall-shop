package com.wanmi.sbc.crm.api.request.customgroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomGroupListParamRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;
}
