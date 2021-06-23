package com.wanmi.sbc.customer.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:57 2019/6/26
 * @Description:
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NormalDistributorLevelNameUpdateRequest {

    /**
     * 分销员等级名称
     */
    @ApiModelProperty(value = "分销员等级名称")
    @NotNull
    private String distributorLevelName;

}
