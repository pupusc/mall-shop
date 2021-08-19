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
 * @description: 发送樊登登陆注册验证码
 * @author: Mr.Tian
 * @create: 2021-01-28 11:41
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengSendCodeRequest implements Serializable {

    private static final long serialVersionUID = -4710836075205692691L;

    /**
     * 用户手机区号（不填默认为 +86）
     */
    @ApiModelProperty(value = "用户手机区号（不填默认为 +86）")
    private String areaCode;

    /**
     * 极验验证二次验证表单数据 chllenge （needGeetest为true时必传）
     */
    @ApiModelProperty(value = "极验验证二次验证表单数据 chllenge （needGeetest为true时必传）")
    private String challenge;

    /**
     * 极验验证二次验证表单数据 validate （needGeetest为true时必传）
     */
    @ApiModelProperty(value = "极验验证二次验证表单数据 validate （needGeetest为true时必传）")
    private String validate;

    /**
     * 极验验证二次验证表单数据 seccode （needGeetest为true时必传）
     */
    @ApiModelProperty(value = "极验验证二次验证表单数据 seccode （needGeetest为true时必传）")
    private String seccode;

    /**
     * 推广人ID
     */
    @ApiModelProperty(value = "推广人ID")
    private String promoId;

    /**
     * validation/check/geetest 接口返回的token
     */
    @ApiModelProperty(value = "validation/check/geetest 接口返回的token")
    @NotBlank
    private String token;
    /**
     * 用户手机号码
     */
    @ApiModelProperty(value = "用户手机号码")
    @NotBlank
    private String mobile;

}
