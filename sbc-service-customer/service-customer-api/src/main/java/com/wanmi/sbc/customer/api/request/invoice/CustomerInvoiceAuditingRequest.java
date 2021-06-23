package com.wanmi.sbc.customer.api.request.invoice;

import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会员增票资质-审核Request
 */
@ApiModel
@Data
public class CustomerInvoiceAuditingRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 7872848146416119573L;

    @ApiModelProperty(value = "增票资质审核状态")
    private CheckState CheckState;

    @ApiModelProperty(value = "专票ids")
    private List<Long> customerInvoiceIds;

}
