package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoDeleteStoreGetByStoreNameRequest extends CustomerBaseRequest {
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    @NotNull
    private String storeName;
}
