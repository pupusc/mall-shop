package com.wanmi.sbc.marketing.api.request.plugin;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-27
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingPluginGetCustomerLevelsByStoreIdsRequest implements Serializable {

    private static final long serialVersionUID = 321112870062604510L;

    @ApiModelProperty(value = "店铺id列表")
    @NotNull
    @Size(min = 1)
    private List<Long> storeIds;

    @ApiModelProperty(value = "客户信息")
    private CustomerDTO customer;
}
