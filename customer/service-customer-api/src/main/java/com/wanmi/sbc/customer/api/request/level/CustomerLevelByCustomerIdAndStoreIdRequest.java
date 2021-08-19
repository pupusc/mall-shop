package com.wanmi.sbc.customer.api.request.level;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 客户等级查询请求参数
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerLevelByCustomerIdAndStoreIdRequest implements Serializable {

    private static final long serialVersionUID = -5023862202125296544L;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @NotNull
    private String customerId;

    @ApiModelProperty(value = "店铺ID")
    @NotNull
    private Long storeId;
}
