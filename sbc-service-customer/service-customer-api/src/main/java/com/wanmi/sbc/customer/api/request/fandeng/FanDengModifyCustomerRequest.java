package com.wanmi.sbc.customer.api.request.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
public class FanDengModifyCustomerRequest implements Serializable {

    private static final long serialVersionUID = -4876836076605692691L;

    /**
     * 樊登会员id
     */
    @ApiModelProperty(value = "樊登会员id")
    private String fanDengUserNo;
    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String customerAccount;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String profilePhoto;
}
