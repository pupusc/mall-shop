package com.wanmi.sbc.customer.api.request.follow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 店铺收藏关注数量条件
 * Created by daiyitian on 2017/11/6.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreCustomerFollowCountRequest implements Serializable {

    private static final long serialVersionUID = -2090161683343238749L;
    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @NotBlank
    private String customerId;

}
