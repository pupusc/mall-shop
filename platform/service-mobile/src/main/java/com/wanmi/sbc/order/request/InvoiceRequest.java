package com.wanmi.sbc.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {

    /**
     * 开发票订单列表
     */
    @ApiModelProperty("订单列表")
    @NotEmpty
    @Size(max = 30)
    private List<String> orderIds;
}
