package com.wanmi.sbc.setting.api.response.systemconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by feitingting on 2019/11/7.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsRopResponse {
    /**
     * 配置ID
     */
    @ApiModelProperty(value = "配置ID")
    private Long configId;

    /**
     * 快递100 api key
     */
    @ApiModelProperty(value = "快递100 api key")
    private String deliveryKey;

    /**
     * 客户key
     */
    @ApiModelProperty(value = "客户key")
    private String customerKey;

    /**
     * 回调地址
     */
    @ApiModelProperty(value = "回调地址")
    private String callBackUrl;

    /**
     * 状态 0:未启用1:已启用
     */
    @ApiModelProperty(value = "状态 0:未启用1:已启用")
    private Integer status;

    /**
     * 订阅推送状态 0:关闭1:开启
     */
    @ApiModelProperty(value="订阅推送状态 0:关闭1:开启")
    private Integer subscribeStatus;
}
