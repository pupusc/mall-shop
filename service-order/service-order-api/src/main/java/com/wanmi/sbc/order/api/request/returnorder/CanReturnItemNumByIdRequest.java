package com.wanmi.sbc.order.api.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 根据退单id查询可退商品数请求结构
 * Created by jinwei on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class CanReturnItemNumByIdRequest implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    @NotBlank
    private String rid;

}
