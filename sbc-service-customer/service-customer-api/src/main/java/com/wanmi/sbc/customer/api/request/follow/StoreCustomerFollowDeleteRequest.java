package com.wanmi.sbc.customer.api.request.follow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 店铺收藏的删除条件
 * Created by daiyitian on 2017/11/6.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreCustomerFollowDeleteRequest implements Serializable {

    private static final long serialVersionUID = -2090161683343238749L;
    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @NotBlank
    private String customerId;

    /**
     * 多个店铺ID
     */
    @ApiModelProperty(value = "多个店铺ID")
    @NotEmpty
    private List<Long> storeIds;

}
