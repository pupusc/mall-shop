package com.wanmi.sbc.customer.api.request.follow;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * 店铺收藏分页条件
 * Created by daiyitian on 2017/11/6.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreCustomerFollowPageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 831104932991964706L;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @NotBlank
    private String customerId;

    @ApiModelProperty(value = "门店id")
    private Long storeId;

}
