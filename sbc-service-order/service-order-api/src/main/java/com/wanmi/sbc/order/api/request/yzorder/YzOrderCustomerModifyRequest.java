package com.wanmi.sbc.order.api.request.yzorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class YzOrderCustomerModifyRequest implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "orderNo")
    private String orderNo;

    /**
     * yz_uid
     */
    @ApiModelProperty(value = "yzUid")
    private Long yzUid;

    /**
     * yz_openId
     */
    @ApiModelProperty(value = "yzOpenId")
    private String yzOpenId;

    /**
     * flag(是否补偿完成0：未开始 1：已完成)
     */
    @ApiModelProperty(value = "flag")
    private Integer flag;
}
