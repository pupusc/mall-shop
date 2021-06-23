package com.wanmi.sbc.customer.api.request.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 樊登登录去初始化用户
 * @author: Mr.Tian
 * @create: 2021-01-28 13:45
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengModifyPaidCustomerRequest implements Serializable {

    private static final long serialVersionUID = -4876836076605692691L;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;
    /**
     * 用户状态（0：未入会 1：体验中 2：体验过期 3：正式会员 4：会员过期 5：停用 ）
     */
    @ApiModelProperty(value = "用户状态（0：未入会 1：体验中 2：体验过期 3：正式会员 4：会员过期 5：停用 ） ")
    private Integer userStatus;

    /**
     * 会期开始时间
     */
    @ApiModelProperty(value = "会期开始时间 ")
    private Long vipStartTime;
    /**
     * 会期结束时间
     */
    @ApiModelProperty(value = "会期结束时间 ")
    private Long vipEndTime;
}
