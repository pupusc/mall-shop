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
 * @description: 调用樊登验证码前置接口入参
 * @author: Mr.Tian
 * @create: 2021-01-28 11:29
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengPrepositionRequest implements Serializable {

    private static final long serialVersionUID = -4710836069805692691L;

    /**
     * 数美需要的deviceId
     */
    @ApiModelProperty(value = "数美需要的deviceId")
    @NotBlank
    private String deviceId;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @NotBlank
    private String mobile;

    /**
     * 手机区号
     */
    @ApiModelProperty(value = "手机区号")
    @NotBlank
    private String areaCode;


    /**
     * 推广人ID
     */
    @ApiModelProperty(value = "推广人ID")
    private String promoId;


    /**
     * 请求人ip
     */
    @ApiModelProperty(value = "请求人ip")
    private String clientIp;
}
