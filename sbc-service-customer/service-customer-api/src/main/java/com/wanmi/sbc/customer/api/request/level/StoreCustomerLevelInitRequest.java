package com.wanmi.sbc.customer.api.request.level;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 店铺等级初始化请求参数
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreCustomerLevelInitRequest implements Serializable {

    private static final long serialVersionUID = -5023862202125296544L;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    @NotNull
    private Long storeId;

}
