package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AtmosphereDeleteRequest implements Serializable {
    private static final long serialVersionUID = 4475733983149670206L;
    @NotNull
    private Integer id;
    @ApiModelProperty("skuId")
    private String skuId;
}
