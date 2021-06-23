package com.wanmi.sbc.order.api.request.linkedmall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 查询所有退货原因请求结构
 * Created by dyt on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class LinkedMallReturnOrderReasonRequest implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    @NotBlank
    private String rid;
}
