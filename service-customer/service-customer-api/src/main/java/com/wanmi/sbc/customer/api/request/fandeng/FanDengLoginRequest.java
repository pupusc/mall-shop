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
 * @description: 樊登免密登陆
 * @author: Mr.Tian
 * @create: 2021-01-28 11:09
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengLoginRequest implements Serializable {

    private static final long serialVersionUID = -4710836069888892691L;
    /**
     * 樊登app的token
     */
    @ApiModelProperty(value = "樊登app的token")
    @NotBlank
    private String appToken;

}
