package com.wanmi.sbc.crm.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-11
 * \* Time: 14:08
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDTO {

/*
    @ApiModelProperty(value = "省份id")
    private String provinceId;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "城市id")
    private String regionId;

    @ApiModelProperty(value = "城市名称")
    private String regionName;
*/

    @ApiModelProperty(value = "省市id")
    private String regionId;

    @ApiModelProperty(value = "省市名称")
    private String regionName;
}
