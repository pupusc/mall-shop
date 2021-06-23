package com.wanmi.sbc.customer.api.request.level;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 客户等级查询请求参数
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerLevelMapByCustomerIdAndStoreIdsRequest implements Serializable {

    private static final long serialVersionUID = -5023862202125296544L;

    /**
     * 客户ID
     */
    @NotNull
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @NotEmpty
    @ApiModelProperty(value = "店铺ID")
    private List<Long> storeIds;
}
