package com.wanmi.sbc.goods.api.request.ares;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispatcherFunctionRequest implements Serializable {

    private static final long serialVersionUID = 15282607304188833L;

    /**
     * 方法类型
     */
    @ApiModelProperty(value = "方法类型", required = true)
    @NotBlank
    private String funcType;

    /**
     * 对应的参数Bean
     */
    @ApiModelProperty(value = "对应的参数Bean", required = true)
    @NotNull
    private Object[] objs;

}
