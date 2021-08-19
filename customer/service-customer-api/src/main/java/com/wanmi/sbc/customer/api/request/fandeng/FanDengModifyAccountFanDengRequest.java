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
public class FanDengModifyAccountFanDengRequest implements Serializable {

    private static final long serialVersionUID = -4876886478955692691L;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;



}
