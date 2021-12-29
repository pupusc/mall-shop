package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ChangeTradeProviderRequest implements Serializable {

    @NotNull
    @ApiModelProperty("pid")
    private String pid;

    @NotEmpty
    @ApiModelProperty("sku编码")
    private Map<String,String> skuNos;
}
