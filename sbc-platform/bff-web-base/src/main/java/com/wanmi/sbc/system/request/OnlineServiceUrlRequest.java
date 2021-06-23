package com.wanmi.sbc.system.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2019-12-30 17:39
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceUrlRequest {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String customerId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String customerName;
}