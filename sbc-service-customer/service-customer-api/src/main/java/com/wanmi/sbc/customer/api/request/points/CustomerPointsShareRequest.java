package com.wanmi.sbc.customer.api.request.points;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Api
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPointsShareRequest implements Serializable {

    private static final long serialVersionUID = 520550848299061894L;

    /**
     * 分享人id
     */
    @NotBlank
    @ApiModelProperty(value = "分享人id")
    public String customerId;

    /**
     * 分享id
     */
    @ApiModelProperty(value = "分享id")
    public String shareId;
}
