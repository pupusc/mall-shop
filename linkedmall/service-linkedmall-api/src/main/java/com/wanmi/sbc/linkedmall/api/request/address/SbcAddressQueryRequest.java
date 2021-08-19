package com.wanmi.sbc.linkedmall.api.request.address;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地址查询请求
 * User: dyt
 * Date: 2020-8-10
 * Time: 17:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SbcAddressQueryRequest {

    @ApiModelProperty(value = "客户端IP")
    private String ip;

    @ApiModelProperty(value = "配送区域ID")
    private String divisionCode;

}
