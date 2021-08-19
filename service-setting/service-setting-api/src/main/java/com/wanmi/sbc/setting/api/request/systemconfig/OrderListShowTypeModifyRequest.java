package com.wanmi.sbc.setting.api.request.systemconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by dyt on 2019/11/6.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListShowTypeModifyRequest {

    /**
     * 状态
     */
    @NotNull
    @ApiModelProperty(value="状态")
    private Integer status;

}
