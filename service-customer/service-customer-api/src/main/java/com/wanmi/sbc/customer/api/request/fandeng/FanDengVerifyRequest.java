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
 * @description: 调用樊登实现的极验api
 * @author: Mr.Tian
 * @create: 2021-01-28 11:34
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengVerifyRequest implements Serializable {

    private static final long serialVersionUID = -4710838564205692691L;

    /**
     * 通常为对象(id，手机号等) 目前只限制传递手机号码
     */
    @ApiModelProperty(value = "通常为对象(id，手机号等) 目前只限制传递手机号码")
    @NotBlank
    private String id;


}
