package com.wanmi.sbc.customer.api.request.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 樊登积分退还入参
 * @author: Mr.Tian
 * @create: 2021-01-28 14:36
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengPointRefundRequest  implements Serializable {

    private static final long serialVersionUID = -4878436069854992695L;

    /**
     * 会员编号
     */
    @ApiModelProperty(value = "会员编号")
    @NotBlank
    private String userNo;

    /**
     * 返还积分
     */
    @ApiModelProperty(value = "返还积分")
    @NotNull
    private long point;


     /**
     * 数据来源类型 1:商城订单
     */
    @ApiModelProperty(value = "数据来源类型 1:商城订单")
    private int sourceType;
     /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    @NotBlank
    private String sourceId;

    /**
     * 变更描述
     */
    @ApiModelProperty(value = "变更描述")
    @NotBlank
    private String desc;



}
