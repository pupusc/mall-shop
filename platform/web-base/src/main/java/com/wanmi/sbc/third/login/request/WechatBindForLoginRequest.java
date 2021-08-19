package com.wanmi.sbc.third.login.request;

import com.wanmi.sbc.third.login.TerminalStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel
@Data
public class WechatBindForLoginRequest {

    /**
     *  验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verifyCode;

    /**
     *   手机号
     */
    @ApiModelProperty(value = "手机号")
    @NotBlank
    @Pattern(regexp = "^134[0-8]\\d{7}$|^13[^4]\\d{8}$|^14[5-9]\\d{8}$|^15[^4]\\d{8}$|^16[6]\\d{8}$|^17[0-8]\\d{8}$|^18[\\d]{9}$|^19[1,5,8,9]\\d{8}$")
    private String phone;

    /**
     *  id（微信id）
     */
    @ApiModelProperty(value = "id（微信id）")
    @NotBlank
    private String id;

    /**
     * 邀请人id
     */
    @ApiModelProperty(value = "邀请人id")
    private String inviteeId;

    @ApiModelProperty(value = "分享人用户id")
    private String shareUserId;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;


    /**
     * 类型终端
     */
    @ApiModelProperty(value = "类型终端")
    @NotNull
    private TerminalStringType channel;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}
