package com.wanmi.sbc.customer.api.request.invoice;


import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 会员增票资质-作废Request
 */
@ApiModel
@Data
public class CustomerInvoiceInvalidRequest extends CustomerBaseRequest implements Serializable {


    private static final long serialVersionUID = -8317041886841339612L;
    /**
     * 专票ids
     */
    @ApiModelProperty(value = "专票ids")
    @NotNull
    private List<Long> customerInvoiceIds;


}
